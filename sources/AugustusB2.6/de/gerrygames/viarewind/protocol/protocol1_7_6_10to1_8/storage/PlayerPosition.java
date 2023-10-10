// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.connection.StoredObject;

public class PlayerPosition extends StoredObject
{
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean positionPacketReceived;
    private double receivedPosY;
    
    public PlayerPosition(final UserConnection user) {
        super(user);
    }
    
    public void setPos(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }
    
    public boolean isPositionPacketReceived() {
        return this.positionPacketReceived;
    }
    
    public void setPositionPacketReceived(final boolean positionPacketReceived) {
        this.positionPacketReceived = positionPacketReceived;
    }
    
    public double getReceivedPosY() {
        return this.receivedPosY;
    }
    
    public void setReceivedPosY(final double receivedPosY) {
        this.receivedPosY = receivedPosY;
    }
    
    public double getPosX() {
        return this.posX;
    }
    
    public void setPosX(final double posX) {
        this.posX = posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public void setPosY(final double posY) {
        this.posY = posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
}
