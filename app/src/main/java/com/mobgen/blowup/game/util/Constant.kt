package com.mobgen.blowup.game.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color

class Constant {
    companion object {
        const val PATH_PAWN = "texture/pawn/"
        val PIXELS_TO_METER = 1 / (Gdx.graphics.height / 10f)

        fun insertSuffix(word: String, suffix: String) = "${word.substringBefore('.')}$suffix.${word.substringAfter('.')}"
        fun getColor(color: Color) = Color(convertColor(color.r), convertColor(color.g), convertColor(color.b), color.a)

        private fun convertColor(channel: Int) = channel / 255f
    }

    enum class Color(val r: Int, val g: Int, val b: Int, val a: Float) {
        PlayerBlue(80, 119, 204, 1f),
    }

    enum class Texture(val tName: String) {
        Board("texture/board.png"),
    }

    enum class Font(val fName: String) {
        MetropolisLight("font/metropolis_light.ttf")
    }

}