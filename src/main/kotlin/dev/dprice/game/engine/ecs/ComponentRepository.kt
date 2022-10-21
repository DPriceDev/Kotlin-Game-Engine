package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.util.SparseArray
import org.koin.core.annotation.Single
import kotlin.reflect.KClass

interface ComponentRepository {
    fun <T: Component> registerComponent(clazz: KClass<T>, component: T) : T

    fun <T: Component> getComponents(clazz: KClass<T>) : SparseArray<T>

    fun getAllComponents() : List<SparseArray<Component>>

    fun <T: Component> getComponent(clazz: KClass<T>, component: Component): T?
}

fun <T: Component> SparseArray<T>.get(component: Component) : T? {
    return getOrNull(component.entity.id)
}

inline fun <reified T: Component> ComponentRepository.registerComponent(component: T) : T {
    return registerComponent(T::class, component)
}

inline fun <reified T: Component> ComponentRepository.getComponents() : SparseArray<T> {
    return getComponents(T::class)
}

inline fun <reified T: Component> ComponentRepository.getComponent(component: Component) : T? {
    return getComponent(T::class, component)
}

@Single
class ComponentRepositoryImpl : ComponentRepository {
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

    override fun getAllComponents() = components.values.toList()

    override fun <T : Component> registerComponent(clazz: KClass<T>, component: T): T {
        getComponents(clazz).apply {
            add(component.entity.id, component)
        }
        return component
    }
}