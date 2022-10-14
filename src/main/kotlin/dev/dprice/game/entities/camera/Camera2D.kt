package dev.dprice.game.entities.camera

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.systems.camera.Camera2DComponent
import dev.dprice.game.systems.input.InputComponent
import dev.dprice.game.systems.input.model.Input
import dev.dprice.game.systems.transform.TransformComponent

object TestInput : Input

class Camera2D : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(entity, Camera2DComponent(entity))
        createComponent(entity,
            InputComponent(entity, mutableListOf(TestInput::class))
        )
    }
}