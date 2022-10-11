package ecs.model

interface System {

    fun run(timeSinceLast: Double)
}