package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener

class ButtonFontEntity(positionLeft: Boolean, heighPosition: Float, private val word: String, private val bitmapFont: BitmapFont, visibility: Boolean, listener: InputListener) : Actor() {

    private val nameGyphLayout = GlyphLayout().apply { setText(bitmapFont, word) }
    private val size = Vector2(nameGyphLayout.width, nameGyphLayout.height)
    private val initPosition = if (positionLeft) -size.x else Gdx.graphics.width.toFloat()
    private val defaultPosition = Gdx.graphics.width.toFloat() / 2 - size.x / 2
    private var goTo = defaultPosition
    private var positionDirection = 0f
    private var positionVariation = 0f
    private var justAnimateListener: () -> Unit = {}
    private val animationSpeed = ANIMATION_SPEED_PERCENT * Gdx.graphics.width

    companion object {
        private const val ANIMATION_SPEED_PERCENT = 0.8f
        private const val CLOSE_ANIMATION_END_PERCENT = 0.02f
    }

    init {
        isVisible = visibility
        setPosition(initPosition, heighPosition)
        setSize(size.x, size.y)
        addListener(listener)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        positionVariation = goTo - x
        positionDirection = if (Math.abs(positionVariation) >= CLOSE_ANIMATION_END_PERCENT * Gdx.graphics.height) {
            positionVariation / Math.abs(positionVariation)
        } else {
            justAnimateListener()
            setPosition(goTo, y)
            0f
        }

        setPosition(x + (positionDirection * animationSpeed * Gdx.graphics.deltaTime), y)
        batch?.color = Color.WHITE
        batch?.let {
            bitmapFont.draw(it, word, x, y + height)
        }

    }

    fun goBack(onJustAnimate: () -> Unit = {}) {
        if (x == goTo) {
            justAnimateListener = onJustAnimate
            goTo = if (goTo != initPosition) initPosition else defaultPosition
        }

    }

    fun deatch() {
        bitmapFont.dispose()
    }

}