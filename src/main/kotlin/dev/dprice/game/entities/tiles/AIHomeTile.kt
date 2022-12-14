package dev.dprice.game.entities.tiles

import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.entities.navigation.NavigatableComponent

fun EntityInteractor.createAIHomeTile(
    position: Vector3f = Vector3f(),
    xTileIndex: Int = 0,
    yTileIndex: Int = 0
) = createEntity {
    registerComponent(NavigatableComponent(false))
    registerComponent(
        TransformComponent(position = position, scale = Vector3f(1f, 1f))
    )
    registerComponent(
        SpriteComponent(
            Texture.TileMap("/textures/spritesheet.png", 8, 8, xTileIndex, yTileIndex, 1)
        )
    )
}
