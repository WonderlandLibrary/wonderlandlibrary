// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import dev.tenacity.utils.player.MovementUtils;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

public class MoveEvent extends Event
{
    private double x;
    private double y;
    private double z;
    
    public MoveEvent(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
    public void setSpeed(final double speed) {
        MovementUtils.setSpeed(this, speed);
    }
}
