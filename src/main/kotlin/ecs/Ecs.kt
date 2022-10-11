package ecs

import ecs.model.Component
import ecs.model.Entity
import ecs.model.System
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.SparseArray

data class ComponentCollection<T: Component>(
    val components: SparseArray<T> = SparseArray(),
)

object ECS : KoinComponent {

    private val systems: List<System> by lazy { getKoin().getAll() }
    private val components: List<ComponentCollection<Component>> by inject()
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