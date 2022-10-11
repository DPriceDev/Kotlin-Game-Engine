package dev.dprice.game.systems.sprite

import dev.dprice.game.ecs.ComponentCollection
import dev.dprice.game.ecs.model.System
import dev.dprice.game.graphics.ShaderRepository
import dev.dprice.game.graphics.ShaderType
import org.lwjgl.opengl.GL20.*
import dev.dprice.game.systems.transform.TransformComponent
import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import java.io.File

class SpriteSystem(
    private val sprites: ComponentCollection<SpriteComponent>,
    private val transforms: ComponentCollection<TransformComponent>,
    private val shaderRepository: ShaderRepository
) : System {

    override fun run(timeSinceLast: Double) {
        sprites.components.forEach {
//            val transform = transforms.components.getOrNull(it.entity.id)
            drawTriangle(it.vertexShader, it.fragmentShader)
        }
    }

    private fun drawTriangle(
        vertexShaderPath: String,
        fragmentShaderPath: String
    ) {
        // Load fragment + vertex shader
        val vertexShader = shaderRepository.getShaderId(vertexShaderPath, ShaderType.VERTEX)
        val fragmentShader = shaderRepository.getShaderId(fragmentShaderPath, ShaderType.FRAGMENT)

        // Load shader program from shaders
        val shaderProgram = shaderRepository.getShaderProgram(vertexShader, fragmentShader)

        val vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val vertices = floatArrayOf(
            0.0f, 0.5f, 0.0f,  // Vertex 1 (X, Y)
            0.5f, -0.5f, 0.0f,  // Vertex 2 (X, Y)
            -0.5f, -0.5f, 0.0f // Vertex 3 (X, Y)
        )

        // Generate a unique id for the vertex buffer object and the vertex array object
        val vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glBindVertexArray(0)

        glClear(GL_COLOR_BUFFER_BIT)

        glUseProgram(shaderProgram)

        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)

        glDrawArrays(GL_TRIANGLES, 0, 3)

        glDisableVertexAttribArray(0)
        glBindVertexArray(0)

        glUseProgram(0)
    }
}