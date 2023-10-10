package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities {
   public boolean disableDamage;
   public boolean allowFlying;
   public boolean isCreativeMode;
   public boolean isFlying;
   private float flySpeed = 0.05F;
   public boolean allowEdit = true;
   private float walkSpeed = 0.1F;

   public void setPlayerWalkSpeed(float var1) {
      this.walkSpeed = var1;
   }

   public void writeCapabilitiesToNBT(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      var2.setBoolean("invulnerable", this.disableDamage);
      var2.setBoolean("flying", this.isFlying);
      var2.setBoolean("mayfly", this.allowFlying);
      var2.setBoolean("instabuild", this.isCreativeMode);
      var2.setBoolean("mayBuild", this.allowEdit);
      var2.setFloat("flySpeed", this.flySpeed);
      var2.setFloat("walkSpeed", this.walkSpeed);
      var1.setTag("abilities", var2);
   }

   public float getWalkSpeed() {
      return this.walkSpeed;
   }

   public void readCapabilitiesFromNBT(NBTTagCompound var1) {
      if (var1.hasKey("abilities", 10)) {
         NBTTagCompound var2 = var1.getCompoundTag("abilities");
         this.disableDamage = var2.getBoolean("invulnerable");
         this.isFlying = var2.getBoolean("flying");
         this.allowFlying = var2.getBoolean("mayfly");
         this.isCreativeMode = var2.getBoolean("instabuild");
         if (var2.hasKey("flySpeed", 99)) {
            this.flySpeed = var2.getFloat("flySpeed");
            this.walkSpeed = var2.getFloat("walkSpeed");
         }

         if (var2.hasKey("mayBuild", 1)) {
            this.allowEdit = var2.getBoolean("mayBuild");
         }
      }

   }

   public float getFlySpeed() {
      return this.flySpeed;
   }

   public void setFlySpeed(float var1) {
      this.flySpeed = var1;
   }
}
