package dev.dprice.game.entities.tiles

import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector2f
import dev.dprice.game.engine.model.Vector3f

fun EntityInteractor.createWallTile(
    position: Vector3f = Vector3f(),
    xTileIndex: Int = 0,
    yTileIndex: Int = 0,
) = createEntity {

    registerComponent(
        TransformComponent(position = position, scale = Vector3f(1f, 1f))
    )
    registerComponent(
        SpriteComponent(
            size = Vector2f(8f, 8f),
            texture = Texture.TileMap(
                "/textures/spritesheet.png",
                8,
                8,
                80f,
                143f,
                xTileIndex,
                yTileIndex,
                1
            )
        )
    )
}
