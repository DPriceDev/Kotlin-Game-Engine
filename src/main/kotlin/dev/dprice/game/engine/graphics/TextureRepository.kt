package dev.dprice.game.engine.graphics

import org.koin.core.annotation.Single
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import java.nio.file.Paths

data class LoadedTexture(
    val id: Int,
    val width: Int,
    val height: Int
) {

    fun bind() {
        GL30.glEnable(GL30.GL_BLEND)
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)

        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)

        GL30.glActiveTexture(GL30.GL_TEXTURE0)
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, id)
    }
}

interface TextureRepository {
    fun getTexture(path: String) : LoadedTexture
}

@Single
class TextureRepositoryImpl : TextureRepository {
    private val textures: MutableMap<String, LoadedTexture> = mutableMapOf()

    override fun getTexture(path: String) : LoadedTexture {
        return textures.getOrElse(path) {
            loadTexture(path).apply {
                textures[path] = this
            }
        }
    }

    private fun loadTexture(textureFile: String): LoadedTexture {
        val texture = GL30.glGenTextures()
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture)

        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST_MIPMAP_LINEAR)
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST)

        val x = IntArray(1)
        val y = IntArray(1)
        val nrChannels = IntArray(1)
        val texturePath = Paths.get("").toAbsolutePath().toString() + "/src/main/resources" + textureFile
        STBImage.stbi_set_flip_vertically_on_load(true)
        val data = STBImage.stbi_load(texturePath, x, y, nrChannels, 0)

        data?.let {
            val format = if (nrChannels.first() == 4) GL30.GL_RGBA else GL30.GL_RGB
            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, format, x[0], y[0], 0, format, GL30.GL_UNSIGNED_BYTE, it)
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D)

            STBImage.stbi_image_free(it)
        } ?: error("Failed to load texture")

        return LoadedTexture(texture, x.first(), y.first())
    }
}