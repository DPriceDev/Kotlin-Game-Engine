package dev.dprice.game.engine

import dev.dprice.game.engine.input.InputRepository
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.input.model.InputAction
import dev.dprice.game.engine.levels.LevelRepository
import dev.dprice.game.engine.levels.model.Level
import dev.dprice.game.engine.levels.LevelLoader
import org.koin.core.KoinApplication

class InputBuilder {
    private val mappings: MutableMap<InputAction, (InputAction) -> Input> = mutableMapOf()

    fun mapInputToAction(action: InputAction, inputConverter: (InputAction) -> Input) {
        mappings[action] = inputConverter
    }

    fun build(inputRepository: InputRepository) {
        mappings.toList().forEach { (input, action) ->
            inputRepository.addInputActionMapping(input, action)
        }
    }
}

class GameBuilder {
    private val inputBuilder = InputBuilder()
    private var koinBuilder : KoinApplication.() -> Unit = { }
    private var levels : MutableList<Level> = mutableListOf()

    fun koin(builder: KoinApplication.() -> Unit) {
        koinBuilder = builder
    }

    fun level(
        name: String = "",
        isStart: Boolean = true,
        builder: LevelLoader.() -> Unit
    ) {
        require(levels.none { it.name == name })
        require(levels.none { it.isStart && isStart })

        levels.add(Level(name, isStart, builder))
    }

    fun input(builder: InputBuilder.() -> Unit) {
        inputBuilder.apply(builder)
    }

    fun build(
        levelRepository: LevelRepository,
        inputRepository: InputRepository,
        koin: KoinApplication
    ) {
        koin.apply(koinBuilder)

        levels.forEach(levelRepository::registerLevel)

        inputBuilder.build(inputRepository)
    }
}