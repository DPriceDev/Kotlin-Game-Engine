package dev.dprice.game.entities.character

import dev.dprice.game.ecs.ComponentCollection
import dev.dprice.game.ecs.model.Component
import dev.dprice.game.ecs.model.Entity
import dev.dprice.game.ecs.model.EntityCreator
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import dev.dprice.game.systems.sprite.SpriteComponent
import dev.dprice.game.systems.transform.Position
import dev.dprice.game.systems.transform.TransformComponent

class Character : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity, Position()))
        createComponent(entity, SpriteComponent(entity))
    }
}

inline fun <reified T : Component> EntityCreator.createComponent(entity: Entity, component: T) : T {
    val collection: ComponentCollection<T> by inject(named<T>())
    collection.components.add(entity.id, component)
    return component
}