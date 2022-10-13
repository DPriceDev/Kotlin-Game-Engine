package dev.dprice.game.engine.ecs.model

import dev.dprice.game.engine.ecs.ComponentCollection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@JvmInline
value class Entity(val id: Int)

interface EntityCreator: KoinComponent {

    fun onCreate(entity: Entity)
}

inline fun <reified T : Component> EntityCreator.createComponent(entity: Entity, component: T) : T {
    val collection: ComponentCollection<T> by inject(named<T>())
    collection.components.add(entity.id, component)
    return component
}