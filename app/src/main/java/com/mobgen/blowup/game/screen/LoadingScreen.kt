package com.mobgen.blowup.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mobgen.blowup.game.BlowUpGameImpl

class LoadingScreen(game: BlowUpGameImpl) : BaseScreen(game) {
    private val stage: Stage = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if (game.checkLoadGameScreen()) {
            stage.addAction(game.loadGameScreen())
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
    }


}