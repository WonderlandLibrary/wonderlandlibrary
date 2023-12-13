// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.event.events;

import slavikcodd3r.rainbow.event.Event;

public class SprintEvent extends Event
{
    private boolean sprinting;
    
    public SprintEvent(final boolean sprinting) {
        this.setSprinting(sprinting);
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
}
