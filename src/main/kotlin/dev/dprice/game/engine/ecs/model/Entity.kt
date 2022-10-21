package dev.dprice.game.engine.ecs.model

import org.koin.core.component.KoinComponent

@JvmInline
value class Entity(val id: Int)

class EntityBuilder : KoinComponent {
    private val components: MutableList<ComponentProvider.(Entity) -> Component> = mutableListOf()

    fun registerComponentBuilder(builder: ComponentProvider.(Entity) -> Component) {
        components.add(builder)
    }

    inline fun <reified T: Component> registerComponent(component: T) : T {
        registerComponentBuilder { entity ->
            entity.apply {
                component.apply {
                    setEntity()
                }
            }
            registerComponent(component)
        }
        return component
    }

    fun ComponentProvider.build(entity: Entity) {
        components.forEach { it(entity) }
    }
}