package dev.dprice.game.entities.enemy

import dev.dprice.game.engine.ecs.model.Component

data class EnemyComponent(
    var decisionTime: Float = 0f,
    val characterIndex: Int
) : Component()
