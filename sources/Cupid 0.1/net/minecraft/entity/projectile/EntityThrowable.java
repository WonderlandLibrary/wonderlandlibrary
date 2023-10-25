package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityThrowable extends Entity implements IProjectile {
  private int xTile = -1;
  
  private int yTile = -1;
  
  private int zTile = -1;
  
  private Block inTile;
  
  protected boolean inGround;
  
  public int throwableShake;
  
  private EntityLivingBase thrower;
  
  private String throwerName;
  
  private int ticksInGround;
  
  private int ticksInAir;
  
  public EntityThrowable(World worldIn) {
    super(worldIn);
    setSize(0.25F, 0.25F);
  }
  
  protected void entityInit() {}
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
    if (Double.isNaN(d0))
      d0 = 4.0D; 
    d0 *= 64.0D;
    return (distance < d0 * d0);
  }
  
  public EntityThrowable(World worldIn, EntityLivingBase throwerIn) {
    super(worldIn);
    this.thrower = throwerIn;
    setSize(0.25F, 0.25F);
    setLocationAndAngles(throwerIn.posX, throwerIn.posY + throwerIn.getEyeHeight(), throwerIn.posZ, throwerIn.rotationYaw, throwerIn.rotationPitch);
    this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
    this.posY -= 0.10000000149011612D;
    this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
    setPosition(this.posX, this.posY, this.posZ);
    float f = 0.4F;
    this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
    this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
    this.motionY = (-MathHelper.sin((this.rotationPitch + getInaccuracy()) / 180.0F * 3.1415927F) * f);
    setThrowableHeading(this.motionX, this.motionY, this.motionZ, getVelocity(), 1.0F);
  }
  
  public EntityThrowable(World worldIn, double x, double y, double z) {
    super(worldIn);
    this.ticksInGround = 0;
    setSize(0.25F, 0.25F);
    setPosition(x, y, z);
  }
  
  protected float getVelocity() {
    return 1.5F;
  }
  
  protected float getInaccuracy() {
    return 0.0F;
  }
  
  public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
    float f = MathHelper.sqrt_double(x * x + y * y + z * z);
    x /= f;
    y /= f;
    z /= f;
    x += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
    y += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
    z += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
    x *= velocity;
    y *= velocity;
    z *= velocity;
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
    float f1 = MathHelper.sqrt_double(x * x + z * z);
    this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / Math.PI);
    this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(y, f1) * 180.0D / Math.PI);
    this.ticksInGround = 0;
  }
  
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt_double(x * x + z * z);
      this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / Math.PI);
      this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(y, f) * 180.0D / Math.PI);
    } 
  }
  
  public void onUpdate() {
    this.lastTickPosX = this.posX;
    this.lastTickPosY = this.posY;
    this.lastTickPosZ = this.posZ;
    super.onUpdate();
    if (this.throwableShake > 0)
      this.throwableShake--; 
    if (this.inGround) {
      if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
        this.ticksInGround++;
        if (this.ticksInGround == 1200)
          setDead(); 
        return;
      } 
      this.inGround = false;
      this.motionX *= (this.rand.nextFloat() * 0.2F);
      this.motionY *= (this.rand.nextFloat() * 0.2F);
      this.motionZ *= (this.rand.nextFloat() * 0.2F);
      this.ticksInGround = 0;
      this.ticksInAir = 0;
    } else {
      this.ticksInAir++;
    } 
    Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
    Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
    vec3 = new Vec3(this.posX, this.posY, this.posZ);
    vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    if (movingobjectposition != null)
      vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord); 
    if (!this.worldObj.isRemote) {
      Entity entity = null;
      List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
      double d0 = 0.0D;
      EntityLivingBase entitylivingbase = getThrower();
      for (int j = 0; j < list.size(); j++) {
        Entity entity1 = list.get(j);
        if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5)) {
          float f = 0.3F;
          AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
          MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
          if (movingobjectposition1 != null) {
            double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);
            if (d1 < d0 || d0 == 0.0D) {
              entity = entity1;
              d0 = d1;
            } 
          } 
        } 
      } 
      if (entity != null)
        movingobjectposition = new MovingObjectPosition(entity); 
    } 
    if (movingobjectposition != null)
      if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal) {
        func_181015_d(movingobjectposition.getBlockPos());
      } else {
        onImpact(movingobjectposition);
      }  
    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ;
    float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
    this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / Math.PI);
    for (this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      this.prevRotationPitch += 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      this.prevRotationYaw -= 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      this.prevRotationYaw += 360.0F; 
    this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
    this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    float f2 = 0.99F;
    float f3 = getGravityVelocity();
    if (isInWater()) {
      for (int i = 0; i < 4; i++) {
        float f4 = 0.25F;
        this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ, new int[0]);
      } 
      f2 = 0.8F;
    } 
    this.motionX *= f2;
    this.motionY *= f2;
    this.motionZ *= f2;
    this.motionY -= f3;
    setPosition(this.posX, this.posY, this.posZ);
  }
  
  protected float getGravityVelocity() {
    return 0.03F;
  }
  
  protected abstract void onImpact(MovingObjectPosition paramMovingObjectPosition);
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    tagCompound.setShort("xTile", (short)this.xTile);
    tagCompound.setShort("yTile", (short)this.yTile);
    tagCompound.setShort("zTile", (short)this.zTile);
    ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
    tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
    tagCompound.setByte("shake", (byte)this.throwableShake);
    tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof net.minecraft.entity.player.EntityPlayer)
      this.throwerName = this.thrower.getName(); 
    tagCompound.setString("ownerName", (this.throwerName == null) ? "" : this.throwerName);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    this.xTile = tagCompund.getShort("xTile");
    this.yTile = tagCompund.getShort("yTile");
    this.zTile = tagCompund.getShort("zTile");
    if (tagCompund.hasKey("inTile", 8)) {
      this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
    } else {
      this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
    } 
    this.throwableShake = tagCompund.getByte("shake") & 0xFF;
    this.inGround = (tagCompund.getByte("inGround") == 1);
    this.thrower = null;
    this.throwerName = tagCompund.getString("ownerName");
    if (this.throwerName != null && this.throwerName.length() == 0)
      this.throwerName = null; 
    this.thrower = getThrower();
  }
  
  public EntityLivingBase getThrower() {
    if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
      this.thrower = (EntityLivingBase)this.worldObj.getPlayerEntityByName(this.throwerName);
      if (this.thrower == null && this.worldObj instanceof WorldServer)
        try {
          Entity entity = ((WorldServer)this.worldObj).getEntityFromUuid(UUID.fromString(this.throwerName));
          if (entity instanceof EntityLivingBase)
            this.thrower = (EntityLivingBase)entity; 
        } catch (Throwable var2) {
          this.thrower = null;
        }  
    } 
    return this.thrower;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\projectile\EntityThrowable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */