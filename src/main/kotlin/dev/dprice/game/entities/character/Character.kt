package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.systems.camera.Camera2DComponent
import dev.dprice.game.systems.input.InputComponent
import dev.dprice.game.systems.input.model.Input
import dev.dprice.game.systems.sprite.SpriteComponent
import dev.dprice.game.systems.transform.TransformComponent
import org.koin.core.annotation.Single

object MoveUp : Input
object MoveDown : Input
object MoveLeft : Input
object MoveRight : Input

@Single
class Character : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(entity, SpriteComponent(entity))
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