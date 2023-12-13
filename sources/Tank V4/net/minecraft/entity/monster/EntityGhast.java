package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast extends EntityFlying implements IMob {
   private int explosionStrength = 1;

   protected float getSoundVolume() {
      return 10.0F;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else if ("fireball".equals(var1.getDamageType()) && var1.getEntity() instanceof EntityPlayer) {
         super.attackEntityFrom(var1, 1000.0F);
         ((EntityPlayer)var1.getEntity()).triggerAchievement(AchievementList.ghast);
         return true;
      } else {
         return super.attackEntityFrom(var1, var2);
      }
   }

   public float getEyeHeight() {
      return 2.6F;
   }

   public EntityGhast(World var1) {
      super(var1);
      this.setSize(4.0F, 4.0F);
      this.isImmuneToFire = true;
      this.experienceValue = 5;
      this.moveHelper = new EntityGhast.GhastMoveHelper(this);
      this.tasks.addTask(5, new EntityGhast.AIRandomFly(this));
      this.tasks.addTask(7, new EntityGhast.AILookAround(this));
      this.tasks.addTask(7, new EntityGhast.AIFireballAttack(this));
      this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
   }

   protected Item getDropItem() {
      return Items.gunpowder;
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.ghast_tear, 1);
      }

      var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.gunpowder, 1);
      }

   }

   public int getFireballStrength() {
      return this.explosionStrength;
   }

   protected String getDeathSound() {
      return "mob.ghast.death";
   }

   public void setAttacking(boolean var1) {
      this.dataWatcher.updateObject(16, (byte)(var1 ? 1 : 0));
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      }

   }

   protected String getLivingSound() {
      return "mob.ghast.moan";
   }

   public boolean getCanSpawnHere() {
      return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      if (var1.hasKey("ExplosionPower", 99)) {
         this.explosionStrength = var1.getInteger("ExplosionPower");
      }

   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
   }

   protected String getHurtSound() {
      return "mob.ghast.scream";
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("ExplosionPower", this.explosionStrength);
   }

   public boolean isAttacking() {
      return this.dataWatcher.getWatchableObjectByte(16) != 0;
   }

   static class AIRandomFly extends EntityAIBase {
      private EntityGhast parentEntity;

      public void startExecuting() {
         Random var1 = this.parentEntity.getRNG();
         double var2 = this.parentEntity.posX + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         double var4 = this.parentEntity.posY + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         double var6 = this.parentEntity.posZ + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         this.parentEntity.getMoveHelper().setMoveTo(var2, var4, var6, 1.0D);
      }

      public AIRandomFly(EntityGhast var1) {
         this.parentEntity = var1;
         this.setMutexBits(1);
      }

      public boolean shouldExecute() {
         EntityMoveHelper var1 = this.parentEntity.getMoveHelper();
         if (!var1.isUpdating()) {
            return true;
         } else {
            double var2 = var1.getX() - this.parentEntity.posX;
            double var4 = var1.getY() - this.parentEntity.posY;
            double var6 = var1.getZ() - this.parentEntity.posZ;
            double var8 = var2 * var2 + var4 * var4 + var6 * var6;
            return var8 < 1.0D || var8 > 3600.0D;
         }
      }

      public boolean continueExecuting() {
         return false;
      }
   }

   static class AIFireballAttack extends EntityAIBase {
      public int attackTimer;
      private EntityGhast parentEntity;

      public AIFireballAttack(EntityGhast var1) {
         this.parentEntity = var1;
      }

      public boolean shouldExecute() {
         return this.parentEntity.getAttackTarget() != null;
      }

      public void resetTask() {
         this.parentEntity.setAttacking(false);
      }

      public void updateTask() {
         EntityLivingBase var1 = this.parentEntity.getAttackTarget();
         double var2 = 64.0D;
         if (var1.getDistanceSqToEntity(this.parentEntity) < var2 * var2 && this.parentEntity.canEntityBeSeen(var1)) {
            World var4 = this.parentEntity.worldObj;
            ++this.attackTimer;
            if (this.attackTimer == 10) {
               var4.playAuxSFXAtEntity((EntityPlayer)null, 1007, new BlockPos(this.parentEntity), 0);
            }

            if (this.attackTimer == 20) {
               double var5 = 4.0D;
               Vec3 var7 = this.parentEntity.getLook(1.0F);
               double var8 = var1.posX - (this.parentEntity.posX + var7.xCoord * var5);
               double var10 = var1.getEntityBoundingBox().minY + (double)(var1.height / 2.0F) - (0.5D + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F));
               double var12 = var1.posZ - (this.parentEntity.posZ + var7.zCoord * var5);
               var4.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos(this.parentEntity), 0);
               EntityLargeFireball var14 = new EntityLargeFireball(var4, this.parentEntity, var8, var10, var12);
               var14.explosionPower = this.parentEntity.getFireballStrength();
               var14.posX = this.parentEntity.posX + var7.xCoord * var5;
               var14.posY = this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F) + 0.5D;
               var14.posZ = this.parentEntity.posZ + var7.zCoord * var5;
               var4.spawnEntityInWorld(var14);
               this.attackTimer = -40;
            }
         } else if (this.attackTimer > 0) {
            --this.attackTimer;
         }

         this.parentEntity.setAttacking(this.attackTimer > 10);
      }

      public void startExecuting() {
         this.attackTimer = 0;
      }
   }

   static class AILookAround extends EntityAIBase {
      private EntityGhast parentEntity;

      public void updateTask() {
         if (this.parentEntity.getAttackTarget() == null) {
            this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.func_181159_b(this.parentEntity.motionX, this.parentEntity.motionZ)) * 180.0F / 3.1415927F;
         } else {
            EntityLivingBase var1 = this.parentEntity.getAttackTarget();
            double var2 = 64.0D;
            if (var1.getDistanceSqToEntity(this.parentEntity) < var2 * var2) {
               double var4 = var1.posX - this.parentEntity.posX;
               double var6 = var1.posZ - this.parentEntity.posZ;
               this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.func_181159_b(var4, var6)) * 180.0F / 3.1415927F;
            }
         }

      }

      public AILookAround(EntityGhast var1) {
         this.parentEntity = var1;
         this.setMutexBits(2);
      }

      public boolean shouldExecute() {
         return true;
      }
   }

   static class GhastMoveHelper extends EntityMoveHelper {
      private EntityGhast parentEntity;
      private int courseChangeCooldown;

      public GhastMoveHelper(EntityGhast var1) {
         super(var1);
         this.parentEntity = var1;
      }

      public void onUpdateMoveHelper() {
         if (this.update) {
            double var1 = this.posX - this.parentEntity.posX;
            double var3 = this.posY - this.parentEntity.posY;
            double var5 = this.posZ - this.parentEntity.posZ;
            double var7 = var1 * var1 + var3 * var3 + var5 * var5;
            if (this.courseChangeCooldown-- <= 0) {
               this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
               var7 = (double)MathHelper.sqrt_double(var7);
               double var10001 = this.posX;
               double var10002 = this.posY;
               double var10003 = this.posZ;
               if (var7 == false) {
                  EntityGhast var10004 = this.parentEntity;
                  var10004.motionX += var1 / var7 * 0.1D;
                  var10004 = this.parentEntity;
                  var10004.motionY += var3 / var7 * 0.1D;
                  var10004 = this.parentEntity;
                  var10004.motionZ += var5 / var7 * 0.1D;
               } else {
                  this.update = false;
               }
            }
         }

      }
   }
}
