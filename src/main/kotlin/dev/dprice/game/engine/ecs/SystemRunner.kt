package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.interactors.EntityInteractor
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.lwjgl.glfw.GLFW.glfwGetTime

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
        systemRepository.getSystems().forEach { (id, system) ->
            val start = glfwGetTime()
            system.invoke(this@SystemRunnerImpl, deltaTime)
            println(
                "system: $id ran for ${ glfwGetTime() - start } seconds"
            )
        }
    }
}