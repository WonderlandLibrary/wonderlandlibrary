// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.render;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventRenderToolTip extends EventCancellable
{
    private final aip stack;
    private final int x;
    private final int y;
    
    public EventRenderToolTip(final aip stack, final int x, final int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }
    
    public aip getStack() {
        return this.stack;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
}
