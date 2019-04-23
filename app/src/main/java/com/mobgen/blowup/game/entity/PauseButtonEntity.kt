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
    }

    init {
        touchable = Touchable.enabled
        setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH, Gdx.graphics.width * BUBBLE_SIZE_WIDTH)
        setPosition(Gdx.graphics.width - width*2.1f, Gdx.graphics.height - height/1.05f )
        addListener(listener)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }

    fun deatch() {
        texture.dispose()
    }
}