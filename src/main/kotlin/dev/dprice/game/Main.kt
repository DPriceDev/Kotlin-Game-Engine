import dev.dprice.game.di.AppModule
import dev.dprice.game.ecs.ECS
import dev.dprice.game.entities.character.Character
import dev.dprice.game.entities.character.di.characterModule
import dev.dprice.game.systems.di.systemsModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*


private var window: Long = 0

fun main(args: Array<String>) {
    run()
}

private fun run() {
    initDI()

    // Initialize the window and openGL
    init()

    // Main loop of program, runs until termination
    loop()

    // Destroy the window and remove callbacks
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    // Terminate opengl and clear error handler
    glfwTerminate()
    glfwSetErrorCallback(null)?.free()
}

private fun initDI() {
    startKoin {
//        // declare used logger
//        logger()

        modules(systemsModule, characterModule, AppModule().module)
    }
}

private fun init() {
    // Add error handler
    GLFWErrorCallback.createPrint(System.err).set()

    // Initialize opengl
    if (!glfwInit()) {
        error("Unable to initialize GLFW")
    }

    glfwDefaultWindowHints() // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)

    // Create the window of a size with the set title
    window = glfwCreateWindow(300, 300, "Hello World!", 0, 0)
    if (window == 0L) {
        error("Failed to create the GLFW window")
    }

    // Key callback for keyboard interactions
    glfwSetKeyCallback(window) { window, key, scancode, action, mods ->
        when {
            // Close the window if escape is pressed
            key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE -> glfwSetWindowShouldClose(window, true)
        }
    }

    // todo: Optional stack push here?

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)

    // Enable v-sync (lock to 60fps)
    glfwSwapInterval(1)

    // Make the window visible
    glfwShowWindow(window)
}

private fun loop() {

    // Bind LWJGL to the current opengl context
    GL.createCapabilities()

    // Set clear colour, i.e. set background colour
    glClearColor(0.2f, 0.3f, 0.3f, 1.0f)

    ECS.createEntity(Character()::onCreate)

    while (!glfwWindowShouldClose(window)) {

        // Clear the current frame buffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        ECS.run(0.0) // todo: Get delta time since last

        // Swap the current frame buffer to the back buffer
        glfwSwapBuffers(window)

        // check for input events
        glfwPollEvents()
    }
}