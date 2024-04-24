package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.world.ClientWorld

abstract class EventWorld : Event() {
    class Join(val world: ClientWorld) : EventWorld()
    class Leave(val screen: Screen) : EventWorld()
}