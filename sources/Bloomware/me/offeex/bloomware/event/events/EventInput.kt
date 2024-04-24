package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.client.input.KeyboardInput
import net.minecraft.client.option.KeyBinding

abstract class EventInput : Event() {
    class Slowdown : EventInput()
    class Movement(val input: KeyboardInput) : EventInput()
    class Binding(val binding: KeyBinding, var pressed: Boolean) : EventInput()
    abstract class Key(val key: Int, val scanCode: Int) : EventInput() {
        class Press(key: Int, scanCode: Int) : Key(key, scanCode)
        class Release(key: Int, scanCode: Int) : Key(key, scanCode)
        class Hold(key: Int, scanCode: Int) : Key(key, scanCode)
    }
}