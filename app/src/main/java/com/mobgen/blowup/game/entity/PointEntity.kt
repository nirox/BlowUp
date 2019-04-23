package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class PointEntity(private val bitmapFontRentSmall: BitmapFont, private val pointColor: Color, private val pointText: String, parentPositionX: Float, parentPositionY: Float, parentSizeX: Float, parentSizeY: Float) : Actor() {

    companion object {
        const val FONT_SIZE = 70
        const val INIT_ELAPSED_TIME = 0.5f
        const val MAX_ELAPSED_TIME = 0.5f + INIT_ELAPSED_TIME
    }

    private var elapsed = INIT_ELAPSED_TIME
    private val pointsGyphLayout = GlyphLayout()

    init {
        pointsGyphLayout.setText(bitmapFontRentSmall, pointText)
        setSize(pointsGyphLayout.width, pointsGyphLayout.height)
        setPosition(parentPositionX + parentSizeX / 2 - width / 2, parentPositionY + parentSizeY / 2 + height / 2)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        elapsed += Gdx.graphics.deltaTime
        if (elapsed > MAX_ELAPSED_TIME) {
            finish()
        }
        if (isVisible) {
            pointsGyphLayout.setText(bitmapFontRentSmall, pointText)
            bitmapFontRentSmall.color = pointColor
            bitmapFontRentSmall.draw(batch, pointText, x, y)
        }
    }

    private fun finish() {
        isVisible = false
    }

    fun deatch() {
        bitmapFontRentSmall.dispose()
    }

}