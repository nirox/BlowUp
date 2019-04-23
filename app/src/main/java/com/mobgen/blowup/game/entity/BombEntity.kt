package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import java.util.*

class BombEntity(private val texture: Texture, listener: InputListener, private val onAutomaticBlowUp: (bubble: BombEntity) -> Unit) : Actor() {
    companion object {
        const val BUBBLE_SIZE_WIDTH = 0.20f
        const val INIT_ELAPSED_TIME = 0.5f
        const val MAX_ELAPSED_TIME = 2f + INIT_ELAPSED_TIME
    }

    private var elapsed = INIT_ELAPSED_TIME
    var isPaused = false
    var axisX = 0
    var axisY = 0

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

    override fun draw(batch: Batch, parentAlpha: Float) {
        if (!isPaused) {
            elapsed += Gdx.graphics.deltaTime

            if (elapsed > MAX_ELAPSED_TIME) {
                automaticBlowUp()
            }
            if (isVisible) {
                batch.color = Color.WHITE
                setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH * (elapsed / MAX_ELAPSED_TIME), Gdx.graphics.width * BUBBLE_SIZE_WIDTH * (elapsed / MAX_ELAPSED_TIME))
                setPosition(x - (elapsed / MAX_ELAPSED_TIME), y - (elapsed / MAX_ELAPSED_TIME))
                batch.draw(texture, x, y, width, height)
            }
        } else {
            batch.color = Color.WHITE
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