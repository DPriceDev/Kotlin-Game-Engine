package dev.dprice.game.ecs

import dev.dprice.game.ecs.model.Component
import dev.dprice.game.ecs.model.Entity
import dev.dprice.game.ecs.model.System
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import dev.dprice.game.util.SparseArray

data class ComponentCollection<T: Component>(
    val components: SparseArray<T> = SparseArray(),
)

// todo: Move to injectable object?
object ECS : KoinComponent {

    private val systems: List<System> by lazy { getKoin().getAll() }
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
}