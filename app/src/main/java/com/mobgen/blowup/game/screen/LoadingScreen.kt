package com.mobgen.blowup.game.screen

import android.util.Log
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mobgen.blowup.game.BlowUpGameImpl
import com.mobgen.blowup.game.entity.LoadEntity

class LoadingScreen(game: BlowUpGameImpl) : BaseScreen(game) {
    private val stage: Stage = Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
    private val loadEntity: LoadEntity = LoadEntity()


    override fun show() {
        super.show()
        stage.addActor(loadEntity)
    }

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(0f, 0f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Log.v("mio", game.checkLoadGameScreen().toString())
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