package dev.dprice.game.systems.camera

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.systems.transform.TransformComponent


class Camera2DSystem(
    private val transforms: ComponentCollection<TransformComponent>,
    private val cameras: ComponentCollection<Camera2DComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        cameras.components.forEach { camera ->
            val transform = transforms.components
                .getOrNull(camera.entity.id)
                ?: error("cannot find transform for camera") // todo: maybe just skip?

            // todo: Move camera to follow transform
            camera.target = transform.position.copy(
                x = camera.target.x + ((transform.position.x - camera.target.x) * timeSinceLast).toFloat(),
                y = -transform.position.y,
                z = -transform.position.z
            )
        }
    }
}