package dev.dprice.game.systems.di

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.entities.character.CharacterSystem
import dev.dprice.game.systems.camera.Camera2DComponent
import dev.dprice.game.systems.camera.Camera2DSystem
import dev.dprice.game.systems.input.InputComponent
import dev.dprice.game.systems.input.InputSystem
import dev.dprice.game.systems.input.model.Input
import dev.dprice.game.systems.physics.PhysicsSystem
import dev.dprice.game.systems.sprite.SpriteComponent
import dev.dprice.game.systems.sprite.SpriteSystem
import dev.dprice.game.systems.transform.TransformComponent
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val systemsModule = module {

    single(named<Camera2DComponent>()) {  ComponentCollection<Camera2DComponent>() }
    single {
        Camera2DSystem(
            get(named<TransformComponent>()),
            get(named<Camera2DComponent>())
        )
    } bind System::class

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

    single(named<InputComponent<Input>>()) { ComponentCollection<InputComponent<Input>>() }
    single {
        InputSystem(
            get(named<InputComponent<Input>>()),
            get()
        )
    } bind System::class

    single {
        CharacterSystem(
            get(named<InputComponent<Input>>())
        )
    } bind System::class
}