package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBlaze extends EntityMob {
   private int heightOffsetUpdateTime;
   private float heightOffset = 0.5F;

   protected String getHurtSound() {
      return "mob.blaze.hit";
   }

   public float getBrightness(float var1) {
      return 1.0F;
   }

   public void fall(float var1, float var2) {
   }

   public boolean isBurning() {
      return this.func_70845_n();
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
   }

   public int getBrightnessForRender(float var1) {
      return 15728880;
   }

   protected void dropFewItems(boolean var1, int var2) {
      if (var1) {
         int var3 = this.rand.nextInt(2 + var2);

         for(int var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.blaze_rod, 1);
         }
      }

   }

   protected Item getDropItem() {
      return Items.blaze_rod;
   }

   public boolean func_70845_n() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void onLivingUpdate() {
      if (!this.onGround && this.motionY < 0.0D) {
         this.motionY *= 0.6D;
      }

      if (this.worldObj.isRemote) {
         if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
            this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
         }

         for(int var1 = 0; var1 < 2; ++var1) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
         }
      }

      super.onLivingUpdate();
   }

   public void setOnFire(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 &= -2;
      }

      this.dataWatcher.updateObject(16, var2);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   protected boolean isValidLightLevel() {
      return true;
   }

   protected void updateAITasks() {
      if (this.isWet()) {
         this.attackEntityFrom(DamageSource.drown, 1.0F);
      }

      --this.heightOffsetUpdateTime;
      if (this.heightOffsetUpdateTime <= 0) {
         this.heightOffsetUpdateTime = 100;
         this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
      }

      EntityLivingBase var1 = this.getAttackTarget();
      if (var1 != null && var1.posY + (double)var1.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset) {
         this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
         this.isAirBorne = true;
      }

      super.updateAITasks();
   }

   protected String getLivingSound() {
      return "mob.blaze.breathe";
   }

   protected String getDeathSound() {
      return "mob.blaze.death";
   }

   public EntityBlaze(World var1) {
      super(var1);
      this.isImmuneToFire = true;
      this.experienceValue = 10;
      this.tasks.addTask(4, new EntityBlaze.AIFireballAttack(this));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
      this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   static class AIFireballAttack extends EntityAIBase {
      private EntityBlaze blaze;
      private int field_179467_b;
      private int field_179468_c;

      public AIFireballAttack(EntityBlaze var1) {
         this.blaze = var1;
         this.setMutexBits(3);
      }

      public boolean shouldExecute() {
         EntityLivingBase var1 = this.blaze.getAttackTarget();
         return var1 != null && var1.isEntityAlive();
      }

      public void startExecuting() {
         this.field_179467_b = 0;
      }

      public void resetTask() {
         this.blaze.setOnFire(false);
      }

      public void updateTask() {
         --this.field_179468_c;
         EntityLivingBase var1 = this.blaze.getAttackTarget();
         double var2 = this.blaze.getDistanceSqToEntity(var1);
         if (var2 < 4.0D) {
            if (this.field_179468_c <= 0) {
               this.field_179468_c = 20;
               this.blaze.attackEntityAsMob(var1);
            }

            this.blaze.getMoveHelper().setMoveTo(var1.posX, var1.posY, var1.posZ, 1.0D);
         } else if (var2 < 256.0D) {
            double var4 = var1.posX - this.blaze.posX;
            double var6 = var1.getEntityBoundingBox().minY + (double)(var1.height / 2.0F) - (this.blaze.posY + (double)(this.blaze.height / 2.0F));
            double var8 = var1.posZ - this.blaze.posZ;
            if (this.field_179468_c <= 0) {
               ++this.field_179467_b;
               if (this.field_179467_b == 1) {
                  this.field_179468_c = 60;
                  this.blaze.setOnFire(true);
               } else if (this.field_179467_b <= 4) {
                  this.field_179468_c = 6;
               } else {
                  this.field_179468_c = 100;
                  this.field_179467_b = 0;
                  this.blaze.setOnFire(false);
               }

               if (this.field_179467_b > 1) {
                  float var10 = MathHelper.sqrt_float(MathHelper.sqrt_double(var2)) * 0.5F;
                  this.blaze.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);

                  for(int var11 = 0; var11 < 1; ++var11) {
                     EntitySmallFireball var12 = new EntitySmallFireball(this.blaze.worldObj, this.blaze, var4 + this.blaze.getRNG().nextGaussian() * (double)var10, var6, var8 + this.blaze.getRNG().nextGaussian() * (double)var10);
                     var12.posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
                     this.blaze.worldObj.spawnEntityInWorld(var12);
                  }
               }
            }

            this.blaze.getLookHelper().setLookPositionWithEntity(var1, 10.0F, 10.0F);
         } else {
            this.blaze.getNavigator().clearPathEntity();
            this.blaze.getMoveHelper().setMoveTo(var1.posX, var1.posY, var1.posZ, 1.0D);
         }

         super.updateTask();
      }
   }
}
