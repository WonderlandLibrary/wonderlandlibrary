// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.render;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventRenderBlock extends EventCancellable
{
    private final awt state;
    private final et pos;
    private final amy access;
    private final buk bufferBuilder;
    
    public EventRenderBlock(final awt state, final et pos, final amy access, final buk bufferBuilder) {
        this.state = state;
        this.pos = pos;
        this.access = access;
        this.bufferBuilder = bufferBuilder;
    }
    
    public awt getState() {
        return this.state;
    }
    
    public buk getBufferBuilder() {
        return this.bufferBuilder;
    }
    
    public et getPos() {
        return this.pos;
    }
    
    public amy getAccess() {
        return this.access;
    }
}
