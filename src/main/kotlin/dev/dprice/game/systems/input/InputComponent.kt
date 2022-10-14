package dev.dprice.game.systems.input

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.systems.input.model.Input

data class InputComponent<T: Input>(
    override val entity: Entity,
    val acceptedInputs: MutableList<T>,
    val inputs: MutableList<Input> = mutableListOf(),
) : Component
