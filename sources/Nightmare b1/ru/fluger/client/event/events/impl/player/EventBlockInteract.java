// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventBlockInteract extends EventCancellable
{
    private et pos;
    private fa face;
    
    public EventBlockInteract(final et pos, final fa face) {
        this.pos = pos;
        this.face = face;
    }
    
    public et getPos() {
        return this.pos;
    }
    
    public void setPos(final et pos) {
        this.pos = pos;
    }
    
    public fa getFace() {
        return this.face;
    }
    
    public void setFace(final fa face) {
        this.face = face;
    }
}
