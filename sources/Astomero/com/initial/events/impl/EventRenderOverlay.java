package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;

public class EventRenderOverlay extends Event
{
    private final ScaledResolution resolution;
    
    public EventRenderOverlay() {
        this.resolution = new ScaledResolution(Minecraft.getMinecraft());
    }
    
    public ScaledResolution getResolution() {
        return this.resolution;
    }
}
