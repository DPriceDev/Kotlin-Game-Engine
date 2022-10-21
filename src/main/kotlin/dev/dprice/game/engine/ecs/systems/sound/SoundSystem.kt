package dev.dprice.game.engine.ecs.systems.sound

import dev.dprice.game.engine.ecs.SystemRepository
import dev.dprice.game.engine.ecs.registerSystem
import org.lwjgl.BufferUtils
import org.lwjgl.PointerBuffer
import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename
import java.io.File
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.file.Paths
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem.getAudioInputStream
import dev.dprice.game.engine.ecs.getComponents

fun SystemRepository.createSoundSystem() = registerSystem<SoundComponent> {

    getComponents<SoundComponent>().filter { !it.isPlaying && it.shouldPlay }.forEach { sound ->

        val soundPath = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/" + sound.path
        val channels: IntBuffer = IntBuffer.allocate(1)
        val sampleRate: IntBuffer = IntBuffer.allocate(1)
        val output: PointerBuffer = PointerBuffer.allocateDirect(1)
        val length = stb_vorbis_decode_filename(soundPath, channels, sampleRate, output)

        val stream: AudioInputStream = getAudioInputStream(File(soundPath))
        val format = stream.format

        val byteArray = ByteArray(stream.available())
        stream.read(byteArray)
        val audioBuffer: ByteBuffer = BufferUtils.createByteBuffer(byteArray.size)
        audioBuffer.put(byteArray)
        stream.close()
        audioBuffer.flip()

        val alFormat = when (format.channels) {
            1 -> when (format.sampleSizeInBits) {
                8 -> AL_FORMAT_MONO8
                16 -> AL_FORMAT_MONO16
                else -> error("a")
            }

            2 -> when (format.sampleSizeInBits) {
                8 -> AL_FORMAT_STEREO8
                16 -> AL_FORMAT_STEREO16
                else -> error("b")
            }

            else -> error("c")
        }

        val buffer: Int = alGenBuffers()
        alBufferData(buffer, alFormat, audioBuffer, format.sampleRate.toInt())
        val source: Int = alGenSources()
        alSourcei(source, AL_BUFFER, buffer)
        alSourcei(source, AL_LOOPING, buffer)
        alSourcePlay(source)

        sound.isPlaying = true
    }
}
