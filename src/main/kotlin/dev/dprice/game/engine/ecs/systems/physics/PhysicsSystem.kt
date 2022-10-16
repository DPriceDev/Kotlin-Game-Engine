package dev.dprice.game.engine.ecs.systems.physics

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.util.SparseArray

class PhysicsSystem(
    private val physicsComponents: SparseArray<PhysicsComponent>,
    private val transformComponents: SparseArray<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        physicsComponents.forEach { physicsComponent ->
            val transformComponent = transformComponents.get(physicsComponent)
                ?: error("No transform found") // todo: Skip

            transformComponent.position = transformComponent.position + physicsComponent.gravity
        }
    }
}