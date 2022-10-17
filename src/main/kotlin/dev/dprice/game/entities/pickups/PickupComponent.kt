package dev.dprice.game.entities.pickups

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.entities.pickups.model.PickupType

data class PickupComponent(
    override val entity: Entity,
    val pickupType: PickupType,
    var isCollected: Boolean = false
) : Component
