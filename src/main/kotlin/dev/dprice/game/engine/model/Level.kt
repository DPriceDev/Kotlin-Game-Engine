package dev.dprice.game.engine.model

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import dev.dprice.game.engine.levels.LevelRepository
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent

interface LevelLoader :
    SystemRepository,
    EntityInteractor,
    KoinComponent {

    fun loadLevel(level: Level)

    fun loadLevel(name: String)

    fun loadStartLevel()
}

@Single([LevelLoader::class])
class LevelLoaderImpl(
    private val levelRepository: LevelRepository,
    systemRepository: SystemRepository,
    entityInteractor: EntityInteractor
) : LevelLoader,
    SystemRepository by systemRepository,
    EntityInteractor by entityInteractor {

    override fun loadLevel(level: Level) {
        level.builder.invoke(this)
    }

    override fun loadStartLevel() {
        val level = levelRepository.getStartLevel()
        loadLevel(level)
    }

    override fun loadLevel(name: String) {
        val level = levelRepository.getLevel(name)
        loadLevel(level)
    }
}

data class Level(
    val name: String,
    val isStart: Boolean,
    val builder: LevelLoader.() -> Unit
)