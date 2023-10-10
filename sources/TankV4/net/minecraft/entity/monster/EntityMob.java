package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class EntityMob extends EntityCreature implements IMob {
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
   }

   public void onLivingUpdate() {
      this.updateArmSwingProgress();
      float var1 = this.getBrightness(1.0F);
      if (var1 > 0.5F) {
         this.entityAge += 2;
      }

      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      }

   }

   protected String getHurtSound() {
      return "game.hostile.hurt";
   }

   protected String getSwimSound() {
      return "game.hostile.swim";
   }

   protected String getFallSoundString(int var1) {
      return var1 > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
   }

   public EntityMob(World var1) {
      super(var1);
      this.experienceValue = 5;
   }

   public boolean getCanSpawnHere() {
      // $FF: Couldn't be decompiled
   }

   protected String getDeathSound() {
      return "game.hostile.die";
   }

   public float getBlockPathWeight(BlockPos var1) {
      return 0.5F - this.worldObj.getLightBrightness(var1);
   }

   public boolean attackEntityAsMob(Entity var1) {
      float var2 = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int var3 = 0;
      if (var1 instanceof EntityLivingBase) {
         var2 += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)var1).getCreatureAttribute());
         var3 += EnchantmentHelper.getKnockbackModifier(this);
      }

      boolean var4 = var1.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
      if (var4) {
         if (var3 > 0) {
            var1.addVelocity((double)(-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * (float)var3 * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * (float)var3 * 0.5F));
            this.motionX *= 0.6D;
            this.motionZ *= 0.6D;
         }

         int var5 = EnchantmentHelper.getFireAspectModifier(this);
         if (var5 > 0) {
            var1.setFire(var5 * 4);
         }

         this.applyEnchantments(this, var1);
      }

      return var4;
   }

   protected String getSplashSound() {
      return "game.hostile.swim.splash";
   }

   protected boolean canDropLoot() {
      return true;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else if (!super.attackEntityFrom(var1, var2)) {
         return false;
      } else {
         Entity var3 = var1.getEntity();
         return this.riddenByEntity != var3 && this.ridingEntity != var3 ? true : true;
      }
   }
}
