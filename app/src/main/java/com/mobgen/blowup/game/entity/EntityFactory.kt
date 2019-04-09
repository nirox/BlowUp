package com.mobgen.blowup.game.entity

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.mobgen.blowup.game.Util
import com.mobgen.blowup.game.screen.MainScreen
import com.mobgen.blowup.game.util.Constant

class EntityFactory(private val assetManager: AssetManager, private val localAssetManager: AssetManager) {
    fun createPlayButton(heightPosition: Float, visibility: Boolean, listener: InputListener) = ButtonFontEntity(heightPosition, Constant.Strings.Play.sName, Util.createBitmapFont(Constant.Font.Rentuck.fName, MainScreen.FONT_PLAY_SIZE), visibility, listener)
    fun createScoreButton(heightPosition: Float, visibility: Boolean, listener: InputListener) = ButtonFontEntity(heightPosition, Constant.Strings.Score.sName, Util.createBitmapFont(Constant.Font.Rentuck.fName, MainScreen.FONT_SCORE_SIZE), visibility, listener)
    fun createBackground() = BackgroundEntity(assetManager.get(Constant.Texture.Background.tName))
    fun createTitleEntity() = TitleEntity(Util.createBitmapFont(Constant.Font.Rentuck.fName, TitleEntity.SMALL_FONT_SIZE), Util.createBitmapFont(Constant.Font.Rentuck.fName, TitleEntity.BIG_FONT_SIZE), Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, TitleEntity.EXCLAMATION_FONT_SIZE))
}