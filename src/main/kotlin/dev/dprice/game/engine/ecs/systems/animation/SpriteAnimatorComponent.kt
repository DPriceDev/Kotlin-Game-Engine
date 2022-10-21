package dev.dprice.game.engine.ecs.systems.animation

import dev.dprice.game.engine.ecs.model.Component

data class SpriteAnimatorComponent(
    var timePerFrame: Float,
    var currentFrameTime: Float = 0f,
    var currentTile: Int = 0,
    var tiles: List<Pair<Int, Int>>,
) : Component() {
    constructor(
        vararg tiles: Pair<Int, Int>,
        timePerFrame: Float,
        currentFrameTime: Float = 0f,
        currentTile: Int = 0
    ) : this(
        timePerFrame,
        currentFrameTime,
        currentTile,
        tiles.toList()
    )
}