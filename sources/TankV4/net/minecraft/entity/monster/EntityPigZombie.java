package net.minecraft.entity.monster;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie extends EntityZombie {
   private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
   private int randomSoundDelay;
   private UUID angerTargetUUID;
   private int angerLevel;
   private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER;

   protected void addRandomDrop() {
      this.dropItem(Items.gold_ingot, 1);
   }

   public EntityPigZombie(World var1) {
      super(var1);
      this.isImmuneToFire = true;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(reinforcementChance).setBaseValue(0.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
   }

   protected void updateAITasks() {
      IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      if (this != false) {
         if (!this.isChild() && !var1.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
            var1.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
         }

         --this.angerLevel;
      } else if (var1.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
         var1.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
      }

      if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
         this.playSound("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
      }

      if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getAITarget() == null) {
         EntityPlayer var2 = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
         this.setRevengeTarget(var2);
         this.attackingPlayer = var2;
         this.recentlyHit = this.getRevengeTimer();
      }

      super.updateAITasks();
   }

   public boolean interact(EntityPlayer var1) {
      return false;
   }

   public void setRevengeTarget(EntityLivingBase var1) {
      super.setRevengeTarget(var1);
      if (var1 != null) {
         this.angerTargetUUID = var1.getUniqueID();
      }

   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(var1)) {
         return false;
      } else {
         Entity var3 = var1.getEntity();
         if (var3 instanceof EntityPlayer) {
            this.becomeAngryAt(var3);
         }

         return super.attackEntityFrom(var1, var2);
      }
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.angerLevel = var1.getShort("Anger");
      String var2 = var1.getString("HurtBy");
      if (var2.length() > 0) {
         this.angerTargetUUID = UUID.fromString(var2);
         EntityPlayer var3 = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
         this.setRevengeTarget(var3);
         if (var3 != null) {
            this.attackingPlayer = var3;
            this.recentlyHit = this.getRevengeTimer();
         }
      }

   }

   private void becomeAngryAt(Entity var1) {
      this.angerLevel = 400 + this.rand.nextInt(400);
      this.randomSoundDelay = this.rand.nextInt(40);
      if (var1 instanceof EntityLivingBase) {
         this.setRevengeTarget((EntityLivingBase)var1);
      }

   }

   protected String getHurtSound() {
      return "mob.zombiepig.zpighurt";
   }

   public boolean isNotColliding() {
      return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
   }

   protected String getDeathSound() {
      return "mob.zombiepig.zpigdeath";
   }

   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
   }

   protected String getLivingSound() {
      return "mob.zombiepig.zpig";
   }

   public boolean getCanSpawnHere() {
      return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   static {
      ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0)).setSaved(false);
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setShort("Anger", (short)this.angerLevel);
      if (this.angerTargetUUID != null) {
         var1.setString("HurtBy", this.angerTargetUUID.toString());
      } else {
         var1.setString("HurtBy", "");
      }

   }

   protected void applyEntityAI() {
      this.targetTasks.addTask(1, new EntityPigZombie.AIHurtByAggressor(this));
      this.targetTasks.addTask(2, new EntityPigZombie.AITargetAggressor(this));
   }

   public void onUpdate() {
      super.onUpdate();
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      super.onInitialSpawn(var1, var2);
      this.setVillager(false);
      return var2;
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(2 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.rotten_flesh, 1);
      }

      var3 = this.rand.nextInt(2 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.gold_nugget, 1);
      }

   }

   static void access$0(EntityPigZombie var0, Entity var1) {
      var0.becomeAngryAt(var1);
   }

   static class AIHurtByAggressor extends EntityAIHurtByTarget {
      public AIHurtByAggressor(EntityPigZombie var1) {
         super(var1, true);
      }

      protected void setEntityAttackTarget(EntityCreature var1, EntityLivingBase var2) {
         super.setEntityAttackTarget(var1, var2);
         if (var1 instanceof EntityPigZombie) {
            EntityPigZombie.access$0((EntityPigZombie)var1, var2);
         }

      }
   }

   static class AITargetAggressor extends EntityAINearestAttackableTarget {
      public boolean shouldExecute() {
         return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
      }

      public AITargetAggressor(EntityPigZombie var1) {
         super(var1, EntityPlayer.class, true);
      }
   }
}
