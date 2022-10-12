package dev.dprice.game.graphics

import org.lwjgl.opengl.GL20

interface ShaderProgram {

    fun initialize(vertexShaderPath: String, fragmentShaderPath: String)

    fun use()

    fun disable()

    fun destroy()
}

class OpenGlShaderProgram : ShaderProgram {
    private var program: Int? = null

    override fun initialize(vertexShaderPath: String, fragmentShaderPath: String) {
        val vertexShader = createShader(vertexShaderPath, GL20.GL_VERTEX_SHADER)
        val fragmentShader = createShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER)

        program = createShaderProgram(vertexShader, fragmentShader)

        GL20.glDeleteShader(vertexShader)
        GL20.glDeleteShader(fragmentShader)
    }

    override fun use() {
        program?.let { GL20.glUseProgram(it) }
    }

    override fun disable() {
        GL20.glUseProgram(0)
    }

    override fun destroy() {
        program?.let {
            GL20.glDeleteProgram(it)
            program = null
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