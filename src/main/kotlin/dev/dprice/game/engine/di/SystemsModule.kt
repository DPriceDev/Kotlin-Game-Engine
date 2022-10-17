package dev.dprice.game.engine.di

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorSystem
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.camera.FollowCamera2DSystem
import dev.dprice.game.engine.ecs.systems.input.InputComponent
import dev.dprice.game.engine.ecs.systems.input.InputSystem
import dev.dprice.game.engine.ecs.systems.physics.PhysicsComponent
import dev.dprice.game.engine.ecs.systems.physics.PhysicsSystem
import dev.dprice.game.engine.ecs.systems.sound.SoundComponent
import dev.dprice.game.engine.ecs.systems.sound.SoundSystem
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.SpriteSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.input.model.Input
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.character.CharacterComponent
import dev.dprice.game.entities.character.CharacterSystem
import dev.dprice.game.entities.navigation.NavigatorComponent
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val componentsModule = module {
    single(named<Camera2DComponent>()) {  SparseArray<Camera2DComponent>() }
    single(named<SpriteComponent>()) {  SparseArray<SpriteComponent>() }
    single(named<TransformComponent>()) { SparseArray<TransformComponent>() }
    single(named<InputComponent<Input>>()) { SparseArray<InputComponent<Input>>() }
    single(named<PhysicsComponent>()) { SparseArray<PhysicsComponent>() }
    single(named<SpriteAnimatorComponent>()) { SparseArray<SpriteAnimatorComponent>() }
    single(named<CharacterComponent>()) { SparseArray<CharacterComponent>() } // todo: move
    single(named<SoundComponent>()) { SparseArray<SoundComponent>() }
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
            get(),
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

    // todo: move
    single {
        CharacterSystem(
            get(named<CharacterComponent>()),
            get(named<InputComponent<Input>>()),
            get(named<TransformComponent>()),
            get(named<NavigatorComponent>())
        )
    } bind System::class

    single {
        SpriteAnimatorSystem(
            get(named<SpriteComponent>()),
            get(named<SpriteAnimatorComponent>())
        )
    } bind System::class

    single {
        SoundSystem(
            get(named<SoundComponent>())
        )
    } bind System::class
}