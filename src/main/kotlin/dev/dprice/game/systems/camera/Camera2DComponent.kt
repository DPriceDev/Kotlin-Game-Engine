package dev.dprice.game.systems.camera

import dev.dprice.game.ecs.model.Component
import dev.dprice.game.ecs.model.Entity
import dev.dprice.game.models.Vector3f

data class Camera2DComponent(
    override val entity: Entity,
    val localPosition: Vector3f,
    val target: Vector3f
) : Component