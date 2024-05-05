package com.initial.events.impl;

import com.initial.events.*;

public class Event2D extends Event
{
    private float width;
    private float height;
    
    public Event2D(final float width, final float height) {
        this.width = width;
        this.height = height;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
}
