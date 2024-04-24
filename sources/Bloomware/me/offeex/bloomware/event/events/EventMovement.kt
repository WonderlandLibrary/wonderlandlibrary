package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.entity.MovementType
import net.minecraft.util.math.Vec3d

abstract class EventMovement : Event() {
    class Step(var vec3d: Vec3d) : EventMovement()
    class Climbing(var vec3d: Vec3d) : EventMovement()
    class IsClimbing(var climbing: Boolean) : EventMovement()
    class Move(var velocity: Vec3d) : EventMovement()
    class Packets(val deltaX: Double, val deltaY: Double, val deltaZ: Double) : EventMovement()
}