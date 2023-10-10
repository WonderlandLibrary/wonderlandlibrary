// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.connection.StorableObject;

public abstract class PlayerPositionStorage implements StorableObject
{
    private double x;
    private double y;
    private double z;
    
    protected PlayerPositionStorage() {
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setCoordinates(final PacketWrapper wrapper, final boolean relative) throws Exception {
        this.setCoordinates(wrapper.get((Type<Double>)Type.DOUBLE, 0), wrapper.get((Type<Double>)Type.DOUBLE, 1), wrapper.get((Type<Double>)Type.DOUBLE, 2), relative);
    }
    
    public void setCoordinates(final double x, final double y, final double z, final boolean relative) {
        if (relative) {
            this.x += x;
            this.y += y;
            this.z += z;
        }
        else {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
