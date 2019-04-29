package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mobgen.blowup.game.util.Constant

class GameScoreEntity(private val bitmapFontRentSmall: BitmapFont) : Actor() {

    companion object {
        const val FONT_SIZE = 40
    }

    private val pointsGyphLayout = GlyphLayout()
    var pointsText = 0
    var minPoints = 0
    var maxPoints = 0
    var colorText = Constant.getColor(Constant.Color.Brown)
    val initPosition = Vector2(Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, Gdx.graphics.height.toFloat())

    init {
        pointsGyphLayout.setText(bitmapFontRentSmall, pointsText.toString())
        bitmapFontRentSmall.color = colorText
        setSize(pointsGyphLayout.width, pointsGyphLayout.height)
        setPosition(initPosition.x, initPosition.y)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        bitmapFontRentSmall.color = colorText
        if (pointsText < 0) pointsText = 0
        if (pointsText > maxPoints) maxPoints = pointsText
        pointsGyphLayout.setText(bitmapFontRentSmall, pointsText.toString())
        batch?.let {
            bitmapFontRentSmall.draw(it, pointsText.toString(), Gdx.graphics.width / 2f - pointsGyphLayout.width / 2f, y)
        }
    }

    fun deatch() {
        bitmapFontRentSmall.dispose()
    }
}