package net.minecraft.entity.monster;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

   public boolean attackEntityAsMob(Entity var1) {
      if (super.attackEntityAsMob(var1)) {
         if (this.getSkeletonType() == 1 && var1 instanceof EntityLivingBase) {
            ((EntityLivingBase)var1).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
         }

         return true;
      } else {
         return false;
      }
   }

   public void updateRidden() {
      super.updateRidden();
      if (this.ridingEntity instanceof EntityCreature) {
         EntityCreature var1 = (EntityCreature)this.ridingEntity;
         this.renderYawOffset = var1.renderYawOffset;
      }

   }

   public float getEyeHeight() {
      return this.getSkeletonType() == 1 ? super.getEyeHeight() : 1.74F;
   }

   protected Item getDropItem() {
      return Items.arrow;
   }

   protected String getDeathSound() {
      return "mob.skeleton.death";
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(13, new Byte((byte)0));
   }

   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      super.setEquipmentBasedOnDifficulty(var1);
      this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
   }

   public EntitySkeleton(World var1) {
      super(var1);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIRestrictSun(this));
      this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
      this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
      this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
      if (var1 != null && !var1.isRemote) {
         this.setCombatTask();
      }

   }

   protected String getLivingSound() {
      return "mob.skeleton.say";
   }

   public void setSkeletonType(int var1) {
      this.dataWatcher.updateObject(13, (byte)var1);
      this.isImmuneToFire = var1 == 1;
      if (var1 == 1) {
         this.setSize(0.72F, 2.535F);
      } else {
         this.setSize(0.6F, 1.95F);
      }

   }

   public void setCurrentItemOrArmor(int var1, ItemStack var2) {
      super.setCurrentItemOrArmor(var1, var2);
      if (!this.worldObj.isRemote && var1 == 0) {
         this.setCombatTask();
      }

   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      var2 = super.onInitialSpawn(var1, var2);
      if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0) {
         this.tasks.addTask(4, this.aiAttackOnCollide);
         this.setSkeletonType(1);
         this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
         this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
      } else {
         this.tasks.addTask(4, this.aiArrowAttack);
         this.setEquipmentBasedOnDifficulty(var1);
         this.setEnchantmentBasedOnDifficulty(var1);
      }

      this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * var1.getClampedAdditionalDifficulty());
      if (this.getEquipmentInSlot(4) == null) {
         Calendar var3 = this.worldObj.getCurrentDate();
         if (var3.get(2) + 1 == 10 && var3.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
            this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
            this.equipmentDropChances[4] = 0.0F;
         }
      }

      return var2;
   }

   public void onLivingUpdate() {
      if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
         float var1 = this.getBrightness(1.0F);
         BlockPos var2 = new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
         if (var1 > 0.5F && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F && this.worldObj.canSeeSky(var2)) {
            boolean var3 = true;
            ItemStack var4 = this.getEquipmentInSlot(4);
            if (var4 != null) {
               if (var4.isItemStackDamageable()) {
                  var4.setItemDamage(var4.getItemDamage() + this.rand.nextInt(2));
                  if (var4.getItemDamage() >= var4.getMaxDamage()) {
                     this.renderBrokenItemStack(var4);
                     this.setCurrentItemOrArmor(4, (ItemStack)null);
                  }
               }

               var3 = false;
            }

            if (var3) {
               this.setFire(8);
            }
         }
      }

      if (this.worldObj.isRemote && this.getSkeletonType() == 1) {
         this.setSize(0.72F, 2.535F);
      }

      super.onLivingUpdate();
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.skeleton.step", 0.15F, 1.0F);
   }

   protected void addRandomDrop() {
      if (this.getSkeletonType() == 1) {
         this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
      }

   }

   public void onDeath(DamageSource var1) {
      super.onDeath(var1);
      if (var1.getSourceOfDamage() instanceof EntityArrow && var1.getEntity() instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)var1.getEntity();
         double var3 = var2.posX - this.posX;
         double var5 = var2.posZ - this.posZ;
         if (var3 * var3 + var5 * var5 >= 2500.0D) {
            var2.triggerAchievement(AchievementList.snipeSkeleton);
         }
      } else if (var1.getEntity() instanceof EntityCreeper && ((EntityCreeper)var1.getEntity()).getPowered() && ((EntityCreeper)var1.getEntity()).isAIEnabled()) {
         ((EntityCreeper)var1.getEntity()).func_175493_co();
         this.entityDropItem(new ItemStack(Items.skull, 1, this.getSkeletonType() == 1 ? 1 : 0), 0.0F);
      }

   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setByte("SkeletonType", (byte)this.getSkeletonType());
   }

   public double getYOffset() {
      return this.isChild() ? 0.0D : -0.35D;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      if (var1.hasKey("SkeletonType", 99)) {
         byte var2 = var1.getByte("SkeletonType");
         this.setSkeletonType(var2);
      }

      this.setCombatTask();
   }

   public int getSkeletonType() {
      return this.dataWatcher.getWatchableObjectByte(13);
   }

   public void setCombatTask() {
      this.tasks.removeTask(this.aiAttackOnCollide);
      this.tasks.removeTask(this.aiArrowAttack);
      ItemStack var1 = this.getHeldItem();
      if (var1 != null && var1.getItem() == Items.bow) {
         this.tasks.addTask(4, this.aiArrowAttack);
      } else {
         this.tasks.addTask(4, this.aiAttackOnCollide);
      }

   }

   protected String getHurtSound() {
      return "mob.skeleton.hurt";
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      EntityArrow var3 = new EntityArrow(this.worldObj, this, var1, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
      int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
      int var5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
      var3.setDamage((double)(var2 * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11F));
      if (var4 > 0) {
         var3.setDamage(var3.getDamage() + (double)var4 * 0.5D + 0.5D);
      }

      if (var5 > 0) {
         var3.setKnockbackStrength(var5);
      }

      if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1) {
         var3.setFire(100);
      }

      this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
      this.worldObj.spawnEntityInWorld(var3);
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3;
      int var4;
      if (this.getSkeletonType() == 1) {
         var3 = this.rand.nextInt(3 + var2) - 1;

         for(var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.coal, 1);
         }
      } else {
         var3 = this.rand.nextInt(3 + var2);

         for(var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Items.arrow, 1);
         }
      }

      var3 = this.rand.nextInt(3 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.bone, 1);
      }

   }
}
