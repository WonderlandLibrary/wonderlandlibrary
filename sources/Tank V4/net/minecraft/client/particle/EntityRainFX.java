package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityRainFX extends EntityFX {
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= (double)this.particleGravity;
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;
      if (this.particleMaxAge-- <= 0) {
         this.setDead();
      }

      if (this.onGround) {
         if (Math.random() < 0.5D) {
            this.setDead();
         }

         this.motionX *= 0.699999988079071D;
         this.motionZ *= 0.699999988079071D;
      }

      BlockPos var1 = new BlockPos(this);
      IBlockState var2 = this.worldObj.getBlockState(var1);
      Block var3 = var2.getBlock();
      var3.setBlockBoundsBasedOnState(this.worldObj, var1);
      Material var4 = var2.getBlock().getMaterial();
      if (var4.isLiquid() || var4.isSolid()) {
         double var5 = 0.0D;
         if (var2.getBlock() instanceof BlockLiquid) {
            var5 = (double)(1.0F - BlockLiquid.getLiquidHeightPercent((Integer)var2.getValue(BlockLiquid.LEVEL)));
         } else {
            var5 = var3.getBlockBoundsMaxY();
         }

         double var7 = (double)MathHelper.floor_double(this.posY) + var5;
         if (this.posY < var7) {
            this.setDead();
         }
      }

   }

   protected EntityRainFX(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
      this.motionX *= 0.30000001192092896D;
      this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
      this.motionZ *= 0.30000001192092896D;
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.setParticleTextureIndex(19 + this.rand.nextInt(4));
      this.setSize(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new EntityRainFX(var2, var3, var5, var7);
      }
   }
}
