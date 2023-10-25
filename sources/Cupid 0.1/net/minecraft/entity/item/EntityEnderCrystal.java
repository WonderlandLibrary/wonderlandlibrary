package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderCrystal extends Entity {
  public int innerRotation;
  
  public int health;
  
  public EntityEnderCrystal(World worldIn) {
    super(worldIn);
    this.preventEntitySpawning = true;
    setSize(2.0F, 2.0F);
    this.health = 5;
    this.innerRotation = this.rand.nextInt(100000);
  }
  
  public EntityEnderCrystal(World worldIn, double p_i1699_2_, double p_i1699_4_, double p_i1699_6_) {
    this(worldIn);
    setPosition(p_i1699_2_, p_i1699_4_, p_i1699_6_);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected void entityInit() {
    this.dataWatcher.addObject(8, Integer.valueOf(this.health));
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.innerRotation++;
    this.dataWatcher.updateObject(8, Integer.valueOf(this.health));
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    if (this.worldObj.provider instanceof net.minecraft.world.WorldProviderEnd && this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock() != Blocks.fire)
      this.worldObj.setBlockState(new BlockPos(i, j, k), Blocks.fire.getDefaultState()); 
  }
  
  protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
  
  protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
  
  public boolean canBeCollidedWith() {
    return true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (!this.isDead && !this.worldObj.isRemote) {
      this.health = 0;
      if (this.health <= 0) {
        setDead();
        if (!this.worldObj.isRemote)
          this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 6.0F, true); 
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\item\EntityEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */