package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.EntitySlime.AISlimeAttack;
import net.minecraft.entity.monster.EntitySlime.AISlimeFaceRandom;
import net.minecraft.entity.monster.EntitySlime.AISlimeFloat;
import net.minecraft.entity.monster.EntitySlime.AISlimeHop;
import net.minecraft.entity.monster.EntitySlime.SlimeMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
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
   public float squishFactor;
   public float prevSquishFactor;
   private boolean wasOnGround;

   public EntitySlime(World worldIn) {
      super(worldIn);
      this.moveHelper = new SlimeMoveHelper(this);
      this.tasks.addTask(1, new AISlimeFloat(this));
      this.tasks.addTask(2, new AISlimeAttack(this));
      this.tasks.addTask(3, new AISlimeFaceRandom(this));
      this.tasks.addTask(5, new AISlimeHop(this));
      this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
      this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
   }

   protected EntitySlime createInstance() {
      return new EntitySlime(this.worldObj);
   }

   public void onCollideWithPlayer(EntityPlayer entityIn) {
      if(this.canDamagePlayer()) {
         this.func_175451_e(entityIn);
      }

   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      super.readEntityFromNBT(tagCompund);
      int i = tagCompund.getInteger("Size");
      if(i < 0) {
         i = 0;
      }

      this.setSlimeSize(i + 1);
      this.wasOnGround = tagCompund.getBoolean("wasOnGround");
   }

   protected float getSoundVolume() {
      return 0.4F * (float)this.getSlimeSize();
   }

   public void onDataWatcherUpdate(int dataID) {
      if(dataID == 16) {
         int i = this.getSlimeSize();
         this.setSize(0.51000005F * (float)i, 0.51000005F * (float)i);
         this.rotationYaw = this.rotationYawHead;
         this.renderYawOffset = this.rotationYawHead;
         if(this.isInWater() && this.rand.nextInt(20) == 0) {
            this.resetHeight();
         }
      }

      super.onDataWatcherUpdate(dataID);
   }

   public void applyEntityCollision(Entity entityIn) {
      super.applyEntityCollision(entityIn);
      if(entityIn instanceof EntityIronGolem && this.canDamagePlayer()) {
         this.func_175451_e((EntityLivingBase)entityIn);
      }

   }

   public void setDead() {
      int i = this.getSlimeSize();
      if(!this.worldObj.isRemote && i > 1 && this.getHealth() <= 0.0F) {
         int j = 2 + this.rand.nextInt(3);

         for(int k = 0; k < j; ++k) {
            float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
            float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
            EntitySlime entityslime = this.createInstance();
            if(this.hasCustomName()) {
               entityslime.setCustomNameTag(this.getCustomNameTag());
            }

            if(this.isNoDespawnRequired()) {
               entityslime.enablePersistence();
            }

            entityslime.setSlimeSize(i / 2);
            entityslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5D, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
            this.worldObj.spawnEntityInWorld(entityslime);
         }
      }

      super.setDead();
   }

   protected void jump() {
      this.motionY = 0.41999998688697815D;
      this.isAirBorne = true;
   }

   public float getEyeHeight() {
      return 0.625F * this.height;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
   }

   protected String getDeathSound() {
      return "mob.slime." + (this.getSlimeSize() > 1?"big":"small");
   }

   protected String getHurtSound() {
      return "mob.slime." + (this.getSlimeSize() > 1?"big":"small");
   }

   public void onUpdate() {
      if(!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
         this.isDead = true;
      }

      this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
      this.prevSquishFactor = this.squishFactor;
      super.onUpdate();
      if(this.onGround && !this.wasOnGround) {
         int i = this.getSlimeSize();

         for(int j = 0; j < i * 8; ++j) {
            float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
            float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
            float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
            float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
            World world = this.worldObj;
            EnumParticleTypes enumparticletypes = this.getParticleType();
            double d0 = this.posX + (double)f2;
            double d1 = this.posZ + (double)f3;
            world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
         }

         if(this.makesSoundOnLand()) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         }

         this.squishAmount = -0.5F;
      } else if(!this.onGround && this.wasOnGround) {
         this.squishAmount = 1.0F;
      }

      this.wasOnGround = this.onGround;
      this.alterSquishAmount();
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      super.writeEntityToNBT(tagCompound);
      tagCompound.setInteger("Size", this.getSlimeSize() - 1);
      tagCompound.setBoolean("wasOnGround", this.wasOnGround);
   }

   public boolean getCanSpawnHere() {
      BlockPos blockpos = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
      Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
      if(this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
         return false;
      } else {
         if(this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
            if(biomegenbase == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
               return super.getCanSpawnHere();
            }

            if(this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D) {
               return super.getCanSpawnHere();
            }
         }

         return false;
      }
   }

   protected Item getDropItem() {
      return this.getSlimeSize() == 1?Items.slime_ball:null;
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
      int i = this.rand.nextInt(3);
      if(i < 2 && this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty()) {
         ++i;
      }

      int j = 1 << i;
      this.setSlimeSize(j);
      return super.onInitialSpawn(difficulty, livingdata);
   }

   public int getVerticalFaceSpeed() {
      return 0;
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   protected int getAttackStrength() {
      return this.getSlimeSize();
   }

   protected String getJumpSound() {
      return "mob.slime." + (this.getSlimeSize() > 1?"big":"small");
   }

   protected int getJumpDelay() {
      return this.rand.nextInt(20) + 10;
   }

   protected void func_175451_e(EntityLivingBase p_175451_1_) {
      int i = this.getSlimeSize();
      if(this.canEntityBeSeen(p_175451_1_) && this.getDistanceSqToEntity(p_175451_1_) < 0.6D * (double)i * 0.6D * (double)i && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength())) {
         this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.applyEnchantments(this, p_175451_1_);
      }

   }

   protected void setSlimeSize(int size) {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)size));
      this.setSize(0.51000005F * (float)size, 0.51000005F * (float)size);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(size * size));
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)(0.2F + 0.1F * (float)size));
      this.setHealth(this.getMaxHealth());
      this.experienceValue = size;
   }

   protected boolean canDamagePlayer() {
      return this.getSlimeSize() > 1;
   }

   public int getSlimeSize() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   protected boolean makesSoundOnJump() {
      return this.getSlimeSize() > 0;
   }

   protected boolean makesSoundOnLand() {
      return this.getSlimeSize() > 2;
   }

   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.SLIME;
   }
}
