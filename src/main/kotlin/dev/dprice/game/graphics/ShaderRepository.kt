package dev.dprice.game.graphics

import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*

enum class ShaderType(val type: Int) {
    VERTEX(GL_VERTEX_SHADER),
    FRAGMENT(GL_FRAGMENT_SHADER)
}

interface ShaderRepository {
    // todo: Potentially delete the shaders?
    fun getShaderId(shaderPath: String, type: ShaderType): Int

    fun getShaderProgram(vertexShader: Int, fragmentShader: Int) : Int
}

@Single
class ShaderRepositoryImpl : ShaderRepository {
    private val shaderMap = mutableMapOf<String, Int>()
    private val shaderProgramMap = mutableMapOf<Pair<Int, Int>, Int>()

    override fun getShaderId(shaderPath: String, type: ShaderType): Int {
        return shaderMap.getOrElse(shaderPath) {
            val shader = createShader(shaderPath, type.type)
            shaderMap[shaderPath] = shader
            return shader
        }
    }

    override fun getShaderProgram(vertexShader: Int, fragmentShader: Int): Int {
        return shaderProgramMap.getOrElse(vertexShader to fragmentShader) {
            glCreateProgram().apply {
                shaderProgramMap[vertexShader to fragmentShader] = this
                glAttachShader(this, vertexShader)
                glAttachShader(this, fragmentShader)
                glLinkProgram(this)

                if(glGetProgrami(this, GL_LINK_STATUS) == GL_FALSE) {
                    error("error: failed to compile shader program for: $vertexShader and $fragmentShader")
                }
            }
        }
    }

    private fun createShader(shaderPath: String, shaderType: Int): Int {
        val vertexShaderSource = javaClass.classLoader
            .getResource(shaderPath)
            ?.readText()
            ?: error("Failed to load shader: $shaderPath")

        return glCreateShader(shaderType).apply {
            glShaderSource(this, vertexShaderSource)
            glCompileShader(this)

            if (glGetShaderi(this, GL_COMPILE_STATUS) == GL_FALSE) {
                error("error: failed to compile shader: $shaderPath")
            }
        }
    }
}