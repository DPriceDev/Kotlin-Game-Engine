package dev.dprice.game.engine.ecs

import org.koin.core.annotation.Single

interface SystemRepository {
    fun registerSystem(id: String, runner: suspend SystemRunner.(delta: Double) -> Unit)

    fun unregisterSystem(id: String)

    fun getSystems() : Map<String, suspend SystemRunner.(Double) -> Unit>
}

@Single
class SystemRepositoryImpl : SystemRepository {
    private val systems: MutableMap<String, suspend SystemRunner.(Double) -> Unit> = mutableMapOf()
    override fun registerSystem(id: String, runner: suspend SystemRunner.(delta: Double) -> Unit) {
        systems[id] = runner
    }

    override fun unregisterSystem(id: String) {
        systems.remove(id)
    }

    override fun getSystems() = systems
}

inline fun <reified T: Any> SystemRepository.registerSystem(
    noinline onRun: suspend SystemRunner.(delta: Double) -> Unit
) {
    registerSystem(
        T::class.qualifiedName ?: error("system id class does not have a name"),
        onRun
    )
}

inline fun <reified T: Any> SystemRepository.unregisterSystem() {
    unregisterSystem(
        T::class.qualifiedName ?: error("system id class does not have a name"),
    )
}
