package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mobgen.blowup.game.util.Constant

class ScoreEntity(private val titleBitmapFont: BitmapFont, private val bitmapFont: BitmapFont, private var scores: List<Pair<String, String>>) : Actor() {
    companion object {
        const val SCORE_FONT = 46
        const val SCORE_FONT_TITLE = 74
        private const val HEIGHT_MERGIN_ELEMENT_PERCENT = 0.03f
        private const val CLOSE_ANIMATION_END_PERCENT = 0.02f
        private const val ANIMATION_SPEED_PERCENT = 1.8f
    }

    private val gyphLayout = GlyphLayout()
    private val gyphLayoutTitle = GlyphLayout()
    private var goTo = 0f
    private var heightVariation = 0f
    private var directionVariation = 0f
    private val animationSpeed = ANIMATION_SPEED_PERCENT * Gdx.graphics.width
    private var justAnimateListener = {}

    init {
        gyphLayoutTitle.setText(titleBitmapFont, Constant.Strings.HightScore.sName)
        reset(scores)
    }

    fun reset(scores: List<Pair<String, String>>) {
        this.scores = scores.reversed()
        val maxElementSize = scores.maxBy { it.first.length + it.second.length }
        gyphLayout.setText(bitmapFont, "${maxElementSize?.first}: ${maxElementSize?.second}")
        setSize(gyphLayout.width, (scores.size - 1) * gyphLayout.height + HEIGHT_MERGIN_ELEMENT_PERCENT * (Gdx.graphics.height * scores.size + 1))
        setPosition(Gdx.graphics.width / 2 - width / 2, -Gdx.graphics.height.toFloat() / 2)
        goTo = Gdx.graphics.height / 2 - height / 2
        justAnimateListener = {}
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        heightVariation = goTo - y
        directionVariation = if (Math.abs(heightVariation) >= CLOSE_ANIMATION_END_PERCENT * Gdx.graphics.width) {
            heightVariation / Math.abs(heightVariation)
        } else {
            justAnimateListener()
            setPosition(x, goTo)
            0f
        }
        setPosition(x, y + (directionVariation * animationSpeed * Gdx.graphics.deltaTime))
        batch?.color = Color.WHITE

        batch?.let {
            titleBitmapFont.draw(it, Constant.Strings.HightScore.sName, Gdx.graphics.width / 2 - gyphLayoutTitle.width / 2, y + (scores.size + 1) * HEIGHT_MERGIN_ELEMENT_PERCENT * Gdx.graphics.height + (scores.size + 1) * gyphLayout.height)
            scores.forEachIndexed { index, value ->
                bitmapFont.draw(it, "${(index + 1)} ${value.first}: ${value.second}", x, y + (scores.size - index - 1) * HEIGHT_MERGIN_ELEMENT_PERCENT * Gdx.graphics.height + (scores.size - index - 1) * gyphLayout.height)
            }
        }

    }

    fun goBack(onEndAnimation: () -> Unit = {}) {
        goTo = if (goTo == Gdx.graphics.height / 2 - height / 2) -Gdx.graphics.height.toFloat() / 2 else Gdx.graphics.height / 2 - height / 2
        justAnimateListener = onEndAnimation
    }

    fun deatch() {
        bitmapFont.dispose()
    }

}