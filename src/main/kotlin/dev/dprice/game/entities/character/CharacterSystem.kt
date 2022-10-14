package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.entities.camera.TestInput
import dev.dprice.game.systems.input.InputComponent
import dev.dprice.game.systems.input.model.Input

class CharacterSystem(
    private val inputs: ComponentCollection<InputComponent<Input>>
) : System {

    override fun run(timeSinceLast: Double) {
        inputs.components.forEach { component ->
            component.inputs.forEach {
                if (it is TestInput) {
                    println("DAVID TEST")
                }
            }
        }
    }
}