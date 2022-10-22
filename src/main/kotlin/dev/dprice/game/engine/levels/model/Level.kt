package dev.dprice.game.engine.levels.model

import dev.dprice.game.engine.levels.LevelLoader

data class Level(
    val name: String,
    val isStart: Boolean,
    val builder: LevelLoader.() -> Unit
)