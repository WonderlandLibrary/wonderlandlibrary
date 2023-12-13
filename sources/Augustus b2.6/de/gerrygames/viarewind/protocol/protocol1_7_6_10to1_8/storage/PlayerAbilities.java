// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.connection.StoredObject;

public class PlayerAbilities extends StoredObject
{
    private boolean sprinting;
    private boolean allowFly;
    private boolean flying;
    private boolean invincible;
    private boolean creative;
    private float flySpeed;
    private float walkSpeed;
    
    public PlayerAbilities(final UserConnection user) {
        super(user);
    }
    
    public byte getFlags() {
        byte flags = 0;
        if (this.invincible) {
            flags |= 0x8;
        }
        if (this.allowFly) {
            flags |= 0x4;
        }
        if (this.flying) {
            flags |= 0x2;
        }
        if (this.creative) {
            flags |= 0x1;
        }
        return flags;
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public boolean isAllowFly() {
        return this.allowFly;
    }
    
    public boolean isFlying() {
        return this.flying;
    }
    
    public boolean isInvincible() {
        return this.invincible;
    }
    
    public boolean isCreative() {
        return this.creative;
    }
    
    public float getFlySpeed() {
        return this.flySpeed;
    }
    
    public float getWalkSpeed() {
        return this.walkSpeed;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
    
    public void setAllowFly(final boolean allowFly) {
        this.allowFly = allowFly;
    }
    
    public void setFlying(final boolean flying) {
        this.flying = flying;
    }
    
    public void setInvincible(final boolean invincible) {
        this.invincible = invincible;
    }
    
    public void setCreative(final boolean creative) {
        this.creative = creative;
    }
    
    public void setFlySpeed(final float flySpeed) {
        this.flySpeed = flySpeed;
    }
    
    public void setWalkSpeed(final float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerAbilities)) {
            return false;
        }
        final PlayerAbilities other = (PlayerAbilities)o;
        return other.canEqual(this) && this.isSprinting() == other.isSprinting() && this.isAllowFly() == other.isAllowFly() && this.isFlying() == other.isFlying() && this.isInvincible() == other.isInvincible() && this.isCreative() == other.isCreative() && Float.compare(this.getFlySpeed(), other.getFlySpeed()) == 0 && Float.compare(this.getWalkSpeed(), other.getWalkSpeed()) == 0;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerAbilities;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSprinting() ? 79 : 97);
        result = result * 59 + (this.isAllowFly() ? 79 : 97);
        result = result * 59 + (this.isFlying() ? 79 : 97);
        result = result * 59 + (this.isInvincible() ? 79 : 97);
        result = result * 59 + (this.isCreative() ? 79 : 97);
        result = result * 59 + Float.floatToIntBits(this.getFlySpeed());
        result = result * 59 + Float.floatToIntBits(this.getWalkSpeed());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerAbilities(sprinting=" + this.isSprinting() + ", allowFly=" + this.isAllowFly() + ", flying=" + this.isFlying() + ", invincible=" + this.isInvincible() + ", creative=" + this.isCreative() + ", flySpeed=" + this.getFlySpeed() + ", walkSpeed=" + this.getWalkSpeed() + ")";
    }
}
