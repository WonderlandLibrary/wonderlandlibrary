package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob extends EntityLiving implements IAnimals {
   public EntityWaterMob(World worldIn) {
      super(worldIn);
   }

   protected int getExperiencePoints(EntityPlayer player) {
      return 1 + this.worldObj.rand.nextInt(3);
   }

   public void onEntityUpdate() {
      int i = this.getAir();
      super.onEntityUpdate();
      if(this.isEntityAlive() && !this.isInWater()) {
         --i;
         this.setAir(i);
         if(this.getAir() == -20) {
            this.setAir(0);
            this.attackEntityFrom(DamageSource.drown, 2.0F);
         }
      } else {
         this.setAir(300);
      }

   }

   public boolean canBreatheUnderwater() {
      return true;
   }

   public boolean isPushedByWater() {
      return false;
   }

   protected boolean canDespawn() {
      return true;
   }

   public boolean getCanSpawnHere() {
      return true;
   }

   public int getTalkInterval() {
      return 120;
   }

   public boolean isNotColliding() {
      return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this);
   }
}
