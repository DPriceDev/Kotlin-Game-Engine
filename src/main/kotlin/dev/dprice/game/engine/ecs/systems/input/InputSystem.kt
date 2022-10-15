package dev.dprice.game.engine.ecs.systems.input

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.input.InputRepository

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
                    component.acceptedInputs.any { inputClass -> input::class == inputClass::class }
                }
            )
        }
    }
}