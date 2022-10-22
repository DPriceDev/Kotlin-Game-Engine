package dev.dprice.game.engine.graphics

import dev.dprice.game.engine.ecs.systems.camera.Camera2DComponent
import dev.dprice.game.engine.ecs.systems.sprite.Texture
import dev.dprice.game.engine.model.*
import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30

interface Renderer {

    fun drawSprite(
        vertices: FloatArray,
        triangleIndices: IntArray,
        texture: Texture,
        vertexShader: String,
        fragmentShader: String,
        spriteTransform: Transform,
        cameraTransform: Camera2DComponent
    )

    fun renderFrame()
}

data class SpriteDrawRequest(
    val vertices: FloatArray,
    val triangleIndices: IntArray,
    val texture: Texture,
    val vertexShader: String,
    val fragmentShader: String,
    val spriteTransform: Transform,
    val cameraTransform: Camera2DComponent
)

@Single
class OpenGlRendererImpl(
    private val shaderRepository: ShaderRepository,
    private val textureRepository: TextureRepository
) : Renderer {
    private var requests: MutableList<SpriteDrawRequest> = mutableListOf()

    override fun drawSprite(
        vertices: FloatArray,
        triangleIndices: IntArray,
        texture: Texture,
        vertexShader: String,
        fragmentShader: String,
        spriteTransform: Transform,
        cameraTransform: Camera2DComponent
    ) {
        requests.add(
            SpriteDrawRequest(
                vertices,
                triangleIndices,
                texture,
                vertexShader,
                fragmentShader,
                spriteTransform,
                cameraTransform
            )
        )
    }

    // todo: advance frame

    override fun renderFrame() {
        val local = requests.toList()
        requests = mutableListOf()

        local.forEach { request ->
            drawToScreen(
                shaderRepository,
                textureRepository,
                request
            )
        }

    }
}

private fun drawToScreen(
    shaderRepository: ShaderRepository,
    textureRepository: TextureRepository,
    request: SpriteDrawRequest
) {
    val program = shaderRepository.getShaderProgram(request.vertexShader, request.fragmentShader)
    program.use()

    val texture = textureRepository.getTexture(request.texture.path)

    val vertices = when(request.texture) {
        is Texture.Full -> request.vertices
        is Texture.TileMap -> getTileMapVertices(texture, request.texture)
    }

    val (width, height) = when(request.texture) {
        is Texture.Full -> texture.width to texture.height
        is Texture.TileMap -> request.texture.tileWidth to request.texture.tileHeight
    }

    // setup vbo and vao and elo
    val (vao, vbo, ebo) = generateSprite(vertices, request.triangleIndices)

    bindTexture(texture)
    val worldTransform = Matrix4f.identity()
        .translate(request.spriteTransform.position)
        .translate(Vector3f(z = 1f))
        .scale(Vector3f(x = width.toFloat(), y = height.toFloat()))
        .scale(request.spriteTransform.scale)
        .rotate(request.spriteTransform.rotation)

    // apply otho or perspective camera?
    program.setMatrix4f("world", worldTransform.asFloatArray())
    program.setMatrix4f("view", request.cameraTransform.viewTransform.asFloatArray())

    // draw sprite to screen
    renderToScreen(vao)

    // unbind buffers vao and vbo
    GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0)
    GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0)
    GL30.glBindVertexArray(0)

    // delete buffers
    program.disable()
    GL30.glDeleteVertexArrays(vao)
    GL15.glDeleteBuffers(vbo)
    GL15.glDeleteBuffers(ebo)
}

private fun bindTexture(texture: LoadedTexture) {
    GL30.glEnable(GL30.GL_BLEND)
    GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)

    GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)

    GL30.glActiveTexture(GL30.GL_TEXTURE0)
    GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.id)
}

private fun generateSprite(vertices: FloatArray, triangleIndices: IntArray): SpriteOutput {
    val vao = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vao)

    val vbo = GL30.glGenBuffers()
    GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
    GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW)

    val ebo = GL30.glGenBuffers()
    GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo)
    GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, triangleIndices, GL30.GL_STATIC_DRAW)

    GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 5 * 4, 0)

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
    GL30.glBindVertexArray(vao)
    GL30.glEnableVertexAttribArray(0)
    GL30.glEnableVertexAttribArray(1)

    GL30.glDrawElements(GL30.GL_TRIANGLES, 6, GL30.GL_UNSIGNED_INT, 0)

    GL30.glDisableVertexAttribArray(1)
    GL30.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
}

data class SpriteOutput(val vao: Int, val vbo: Int, val ebo: Int)