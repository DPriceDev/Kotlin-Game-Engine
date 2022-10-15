package dev.dprice.game.engine.ecs.systems.camera

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.ecs.systems.camera.model.Fustrum

data class Camera2DComponent(
    override val entity: Entity,
    var target: Vector3f = Vector3f(),
    var depth: Float = 50f,
    var fustrum: Fustrum = Fustrum(
        width = 800f,
        height = 600f,
        depth = 100f
    )
) : Component