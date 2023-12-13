// 
// Decompiled by Procyon v0.5.36
// 

package net.lenni0451.eventapi.events.premade;

import net.lenni0451.eventapi.events.types.ITyped;
import net.lenni0451.eventapi.events.IEvent;

public class EventTyped implements IEvent, ITyped
{
    private final byte type;
    
    protected EventTyped(final byte type) {
        this.type = type;
    }
    
    @Override
    public byte getType() {
        return this.type;
    }
}
