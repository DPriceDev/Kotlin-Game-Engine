package dev.dprice.game.engine.ecs.systems.sound

import dev.dprice.game.engine.ecs.model.Component

data class SoundComponent(
    val path: String,
    val shouldPlay: Boolean = false,
    var isPlaying: Boolean = false
) : Component()