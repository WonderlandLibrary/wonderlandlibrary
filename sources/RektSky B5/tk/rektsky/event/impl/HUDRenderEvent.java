/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import tk.rektsky.event.Event;

public class HUDRenderEvent
extends Event {
    private GuiIngame gui;
    private ScaledResolution scaledResolution;

    public HUDRenderEvent(ScaledResolution sr, GuiIngame gui) {
        this.gui = gui;
        this.scaledResolution = sr;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public GuiIngame getGui() {
        return this.gui;
    }
}

