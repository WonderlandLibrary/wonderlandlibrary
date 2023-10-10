package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable {
   private EntityLivingBase field_181555_c;

   public EntityEnderPearl(World var1, EntityLivingBase var2) {
      super(var1, var2);
      this.field_181555_c = var2;
   }

   public EntityEnderPearl(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public EntityEnderPearl(World var1) {
      super(var1);
   }

   public void onUpdate() {
      EntityLivingBase var1 = this.getThrower();
      if (var1 != null && var1 instanceof EntityPlayer && !var1.isEntityAlive()) {
         this.setDead();
      } else {
         super.onUpdate();
      }

   }

   protected void onImpact(MovingObjectPosition var1) {
      EntityLivingBase var2 = this.getThrower();
      if (var1.entityHit != null) {
         if (var1.entityHit == this.field_181555_c) {
            return;
         }

         var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, var2), 0.0F);
      }

      for(int var3 = 0; var3 < 32; ++var3) {
         this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
      }

      if (!this.worldObj.isRemote) {
         if (var2 instanceof EntityPlayerMP) {
            EntityPlayerMP var5 = (EntityPlayerMP)var2;
            if (var5.playerNetServerHandler.getNetworkManager().isChannelOpen() && var5.worldObj == this.worldObj && !var5.isPlayerSleeping()) {
               if (this.rand.nextFloat() < 0.05F && this.worldObj.getGameRules().getBoolean("doMobSpawning")) {
                  EntityEndermite var4 = new EntityEndermite(this.worldObj);
                  var4.setSpawnedByPlayer(true);
                  var4.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
                  this.worldObj.spawnEntityInWorld(var4);
               }

               if (var2.isRiding()) {
                  var2.mountEntity((Entity)null);
               }

               var2.setPositionAndUpdate(this.posX, this.posY, this.posZ);
               var2.fallDistance = 0.0F;
               var2.attackEntityFrom(DamageSource.fall, 5.0F);
            }
         } else if (var2 != null) {
            var2.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            var2.fallDistance = 0.0F;
         }

         this.setDead();
      }

   }
}
