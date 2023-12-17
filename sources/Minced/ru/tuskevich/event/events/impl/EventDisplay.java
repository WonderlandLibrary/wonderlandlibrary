// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.event.events.Event;

public class EventDisplay implements Event
{
    public float ticks;
    public ScaledResolution sr;
    
    public EventDisplay(final float t, final ScaledResolution sr) {
        this.sr = sr;
        this.ticks = t;
    }
}
