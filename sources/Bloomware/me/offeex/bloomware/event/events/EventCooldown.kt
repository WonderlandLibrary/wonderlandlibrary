package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event

abstract class EventCooldown(var cooldown: Int) : Event() {
    class BlockBreaking(cooldown: Int) : EventCooldown(cooldown)
}