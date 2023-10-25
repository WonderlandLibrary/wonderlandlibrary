package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
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
  private int lifetime = 0;
  
  private boolean playerSpawned = false;
  
  public EntityEndermite(World worldIn) {
    super(worldIn);
    this.experienceValue = 3;
    setSize(0.4F, 0.3F);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIWander(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  public float getEyeHeight() {
    return 0.1F;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected String getLivingSound() {
    return "mob.silverfish.say";
  }
  
  protected String getHurtSound() {
    return "mob.silverfish.hit";
  }
  
  protected String getDeathSound() {
    return "mob.silverfish.kill";
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound("mob.silverfish.step", 0.15F, 1.0F);
  }
  
  protected Item getDropItem() {
    return null;
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    this.lifetime = tagCompund.getInteger("Lifetime");
    this.playerSpawned = tagCompund.getBoolean("PlayerSpawned");
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("Lifetime", this.lifetime);
    tagCompound.setBoolean("PlayerSpawned", this.playerSpawned);
  }
  
  public void onUpdate() {
    this.renderYawOffset = this.rotationYaw;
    super.onUpdate();
  }
  
  public boolean isSpawnedByPlayer() {
    return this.playerSpawned;
  }
  
  public void setSpawnedByPlayer(boolean spawnedByPlayer) {
    this.playerSpawned = spawnedByPlayer;
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.worldObj.isRemote) {
      for (int i = 0; i < 2; i++)
        this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]); 
    } else {
      if (!isNoDespawnRequired())
        this.lifetime++; 
      if (this.lifetime >= 2400)
        setDead(); 
    } 
  }
  
  protected boolean isValidLightLevel() {
    return true;
  }
  
  public boolean getCanSpawnHere() {
    if (super.getCanSpawnHere()) {
      EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity((Entity)this, 5.0D);
      return (entityplayer == null);
    } 
    return false;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.ARTHROPOD;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */