package entities.character

import ecs.ComponentCollection
import ecs.model.Component
import ecs.model.Entity
import ecs.model.EntityCreator
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import systems.sprite.SpriteComponent
import systems.transform.Position
import systems.transform.TransformComponent

class Character : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity, Position()))
        createComponent(entity, SpriteComponent(entity, 1))
    }
}

inline fun <reified T : Component> EntityCreator.createComponent(entity: Entity, component: T) : T {
    val collection: ComponentCollection<T> by inject(named<T>())
    collection.components.add(entity.id, component)
    return component
}