package dev.dprice.game.engine

import dev.dprice.game.engine.di.EngineModule
import dev.dprice.game.engine.ecs.SystemRunner
import dev.dprice.game.engine.graphics.Renderer
import dev.dprice.game.engine.graphics.model.Window
import dev.dprice.game.engine.input.InputRepository
import dev.dprice.game.engine.input.model.InputAction
import dev.dprice.game.engine.levels.LevelLoader
import dev.dprice.game.engine.levels.LevelRepository
import kotlinx.coroutines.*
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.glfw.GLFW.glfwSetWindowTitle
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext

fun runGame(setup: GameBuilder.() -> Unit) = runBlocking {
    val koinApp = GlobalContext.startKoin {
        modules(EngineModule().module)

        modules(
            module {
//                single { RendererImpl() } bind Renderer::class

                single(named("main")) { newSingleThreadContext("main") } bind CoroutineDispatcher::class
                single(named("render")) { this@runBlocking.coroutineContext } bind CoroutineContext::class
                single(named("audio")) { newSingleThreadContext("audio") } bind CoroutineDispatcher::class
            }
        )
    }

    val inputRepository = koinApp.koin.get<InputRepository>()
    val systemRunner = koinApp.koin.get<SystemRunner>()
    val levelRepository = koinApp.koin.get<LevelRepository>()

    GameBuilder().apply(setup).build(levelRepository, inputRepository, koinApp)

    val levelLoader = koinApp.koin.get<LevelLoader>()

    val mainDispatcher = koinApp.koin.get<CoroutineContext>(named("render"))

    val renderer = koinApp.koin.get<Renderer>()

    runGame(inputRepository, levelLoader, systemRunner, mainDispatcher, renderer)
}

private suspend fun runGame(
    inputRepository: InputRepository,
    levelLoader: LevelLoader,
    systemRunner: SystemRunner,
    mainDispatcher: CoroutineContext,
    renderer: Renderer
) {
    initializeGLFW()

        val window = initializeWindow()
        setupInputCallback(window, inputRepository)
        setActiveWindow(window)

        levelLoader.loadStartLevel()

        // blocks until window is closed

        gameLoop(window, systemRunner, mainDispatcher, renderer)


        tearDownWindow(window)
        tearDownGLFW()
}

private fun initializeGLFW() {
    // Add error handler
    GLFWErrorCallback.createPrint(System.err).set()

    // Initialize opengl
    if (!GLFW.glfwInit()) {
        error("Unable to initialize GLFW")
    }
}

private fun setupInputCallback(window: Window, inputRepository: InputRepository) {
    // Key callback for keyboard interactions
    GLFW.glfwSetKeyCallback(window.id) { _, key, _, action, _ ->
        inputRepository.addInputAction(InputAction(key, action))
    }
}

private fun initializeWindow(): Window {
    GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2)

    // Create the window of a size with the set title
    val id = GLFW.glfwCreateWindow(600, 600, "Hello World!", 0, 0)
    if (id == 0L) {
        error("Failed to create the GLFW window")
    }
    return Window(id)
}

fun setActiveWindow(window: Window) {
    // Make the OpenGL context current
    GLFW.glfwMakeContextCurrent(window.id)

    // Enable v-sync (lock to 60fps)
    GLFW.glfwSwapInterval(0)

    // Make the window visible
    GLFW.glfwShowWindow(window.id)
}

private suspend fun gameLoop(
    window: Window,
    systemRunner: SystemRunner,
    mainDispatcher: CoroutineContext,
    renderer: Renderer
) = coroutineScope {
    // Bind LWJGL to the current opengl context
    GL.createCapabilities()

    setupOpenAl()

    // Set clear colour, i.e. set background colour
    GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)

    var time = glfwGetTime()

    while (!GLFW.glfwWindowShouldClose(window.id)) {

        // Calculate delta time
        val delta = (glfwGetTime() - time)
        time = glfwGetTime()

        // Clear the current frame buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        val deferred = async(mainDispatcher) {
            val systemTime = glfwGetTime()
            systemRunner.run(delta)
            println("System time: ${ glfwGetTime() - systemTime }")
        }

        val renderTime = glfwGetTime()
        renderer.renderFrame()
        println("Render time: ${ glfwGetTime() - renderTime }")

        deferred.await()

        showFPS(window)

        // Swap the current frame buffer to the back buffer
        GLFW.glfwSwapBuffers(window.id)

        // check for input events
        GLFW.glfwPollEvents()
    }
}

var lastTime = 0.0
var nbFrames = 0

fun showFPS(window: Window) {
    // Measure speed
    val currentTime = glfwGetTime()
    val delta = currentTime - lastTime
    nbFrames++
    if ( delta >= 1.0 ){ // If last cout was more than 1 sec ago
        val fps = nbFrames / delta

        glfwSetWindowTitle(window.id, "fps: $fps")
        println("fps: $fps")

        nbFrames = 0
        lastTime = currentTime
    }
}

private fun setupOpenAl() {
    val device: Long = ALC10.alcOpenDevice(null as ByteBuffer?)
    val deviceCaps = ALC.createCapabilities(device)

    val context: Long = ALC10.alcCreateContext(device, null as IntArray?)
    ALC10.alcMakeContextCurrent(context)
    AL.createCapabilities(deviceCaps)
}

private fun tearDownWindow(window: Window) {
    // Destroy the window and remove callbacks
    Callbacks.glfwFreeCallbacks(window.id)
    GLFW.glfwDestroyWindow(window.id)
}

private fun tearDownGLFW() {
    // Terminate opengl and clear error handler
    GLFW.glfwTerminate()
    GLFW.glfwSetErrorCallback(null)?.free()
}