package systems.sprite

import ecs.ComponentCollection
import ecs.model.System
import systems.transform.TransformComponent

class SpriteSystem(
    private val sprites: ComponentCollection<SpriteComponent>,
    private val transforms: ComponentCollection<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        sprites.components.forEach {
            val transform = transforms.components.getOrNull(it.entity.id)
            val test = 1
        }
    }
}