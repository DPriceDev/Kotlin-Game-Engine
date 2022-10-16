package dev.dprice.game.engine

import dev.dprice.game.engine.ecs.ECS
import dev.dprice.game.engine.input.InputRepository
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.input.model.InputAction
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext

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

class GameBuilder : KoinComponent {
    private val inputBuilder = InputBuilder()
    private var koinBuilder : Koin.() -> Unit = { }
    private var ecsBuilder : ECS.() -> Unit = { }

    fun koin(builder: Koin.() -> Unit) {
        koinBuilder = builder
    }

    fun ecs(builder: ECS.() -> Unit) {
        ecsBuilder = builder
    }

    fun input(builder: InputBuilder.() -> Unit) {
        inputBuilder.apply(builder)
    }

    fun build() {
        GlobalContext.get().apply(koinBuilder)
        ECS.apply(ecsBuilder)

        val inputRepository: InputRepository by inject()
        inputBuilder.build(inputRepository)
    }
}