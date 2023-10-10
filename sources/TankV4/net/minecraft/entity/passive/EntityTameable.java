package net.minecraft.entity.passive;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {
   protected EntityAISit aiSit = new EntityAISit(this);

   public void handleStatusUpdate(byte var1) {
      if (var1 == 7) {
         this.playTameEffect(true);
      } else if (var1 == 6) {
         this.playTameEffect(false);
      } else {
         super.handleStatusUpdate(var1);
      }

   }

   public void setSitting(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }

   }

   public void setTamed(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 4));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -5));
      }

      this.setupTamedAI();
   }

   public Team getTeam() {
      if (this != false) {
         EntityLivingBase var1 = this.getOwner();
         if (var1 != null) {
            return var1.getTeam();
         }
      }

      return super.getTeam();
   }

   protected void playTameEffect(boolean var1) {
      EnumParticleTypes var2 = EnumParticleTypes.HEART;
      if (!var1) {
         var2 = EnumParticleTypes.SMOKE_NORMAL;
      }

      for(int var3 = 0; var3 < 7; ++var3) {
         double var4 = this.rand.nextGaussian() * 0.02D;
         double var6 = this.rand.nextGaussian() * 0.02D;
         double var8 = this.rand.nextGaussian() * 0.02D;
         this.worldObj.spawnParticle(var2, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
      }

   }

   public void onDeath(DamageSource var1) {
      if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages") && this.hasCustomName() && this.getOwner() instanceof EntityPlayerMP) {
         ((EntityPlayerMP)this.getOwner()).addChatMessage(this.getCombatTracker().getDeathMessage());
      }

      super.onDeath(var1);
   }

   public void setOwnerId(String var1) {
      this.dataWatcher.updateObject(17, var1);
   }

   public boolean isOwner(EntityLivingBase var1) {
      return var1 == this.getOwner();
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
      this.dataWatcher.addObject(17, "");
   }

   public Entity getOwner() {
      return this.getOwner();
   }

   public boolean shouldAttackEntity(EntityLivingBase var1, EntityLivingBase var2) {
      return true;
   }

   public EntityLivingBase getOwner() {
      try {
         UUID var1 = UUID.fromString(this.getOwnerId());
         return var1 == null ? null : this.worldObj.getPlayerEntityByUUID(var1);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      String var2 = "";
      if (var1.hasKey("OwnerUUID", 8)) {
         var2 = var1.getString("OwnerUUID");
      } else {
         String var3 = var1.getString("Owner");
         var2 = PreYggdrasilConverter.getStringUUIDFromName(var3);
      }

      if (var2.length() > 0) {
         this.setOwnerId(var2);
         this.setTamed(true);
      }

      this.aiSit.setSitting(var1.getBoolean("Sitting"));
      this.setSitting(var1.getBoolean("Sitting"));
   }

   public String getOwnerId() {
      return this.dataWatcher.getWatchableObjectString(17);
   }

   protected void setupTamedAI() {
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      if (this.getOwnerId() == null) {
         var1.setString("OwnerUUID", "");
      } else {
         var1.setString("OwnerUUID", this.getOwnerId());
      }

      var1.setBoolean("Sitting", this.isSitting());
   }

   public boolean isOnSameTeam(EntityLivingBase var1) {
      if (this != false) {
         EntityLivingBase var2 = this.getOwner();
         if (var1 == var2) {
            return true;
         }

         if (var2 != null) {
            return var2.isOnSameTeam(var1);
         }
      }

      return super.isOnSameTeam(var1);
   }

   public EntityAISit getAISit() {
      return this.aiSit;
   }

   public EntityTameable(World var1) {
      super(var1);
      this.setupTamedAI();
   }

   public boolean isSitting() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }
}
