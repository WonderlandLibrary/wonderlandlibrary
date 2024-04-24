package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.util.hit.EntityHitResult

abstract class EventRaycast : Event() {
	class TargetedEntity(var entityHitResult: EntityHitResult?) : EventRaycast()
}