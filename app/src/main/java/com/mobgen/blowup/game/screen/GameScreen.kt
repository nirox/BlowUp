package com.mobgen.blowup.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mobgen.blowup.game.BlowUpGameImpl
import com.mobgen.blowup.game.entity.*
import java.util.*

class GameScreen(game: BlowUpGameImpl, private val onEnd: () -> Unit = {}) : BaseScreen(game) {
    companion object {
        const val TAG = "GameScreen"
        const val SUCCESS_POINTS = 10
        const val FAIL_POINTS = -5
        const val BIG_FAIL_POINTS = -30
        const val FONTS_SIZE = 75
        const val POINTS_EACH_LEVEL = 100
        const val INITIAL_MAX_BUBBLE_SCREEN = 1
        const val INITIAL_SPAWN_BOMBS = 20
        const val INITIAL_SPAWN_TIME = 0.25f
        const val COUNTER_TIME = 0f
    }

    private val stage: Stage = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
    private var backgroundEntity: BackgroundEntity
    private var bubbles = arrayListOf<BubbleEntity>()
    private var bombs = arrayListOf<BombEntity>()

    private var counterTime: Float = COUNTER_TIME
    private var spawnTime: Float = INITIAL_SPAWN_TIME
    private var spawnBombs: Int = INITIAL_SPAWN_BOMBS
    private var maxBubbleColor: Int = INITIAL_MAX_BUBBLE_SCREEN
    private val random = Random()
    private var randomX: Float = 0f
    private var randomY: Float = 0f
    private var failed: Boolean = false
    private var changingLevel: Boolean = false
    private var entityFactory: EntityFactory = EntityFactory(game.assetManager, game.localAssetManager)
    private var targetEntity: TargetColorEntity
    private var bubbleButtons: TargetColorEntity
    private var gameBar: GameBarEntity
    private var timer: TimerEntity
    private var gameScoreEntity: GameScoreEntity
    private var levelTextEntity: LevelTextEntity
    private var getTextEntity: GetTextEntity
    private var pauseButton: PauseButtonEntity
    private lateinit var lastPointCreated: PointEntity
    private val resumeButtonEntity: ButtonFontEntity
    private val exitButtonEntity: ButtonFontEntity

    private var isPaused = false
    var count = 0
    private var totalBubblesCount = 0
    private var bombLocalization = Vector2(0f, 0f)
    var colorsAmount = mutableMapOf<Color, Int>()

