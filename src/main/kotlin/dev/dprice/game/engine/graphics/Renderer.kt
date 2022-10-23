package dev.dprice.game.engine.graphics

import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.model.Transform3f
import dev.dprice.game.engine.model.Vector3f
import dev.dprice.game.engine.model.asFloatArray
import dev.dprice.game.engine.model.asMatrix4f
import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL31.glDrawElementsInstanced
import org.lwjgl.opengl.GL33

interface Renderer {

    fun drawTriangles(
        vertices: FloatArray,
        triangleIndices: IntArray,
        texture: Texture,
        vertexShader: String,
        fragmentShader: String,
        transform: Transform3f,
        camera: Camera2DComponent
    )

    fun drawTrianglesInstanced(request: InstancedTriangleDrawRequest)

    fun renderFrame()
}

data class TriangleDrawRequest(
    val vertices: FloatArray,
    val triangleIndices: IntArray,
    val texture: Texture,
    val vertexShader: String,
    val fragmentShader: String,
    val transform: Transform3f,
    val camera: Camera2DComponent
)

data class InstancedTriangleDrawRequest(
    val count: Int,
    val vertices: List<FloatArray>,
    val triangleIndices: IntArray,
    val texture: Texture,
    val vertexShader: String,
    val fragmentShader: String,
    val transform: List<Transform3f>,
    val camera: Camera2DComponent
)

@Single
class OpenGlRendererImpl(
    private val shaderRepository: ShaderRepository,
    private val textureRepository: TextureRepository,
    private val bufferRepository: BufferRepository
) : Renderer {
    private var requests: MutableList<TriangleDrawRequest> = mutableListOf()

    private var instancedRequests: MutableList<InstancedTriangleDrawRequest> = mutableListOf()

    override fun drawTriangles(
        vertices: FloatArray,
        triangleIndices: IntArray,
        texture: Texture,
        vertexShader: String,
        fragmentShader: String,
        transform: Transform3f,
        camera: Camera2DComponent
    ) {
        requests.add(
            TriangleDrawRequest(
                vertices,
                triangleIndices,
                texture,
                vertexShader,
                fragmentShader,
                transform,
                camera
            )
        )
    }

    override fun drawTrianglesInstanced(request: InstancedTriangleDrawRequest) {
        instancedRequests.add(request)
    }

    override fun renderFrame() {
        val frameRequests = requests
        val frameInstancedRequests = instancedRequests
        requests = mutableListOf()
        instancedRequests = mutableListOf()

        frameRequests.forEach { request ->
            drawToScreen(request)
        }

        frameInstancedRequests.forEach { request ->
            drawInstancedToScreen(request)
        }
    }

    private fun drawToScreen(
        request: TriangleDrawRequest
    ) {
        val program = shaderRepository.getShaderProgram(request.vertexShader, request.fragmentShader)
        program.use()

        // setup vbo and vao and elo
        val bufferId = bufferRepository.getBufferId(
            request.vertexShader,
            request.vertices,
            request.triangleIndices
        )

        // load vao, vbo, ebo, and texture
        bufferId.enable(request.vertices)

        val texture = textureRepository.getTexture(request.texture.path)
        texture.bind()

        // FIXME: Doing this calculation reduces fps
        val worldTransform = request.transform.asMatrix4f()

        // Apply uniform values to shader
        program.setMatrix4f("world", worldTransform.asFloatArray())
        program.setMatrix4f("view", request.camera.viewTransform.asFloatArray())

        // draw sprite to screen
        drawTriangles(bufferId.vao)

        // unbind buffers vao and vbo
        bufferId.disable()

        // delete buffers
        program.disable()
    }

    private fun drawInstancedToScreen(request: InstancedTriangleDrawRequest) {
        val program = shaderRepository.getShaderProgram(request.vertexShader, request.fragmentShader)
        program.use()

        // setup vbo and vao and elo
        // todo: Split out tile coords to separate vbo
        val vertices = floatArrayOf(
            0.0f, 0.0f, 0.0f, // right, top, // top right
            0.0f, -0.5f, 0.0f,// right, bottom,  // bottom right
            -0.5f, -0.5f, 0.0f, //left, bottom, // bottom left
            -0.5f, 0.0f, 0.0f, // left, top,   // top left
            0.5f, 0.5f, 0.0f, // right, top, // top right
            0.5f, 0.0f, 0.0f,// right, bottom,  // bottom right
            0.0f, 0.0f, 0.0f, //left, bottom, // bottom left
            0.0f, 0.5f, 0.0f, // left, top,   // top left
        )
            //request.vertices.toList().reduce { acc, floats -> acc.plus(floats) }

        val bufferId = bufferRepository.getBufferId(
            request.vertexShader,
            vertices,
            request.triangleIndices
        )

        // load vao, vbo, ebo, and texture
        // todo: switch to bind buffer array data?
        bufferId.enable(vertices)

        // Set vbo to be instanced for verts and coords
        GL30.glEnableVertexAttribArray(0)
//        GL30.glEnableVertexAttribArray(1)

        GL33.glVertexAttribDivisor(0, 1)
//        GL33.glVertexAttribDivisor(1, 1)

        // Bind texture
        // todo: Add enabling correct vbo into texture
        val texture = textureRepository.getTexture(request.texture.path)
        texture.bind()

        // Calculate transforms
//        val transforms = request.transform
//            .map { it.asMatrix4f().asFloatArray() }
//            .reduce { acc, floats -> acc.plus(floats) }

        // Bind transformation matrices
        //val transformVbo = bindTransforms(transforms)

        // Apply camera projection values to shader
        program.setMatrix4f("world", Transform3f(scale = Vector3f(20f, 20f, 20f)).asMatrix4f().asFloatArray())
        program.setMatrix4f("view", request.camera.viewTransform.asFloatArray())

        // draw sprites to screen
        drawTrianglesInstanced(bufferId.vao, 2)

        // unbind buffers vao and vbo
        bufferId.disable() // todo: Should this cover atrib arrays?
        program.disable()

        // todo: Move to buffer id
//        glDeleteBuffers(transformVbo)
    }

    private fun bindTransforms(transforms: FloatArray): Int {
        // todo: move into buffer id as list of vbos

        val transformVbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, transformVbo)
        glBufferData(GL_ARRAY_BUFFER, transforms, GL_STATIC_DRAW)

        repeat(4) { index ->
            GL20.glVertexAttribPointer(
                2 + index,
                4,
                GL_FLOAT,
                false,
                Float.SIZE_BYTES * 16,
                index * Float.SIZE_BYTES * 4L
            )
            GL33.glVertexAttribDivisor(2 + index, 1)
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        return transformVbo
    }

    private fun drawTriangles(vao: Int) {
        enableVertexArray(vao)
        GL30.glDrawElements(GL30.GL_TRIANGLES, 6, GL30.GL_UNSIGNED_INT, 0)
        disableVertexArray()
    }

    private fun drawTrianglesInstanced(vao: Int, count: Int) {
        enableVertexArray(vao)
        glDrawElementsInstanced(GL30.GL_TRIANGLES, 6, GL30.GL_UNSIGNED_INT, 0, count)
        disableVertexArray()
    }

    // todo: enable from buffer id fun use()
    private fun enableVertexArray(vao: Int) {
        GL30.glBindVertexArray(vao)
        GL30.glEnableVertexAttribArray(0)
//        GL30.glEnableVertexAttribArray(1)
    }

    private fun disableVertexArray() {
//        GL30.glDisableVertexAttribArray(1)
        GL30.glDisableVertexAttribArray(0)
        GL30.glBindVertexArray(0)
    }
}