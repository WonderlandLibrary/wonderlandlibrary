package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.client.gui.screen.Screen

abstract class EventScreen(var screen: Screen?) : Event() {
    class Open(s: Screen?) : EventScreen(s)
    class Update(s: Screen?) : EventScreen(s)
    class Set(s: Screen?) : EventScreen(s)
}