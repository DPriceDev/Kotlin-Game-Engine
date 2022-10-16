package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class CharacterComponent(
    override val entity: Entity,
    var direction: Direction = Direction.RIGHT,
    val movementSpeed: Float = 60f
) : Component
