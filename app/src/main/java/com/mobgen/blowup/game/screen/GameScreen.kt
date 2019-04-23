package com.mobgen.blowup.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mobgen.blowup.game.BlowUpGameImpl
import com.mobgen.blowup.game.entity.BackgroundEntity
import com.mobgen.blowup.game.entity.EntityFactory

class GameScreen(game: BlowUpGameImpl) : BaseScreen(game) {
    companion object {
        const val TAG = "GameScreen"
    }

    private val stage: Stage = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
    private var backgroundEntity: BackgroundEntity

    init {
        val entityFactory = EntityFactory(game.assetManager, game.localAssetManager)
        backgroundEntity = entityFactory.createBackground()
        backgroundEntity.moveToPositionWithoutAnimation(backgroundEntity.waterPosition)
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = stage
        stage.addActor(backgroundEntity)

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
    }

    fun goBack(onEnd: () -> Unit = {}) {
        //TODO
        onEnd()
    }
}