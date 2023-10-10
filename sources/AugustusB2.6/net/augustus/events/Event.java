// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

import net.lenni0451.eventapi.events.IEvent;

public class Event implements IEvent
{
    private boolean canceled;
    
    public boolean isCanceled() {
        return this.canceled;
    }
    
    public void setCanceled(final boolean canceled) {
        this.canceled = canceled;
    }
}
