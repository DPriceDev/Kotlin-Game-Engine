package dev.dprice.game.entities.level

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.camera.model.Fustrum
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent

class Maze : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(entity, Camera2DComponent(entity, fustrum = Fustrum(28 * 10f, 31 * 10f)))
        createComponent(entity, MazeComponent(entity))
    }
}