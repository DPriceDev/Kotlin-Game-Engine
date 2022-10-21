package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.model.Entity
import org.koin.core.annotation.Single

interface EntityRepository {
    fun createEntity() : Entity

    fun removeEntity(entity: Entity)
}

@Single
class EntityRepositoryImpl : EntityRepository {
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
        if (entity.id == currentMaxEntity - 1) {
            currentMaxEntity--
        } else {
            returnedEntities.add(entity.id)
        }
    }
}