package dev.dprice.game.engine.ecs.systems.animation

import dev.dprice.game.engine.ecs.model.SystemProvider
import dev.dprice.game.engine.ecs.model.getComponent
import dev.dprice.game.engine.ecs.model.getComponents
import dev.dprice.game.engine.ecs.model.registerSystem
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture


fun SystemProvider.createSpriteAnimatorSystem() = registerSystem<SpriteAnimatorComponent> { timeSinceLast ->

    getComponents<SpriteAnimatorComponent>().forEach { animator ->
        getComponent<SpriteComponent>(animator)?.let { sprite ->
            val currentTime = animator.currentFrameTime + timeSinceLast.toFloat()
            if (currentTime >= animator.timePerFrame) {
                val tiles = animator.tiles
                val nextIndex = animator.currentTile + 1
                val newIndex = if (nextIndex > tiles.lastIndex) 0 else nextIndex
                val (x, y) = tiles.get(newIndex)

                (sprite.texture as? Texture.TileMap)?.let {
                    it.xIndex = x
                    it.yIndex = y
                }

                animator.currentFrameTime = 0f
                animator.currentTile = newIndex
            } else {
                animator.currentFrameTime = currentTime
            }
        }
    }
}
