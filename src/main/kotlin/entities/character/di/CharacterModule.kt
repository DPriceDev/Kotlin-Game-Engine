package entities.character.di

import entities.character.Character
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val characterModule = module {
    singleOf<Character>(::Character)
}