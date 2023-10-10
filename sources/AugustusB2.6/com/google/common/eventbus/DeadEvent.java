// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
public class DeadEvent
{
    private final Object source;
    private final Object event;
    
    public DeadEvent(final Object source, final Object event) {
        this.source = Preconditions.checkNotNull(source);
        this.event = Preconditions.checkNotNull(event);
    }
    
    public Object getSource() {
        return this.source;
    }
    
    public Object getEvent() {
        return this.event;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("source", this.source).add("event", this.event).toString();
    }
}
