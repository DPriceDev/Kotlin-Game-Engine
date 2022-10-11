package systems.transform

import ecs.model.Component
import ecs.model.Entity

data class Position(
    var x: Int = 0,
    var y: Int = 0
)

data class TransformComponent(
    override val entity: Entity,
    var position: Position
) : Component