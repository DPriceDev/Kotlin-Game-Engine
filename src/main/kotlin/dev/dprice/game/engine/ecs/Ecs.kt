package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.model.*
import dev.dprice.game.engine.util.SparseArray
import org.koin.core.Koin
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

@Single
class ComponentRepositoryImpl : ComponentProvider {
    private var components: MutableMap<KClass<*>, SparseArray<Component>> = mutableMapOf()

    override fun <T : Component> getComponents(clazz: KClass<T>): SparseArray<T> {
        return components
            .filter { it.key == clazz }
            .values
            .filterIsInstance<SparseArray<T>>()
            .firstOrNull()
            ?: createNewComponentArray(clazz)
    }

    private fun <T: Component> createNewComponentArray(clazz: KClass<T>): SparseArray<T> {
        val array = SparseArray<T>()
        components[clazz] = (array as SparseArray<Component>)
        return array
    }

    override fun <T : Component> getComponent(clazz: KClass<T>, component: Component) = getComponents(clazz).get(component)

    override fun <T : Component> registerComponent(clazz: KClass<T>, component: T): T {
        getComponents(clazz).apply {
            add(component.entity.id, component)
        }
        return component
    }
}

// todo: Move to injectable object?
object ECS : KoinComponent, EntityProvider, SystemProvider, System /* todo: Switch to class that takes ecs? */ {

    // split out?
    private val systems: MutableMap<String, System.(Double) -> Unit> = mutableMapOf()

    // todo: Split out?
    private val components: List<SparseArray<Component>> by lazy { Koin().getAll() }

    private val entityManager: EntityManager by inject()

    private val componentProvider: ComponentProvider by inject()

    fun run(timeSinceLast: Double) {
        systems.values.forEach { system ->
            system.invoke(this, timeSinceLast)
        }
    }

    override fun createEntity(builder: EntityBuilder.() -> Unit): Entity {
        val entity = entityManager.createEntity()

        EntityBuilder().apply {
            builder()
            componentProvider.apply {
                build(entity)
            }
        }

        return entity
    }

    override fun deleteEntity(entity: Entity) {
        deleteComponents(entity)
        entityManager.removeEntity(entity)
    }

    override fun <T : Component> registerComponent(clazz: KClass<T>, component: T): T {
        return componentProvider.registerComponent(clazz, component)
    }

    private fun deleteComponents(entity: Entity) {
        components.forEach { collection ->
            collection.remove(entity.id)
        }
    }

    override fun registerSystem(id: String, runner: System.(delta: Double) -> Unit) {
        systems[id] = runner
    }

    override fun unregisterSystem(id: String) {
        systems.remove(id)
    }

    override fun <T : Component> getComponent(clazz: KClass<T>, component: Component): T? {
        return componentProvider.getComponent(clazz, component)
    }

    override fun <T: Component> getComponents(clazz: KClass<T>) : SparseArray<T> {
        return componentProvider.getComponents(clazz)
    }
}