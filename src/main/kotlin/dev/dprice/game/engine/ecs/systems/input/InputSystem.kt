package dev.dprice.game.engine.ecs.systems.input

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.input.InputRepository
import org.koin.core.component.inject
import dev.dprice.game.engine.ecs.getComponents

fun SystemRepository.createInputSystem() = registerSystem<InputComponent> {
    val inputRepository: InputRepository by inject()
    val inputs = inputRepository.getCurrentInputs() // todo: Could be grouped for quicker
    inputRepository.clearInputActions() // todo: Potentially could erase inputs by accident?

    getComponents<InputComponent>().forEach { component ->
        component.inputs.clear()
        component.inputs.addAll(
            inputs.filter { input ->
                component.acceptedInputs.any { inputClass -> input::class == inputClass }
            }
        )
    }
}