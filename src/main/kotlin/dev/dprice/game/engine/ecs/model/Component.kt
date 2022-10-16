package dev.dprice.game.engine.ecs.model

import dev.dprice.game.engine.util.SparseArray

interface Component {
    val entity: Entity
}

// todo: move?
fun <T: Component> SparseArray<T>.get(component: Component) : T? {
    return getOrNull(component.entity.id)
}