    init {
        backgroundEntity = entityFactory.createBackground()
        backgroundEntity.moveToPositionWithoutAnimation(backgroundEntity.waterPosition)
        pauseButton = entityFactory.createPauseButton(
                listener = object : InputListener() {
                    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        pauseGame()
                        return super.touchDown(event, x, y, pointer, button)
                    }
                })
        resumeButtonEntity = entityFactory.createResumeButton(Gdx.graphics.height / 2f, true, object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                pauseGame()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        exitButtonEntity = entityFactory.createExitButton(Gdx.graphics.height / 2f, true, object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                onEnd()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        timer = entityFactory.createTimer()
        gameBar = entityFactory.createGameBar()
        targetEntity = entityFactory.createTarget()
        bubbleButtons = entityFactory.createTarget()
        gameScoreEntity = entityFactory.createGameScore()
        levelTextEntity = entityFactory.createLevelText()
        getTextEntity = entityFactory.createGetText()

    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
        stage.addActor(backgroundEntity)
        stage.addActor(timer)
        stage.addActor(gameBar)
        stage.addActor(targetEntity)
        stage.addActor(gameScoreEntity)
        stage.addActor(levelTextEntity)
        stage.addActor(getTextEntity)
        stage.addActor(pauseButton)
        counterTime = COUNTER_TIME
        levelTextEntity.level++
        spawnTime = INITIAL_SPAWN_TIME
        spawnBombs = INITIAL_SPAWN_BOMBS
        maxBubbleColor = INITIAL_MAX_BUBBLE_SCREEN
        counterTime = 0f
        timer.time = 0f
        bubbleButtons.changeColor(Color.BLUE)
        levelTextEntity.level = 0
        levelTextEntity.levelText = "Level "
        gameScoreEntity.pointsText = 0
        getTextEntity.points = 0
        prepareLevel()
        if (isPaused) pauseGame()
    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (!isPaused) {
            counterTime += delta

            if (!changingLevel) {
                timer.time += delta * Gdx.graphics.height * 0.066f

                if (timer.time >= Gdx.graphics.height) {
                    prepareLevel()
                } else {
                    timer.time += delta

                    if (counterTime >= spawnTime) {
                        randomX = random.nextInt(Gdx.graphics.width - (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH).toInt()).toFloat() + (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH).toInt() / 4
                        randomY = random.nextInt(Gdx.graphics.height - (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH).toInt() - (0.1f * Gdx.graphics.height.toFloat()).toInt()).toFloat() + (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH).toInt() / 3

                        if (totalBubblesCount >= spawnBombs) {
                            if (!bubbles.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BombEntity.BUBBLE_SIZE_WIDTH).toInt() }
                                    && !bombs.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BombEntity.BUBBLE_SIZE_WIDTH).toInt() }) {
                                val bomb = entityFactory.createBomb(
                                        listener = object : InputListener() {
                                            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                                                if (!isPaused) {
                                                    val bombTarget = (event?.target as BombEntity)
                                                    bombTarget.blowUp()
                                                    bombs.remove(bombTarget)
                                                }
                                                return super.touchDown(event, x, y, pointer, button)
                                            }
                                        },
                                        onAutomaticBlowUp = {
                                            failed = true
                                            gameScoreEntity.pointsText += BIG_FAIL_POINTS
                                            addPoint(it.x, it.y, it.width, it.height, Color.RED, BIG_FAIL_POINTS.toString())
                                            bombs.remove(it)
                                        })

                                bomb.setPosition(randomX, randomY)
                                counterTime = 0f
                                bombs.add(bomb)
                                stage.addActor(bomb)
                                bombLocalization = Vector2(bomb.x, bomb.y)
                                totalBubblesCount = 0
                            }
                        } else {
                            if (!bubbles.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH).toInt() }
                                    && !bombs.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BombEntity.BUBBLE_SIZE_WIDTH).toInt() }) {
                                val bubble = entityFactory.createBubble(
                                        listener = object : InputListener() {
                                            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                                                if (!isPaused) {
                                                    val bubbleTarget = (event?.target as BubbleEntity)
                                                    val pointText: String
                                                    val colorFont: Color
                                                    if (bubbleTarget.bubbleColor == targetEntity.bubbleColor) {
                                                        colorFont = Color.WHITE
                                                        gameScoreEntity.pointsText += SUCCESS_POINTS
                                                        pointText = "+$SUCCESS_POINTS"
                                                    } else {
                                                        failed = true
                                                        targetEntity.changeColor(bubbleTarget.bubbleColor)
                                                        colorFont = Color.RED
                                                        gameScoreEntity.pointsText += BIG_FAIL_POINTS
                                                        pointText = BIG_FAIL_POINTS.toString()
                                                    }

                                                    addPoint(bubbleTarget.x, bubbleTarget.y, bubbleTarget.width, bubbleTarget.height, colorFont, pointText)
                                                    bubbleTarget.blowUp()
                                                    count = colorsAmount[bubbleTarget.bubbleColor]
                                                            ?: 0
                                                    colorsAmount[bubbleTarget.bubbleColor] = count - 1
                                                    bubbles.remove(bubbleTarget)
                                                }
                                                return super.touchDown(event, x, y, pointer, button)
                                            }
                                        },
                                        onAutomaticBlowUp = {
                                            if (it.bubbleColor == targetEntity.bubbleColor) {
                                                failed = true
                                                gameScoreEntity.pointsText += FAIL_POINTS
                                                addPoint(it.x, it.y, it.width, it.height, Color.RED, FAIL_POINTS.toString())

                                            }
                                            count = colorsAmount[it.bubbleColor]
                                                    ?: 0
                                            colorsAmount[it.bubbleColor] = count - 1
                                            bubbles.remove(it)
                                        })

                                colorsAmount.forEach { (key, value) ->
                                    if (value > maxBubbleColor) bubble.possibleColors.remove(key)
                                }
                                bubble.getRandomColor()
                                count = colorsAmount[bubble.bubbleColor] ?: 0
                                colorsAmount[bubble.bubbleColor] = count + 1
                                bubble.setPosition(randomX, randomY)
                                counterTime = 0f
                                if (bubble.possibleColors.isNotEmpty()) {
                                    bubbles.add(bubble)
                                    stage.addActor(bubble)
                                    totalBubblesCount++
                                }
                            }
                        }
                    }
                }
            } else if (changingLevel && counterTime > 5) {
                changingLevel = false
                counterTime = 0f
                timer.time = 0f
                levelTextEntity.isVisible = false
            }

        }
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        super.hide()
        stage.clear()
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
        backgroundEntity.deatch()
        gameBar.deatch()
        targetEntity.deatch()
        gameScoreEntity.deatch()
        pauseButton.deatch()
        bubbles.forEach { it.deatch() }
    }

    fun goBack() {
        pauseGame()
    }

    private fun addPoint(axisX: Float, axisY: Float, width: Float, height: Float, color: Color, pointsText: String) {
        lastPointCreated = entityFactory.createPoints(color, pointsText, axisX, axisY, width, height)
        stage.addActor(lastPointCreated)
    }

    private fun prepareLevel() {
        bubbles.forEach {
            it.blowUp()
        }
        bombs.forEach {
            it.blowUp()
        }
        bubbles.clear()
        colorsAmount.clear()
        bombs.clear()
        changingLevel = true

        if (gameScoreEntity.pointsText >= levelTextEntity.level * POINTS_EACH_LEVEL) {
            targetEntity.changeColor(TargetColorEntity.getRandomColor())
            counterTime = 0f
            levelTextEntity.level++
            getTextEntity.points = levelTextEntity.level * 100
            spawnTime -= 0.02f
            spawnBombs -= 1
            maxBubbleColor += (levelTextEntity.level) / 6
            levelTextEntity.isVisible = true
        } else {
            bubbleButtons.changeColor(Color.RED)
            levelTextEntity.level = 0
            levelTextEntity.levelText = "Game Over"
            levelTextEntity.isVisible = true
            pauseGame()
        }

        levelTextEntity.isVisible = true
    }

    private fun pauseGame() {
        if (isPaused) {
            pauseButton.isVisible = true
            resumeButtonEntity.remove()
            exitButtonEntity.remove()
            bubbleButtons.remove()

            bubbles.forEach {
                it.isPaused = false
            }
            bombs.forEach {
                it.isPaused = false
            }
            isPaused = false
        } else {
            pauseButton.isVisible = false
            stage.addActor(bubbleButtons)
            bubbleButtons.height = Gdx.graphics.width / 1.5f
            bubbleButtons.width = Gdx.graphics.width / 1.5f
            bubbleButtons.x = Gdx.graphics.width / 2f - bubbleButtons.width / 2
            bubbleButtons.y = Gdx.graphics.height / 2 - bubbleButtons.width / 1.5f

            stage.addActor(exitButtonEntity)
            if (levelTextEntity.level != 0) {
                bubbleButtons.changeColor(Color.BLUE)
                stage.addActor(resumeButtonEntity)
                resumeButtonEntity.setPosition(resumeButtonEntity.x, bubbleButtons.y + (bubbleButtons.height/4)*3 - resumeButtonEntity.height)
                exitButtonEntity.setPosition(exitButtonEntity.x, bubbleButtons.y + bubbleButtons.height/4)
            } else {
                var scoreTemp = gameScoreEntity.pointsText
                gameScoreEntity = entityFactory.createGameScore()
                gameScoreEntity.colorText = Color(1f, 1f, 1f, 1f)
                stage.addActor(gameScoreEntity)
                gameScoreEntity.pointsText = scoreTemp
                gameScoreEntity.setPosition(gameScoreEntity.x, bubbleButtons.y + (bubbleButtons.height/4)*3 + gameScoreEntity.height/2)
                exitButtonEntity.setPosition(exitButtonEntity.x, bubbleButtons.y + bubbleButtons.height/4 - exitButtonEntity.height/2)
                bubbleButtons.changeColor(Color.RED)
            }

            bubbles.forEach {
                it.isPaused = true
            }
            bombs.forEach {
                it.isPaused = true
            }
            isPaused = true
        }
    }
}