package net.minecraft.entity.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball {
   protected float getMotionFactor() {
      return this != false ? 0.73F : super.getMotionFactor();
   }

   public boolean isBurning() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public void setInvulnerable(boolean var1) {
      this.dataWatcher.updateObject(10, (byte)(var1 ? 1 : 0));
   }

   protected void onImpact(MovingObjectPosition var1) {
      if (!this.worldObj.isRemote) {
         if (var1.entityHit != null) {
            if (this.shootingEntity != null) {
               if (var1.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F)) {
                  if (!var1.entityHit.isEntityAlive()) {
                     this.shootingEntity.heal(5.0F);
                  } else {
                     this.applyEnchantments(this.shootingEntity, var1.entityHit);
                  }
               }
            } else {
               var1.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
            }

            if (var1.entityHit instanceof EntityLivingBase) {
               byte var2 = 0;
               if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                  var2 = 10;
               } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                  var2 = 40;
               }

               if (var2 > 0) {
                  ((EntityLivingBase)var1.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * var2, 1));
               }
            }
         }

         this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
         this.setDead();
      }

   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return false;
   }

   public EntityWitherSkull(World var1) {
      super(var1);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntityWitherSkull(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.setSize(0.3125F, 0.3125F);
   }

   public float getExplosionResistance(Explosion param1, World param2, BlockPos param3, IBlockState param4) {
      // $FF: Couldn't be decompiled
   }

   protected void entityInit() {
      this.dataWatcher.addObject(10, (byte)0);
   }

   public EntityWitherSkull(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(var1, var2, var3, var5, var7);
      this.setSize(0.3125F, 0.3125F);
   }
}
