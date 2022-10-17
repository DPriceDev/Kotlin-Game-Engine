package dev.dprice.game.entities.pickups

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.model.get
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.length
import dev.dprice.game.engine.util.SparseArray
import dev.dprice.game.entities.character.CharacterComponent
import kotlin.math.abs

class PickupSystem(
    private val pickups: SparseArray<PickupComponent>,
    private val characters: SparseArray<CharacterComponent>,
    private val transforms: SparseArray<TransformComponent>,
    private val sprites: SparseArray<SpriteComponent>
) : System {

    override fun run(timeSinceLast: Double) {
        pickups.filter { !it.isCollected }.forEach { pickup ->
            val pickupTransform = transforms.get(pickup) ?: error("Pickup does not have a transform")

            characters.forEach { character ->
                val characterTransform = transforms.get(character) ?: error("Character does not have a transform")
                val difference = pickupTransform.position - characterTransform.position
                val distance = abs(difference.length())

                if (distance < 1f) {
                    pickup.isCollected = true
                    val sprite = sprites.get(pickup) ?: error("Pickup does not have a sprite")
                    val texture = (sprite.texture as? Texture.TileMap) ?: error("pickup texture is not a tilemap")
                    texture.yIndex = 5
                    texture.xIndex = 12
                }
            }
        }
    }
}