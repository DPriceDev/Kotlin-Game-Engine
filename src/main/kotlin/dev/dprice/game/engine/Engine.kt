package dev.dprice.game.engine

import dev.dprice.game.engine.di.EngineModule
import dev.dprice.game.engine.di.componentsModule
import dev.dprice.game.engine.di.systemsModule
import dev.dprice.game.engine.ecs.ECS
import dev.dprice.game.engine.graphics.model.Window
import dev.dprice.game.engine.input.InputRepository
import dev.dprice.game.engine.input.model.InputAction
import org.koin.core.context.GlobalContext
import org.koin.ksp.generated.module
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11

fun runGame(setup: GameBuilder.() -> Unit) {
    GlobalContext.startKoin {
        modules(
            systemsModule,
            EngineModule().module,
            componentsModule
        )
    }

    val inputRepository = GlobalContext.get().get<InputRepository>()

    GameBuilder()
        .apply(setup)
        .build(ECS, inputRepository)

    runGame(inputRepository)
}

private fun runGame(inputRepository: InputRepository) {
    initializeGLFW()
    val window = initializeWindow()
    setupInputCallback(window, inputRepository)
    setActiveWindow(window)

    // blocks until window is closed
    gameLoop(window)

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
    val id = GLFW.glfwCreateWindow(800, 600, "Hello World!", 0, 0)
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

private fun gameLoop(window: Window) {
    // Bind LWJGL to the current opengl context
    GL.createCapabilities()

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
        ECS.run(delta)

        // Swap the current frame buffer to the back buffer
        GLFW.glfwSwapBuffers(window.id)

        // check for input events
        GLFW.glfwPollEvents()
    }
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