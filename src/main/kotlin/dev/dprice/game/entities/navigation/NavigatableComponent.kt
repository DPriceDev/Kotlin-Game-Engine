package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.entities.character.Direction

data class NavigatableComponent(
    var canPlayerUse: Boolean = true,
    var isAttached: Boolean = false,
    var connectedNodes: Map<Direction, NavigatableComponent> = emptyMap(),
    var searchDistance: Float = 8.5f
) : Component()
