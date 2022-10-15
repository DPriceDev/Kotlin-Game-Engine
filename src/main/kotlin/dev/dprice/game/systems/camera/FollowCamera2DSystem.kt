package dev.dprice.game.systems.camera

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.model.lerpTo
import dev.dprice.game.engine.model.negate
import dev.dprice.game.systems.transform.TransformComponent


class FollowCamera2DSystem(
    private val transforms: ComponentCollection<TransformComponent>,
    private val cameras: ComponentCollection<Camera2DComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        cameras.components.forEach { camera ->
            val transform = transforms.components
                .getOrNull(camera.entity.id)
                ?: error("cannot find transform for camera") // todo: maybe just skip?

            camera.target = camera.target
                .lerpTo(transform.position.negate(), (timeSinceLast * 2f).toFloat())
        }
    }
}