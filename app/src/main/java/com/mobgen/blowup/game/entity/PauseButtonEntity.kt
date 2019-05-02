package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable

class PauseButtonEntity(private val texture: Texture, listener: InputListener) : Actor() {
    companion object {
        const val BUBBLE_SIZE_WIDTH = 0.15f
        private const val HEIGHT_MARGIN_PERCENT = 1.05f
        private const val WIDTH_MARGIN_PERCENT = 2.1f
    }

    init {
        touchable = Touchable.enabled
        setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH, Gdx.graphics.width * BUBBLE_SIZE_WIDTH)
        setPosition(Gdx.graphics.width - width * WIDTH_MARGIN_PERCENT, Gdx.graphics.height - height / HEIGHT_MARGIN_PERCENT)
        addListener(listener)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }

    fun deatch() {
        texture.dispose()
    }
}