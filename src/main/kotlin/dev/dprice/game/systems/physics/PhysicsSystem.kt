package dev.dprice.game.systems.physics

import dev.dprice.game.ecs.ComponentCollection
import dev.dprice.game.ecs.model.System
import dev.dprice.game.systems.transform.TransformComponent

class PhysicsSystem(
    private val components: ComponentCollection<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        components.components.forEach {
            it.position.x += 1f
            it.position.y += 1f
        }
    }
}