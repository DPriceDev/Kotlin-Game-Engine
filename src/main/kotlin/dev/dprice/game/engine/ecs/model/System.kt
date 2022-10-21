package dev.dprice.game.engine.ecs.model

import org.koin.core.component.KoinComponent

interface System :
    KoinComponent,
    EntityProvider,
    ComponentProvider