package dev.dprice.game.engine.input

import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.input.model.InputAction
import org.koin.core.annotation.Single

interface InputRepository {
    fun getCurrentInputs() : List<Input>

    fun addInputAction(input: InputAction)

    fun clearInputActions()

    fun addInputActionMapping(action: InputAction, inputConverter: (InputAction) -> Input)
}

@Single(binds = [InputRepository::class])
class InputRepositoryImpl : InputRepository {
    private var inputs: List<InputAction> = emptyList()

    private val mappings: MutableMap<InputAction, (InputAction) -> Input> = mutableMapOf()

    override fun addInputAction(input: InputAction) {
        if (mappings.containsKey(input)) {
            inputs = inputs.plusElement(input)
        }
    }

    override fun clearInputActions() {
        inputs = emptyList()
    }

    override fun addInputActionMapping(action: InputAction, inputConverter: (InputAction) -> Input) {
        mappings[action] = inputConverter
    }

    override fun getCurrentInputs(): List<Input> {
        return inputs.mapNotNull { mappings[it]?.invoke(it) }
    }
}