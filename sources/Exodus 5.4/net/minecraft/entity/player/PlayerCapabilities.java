/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities {
    public boolean isFlying;
    public boolean allowEdit = true;
    private float flySpeed = 0.05f;
    public boolean isCreativeMode;
    public boolean disableDamage;
    public boolean allowFlying;
    private float walkSpeed = 0.1f;

    public void setPlayerWalkSpeed(float f) {
        this.walkSpeed = f;
    }

    public void writeCapabilitiesToNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        nBTTagCompound2.setBoolean("invulnerable", this.disableDamage);
        nBTTagCompound2.setBoolean("flying", this.isFlying);
        nBTTagCompound2.setBoolean("mayfly", this.allowFlying);
        nBTTagCompound2.setBoolean("instabuild", this.isCreativeMode);
        nBTTagCompound2.setBoolean("mayBuild", this.allowEdit);
        nBTTagCompound2.setFloat("flySpeed", this.flySpeed);
        nBTTagCompound2.setFloat("walkSpeed", this.walkSpeed);
        nBTTagCompound.setTag("abilities", nBTTagCompound2);
    }

    public void readCapabilitiesFromNBT(NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound.hasKey("abilities", 10)) {
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("abilities");
            this.disableDamage = nBTTagCompound2.getBoolean("invulnerable");
            this.isFlying = nBTTagCompound2.getBoolean("flying");
            this.allowFlying = nBTTagCompound2.getBoolean("mayfly");
            this.isCreativeMode = nBTTagCompound2.getBoolean("instabuild");
            if (nBTTagCompound2.hasKey("flySpeed", 99)) {
                this.flySpeed = nBTTagCompound2.getFloat("flySpeed");
                this.walkSpeed = nBTTagCompound2.getFloat("walkSpeed");
            }
            if (nBTTagCompound2.hasKey("mayBuild", 1)) {
                this.allowEdit = nBTTagCompound2.getBoolean("mayBuild");
            }
        }
    }

    public float getWalkSpeed() {
        return this.walkSpeed;
    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public void setFlySpeed(float f) {
        this.flySpeed = f;
    }
}

