package dev.dprice.game.engine.levels

import dev.dprice.game.engine.model.Level
import org.koin.core.annotation.Single

interface LevelRepository {
    fun registerLevel(level: Level)

    fun getStartLevel() : Level

    fun getLevel(name: String) : Level
}

@Single
class LevelRepositoryImpl : LevelRepository {
    private val levels: MutableList<Level> = mutableListOf()

    override fun registerLevel(level: Level) {
        levels.add(level)
    }

    override fun getLevel(name: String): Level {
        return levels.find { it.name == name } ?: error("Failed to find level $name")
    }

    override fun getStartLevel(): Level {
        return levels.find { it.isStart } ?: error("Failed to find starting level")
    }
}