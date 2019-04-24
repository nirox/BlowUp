package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mobgen.blowup.game.util.Constant

class LevelTextEntity(private val bitmapFontRentSmall: BitmapFont) : Actor() {

    companion object {
        const val FONT_SIZE = 70
        const val FONT_POSITION_PERCENT = 0.8f
    }

    private val pointsGyphLayout = GlyphLayout()
    var level = 0
    var levelText = "Level "
    var colorText = Constant.getColor(Constant.Color.Brown)

    init {
        isVisible = false
        pointsGyphLayout.setText(bitmapFontRentSmall, levelText + level)
        bitmapFontRentSmall.color = colorText
        setSize(pointsGyphLayout.width, pointsGyphLayout.height)
        setPosition(Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, Gdx.graphics.height * FONT_POSITION_PERCENT)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (isVisible) {
            batch?.let {
                if (level != 0)
                    pointsGyphLayout.setText(bitmapFontRentSmall, levelText + level)
                else
                    pointsGyphLayout.setText(bitmapFontRentSmall, levelText)

                setSize(pointsGyphLayout.width, pointsGyphLayout.height)

                if (level != 0)
                    bitmapFontRentSmall.draw(it, levelText + level, Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, y)
                else
                    bitmapFontRentSmall.draw(it, levelText, Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, y)
            }
        }
    }

    fun deatch() {
        bitmapFontRentSmall.dispose()
    }

}