package dev.dprice.game.entities.level

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

data class MazeComponent(
    override val entity: Entity,
    var isMazeGenerated: Boolean = false
) : Component