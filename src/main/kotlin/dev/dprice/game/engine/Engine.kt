package dev.dprice.game.engine

import dev.dprice.game.engine.di.EngineModule
import dev.dprice.game.engine.ecs.SystemRunner
import dev.dprice.game.engine.graphics.model.Window
import dev.dprice.game.engine.input.InputRepository
import dev.dprice.game.engine.input.model.InputAction
import dev.dprice.game.engine.levels.LevelLoader
import dev.dprice.game.engine.levels.LevelRepository
import org.koin.core.context.GlobalContext
import org.koin.ksp.generated.module
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

fun runGame(setup: GameBuilder.() -> Unit) {
    val koinApp = GlobalContext.startKoin {
        modules(EngineModule().module)
    }

    val inputRepository = koinApp.koin.get<InputRepository>()
    val systemRunner = koinApp.koin.get<SystemRunner>()
    val levelRepository = koinApp.koin.get<LevelRepository>()

    GameBuilder().apply(setup).build(levelRepository, inputRepository, koinApp)

    val levelLoader = koinApp.koin.get<LevelLoader>()

    runGame(inputRepository, levelLoader, systemRunner)
}

private fun runGame(
    inputRepository: InputRepository,
    levelLoader: LevelLoader,
    systemRunner: SystemRunner
) {
    initializeGLFW()
    val window = initializeWindow()
    setupInputCallback(window, inputRepository)
    setActiveWindow(window)

    levelLoader.loadStartLevel()

    // blocks until window is closed
    gameLoop(window, systemRunner)

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
    GLFW.glfwSwapInterval(1)

    // Make the window visible
    GLFW.glfwShowWindow(window.id)
}

private fun gameLoop(window: Window, systemRunner: SystemRunner) {
    // Bind LWJGL to the current opengl context
    GL.createCapabilities()

    setupOpenAl()

    // Set clear colour, i.e. set background colour
    GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)

    var time = System.nanoTime()

    while (!GLFW.glfwWindowShouldClose(window.id)) {

        // Calculate delta time
        val delta = (System.nanoTime() - time) / 1000000000.0
        time = System.nanoTime()

        // Clear the current frame buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        // Run the ecs systems
        val time2 = System.currentTimeMillis()
        systemRunner.run(delta)
        val deltaTwo = (System.currentTimeMillis() - time2) // 1000000000.0
        println(deltaTwo)

        // Swap the current frame buffer to the back buffer
        GLFW.glfwSwapBuffers(window.id)

        // check for input events
        GLFW.glfwPollEvents()
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