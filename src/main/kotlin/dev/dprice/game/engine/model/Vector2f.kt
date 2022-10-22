package dev.dprice.game.engine.model

import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

data class Vector2f(
    var x: Float = 0f,
    var y: Float = 0f
) {
    operator fun get(index: Int) = arrayOf(x, y)[index]

    operator fun minus(other: Vector2f) = Vector2f(
        x = x - other.x,
        y = y - other.y
    )

    operator fun plus(other: Vector2f) = Vector2f(
        x = x + other.x,
        y = y + other.y
    )

    operator fun div(other: Vector2f) = Vector2f(
        x / other.x,
        y / other.y
    )

    operator fun times(other: Vector2f) = Vector2f(
        x * other.x,
        y * other.y
    )

    operator fun div(value: Float) = Vector2f(
        x / value,
        y / value
    )

    operator fun times(value: Float) = Vector2f(
        x * value,
        y * value
    )
}

/**
 * Length of the vector
 */
fun Vector2f.length() = sqrt(x * x + y * y)

/**
 * Get unit vector from vector
 */
fun Vector2f.normalize() = this / length()

fun Vector2f.crossProduct(other: Vector2f) = Vector2f(
    x = y * other.x - x * other.y,
    y = y * other.x - x * other.y
)

fun Vector2f.dotProduct(other: Vector2f) = x * other.x + y * other.y

fun Vector2f.negate() = this * -1f

fun Vector2f.lerpTo(target: Vector2f, amount: Float) : Vector2f {
    return this * (1f - amount) + target * amount
}

fun Vector2f.angleTo(target: Vector2f) : Degree {
    return Radian(acos(dotProduct(target) / (length() * target.length()))).toDegrees()
}

fun Vector2f.directionalAngleTo(target: Vector2f) : Degree {
    val angle = atan2(
        y - target.y,
        x - target.x
    )
    return Radian(angle).toDegrees()
}