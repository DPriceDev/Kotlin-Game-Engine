package dev.dprice.game.engine.ecs.model

interface SystemProvider {
    fun registerSystem(id: String, runner: System.(delta: Double) -> Unit)

    fun unregisterSystem(id: String)
}

inline fun <reified T: Any> SystemProvider.registerSystem(
    noinline onRun: System.(delta: Double) -> Unit
) {
    registerSystem(
        T::class.qualifiedName ?: error("system id class does not have a name"),
        onRun
    )
}

inline fun <reified T: Any> SystemProvider.unregisterSystem() {
    unregisterSystem(
        T::class.qualifiedName ?: error("system id class does not have a name"),
    )
}
