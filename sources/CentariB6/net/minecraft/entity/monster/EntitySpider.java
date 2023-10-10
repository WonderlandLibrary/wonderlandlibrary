package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider.AISpiderAttack;
import net.minecraft.entity.monster.EntitySpider.AISpiderTarget;
import net.minecraft.entity.monster.EntitySpider.GroupData;
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
   public EntitySpider(World worldIn) {
      super(worldIn);
      this.setSize(1.4F, 0.9F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(4, new AISpiderAttack(this, EntityPlayer.class));
      this.tasks.addTask(4, new AISpiderAttack(this, EntityIronGolem.class));
      this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new AISpiderTarget(this, EntityPlayer.class));
      this.targetTasks.addTask(3, new AISpiderTarget(this, EntityIronGolem.class));
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
      super.dropFewItems(p_70628_1_, p_70628_2_);
      if(p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0)) {
         this.dropItem(Items.spider_eye, 1);
      }

   }

   protected void playStepSound(BlockPos pos, Block blockIn) {
      this.playSound("mob.spider.step", 0.15F, 1.0F);
   }

   public double getMountedYOffset() {
      return (double)(this.height * 0.5F);
   }

   public boolean isPotionApplicable(PotionEffect potioneffectIn) {
      return potioneffectIn.getPotionID() == Potion.poison.id?false:super.isPotionApplicable(potioneffectIn);
   }

   public float getEyeHeight() {
      return 0.65F;
   }

   public void setInWeb() {
   }

   public boolean isOnLadder() {
      return this.isBesideClimbableBlock();
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   protected String getDeathSound() {
      return "mob.spider.death";
   }

   protected String getHurtSound() {
      return "mob.spider.say";
   }

   public void onUpdate() {
      super.onUpdate();
      if(!this.worldObj.isRemote) {
         this.setBesideClimbableBlock(this.isCollidedHorizontally);
      }

   }

   protected String getLivingSound() {
      return "mob.spider.say";
   }

   protected Item getDropItem() {
      return Items.string;
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
      livingdata = super.onInitialSpawn(difficulty, livingdata);
      if(this.worldObj.rand.nextInt(100) == 0) {
         EntitySkeleton entityskeleton = new EntitySkeleton(this.worldObj);
         entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
         entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
         this.worldObj.spawnEntityInWorld(entityskeleton);
         entityskeleton.mountEntity(this);
      }

      if(livingdata == null) {
         livingdata = new GroupData();
         if(this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty()) {
            ((GroupData)livingdata).func_111104_a(this.worldObj.rand);
         }
      }

      if(livingdata instanceof GroupData) {
         int i = ((GroupData)livingdata).potionEffectId;
         if(i > 0 && Potion.potionTypes[i] != null) {
            this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
         }
      }

      return livingdata;
   }

   protected PathNavigate getNewNavigator(World worldIn) {
      return new PathNavigateClimber(this, worldIn);
   }

   public boolean isBesideClimbableBlock() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setBesideClimbableBlock(boolean p_70839_1_) {
      byte b0 = this.dataWatcher.getWatchableObjectByte(16);
      if(p_70839_1_) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 = (byte)(b0 & -2);
      }

      this.dataWatcher.updateObject(16, Byte.valueOf(b0));
   }
}
