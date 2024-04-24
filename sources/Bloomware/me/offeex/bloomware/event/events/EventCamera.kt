package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import me.offeex.bloomware.event.EventReturnable
import net.minecraft.entity.Entity

abstract class EventCamera : EventReturnable() {
    class Rotation(var yaw: Float, var pitch: Float) : EventCamera()
    class Position(var x: Double, var y: Double, var z: Double) : EventCamera()
    class ClipToSpace(var desiredDistance: Double) : EventCamera()
}