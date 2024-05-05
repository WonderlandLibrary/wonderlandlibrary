package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.client.gui.*;

public class EventRender2D extends Event
{
    public ScaledResolution sr;
    private int width;
    private int height;
    float partialTicks;
    
    public EventRender2D(final ScaledResolution sr, final float ticks, final int width, final int height) {
        this.partialTicks = ticks;
        this.sr = sr;
        this.width = width;
        this.height = height;
    }
    
    public float getTicks() {
        return this.partialTicks;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
}
