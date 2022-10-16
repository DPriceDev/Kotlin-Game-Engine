package dev.dprice.game.entities.level

import dev.dprice.game.engine.ecs.ECS
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.character.Character
import dev.dprice.game.entities.enemy.Enemy
import dev.dprice.game.entities.tiles.*

class MazeGeneratorSystem(
    private val mazeComponents: SparseArray<MazeComponent>,
    private val transformComponents: SparseArray<TransformComponent>
) : System {

    override fun run(timeSinceLast: Double) {

        mazeComponents.filter { !it.isMazeGenerated }.forEach { level ->
            val transform = transformComponents.get(level) ?: error("Cannot find transform for level")

            val map = javaClass.classLoader
                .getResource("map.txt")
                ?.readText()
                ?.lines()
                ?.map { it.toList() }
                ?: error("Failed to load map.txt")

            map.forEachIndexed { rowIndex, row ->
                val y = (14 * 8f) - (rowIndex * 8f)
                row.forEachIndexed { columnIndex, tile ->
                    val x = (columnIndex * 8f) - (14 * 8f)
                    val position = transform.position + Vector3f(x = x, y = y)
                    createTile(tile, position)
                }
            }

            ECS.createEntity(
                Character(
                    Vector3f(y = -(8f * 9))
                )::onCreate
            )

            ECS.createEntity(Enemy(characterIndex = 4)::onCreate)
            ECS.createEntity(Enemy(characterIndex = 5)::onCreate)
            ECS.createEntity(Enemy(characterIndex = 6)::onCreate)
            ECS.createEntity(Enemy(characterIndex = 7)::onCreate)

            level.isMazeGenerated = true
        }
    }

    private fun createTile(character: Char, position: Vector3f) {
        when(character) {
            'a' -> createWall(position, 1, 3)
            'b' -> createWall(position, 0, 3)
            'c' -> createWall(position, 5, 3)
            'd' -> createWall(position, 4, 3)
            '0' -> createWall(position, 12, 5)
            '|' -> createWall(position, 3, 3)
            '-' -> createWall(position, 10, 3)
            '_' -> createWall(position, 12, 3)
            '!' -> createWall(position, 2, 3)
            '^' -> createWall(position, 14, 3)
            'l' -> createWall(position, 8, 4)
            'i' -> createWall(position, 9, 4)
            '~' -> createWall(position, 4, 4)
            'e' -> createWall(position, 7, 5)
            'f' -> createWall(position, 6, 5)
            'g' -> createWall(position, 9, 5)
            'h' -> createWall(position, 8, 5)
            'q' -> createWall(position, 10, 5)
            'r' -> createWall(position, 11, 5)
            's' -> createWall(position, 2, 5)
            't' -> createWall(position, 3, 5)
            'u' -> createWall(position, 4, 5)
            'v' -> createWall(position, 5, 5)
            'w' -> createWall(position, 6, 3)
            'x' -> createWall(position, 7, 3)
            'y' -> createWall(position, 8, 3)
            'z' -> createWall(position, 9, 3)
            '1' -> createWall(position, 0, 5)
            '2' -> createWall(position, 1, 5)
            '3' -> createWall(position, 12, 4)
            '4' -> createWall(position, 13, 4)
            '5' -> createWall(position, 14, 4)
            '6' -> createWall(position, 15, 4)
            '.' -> createWalkable(position, 12, 5)
            '*' -> createWalkable(position, 15, 5)
            ' ' -> createWalkable(position, 13, 5)
            '+' -> createAISpawnable(position, 12, 5)
            '@' -> createPlayerSpawnable(position, 12, 5)
            '&' -> createFruitSpawnable(position, 12, 5)
            else -> error("unhandled map tile: $character")
        }
    }

    private fun createWall(position: Vector3f, x: Int, y: Int) {
        ECS.createEntity(
            WallTile(
                position, x, y
            )::onCreate
        )
    }

    private fun createWalkable(position: Vector3f, x: Int, y: Int) {
        ECS.createEntity(
            WalkableTile(
                position, x, y
            )::onCreate
        )
    }

    private fun createAISpawnable(position: Vector3f, x: Int, y: Int) {
        ECS.createEntity(
            AIHomeTile(
                position, x, y
            )::onCreate
        )
    }

    private fun createPlayerSpawnable(position: Vector3f, x: Int, y: Int) {
        ECS.createEntity(
            PlayerSpawnTile(
                position, x, y
            )::onCreate
        )
    }

    private fun createFruitSpawnable(position: Vector3f, x: Int, y: Int) {
        ECS.createEntity(
            FruitSpawnTile(
                position, x, y
            )::onCreate
        )
    }
}