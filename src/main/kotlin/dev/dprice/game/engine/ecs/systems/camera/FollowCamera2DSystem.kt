package dev.dprice.game.engine.ecs.systems.camera

import dev.dprice.game.engine.ecs.model.SystemProvider
import dev.dprice.game.engine.ecs.model.registerSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.lerpTo
import dev.dprice.game.engine.ecs.model.getComponents
import dev.dprice.game.engine.ecs.model.getComponent
import dev.dprice.game.engine.model.negate

fun SystemProvider.createFollowCamera2DSystem() = registerSystem<Camera2DComponent> { timeSinceLast ->

    getComponents<Camera2DComponent>().forEach { camera ->
        val transform = getComponent<TransformComponent>(camera)
            ?: error("cannot find transform for camera") // todo: maybe just skip?

        camera.target = camera.target
            .lerpTo(transform.position.negate(), (timeSinceLast * 2f).toFloat())
    }
}
