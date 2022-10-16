package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.util.SparseArray

class CharacterSystem(
    private val inputs: SparseArray<InputComponent<Input>>,
    private val transforms: SparseArray<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        inputs.forEach { component ->
            val transform = transforms.get(component) ?: error("transform not found in physics")
            component.inputs.forEach {
                transform.position = when(it) {
                    MoveUp -> transform.position + Vector3f(y = 10f)
                    MoveDown -> transform.position + Vector3f(y = -10f)
                    MoveLeft -> transform.position + Vector3f(x = -10f)
                    MoveRight -> transform.position + Vector3f(x = 10f)
                    else -> transform.position
                }
            }
        }
    }
}