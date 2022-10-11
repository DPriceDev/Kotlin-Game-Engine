package dev.dprice.game.systems.sprite

import dev.dprice.game.ecs.model.Component
import dev.dprice.game.ecs.model.Entity

class SpriteComponent(
    override val entity: Entity,
    val vertexShader: String = "shaders/BasicVertexShader.glsl",
    val fragmentShader: String = "shaders/BasicFragmentShader.glsl",
    ) : Component