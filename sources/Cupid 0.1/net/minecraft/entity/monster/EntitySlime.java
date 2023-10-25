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
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer(this));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAIFindEntityNearest(this, EntityIronGolem.class));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
  }
  
  protected void setSlimeSize(int size) {
    this.dataWatcher.updateObject(16, Byte.valueOf((byte)size));
    setSize(0.51000005F * size, 0.51000005F * size);
    setPosition(this.posX, this.posY, this.posZ);
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((size * size));
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((0.2F + 0.1F * size));
    setHealth(getMaxHealth());
    this.experienceValue = size;
  }
  
  public int getSlimeSize() {
    return this.dataWatcher.getWatchableObjectByte(16);
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("Size", getSlimeSize() - 1);
    tagCompound.setBoolean("wasOnGround", this.wasOnGround);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    int i = tagCompund.getInteger("Size");
    if (i < 0)
      i = 0; 
    setSlimeSize(i + 1);
    this.wasOnGround = tagCompund.getBoolean("wasOnGround");
  }
  
  protected EnumParticleTypes getParticleType() {
    return EnumParticleTypes.SLIME;
  }
  
  protected String getJumpSound() {
    return "mob.slime." + ((getSlimeSize() > 1) ? "big" : "small");
  }
  
  public void onUpdate() {
    if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && getSlimeSize() > 0)
      this.isDead = true; 
    this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
    this.prevSquishFactor = this.squishFactor;
    super.onUpdate();
    if (this.onGround && !this.wasOnGround) {
      int i = getSlimeSize();
      for (int j = 0; j < i * 8; j++) {
        float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
        float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
        float f2 = MathHelper.sin(f) * i * 0.5F * f1;
        float f3 = MathHelper.cos(f) * i * 0.5F * f1;
        World world = this.worldObj;
        EnumParticleTypes enumparticletypes = getParticleType();
        double d0 = this.posX + f2;
        double d1 = this.posZ + f3;
        world.spawnParticle(enumparticletypes, d0, (getEntityBoundingBox()).minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
      } 
      if (makesSoundOnLand())
        playSound(getJumpSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F); 
      this.squishAmount = -0.5F;
    } else if (!this.onGround && this.wasOnGround) {
      this.squishAmount = 1.0F;
    } 
    this.wasOnGround = this.onGround;
    alterSquishAmount();
  }
  
  protected void alterSquishAmount() {
    this.squishAmount *= 0.6F;
  }
  
  protected int getJumpDelay() {
    return this.rand.nextInt(20) + 10;
  }
  
  protected EntitySlime createInstance() {
    return new EntitySlime(this.worldObj);
  }
  
  public void onDataWatcherUpdate(int dataID) {
    if (dataID == 16) {
      int i = getSlimeSize();
      setSize(0.51000005F * i, 0.51000005F * i);
      this.rotationYaw = this.rotationYawHead;
      this.renderYawOffset = this.rotationYawHead;
      if (isInWater() && this.rand.nextInt(20) == 0)
        resetHeight(); 
    } 
    super.onDataWatcherUpdate(dataID);
  }
  
  public void setDead() {
    int i = getSlimeSize();
    if (!this.worldObj.isRemote && i > 1 && getHealth() <= 0.0F) {
      int j = 2 + this.rand.nextInt(3);
      for (int k = 0; k < j; k++) {
        float f = ((k % 2) - 0.5F) * i / 4.0F;
        float f1 = ((k / 2) - 0.5F) * i / 4.0F;
        EntitySlime entityslime = createInstance();
        if (hasCustomName())
          entityslime.setCustomNameTag(getCustomNameTag()); 
        if (isNoDespawnRequired())
          entityslime.enablePersistence(); 
        entityslime.setSlimeSize(i / 2);
        entityslime.setLocationAndAngles(this.posX + f, this.posY + 0.5D, this.posZ + f1, this.rand.nextFloat() * 360.0F, 0.0F);
        this.worldObj.spawnEntityInWorld((Entity)entityslime);
      } 
    } 
    super.setDead();
  }
  
  public void applyEntityCollision(Entity entityIn) {
    super.applyEntityCollision(entityIn);
    if (entityIn instanceof EntityIronGolem && canDamagePlayer())
      func_175451_e((EntityLivingBase)entityIn); 
  }
  
  public void onCollideWithPlayer(EntityPlayer entityIn) {
    if (canDamagePlayer())
      func_175451_e((EntityLivingBase)entityIn); 
  }
  
  protected void func_175451_e(EntityLivingBase p_175451_1_) {
    int i = getSlimeSize();
    if (canEntityBeSeen((Entity)p_175451_1_) && getDistanceSqToEntity((Entity)p_175451_1_) < 0.6D * i * 0.6D * i && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), getAttackStrength())) {
      playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      applyEnchantments((EntityLivingBase)this, (Entity)p_175451_1_);
    } 
  }
  
  public float getEyeHeight() {
    return 0.625F * this.height;
  }
  
  protected boolean canDamagePlayer() {
    return (getSlimeSize() > 1);
  }
  
  protected int getAttackStrength() {
    return getSlimeSize();
  }
  
  protected String getHurtSound() {
    return "mob.slime." + ((getSlimeSize() > 1) ? "big" : "small");
  }
  
  protected String getDeathSound() {
    return "mob.slime." + ((getSlimeSize() > 1) ? "big" : "small");
  }
  
  protected Item getDropItem() {
    return (getSlimeSize() == 1) ? Items.slime_ball : null;
  }
  
  public boolean getCanSpawnHere() {
    BlockPos blockpos = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
    Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
    if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1)
      return false; 
    if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
      BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
      if (biomegenbase == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos((Entity)this)) <= this.rand.nextInt(8))
        return super.getCanSpawnHere(); 
      if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D)
        return super.getCanSpawnHere(); 
    } 
    return false;
  }
  
  protected float getSoundVolume() {
    return 0.4F * getSlimeSize();
  }
  
  public int getVerticalFaceSpeed() {
    return 0;
  }
  
  protected boolean makesSoundOnJump() {
    return (getSlimeSize() > 0);
  }
  
  protected boolean makesSoundOnLand() {
    return (getSlimeSize() > 2);
  }
  
  protected void jump() {
    this.motionY = 0.41999998688697815D;
    this.isAirBorne = true;
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    int i = this.rand.nextInt(3);
    if (i < 2 && this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty())
      i++; 
    int j = 1 << i;
    setSlimeSize(j);
    return super.onInitialSpawn(difficulty, livingdata);
  }
  
  static class AISlimeAttack extends EntityAIBase {
    private EntitySlime slime;
    
    private int field_179465_b;
    
    public AISlimeAttack(EntitySlime p_i45824_1_) {
      this.slime = p_i45824_1_;
      setMutexBits(2);
    }
    
    public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
      return (entitylivingbase == null) ? false : (!entitylivingbase.isEntityAlive() ? false : ((!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage)));
    }
    
    public void startExecuting() {
      this.field_179465_b = 300;
      super.startExecuting();
    }
    
    public boolean continueExecuting() {
      EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
      return (entitylivingbase == null) ? false : (!entitylivingbase.isEntityAlive() ? false : ((entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage) ? false : ((--this.field_179465_b > 0))));
    }
    
    public void updateTask() {
      this.slime.faceEntity((Entity)this.slime.getAttackTarget(), 10.0F, 10.0F);
      ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
    }
  }
  
  static class AISlimeFaceRandom extends EntityAIBase {
    private EntitySlime slime;
    
    private float field_179459_b;
    
    private int field_179460_c;
    
    public AISlimeFaceRandom(EntitySlime p_i45820_1_) {
      this.slime = p_i45820_1_;
      setMutexBits(2);
    }
    
    public boolean shouldExecute() {
      return (this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava()));
    }
    
    public void updateTask() {
      if (--this.field_179460_c <= 0) {
        this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
        this.field_179459_b = this.slime.getRNG().nextInt(360);
      } 
      ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
    }
  }
  
  static class AISlimeFloat extends EntityAIBase {
    private EntitySlime slime;
    
    public AISlimeFloat(EntitySlime p_i45823_1_) {
      this.slime = p_i45823_1_;
      setMutexBits(5);
      ((PathNavigateGround)p_i45823_1_.getNavigator()).setCanSwim(true);
    }
    
    public boolean shouldExecute() {
      return (this.slime.isInWater() || this.slime.isInLava());
    }
    
    public void updateTask() {
      if (this.slime.getRNG().nextFloat() < 0.8F)
        this.slime.getJumpHelper().setJumping(); 
      ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
    }
  }
  
  static class AISlimeHop extends EntityAIBase {
    private EntitySlime slime;
    
    public AISlimeHop(EntitySlime p_i45822_1_) {
      this.slime = p_i45822_1_;
      setMutexBits(5);
    }
    
    public boolean shouldExecute() {
      return true;
    }
    
    public void updateTask() {
      ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
    }
  }
  
  static class SlimeMoveHelper extends EntityMoveHelper {
    private float field_179922_g;
    
    private int field_179924_h;
    
    private EntitySlime slime;
    
    private boolean field_179923_j;
    
    public SlimeMoveHelper(EntitySlime p_i45821_1_) {
      super(p_i45821_1_);
      this.slime = p_i45821_1_;
    }
    
    public void func_179920_a(float p_179920_1_, boolean p_179920_2_) {
      this.field_179922_g = p_179920_1_;
      this.field_179923_j = p_179920_2_;
    }
    
    public void setSpeed(double speedIn) {
      this.speed = speedIn;
      this.update = true;
    }
    
    public void onUpdateMoveHelper() {
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0F);
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
            if (this.field_179923_j)
              this.field_179924_h /= 3; 
            this.slime.getJumpHelper().setJumping();
            if (this.slime.makesSoundOnJump())
              this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F); 
          } else {
            this.slime.moveStrafing = this.slime.moveForward = 0.0F;
            this.entity.setAIMoveSpeed(0.0F);
          } 
        } else {
          this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntitySlime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */