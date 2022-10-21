package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.Component

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class CharacterComponent(
    var direction: Direction? = null,
    val movementSpeed: Float = 60f
) : Component()
