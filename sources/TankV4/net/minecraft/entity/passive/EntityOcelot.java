package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityOcelot extends EntityTameable {
   private EntityAIAvoidEntity avoidEntity;
   private EntityAITempt aiTempt;

   public void updateAITasks() {
      if (this.getMoveHelper().isUpdating()) {
         double var1 = this.getMoveHelper().getSpeed();
         if (var1 == 0.6D) {
            this.setSneaking(true);
            this.setSprinting(false);
         } else if (var1 == 1.33D) {
            this.setSneaking(false);
            this.setSprinting(true);
         } else {
            this.setSneaking(false);
            this.setSprinting(false);
         }
      } else {
         this.setSneaking(false);
         this.setSprinting(false);
      }

   }

   public void setTameSkin(int var1) {
      this.dataWatcher.updateObject(18, (byte)var1);
   }

   public boolean attackEntityAsMob(Entity var1) {
      return var1.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
   }

   public String getName() {
      return this.hasCustomName() ? this.getCustomNameTag() : (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
   }

   public void fall(float var1, float var2) {
   }

   protected void setupTamedAI() {
      if (this.avoidEntity == null) {
         this.avoidEntity = new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D);
      }

      this.tasks.removeTask(this.avoidEntity);
      if (!this.isTamed()) {
         this.tasks.addTask(4, this.avoidEntity);
      }

   }

   public EntityOcelot createChild(EntityAgeable var1) {
      EntityOcelot var2 = new EntityOcelot(this.worldObj);
      if (this.isTamed()) {
         var2.setOwnerId(this.getOwnerId());
         var2.setTamed(true);
         var2.setTameSkin(this.getTameSkin());
      }

      return var2;
   }

   public boolean canMateWith(EntityAnimal var1) {
      if (var1 == this) {
         return false;
      } else if (!this.isTamed()) {
         return false;
      } else if (!(var1 instanceof EntityOcelot)) {
         return false;
      } else {
         EntityOcelot var2 = (EntityOcelot)var1;
         return !var2.isTamed() ? false : this.isInLove() && var2.isInLove();
      }
   }

   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   protected float getSoundVolume() {
      return 0.4F;
   }

   public EntityOcelot(World var1) {
      super(var1);
      this.setSize(0.6F, 0.7F);
      ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.6D, Items.fish, true));
      this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
      this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8D));
      this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
      this.tasks.addTask(8, new EntityAIOcelotAttack(this));
      this.tasks.addTask(9, new EntityAIMate(this, 0.8D));
      this.tasks.addTask(10, new EntityAIWander(this, 0.8D));
      this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
      this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("CatType", this.getTameSkin());
   }

   protected Item getDropItem() {
      return Items.leather;
   }

   public void setTamed(boolean var1) {
      super.setTamed(var1);
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      var2 = super.onInitialSpawn(var1, var2);
      if (this.worldObj.rand.nextInt(7) == 0) {
         for(int var3 = 0; var3 < 2; ++var3) {
            EntityOcelot var4 = new EntityOcelot(this.worldObj);
            var4.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            var4.setGrowingAge(-24000);
            this.worldObj.spawnEntityInWorld(var4);
         }
      }

      return var2;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.setTameSkin(var1.getInteger("CatType"));
   }

   public boolean getCanSpawnHere() {
      return this.worldObj.rand.nextInt(3) != 0;
   }

   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if (this.isTamed()) {
         if (this.isOwner(var1) && !this.worldObj.isRemote && var2 != null) {
            this.aiSit.setSitting(!this.isSitting());
         }
      } else if (this.aiTempt.isRunning() && var2 != null && var2.getItem() == Items.fish && var1.getDistanceSqToEntity(this) < 9.0D) {
         if (!var1.capabilities.isCreativeMode) {
            --var2.stackSize;
         }

         if (var2.stackSize <= 0) {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
         }

         if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(3) == 0) {
               this.setTamed(true);
               this.setTameSkin(1 + this.worldObj.rand.nextInt(3));
               this.setOwnerId(var1.getUniqueID().toString());
               this.playTameEffect(true);
               this.aiSit.setSitting(true);
               this.worldObj.setEntityState(this, (byte)7);
            } else {
               this.playTameEffect(false);
               this.worldObj.setEntityState(this, (byte)6);
            }
         }

         return true;
      }

      return super.interact(var1);
   }

   protected String getDeathSound() {
      return "mob.cat.hitt";
   }

   public boolean isNotColliding() {
      if (this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
         BlockPos var1 = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
         if (var1.getY() < this.worldObj.func_181545_F()) {
            return false;
         }

         Block var2 = this.worldObj.getBlockState(var1.down()).getBlock();
         if (var2 == Blocks.grass || var2.getMaterial() == Material.leaves) {
            return true;
         }
      }

      return false;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         this.aiSit.setSitting(false);
         return super.attackEntityFrom(var1, var2);
      }
   }

   protected String getHurtSound() {
      return "mob.cat.hitt";
   }

   public int getTameSkin() {
      return this.dataWatcher.getWatchableObjectByte(18);
   }

   protected boolean canDespawn() {
      return !this.isTamed() && this.ticksExisted > 2400;
   }

   protected void dropFewItems(boolean var1, int var2) {
   }

   protected String getLivingSound() {
      return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(18, (byte)0);
   }
}
