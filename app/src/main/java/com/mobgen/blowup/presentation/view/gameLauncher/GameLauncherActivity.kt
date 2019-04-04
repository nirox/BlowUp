package com.mobgen.blowup.presentation.view.gameLauncher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.mobgen.blowup.BlowUpApplication
import com.mobgen.blowup.game.BlowUpGame
import com.mobgen.blowup.game.BlowUpGameImpl


class GameLauncherActivity : AndroidApplication(), BlowUpGame.Listener {

    private lateinit var blowUpGame: BlowUpGame

    companion object {
        fun newInstance(context: Context) = Intent(context, GameLauncherActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blowUpGame = BlowUpGameImpl(this)
        AndroidApplicationConfiguration()
        initialize(blowUpGame as com.badlogic.gdx.Game, AndroidApplicationConfiguration())
        initDagger()
    }

    private fun initDagger() {
        val app = application as BlowUpApplication
        app.mainComponent.inject(this)
    }

    private fun doInGame(handler: () -> Unit) {
        Gdx.app.postRunnable(handler)
    }
}