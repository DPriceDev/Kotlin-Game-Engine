package dev.dprice.game.systems.di

import dev.dprice.game.ecs.ComponentCollection
import dev.dprice.game.ecs.model.System
import dev.dprice.game.systems.sprite.SpriteComponent
import dev.dprice.game.systems.sprite.SpriteSystem
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import dev.dprice.game.systems.physics.PhysicsSystem
import dev.dprice.game.systems.transform.TransformComponent

val systemsModule = module {

    single(named<SpriteComponent>()) {  ComponentCollection<SpriteComponent>() }
    single { SpriteSystem(get(named<SpriteComponent>()), get(named<TransformComponent>()), get()) } bind System::class

    single(named<TransformComponent>()) { ComponentCollection<TransformComponent>() }
    single { PhysicsSystem(get(named<TransformComponent>())) } bind System::class
}