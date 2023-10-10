package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySnowball extends EntityThrowable {
   public EntitySnowball(World var1, EntityLivingBase var2) {
      super(var1, var2);
   }

   public EntitySnowball(World var1) {
      super(var1);
   }

   public EntitySnowball(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   protected void onImpact(MovingObjectPosition var1) {
      if (var1.entityHit != null) {
         byte var2 = 0;
         if (var1.entityHit instanceof EntityBlaze) {
            var2 = 3;
         }

         var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)var2);
      }

      for(int var3 = 0; var3 < 8; ++var3) {
         this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
      }

      if (!this.worldObj.isRemote) {
         this.setDead();
      }

   }
}
