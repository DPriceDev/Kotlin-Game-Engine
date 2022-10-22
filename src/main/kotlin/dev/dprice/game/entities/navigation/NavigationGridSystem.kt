package dev.dprice.game.entities.navigation

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.directionalAngleTo
import dev.dprice.game.engine.model.length
import dev.dprice.game.entities.character.Direction
import kotlin.math.abs

fun SystemRepository.createNavigationGridSystem() = registerSystem("navigation grid") { timeSinceLast ->

    val nodesAndTransforms = getComponents<NavigatableComponent>()
        .map { it to (getComponent<TransformComponent>(it)) }

    nodesAndTransforms
        .filter { !it.first.isAttached }
        .forEach { (node, transform) ->

            val closestNodes = nodesAndTransforms.filter { (_, otherTransform) ->
                val difference = transform.position - otherTransform.position
                abs(difference.length()) <= node.searchDistance && transform != otherTransform
            }

            node.connectedNodes = closestNodes.associate { (otherNode, otherTransform) ->
                val angle = transform.position.directionalAngleTo(otherTransform.position)

                val direction = when (angle.value) {
                    0.0f -> Direction.LEFT
                    90.0f -> Direction.DOWN
                    180.0f -> Direction.RIGHT
                    -90.0f -> Direction.UP
                    else -> error("incorrect angle found: ")
                }
                direction to otherNode
            }

            node.isAttached = true
        }
}