package com.initial.events.impl;

import net.minecraft.client.gui.*;

public class EventRenderGUI extends EventNigger<EventRenderGUI>
{
    public ScaledResolution sr;
    
    public EventRenderGUI(final ScaledResolution sr) {
        this.sr = sr;
    }
}
