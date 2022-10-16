package dev.dprice.game.entities.tiles

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.entities.navigation.NavigatableComponent

class WalkableTile(
    private val position: Vector3f,
    private val xTileIndex: Int,
    private val yTileIndex: Int,
    private val canPlayerUse: Boolean = true
) : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, NavigatableComponent(entity, canPlayerUse))
        createComponent(
            entity,
            TransformComponent(entity, position = position, scale = Vector3f(1f, 1f))
        )
        createComponent(
            entity,
            SpriteComponent(
                entity,
                Texture.TileMap("/textures/spritesheet.png", 8, 8, xTileIndex, yTileIndex, 1)
            )
        )
    }
}