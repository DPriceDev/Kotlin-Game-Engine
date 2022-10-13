package dev.dprice.game.engine.ecs.model

interface System {

    fun run(timeSinceLast: Double)
}