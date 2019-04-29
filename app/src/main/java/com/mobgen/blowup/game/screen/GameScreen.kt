package com.mobgen.blowup.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mobgen.blowup.game.BlowUpGameImpl
import com.mobgen.blowup.game.SoundFactory
import com.mobgen.blowup.game.entity.*
import com.mobgen.blowup.game.util.Constant
import java.util.*

class GameScreen(game: BlowUpGameImpl, private val onEnd: () -> Unit = {}) : BaseScreen(game) {
    companion object {
        const val TAG = "GameScreen"
        const val FONT_SIZE = 75
        const val FONT_SIZE_INPUT = 56
        private const val SUCCESS_POINTS = 10
        private const val COUNTER_TIME = 0f
        private const val FAIL_POINTS = -5
        private const val BIG_FAIL_POINTS = -30
        private const val POINTS_EACH_LEVEL = 150
        private const val INITIAL_MAX_BUBBLE_SCREEN = 1
        private const val INITIAL_SPAWN_TIME = 0.25f
        private const val INITIAL_SPAWN_BOMB_EACH_BUBBLE = 4
        private const val MIN_SPAWN_BOMB_EACH_BUBBLE = 1
        private const val SPAWN_TIME_DECREMENT_EACH_LEVEL = 0.02f
        private const val MAX_BOMB_EACH_BUBBLE_DECREMENT_EACH_LEVEL = 4
        private const val MAX_BUBBLE_SCREEN_INCREMENT_EACH_X_LEVEL = 6
        private const val DECREMENT_BOMB_EACH_X_LEVEL = 1
        private const val INCREMENT_BUBBLE_EACH_X_LEVEL = 1
        private const val BUBBLE_PAUSE_WIDTH_PERCENT = 1.5f
        private const val MAX_TIME = 0.066f
        private const val WIDTH_MARGIN_INPUT = 0.06f
        private const val TIME_BETWEEN_LEVEL = 2
    }

    private val stage: Stage = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
    private var backgroundEntity: BackgroundEntity
    private var bubbles = arrayListOf<BubbleEntity>()
    private var bombs = arrayListOf<BombEntity>()
    private var counterTime: Float = COUNTER_TIME
    private var spawnTime: Float = INITIAL_SPAWN_TIME
    private var spawnBombs: Int = INITIAL_SPAWN_BOMB_EACH_BUBBLE
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
    private val resumeButtonEntity: ButtonFontEntity
    private val exitButtonEntity: ButtonFontEntity
    private var isPaused = false
    private var count = 0
    private var currentTargetBubblesCount = 0
    private var bombLocalization = Vector2(0f, 0f)
    private var colorsAmount = mutableMapOf<Color, Int>()
    private lateinit var lastPointCreated: PointEntity
    private val fielTextInput = TextField("", entityFactory.createTextStyleDefault())
    private lateinit var gameOverSound: Music

