package systems.physics

import ecs.ComponentCollection
import ecs.model.System
import systems.transform.TransformComponent

class PhysicsSystem(
    private val components: ComponentCollection<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        components.components.forEach {
            it.position.x += 1
            it.position.y += 1
        }
    }
}