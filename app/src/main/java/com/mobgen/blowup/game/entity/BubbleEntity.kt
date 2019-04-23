package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import java.util.*

class BubbleEntity(private val texture: Texture, listener: InputListener, private val onAutomaticBlowUp: (bubble: BubbleEntity) -> Unit) : Actor() {
    companion object {
        const val BUBBLE_SIZE_WIDTH = 0.20f
        const val INIT_ELAPSED_TIME = 0.5f
        const val MAX_ELAPSED_TIME = 2f + INIT_ELAPSED_TIME
        private val random = Random()
    }

    private var elapsed = INIT_ELAPSED_TIME
    var isPaused = false
    var bubbleColor = Color()
    val possibleColors = mutableListOf<Color>(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

    init {
        isVisible = false
        touchable = Touchable.enabled
        setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH, Gdx.graphics.width * BUBBLE_SIZE_WIDTH)
        addListener(listener)
    }

    override fun setPosition(x: Float, y: Float) {
        isVisible = true
        super.setPosition(x, y)
    }

    fun getRandomColor() {
        if (possibleColors.isNotEmpty())
            bubbleColor = possibleColors[random.nextInt(possibleColors.size)]
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (!isPaused) {
            elapsed += Gdx.graphics.deltaTime

            if (elapsed > MAX_ELAPSED_TIME) {
                automaticBlowUp()
            }
            if (isVisible) {
                batch.color = bubbleColor
                setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH * (elapsed / MAX_ELAPSED_TIME), Gdx.graphics.width * BUBBLE_SIZE_WIDTH * (elapsed / MAX_ELAPSED_TIME))
                setPosition(x - (elapsed / MAX_ELAPSED_TIME), y - (elapsed / MAX_ELAPSED_TIME))
                batch.draw(texture, x, y, width, height)
            }
        } else {
            batch.color = bubbleColor
            batch.draw(texture, x, y, width, height)
        }


    }

    fun deatch() {
        texture.dispose()
    }

    fun blowUp() {
        isVisible = false
    }

    private fun automaticBlowUp() {
        if (isVisible) onAutomaticBlowUp(this)
        blowUp()
    }

    override fun equals(other: Any?): Boolean {
        return this.x == (other as Actor).x && this.y == other.y
    }

    override fun hashCode(): Int {
        return this.x.hashCode() + this.y.hashCode()
    }
}