package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class EntityBlockDustFX extends EntityDiggingFX {
   protected EntityBlockDustFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, IBlockState var14) {
      super(var1, var2, var4, var6, var8, var10, var12, var14);
      this.motionX = var8;
      this.motionY = var10;
      this.motionZ = var12;
   }

   public static class Factory implements IParticleFactory {
      public EntityFX getEntityFX(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         IBlockState var16 = Block.getStateById(var15[0]);
         return var16.getBlock().getRenderType() == -1 ? null : (new EntityBlockDustFX(var2, var3, var5, var7, var9, var11, var13, var16)).func_174845_l();
      }
   }
}
