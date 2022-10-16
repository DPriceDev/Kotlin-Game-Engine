package dev.dprice.game.engine.ecs.systems.animation

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.util.SparseArray

class SpriteAnimatorSystem(
    private val sprites: SparseArray<SpriteComponent>,
    private val animators: SparseArray<SpriteAnimatorComponent>,
) : System {

    override fun run(timeSinceLast: Double) {
        animators.forEach { animator ->
            sprites.get(animator)?.let { sprite ->
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
}