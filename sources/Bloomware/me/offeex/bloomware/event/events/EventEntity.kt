package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.entity.Entity
import net.minecraft.entity.Entity.RemovalReason

abstract class EventEntity(val id: Int, val entity: Entity) : Event() {
    class Add(id: Int, e: Entity) : EventEntity(id, e)
    class Remove(id: Int, e: Entity, val reason: RemovalReason) : EventEntity(id, e)
}