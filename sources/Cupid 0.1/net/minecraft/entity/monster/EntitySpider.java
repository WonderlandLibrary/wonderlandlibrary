package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
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
  public EntitySpider(World worldIn) {
    super(worldIn);
    setSize(1.4F, 0.9F);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(3, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.4F));
    this.tasks.addTask(4, (EntityAIBase)new AISpiderAttack(this, (Class)EntityPlayer.class));
    this.tasks.addTask(4, (EntityAIBase)new AISpiderAttack(this, (Class)EntityIronGolem.class));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWander(this, 0.8D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new AISpiderTarget<>(this, EntityPlayer.class));
    this.targetTasks.addTask(3, (EntityAIBase)new AISpiderTarget<>(this, EntityIronGolem.class));
  }
  
  public double getMountedYOffset() {
    return (this.height * 0.5F);
  }
  
  protected PathNavigate getNewNavigator(World worldIn) {
    return (PathNavigate)new PathNavigateClimber((EntityLiving)this, worldIn);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, new Byte((byte)0));
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.worldObj.isRemote)
      setBesideClimbableBlock(this.isCollidedHorizontally); 
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
  }
  
  protected String getLivingSound() {
    return "mob.spider.say";
  }
  
  protected String getHurtSound() {
    return "mob.spider.say";
  }
  
  protected String getDeathSound() {
    return "mob.spider.death";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.spider.step", 0.15F, 1.0F);
  }
  
  protected Item getDropItem() {
    return Items.string;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    super.dropFewItems(p_70628_1_, p_70628_2_);
    if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0))
      dropItem(Items.spider_eye, 1); 
  }
  
  public boolean isOnLadder() {
    return isBesideClimbableBlock();
  }
  
  public void setInWeb() {}
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.ARTHROPOD;
  }
  
  public boolean isPotionApplicable(PotionEffect potioneffectIn) {
    return (potioneffectIn.getPotionID() == Potion.poison.id) ? false : super.isPotionApplicable(potioneffectIn);
  }
  
  public boolean isBesideClimbableBlock() {
    return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
  }
  
  public void setBesideClimbableBlock(boolean p_70839_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    if (p_70839_1_) {
      b0 = (byte)(b0 | 0x1);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFE);
    } 
    this.dataWatcher.updateObject(16, Byte.valueOf(b0));
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    if (this.worldObj.rand.nextInt(100) == 0) {
      EntitySkeleton entityskeleton = new EntitySkeleton(this.worldObj);
      entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
      entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
      this.worldObj.spawnEntityInWorld((Entity)entityskeleton);
      entityskeleton.mountEntity((Entity)this);
    } 
    if (livingdata == null) {
      livingdata = new GroupData();
      if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
        ((GroupData)livingdata).func_111104_a(this.worldObj.rand); 
    } 
    if (livingdata instanceof GroupData) {
      int i = ((GroupData)livingdata).potionEffectId;
      if (i > 0 && Potion.potionTypes[i] != null)
        addPotionEffect(new PotionEffect(i, 2147483647)); 
    } 
    return livingdata;
  }
  
  public float getEyeHeight() {
    return 0.65F;
  }
  
  static class AISpiderAttack extends EntityAIAttackOnCollide {
    public AISpiderAttack(EntitySpider p_i45819_1_, Class<? extends Entity> targetClass) {
      super(p_i45819_1_, targetClass, 1.0D, true);
    }
    
    public boolean continueExecuting() {
      float f = this.attacker.getBrightness(1.0F);
      if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
        this.attacker.setAttackTarget((EntityLivingBase)null);
        return false;
      } 
      return super.continueExecuting();
    }
    
    protected double func_179512_a(EntityLivingBase attackTarget) {
      return (4.0F + attackTarget.width);
    }
  }
  
  static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget {
    public AISpiderTarget(EntitySpider p_i45818_1_, Class<T> classTarget) {
      super(p_i45818_1_, classTarget, true);
    }
    
    public boolean shouldExecute() {
      float f = this.taskOwner.getBrightness(1.0F);
      return (f >= 0.5F) ? false : super.shouldExecute();
    }
  }
  
  public static class GroupData implements IEntityLivingData {
    public int potionEffectId;
    
    public void func_111104_a(Random rand) {
      int i = rand.nextInt(5);
      if (i <= 1) {
        this.potionEffectId = Potion.moveSpeed.id;
      } else if (i <= 2) {
        this.potionEffectId = Potion.damageBoost.id;
      } else if (i <= 3) {
        this.potionEffectId = Potion.regeneration.id;
      } else if (i <= 4) {
        this.potionEffectId = Potion.invisibility.id;
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntitySpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */