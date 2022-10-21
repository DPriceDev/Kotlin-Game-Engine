package dev.dprice.game.engine.ecs.interactors

import dev.dprice.game.engine.ecs.ComponentRepository
import dev.dprice.game.engine.ecs.EntityRepository
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityBuilder
import org.koin.core.annotation.Single

interface EntityInteractor {
    fun createEntity(builder: EntityBuilder.() -> Unit) : Entity

    fun deleteEntity(entity: Entity)
}

@Single
class EntityInteractorImpl(
    private val componentRepository: ComponentRepository,
    private val entityRepository: EntityRepository
) : EntityInteractor {

    override fun createEntity(builder: EntityBuilder.() -> Unit): Entity {
        val entity = entityRepository.createEntity()

        EntityBuilder().apply {
            builder()
            componentRepository.apply {
                build(entity)
            }
        }

        return entity
    }

    override fun deleteEntity(entity: Entity) {
        componentRepository.getAllComponents().forEach {
            it.remove(entity.id)
        }
        entityRepository.removeEntity(entity)
    }
}

