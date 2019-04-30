package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.mobgen.blowup.game.Util


class BombEntity(private val bombFuseSound: Sound, private val bombExplotionSound: Sound, private val texture: Texture, private val explodeTexture: Texture, private val explotionTexture: Texture, listener: InputListener, private val onAutomaticBlowUp: (bomb: BombEntity) -> Unit) : Actor() {
    companion object {
        private const val BUBBLE_SIZE_WIDTH = 0.1f
        const val BUBBLE_SIZE_WIDTH_MAX = 0.2f
        const val INCREMENT_IN_EACH_FRAME = 0.4f
        const val MAX_ELAPSED_TIME = 2f
        const val DECREASE_TIME_MAX_PERCENT = 0.25f
        const val COLOR_TIME_MIN = 0.1f
        const val SIZE_PERCENT = 1.8f
        const val POSITION_PERCENT = 0.53f
        const val FRAME_COLS = 5
        const val FRAME_ROWS = 3
        const val FRAME_DURATION = 0.07f
        const val MAX_ANIMATION_EXPLOSION_TIME = 0.77f
    }

    private var elapsed = 0f
    private var changeColorTimeMax = 0.4f
    private var changeColorTime = 0f
    private var bombFuseId = 0L
    private var loadAnimation: Animation<TextureRegion>
    var isPaused = false
        set(value) {
            field = value
            if (value) {
                bombFuseSound.stop()
                bombExplotionSound.stop()
            }
        }

    init {
        isVisible = false
        touchable = Touchable.enabled
        setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH, Gdx.graphics.width * BUBBLE_SIZE_WIDTH)
        addListener(listener)

        val animationFrames = Util.createTextureRegion(explotionTexture, FRAME_COLS, FRAME_ROWS, 1)

        loadAnimation = Animation(FRAME_DURATION, animationFrames, Animation.PlayMode.LOOP)
    }

    override fun setPosition(x: Float, y: Float) {
        isVisible = true
        super.setPosition(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = Color.WHITE
        if (!isPaused) {
            bombControl()
            if (isVisible) {
                if (width < Gdx.graphics.width * BUBBLE_SIZE_WIDTH_MAX) {
                    setSize(width + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime, height + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime)
                    setPosition(x - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2, y - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2)
                }
                if (!isTouchable) drawExplosion(batch) else drawBlink(batch)

            }
        } else {
            if (!isTouchable) drawExplosion(batch) else batch.draw(texture, x, y, width, height)
        }
    }

    private fun bombControl() {
        if (elapsed == 0f && isTouchable) bombFuseId = bombFuseSound.play()
        elapsed += Gdx.graphics.deltaTime
        changeColorTime += Gdx.graphics.deltaTime

        if (elapsed > MAX_ELAPSED_TIME) {
            automaticBlowUp()
        }
    }

    private fun drawBlink(batch: Batch) {
        if (changeColorTime >= changeColorTimeMax) {
            batch.draw(explodeTexture, x, y, width, height)
            changeColorTime = 0f
            if (changeColorTimeMax >= COLOR_TIME_MIN) changeColorTimeMax -= changeColorTimeMax * DECREASE_TIME_MAX_PERCENT
        } else batch.draw(texture, x, y, width, height)
    }

    private fun drawExplosion(batch: Batch) {
        batch.draw(loadAnimation.getKeyFrame(elapsed), x, y, width, height)
        if (elapsed > MAX_ANIMATION_EXPLOSION_TIME)
            finish()
    }

    fun deatch() {
        texture.dispose()
        bombExplotionSound.dispose()
        bombFuseSound.dispose()
    }

    fun blowUp() {
        elapsed = 0f
        touchable = Touchable.disabled
        bombFuseSound.stop(bombFuseId)
        bombExplotionSound.play()
        setPosition(x - width * POSITION_PERCENT, y - width * POSITION_PERCENT)
        setSize(width * SIZE_PERCENT, height * SIZE_PERCENT)
    }

    fun finish() {
        isVisible = false
        touchable = Touchable.disabled
        bombFuseSound.stop(bombFuseId)
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