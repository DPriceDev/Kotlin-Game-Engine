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
        val xIndex: Int,
        val yIndex: Int,
        val spacing: Int = 0
    ) : Texture()
}

class SpriteComponent(
    override val entity: Entity,
    val texture: Texture,
    val vertexShader: String = "shaders/BasicVertexShader.glsl",
    val fragmentShader: String = "shaders/BasicFragmentShader.glsl",
) : Component