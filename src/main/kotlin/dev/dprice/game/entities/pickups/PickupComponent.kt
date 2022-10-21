package dev.dprice.game.entities.pickups

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.entities.pickups.model.PickupType

data class PickupComponent(
    val pickupType: PickupType,
    var isCollected: Boolean = false
) : Component()
