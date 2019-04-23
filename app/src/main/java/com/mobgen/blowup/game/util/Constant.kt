package com.mobgen.blowup.game.util

class Constant {

    enum class Color(val r: Int, val g: Int, val b: Int, val a: Float) {
        PlayerBlue(80, 119, 204, 1f),
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

    enum class Strings(val sName: String) {
        Play("Play"),
        Score("Score"),
        Resume("Resume"),
        Exit("Exit")
    }

}