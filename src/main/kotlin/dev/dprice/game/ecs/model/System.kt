package dev.dprice.game.ecs.model

interface System {

    fun run(timeSinceLast: Double)
}