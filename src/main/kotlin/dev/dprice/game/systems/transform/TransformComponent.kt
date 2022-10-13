package dev.dprice.game.systems.transform

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.model.Rotation3f
import dev.dprice.game.engine.model.Vector3f

data class TransformComponent(
    override val entity: Entity,
    var position: Vector3f = Vector3f(0f, 0f),
    var scale: Vector3f = Vector3f(200f, 200f, 1f),
    var rotation: Rotation3f = Rotation3f()
) : Component