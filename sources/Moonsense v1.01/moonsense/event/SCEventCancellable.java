// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event;

public class SCEventCancellable extends SCEvent
{
    private boolean cancelled;
    
    public SCEventCancellable() {
        this.cancelled = false;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
