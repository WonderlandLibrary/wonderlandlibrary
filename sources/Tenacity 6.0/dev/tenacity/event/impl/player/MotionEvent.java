// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

@Exclude({ Strategy.NAME_REMAPPING })
public class MotionEvent extends StateEvent
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    
    public MotionEvent(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getX() {
        return this.x;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setX(final double x) {
        this.x = x;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getY() {
        return this.y;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setY(final double y) {
        this.y = y;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public double getZ() {
        return this.z;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setZ(final double z) {
        this.z = z;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getYaw() {
        return this.yaw;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getPitch() {
        return this.pitch;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean isOnGround() {
        return this.onGround;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setRotations(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
