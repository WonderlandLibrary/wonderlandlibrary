// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event;

public abstract class Event
{
    protected boolean cancelled;
    
    public void fire() {
        this.cancelled = false;
        EventSystem.fire(this);
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
}
