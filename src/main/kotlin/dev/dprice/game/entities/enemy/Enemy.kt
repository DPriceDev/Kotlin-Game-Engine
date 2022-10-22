package dev.dprice.game.entities.enemy

import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector2f
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.systems.navigation.NavigatorComponent

fun EntityInteractor.createEnemy(
    spawnPosition: Vector3f = Vector3f(),
    characterIndex: Int = 4
) = createEntity {

    registerComponent(TransformComponent(position = spawnPosition))
    registerComponent(EnemyComponent(characterIndex = characterIndex))
    registerComponent(NavigatorComponent(canUseAISpaces = true))
    registerComponent(
        SpriteComponent(
            size = Vector2f(15f, 15f),
            texture = Texture.TileMap(
                "/textures/charsheet-two.png",
                15,
                15,
                248f,
                224f,
                0,
                4,
                1
            ), zDepth = 1f
        )
    )
    registerComponent(
        SpriteAnimatorComponent(
            0 to characterIndex,
            1 to characterIndex,
            timePerFrame = 0.1f
        )
    )
}
