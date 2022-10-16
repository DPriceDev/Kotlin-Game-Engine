package dev.dprice.game.engine.ecs.systems.animation

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

data class SpriteAnimatorComponent(
    override val entity: Entity,
    var tiles: List<Pair<Int, Int>>,
    var timePerFrame: Float,
    var currentFrameTime: Float = 0f,
    var currentTile: Int = 0
) : Component