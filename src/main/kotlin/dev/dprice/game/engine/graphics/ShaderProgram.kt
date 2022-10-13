package dev.dprice.game.engine.graphics

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*

interface ShaderProgram {

    fun initialize(vertexShaderPath: String, fragmentShaderPath: String)

    fun use()

    fun disable()

    fun destroy()

    fun setMatrix4f(name: String, matrix: FloatArray)
}

class OpenGlShaderProgram : ShaderProgram {
    private var program: Int = -1

    override fun initialize(vertexShaderPath: String, fragmentShaderPath: String) {
        val vertexShader = createShader(vertexShaderPath, GL20.GL_VERTEX_SHADER)
        val fragmentShader = createShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER)

        program = createShaderProgram(vertexShader, fragmentShader)

        GL20.glDeleteShader(vertexShader)
        GL20.glDeleteShader(fragmentShader)
    }

    override fun use() {
        glUseProgram(program)
    }

    override fun disable() {
        glUseProgram(0)
    }

    override fun destroy() {
        glDeleteProgram(program)
    }

    override fun setMatrix4f(name: String, matrix: FloatArray) {
        val location = glGetUniformLocation(program, name)

        when (val error = glGetError()) {
            GL_NO_ERROR -> { /* no-op */ }
            GL_INVALID_OPERATION -> error("failed to set matrix 4f uniform value: INVALID OPERATION")
            GL_INVALID_VALUE -> error("failed to set matrix 4f uniform value: INVALID VALUE")
            else -> error("failed to set matrix 4f uniform value: $error")
        }

        glUniformMatrix4fv(
            location,
            false,
            matrix
        )

        when (val error = glGetError()) {
            GL_NO_ERROR -> { /* no-op */ }
            GL_INVALID_OPERATION -> error("failed to set matrix 4f uniform value: INVALID OPERATION")
            GL_INVALID_VALUE -> error("failed to set matrix 4f uniform value: INVALID VALUE")
            else -> error("failed to set matrix 4f uniform value: $error")
        }
    }

    private fun createShader(shaderPath: String, shaderType: Int): Int {
        val vertexShaderSource = javaClass.classLoader
            .getResource(shaderPath)
            ?.readText()
            ?: error("Failed to load shader: $shaderPath")

        return GL20.glCreateShader(shaderType).apply {
            GL20.glShaderSource(this, vertexShaderSource)
            GL20.glCompileShader(this)

            if (GL20.glGetShaderi(this, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
                error("error: failed to compile shader: $shaderPath")
            }
        }
    }

    private fun createShaderProgram(vertexShader: Int, fragmentShader: Int): Int {
        return GL20.glCreateProgram().apply {
            GL20.glAttachShader(this, vertexShader)
            GL20.glAttachShader(this, fragmentShader)
            GL20.glLinkProgram(this)

            if(GL20.glGetProgrami(this, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
                error("error: failed to compile shader program for: $vertexShader and $fragmentShader")
            }
        }
    }
}