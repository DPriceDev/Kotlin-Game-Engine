package dev.dprice.game.entities.level

import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.LevelLoader
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.entities.character.createCharacter
import dev.dprice.game.entities.enemy.createEnemy
import dev.dprice.game.entities.tiles.*

fun LevelLoader.createMazeGeneratorSystem() = registerSystem<MazeComponent> {

    getComponents<MazeComponent>().filter { !it.isMazeGenerated }.forEach { level ->
        val transform = getComponent<TransformComponent>(level) ?: error("Cannot find transform for level")

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

        createCharacter(Vector3f(y = -(8f * 9)))

        createEnemy(characterIndex = 4)
        createEnemy(characterIndex = 5)
        createEnemy(characterIndex = 6)
        createEnemy(characterIndex = 7)

        level.isMazeGenerated = true
    }
}

private fun LevelLoader.createTile(character: Char, position: Vector3f) {
    when (character) {
        'a' -> createWallTile(position, 1, 3)
        'b' -> createWallTile(position, 0, 3)
        'c' -> createWallTile(position, 5, 3)
        'd' -> createWallTile(position, 4, 3)
        '0' -> createWallTile(position, 12, 5)
        '|' -> createWallTile(position, 3, 3)
        '-' -> createWallTile(position, 10, 3)
        '_' -> createWallTile(position, 12, 3)
        '!' -> createWallTile(position, 2, 3)
        '^' -> createWallTile(position, 14, 3)
        'l' -> createWallTile(position, 8, 4)
        'i' -> createWallTile(position, 9, 4)
        '~' -> createWallTile(position, 4, 4)
        'e' -> createWallTile(position, 7, 5)
        'f' -> createWallTile(position, 6, 5)
        'g' -> createWallTile(position, 9, 5)
        'h' -> createWallTile(position, 8, 5)
        'q' -> createWallTile(position, 10, 5)
        'r' -> createWallTile(position, 11, 5)
        's' -> createWallTile(position, 2, 5)
        't' -> createWallTile(position, 3, 5)
        'u' -> createWallTile(position, 4, 5)
        'v' -> createWallTile(position, 5, 5)
        'w' -> createWallTile(position, 6, 3)
        'x' -> createWallTile(position, 7, 3)
        'y' -> createWallTile(position, 8, 3)
        'z' -> createWallTile(position, 9, 3)
        '1' -> createWallTile(position, 0, 5)
        '2' -> createWallTile(position, 1, 5)
        '3' -> createWallTile(position, 12, 4)
        '4' -> createWallTile(position, 13, 4)
        '5' -> createWallTile(position, 14, 4)
        '6' -> createWallTile(position, 15, 4)
        '.' -> createWalkableTile(position, 12, 5)
        '*' -> createWalkableTile(position, 15, 5)
        ' ' -> createPelletTile(position, 13, 5)
        '+' -> createAIHomeTile(position, 12, 5)
        '@' -> createPlayerSpawnTile(position, 12, 5)
        '&' -> createFruitSpawnTile(position, 12, 5)
        else -> error("unhandled map tile: $character")
    }
}