package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.model.length
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.character.Direction
import kotlin.math.abs

class NavigationSystem(
    private val navigators: SparseArray<NavigatorComponent>,
    private val nodes: SparseArray<NavigatableComponent>,
    private val transforms: SparseArray<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {

        navigators.forEach { navigator ->
            val navigatorTransform = transforms.get(navigator) ?: error("navigator has no transform")
            val availableNodes = nodes
                .map { node ->
                    val nodeTransform = transforms.get(node) ?: error("node has no transform")
                    (nodeTransform.position - navigatorTransform.position) to node
                }
                .filter { (difference, node) ->
                    (abs(difference.length()) <= navigator.searchDistance) && node.canPlayerUse
                }

            val availableDirections = availableNodes.map { (difference, node) ->
                when {
                    difference.x > 0 && abs(difference.x) > abs(difference.y) -> Direction.RIGHT to node
                    difference.x < 0 && abs(difference.x) > abs(difference.y) -> Direction.LEFT to node
                    difference.y > 0 && abs(difference.y) > abs(difference.x) -> Direction.UP to node
                    difference.y < 0 && abs(difference.y) > abs(difference.x) -> Direction.DOWN to node
                    difference == Vector3f() -> navigator.direction to node
                    else -> error("Failed to find direction to navigate")
                }
            }

            navigator.availableDirections = availableDirections.map { it.first }

            // todo: switch to move towards a specific node location
            val movement = when (navigator.direction) {
                Direction.UP -> {
                    if (availableDirections.any { it.first == Direction.UP }) {
                        val vector = availableDirections.maxBy {
                            val nodeTransform = transforms.get(it.second) ?: error("transform not found")
                            nodeTransform.position.y
                        }
                        val nodeTransform = transforms.get(vector.second) ?: error("transform not found")
                        val difference = nodeTransform.position - navigatorTransform.position
                        Vector3f(y = 60f, x= difference.x)
                    } else {
                        Vector3f()
                    }
                }
                Direction.DOWN -> {
                    if (availableDirections.any { it.first == Direction.DOWN }) {
                        val vector = availableDirections.minBy {
                            val nodeTransform = transforms.get(it.second) ?: error("transform not found")
                            nodeTransform.position.y
                        }
                        val nodeTransform = transforms.get(vector.second) ?: error("transform not found")
                        val difference = nodeTransform.position - navigatorTransform.position
                        Vector3f(y = -60f, x= difference.x)
                    } else {
                        Vector3f()
                    }
                }
                Direction.LEFT -> {
                    if (availableDirections.any { it.first == Direction.LEFT }) {
                        val vector = availableDirections.minBy {
                            val nodeTransform = transforms.get(it.second) ?: error("transform not found")
                            nodeTransform.position.x
                        }
                        val nodeTransform = transforms.get(vector.second) ?: error("transform not found")
                        val difference = nodeTransform.position - navigatorTransform.position
                        Vector3f(x = -60f, y= difference.y)
                    } else {
                        Vector3f()
                    }
                }
                Direction.RIGHT -> {
                    if (availableDirections.any { it.first == Direction.RIGHT }) {
                        val vector = availableDirections.maxBy {
                            val nodeTransform = transforms.get(it.second) ?: error("transform not found")
                            nodeTransform.position.x
                        }
                        val nodeTransform = transforms.get(vector.second) ?: error("transform not found")
                        val difference = nodeTransform.position - navigatorTransform.position
                        Vector3f(x = 60f, y= difference.y)
                    } else {
                        Vector3f()
                    }
                }
            }

            navigatorTransform.position = navigatorTransform.position + (movement * timeSinceLast.toFloat())
        }
    }
}