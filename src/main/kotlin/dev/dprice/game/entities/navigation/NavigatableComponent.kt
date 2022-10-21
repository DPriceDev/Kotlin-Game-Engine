package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.Component

data class NavigatableComponent(
    val canPlayerUse: Boolean = true
) : Component()
