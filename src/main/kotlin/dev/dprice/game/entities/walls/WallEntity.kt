package dev.dprice.game.entities.walls

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import org.koin.core.annotation.Single

@Single
class WallEntity : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity, scale = Vector3f(10f, 10f)))
        createComponent(
            entity,
            SpriteComponent(
                entity,
                Texture.TileMap("/textures/spritesheet.png", 8, 8, 1, 1, 1)
            )
        )
    }
}