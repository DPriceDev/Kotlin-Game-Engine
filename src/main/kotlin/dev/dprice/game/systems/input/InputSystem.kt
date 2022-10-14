package dev.dprice.game.systems.input

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.systems.input.model.Input

class InputSystem(
    private val inputComponents: ComponentCollection<InputComponent<Input>>,
    private val inputRepository: InputRepository
) : System {

    override fun run(timeSinceLast: Double) {
        val inputs = inputRepository.getCurrentInputs() // todo: Could be grouped for quicker
        inputRepository.clearInputActions() // todo: Potentially could erase inputs by accident?

        inputComponents.components.forEach { component ->
            component.inputs.clear()
            component.inputs.addAll(
                inputs.filter { input ->
                    component.acceptedInputs.any { inputClass ->
                        input::class == inputClass
                    }
                }
            )
        }
    }
}