package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.ECS.createEntity
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.sound.SoundComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.entities.navigation.NavigatorComponent

data class MoveUp(val isPressed: Boolean) : Input
data class MoveDown(val isPressed: Boolean) : Input
data class MoveLeft(val isPressed: Boolean) : Input
data class MoveRight(val isPressed: Boolean) : Input

fun createCharacter(
    spawnPosition: Vector3f = Vector3f()
) = createEntity {
    registerComponent(TransformComponent(spawnPosition))
    registerComponent(CharacterComponent())
    registerComponent(NavigatorComponent())
    registerComponent(
        SpriteComponent(
            Texture.TileMap(
                "/textures/charsheet-two.png",
                15,
                15,
                0,
                0,
                1
            ),
            zDepth = 1f
        )
    )
    registerComponent(
        SpriteAnimatorComponent(
            0 to 0,
            1 to 0,
            2 to 0,
            1 to 0,
            timePerFrame = 0.1f,
        )
    )
    registerComponent(
        InputComponent(
            MoveUp::class,
            MoveDown::class,
            MoveLeft::class,
            MoveRight::class
        )
    )
    registerComponent(
        SoundComponent("sound/pacman_chomp.wav", shouldPlay = true)
    )
}