package com.mobgen.blowup.game.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

class BackgroundEntity(private val textureBackgroundWater: Texture, private val textureBackgroundRock: Texture, private val textureBackgroundCave: Texture) : Actor() {

    companion object {
        private const val HEIGHT_MARGIN_PERCENT = 0.02f
        private const val BACKGROUND_WATER_SCREENS = 2
        const val ANIMATION_SPEED_PERCENT = 1.2f
        private const val CLOSE_ANIMATION_END_PERCENT = 0.02f
    }

    private val animationSpeed = ANIMATION_SPEED_PERCENT * Gdx.graphics.width
    private val sizeBackgroundWater = Vector2(Gdx.graphics.width.toFloat(), BACKGROUND_WATER_SCREENS * Gdx.graphics.height.toFloat())
    private val sizeBackgroundRock = Vector2(Gdx.graphics.width.toFloat(), Gdx.graphics.width * textureBackgroundRock.height / textureBackgroundRock.width.toFloat())
    private val sizeBackgroundCave = Vector2(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var animate = false
    private var distanceVariation  =0f
    private var goTo = 0f
    private lateinit var onEndAnimation: () -> Unit

    val waterPosition = -(sizeBackgroundWater.y / 2 - HEIGHT_MARGIN_PERCENT * Gdx.graphics.height)
    val floorPosition = 0f
    val cavePosition = (sizeBackgroundRock.y + sizeBackgroundCave.y)

    init {
        setPosition(floorPosition, floorPosition)
        setSize(sizeBackgroundWater.x, sizeBackgroundWater.y + sizeBackgroundRock.y + sizeBackgroundCave.y)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        if (animate) {
            distanceVariation = goTo - y

            when {
                Math.abs(distanceVariation) <= CLOSE_ANIMATION_END_PERCENT * Gdx.graphics.height -> {
                    setPosition(x, goTo)
                    animate = false
                    onEndAnimation()
                }
                distanceVariation != 0f -> setPosition(x, y + (animationDirection() * animationSpeed.toFloat() * Gdx.graphics.deltaTime))
            }
        }

        batch?.color = Color.WHITE
        batch?.draw(textureBackgroundWater, x, y, sizeBackgroundWater.x, sizeBackgroundWater.y)
        batch?.draw(textureBackgroundRock, x, y - sizeBackgroundRock.y, sizeBackgroundRock.x, sizeBackgroundRock.y)
        batch?.draw(textureBackgroundCave, x, y - sizeBackgroundRock.y - sizeBackgroundCave.y, sizeBackgroundCave.x, sizeBackgroundCave.y)
    }

    fun moveToPositionWithoutAnimation(goToPosition: Float) {
        if (!animate) {
            goTo = goToPosition
            setPosition(x, goToPosition)
        }
    }


    fun moveToPosition(goToPosition: Float, onStartAnimation: () -> Unit = {}, onEndAnimation: () -> Unit = {}) {
        if (!animate && goToPosition != y) {
            animate = true
            goTo = goToPosition
            onStartAnimation()
            this.onEndAnimation = onEndAnimation
        }
    }


    fun toTop(): Boolean = animationDirection() > 0

    fun deatch() {
        textureBackgroundWater.dispose()
        textureBackgroundRock.dispose()
        textureBackgroundCave.dispose()
    }

    private fun animationDirection(): Float {
        val distance = goTo - y
        return (distance) / Math.abs(distance)
    }

}