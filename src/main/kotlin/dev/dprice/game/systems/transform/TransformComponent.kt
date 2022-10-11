package dev.dprice.game.systems.transform

import dev.dprice.game.ecs.model.Component
import dev.dprice.game.ecs.model.Entity

data class Position(
    var x: Int = 0,
    var y: Int = 0
)

data class TransformComponent(
    override val entity: Entity,
    var position: Position
) : Component