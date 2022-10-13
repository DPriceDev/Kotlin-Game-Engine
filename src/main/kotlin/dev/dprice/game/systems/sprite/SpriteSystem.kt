package dev.dprice.game.systems.sprite

import dev.dprice.game.engine.ecs.ComponentCollection
import dev.dprice.game.engine.ecs.model.System
import dev.dprice.game.engine.graphics.ShaderProgram
import dev.dprice.game.engine.graphics.ShaderRepository
import dev.dprice.game.engine.graphics.util.orthographicMatrix
import dev.dprice.game.engine.model.*
import dev.dprice.game.systems.camera.Camera2DComponent
import dev.dprice.game.systems.transform.TransformComponent
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30.*

class SpriteSystem(
    private val sprites: ComponentCollection<SpriteComponent>,
    private val transforms: ComponentCollection<TransformComponent>,
    private val cameras: ComponentCollection<Camera2DComponent>,
    private val shaderRepository: ShaderRepository
) : System {

    override fun run(timeSinceLast: Double) {
        sprites.components.forEach { sprite ->
            val transform = transforms.components
                .getOrNull(sprite.entity.id)
                ?: error("cannot find transform for sprite") // todo: maybe just skip?


            cameras.components.forEach { camera ->
                drawToScreen(sprite, transform, camera)
            }
        }
    }

    private fun drawToScreen(
        sprite: SpriteComponent,
        transform: TransformComponent,
        camera: Camera2DComponent
    ) {
        val program = shaderRepository.getShaderProgram(sprite.vertexShader, sprite.fragmentShader)

        program.use()

        val worldTransform = Matrix4f.identity()
            .translate(transform.position)
            .scale(transform.scale)

        val viewTransform = Matrix4f.identity()
           .translate(Vector3f(z = -0.5f, x= 1f, y = 1f))

        val fustrumTransform = orthographicMatrix(0f, 800f, 0f, 600f, 0.1f, 2.0f)

        // apply otho or perspective camera?
        program.setMatrix4f("world", worldTransform.asFloatArray())
        program.setMatrix4f("camera", fustrumTransform.asFloatArray())
        program.setMatrix4f("view", viewTransform.asFloatArray())

        // setup vbo and vao and elo
        val (vao, vbo, ebo) = generateSprite()

        // draw sprite to screen
        renderToScreen(vao, program)

        // clear up
        glDeleteVertexArrays(vao)
        GL15.glDeleteBuffers(vbo)
        GL15.glDeleteBuffers(ebo)
    }

    data class SpriteOutput(val vao: Int, val vbo: Int, val ebo: Int)

    private fun generateSprite(): SpriteOutput {

        val vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW)

        val ebo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, quadTriangleIndices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glBindVertexArray(0)

        return SpriteOutput(vao, vbo, ebo)
    }

    private fun renderToScreen(vao: Int, shaderProgram: ShaderProgram) {

        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glBindVertexArray(0)

        shaderProgram.disable()
    }

    companion object {
        private val quadVertices = floatArrayOf(
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left
        )
        private val quadTriangleIndices = intArrayOf(
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
        )
    }
}