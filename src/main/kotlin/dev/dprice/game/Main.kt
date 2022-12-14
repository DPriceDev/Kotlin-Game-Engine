package dev.dprice.game

import dev.dprice.game.engine.ecs.systems.animation.createSpriteAnimatorSystem
import dev.dprice.game.engine.ecs.systems.input.createInputSystem
import dev.dprice.game.engine.ecs.systems.sound.createSoundSystem
import dev.dprice.game.engine.ecs.systems.sprite.createSpriteSystem
import dev.dprice.game.engine.input.model.InputAction
import dev.dprice.game.engine.runGame
import dev.dprice.game.entities.character.*
import dev.dprice.game.entities.enemy.createEnemySystem
import dev.dprice.game.entities.level.createMaze
import dev.dprice.game.entities.level.createMazeGeneratorSystem
import dev.dprice.game.entities.navigation.createNavigationGridSystem
import dev.dprice.game.entities.navigation.createNavigationSystem
import dev.dprice.game.entities.pickups.createPickUpSystem
import org.lwjgl.glfw.GLFW.*

fun main(args: Array<String>) = runGame {

    level {
        createInputSystem()
        createNavigationGridSystem()

        createNavigationSystem()
        createMazeGeneratorSystem()
        createCharacterSystem()
        createEnemySystem()

        createPickUpSystem()
        createSoundSystem()

        createSpriteAnimatorSystem()
        createSpriteSystem()
        //todo: ordering systems? registerSystem<SpriteSystem>(after = InputSystem)

        createMaze()
    }

    input {
//            mapInputToAxis(
//                MoveUpAxis,
//                InputAction(GLFW_KEY_W, GLFW_PRESS) to { 1f },
//                InputAction(GLFW_KEY_S, GLFW_PRESS) to { -1f },
//            )

//            mapInputToAxis(
//                MoveRightAxis,
//                GLFW_KEY_D to { 1f },
//                GLFW_KEY_A to { -1f },
//            )

        mapInputToAction(InputAction(GLFW_KEY_W, GLFW_PRESS)) { MoveUp(true) }
        mapInputToAction(InputAction(GLFW_KEY_W, GLFW_RELEASE)) { MoveUp(false) }
        mapInputToAction(InputAction(GLFW_KEY_S, GLFW_PRESS)) { MoveDown(true) }
        mapInputToAction(InputAction(GLFW_KEY_S, GLFW_RELEASE)) { MoveDown(false) }
        mapInputToAction(InputAction(GLFW_KEY_A, GLFW_PRESS)) { MoveLeft(true) }
        mapInputToAction(InputAction(GLFW_KEY_A, GLFW_RELEASE)) { MoveLeft(false) }
        mapInputToAction(InputAction(GLFW_KEY_D, GLFW_PRESS)) { MoveRight(true) }
        mapInputToAction(InputAction(GLFW_KEY_D, GLFW_RELEASE)) { MoveRight(false) }
    }

// todo: add window config
//        window {
//            title = ""
//            width = 800
//            height = 600
//        }
}