package com.mobgen.blowup.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Array

class Util {

    companion object {

        fun createTextureRegion(texture: Texture, cols: Int, rows: Int, repeatTimes: Int): Array<TextureRegion> {
            val tmp = TextureRegion.split(texture, texture.width / cols, texture.height / rows)
            val animationFrames = Array<TextureRegion>(cols * rows * repeatTimes)
            for (time in 0 until repeatTimes) {
                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        animationFrames.add(tmp[row][col])
                    }
                }
            }
            return animationFrames
        }

        fun createBitmapFont(fontName: String, size: Int): BitmapFont {
            val generator = FreeTypeFontGenerator(Gdx.files.internal(fontName))
            val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
            parameter.size = size * Gdx.graphics.density.toInt()
            val bitmapFont = generator.generateFont(parameter)
            generator.dispose()
            return bitmapFont
        }
    }

}