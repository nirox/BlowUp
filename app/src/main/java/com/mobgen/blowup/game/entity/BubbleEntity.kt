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
import java.util.*

class BubbleEntity(private val blowUpSound: Sound, private val explodeTexture: Texture, private val texture: Texture, listener: InputListener, private val onAutomaticBlowUp: (bubble: BubbleEntity) -> Unit) : Actor() {
    companion object {
        private const val BUBBLE_SIZE_WIDTH = 0.1f
        const val BUBBLE_SIZE_WIDTH_MAX = 0.2f
        const val INCREMENT_IN_EACH_FRAME = 0.06f
        const val MAX_ELAPSED_TIME = 2f
        private val random = Random()
        const val SIZE_PERCENT = 1.8f
        const val POSITION_PERCENT = 0.4f
        const val FRAME_COLS = 5
        const val FRAME_ROWS = 1
        const val FRAME_DURATION = 0.075f
        const val MAX_ANIMATION_BLOW_UP_TIME = 0.3f
    }

    private var elapsed = 0f
    var isPaused = false
        set(value) {
            field = value
            if (value) blowUpSound.stop()
        }
    var bubbleColor = Color()
    val possibleColors = mutableListOf<Color>(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
    private var loadAnimation: Animation<TextureRegion>

    init {
        isVisible = false
        touchable = Touchable.enabled
        setSize(Gdx.graphics.width * BUBBLE_SIZE_WIDTH, Gdx.graphics.width * BUBBLE_SIZE_WIDTH)
        addListener(listener)

        val animationFrames = Util.createTextureRegion(explodeTexture, FRAME_COLS, FRAME_ROWS, 1)

        loadAnimation = Animation(FRAME_DURATION, animationFrames, Animation.PlayMode.LOOP)
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
        batch.color = bubbleColor

        if (!isPaused) {
            elapsed += Gdx.graphics.deltaTime
            blowUpControl(batch)
            if (elapsed > MAX_ELAPSED_TIME) automaticBlowUp()
            if (isVisible) drawBubble(batch)
        } else {
            blowUpControl(batch)
        }
    }

    fun deatch() {
        texture.dispose()
        blowUpSound.dispose()
    }

    fun blowUp() {
        blowUpSound.play()
        elapsed = 0f
        touchable = Touchable.disabled
        setPosition(x - width * POSITION_PERCENT, y - width * POSITION_PERCENT)
        setSize(width * SIZE_PERCENT, height * SIZE_PERCENT)
    }

    fun finish() {
        isVisible = false
    }

    private fun automaticBlowUp() {
        if (isTouchable) {
            onAutomaticBlowUp(this)
        }
        blowUp()
    }

    private fun drawBubble(batch: Batch) {
        batch.color = bubbleColor
        setSize(width + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime, height + INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime)
        setPosition(x - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2, y - INCREMENT_IN_EACH_FRAME * Gdx.graphics.width * Gdx.graphics.deltaTime / 2)
    }

    private fun blowUpControl(batch: Batch) {
        if (!isTouchable) {
            batch.draw(loadAnimation.getKeyFrame(elapsed), x, y, width, height)
            if (elapsed > MAX_ANIMATION_BLOW_UP_TIME)
                finish()
        } else {
            batch.draw(texture, x, y, width, height)
        }
    }

    override fun equals(other: Any?): Boolean {
        return this.x == (other as Actor).x && this.y == other.y
    }

    override fun hashCode(): Int {
        return this.x.hashCode() + this.y.hashCode()
    }
}