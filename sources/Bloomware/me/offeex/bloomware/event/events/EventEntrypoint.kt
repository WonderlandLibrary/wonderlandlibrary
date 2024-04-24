package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event

abstract class EventEntrypoint : Event() {
    class Main : EventEntrypoint()
    class SetOverlay : EventEntrypoint()
    class Stop : EventEntrypoint()
}