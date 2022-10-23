package dev.dprice.game.engine.graphics

import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30

data class BufferId(val vao: Int, val vbo: Int, val ebo: Int) {

    fun enable(vertices: FloatArray) {
        enable()

        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW)
    }

    fun enable() {
        GL30.glBindVertexArray(vao)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo)
    }

    fun disable() {
        GL30.glBindVertexArray(0)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0)
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0)
    }
}

interface BufferRepository {
    fun getBufferId(id: String, vertices: FloatArray, triangleIndices: IntArray) : BufferId

    fun unregisterBufferId(bufferId: BufferId)
}

@Single
class BufferRepositoryImpl : BufferRepository {
    private val buffers: MutableMap<String, BufferId> = mutableMapOf()

    override fun getBufferId(id: String, vertices: FloatArray, triangleIndices: IntArray): BufferId {
        return buffers.getOrElse(id) {
            val bufferId = generateBufferObjects(vertices, triangleIndices)
            buffers[id] = bufferId
            bufferId
        }
    }

    override fun unregisterBufferId(bufferId: BufferId) {
        GL30.glDeleteVertexArrays(bufferId.vao)
        GL15.glDeleteBuffers(bufferId.vbo)
        GL15.glDeleteBuffers(bufferId.ebo)
    }

    private fun generateBufferObjects(vertices: FloatArray, triangleIndices: IntArray): BufferId {
        val vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        val vbo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW)

        val ebo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, triangleIndices, GL30.GL_STATIC_DRAW)

        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0)
//        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)

        return BufferId(vao, vbo, ebo)
    }
}