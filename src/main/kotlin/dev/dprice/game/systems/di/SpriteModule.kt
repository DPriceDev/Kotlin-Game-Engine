package dev.dprice.game.systems.di

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.systems.camera.Camera2DComponent
import dev.dprice.game.systems.sprite.SpriteComponent
import dev.dprice.game.systems.sprite.SpriteSystem
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import dev.dprice.game.systems.physics.PhysicsSystem
import dev.dprice.game.systems.transform.TransformComponent

val systemsModule = module {

    single(named<Camera2DComponent>()) {  ComponentCollection<Camera2DComponent>() }


    single(named<SpriteComponent>()) {  ComponentCollection<SpriteComponent>() }
    single {
        SpriteSystem(
            get(named<SpriteComponent>()),
            get(named<TransformComponent>()),
            get(named<Camera2DComponent>()),
            get()
        )
    } bind System::class

    single(named<TransformComponent>()) { ComponentCollection<TransformComponent>() }
    single { PhysicsSystem(get(named<TransformComponent>())) } bind System::class
}