package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDropParticleFX extends EntityFX {
  private Material materialType;
  
  private int bobTimer;
  
  protected EntityDropParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
    this.motionX = this.motionY = this.motionZ = 0.0D;
    if (p_i1203_8_ == Material.water) {
      this.particleRed = 0.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 1.0F;
    } else {
      this.particleRed = 1.0F;
      this.particleGreen = 0.0F;
      this.particleBlue = 0.0F;
    } 
    setParticleTextureIndex(113);
    setSize(0.01F, 0.01F);
    this.particleGravity = 0.06F;
    this.materialType = p_i1203_8_;
    this.bobTimer = 40;
    this.particleMaxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
    this.motionX = this.motionY = this.motionZ = 0.0D;
  }
  
  public int getBrightnessForRender(float partialTicks) {
    return (this.materialType == Material.water) ? super.getBrightnessForRender(partialTicks) : 257;
  }
  
  public float getBrightness(float partialTicks) {
    return (this.materialType == Material.water) ? super.getBrightness(partialTicks) : 1.0F;
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.materialType == Material.water) {
      this.particleRed = 0.2F;
      this.particleGreen = 0.3F;
      this.particleBlue = 1.0F;
    } else {
      this.particleRed = 1.0F;
      this.particleGreen = 16.0F / (40 - this.bobTimer + 16);
      this.particleBlue = 4.0F / (40 - this.bobTimer + 8);
    } 
    this.motionY -= this.particleGravity;
    if (this.bobTimer-- > 0) {
      this.motionX *= 0.02D;
      this.motionY *= 0.02D;
      this.motionZ *= 0.02D;
      setParticleTextureIndex(113);
    } else {
      setParticleTextureIndex(112);
    } 
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.9800000190734863D;
    this.motionY *= 0.9800000190734863D;
    this.motionZ *= 0.9800000190734863D;
    if (this.particleMaxAge-- <= 0)
      setDead(); 
    if (this.onGround) {
      if (this.materialType == Material.water) {
        setDead();
        this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      } else {
        setParticleTextureIndex(114);
      } 
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
    } 
    BlockPos blockpos = new BlockPos(this);
    IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
    Material material = iblockstate.getBlock().getMaterial();
    if (material.isLiquid() || material.isSolid()) {
      double d0 = 0.0D;
      if (iblockstate.getBlock() instanceof BlockLiquid)
        d0 = BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue()); 
      double d1 = (MathHelper.floor_double(this.posY) + 1) - d0;
      if (this.posY < d1)
        setDead(); 
    } 
  }
  
  public static class LavaFactory implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new EntityDropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.lava);
    }
  }
  
  public static class WaterFactory implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new EntityDropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.water);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\particle\EntityDropParticleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */