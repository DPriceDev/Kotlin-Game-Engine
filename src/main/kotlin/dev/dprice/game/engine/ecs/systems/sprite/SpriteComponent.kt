package dev.dprice.game.engine.ecs.systems.sprite

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.model.Vector2f

sealed class Texture {
    abstract val path: String
    data class Full(override val path: String) : Texture()

    data class TileMap(
        override val path: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val textureHeight: Float,
        val textureWidth: Float,
        val xIndex: Int,
        val yIndex: Int,
        val spacing: Int = 0
    ) : Texture() {

        val tileCoords = buildTileCoords()

        private fun buildTileCoords(): TileCoords {
            val xPercent = tileWidth.toFloat() / textureWidth
            val yPercent = tileHeight.toFloat() / textureHeight

            val xSpacing = spacing.takeIf { it > 0 }?.let {
                xIndex * (it / textureWidth)
            } ?: 0f
            val ySpacing = spacing.takeIf { it > 0 }?.let {
                yIndex * (it / textureHeight)
            } ?: 0f

            return TileCoords(
                1f - (yIndex * yPercent + ySpacing),
                1f - ((yIndex + 1) * yPercent + ySpacing),
                (xIndex) * xPercent + xSpacing,
                (xIndex + 1) * xPercent + xSpacing
            )
        }

        data class TileCoords(
            val top: Float,
            val bottom: Float,
            val left: Float,
            val right: Float
        )
    }
}

// todo: Split into sprite and tileMapSprite
// todo: Make animation component for tileMap sprites
class SpriteComponent(
    var texture: Texture,
    val size: Vector2f,
    val zDepth: Float = 0f,
    val vertexShader: String = "shaders/InstancedSpriteVertexShader.glsl",
    val fragmentShader: String = "shaders/SpriteFragmentShader.glsl",
) : Component()