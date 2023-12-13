package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball {
   public EntitySmallFireball(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(var1, var2, var3, var5, var7);
      this.setSize(0.3125F, 0.3125F);
   }

   protected void onImpact(MovingObjectPosition var1) {
      if (!this.worldObj.isRemote) {
         boolean var2;
         if (var1.entityHit != null) {
            var2 = var1.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);
            if (var2) {
               this.applyEnchantments(this.shootingEntity, var1.entityHit);
               if (!var1.entityHit.isImmuneToFire()) {
                  var1.entityHit.setFire(5);
               }
            }
         } else {
            var2 = true;
            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
               var2 = this.worldObj.getGameRules().getBoolean("mobGriefing");
            }

            if (var2) {
               BlockPos var3 = var1.getBlockPos().offset(var1.sideHit);
               if (this.worldObj.isAirBlock(var3)) {
                  this.worldObj.setBlockState(var3, Blocks.fire.getDefaultState());
               }
            }
         }

         this.setDead();
      }

   }

   public EntitySmallFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.setSize(0.3125F, 0.3125F);
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return false;
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public EntitySmallFireball(World var1) {
      super(var1);
      this.setSize(0.3125F, 0.3125F);
   }
}
