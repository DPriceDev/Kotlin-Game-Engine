package dev.dprice.game.systems.pickups

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.systems.pickups.model.PickupType

data class PickupComponent(
    val pickupType: PickupType,
    var isCollected: Boolean = false
) : Component()
