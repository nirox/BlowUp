package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class PointEntity(private val bitmapFontRentSmall: BitmapFont, private var pointColor: Color, private val pointText: String, parentPositionX: Float, parentPositionY: Float, parentSizeX: Float, parentSizeY: Float) : Actor() {

    companion object {
        const val FONT_SIZE = 60
        const val VANISHING_TIME = 1f
        const val MOVEMENT_PERCENT = 0.1f
        const val MOVE_DISTANCE = 0.1f
        const val INITIAL_POSITION_DISTANCE = 50
    }

    private var elapsed = 0f
    private val pointsGyphLayout = GlyphLayout()
    private var initialPosition = 0f
    private var reachTop = false
    private var alphaColor = 1f

    init {
        pointsGyphLayout.setText(bitmapFontRentSmall, pointText)
        setSize(pointsGyphLayout.width, pointsGyphLayout.height)
        setPosition(parentPositionX + parentSizeX / 2 - width / 2, parentPositionY + parentSizeY / 2 + height / 2)
        initialPosition = y
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        elapsed += Gdx.graphics.deltaTime
        if (!reachTop) {
            if ((initialPosition + Gdx.graphics.height * MOVE_DISTANCE) - y > INITIAL_POSITION_DISTANCE) {
                setPosition(x, y + ((initialPosition + Gdx.graphics.height * MOVE_DISTANCE) - y) * MOVEMENT_PERCENT)
            } else {
                reachTop = true
                initialPosition = y + INITIAL_POSITION_DISTANCE
                elapsed = 0f
            }
        } else {
            if (alphaColor > 0f) {
                setPosition(x, y - (initialPosition - y) * MOVEMENT_PERCENT)
                alphaColor = VANISHING_TIME - elapsed*2
                pointColor = Color(pointColor.r, pointColor.g, pointColor.b, alphaColor)
            } else {
                finish()
            }
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