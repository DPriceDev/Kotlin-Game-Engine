package dev.dprice.game.engine.graphics.util

import dev.dprice.game.engine.model.Matrix4f

fun orthographicMatrix(
    left: Float,
    right: Float,
    bottom: Float,
    top: Float,
    near: Float,
    far: Float
) = Matrix4f(
    scale(right, left),   0f,                 0f,               translate(right, left),
    0f,                 scale(top, bottom),   0f,               translate(top, bottom),
    0f,                 0f,                 -scale(far, near),  translate(far, near),
    0f,                 0f,                 0f,                 1f
)

private fun translate(a: Float, b: Float) : Float = -(a + b) / (a - b)

private fun scale(a: Float, b: Float) : Float = 2 / (a - b)