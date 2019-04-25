package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import java.util.*

class TargetColorEntity(private val texture: Texture) : Actor() {
    companion object {
        private val random = Random()
        private val possibleColors = listOf<Color>(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        fun getRandomColor() = possibleColors[random.nextInt(possibleColors.size)]
        private const val HEIGHT_MARGIN_PERCENT = 1.1f
        private const val WIDTH_MARGIN_PERCENT = 2.5f
    }

    var bubbleColor = getRandomColor()

    fun changeColor(recievedColor: Color) {
        bubbleColor = recievedColor
    }

    init {
        setSize((Gdx.graphics.width / 8).toFloat(), (Gdx.graphics.width / 8).toFloat())
        setPosition(Gdx.graphics.width / 2 - width * WIDTH_MARGIN_PERCENT, Gdx.graphics.height - height * HEIGHT_MARGIN_PERCENT)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = bubbleColor
        batch.draw(texture, x, y, width, height)
    }

    fun deatch() {
        texture.dispose()
    }

}