package dev.dprice.game.entities.enemy

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.entities.character.Direction
import dev.dprice.game.entities.navigation.NavigatorComponent
import kotlin.math.max
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents

fun SystemRepository.createEnemySystem() = registerSystem<EnemyComponent> { timeSinceLast ->

    getComponents<EnemyComponent>().forEach { enemy ->
        val navigator = getComponent<NavigatorComponent>(enemy) ?: error("enemy has no navigator")
        val animator = getComponent<SpriteAnimatorComponent>(enemy) ?: error("enemy has no animator")

        enemy.decisionTime = max(0f, enemy.decisionTime - timeSinceLast.toFloat())

        if (navigator.availableDirections.isNotEmpty() && !navigator.availableDirections.contains(navigator.direction)) {
            navigator.direction = navigator.availableDirections.minus(navigator.direction).random()
        } else if (
            navigator.availableDirections.size == 2
            && !navigator.availableDirections.contains(navigator.direction)
            && navigator.availableDirections.containsOpposite(navigator.direction)
        ) {
            navigator.direction = navigator.availableDirections.random()
        } else if (
            navigator.availableDirections.size > 2
            && !navigator.availableDirections.contains(navigator.direction)
            && navigator.availableDirections.containsOpposite(navigator.direction)
        ) {
            navigator.direction = navigator.availableDirections.random()
        } else if (
            navigator.availableDirections.size > 2
            && navigator.availableDirections.contains(navigator.direction)
            && navigator.availableDirections.containsOpposite(navigator.direction)
        ) {
            navigator.direction = navigator.availableDirections.minus(navigator.direction.opposite()).random()
        }


        animator.tiles = when (navigator.direction) {
            Direction.UP -> listOf(
                4 to enemy.characterIndex,
                5 to enemy.characterIndex
            )

            Direction.DOWN -> listOf(
                6 to enemy.characterIndex,
                7 to enemy.characterIndex
            )

            Direction.LEFT -> listOf(
                2 to enemy.characterIndex,
                3 to enemy.characterIndex
            )

            Direction.RIGHT -> listOf(
                0 to enemy.characterIndex,
                1 to enemy.characterIndex
            )
        }
    }
}

private fun List<Direction>.containsOpposite(direction: Direction) = contains(direction.opposite())

private fun Direction.opposite() = when (this) {
    Direction.UP -> Direction.DOWN
    Direction.DOWN -> Direction.UP
    Direction.LEFT -> Direction.RIGHT
    Direction.RIGHT -> Direction.LEFT
}