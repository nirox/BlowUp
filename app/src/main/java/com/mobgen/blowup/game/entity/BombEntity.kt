package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable

class BombEntity(private val bombFuseSound: Sound, private val bombExplotionSound: Sound, private val texture: Texture, listener: InputListener, private val onAutomaticBlowUp: (bubble: BombEntity) -> Unit) : Actor() {
    companion object {
        private const val BUBBLE_SIZE_WIDTH = 0.1f
        const val BUBBLE_SIZE_WIDTH_MAX = 0.2f
        const val INCREMENT_IN_EACH_FRAME = 0.06f
        const val MAX_ELAPSED_TIME = 2f
    }

    private var elapsed = 0f
    private var bombFuseId = 0L
    var isPaused = false
        set(value) {
            field = value
            if (!value) {
                bombFuseSound.stop()
                bombExplotionSound.stop()
            }
        }

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
            if (elapsed == 0f && isVisible)
                bombFuseId = bombFuseSound.play()
            elapsed += Gdx.graphics.deltaTime

            if (elapsed > MAX_ELAPSED_TIME) {
                bombExplotionSound.play()
                automaticBlowUp()
            }
            if (isVisible) {
                batch.color = Color.WHITE
                setSize(width + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime, height + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime)
                setPosition(x - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2, y - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2)
                batch.draw(texture, x, y, width, height)
            }
        } else {
            batch.color = Color.WHITE
            batch.draw(texture, x, y, width, height)
        }


    }

    fun deatch() {
        texture.dispose()
        bombExplotionSound.dispose()
        bombFuseSound.dispose()
    }

    fun blowUp() {
        bombFuseSound.stop(bombFuseId)
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