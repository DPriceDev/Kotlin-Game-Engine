package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.SystemProvider
import dev.dprice.game.engine.ecs.model.registerSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.model.angleTo
import dev.dprice.game.engine.model.length
import dev.dprice.game.entities.character.Direction
import kotlin.math.abs
import kotlin.math.min
import dev.dprice.game.engine.ecs.model.getComponents
import dev.dprice.game.engine.ecs.model.getComponent
import kotlin.math.sign

fun SystemProvider.createNavigationSystem() = registerSystem<NavigatorComponent> { timeSinceLast ->

    getComponents<NavigatorComponent>().forEach { navigator ->
        val navigatorTransform = getComponent<TransformComponent>(navigator) ?: error("navigator has no transform")
        val availableNodes = getComponents<NavigatableComponent>()
            .map { node ->
                val nodeTransform = getComponent<TransformComponent>(node) ?: error("node has no transform")
                (nodeTransform.position - navigatorTransform.position) to node
            }
            .filter { (difference, node) ->
                (abs(difference.length()) <= navigator.searchDistance) && (node.canPlayerUse || navigator.canUseAISpaces)
            }

        val availableDirections = availableNodes.mapNotNull { (difference, _) ->
            val angleX = difference.angleTo(Vector3f(1f)).value
            val angleY = difference.angleTo(Vector3f(y = 1f)).value
            when {
                difference == Vector3f() -> null
                angleX in -10f..10f -> Direction.RIGHT to difference
                angleX in 170f..180f || angleX in -180f..-170f -> Direction.LEFT to difference
                angleY in -10f..10f -> Direction.UP to difference
                angleY in 170f..180f || angleY in -180f..-170f -> Direction.DOWN to difference
                else -> null
            }
        }

        navigator.availableDirections = availableDirections.map { it.first }

        val speed = navigator.movementSpeed

        if (navigator.direction == Direction.RIGHT && navigator.availableDirections.contains(Direction.RIGHT)) {
            val test = availableDirections
                .filter { it.first == Direction.RIGHT }
                .maxBy { (_, difference) -> abs(difference.length()) }
            navigatorTransform.position = navigatorTransform.position + Vector3f(
                x = min(speed * timeSinceLast.toFloat(), test.second.length()),
                y = test.second.y.sign * min(speed * timeSinceLast.toFloat(), abs(test.second.y))
            )
        }

        if (navigator.direction == Direction.LEFT && navigator.availableDirections.contains(Direction.LEFT)) {
            val test = availableDirections
                .filter { it.first == Direction.LEFT }
                .maxBy { (_, difference) -> abs(difference.length()) }
            navigatorTransform.position = navigatorTransform.position + Vector3f(
                x = -min(speed * timeSinceLast.toFloat(), test.second.length()),
                y = test.second.y.sign * min(speed * timeSinceLast.toFloat(), abs(test.second.y))
            )
        }

        if (navigator.direction == Direction.UP && navigator.availableDirections.contains(Direction.UP)) {
            val test = availableDirections
                .filter { it.first == Direction.UP }
                .maxBy { (_, difference) -> abs(difference.length()) }
            navigatorTransform.position = navigatorTransform.position + Vector3f(
                y = min(speed * timeSinceLast.toFloat(), test.second.length()),
                x = test.second.x.sign * min(speed * timeSinceLast.toFloat(), abs(test.second.x))
            )
        }

        if (navigator.direction == Direction.DOWN && navigator.availableDirections.contains(Direction.DOWN)) {
            val test = availableDirections
                .filter { it.first == Direction.DOWN }
                .maxBy { (_, difference) -> abs(difference.length()) }
            navigatorTransform.position = navigatorTransform.position + Vector3f(
                y = -min(speed * timeSinceLast.toFloat(), test.second.length()),
                x = test.second.x.sign * min(speed * timeSinceLast.toFloat(), abs(test.second.x))
            )
        }
    }
}