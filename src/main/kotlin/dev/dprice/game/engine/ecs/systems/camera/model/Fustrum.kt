package dev.dprice.game.engine.ecs.systems.camera.model

import dev.dprice.game.engine.graphics.util.orthographicMatrix
import dev.dprice.game.engine.model.Matrix4f

data class Fustrum(
    val width: Float,
    val height: Float,
    val depth: Float = 100f,
    val nearestDepth: Float = 0.1f
) {

    val orthographic: Matrix4f = orthographicMatrix(
        -(width / 2),
        (width / 2),
        -(height / 2),
        (height / 2),
        nearestDepth,
        depth
    )
}