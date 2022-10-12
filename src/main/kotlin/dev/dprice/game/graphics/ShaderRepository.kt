package dev.dprice.game.graphics

import org.koin.core.annotation.Single

interface ShaderRepository {
    fun getShaderProgram(vertexShaderPath: String, fragmentShaderPath: String) : ShaderProgram
}

@Single
class ShaderRepositoryImpl : ShaderRepository {
    private val shaderProgramMap = mutableMapOf<Pair<String, String>, ShaderProgram>()

    override fun getShaderProgram(vertexShaderPath: String, fragmentShaderPath: String): ShaderProgram {
        return shaderProgramMap.getOrElse(vertexShaderPath to fragmentShaderPath) {
            val program: ShaderProgram = OpenGlShaderProgram() // todo: extract to factory?
            shaderProgramMap[vertexShaderPath to fragmentShaderPath] = program
            program.initialize(vertexShaderPath, fragmentShaderPath)
            program
        }
    }
}