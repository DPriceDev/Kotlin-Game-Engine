package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.input.model.Input
import org.koin.core.annotation.Single

object MoveUp : Input
object MoveDown : Input
object MoveLeft : Input
object MoveRight : Input

@Single
class Character : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(
            entity,
            SpriteComponent(
                entity,
                Texture.TileMap("/textures/charsheet.png", 16, 16, 0, 0),
                zDepth = 1f
            )
        )
        createComponent(
            entity,
            SpriteAnimatorComponent(
                entity,
                listOf(
                    0 to 0,
                    1 to 0,
                    2 to 0,
                    1 to 0
                ),
                0.1f
            )
        )
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