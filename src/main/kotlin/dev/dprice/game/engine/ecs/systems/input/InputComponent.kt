package dev.dprice.game.engine.ecs.systems.input

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.input.model.Input

data class InputComponent<T: Input>(
    override val entity: Entity,
    val acceptedInputs: MutableList<T>,
    val inputs: MutableList<Input> = mutableListOf(),
) : Component
