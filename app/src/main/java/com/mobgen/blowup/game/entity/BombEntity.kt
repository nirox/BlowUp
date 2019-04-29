package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import java.util.*

class BombEntity(private val texture: Texture, private val explodeTexture: Texture, listener: InputListener, private val onAutomaticBlowUp: (bomb: BombEntity) -> Unit) : Actor() {
    companion object {
        private const val BUBBLE_SIZE_WIDTH = 0.1f
        const val BUBBLE_SIZE_WIDTH_MAX = 0.2f
        const val INCREMENT_IN_EACH_FRAME = 0.4f
        const val MAX_ELAPSED_TIME = 2f
        const val DECREASE_TIME_MAX_PERCENT = 0.25f
        const val COLOR_TIME_MIN = 0.1f
    }
    private var elapsed = 0f
    private var changeColorTimeMax = 0.4f
    private var changeColorTime = 0f
    var isPaused = false

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
            changeColorTime += Gdx.graphics.deltaTime
            if (elapsed > MAX_ELAPSED_TIME) {
                automaticBlowUp()
            }
            if (isVisible) {
                batch.color = Color.WHITE
                if (width < Gdx.graphics.width * BUBBLE_SIZE_WIDTH_MAX) {
                    setSize(width + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime, height + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime)
                    setPosition(x - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2, y - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2)
                }
                if (changeColorTime >=  changeColorTimeMax){
                    batch.draw(explodeTexture, x, y, width, height)
                    changeColorTime = 0f
                    if (changeColorTimeMax >= COLOR_TIME_MIN) changeColorTimeMax -= changeColorTimeMax * DECREASE_TIME_MAX_PERCENT
                } else
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