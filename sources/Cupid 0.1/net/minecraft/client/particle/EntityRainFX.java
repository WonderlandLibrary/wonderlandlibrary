package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityRainFX extends EntityFX {
  protected EntityRainFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
    this.motionX *= 0.30000001192092896D;
    this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
    this.motionZ *= 0.30000001192092896D;
    this.particleRed = 1.0F;
    this.particleGreen = 1.0F;
    this.particleBlue = 1.0F;
    setParticleTextureIndex(19 + this.rand.nextInt(4));
    setSize(0.01F, 0.01F);
    this.particleGravity = 0.06F;
    this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.motionY -= this.particleGravity;
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.9800000190734863D;
    this.motionY *= 0.9800000190734863D;
    this.motionZ *= 0.9800000190734863D;
    if (this.particleMaxAge-- <= 0)
      setDead(); 
    if (this.onGround) {
      if (Math.random() < 0.5D)
        setDead(); 
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
    } 
    BlockPos blockpos = new BlockPos(this);
    IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    block.setBlockBoundsBasedOnState((IBlockAccess)this.worldObj, blockpos);
    Material material = iblockstate.getBlock().getMaterial();
    if (material.isLiquid() || material.isSolid()) {
      double d0 = 0.0D;
      if (iblockstate.getBlock() instanceof BlockLiquid) {
        d0 = (1.0F - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue()));
      } else {
        d0 = block.getBlockBoundsMaxY();
      } 
      double d1 = MathHelper.floor_double(this.posY) + d0;
      if (this.posY < d1)
        setDead(); 
    } 
  }
  
  public static class Factory implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new EntityRainFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\particle\EntityRainFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */