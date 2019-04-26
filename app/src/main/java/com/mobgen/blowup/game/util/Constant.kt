package com.mobgen.blowup.game.util

import com.badlogic.gdx.graphics.Color

class Constant {

    companion object {
        private fun convertColor(channel: Int) = channel / 255f
        fun getColor(color: Color) = Color(convertColor(color.r), convertColor(color.g), convertColor(color.b), color.a)
    }

    enum class Texture(val tName: String) {
        BackgroundWater("texture/backgroundWater.png"),
        BackgroundRock("texture/backgroundRock.png"),
        BackgroundCave("texture/backgroundCave.png"),
        Bubble("texture/bubble.png"),
        BubbleGame("texture/bubbleGame.png"),
        Bomb("texture/bomb.png"),
        GameBar("texture/gameBar.png"),
        PauseIcon("texture/pause.png"),
        WhitePixel("texture/whitePixel.png")
    }

    enum class Font(val fName: String) {
        Rentuck("font/rentuck.ttf"),
        MarkerFeltWide("font/markerfeltwide.ttf")
    }

    enum class Skin(val sName: String) {
        Default("skin/uiskin.json")
    }

    enum class Strings(val sName: String) {
        Play("Play"),
        Score("Score"),
        Resume("Resume"),
        Exit("Exit"),
        GameOver("Game Over"),
        Level("Level"),
        DefaultPlayerName("Anonymous"),
        HightScore("Hight Score")
    }

    enum class Sound(val sName: String) {
        Background("sound/background.mp3"),
        BlowUp("sound/blowUp.wav"),
        BombExplotion("sound/bombExplotion.wav"),
        BombFuse("sound/bombFuse.wav"),
        GameOver("sound/gameOver.wav")

    }

    enum class Color(val r: Int, val g: Int, val b: Int, val a: Float) {
        Brown(64, 31, 0, 1f)
    }

}