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
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.entities.navigation.NavigatorComponent
import org.koin.core.annotation.Single

object MoveUp : Input
object MoveDown : Input
object MoveLeft : Input
object MoveRight : Input

@Single
class Character(
    private val spawnPosition: Vector3f = Vector3f()
) : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity, position = spawnPosition))
        createComponent(entity, CharacterComponent(entity))
        createComponent(entity, NavigatorComponent(entity))
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