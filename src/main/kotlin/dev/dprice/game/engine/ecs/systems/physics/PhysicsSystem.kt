package dev.dprice.game.engine.ecs.systems.physics

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents

fun SystemRepository.createInputSystem() = registerSystem<InputComponent> {
    getComponents<PhysicsComponent>().forEach { physicsComponent ->
        val transformComponent = getComponent<TransformComponent>(physicsComponent)
            ?: error("No transform found") // todo: Skip

        transformComponent.position = transformComponent.position + physicsComponent.gravity
    }
}
