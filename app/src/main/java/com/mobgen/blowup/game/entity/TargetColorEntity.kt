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
    }

    var bubbleColor = getRandomColor()
    private var targetPositionX = Gdx.graphics.width / 2 - width * 2.5f
    private var targetPositionY = Gdx.graphics.height - height * 1.1f
    private var targetSize = (Gdx.graphics.width / 8).toFloat()

    fun changeColor(recievedColor: Color) {
        bubbleColor = recievedColor
    }

    init {
        targetSize = (Gdx.graphics.width / 8).toFloat()
        setSize(targetSize, targetSize)
        targetPositionX = Gdx.graphics.width / 2 - width * 2.5f
        targetPositionY = Gdx.graphics.height - height * 1.1f
        setPosition(targetPositionX, targetPositionY)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = bubbleColor
        batch.draw(texture, x, y, width, height)
    }

    fun deatch() {
        texture.dispose()
    }

}