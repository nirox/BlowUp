package com.mobgen.blowup.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.mobgen.blowup.game.util.Constant

class SoundFactory(private val assetManager: AssetManager) {

    fun getBackgroundSound() = assetManager.get(Constant.Sound.Background.sName, Music::class.java)
    fun getBombExplotion() = assetManager.get(Constant.Sound.BombExplotion.sName, Sound::class.java)
    fun getBombFuse() = assetManager.get(Constant.Sound.BombFuse.sName, Sound::class.java)
    fun getBlowUp() = assetManager.get(Constant.Sound.BlowUp.sName, Sound::class.java)
    fun getGameOver() = assetManager.get(Constant.Sound.GameOver.sName, Music::class.java)

}