    init {
        backgroundEntity = entityFactory.createBackground()
        backgroundEntity.moveToPositionWithoutAnimation(backgroundEntity.waterPosition)
        timer = entityFactory.createTimer()
        gameBar = entityFactory.createGameBar()
        targetEntity = entityFactory.createTarget()
        bubbleButtons = entityFactory.createTarget()
        gameScoreEntity = entityFactory.createGameScore()
        levelTextEntity = entityFactory.createLevelText()
        getTextEntity = entityFactory.createGetText()
        pauseButton = entityFactory.createPauseButton(
                listener = object : InputListener() {
                    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        controlPauseGame()
                        return super.touchDown(event, x, y, pointer, button)
                    }
                })
        resumeButtonEntity = entityFactory.createResumeButton(Gdx.graphics.height / 2f, true, object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                controlPauseGame()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        exitButtonEntity = entityFactory.createExitButton(Gdx.graphics.height / 2f, true, object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (levelTextEntity.level == 0) game.saveScore(if (fielTextInput.text.isNullOrBlank()) Constant.Strings.DefaultPlayerName.sName else fielTextInput.text, gameScoreEntity.pointsText)
                game.backgroundMusic.play()
                onEnd()
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        exitButtonEntity.moveWithoutAnimation()
        resumeButtonEntity.moveWithoutAnimation()

    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
        gameOverSound = SoundFactory(game.assetManager).getGameOver()
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
        spawnBombs = INITIAL_SPAWN_BOMB_EACH_BUBBLE
        maxBubbleColor = INITIAL_MAX_BUBBLE_SCREEN
        counterTime = 0f
        timer.time = 0f
        bubbleButtons.changeColor(Color.BLUE)
        levelTextEntity.level = 0
        levelTextEntity.levelText = Constant.Strings.Level.sName
        gameScoreEntity.apply {
            pointsText = 0
            colorText = Constant.getColor(Constant.Color.Brown)
            setPosition(initPosition.x, initPosition.y)
        }
        getTextEntity.points = 0
        prepareLevel()
        if (isPaused) controlPauseGame()
    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (!isPaused) {
            counterTime += delta

            if (!changingLevel) {
                timer.time += delta * Gdx.graphics.height * MAX_TIME

                if (timer.time >= Gdx.graphics.height) {
                    prepareLevel()
                } else {
                    timer.time += delta

                    if (counterTime >= spawnTime) {
                        randomX = random.nextInt(Gdx.graphics.width - (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH_MAX).toInt()).toFloat() + (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() / 2
                        randomY = random.nextInt(Gdx.graphics.height - (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() - (0.1f * Gdx.graphics.height.toFloat()).toInt()).toFloat() + (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() / 2

                        if (currentTargetBubblesCount > spawnBombs) {
                            createBomb()
                        } else {
                            createBubble()
                        }
                    }
                }
            } else if (changingLevel && counterTime >= TIME_BETWEEN_LEVEL) {
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
        gameOverSound.dispose()
        backgroundEntity.deatch()
        gameBar.deatch()
        targetEntity.deatch()
        gameScoreEntity.deatch()
        pauseButton.deatch()
        bubbles.forEach { it.deatch() }
    }

    fun goBack() {
        controlPauseGame()
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
            gameScoreEntity.minPoints = levelTextEntity.level * POINTS_EACH_LEVEL
            levelTextEntity.level++
            getTextEntity.points = levelTextEntity.level * POINTS_EACH_LEVEL
            spawnTime -= SPAWN_TIME_DECREMENT_EACH_LEVEL
            if (spawnBombs >= MIN_SPAWN_BOMB_EACH_BUBBLE && levelTextEntity.level % MAX_BOMB_EACH_BUBBLE_DECREMENT_EACH_LEVEL == 0) spawnBombs -=  DECREMENT_BOMB_EACH_X_LEVEL
            if(levelTextEntity.level % MAX_BUBBLE_SCREEN_INCREMENT_EACH_X_LEVEL == 0)maxBubbleColor += INCREMENT_BUBBLE_EACH_X_LEVEL
            levelTextEntity.isVisible = true
        } else {
            bubbleButtons.changeColor(Color.RED)
            levelTextEntity.level = 0
            levelTextEntity.levelText = Constant.Strings.GameOver.sName
            levelTextEntity.isVisible = true
            controlPauseGame()
        }

        levelTextEntity.isVisible = true
    }

    private fun controlPauseGame() {
        if (isPaused) {
            game.backgroundMusic.play()
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
            createPauseMenu()
            pauseEntitiesAndSounds()
            isPaused = true
        }
    }

    private fun pauseEntitiesAndSounds() {
        game.backgroundMusic.pause()
        bubbles.forEach {
            it.isPaused = true
        }
        bombs.forEach {
            it.isPaused = true
        }
    }

    private fun createBomb() {
        if (!bubbles.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() }
                && !bombs.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BombEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() }) {
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
            currentTargetBubblesCount = 0
        }
    }

    private fun createBubble() {
        if (!bubbles.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BubbleEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() }
                && !bombs.any { Vector2(it.x, it.y).dst(randomX, randomY) < (Gdx.graphics.width * BombEntity.BUBBLE_SIZE_WIDTH_MAX).toInt() }) {
            val bubble = entityFactory.createBubble(
                    listener = object : InputListener() {
                        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                            onTouchBubble(event?.target)
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
                if (bubble.bubbleColor == targetEntity.bubbleColor)
                    currentTargetBubblesCount++
            }
        }
    }

    private fun onTouchBubble(target: Actor?) {
        if (!isPaused) {
            val bubbleTarget = (target as BubbleEntity)
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
    }

    private fun createPauseMenu() {
        pauseButton.isVisible = false
        stage.addActor(bubbleButtons)
        bubbleButtons.height = Gdx.graphics.width / BUBBLE_PAUSE_WIDTH_PERCENT
        bubbleButtons.width = Gdx.graphics.width / BUBBLE_PAUSE_WIDTH_PERCENT
        bubbleButtons.x = Gdx.graphics.width / 2 - bubbleButtons.width / 2
        bubbleButtons.y = Gdx.graphics.height / 2 - bubbleButtons.width / BUBBLE_PAUSE_WIDTH_PERCENT

        stage.addActor(exitButtonEntity)
        if (levelTextEntity.level != 0) {
            bubbleButtons.changeColor(Color.BLUE)
            stage.addActor(resumeButtonEntity)
            resumeButtonEntity.setPosition(resumeButtonEntity.x, bubbleButtons.y + (bubbleButtons.height / 4) * 3 - resumeButtonEntity.height)
            exitButtonEntity.setPosition(exitButtonEntity.x, bubbleButtons.y + bubbleButtons.height / 4)
        } else {
            pauseEntitiesAndSounds()
            game.backgroundMusic.stop()
            gameOverSound.play()
            val scoreTemp = gameScoreEntity.pointsText
            gameScoreEntity = entityFactory.createGameScore()
            gameScoreEntity.colorText = Color.WHITE
            stage.addActor(gameScoreEntity)
            gameScoreEntity.pointsText = scoreTemp
            gameScoreEntity.setPosition(gameScoreEntity.x, bubbleButtons.y + (bubbleButtons.height / 4) * 3 + gameScoreEntity.height / 2)
            exitButtonEntity.setPosition(exitButtonEntity.x, bubbleButtons.y + bubbleButtons.height / 4 - exitButtonEntity.height / 2)
            bubbleButtons.changeColor(Color.RED)
            stage.addActor(fielTextInput.apply {
                setPosition(bubbleButtons.x + Gdx.graphics.width * WIDTH_MARGIN_INPUT / 2, gameScoreEntity.y - 2 * height)
                width = bubbleButtons.width - Gdx.graphics.width * WIDTH_MARGIN_INPUT

            })
        }

    }
}