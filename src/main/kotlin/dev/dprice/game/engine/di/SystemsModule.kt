package dev.dprice.game.engine.di

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.entities.character.CharacterSystem
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.camera.FollowCamera2DSystem
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.input.InputSystem
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.ecs.systems.physics.PhysicsComponent
import dev.dprice.game.engine.ecs.systems.physics.PhysicsSystem
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val componentsModule = module {
    single(named<Camera2DComponent>()) {  ComponentCollection<Camera2DComponent>() }
    single(named<SpriteComponent>()) {  ComponentCollection<SpriteComponent>() }
    single(named<TransformComponent>()) { ComponentCollection<TransformComponent>() }
    single(named<InputComponent<Input>>()) { ComponentCollection<InputComponent<Input>>() }
    single(named<PhysicsComponent>()) { ComponentCollection<PhysicsComponent>() }
}

val systemsModule = module {

    single {
        FollowCamera2DSystem(
            get(named<TransformComponent>()),
            get(named<Camera2DComponent>())
        )
    } bind System::class

    single {
        SpriteSystem(
            get(named<SpriteComponent>()),
            get(named<TransformComponent>()),
            get(named<Camera2DComponent>()),
            get()
        )
    } bind System::class

    single {
        PhysicsSystem(
            get(named<PhysicsComponent>()),
            get(named<TransformComponent>()),
        )
    } bind System::class

    single {
        InputSystem(
            get(named<InputComponent<Input>>()),
            get()
        )
    } bind System::class

    single {
        CharacterSystem(
            get(named<InputComponent<Input>>()),
            get(named<TransformComponent>())
        )
    } bind System::class
}