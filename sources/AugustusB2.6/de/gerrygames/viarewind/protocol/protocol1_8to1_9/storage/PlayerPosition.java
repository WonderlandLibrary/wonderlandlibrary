// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

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
    private int confirmId;
    
    public PlayerPosition(final UserConnection user) {
        super(user);
        this.confirmId = -1;
    }
    
    public void setPos(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw % 360.0f;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch % 360.0f;
    }
    
    public double getPosX() {
        return this.posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public int getConfirmId() {
        return this.confirmId;
    }
    
    public void setPosX(final double posX) {
        this.posX = posX;
    }
    
    public void setPosY(final double posY) {
        this.posY = posY;
    }
    
    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    public void setConfirmId(final int confirmId) {
        this.confirmId = confirmId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerPosition)) {
            return false;
        }
        final PlayerPosition other = (PlayerPosition)o;
        return other.canEqual(this) && Double.compare(this.getPosX(), other.getPosX()) == 0 && Double.compare(this.getPosY(), other.getPosY()) == 0 && Double.compare(this.getPosZ(), other.getPosZ()) == 0 && Float.compare(this.getYaw(), other.getYaw()) == 0 && Float.compare(this.getPitch(), other.getPitch()) == 0 && this.isOnGround() == other.isOnGround() && this.getConfirmId() == other.getConfirmId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerPosition;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $posX = Double.doubleToLongBits(this.getPosX());
        result = result * 59 + (int)($posX >>> 32 ^ $posX);
        final long $posY = Double.doubleToLongBits(this.getPosY());
        result = result * 59 + (int)($posY >>> 32 ^ $posY);
        final long $posZ = Double.doubleToLongBits(this.getPosZ());
        result = result * 59 + (int)($posZ >>> 32 ^ $posZ);
        result = result * 59 + Float.floatToIntBits(this.getYaw());
        result = result * 59 + Float.floatToIntBits(this.getPitch());
        result = result * 59 + (this.isOnGround() ? 79 : 97);
        result = result * 59 + this.getConfirmId();
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerPosition(posX=" + this.getPosX() + ", posY=" + this.getPosY() + ", posZ=" + this.getPosZ() + ", yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ", onGround=" + this.isOnGround() + ", confirmId=" + this.getConfirmId() + ")";
    }
}
