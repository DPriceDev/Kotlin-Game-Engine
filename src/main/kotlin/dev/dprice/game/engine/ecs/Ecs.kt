package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.util.SparseArray
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

// todo: Replace with just the sparse array?
data class ComponentCollection<T: Component>(
    val components: SparseArray<T> = SparseArray(),
)

// todo: Move to injectable object?
object ECS : KoinComponent {

    // split out?
    private val systems: MutableList<System> = mutableListOf()

    // todo: Split out?
    private val components: List<ComponentCollection<Component>> by inject()

    // todo: Move to entity manager that just handles unused ids
    private val entities: MutableList<Entity> = mutableListOf()

    fun run(timeSinceLast: Double) {
        systems.forEach { it.run(timeSinceLast) }
    }

    fun createEntity(builder: (Entity) -> Unit) {
        val entity = Entity(1)

        builder(entity)

        entities.add(entity)
    }

    fun deleteEntity(id: Int) {
        deleteComponents(id)

        entities.removeIf { it.id == id }
    }

    private fun deleteComponents(id: Int) {
        components.forEach { collection ->
            collection.components.remove(id)
        }
    }

    fun addSystem(system: System) : ECS {
        systems.add(system)
        return this
    }

    fun <T: System> removeSystem(type: KClass<T>) : ECS {
        systems.removeIf { type.isInstance(it) }
        return this
    }
}

inline fun <reified T : System> ECS.registerSystem() : ECS {
    val system: T by inject()
    return addSystem(system = system)
}

inline fun <reified T : System> ECS.unregisterSystem() =  removeSystem(T::class)
