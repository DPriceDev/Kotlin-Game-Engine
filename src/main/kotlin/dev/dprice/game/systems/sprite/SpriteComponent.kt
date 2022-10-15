package dev.dprice.game.systems.sprite

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

class SpriteComponent(
    override val entity: Entity,
    val texturePath: String,
    val vertexShader: String = "shaders/BasicVertexShader.glsl",
    val fragmentShader: String = "shaders/BasicFragmentShader.glsl",
) : Component