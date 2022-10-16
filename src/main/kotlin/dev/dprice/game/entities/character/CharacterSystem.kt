package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.model.Degree
import dev.dprice.game.engine.model.Rotation3f
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.util.SparseArray

class CharacterSystem(
    private val characters: SparseArray<CharacterComponent>,
    private val inputs: SparseArray<InputComponent<Input>>,
    private val transforms: SparseArray<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        characters.forEach { character ->
            val transform = transforms.get(character) ?: error("transform not found for character")
            val input = inputs.get(character) ?: error("input not found for character")

            input.inputs.forEach {
                character.direction = when(it) {
                    MoveUp -> Direction.UP
                    MoveDown -> Direction.DOWN
                    MoveLeft -> Direction.LEFT
                    MoveRight -> Direction.RIGHT
                    else -> character.direction
                }
            }

            val (movement, rotation) = when(character.direction) {
                Direction.UP -> Vector3f(y = character.movementSpeed) to Rotation3f(yaw = Degree(90f))
                Direction.DOWN -> Vector3f(y = -character.movementSpeed) to Rotation3f(yaw = Degree(-90f))
                Direction.LEFT -> Vector3f(x = -character.movementSpeed) to Rotation3f(yaw = Degree(180f))
                Direction.RIGHT -> Vector3f(x = character.movementSpeed) to Rotation3f(yaw = Degree(0f))
            }

            transform.position = transform.position + (movement * timeSinceLast.toFloat())
            transform.rotation = rotation
        }
    }
}