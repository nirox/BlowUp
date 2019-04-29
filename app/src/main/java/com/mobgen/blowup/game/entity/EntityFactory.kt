package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.mobgen.blowup.game.Util
import com.mobgen.blowup.game.screen.GameScreen
import com.mobgen.blowup.game.screen.MainScreen
import com.mobgen.blowup.game.util.Constant

class EntityFactory(private val assetManager: AssetManager, private val localAssetManager: AssetManager) {

    companion object {
        private const val CURSOR = "cursor"
        private const val TEXTFIELD = "textfield"
    }

    private val pointBitmapFont = Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, PointEntity.FONT_SIZE)
    private val rentuckBitmapFont = Util.createBitmapFont(Constant.Font.Rentuck.fName, GameScreen.FONT_SIZE_INPUT)
    private val skin = Skin(Gdx.files.internal(Constant.Skin.Default.sName))

    fun createPlayButton(heightPosition: Float, visibility: Boolean, listener: InputListener) = ButtonFontEntity(true, heightPosition, Constant.Strings.Play.sName, Util.createBitmapFont(Constant.Font.Rentuck.fName, MainScreen.FONT_PLAY_SIZE), visibility, listener)
    fun createScoreButton(heightPosition: Float, visibility: Boolean, listener: InputListener) = ButtonFontEntity(false, heightPosition, Constant.Strings.Score.sName, Util.createBitmapFont(Constant.Font.Rentuck.fName, MainScreen.FONT_SCORE_SIZE), visibility, listener)
    fun createBackground() = BackgroundEntity(assetManager.get(Constant.Texture.BackgroundWater.tName), assetManager.get(Constant.Texture.BackgroundRock.tName), assetManager.get(Constant.Texture.BackgroundCave.tName))
    fun createTitleEntity() = TitleEntity(assetManager.get(Constant.Texture.Bubble.tName), Util.createBitmapFont(Constant.Font.Rentuck.fName, TitleEntity.BIG_FONT_SIZE), Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, TitleEntity.EXCLAMATION_FONT_SIZE))
    fun createScoreEntity(scores: List<Pair<String, String>>) = ScoreEntity(Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, ScoreEntity.SCORE_FONT_TITLE),Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, ScoreEntity.SCORE_FONT), scores.toMutableList())
    fun createBubble(listener: InputListener, onAutomaticBlowUp: (bubble: BubbleEntity) -> Unit) = BubbleEntity(assetManager.get(Constant.Texture.BubbleGame.tName), assetManager.get(Constant.Texture.ExplodingBubble.tName), listener, onAutomaticBlowUp)
    fun createBomb(listener: InputListener, onAutomaticBlowUp: (bomb: BombEntity) -> Unit) = BombEntity(assetManager.get(Constant.Texture.Bomb.tName), assetManager.get(Constant.Texture.ExplodingBomb.tName), listener, onAutomaticBlowUp)
    fun createPauseButton(listener: InputListener) = PauseButtonEntity(assetManager.get(Constant.Texture.PauseIcon.tName), listener)
    fun createTarget() = TargetColorEntity(assetManager.get(Constant.Texture.BubbleGame.tName))
    fun createGameBar() = GameBarEntity(assetManager.get(Constant.Texture.GameBar.tName))
    fun createTimer() = TimerEntity(assetManager.get(Constant.Texture.WhitePixel.tName))
    fun createGameScore() = GameScoreEntity(Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, GameScoreEntity.FONT_SIZE))
    fun createLevelText() = LevelTextEntity(Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, LevelTextEntity.FONT_SIZE))
    fun createGetText() = GetTextEntity(Util.createBitmapFont(Constant.Font.MarkerFeltWide.fName, GetTextEntity.FONT_SIZE))
    fun createPoints(pointColor: Color, pointText: String, parentPositionX: Float, parentPositionY: Float, parentSizeX: Float, parentSizeY: Float) = PointEntity(pointBitmapFont, pointColor, pointText, parentPositionX, parentPositionY, parentSizeX, parentSizeY)
    fun createResumeButton(heightPosition: Float, visibility: Boolean, listener: InputListener) = ButtonFontEntity(true, heightPosition, Constant.Strings.Resume.sName, Util.createBitmapFont(Constant.Font.Rentuck.fName, GameScreen.FONT_SIZE), visibility, listener)
    fun createExitButton(heightPosition: Float, visibility: Boolean, listener: InputListener) = ButtonFontEntity(false, heightPosition, Constant.Strings.Exit.sName, Util.createBitmapFont(Constant.Font.Rentuck.fName, GameScreen.FONT_SIZE), visibility, listener)

    fun createTextStyleDefault() = TextField.TextFieldStyle().apply {
        font = rentuckBitmapFont
        fontColor = Color.BLACK
        cursor = skin.getDrawable(CURSOR)
        background = skin.getDrawable(TEXTFIELD)
    }
}