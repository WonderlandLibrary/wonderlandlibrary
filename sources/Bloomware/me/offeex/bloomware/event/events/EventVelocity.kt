package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.client.network.ClientPlayerEntity

abstract class EventVelocity(var x: Double, var y: Double, var z: Double) : Event() {
    class Player(val player: ClientPlayerEntity, x: Double, y: Double, z: Double) : EventVelocity(x, y, z)
    class Explosion(x: Double, y: Double, z: Double) : EventVelocity(x, y, z)
    class Fluid(x: Double, y: Double, z: Double) : EventVelocity(x, y, z)
    abstract class Push(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) : EventVelocity(x, y, z) {
        class Entities(val player: ClientPlayerEntity, x: Double, y: Double, z: Double) : Push(x, y, z)
        class Blocks : Push()
    }
}