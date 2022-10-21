package dev.dprice.game.engine.ecs.model

import dev.dprice.game.engine.util.SparseArray
import kotlin.reflect.KClass

interface ComponentProvider {
    fun <T: Component> registerComponent(clazz: KClass<T>, component: T) : T

    fun <T: Component> getComponents(clazz: KClass<T>) : SparseArray<T>

    fun <T: Component> getComponent(clazz: KClass<T>, component: Component): T?
}

fun <T: Component> SparseArray<T>.get(component: Component) : T? {
    return getOrNull(component.entity.id)
}

inline fun <reified T: Component> ComponentProvider.registerComponent(component: T) : T {
    return registerComponent(T::class, component)
}

inline fun <reified T: Component> ComponentProvider.getComponents() : SparseArray<T> {
    return getComponents(T::class)
}

inline fun <reified T: Component> ComponentProvider.getComponent(component: Component) : T? {
    return getComponent(T::class, component)
}