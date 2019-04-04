package com.mobgen.blowup.game.screen

import com.badlogic.gdx.Screen
import com.mobgen.blowup.game.BlowUpGameImpl

abstract class BaseScreen(val game: BlowUpGameImpl) : Screen {

    override fun hide() {}

    override fun show() {}

    override fun render(delta: Float) {}

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {}

}