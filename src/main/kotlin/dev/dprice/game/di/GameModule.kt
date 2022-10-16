package dev.dprice.game.di

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.enemy.EnemyComponent
import dev.dprice.game.entities.enemy.EnemySystem
import dev.dprice.game.entities.level.MazeComponent
import dev.dprice.game.entities.level.MazeGeneratorSystem
import dev.dprice.game.entities.navigation.NavigatableComponent
import dev.dprice.game.entities.navigation.NavigationSystem
import dev.dprice.game.entities.navigation.NavigatorComponent
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val gameModule = module {
    single(named<MazeComponent>()) { SparseArray<MazeComponent>() }
    single {
        MazeGeneratorSystem(get(named<MazeComponent>()), get(named<TransformComponent>()))
    } bind System::class

    single(named<NavigatorComponent>()) { SparseArray<NavigatorComponent>() }
    single(named<NavigatableComponent>()) { SparseArray<NavigatableComponent>() }
    single {
        NavigationSystem(
            get(named<NavigatorComponent>()),
            get(named<NavigatableComponent>()),
            get(named<TransformComponent>())
        )
    } bind System::class

    single(named<EnemyComponent>()) { SparseArray<EnemyComponent>() }

    single {
        EnemySystem(
            get(named<EnemyComponent>()),
            get(named<NavigatorComponent>()),
            get(named<SpriteAnimatorComponent>())
        )
    } bind System::class
}