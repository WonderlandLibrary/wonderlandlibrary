package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpider extends EntityMob {
   protected String getDeathSound() {
      return "mob.spider.death";
   }

   public double getMountedYOffset() {
      return (double)(this.height * 0.5F);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
   }

   public void setBesideClimbableBlock(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 &= -2;
      }

      this.dataWatcher.updateObject(16, var2);
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote) {
         this.setBesideClimbableBlock(this.isCollidedHorizontally);
      }

   }

   protected Item getDropItem() {
      return Items.string;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound("mob.spider.step", 0.15F, 1.0F);
   }

   protected String getLivingSound() {
      return "mob.spider.say";
   }

   protected PathNavigate getNewNavigator(World var1) {
      return new PathNavigateClimber(this, var1);
   }

   protected String getHurtSound() {
      return "mob.spider.say";
   }

   public boolean isOnLadder() {
      return this.isBesideClimbableBlock();
   }

   public boolean isPotionApplicable(PotionEffect var1) {
      return var1.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(var1);
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   public EntitySpider(World var1) {
      super(var1);
      this.setSize(1.4F, 0.9F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(4, new EntitySpider.AISpiderAttack(this, EntityPlayer.class));
      this.tasks.addTask(4, new EntitySpider.AISpiderAttack(this, EntityIronGolem.class));
      this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new EntitySpider.AISpiderTarget(this, EntityPlayer.class));
      this.targetTasks.addTask(3, new EntitySpider.AISpiderTarget(this, EntityIronGolem.class));
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      Object var5 = super.onInitialSpawn(var1, var2);
      if (this.worldObj.rand.nextInt(100) == 0) {
         EntitySkeleton var3 = new EntitySkeleton(this.worldObj);
         var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
         var3.onInitialSpawn(var1, (IEntityLivingData)null);
         this.worldObj.spawnEntityInWorld(var3);
         var3.mountEntity(this);
      }

      if (var5 == null) {
         var5 = new EntitySpider.GroupData();
         if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * var1.getClampedAdditionalDifficulty()) {
            ((EntitySpider.GroupData)var5).func_111104_a(this.worldObj.rand);
         }
      }

      if (var5 instanceof EntitySpider.GroupData) {
         int var6 = ((EntitySpider.GroupData)var5).potionEffectId;
         if (var6 > 0 && Potion.potionTypes[var6] != null) {
            this.addPotionEffect(new PotionEffect(var6, Integer.MAX_VALUE));
         }
      }

      return (IEntityLivingData)var5;
   }

   public boolean isBesideClimbableBlock() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setInWeb() {
   }

   protected void dropFewItems(boolean var1, int var2) {
      super.dropFewItems(var1, var2);
      if (var1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + var2) > 0)) {
         this.dropItem(Items.spider_eye, 1);
      }

   }

   public float getEyeHeight() {
      return 0.65F;
   }

   static class AISpiderTarget extends EntityAINearestAttackableTarget {
      public AISpiderTarget(EntitySpider var1, Class var2) {
         super(var1, var2, true);
      }

      public boolean shouldExecute() {
         float var1 = this.taskOwner.getBrightness(1.0F);
         return var1 >= 0.5F ? false : super.shouldExecute();
      }
   }

   static class AISpiderAttack extends EntityAIAttackOnCollide {
      public boolean continueExecuting() {
         float var1 = this.attacker.getBrightness(1.0F);
         if (var1 >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
            this.attacker.setAttackTarget((EntityLivingBase)null);
            return false;
         } else {
            return super.continueExecuting();
         }
      }

      protected double func_179512_a(EntityLivingBase var1) {
         return (double)(4.0F + var1.width);
      }

      public AISpiderAttack(EntitySpider var1, Class var2) {
         super(var1, var2, 1.0D, true);
      }
   }

   public static class GroupData implements IEntityLivingData {
      public int potionEffectId;

      public void func_111104_a(Random var1) {
         int var2 = var1.nextInt(5);
         if (var2 <= 1) {
            this.potionEffectId = Potion.moveSpeed.id;
         } else if (var2 <= 2) {
            this.potionEffectId = Potion.damageBoost.id;
         } else if (var2 <= 3) {
            this.potionEffectId = Potion.regeneration.id;
         } else if (var2 <= 4) {
            this.potionEffectId = Potion.invisibility.id;
         }

      }
   }
}
