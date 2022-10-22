package dev.dprice.game.entities.level


import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.camera.model.Fustrum
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent

fun EntityInteractor.createMaze() = createEntity {
    registerComponent(TransformComponent())
    registerComponent(
        Camera2DComponent(fustrum = Fustrum(28 * 10f, 31 * 10f))
    )
    registerComponent(MazeComponent())
}