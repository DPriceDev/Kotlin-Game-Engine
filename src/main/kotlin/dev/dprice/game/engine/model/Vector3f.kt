package dev.dprice.game.engine.model

data class Vector3f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {
    operator fun get(index: Int) = listOf(x, y, z)[index]

    operator fun minus(other: Vector3f) = Vector3f(
        x = x - other.x,
        y = y - other.y,
        z = z - other.z
    )

    operator fun plus(other: Vector3f) = Vector3f(
        x = x + other.x,
        y = y + other.y,
        z = z + other.z
    )
}