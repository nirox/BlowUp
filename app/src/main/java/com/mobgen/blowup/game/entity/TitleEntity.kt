package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class TitleEntity(private val bitmapFontRentSmall: BitmapFont, private val bitmapFontRentBig: BitmapFont, private val bitmapFontMarkerBig: BitmapFont) : Actor() {

    companion object {
        const val SMALL_FONT_SIZE = 40
        const val BIG_FONT_SIZE = 97
        const val EXCLAMATION_FONT_SIZE = 80
        private const val MARGIN_ELEMENT_FONT_PERCENT = 0.02f
        private const val MARGIN_TITLE_PERCENT = 0.04f
        private const val HEIGHT_PERCENT = 0.6f
        private const val ANIMATION_VERTICAL_SPEED_PERCENT = 0.2f
        private const val ANIMATION_HORIZONTAL_SPEED_PERCENT = 0.3f
    }

    private val theGyphLayout = GlyphLayout()
    private val bubbleGyphLayout = GlyphLayout()
    private val explodeGyphLayout = GlyphLayout()
    private val exclamationGyphLayout = GlyphLayout()

    private var leftAnimated = true
    private val animationVerticalSpeed = ANIMATION_VERTICAL_SPEED_PERCENT * Gdx.graphics.height
    private val animationHorizontalSpeed = ANIMATION_HORIZONTAL_SPEED_PERCENT * Gdx.graphics.width

    init {
        theGyphLayout.setText(bitmapFontRentSmall, "the")
        bubbleGyphLayout.setText(bitmapFontRentBig, "bubble")
        explodeGyphLayout.setText(bitmapFontRentBig, "Explode")
        exclamationGyphLayout.setText(bitmapFontMarkerBig, "!")
        setSize(explodeGyphLayout.width, explodeGyphLayout.height + bubbleGyphLayout.height)
        setPosition(Gdx.graphics.width / 2f - width / 2f, Gdx.graphics.height.toFloat())
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        when {
            y >= Gdx.graphics.height * HEIGHT_PERCENT -> setPosition(x, y - (Gdx.graphics.deltaTime * animationVerticalSpeed))
            leftAnimated -> {
                if (x <= MARGIN_TITLE_PERCENT * Gdx.graphics.width) leftAnimated = false
                setPosition(x - Gdx.graphics.deltaTime * animationHorizontalSpeed, y)
            }
            else -> {
                if (x >= (Gdx.graphics.width - MARGIN_TITLE_PERCENT * Gdx.graphics.width - width)) leftAnimated = true
                setPosition(x + Gdx.graphics.deltaTime * animationHorizontalSpeed, y)
            }
        }


        batch?.let {
            bitmapFontRentBig.draw(it, "Explode", x, y + height)
            bitmapFontRentSmall.draw(it, "the", x, y - explodeGyphLayout.height + height)
            bitmapFontRentBig.draw(it, "bubble", x + theGyphLayout.width + MARGIN_ELEMENT_FONT_PERCENT * Gdx.graphics.width, y - explodeGyphLayout.height + height)
            bitmapFontMarkerBig.draw(it, "!", x + theGyphLayout.width + bubbleGyphLayout.width + MARGIN_ELEMENT_FONT_PERCENT * Gdx.graphics.width, y - explodeGyphLayout.height + height)
        }

    }

    fun deatch() {
        bitmapFontRentSmall.dispose()
        bitmapFontRentBig.dispose()
    }

}