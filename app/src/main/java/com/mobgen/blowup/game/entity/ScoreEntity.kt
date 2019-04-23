package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class ScoreEntity(private val bitmapFont: BitmapFont, private val scores: MutableList<Pair<String, String>>) : Actor() {
    companion object {
        const val SCORE_FONT = 46
        private const val HEIGHT_MERGIN_ELEMENT_PERCENT = 0.03f
        private const val CLOSE_ANIMATION_END_PERCENT = 0.02f
        private const val ANIMATION_SPEED_PERCENT = 0.8f
    }

    private val gyphLayout = GlyphLayout()
    private var goTo = 0f
    private var heightVariation = 0f
    private var directionVariation = 0f
    private val animationSpeed = ANIMATION_SPEED_PERCENT * Gdx.graphics.width
    private var justAnimateListener = {}

    init {
        scores.add(Pair("Nirox", "79000"))
        scores.add(Pair("Pepito", "8000"))
        scores.add(Pair("Pepitox", "900"))
        scores.add(Pair("Pepito", "30"))
        scores.add(Pair("Antonio", "0"))
        val maxElementSize = scores.maxBy { it.first.length + it.second.length }
        gyphLayout.setText(bitmapFont, "${maxElementSize?.first}: ${maxElementSize?.second}")
        reset()
    }

    fun reset() {
        setSize(gyphLayout.width, (scores.size - 1) * gyphLayout.height + HEIGHT_MERGIN_ELEMENT_PERCENT * Gdx.graphics.height * scores.size)
        setPosition(Gdx.graphics.width / 2 - width / 2, -Gdx.graphics.height.toFloat() / 2)
        goTo = height
        justAnimateListener = {}
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        heightVariation = goTo - y
        directionVariation = if (Math.abs(heightVariation) >= CLOSE_ANIMATION_END_PERCENT * Gdx.graphics.height) {
            heightVariation / Math.abs(heightVariation)
        } else {
            justAnimateListener()
            setPosition(x, goTo)
            0f
        }
        setPosition(x, y + (directionVariation * animationSpeed * Gdx.graphics.deltaTime))
        batch?.color = Color.WHITE
        batch?.let {
            scores.forEachIndexed { index, value ->
                bitmapFont.draw(it, "${(index + 1)} ${value.first}: ${value.second}", x, y + (scores.size - index) * HEIGHT_MERGIN_ELEMENT_PERCENT * Gdx.graphics.height + (scores.size - index) * gyphLayout.height)
            }
        }

    }

    fun goBack(onEndAnimation: () -> Unit = {}) {
        goTo = if (goTo == height) -Gdx.graphics.height.toFloat() / 2 else height
        justAnimateListener = onEndAnimation
    }

    fun deatch() {
        bitmapFont.dispose()
    }

}