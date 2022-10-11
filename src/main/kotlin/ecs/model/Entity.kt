package ecs.model

import org.koin.core.component.KoinComponent

@JvmInline
value class Entity(val id: Int)

interface EntityCreator: KoinComponent {

    fun onCreate(entity: Entity)
}