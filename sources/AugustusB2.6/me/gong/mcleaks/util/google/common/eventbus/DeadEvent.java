// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.eventbus;

import me.gong.mcleaks.util.google.common.base.MoreObjects;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
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
