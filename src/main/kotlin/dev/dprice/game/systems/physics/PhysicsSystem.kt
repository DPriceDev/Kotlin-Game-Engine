package dev.dprice.game.systems.physics

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.systems.transform.TransformComponent

class PhysicsSystem(
    private val components: ComponentCollection<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        components.components.forEach {
            it.position.x += (10f * timeSinceLast).toFloat()
            it.position.y += (10f * timeSinceLast).toFloat()
            //it.position.z += 0f
        }
    }
}