package dev.dprice.game.entities.character

import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.EntityCreator
import dev.dprice.game.engine.ecs.model.createComponent
import dev.dprice.game.systems.sprite.SpriteComponent
import dev.dprice.game.systems.transform.TransformComponent
import org.koin.core.annotation.Single

@Single
class Character : EntityCreator {

    override fun onCreate(entity: Entity) {
        createComponent(entity, TransformComponent(entity))
        createComponent(entity, SpriteComponent(entity))
    }
}