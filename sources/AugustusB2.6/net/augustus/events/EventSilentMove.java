// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventSilentMove extends Event
{
    private boolean silent;
    private float yaw;
    private boolean advanced;
    
    public EventSilentMove(final float yaw) {
        this.yaw = yaw;
    }
    
    public boolean isSilent() {
        return this.silent;
    }
    
    public void setSilent(final boolean silent) {
        this.silent = silent;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public boolean isAdvanced() {
        return this.advanced;
    }
    
    public void setAdvanced(final boolean advanced) {
        this.advanced = advanced;
    }
}
