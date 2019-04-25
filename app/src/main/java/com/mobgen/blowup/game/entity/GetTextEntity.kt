package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class GetTextEntity(private val bitmapFontRentSmall: BitmapFont) : Actor() {

    companion object {
        const val FONT_SIZE = 20
        const val VERTICAL_POSITION_PERCENT = 0.08f


        const val GET_TEXT = "Get at least "
    }

    private val pointsGyphLayout = GlyphLayout()
    var points = 0
    var getText = GET_TEXT
    private var colorText = Color.WHITE

    init {
        pointsGyphLayout.setText(bitmapFontRentSmall, getText + points)
        bitmapFontRentSmall.color = colorText
        setSize(pointsGyphLayout.width, pointsGyphLayout.height)
        setPosition(Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, Gdx.graphics.height.toFloat() - Gdx.graphics.width * VERTICAL_POSITION_PERCENT)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (isVisible) {
            batch?.let {
                if (points != 0)
                    pointsGyphLayout.setText(bitmapFontRentSmall, getText + points)
                else
                    pointsGyphLayout.setText(bitmapFontRentSmall, getText)

                setSize(pointsGyphLayout.width, pointsGyphLayout.height)

                if (points != 0)
                    bitmapFontRentSmall.draw(it, getText + points, Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, y)
                else
                    bitmapFontRentSmall.draw(it, getText, Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, y)
            }
        }
    }

    fun deatch() {
        bitmapFontRentSmall.dispose()
    }

}