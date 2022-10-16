package dev.dprice.game.engine.ecs.systems.camera

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.lerpTo
import dev.dprice.game.engine.model.negate
import dev.dprice.game.engine.util.SparseArray


class FollowCamera2DSystem(
    private val transforms: SparseArray<TransformComponent>,
    private val cameras: SparseArray<Camera2DComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        cameras.forEach { camera ->
            val transform = transforms
                .getOrNull(camera.entity.id)
                ?: error("cannot find transform for camera") // todo: maybe just skip?

            camera.target = camera.target
                .lerpTo(transform.position.negate(), (timeSinceLast * 2f).toFloat())
        }
    }
}