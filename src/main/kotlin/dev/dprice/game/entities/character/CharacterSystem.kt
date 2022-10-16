package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.model.Degree
import dev.dprice.game.engine.model.Rotation3f
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.navigation.NavigatorComponent

class CharacterSystem(
    private val characters: SparseArray<CharacterComponent>,
    private val inputs: SparseArray<InputComponent<Input>>,
    private val transforms: SparseArray<TransformComponent>,
    private val navigators: SparseArray<NavigatorComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        characters.forEach { character ->
            val transform = transforms.get(character) ?: error("transform not found for character")
            val input = inputs.get(character) ?: error("input not found for character")
            val navigator = navigators.get(character) ?: error("navigator not found in character")

            input.inputs.forEach {
                character.direction = when {
                    it == MoveUp && navigator.availableDirections.contains(Direction.UP) -> Direction.UP
                    it == MoveDown && navigator.availableDirections.contains(Direction.DOWN) -> Direction.DOWN
                    it == MoveLeft && navigator.availableDirections.contains(Direction.LEFT) -> Direction.LEFT
                    it == MoveRight && navigator.availableDirections.contains(Direction.RIGHT) -> Direction.RIGHT
                    else -> character.direction
                }
            }

            val rotation = when(character.direction) {
                Direction.UP -> Rotation3f(yaw = Degree(90f))
                Direction.DOWN -> Rotation3f(yaw = Degree(-90f))
                Direction.LEFT -> Rotation3f(yaw = Degree(180f))
                Direction.RIGHT -> Rotation3f(yaw = Degree(0f))
            }

            transform.rotation = rotation
            navigator.direction = character.direction
        }
    }
}