package dev.dprice.game.engine.model

data class Transform3f(
    var position: Vector3f = Vector3f(0f, 0f),
    var scale: Vector3f = Vector3f(1f, 1f, 1f),
    var rotation: Rotation3f = Rotation3f()
)

fun Transform3f.asMatrix4f(): Matrix4f {
    return Matrix4f(
        arrayOf(
            scale.x, 0f, 0f, position.x,
            0f, scale.y, 0f, position.y,
            0f, 0f, scale.z, position.z,
            0f, 0f, 0f, 1f
        )
    ).rotate(rotation)
}