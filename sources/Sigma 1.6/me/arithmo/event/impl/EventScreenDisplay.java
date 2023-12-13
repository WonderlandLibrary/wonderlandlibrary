/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventScreenDisplay
extends Event {
    private GuiScreen screen;

    public void fire(GuiScreen screen) {
        this.screen = screen;
        super.fire();
    }

    public GuiScreen getGuiScreen() {
        return this.screen;
    }
}

