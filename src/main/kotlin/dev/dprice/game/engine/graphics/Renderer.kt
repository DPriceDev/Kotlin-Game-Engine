package dev.dprice.game.engine.graphics

import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.model.*
import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL30

interface Renderer {

    fun drawTriangles(
        vertices: FloatArray,
        triangleIndices: IntArray,
        texture: Texture,
        vertexShader: String,
        fragmentShader: String,
        transform: Transform,
        camera: Camera2DComponent
    )

    fun renderFrame()
}

data class TriangleDrawRequest(
    val vertices: FloatArray,
    val triangleIndices: IntArray,
    val texture: Texture,
    val vertexShader: String,
    val fragmentShader: String,
    val transform: Transform,
    val camera: Camera2DComponent
)

@Single
class OpenGlRendererImpl(
    private val shaderRepository: ShaderRepository,
    private val textureRepository: TextureRepository,
    private val bufferRepository: BufferRepository
) : Renderer {
    private var requests: MutableList<TriangleDrawRequest> = mutableListOf()

    override fun drawTriangles(
        vertices: FloatArray,
        triangleIndices: IntArray,
        texture: Texture,
        vertexShader: String,
        fragmentShader: String,
        transform: Transform,
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

    override fun renderFrame() {
        requests.let { requests ->
            this.requests = mutableListOf()
            requests.forEach { request ->
                drawToScreen(request)
            }
        }
    }

    private fun drawToScreen(request: TriangleDrawRequest) {
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
        val worldTransform = Matrix4f.identity()
            .translate(request.transform.position)
            .scale(request.transform.scale)
            .rotate(request.transform.rotation)

        // Apply uniform values to shader
        program.setMatrix4f("world", worldTransform.asFloatArray())
        program.setMatrix4f("view", request.camera.viewTransform.asFloatArray())

        // draw sprite to screen
        renderToScreen(bufferId.vao)

        // unbind buffers vao and vbo
        bufferId.disable()

        // delete buffers
        program.disable()
    }

    private fun renderToScreen(vao: Int) {
        GL30.glBindVertexArray(vao)
        GL30.glEnableVertexAttribArray(0)
        GL30.glEnableVertexAttribArray(1)

        GL30.glDrawElements(GL30.GL_TRIANGLES, 6, GL30.GL_UNSIGNED_INT, 0)

        GL30.glDisableVertexAttribArray(1)
        GL30.glDisableVertexAttribArray(0)
        GL30.glBindVertexArray(0)
    }
}