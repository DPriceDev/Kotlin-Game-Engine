package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

data class NavigatableComponent(
    override val entity: Entity,
    val canPlayerUse: Boolean = true
) : Component
