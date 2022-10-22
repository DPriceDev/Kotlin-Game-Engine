package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.model.length
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.sign

fun SystemRepository.createNavigationSystem() = registerSystem<NavigatorComponent> { timeSinceLast ->

    val nodes = getComponents<NavigatableComponent>()

    getComponents<NavigatorComponent>().forEach { navigator ->
        val transform = getComponent<TransformComponent>(navigator) ?: error("navigator transform not found")

        val closest = nodes.minBy { node ->
            val nodeTransform = getComponent<TransformComponent>(node) ?: error("node transform not found")
            abs((transform.position - nodeTransform.position).length())
        }

        navigator.currentNode = closest
        navigator.targetNode = closest.connectedNodes.get(navigator.direction) ?: closest
        navigator.availableDirections = closest.connectedNodes.map { it.key }

        val speed = navigator.movementSpeed
        val target = navigator.targetNode
        val targetTransform = target?.let { getComponent<TransformComponent>(target) } ?: error("")
        val difference = targetTransform.position - transform.position
        val diff = Vector3f(
            x = difference.x.sign * min(speed * timeSinceLast.toFloat(), difference.x.absoluteValue),
            y = difference.y.sign * min(speed * timeSinceLast.toFloat(), difference.y.absoluteValue)
        )
        transform.position = transform.position + diff
    }
}