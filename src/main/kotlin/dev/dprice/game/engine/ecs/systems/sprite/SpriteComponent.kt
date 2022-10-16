package dev.dprice.game.engine.ecs.systems.sprite

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

sealed class Texture {
    abstract val path: String
    class Full(override val path: String) : Texture()

    class TileMap(
        override val path: String,
        val tileWidth: Int,
        val tileHeight: Int,
        var xIndex: Int,
        var yIndex: Int,
        val spacing: Int = 0
    ) : Texture()
}

// todo: Split into sprite and tileMapSprite
// todo: Make animation component for tileMap sprites
class SpriteComponent(
    override val entity: Entity,
    val texture: Texture,
    val zDepth: Float = 0f,
    val vertexShader: String = "shaders/BasicVertexShader.glsl",
    val fragmentShader: String = "shaders/BasicFragmentShader.glsl",
) : Component