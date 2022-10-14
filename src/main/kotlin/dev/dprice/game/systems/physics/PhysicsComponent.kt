package dev.dprice.game.systems.physics

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.model.Vector3f

data class PhysicsComponent(
    override val entity: Entity,
    val gravity: Vector3f = Vector3f(z = -10f)
) : Component
