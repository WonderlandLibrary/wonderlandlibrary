package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event

abstract class EventSprint : Event() {
    class ForwardMovement(var has: Boolean) : EventSprint()
    class JumpAcceleration(var yaw: Float) : EventSprint()
}