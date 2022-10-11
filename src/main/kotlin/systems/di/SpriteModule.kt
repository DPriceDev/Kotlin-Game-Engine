package systems.di

import ecs.ComponentCollection
import ecs.model.System
import systems.sprite.SpriteComponent
import systems.sprite.SpriteSystem
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import systems.physics.PhysicsSystem
import systems.transform.TransformComponent

val systemsModule = module {

    single(named<SpriteComponent>()) {  ComponentCollection<SpriteComponent>() }
    single { SpriteSystem(get(named<SpriteComponent>()), get(named<TransformComponent>())) } bind System::class

    single(named<TransformComponent>()) { ComponentCollection<TransformComponent>() }
    single { PhysicsSystem(get(named<TransformComponent>())) } bind System::class
}