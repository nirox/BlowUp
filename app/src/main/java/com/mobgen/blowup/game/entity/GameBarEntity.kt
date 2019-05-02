package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

class GameBarEntity(private val texture: Texture) : Actor() {
    companion object {
        private const val HEIGHT_PERCENT = 0.1f
    }

    private val size = Vector2((texture.width * HEIGHT_PERCENT * Gdx.graphics.height) / texture.height.toFloat(), HEIGHT_PERCENT * Gdx.graphics.height.toFloat())

    init {
        setSize(size.x, size.y)
        setPosition(Gdx.graphics.width / 2 - width / 2, Gdx.graphics.height - height)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = Color.WHITE
        batch.draw(texture, x, y, width, height)
    }

    fun deatch() {
        texture.dispose()
    }
}