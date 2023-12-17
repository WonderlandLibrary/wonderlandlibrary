// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import ru.tuskevich.event.events.Event;

public class EventEntityBoundingBox implements Event
{
    private float size;
    
    public float getSize() {
        return this.size;
    }
    
    public void setSize(final float size) {
        this.size = size;
    }
}
