package dev.dprice.game.engine.ecs.systems.camera.model

data class Fustrum(
    val width: Float,
    val height: Float,
    val depth: Float = 100f,
    val nearestDepth: Float = 0.1f
)