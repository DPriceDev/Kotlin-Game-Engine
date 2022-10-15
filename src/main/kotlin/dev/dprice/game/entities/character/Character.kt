package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import org.koin.core.annotation.Single

object MoveUp : Input
object MoveDown : Input
object MoveLeft : Input
object MoveRight : Input

@Single
class Character : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(entity, SpriteComponent(entity, "/textures/container.jpeg"))
        createComponent(entity, Camera2DComponent(entity))
        createComponent(entity,
            InputComponent(
                entity,
                mutableListOf(
                    MoveUp,
                    MoveDown,
                    MoveLeft,
                    MoveRight
                )
            )
        )
    }
}