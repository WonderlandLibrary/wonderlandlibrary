package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event

/*
 * Used to draw custom framebuffer to the main framebuffer
 */
abstract class EventDraw : Event() {
    class EntityOutline(val tickDelta: Float) : EventDraw()
}