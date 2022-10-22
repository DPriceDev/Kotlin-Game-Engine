package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.entities.character.Direction

data class NavigatorComponent(
    var direction: Direction = Direction.RIGHT,
    var currentNode: NavigatableComponent? = null,
    var targetNode: NavigatableComponent? = null,
    var availableDirections: List<Direction> = emptyList(),
    val canUseAISpaces: Boolean = false,
    val movementSpeed: Float = 50f // todo: Move to movement component?
) : Component()
