package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.util.SparseArray
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

interface EntityManager {
    fun createEntity() : Entity

    fun removeEntity(entity: Entity)
}

@Single
class EntityManagerImpl : EntityManager {
    private var currentMaxEntity = 0
    private val returnedEntities = mutableSetOf<Int>()

    override fun createEntity(): Entity {
        return if (returnedEntities.isEmpty()) {
            currentMaxEntity++
            Entity(currentMaxEntity - 1)
        } else {
            val id = returnedEntities.first()
            returnedEntities.remove(id)
            Entity(id)
        }
    }

    override fun removeEntity(entity: Entity) {
        if (entity.id == currentMaxEntity) {
            currentMaxEntity--
        } else {
            returnedEntities.add(entity.id)
        }
    }
}

// todo: Move to injectable object?
object ECS : KoinComponent {

    // split out?
    private val systems: MutableList<System> = mutableListOf()

    // todo: Split out?
    private val components: List<SparseArray<Component>> by inject()

    private val entityManager: EntityManager by inject()

    fun run(timeSinceLast: Double) {
        systems.forEach { it.run(timeSinceLast) }
    }

    // todo: Switch to a dsl
    fun createEntity(builder: (Entity) -> Unit) {
        val entity = entityManager.createEntity()

        builder(entity)
    }


    fun deleteEntity(entity: Entity) {
        deleteComponents(entity.id)
        entityManager.removeEntity(entity)
    }

    private fun deleteComponents(id: Int) {
        components.forEach { collection ->
            collection.remove(id)
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
