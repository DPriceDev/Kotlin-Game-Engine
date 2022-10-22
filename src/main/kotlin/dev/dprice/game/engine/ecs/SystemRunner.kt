package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent

interface SystemRunner :
    KoinComponent,
    EntityInteractor,
    ComponentRepository {
    fun run(deltaTime: Double)
}

@Single([SystemRunner::class])
class SystemRunnerImpl(
    private val systemRepository: SystemRepository,
    entityInteractor: EntityInteractor,
    componentRepository: ComponentRepository
) : SystemRunner,
    EntityInteractor by entityInteractor,
    ComponentRepository by componentRepository {

    override fun run(deltaTime: Double) {
        systemRepository.getSystems().forEach { system ->
            system.invoke(this@SystemRunnerImpl, deltaTime)
        }
    }
}