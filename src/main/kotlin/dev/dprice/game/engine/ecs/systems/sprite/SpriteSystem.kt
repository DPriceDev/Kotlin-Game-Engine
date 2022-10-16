package dev.dprice.game.engine.ecs.systems.sprite

import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.transform.TransformComponent
import dev.dprice.game.engine.graphics.LoadedTexture
import dev.dprice.game.engine.graphics.ShaderRepository
import dev.dprice.game.engine.graphics.TextureRepository
import dev.dprice.game.engine.graphics.util.orthographicMatrix
import dev.dprice.game.engine.model.*
import dev.dprice.game.engine.util.SparseArray
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30.*

class SpriteSystem(
    private val sprites: SparseArray<SpriteComponent>,
    private val transforms: SparseArray<TransformComponent>,
    private val cameras: SparseArray<Camera2DComponent>,
    private val shaderRepository: ShaderRepository,
    private val textureRepository: TextureRepository
) : System {

    override fun run(timeSinceLast: Double) {

        cameras.forEach { camera ->
            val cameraTransform = camera.getCameraTransform()

            sprites.forEach { sprite ->
                val transform = transforms
                    .getOrNull(sprite.entity.id)
                    ?: error("cannot find transform for sprite") // todo: maybe just skip?

                drawToScreen(sprite, transform, cameraTransform)
            }
        }
    }

    private fun Camera2DComponent.getCameraTransform(): Matrix4f {
        val viewTransform = Matrix4f.identity()
            .translate(target.copy(z = target.z - depth))

        val fustrumTransform = with(fustrum) {
            orthographicMatrix(
                -(width / 2),
                (width / 2),
                -(height / 2),
                (height / 2),
                nearestDepth,
                depth
            )
        }
        return fustrumTransform * viewTransform
    }

    private fun drawToScreen(
        sprite: SpriteComponent,
        transform: TransformComponent,
        cameraTransform: Matrix4f
    ) {
        val program = shaderRepository.getShaderProgram(sprite.vertexShader, sprite.fragmentShader)
        program.use()

        val texture = textureRepository.getTexture(sprite.texture.path)

        val vertices = when(sprite.texture) {
            is Texture.Full -> quadVerticesAndCoords
            is Texture.TileMap -> getTileMapVertices(texture, sprite.texture)
        }

        val (width, height) = when(sprite.texture) {
            is Texture.Full -> texture.width to texture.height
            is Texture.TileMap -> sprite.texture.tileWidth to sprite.texture.tileHeight
        }

        // setup vbo and vao and elo
        val (vao, vbo, ebo) = generateSprite(vertices)

        bindTexture(texture)

        val worldTransform = Matrix4f.identity()
            .translate(transform.position)
            .translate(Vector3f(z = sprite.zDepth))
            .scale(Vector3f(x = width.toFloat(), y = height.toFloat()))
            .scale(transform.scale)
            .rotate(transform.rotation)

        // apply otho or perspective camera?
        program.setMatrix4f("world", worldTransform.asFloatArray())
        program.setMatrix4f("view", cameraTransform.asFloatArray())

        // draw sprite to screen
        renderToScreen(vao)

        // unbind buffers vao and vbo
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        // delete buffers
        program.disable()
        glDeleteVertexArrays(vao)
        GL15.glDeleteBuffers(vbo)
        GL15.glDeleteBuffers(ebo)
    }

    private fun bindTexture(texture: LoadedTexture) {
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture.id)
    }

    private fun generateSprite(vertices: FloatArray): SpriteOutput {
        val vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        val ebo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, quadTriangleIndices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0)

        return SpriteOutput(vao, vbo, ebo)
    }

    private fun getTileMapVertices(
        loadedTexture: LoadedTexture,
        tileMapTexture: Texture.TileMap
    ): FloatArray {
        val xPercent = tileMapTexture.tileWidth.toFloat() / loadedTexture.width
        val yPercent = tileMapTexture.tileHeight.toFloat() / loadedTexture.height

        val xSpacing = tileMapTexture.spacing.takeIf { it > 0 }?.let {
            tileMapTexture.xIndex * (it / loadedTexture.width.toFloat())
        } ?: 0f
        val ySpacing = tileMapTexture.spacing.takeIf { it > 0 }?.let {
            tileMapTexture.yIndex * (it / loadedTexture.height.toFloat())
        } ?: 0f

        val left = (tileMapTexture.xIndex) * xPercent + xSpacing
        val right = (tileMapTexture.xIndex + 1) * xPercent + xSpacing
        val top = 1f - (tileMapTexture.yIndex * yPercent + ySpacing)
        val bottom = 1f - ((tileMapTexture.yIndex + 1) * yPercent + ySpacing)

        return floatArrayOf(
            0.5f, 0.5f, 0.0f, right, top, // top right
            0.5f, -0.5f, 0.0f, right, bottom,  // bottom right
            -0.5f, -0.5f, 0.0f, left, bottom, // bottom left
            -0.5f, 0.5f, 0.0f, left, top,   // top left
        )
    }



    private fun renderToScreen(vao: Int) {
        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }

    data class SpriteOutput(val vao: Int, val vbo: Int, val ebo: Int)

    companion object {
        // todo: move to method to add texture coords from sprite map
        private val quadVerticesAndCoords = floatArrayOf(
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,   // top left
        )

        private val quadVertices = floatArrayOf(
            0.5f, 0.5f, 0.0f, // top right
            0.5f, -0.5f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, // top left
        )

        private val quadTriangleIndices = intArrayOf(
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
        )
    }
}