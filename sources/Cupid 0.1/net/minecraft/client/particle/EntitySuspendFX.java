package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntitySuspendFX extends EntityFX {
  protected EntitySuspendFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
    super(worldIn, xCoordIn, yCoordIn - 0.125D, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    this.particleRed = 0.4F;
    this.particleGreen = 0.4F;
    this.particleBlue = 0.7F;
    setParticleTextureIndex(0);
    setSize(0.01F, 0.01F);
    this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
    this.motionX = xSpeedIn * 0.0D;
    this.motionY = ySpeedIn * 0.0D;
    this.motionZ = zSpeedIn * 0.0D;
    this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    moveEntity(this.motionX, this.motionY, this.motionZ);
    if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() != Material.water)
      setDead(); 
    if (this.particleMaxAge-- <= 0)
      setDead(); 
  }
  
  public static class Factory implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new EntitySuspendFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\particle\EntitySuspendFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */