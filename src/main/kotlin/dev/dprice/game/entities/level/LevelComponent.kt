package dev.dprice.game.entities.level

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

data class LevelComponent(
    override val entity: Entity,
    var isLevelGenerated: Boolean = false
) : Component