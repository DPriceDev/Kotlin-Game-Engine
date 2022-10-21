package dev.dprice.game.engine.ecs.systems.physics

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.model.Vector3f

data class PhysicsComponent(
    val gravity: Vector3f = Vector3f(z = -10f)
) : Component()
