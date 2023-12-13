package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
   private final EntityAIControlledByPlayer aiControlledByPlayer;

   public void setSaddled(boolean var1) {
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)1);
      } else {
         this.dataWatcher.updateObject(16, (byte)0);
      }

   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.setSaddled(var1.getBoolean("Saddle"));
   }

   public void onStruckByLightning(EntityLightningBolt var1) {
      if (!this.worldObj.isRemote && !this.isDead) {
         EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
         var2.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
         var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         var2.setNoAI(this.isAIDisabled());
         if (this.hasCustomName()) {
            var2.setCustomNameTag(this.getCustomNameTag());
            var2.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
         }

         this.worldObj.spawnEntityInWorld(var2);
         this.setDead();
      }

   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setBoolean("Saddle", this.getSaddled());
   }

   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         if (this.isBurning()) {
            this.dropItem(Items.cooked_porkchop, 1);
         } else {
            this.dropItem(Items.porkchop, 1);
         }
      }

      if (this != false) {
         this.dropItem(Items.saddle, 1);
      }

   }

   public EntityPig(World var1) {
      super(var1);
      this.setSize(0.9F, 0.9F);
      ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
      this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
      this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
      this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot_on_a_stick, false));
      this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot, false));
      this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
      this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
   }

   public boolean isBreedingItem(ItemStack var1) {
      return var1 != null && var1.getItem() == Items.carrot;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.pig.step", 0.15F, 1.0F);
   }

   protected Item getDropItem() {
      return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
   }

   public boolean canBeSteered() {
      ItemStack var1 = ((EntityPlayer)this.riddenByEntity).getHeldItem();
      return var1 != null && var1.getItem() == Items.carrot_on_a_stick;
   }

   public boolean interact(EntityPlayer var1) {
      if (super.interact(var1)) {
         return true;
      } else if (this != false && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == var1)) {
         var1.mountEntity(this);
         return true;
      } else {
         return false;
      }
   }

   protected String getDeathSound() {
      return "mob.pig.death";
   }

   public EntityPig createChild(EntityAgeable var1) {
      return new EntityPig(this.worldObj);
   }

   protected String getHurtSound() {
      return "mob.pig.say";
   }

   protected String getLivingSound() {
      return "mob.pig.say";
   }

   public void fall(float var1, float var2) {
      super.fall(var1, var2);
      if (var1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
         ((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
      }

   }

   public EntityAIControlledByPlayer getAIControlledByPlayer() {
      return this.aiControlledByPlayer;
   }
}
