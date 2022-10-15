package dev.dprice.game.engine.ecs.systems.camera.model

data class Fustrum(
    val width: Float,
    val height: Float,
    val depth: Float,
    val nearestDepth: Float = 0.1f
)