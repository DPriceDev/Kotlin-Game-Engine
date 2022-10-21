package dev.dprice.game.engine.ecs.systems.camera

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.lerpTo
import dev.dprice.game.engine.model.negate
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents

fun SystemRepository.createFollowCamera2DSystem() = registerSystem<Camera2DComponent> { timeSinceLast ->

    getComponents<Camera2DComponent>().forEach { camera ->
        val transform = getComponent<TransformComponent>(camera)
            ?: error("cannot find transform for camera") // todo: maybe just skip?

        camera.target = camera.target
            .lerpTo(transform.position.negate(), (timeSinceLast * 2f).toFloat())
    }
}
