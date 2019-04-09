package com.mobgen.blowup.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.mobgen.blowup.game.screen.GameScreen
import com.mobgen.blowup.game.screen.LoadingScreen
import com.mobgen.blowup.game.screen.MainScreen
import com.mobgen.blowup.game.screen.ScoreScreen
import com.mobgen.blowup.game.util.Constant


class BlowUpGameImpl(private val activity: BlowUpGame.Listener) : Game(), BlowUpGame {

    private lateinit var loadingScreen: LoadingScreen
    private var loadGameScreen = false
    private lateinit var mainScreen: MainScreen
    private lateinit var gameScreen: GameScreen
    private lateinit var scoreScreen: ScoreScreen

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
        launchMainGameScreen()
    }


    fun launchMainGameScreen() {
        val textureParameter = TextureLoader.TextureParameter().apply {
            minFilter = Texture.TextureFilter.MipMapLinearLinear
            magFilter = Texture.TextureFilter.Linear
            genMipMaps = true
        }

        Constant.Texture.values().forEach { assetManager.load(it.tName, Texture::class.java, textureParameter) }
    }

    fun checkLoadGameScreen() = assetManager.update() && localAssetManager.update() && !loadGameScreen

    fun loadGameScreen(): SequenceAction {
        loadGameScreen = true
        return Actions.sequence(Actions.run {
            goMainScreen()
            loadingScreen.dispose()
        })
    }

    fun goMainScreen() {
        mainScreen = MainScreen(this)
        setScreen(mainScreen)
    }

    fun goGameScreen() {
        gameScreen = GameScreen(this)
        setScreen(gameScreen)
    }

    fun goScoreScreen() {
        scoreScreen = ScoreScreen(this)
        setScreen(scoreScreen)
    }

    override fun dispose() {
        super.dispose()
        mainScreen.dispose()
        scoreScreen.dispose()
        gameScreen.dispose()
    }

}