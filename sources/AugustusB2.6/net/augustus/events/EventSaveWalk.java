// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

import net.minecraft.entity.Entity;

public class EventSaveWalk extends Event
{
    private double motionX;
    private double motionY;
    private double motionZ;
    private boolean saveWalk;
    private boolean disableSneak;
    private Entity entity;
    
    public EventSaveWalk(final double x, final double y, final double z, final Entity entity) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }
    
    public boolean isDisableSneak() {
        return this.disableSneak;
    }
    
    public void setDisableSneak(final boolean disableSneak) {
        this.disableSneak = disableSneak;
    }
    
    public double getMotionX() {
        return this.motionX;
    }
    
    public void setMotionX(final double motionX) {
        this.motionX = motionX;
    }
    
    public double getMotionY() {
        return this.motionY;
    }
    
    public void setMotionY(final double motionY) {
        this.motionY = motionY;
    }
    
    public double getMotionZ() {
        return this.motionZ;
    }
    
    public void setMotionZ(final double motionZ) {
        this.motionZ = motionZ;
    }
    
    public boolean isSaveWalk() {
        return this.saveWalk;
    }
    
    public void setSaveWalk(final boolean saveWalk) {
        this.saveWalk = saveWalk;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
}
