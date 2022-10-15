package dev.dprice.game.engine

import dev.dprice.game.engine.ecs.ECS
import dev.dprice.game.engine.input.InputRepository
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.input.model.InputAction

class InputBuilder {
    private val mappings: MutableMap<InputAction, (InputAction) -> Input> = mutableMapOf()

    fun mapInputToAction(action: InputAction, inputConverter: (InputAction) -> Input) {
        mappings[action] = inputConverter
    }

    fun build(inputRepository: InputRepository) {
        mappings.toList().forEach { (input, action) ->
            inputRepository.addInputActionMapping(input, action)
        }
    }
}

class GameBuilder {
    private val inputBuilder = InputBuilder()

    fun ecs(builder: ECS.() -> Unit) {
        ECS.apply(builder)
    }

    fun input(builder: InputBuilder.() -> Unit) {
        inputBuilder.apply(builder)
    }

    fun build(ecs: ECS, inputRepository: InputRepository) {
        inputBuilder.build(inputRepository)
    }
}