package dev.dprice.game.engine.ecs.model

import dev.dprice.game.engine.ecs.ComponentRepository
import dev.dprice.game.engine.ecs.registerComponent
import org.koin.core.component.KoinComponent

@JvmInline
value class Entity(val id: Int)

class EntityBuilder : KoinComponent {
    private val components: MutableList<ComponentRepository.(Entity) -> Component> = mutableListOf()

    fun registerComponentBuilder(builder: ComponentRepository.(Entity) -> Component) {
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

    fun ComponentRepository.build(entity: Entity) {
        components.forEach { it(entity) }
    }
}