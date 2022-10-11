package dev.dprice.game.entities.character.di

import dev.dprice.game.entities.character.Character
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val characterModule = module {
    singleOf<Character>(::Character)
}