package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mobgen.blowup.game.Util

class BackgroundEntity(private val texture: Texture) : Actor() {
    companion object {
        private const val FRAME_COLS = 5
        private const val FRAME_ROWS = 20
        private const val FRAME_DURATION = 0.025f
    }

    private var elapsed = 0f
    private var loadAnimation: Animation<TextureRegion>
    private val frameSize = Vector2(texture.width / FRAME_COLS.toFloat(), texture.height / FRAME_ROWS.toFloat())
    private val size = Vector2((frameSize.x * Gdx.graphics.height) / frameSize.y, Gdx.graphics.height.toFloat())

    init {
        val animationFrames = Util.createTextureRegion(texture, FRAME_COLS, FRAME_ROWS, 1)
        animationFrames.removeIndex(animationFrames.size - 1)
        loadAnimation = Animation(FRAME_DURATION, animationFrames, Animation.PlayMode.LOOP)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        elapsed += Gdx.graphics.deltaTime

        batch?.draw(loadAnimation.getKeyFrame(elapsed), Gdx.graphics.width / 2f - size.x / 2f, Gdx.graphics.height / 2f - size.y / 2f, size.x, size.y)
    }

    fun deatch() {
        texture.dispose()
    }

}