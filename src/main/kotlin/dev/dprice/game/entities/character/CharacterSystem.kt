package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.systems.input.InputComponent
import dev.dprice.game.systems.input.model.Input
import dev.dprice.game.systems.transform.TransformComponent

class CharacterSystem(
    private val inputs: ComponentCollection<InputComponent<Input>>,
    private val transforms: ComponentCollection<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        inputs.components.forEach { component ->
            val transform = transforms.components.getOrNull(component.entity.id) ?: error("transform not found in physics")
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