package dev.dprice.game.engine.ecs.model

interface EntityProvider {
    fun createEntity(builder: EntityBuilder.() -> Unit) : Entity

    fun deleteEntity(entity: Entity)
}