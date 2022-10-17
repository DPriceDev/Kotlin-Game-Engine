package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.entities.character.Direction

data class NavigatorComponent(
    override val entity: Entity,
    var direction: Direction = Direction.RIGHT,
    var availableDirections: List<Direction> = emptyList(),
    var searchDistance: Float = 8.5f,
    val canUseAISpaces: Boolean = false,
    val movementSpeed: Float = 50f
) : Component
