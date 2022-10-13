package dev.dprice.game.entities.camera

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.systems.camera.Camera2DComponent
import dev.dprice.game.systems.transform.TransformComponent

class Camera2D : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(entity, Camera2DComponent(entity, Vector3f(z = 10f), Vector3f()))
    }
}