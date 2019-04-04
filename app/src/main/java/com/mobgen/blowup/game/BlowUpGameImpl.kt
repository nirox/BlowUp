package com.mobgen.blowup.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.mobgen.blowup.game.screen.LoadingScreen


class BlowUpGameImpl(private val activity: BlowUpGame.Listener) : Game(), BlowUpGame {

    private lateinit var loadingScreen: LoadingScreen
    private var loadGameScreen = false

    companion object {
        const val TAG = "BlowUpGameImpl"
        const val GRAVITY_Y = 0f
        const val GRAVITY_X = 0f
    }

    val assetManager = AssetManager()
    val localAssetManager = AssetManager(LocalFileHandleResolver())

    override fun create() {
        loadingScreen = LoadingScreen(this)
        setScreen(loadingScreen)
    }


    fun launchMainGameScreen() {
        /*val textureParameter = TextureParameter()
        textureParameter.minFilter = TextureFilter.MipMapLinearLinear
        textureParameter.magFilter = TextureFilter.Linear
        textureParameter.genMipMaps = true

        assetManager.load(Constant.Texture.Board.tName, Texture::class.java)
        assetManager.load(Constant.Texture.DiceAnimation.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.DiceFaces.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.TurnButton.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.Bubble.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.ExitButton.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.DiceDisabled.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.CardDisabled.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.BattleDisabled.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.BrightnessField.tName, Texture::class.java, textureParameter)
        assetManager.load(Constant.Texture.BrightnessPawnsInField.tName, Texture::class.java, textureParameter)*/
    }

    fun checkLoadGameScreen() = assetManager.update() && localAssetManager.update() && !loadGameScreen

    fun loadGameScreen(): SequenceAction {
        loadGameScreen = true
        return Actions.sequence(Actions.run {
            /*mainScreen = MainGameScreen(this)
            setScreen(mainScreen)
            loadingScreen.dispose()*/
        })
    }

    override fun dispose() {
        super.dispose()
        //if (::mainScreen.isInitialized) mainScreen.dispose()
    }

}