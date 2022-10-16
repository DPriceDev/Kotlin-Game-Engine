package dev.dprice.game.entities.enemy

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.animation.SpriteAnimatorComponent
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.character.Direction
import dev.dprice.game.entities.navigation.NavigatorComponent
import kotlin.math.max

class EnemySystem(
    private val enemies: SparseArray<EnemyComponent>,
    private val navigators: SparseArray<NavigatorComponent>,
    private val animators: SparseArray<SpriteAnimatorComponent>
) : System {

    override fun run(timeSinceLast: Double) {

        enemies.forEach { enemy ->
            val navigator = navigators.get(enemy) ?: error("enemy has no navigator")
            val animator = animators.get(enemy) ?: error("enemy has no animator")

            enemy.decisionTime = max(0f, enemy.decisionTime - timeSinceLast.toFloat())

            if (navigator.availableDirections.isNotEmpty() && !navigator.availableDirections.contains(navigator.direction)) {
                navigator.direction = navigator.availableDirections.minus(navigator.direction).random()
            } else if (
                navigator.availableDirections.size == 2
                && !navigator.availableDirections.contains(navigator.direction)
                && navigator.availableDirections.containsOpposite(navigator.direction)
            ) {
                navigator.direction = navigator.availableDirections.random()
            }
            else if (
                navigator.availableDirections.size > 2
                && !navigator.availableDirections.contains(navigator.direction)
                && navigator.availableDirections.containsOpposite(navigator.direction)
            ) {
                navigator.direction = navigator.availableDirections.random()
            }
            else if (
                navigator.availableDirections.size > 2
                && navigator.availableDirections.contains(navigator.direction)
                && navigator.availableDirections.containsOpposite(navigator.direction)
            ) {
                navigator.direction = navigator.availableDirections.minus(navigator.direction.opposite()).random()
            }


            animator.tiles = when(navigator.direction) {
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

    private fun Direction.opposite() = when(this) {
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
        Direction.LEFT -> Direction.RIGHT
        Direction.RIGHT -> Direction.LEFT
    }
}