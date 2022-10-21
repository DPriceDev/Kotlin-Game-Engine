package dev.dprice.game.entities.level

import dev.dprice.game.engine.ecs.model.Component

data class MazeComponent(
    var isMazeGenerated: Boolean = false
) : Component()