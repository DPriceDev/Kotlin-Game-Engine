package dev.dprice.game.systems.input

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.systems.input.model.Input
import kotlin.reflect.KClass

data class InputComponent<T: Input>(
    override val entity: Entity,
    val acceptedInputs: MutableList<KClass<T>>,
    val inputs: MutableList<Input> = mutableListOf(),
) : Component
