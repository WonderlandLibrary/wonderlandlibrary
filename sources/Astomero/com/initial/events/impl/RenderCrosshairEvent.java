package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.client.gui.*;

public final class RenderCrosshairEvent extends Event
{
    private final ScaledResolution sr;
    
    public RenderCrosshairEvent(final ScaledResolution sr) {
        this.sr = sr;
    }
    
    public ScaledResolution getScaledRes() {
        return this.sr;
    }
}
