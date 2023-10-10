package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityEndermite extends EntityMob {
   private boolean playerSpawned = false;
   private int lifetime = 0;

   protected String getLivingSound() {
      return "mob.silverfish.say";
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.silverfish.step", 0.15F, 1.0F);
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("Lifetime", this.lifetime);
      var1.setBoolean("PlayerSpawned", this.playerSpawned);
   }

   public EntityEndermite(World var1) {
      super(var1);
      this.experienceValue = 3;
      this.setSize(0.4F, 0.3F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
      this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.lifetime = var1.getInteger("Lifetime");
      this.playerSpawned = var1.getBoolean("PlayerSpawned");
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.worldObj.isRemote) {
         for(int var1 = 0; var1 < 2; ++var1) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
         }
      } else {
         if (!this.isNoDespawnRequired()) {
            ++this.lifetime;
         }

         if (this.lifetime >= 2400) {
            this.setDead();
         }
      }

   }

   public boolean getCanSpawnHere() {
      if (super.getCanSpawnHere()) {
         EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
         return var1 == null;
      } else {
         return false;
      }
   }

   public boolean isSpawnedByPlayer() {
      return this.playerSpawned;
   }

   protected boolean isValidLightLevel() {
      return true;
   }

   public float getEyeHeight() {
      return 0.1F;
   }

   public void onUpdate() {
      this.renderYawOffset = this.rotationYaw;
      super.onUpdate();
   }

   protected Item getDropItem() {
      return null;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
   }

   protected String getDeathSound() {
      return "mob.silverfish.kill";
   }

   protected String getHurtSound() {
      return "mob.silverfish.hit";
   }

   public void setSpawnedByPlayer(boolean var1) {
      this.playerSpawned = var1;
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }
}
