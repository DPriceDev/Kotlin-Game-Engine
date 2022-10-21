package dev.dprice.game.entities.pickups

import dev.dprice.game.engine.ecs.model.SystemProvider
import dev.dprice.game.engine.ecs.model.registerSystem
import dev.dprice.game.engine.ecs.systems.sprite.SpriteComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.model.length
import dev.dprice.game.entities.character.CharacterComponent
import kotlin.math.abs
import dev.dprice.game.engine.ecs.model.getComponents
import dev.dprice.game.engine.ecs.model.getComponent

fun SystemProvider.createPickUpSystem() = registerSystem<PickupComponent> {
    getComponents<PickupComponent>().filter { !it.isCollected }.forEach { pickup ->
        val pickupTransform = getComponent<TransformComponent>(pickup) ?: error("Pickup does not have a transform")

        getComponents<CharacterComponent>().forEach { character ->
            val characterTransform =
                getComponent<TransformComponent>(character) ?: error("Character does not have a transform")
            val difference = pickupTransform.position - characterTransform.position
            val distance = abs(difference.length())

            if (distance < 1f) {
                pickup.isCollected = true
                val sprite = getComponent<SpriteComponent>(pickup) ?: error("Pickup does not have a sprite")
                val texture = (sprite.texture as? Texture.TileMap) ?: error("pickup texture is not a tilemap")
                texture.yIndex = 5
                texture.xIndex = 12
            }
        }
    }
}
