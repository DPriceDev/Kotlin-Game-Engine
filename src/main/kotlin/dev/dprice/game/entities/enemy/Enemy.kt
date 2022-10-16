package dev.dprice.game.entities.enemy

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.entities.navigation.NavigatorComponent

class Enemy(
    private val spawnPosition: Vector3f = Vector3f(),
    private val characterIndex: Int = 4
) : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity, position = spawnPosition))
        createComponent(entity, EnemyComponent(entity, characterIndex = characterIndex))
        createComponent(entity, NavigatorComponent(entity, canUseAISpaces = true))
        createComponent(
            entity,
            SpriteComponent(
                entity,
                Texture.TileMap("/textures/charsheet-two.png", 15, 15, 0, 4,1),
                zDepth = 1f
            )
        )
        createComponent(
            entity,
            SpriteAnimatorComponent(
                entity,
                listOf(
                    0 to characterIndex,
                    1 to characterIndex,
                ),
                0.1f
            )
        )
    }
}