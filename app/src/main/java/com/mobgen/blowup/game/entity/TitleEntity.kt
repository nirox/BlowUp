package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

class TitleEntity(private val bubbleTexture: Texture, private val bitmapFontRent: BitmapFont, private val bitmapFontMarkerBig: BitmapFont) : Actor() {

    companion object {
        const val BIG_FONT_SIZE = 66
        const val EXCLAMATION_FONT_SIZE = 64
        private const val HEIGHT_PERCENT = 0.5f
        private const val MIN_WIDTH_BUBBLE_PERCENT = 0.6f
        private const val MAX_WIDTH_BUBBLE_PERCENT = 0.65f
        private const val ANIMATION_SPEED_PERCENT = 0.1f

        private const val APP_NAME = "Blow Up"
        private const val EXCLAMATION = "!"
    }

    private val bubbleUpGyphLayout = GlyphLayout()
    private val exclamationGyphLayout = GlyphLayout()

    private var movementDirection = +1
    private val animationSpeed = ANIMATION_SPEED_PERCENT * Gdx.graphics.width
    private val sizeBubble = Vector2(Gdx.graphics.width.toFloat() * MIN_WIDTH_BUBBLE_PERCENT, MIN_WIDTH_BUBBLE_PERCENT * Gdx.graphics.width * bubbleTexture.height / bubbleTexture.width.toFloat())
    private var heightVariation = 0f
    private var directionVariation = -1
    private var backgroundMovement = false
    private var toTop = false

    init {
        bubbleUpGyphLayout.setText(bitmapFontRent, APP_NAME)
        exclamationGyphLayout.setText(bitmapFontMarkerBig, EXCLAMATION)
        setSize(sizeBubble.x, sizeBubble.y)
        setPosition(Gdx.graphics.width / 2f - width / 2f, Gdx.graphics.height * HEIGHT_PERCENT)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        heightVariation = if (backgroundMovement) BackgroundEntity.ANIMATION_SPEED_PERCENT * Gdx.graphics.width * Gdx.graphics.deltaTime else 0f
        directionVariation = if (toTop) 1 else -1

        when {
            width <= MIN_WIDTH_BUBBLE_PERCENT * Gdx.graphics.width -> movementDirection = +1
            width >= MAX_WIDTH_BUBBLE_PERCENT * Gdx.graphics.width -> movementDirection = -1
        }

        setSize(width + animationSpeed * Gdx.graphics.deltaTime * movementDirection, height + animationSpeed * Gdx.graphics.deltaTime * movementDirection)
        setPosition(x + (animationSpeed * Gdx.graphics.deltaTime) / 2 * -1 * movementDirection, y + (heightVariation * directionVariation) + (animationSpeed * Gdx.graphics.deltaTime) / 2 * -1 * movementDirection)
        batch?.color = Color.WHITE
        batch?.let {
            batch.draw(bubbleTexture, x, y, width, height)
            bitmapFontRent.draw(it, APP_NAME, x + width / 2f - bubbleUpGyphLayout.width / 2f, y + height / 2 + bubbleUpGyphLayout.height / 2f)
            bitmapFontMarkerBig.draw(it, EXCLAMATION, x + width / 2f - bubbleUpGyphLayout.width / 2f + bubbleUpGyphLayout.width, y + height / 2 + bubbleUpGyphLayout.height / 2)
        }

    }

    fun setAnimationValues(backgroundMovement: Boolean, toTop: Boolean = false) {
        this.backgroundMovement = backgroundMovement
        this.toTop = if (!backgroundMovement) !toTop else toTop
    }

    fun deatch() {
        bitmapFontRent.dispose()
        bitmapFontMarkerBig.dispose()
    }

}