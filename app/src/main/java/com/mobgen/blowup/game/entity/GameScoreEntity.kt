package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class GameScoreEntity(private val bitmapFontRentSmall: BitmapFont) : Actor() {

    companion object {
        const val FONT_SIZE = 40
    }

    private val pointsGyphLayout = GlyphLayout()
    var pointsText = 0

    init {
        pointsGyphLayout.setText(bitmapFontRentSmall, pointsText.toString())
        bitmapFontRentSmall.color = Color(0.25f, 0.12f, 0f, 1f)
        setSize(pointsGyphLayout.width, pointsGyphLayout.height)
        setPosition(Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, Gdx.graphics.height.toFloat() - Gdx.graphics.width * 0.025f)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        if (pointsText <= 0) pointsText = 0
        pointsGyphLayout.setText(bitmapFontRentSmall, pointsText.toString())
        batch?.let {
            bitmapFontRentSmall.draw(it, pointsText.toString(), Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, y)
        }
    }

    fun deatch() {
        bitmapFontRentSmall.dispose()
    }

}