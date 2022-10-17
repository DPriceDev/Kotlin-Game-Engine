package dev.dprice.game.engine.ecs.systems.sound

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity

data class SoundComponent(
    override val entity: Entity,
    val path: String,
    val shouldPlay: Boolean = false,
    var isPlaying: Boolean = false
) : Component