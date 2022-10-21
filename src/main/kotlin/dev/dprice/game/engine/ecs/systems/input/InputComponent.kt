package dev.dprice.game.engine.ecs.systems.input

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.input.model.Input
import kotlin.reflect.KClass

data class InputComponent(
    val acceptedInputs: MutableList<KClass<*>>,
    val inputs: MutableList<Input> = mutableListOf(),
) : Component() {
    constructor(
        vararg acceptedInputs: KClass<*>,
        inputs: MutableList<Input> = mutableListOf()
    ) : this(acceptedInputs.toMutableList(), inputs)
}