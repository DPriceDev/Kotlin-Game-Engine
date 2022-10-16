package dev.dprice.game.entities.enemy

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

data class EnemyComponent(
    override val entity: Entity,
    var decisionTime: Float = 0f,
    val characterIndex: Int
) : Component
