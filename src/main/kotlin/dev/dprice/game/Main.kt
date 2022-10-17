package dev.dprice.game

import dev.dprice.game.di.gameModule
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorSystem
import dev.dprice.game.engine.ecs.systems.input.InputSystem
import dev.dprice.game.engine.ecs.systems.sound.SoundSystem
import dev.dprice.game.engine.ecs.systems.sprite.SpriteSystem
import dev.dprice.game.engine.input.model.InputAction
import dev.dprice.game.engine.runGame
import dev.dprice.game.entities.character.*
import dev.dprice.game.entities.enemy.EnemySystem
import dev.dprice.game.entities.level.Maze
import dev.dprice.game.entities.level.MazeGeneratorSystem
import dev.dprice.game.entities.navigation.NavigationSystem
import dev.dprice.game.entities.pickups.PickupSystem
import org.lwjgl.glfw.GLFW.*

fun main(args: Array<String>) {
    runGame {

        koin {
            loadModules(listOf(gameModule))
        }

        ecs {
            registerSystem<InputSystem>()

            registerSystem<NavigationSystem>()
            registerSystem<MazeGeneratorSystem>()
            registerSystem<CharacterSystem>()
            registerSystem<EnemySystem>()
            registerSystem<PickupSystem>()
            registerSystem<SoundSystem>()

            registerSystem<SpriteSystem>()
            registerSystem<SpriteAnimatorSystem>()
            //todo: ordering systems? registerSystem<SpriteSystem>(after = InputSystem)

            createEntity(Maze()::onCreate)

            // todo: Create map generator entity

            // todo: Create enemy

            // todo: Create pickup generator?

            // todo: somehow signal start of game?
        }

        input {
            mapInputToAction(InputAction(GLFW_KEY_W, GLFW_PRESS)) { MoveUp }
            mapInputToAction(InputAction(GLFW_KEY_S, GLFW_PRESS)) { MoveDown }
            mapInputToAction(InputAction(GLFW_KEY_A, GLFW_PRESS)) { MoveLeft }
            mapInputToAction(InputAction(GLFW_KEY_D, GLFW_PRESS)) { MoveRight }
        }

// todo: Maybe register levels?
//        levels {
//            level("one", start = true) {
//                registerSystem<SpriteSystem>()
//                registerSystem<InputSystem>()
//                registerSystem<FollowCamera2DSystem>()
//                registerSystem<CharacterSystem>()
//
//                createEntity(Character()::onCreate)
//            }
//
//            level("two") {
//                registerSystem<SpriteSystem>()
//                registerSystem<InputSystem>()
//                registerSystem<FollowCamera2DSystem>()
//                registerSystem<CharacterSystem>()
//
//                createEntity(Character()::onCreate)
//            }
//        }

// todo: add window config
//        window {
//            title = ""
//            width = 800
//            height = 600
//        }
    }
}