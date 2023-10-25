package net.minecraft.entity.passive;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {
  protected EntityAISit aiSit = new EntityAISit(this);
  
  public EntityTameable(World worldIn) {
    super(worldIn);
    setupTamedAI();
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(17, "");
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    if (getOwnerId() == null) {
      tagCompound.setString("OwnerUUID", "");
    } else {
      tagCompound.setString("OwnerUUID", getOwnerId());
    } 
    tagCompound.setBoolean("Sitting", isSitting());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    String s = "";
    if (tagCompund.hasKey("OwnerUUID", 8)) {
      s = tagCompund.getString("OwnerUUID");
    } else {
      String s1 = tagCompund.getString("Owner");
      s = PreYggdrasilConverter.getStringUUIDFromName(s1);
    } 
    if (s.length() > 0) {
      setOwnerId(s);
      setTamed(true);
    } 
    this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
    setSitting(tagCompund.getBoolean("Sitting"));
  }
  
  protected void playTameEffect(boolean play) {
    EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
    if (!play)
      enumparticletypes = EnumParticleTypes.SMOKE_NORMAL; 
    for (int i = 0; i < 7; i++) {
      double d0 = this.rand.nextGaussian() * 0.02D;
      double d1 = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      this.worldObj.spawnParticle(enumparticletypes, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 7) {
      playTameEffect(true);
    } else if (id == 6) {
      playTameEffect(false);
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public boolean isTamed() {
    return ((this.dataWatcher.getWatchableObjectByte(16) & 0x4) != 0);
  }
  
  public void setTamed(boolean tamed) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    if (tamed) {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x4)));
    } else {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFB)));
    } 
    setupTamedAI();
  }
  
  protected void setupTamedAI() {}
  
  public boolean isSitting() {
    return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
  }
  
  public void setSitting(boolean sitting) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    if (sitting) {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
    } else {
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    } 
  }
  
  public String getOwnerId() {
    return this.dataWatcher.getWatchableObjectString(17);
  }
  
  public void setOwnerId(String ownerUuid) {
    this.dataWatcher.updateObject(17, ownerUuid);
  }
  
  public EntityLivingBase getOwner() {
    try {
      UUID uuid = UUID.fromString(getOwnerId());
      return (uuid == null) ? null : (EntityLivingBase)this.worldObj.getPlayerEntityByUUID(uuid);
    } catch (IllegalArgumentException var2) {
      return null;
    } 
  }
  
  public boolean isOwner(EntityLivingBase entityIn) {
    return (entityIn == getOwner());
  }
  
  public EntityAISit getAISit() {
    return this.aiSit;
  }
  
  public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
    return true;
  }
  
  public Team getTeam() {
    if (isTamed()) {
      EntityLivingBase entitylivingbase = getOwner();
      if (entitylivingbase != null)
        return entitylivingbase.getTeam(); 
    } 
    return super.getTeam();
  }
  
  public boolean isOnSameTeam(EntityLivingBase otherEntity) {
    if (isTamed()) {
      EntityLivingBase entitylivingbase = getOwner();
      if (otherEntity == entitylivingbase)
        return true; 
      if (entitylivingbase != null)
        return entitylivingbase.isOnSameTeam(otherEntity); 
    } 
    return super.isOnSameTeam(otherEntity);
  }
  
  public void onDeath(DamageSource cause) {
    if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages") && hasCustomName() && getOwner() instanceof EntityPlayerMP)
      ((EntityPlayerMP)getOwner()).addChatMessage(getCombatTracker().getDeathMessage()); 
    super.onDeath(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityTameable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */