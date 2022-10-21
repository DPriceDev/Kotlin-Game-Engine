package dev.dprice.game.engine.ecs.systems.physics

import dev.dprice.game.engine.ecs.model.SystemProvider
import dev.dprice.game.engine.ecs.model.registerSystem
import dev.dprice.game.engine.ecs.model.getComponents
import dev.dprice.game.engine.ecs.model.getComponent
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent

fun SystemProvider.createInputSystem() = registerSystem<InputComponent> {
    getComponents<PhysicsComponent>().forEach { physicsComponent ->
        val transformComponent = getComponent<TransformComponent>(physicsComponent)
            ?: error("No transform found") // todo: Skip

        transformComponent.position = transformComponent.position + physicsComponent.gravity
    }
}
