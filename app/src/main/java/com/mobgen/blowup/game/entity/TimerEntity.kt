package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

class TimerEntity(private val texture: Texture) : Actor() {
    var time = 0f

    init {
        setSize(Gdx.graphics.width*0.03f, 0f)
        setPosition(0f, 0f)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = Color(1f,0f,0f,1f)

        batch.draw(texture, x, y, width, time)
    }

    fun deatch() {
        texture.dispose()
    }
}