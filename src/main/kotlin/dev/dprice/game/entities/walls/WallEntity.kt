package dev.dprice.game.entities.walls

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import org.koin.core.annotation.Single

@Single
class WallEntity : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(
            entity,
            SpriteComponent(
                entity,
                Texture.TileMap("/textures/pacman-spritesheet.png", 16, 16, 0)
            )
        )
    }
}