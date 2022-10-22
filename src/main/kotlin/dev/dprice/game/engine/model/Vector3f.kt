package dev.dprice.game.engine.model

import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

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

    operator fun div(other: Vector3f) = Vector3f(
        x / other.x,
        y / other.y,
        z / other.z
    )

    operator fun times(other: Vector3f) = Vector3f(
        x * other.x,
        y * other.y,
        z * other.z
    )

    operator fun div(value: Float) = Vector3f(
        x / value,
        y / value,
        z / value
    )

    operator fun times(value: Float) = Vector3f(
        x * value,
        y * value,
        z * value
    )
}

/**
 * Length of the vector
 */
fun Vector3f.length() = sqrt(x * x + y * y + z * z)

/**
 * Get unit vector from vector
 */
fun Vector3f.normalize() = this / length()

fun Vector3f.crossProduct(other: Vector3f) = Vector3f(
    x = y * other.z - z * other.y,
    y = z * other.x - x * other.z,
    z = x * other.y - y * other.x
)

fun Vector3f.dotProduct(other: Vector3f) = x * other.x + y * other.y + z * other.z

fun Vector3f.negate() = this * -1f

fun Vector3f.lerpTo(target: Vector3f, amount: Float) : Vector3f {
    return this * (1f - amount) + target * amount
}

fun Vector3f.angleTo(target: Vector3f) : Degree {
    return Radian(acos(dotProduct(target) / (length() * target.length()))).toDegrees()
}

fun Vector3f.directionalAngleTo(target: Vector3f) : Degree {
    val angle = atan2(
        y - target.y,
        x - target.x
    )
    return Radian(angle).toDegrees()
}