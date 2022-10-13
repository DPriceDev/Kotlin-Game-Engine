package dev.dprice.game.engine.model

data class Vector4f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 0f
) {
    operator fun get(index: Int) = listOf(x, y, z, w)[index]
}