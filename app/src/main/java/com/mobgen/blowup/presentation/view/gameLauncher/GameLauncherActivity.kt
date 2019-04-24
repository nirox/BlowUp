package com.mobgen.blowup.presentation.view.gameLauncher

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.mobgen.blowup.BlowUpApplication
import com.mobgen.blowup.game.BlowUpGame
import com.mobgen.blowup.game.BlowUpGameImpl


class GameLauncherActivity : AndroidApplication(), BlowUpGame.Listener {

    private lateinit var blowUpGame: BlowUpGame
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREFERENCES_NAME = "blowUpScores"
        fun newInstance(context: Context) = Intent(context, GameLauncherActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        blowUpGame = BlowUpGameImpl(this)
        AndroidApplicationConfiguration()
        initialize(blowUpGame as com.badlogic.gdx.Game, AndroidApplicationConfiguration())
        initDagger()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        blowUpGame.onBack()
    }

    override fun saveScoreData(value: Pair<String, String>) {
        val scores: MutableSet<String> = sharedPreferences.getStringSet(value.first, mutableSetOf())
                ?: mutableSetOf()
        scores.add(value.second)
        //sharedPreferences.edit().remove(value.first).apply()
        sharedPreferences.edit().clear().putStringSet(value.first, scores).commit()
    }

    override fun getScoreData(number: Int): List<Pair<String, String>> {
        val scores = mutableListOf<Pair<String, String>>()
        sharedPreferences.all.forEach {
            scores.addAll((it.value as MutableSet<String>).map { value -> Pair(it.key, value) })
        }
        val elements = if (scores.size < number) scores.size else number
        return if (scores.isEmpty()) listOf() else scores.apply { sortBy { it.second.toInt() } }.subList(0, elements)
    }

    private fun initDagger() {
        val app = application as BlowUpApplication
        app.mainComponent.inject(this)
    }

    private fun doInGame(handler: () -> Unit) {
        Gdx.app.postRunnable(handler)
    }

}