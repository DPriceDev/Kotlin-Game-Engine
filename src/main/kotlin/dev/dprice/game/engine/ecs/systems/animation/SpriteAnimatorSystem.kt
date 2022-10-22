package dev.dprice.game.engine.ecs.systems.animation

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture

fun SystemRepository.createSpriteAnimatorSystem() = registerSystem<SpriteAnimatorComponent> { timeSinceLast ->

    getComponents<SpriteAnimatorComponent>().forEach { animator ->
        val sprite = getComponent<SpriteComponent>(animator)
        val currentTime = animator.currentFrameTime + timeSinceLast.toFloat()
        if (currentTime >= animator.timePerFrame) {
            val tiles = animator.tiles
            val nextIndex = animator.currentTile + 1
            val newIndex = if (nextIndex > tiles.lastIndex) 0 else nextIndex
            val (x, y) = tiles.get(newIndex)

           (sprite.texture as? Texture.TileMap)?.let {
               sprite.texture = it.copy(xIndex = x, yIndex = y)
           }

            animator.currentFrameTime = 0f
            animator.currentTile = newIndex
        } else {
            animator.currentFrameTime = currentTime
        }
    }
}
