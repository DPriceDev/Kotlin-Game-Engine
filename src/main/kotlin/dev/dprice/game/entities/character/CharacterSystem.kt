package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Degree
import dev.dprice.game.engine.model.Rotation3f
import dev.dprice.game.systems.navigation.NavigatorComponent
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents

fun SystemRepository.createCharacterSystem() = registerSystem<CharacterComponent> {

    getComponents<CharacterComponent>().forEach { character ->
        val transform = getComponent<TransformComponent>(character) ?: error("transform not found for character")
        val input = getComponent<InputComponent>(character) ?: error("input not found for character")
        val navigator = getComponent<NavigatorComponent>(character) ?: error("navigator not found in character")

        input.inputs.forEach {
            character.direction = when (it) {
                is MoveUp -> if (it.isPressed) Direction.UP else null
                is MoveDown -> if (it.isPressed) Direction.DOWN else null
                is MoveLeft -> if (it.isPressed) Direction.LEFT else null
                is MoveRight -> if (it.isPressed) Direction.RIGHT else null
                else -> navigator.direction
            }
        }

        navigator.direction = when {
            character.direction == Direction.UP && navigator.availableDirections.contains(Direction.UP) -> Direction.UP
            character.direction == Direction.DOWN && navigator.availableDirections.contains(Direction.DOWN) -> Direction.DOWN
            character.direction == Direction.LEFT && navigator.availableDirections.contains(Direction.LEFT) -> Direction.LEFT
            character.direction == Direction.RIGHT && navigator.availableDirections.contains(Direction.RIGHT) -> Direction.RIGHT
            else -> navigator.direction
        }

        val rotation = when (navigator.direction) {
            Direction.UP -> Rotation3f(yaw = Degree(90f))
            Direction.DOWN -> Rotation3f(yaw = Degree(-90f))
            Direction.LEFT -> Rotation3f(yaw = Degree(180f))
            Direction.RIGHT -> Rotation3f(yaw = Degree(0f))
        }

        transform.rotation = rotation
    }
}
