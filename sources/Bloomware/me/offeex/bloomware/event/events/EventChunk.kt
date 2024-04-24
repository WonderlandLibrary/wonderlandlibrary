package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.world.chunk.WorldChunk

abstract class EventChunk(val chunk: WorldChunk) : Event() {
    class Load(chunk: WorldChunk) : EventChunk(chunk)
    class Unload(chunk: WorldChunk) : EventChunk(chunk)
}