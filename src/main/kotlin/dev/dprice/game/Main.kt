
import dev.dprice.game.di.AppModule
import dev.dprice.game.engine.ecs.ECS
import dev.dprice.game.entities.character.*
import dev.dprice.game.systems.di.componentsModule
import dev.dprice.game.systems.di.systemsModule
import dev.dprice.game.systems.input.model.InputAction
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

        modules(systemsModule, AppModule().module, componentsModule)
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
    window = glfwCreateWindow(800, 600, "Hello World!", 0, 0)
    if (window == 0L) {
        error("Failed to create the GLFW window")
    }

    // Key callback for keyboard interactions
    glfwSetKeyCallback(window) { window, key, scancode, action, mods ->

        ECS.registerInput(key, action)

//        when {
//            // Close the window if escape is pressed
//            key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE -> glfwSetWindowShouldClose(window, true)
//        }
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

    ECS.addInputMapping(InputAction(GLFW_KEY_W, GLFW_PRESS)) { inputAction -> MoveUp }
    ECS.addInputMapping(InputAction(GLFW_KEY_S, GLFW_PRESS)) { inputAction -> MoveDown }
    ECS.addInputMapping(InputAction(GLFW_KEY_A, GLFW_PRESS)) { inputAction -> MoveLeft }
    ECS.addInputMapping(InputAction(GLFW_KEY_D, GLFW_PRESS)) { inputAction -> MoveRight }


    var time = System.nanoTime()

    while (!glfwWindowShouldClose(window)) {
        val delta = (System.nanoTime() - time) / 1000000000.0
        time = System.nanoTime()

        // Clear the current frame buffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        ECS.run(delta) // todo: Get delta time since last

        // Swap the current frame buffer to the back buffer
        glfwSwapBuffers(window)

        // check for input events
        glfwPollEvents()
    }
}