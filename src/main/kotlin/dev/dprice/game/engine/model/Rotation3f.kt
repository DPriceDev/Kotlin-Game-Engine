package dev.dprice.game.engine.model

import kotlin.math.PI

data class Rotation3f(
    var roll: Degree = Degree(0f),
    var pitch: Degree = Degree(0f),
    var yaw: Degree = Degree(0f)
)

@JvmInline
value class Radian(val value: Float)

@JvmInline
value class Degree(val value: Float)

fun Radian.toDegrees() = Degree(this.value * (180 / PI).toFloat())

fun Degree.toRadians() = Radian(this.value * (PI / 180).toFloat())