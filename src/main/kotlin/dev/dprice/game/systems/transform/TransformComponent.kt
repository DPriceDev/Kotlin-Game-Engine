package dev.dprice.game.systems.transform

import dev.dprice.game.ecs.model.Component
import dev.dprice.game.ecs.model.Entity
import dev.dprice.game.models.Rotation3f
import dev.dprice.game.models.Vector3f

data class TransformComponent(
    override val entity: Entity,
    var position: Vector3f = Vector3f(),
    var scale: Vector3f = Vector3f(),
    var rotation: Rotation3f = Rotation3f()
) : Component