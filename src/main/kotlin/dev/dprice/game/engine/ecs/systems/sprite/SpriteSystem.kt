package dev.dprice.game.engine.ecs.systems.sprite

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.getComponent
import dev.dprice.game.engine.ecs.getComponents
import dev.dprice.game.engine.ecs.registerSystem
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.ecs.systems.transform.asTransform
import dev.dprice.game.engine.graphics.Renderer
import dev.dprice.game.engine.model.Vector3f
import org.koin.core.component.inject

fun SystemRepository.createSpriteSystem() = registerSystem<SpriteComponent> {
    val renderer: Renderer by inject()

    getComponents<Camera2DComponent>().forEach { camera ->

        getComponents<SpriteComponent>().forEach { sprite ->
            val transform = getComponent<TransformComponent>(sprite)

            val vertices = when (val texture = sprite.texture) {
                is Texture.Full -> quadVerticesAndCoords
                is Texture.TileMap -> getTileMapVertices(texture)
            }

            renderer.drawTriangles(
                vertices,
                quadTriangleIndices,
                sprite.texture,
                sprite.vertexShader,
                sprite.fragmentShader,
                transform.asTransform().copy(
                    scale = transform.scale * Vector3f(sprite.size.x, sprite.size.y)
                ),
                camera
            )
        }
    }
}

private fun getTileMapVertices(
    tileMap: Texture.TileMap
): FloatArray {

    return with(tileMap.tileCoords) {
         floatArrayOf(
            0.5f, 0.5f, 0.0f, right, top, // top right
            0.5f, -0.5f, 0.0f, right, bottom,  // bottom right
            -0.5f, -0.5f, 0.0f, left, bottom, // bottom left
            -0.5f, 0.5f, 0.0f, left, top,   // top left
        )
    }
}

// todo: move to method to add texture coords from sprite map???? what did i mean :'( ?
private val quadVerticesAndCoords = floatArrayOf(
    0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // top right
    0.5f, -0.5f, 0.0f, 1.0f, 0.0f,  // bottom right
    -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, // bottom left
    -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,   // top left
)

private val quadTriangleIndices = intArrayOf(
    0, 1, 3,   // first triangle
    1, 2, 3    // second triangle
)