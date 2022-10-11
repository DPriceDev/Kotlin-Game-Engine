package systems.sprite

import ecs.ComponentCollection
import ecs.model.Component
import ecs.model.Entity
import systems.transform.TransformComponent

class SpriteComponent(
    override val entity: Entity,
    val imageId: Int
) : Component