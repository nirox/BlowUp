package com.mobgen.blowup.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mobgen.blowup.game.BlowUpGameImpl
import com.mobgen.blowup.game.entity.BackgroundEntity
import com.mobgen.blowup.game.entity.ButtonFontEntity
import com.mobgen.blowup.game.entity.EntityFactory
import com.mobgen.blowup.game.entity.TitleEntity


class MainScreen(game: BlowUpGameImpl) : BaseScreen(game) {
    private val stage: Stage = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
    private val backgroundEntity: BackgroundEntity
    private lateinit var playButtonEntity: ButtonFontEntity
    private lateinit var scoreButtonEntity: ButtonFontEntity
    private val titleEntity: TitleEntity

    private var movementDirectionTop = false

    companion object {
        const val HEIGHT_SCORE_POSITION_PERCENT = 0.20f
        const val HEIGHT_PLAY_POSITION_PERCENT = 0.30f
        const val FONT_PLAY_SIZE = 90
        const val FONT_SCORE_SIZE = 50
        const val TAG = "MainScreen"
    }

    init {
        val entityFactory = EntityFactory(game.assetManager, game.localAssetManager)
        titleEntity = entityFactory.createTitleEntity()
        backgroundEntity = entityFactory.createBackground()
        playButtonEntity = entityFactory.createPlayButton(Gdx.graphics.height * HEIGHT_PLAY_POSITION_PERCENT, true, object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                playButtonEntity.goBack {
                    backgroundEntity.moveToPosition(
                            backgroundEntity.waterPosition,
                            onStartAnimation = {
                                movementDirectionTop = backgroundEntity.toTop()
                                titleEntity.setAnimationValues(backgroundMovement = true, toTop = movementDirectionTop)
                            },
                            onEndAnimation = {
                                titleEntity.setAnimationValues(backgroundMovement = false)
                                game.goGameScreen()
                            })
                }
                scoreButtonEntity.goBack()

                return super.touchDown(event, x, y, pointer, button)
            }
        })
        scoreButtonEntity = entityFactory.createScoreButton(Gdx.graphics.height * HEIGHT_SCORE_POSITION_PERCENT, true, object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                playButtonEntity.goBack {
                    backgroundEntity.moveToPosition(
                            backgroundEntity.cavePosition,
                            onStartAnimation = {
                                movementDirectionTop = backgroundEntity.toTop()
                                titleEntity.setAnimationValues(backgroundMovement = true, toTop = movementDirectionTop)
                            },
                            onEndAnimation = {
                                titleEntity.setAnimationValues(backgroundMovement = false)
                                game.goScoreScreen()
                            })
                }
                scoreButtonEntity.goBack()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
        stage.addActor(backgroundEntity)
        stage.addActor(titleEntity)
        stage.addActor(playButtonEntity)
        stage.addActor(scoreButtonEntity)

    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
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
        playButtonEntity.deatch()
        scoreButtonEntity.deatch()
        titleEntity.deatch()
    }

    fun goBack(onEnd: () -> Unit = {}) {
        backgroundEntity.moveToPosition(
                backgroundEntity.floorPosition,
                onStartAnimation = {
                    titleEntity.setAnimationValues(backgroundMovement = true, toTop = !movementDirectionTop)
                },
                onEndAnimation = {
                    titleEntity.setAnimationValues(backgroundMovement = false)
                    playButtonEntity.goBack()
                    scoreButtonEntity.goBack()
                    onEnd()
                }
        )
    }
}