package dev.dprice.game.engine.ecs.systems.camera

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.systems.camera.model.Fustrum
import dev.dprice.game.engine.model.Vector3f

data class Camera2DComponent(
    var target: Vector3f = Vector3f(),
    var depth: Float = 50f,
    var fustrum: Fustrum = Fustrum(
        width = 800f,
        height = 600f,
        depth = 100f
    )
) : Component()