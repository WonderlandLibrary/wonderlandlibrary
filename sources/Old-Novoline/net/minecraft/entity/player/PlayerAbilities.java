package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerAbilities {

    private boolean disabledDamage;
    private boolean isFlying;
    private boolean allowFlying;
    private boolean isCreative;
    private boolean allowEdit = true;
    private float flySpeed = 0.05F;
    private float walkSpeed = 0.1F;

    public void writeCapabilitiesToNBT(NBTTagCompound tagCompound) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("invulnerable", this.disabledDamage);
        nbttagcompound.setBoolean("flying", this.isFlying);
        nbttagcompound.setBoolean("mayfly", this.allowFlying);
        nbttagcompound.setBoolean("instabuild", this.isCreative);
        nbttagcompound.setBoolean("mayBuild", this.allowEdit);
        nbttagcompound.setFloat("flySpeed", this.flySpeed);
        nbttagcompound.setFloat("walkSpeed", this.walkSpeed);
        tagCompound.setTag("abilities", nbttagcompound);
    }

    public void readCapabilitiesFromNBT(NBTTagCompound tagCompound) {
        if (tagCompound.hasKey("abilities", 10)) {
            NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("abilities");
            this.disabledDamage = nbttagcompound.getBoolean("invulnerable");
            this.isFlying = nbttagcompound.getBoolean("flying");
            this.allowFlying = nbttagcompound.getBoolean("mayfly");
            this.isCreative = nbttagcompound.getBoolean("instabuild");

            if (nbttagcompound.hasKey("flySpeed", 99)) {
                this.flySpeed = nbttagcompound.getFloat("flySpeed");
                this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
            }

            if (nbttagcompound.hasKey("mayBuild", 1)) {
                this.allowEdit = nbttagcompound.getBoolean("mayBuild");
            }
        }
    }

    public void setDisabledDamage(boolean disabledDamage) {
        this.disabledDamage = disabledDamage;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public void setAllowFlying(boolean allowFlying) {
        this.allowFlying = allowFlying;
    }

    public void setCreative(boolean creative) {
        isCreative = creative;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public boolean isDisabledDamage() {
        return disabledDamage;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public boolean isAllowFlying() {
        return allowFlying;
    }

    public boolean isCreative() {
        return isCreative;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }
}
