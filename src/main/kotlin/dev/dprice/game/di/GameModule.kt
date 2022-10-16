package dev.dprice.game.di

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.level.LevelComponent
import dev.dprice.game.entities.level.MazeGeneratorSystem
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val gameModule = module {
    single(named<LevelComponent>()) { SparseArray<LevelComponent>() }
    single {
        MazeGeneratorSystem(get(named<LevelComponent>()), get(named<TransformComponent>()))
    } bind System::class
}