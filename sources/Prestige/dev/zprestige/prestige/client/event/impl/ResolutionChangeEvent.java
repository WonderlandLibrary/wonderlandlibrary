package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.client.util.Window;

public class ResolutionChangeEvent extends Event {
    public Window window;

    public ResolutionChangeEvent(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return this.window;
    }
}
