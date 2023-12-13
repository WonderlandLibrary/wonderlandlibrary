package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntitySlime extends EntityLiving implements IMob {
   public float squishAmount;
   public float prevSquishFactor;
   public float squishFactor;
   private boolean wasOnGround;

   public EntitySlime(World var1) {
      super(var1);
      this.moveHelper = new EntitySlime.SlimeMoveHelper(this);
      this.tasks.addTask(1, new EntitySlime.AISlimeFloat(this));
      this.tasks.addTask(2, new EntitySlime.AISlimeAttack(this));
      this.tasks.addTask(3, new EntitySlime.AISlimeFaceRandom(this));
      this.tasks.addTask(5, new EntitySlime.AISlimeHop(this));
      this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
      this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
   }

   protected boolean makesSoundOnJump() {
      return this.getSlimeSize() > 0;
   }

   public void onCollideWithPlayer(EntityPlayer var1) {
      if (this != false) {
         this.func_175451_e(var1);
      }

   }

   public void onDataWatcherUpdate(int var1) {
      if (var1 == 16) {
         int var2 = this.getSlimeSize();
         this.setSize(0.51000005F * (float)var2, 0.51000005F * (float)var2);
         this.rotationYaw = this.rotationYawHead;
         this.renderYawOffset = this.rotationYawHead;
         if (this.isInWater() && this.rand.nextInt(20) == 0) {
            this.resetHeight();
         }
      }

      super.onDataWatcherUpdate(var1);
   }

   public float getEyeHeight() {
      return 0.625F * this.height;
   }

   protected String getDeathSound() {
      return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
   }

   public int getVerticalFaceSpeed() {
      return 0;
   }

   public void applyEntityCollision(Entity param1) {
      // $FF: Couldn't be decompiled
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      int var3 = this.rand.nextInt(3);
      if (var3 < 2 && this.rand.nextFloat() < 0.5F * var1.getClampedAdditionalDifficulty()) {
         ++var3;
      }

      int var4 = 1 << var3;
      this.setSlimeSize(var4);
      return super.onInitialSpawn(var1, var2);
   }

   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.SLIME;
   }

   public void setDead() {
      int var1 = this.getSlimeSize();
      if (!this.worldObj.isRemote && var1 > 1 && this.getHealth() <= 0.0F) {
         int var2 = 2 + this.rand.nextInt(3);

         for(int var3 = 0; var3 < var2; ++var3) {
            float var4 = ((float)(var3 % 2) - 0.5F) * (float)var1 / 4.0F;
            float var5 = ((float)(var3 / 2) - 0.5F) * (float)var1 / 4.0F;
            EntitySlime var6 = this.createInstance();
            if (this.hasCustomName()) {
               var6.setCustomNameTag(this.getCustomNameTag());
            }

            if (this.isNoDespawnRequired()) {
               var6.enablePersistence();
            }

            var6.setSlimeSize(var1 / 2);
            var6.setLocationAndAngles(this.posX + (double)var4, this.posY + 0.5D, this.posZ + (double)var5, this.rand.nextFloat() * 360.0F, 0.0F);
            this.worldObj.spawnEntityInWorld(var6);
         }
      }

      super.setDead();
   }

   public boolean getCanSpawnHere() {
      BlockPos var1 = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
      Chunk var2 = this.worldObj.getChunkFromBlockCoords(var1);
      if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
         return false;
      } else {
         if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            BiomeGenBase var3 = this.worldObj.getBiomeGenForCoords(var1);
            if (var3 == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
               return super.getCanSpawnHere();
            }

            if (this.rand.nextInt(10) == 0 && var2.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D) {
               return super.getCanSpawnHere();
            }
         }

         return false;
      }
   }

   protected String getJumpSound() {
      return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
   }

   protected EntitySlime createInstance() {
      return new EntitySlime(this.worldObj);
   }

   protected String getHurtSound() {
      return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      int var2 = var1.getInteger("Size");
      if (var2 < 0) {
         var2 = 0;
      }

      this.setSlimeSize(var2 + 1);
      this.wasOnGround = var1.getBoolean("wasOnGround");
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   public int getSlimeSize() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   protected Item getDropItem() {
      return this.getSlimeSize() == 1 ? Items.slime_ball : null;
   }

   protected void func_175451_e(EntityLivingBase var1) {
      int var2 = this.getSlimeSize();
      if (this.canEntityBeSeen(var1) && this.getDistanceSqToEntity(var1) < 0.6D * (double)var2 * 0.6D * (double)var2 && var1.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength())) {
         this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.applyEnchantments(this, var1);
      }

   }

   protected void jump() {
      this.motionY = 0.41999998688697815D;
      this.isAirBorne = true;
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("Size", this.getSlimeSize() - 1);
      var1.setBoolean("wasOnGround", this.wasOnGround);
   }

   protected int getJumpDelay() {
      return this.rand.nextInt(20) + 10;
   }

   protected int getAttackStrength() {
      return this.getSlimeSize();
   }

   protected float getSoundVolume() {
      return 0.4F * (float)this.getSlimeSize();
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)1);
   }

   public void onUpdate() {
      if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
         this.isDead = true;
      }

      this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
      this.prevSquishFactor = this.squishFactor;
      super.onUpdate();
      if (this.onGround && !this.wasOnGround) {
         int var1 = this.getSlimeSize();

         for(int var2 = 0; var2 < var1 * 8; ++var2) {
            float var3 = this.rand.nextFloat() * 3.1415927F * 2.0F;
            float var4 = this.rand.nextFloat() * 0.5F + 0.5F;
            float var5 = MathHelper.sin(var3) * (float)var1 * 0.5F * var4;
            float var6 = MathHelper.cos(var3) * (float)var1 * 0.5F * var4;
            World var7 = this.worldObj;
            EnumParticleTypes var8 = this.getParticleType();
            double var9 = this.posX + (double)var5;
            double var11 = this.posZ + (double)var6;
            var7.spawnParticle(var8, var9, this.getEntityBoundingBox().minY, var11, 0.0D, 0.0D, 0.0D);
         }

         if (this != false) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         }

         this.squishAmount = -0.5F;
      } else if (!this.onGround && this.wasOnGround) {
         this.squishAmount = 1.0F;
      }

      this.wasOnGround = this.onGround;
      this.alterSquishAmount();
   }

   protected void setSlimeSize(int var1) {
      this.dataWatcher.updateObject(16, (byte)var1);
      this.setSize(0.51000005F * (float)var1, 0.51000005F * (float)var1);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(var1 * var1));
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)(0.2F + 0.1F * (float)var1));
      this.setHealth(this.getMaxHealth());
      this.experienceValue = var1;
   }

   static class AISlimeFloat extends EntityAIBase {
      private EntitySlime slime;

      public AISlimeFloat(EntitySlime var1) {
         this.slime = var1;
         this.setMutexBits(5);
         ((PathNavigateGround)var1.getNavigator()).setCanSwim(true);
      }

      public boolean shouldExecute() {
         return this.slime.isInWater() || this.slime.isInLava();
      }

      public void updateTask() {
         if (this.slime.getRNG().nextFloat() < 0.8F) {
            this.slime.getJumpHelper().setJumping();
         }

         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
      }
   }

   static class AISlimeFaceRandom extends EntityAIBase {
      private float field_179459_b;
      private EntitySlime slime;
      private int field_179460_c;

      public boolean shouldExecute() {
         return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava());
      }

      public AISlimeFaceRandom(EntitySlime var1) {
         this.slime = var1;
         this.setMutexBits(2);
      }

      public void updateTask() {
         if (--this.field_179460_c <= 0) {
            this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
            this.field_179459_b = (float)this.slime.getRNG().nextInt(360);
         }

         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
      }
   }

   static class SlimeMoveHelper extends EntityMoveHelper {
      private EntitySlime slime;
      private int field_179924_h;
      private float field_179922_g;
      private boolean field_179923_j;

      public void onUpdateMoveHelper() {
         this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0F);
         this.entity.rotationYawHead = this.entity.rotationYaw;
         this.entity.renderYawOffset = this.entity.rotationYaw;
         if (!this.update) {
            this.entity.setMoveForward(0.0F);
         } else {
            this.update = false;
            if (this.entity.onGround) {
               this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
               if (this.field_179924_h-- <= 0) {
                  this.field_179924_h = this.slime.getJumpDelay();
                  if (this.field_179923_j) {
                     this.field_179924_h /= 3;
                  }

                  this.slime.getJumpHelper().setJumping();
                  if (this.slime.makesSoundOnJump()) {
                     this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                  }
               } else {
                  this.slime.moveStrafing = this.slime.moveForward = 0.0F;
                  this.entity.setAIMoveSpeed(0.0F);
               }
            } else {
               this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
            }
         }

      }

      public void setSpeed(double var1) {
         this.speed = var1;
         this.update = true;
      }

      public SlimeMoveHelper(EntitySlime var1) {
         super(var1);
         this.slime = var1;
      }

      public void func_179920_a(float var1, boolean var2) {
         this.field_179922_g = var1;
         this.field_179923_j = var2;
      }
   }

   static class AISlimeHop extends EntityAIBase {
      private EntitySlime slime;

      public boolean shouldExecute() {
         return true;
      }

      public void updateTask() {
         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
      }

      public AISlimeHop(EntitySlime var1) {
         this.slime = var1;
         this.setMutexBits(5);
      }
   }

   static class AISlimeAttack extends EntityAIBase {
      private int field_179465_b;
      private EntitySlime slime;

      public AISlimeAttack(EntitySlime var1) {
         this.slime = var1;
         this.setMutexBits(2);
      }

      public boolean shouldExecute() {
         EntityLivingBase var1 = this.slime.getAttackTarget();
         return var1 == null ? false : (!var1.isEntityAlive() ? false : !(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).capabilities.disableDamage);
      }

      public void startExecuting() {
         this.field_179465_b = 300;
         super.startExecuting();
      }

      public void updateTask() {
         this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
         ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
      }

      public boolean continueExecuting() {
         EntityLivingBase var1 = this.slime.getAttackTarget();
         return var1 == null ? false : (!var1.isEntityAlive() ? false : (var1 instanceof EntityPlayer && ((EntityPlayer)var1).capabilities.disableDamage ? false : --this.field_179465_b > 0));
      }
   }
}
