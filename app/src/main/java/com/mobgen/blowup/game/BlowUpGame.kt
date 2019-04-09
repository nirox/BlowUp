package com.mobgen.blowup.game

interface BlowUpGame {
    interface Listener {
        fun getScoreData(number: Int): List<Pair<String, String>>
        fun saveScoreData(value: Pair<String, String>)
        fun goBack()
    }
}
