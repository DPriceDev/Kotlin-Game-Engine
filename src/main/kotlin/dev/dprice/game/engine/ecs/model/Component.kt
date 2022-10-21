package dev.dprice.game.engine.ecs.model

abstract class Component {
    private var mutableEntity: Entity? = null

    val entity: Entity
        get() = mutableEntity ?: error("Entity has not been set")

    fun Entity.setEntity() {
        this@Component.mutableEntity = this
    }
}
