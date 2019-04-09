package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener

class ButtonFontEntity(private val heighPosition: Float, private val word: String, private val bitmapFont: BitmapFont, visibility: Boolean, listener: InputListener) : Actor() {
    private val size: Vector2
    private val nameGyphLayout = GlyphLayout()

    init {
        nameGyphLayout.setText(bitmapFont, word)
        size = Vector2(nameGyphLayout.width, nameGyphLayout.height)
        isVisible = visibility
        setPosition(Gdx.graphics.width / 2 - size.x / 2, heighPosition)
        setSize(size.x, size.y)
        addListener(listener)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        batch?.let {
            bitmapFont.draw(it, word, x, y + height)
        }

    }

    fun deatch() {
        bitmapFont.dispose()
    }

}