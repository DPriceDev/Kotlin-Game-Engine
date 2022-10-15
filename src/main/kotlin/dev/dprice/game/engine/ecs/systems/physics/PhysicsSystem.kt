package dev.dprice.game.engine.ecs.systems.physics

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent

class PhysicsSystem(
    private val physicsComponents: ComponentCollection<PhysicsComponent>,
    private val transformComponents: ComponentCollection<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        physicsComponents.components.forEach { physicsComponent ->
            val transformComponent = transformComponents.components
                .getOrNull(physicsComponent.entity.id)
                ?: error("No transform found") // todo: Skip

            transformComponent.position = transformComponent.position + physicsComponent.gravity
        }
    }
}