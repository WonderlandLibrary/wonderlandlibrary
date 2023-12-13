package shadersmod.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class ShadowUtils {
   public static Iterator makeShadowChunkIterator(WorldClient var0, double var1, Entity var3, int var4, ViewFrustum var5) {
      float var6 = Shaders.getShadowRenderDistance();
      if (var6 > 0.0F && var6 < (float)((var4 - 1) * 16)) {
         int var18 = MathHelper.ceiling_float_int(var6 / 16.0F) + 1;
         float var19 = var0.getCelestialAngleRadians((float)var1);
         float var9 = Shaders.sunPathRotation * 0.017453292F;
         float var10 = var19 > 1.5707964F && var19 < 4.712389F ? var19 + 3.1415927F : var19;
         float var11 = -MathHelper.sin(var10);
         float var12 = MathHelper.cos(var10) * MathHelper.cos(var9);
         float var13 = -MathHelper.cos(var10) * MathHelper.sin(var9);
         BlockPos var14 = new BlockPos(MathHelper.floor_double(var3.posX) >> 4, MathHelper.floor_double(var3.posY) >> 4, MathHelper.floor_double(var3.posZ) >> 4);
         BlockPos var15 = var14.add((double)(-var11 * (float)var18), (double)(-var12 * (float)var18), (double)(-var13 * (float)var18));
         BlockPos var16 = var14.add((double)(var11 * (float)var4), (double)(var12 * (float)var4), (double)(var13 * (float)var4));
         IteratorRenderChunks var17 = new IteratorRenderChunks(var5, var15, var16, var18, var18);
         return var17;
      } else {
         List var7 = Arrays.asList(var5.renderChunks);
         Iterator var8 = var7.iterator();
         return var8;
      }
   }
}
