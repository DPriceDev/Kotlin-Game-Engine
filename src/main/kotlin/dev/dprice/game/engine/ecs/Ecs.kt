package dev.dprice.game.engine.ecs

import dev.dprice.game.engine.ecs.model.Component
import dev.dprice.game.engine.ecs.model.Entity
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.systems.input.InputRepository
import dev.dprice.game.systems.input.model.Input
import dev.dprice.game.systems.input.model.InputAction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ComponentCollection<T: Component>(
    val components: SparseArray<T> = SparseArray(),
)

// todo: Move to injectable object?
object ECS : KoinComponent {

    private val systems: List<System> by lazy { getKoin().getAll() }
    private val components: List<ComponentCollection<Component>> by inject()
    private val inputRepository: InputRepository by inject()

    // todo: Move to entity manager that just handles unused ids
    private val entities: MutableList<Entity> = mutableListOf()

    fun run(timeSinceLast: Double) {
        systems.forEach { it.run(timeSinceLast) }
    }

    fun createEntity(builder: (Entity) -> Unit) {
        val entity = Entity(1)

        builder(entity)

        entities.add(entity)
    }

    fun deleteEntity(id: Int) {
        deleteComponents(id)

        entities.removeIf { it.id == id }
    }

    private fun deleteComponents(id: Int) {
        components.forEach { collection ->
            collection.components.remove(id)
        }
    }

    fun addInputMapping(action: InputAction, inputConverter: (InputAction) -> Input) {
        inputRepository.addInputActionMapping(action, inputConverter)
    }

    fun registerInput(key: Int, action: Int) {
        inputRepository.addInputAction(InputAction(key, action))
    }
}