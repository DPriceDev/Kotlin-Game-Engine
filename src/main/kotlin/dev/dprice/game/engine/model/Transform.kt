package dev.dprice.game.engine.model

data class Transform(
    var position: Vector3f = Vector3f(0f, 0f),
    var scale: Vector3f = Vector3f(1f, 1f, 1f),
    var rotation: Rotation3f = Rotation3f()
)