package optifine;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ClearWater {
   public static void updateWaterOpacity(GameSettings var0, World var1) {
      if (var0 != null) {
         byte var2 = 3;
         if (var0.ofClearWater) {
            var2 = 1;
         }

         BlockLeavesBase.setLightOpacity(Blocks.water, var2);
         BlockLeavesBase.setLightOpacity(Blocks.flowing_water, var2);
      }

      if (var1 != null) {
         IChunkProvider var25 = var1.getChunkProvider();
         if (var25 != null) {
            Entity var3 = Config.getMinecraft().getRenderViewEntity();
            if (var3 != null) {
               int var4 = (int)var3.posX / 16;
               int var5 = (int)var3.posZ / 16;
               int var6 = var4 - 512;
               int var7 = var4 + 512;
               int var8 = var5 - 512;
               int var9 = var5 + 512;
               int var10 = 0;

               for(int var11 = var6; var11 < var7; ++var11) {
                  for(int var12 = var8; var12 < var9; ++var12) {
                     if (var25.chunkExists(var11, var12)) {
                        Chunk var13 = var25.provideChunk(var11, var12);
                        if (var13 != null && !(var13 instanceof EmptyChunk)) {
                           int var14 = var11 << 4;
                           int var15 = var12 << 4;
                           int var16 = var14 + 16;
                           int var17 = var15 + 16;
                           BlockPosM var18 = new BlockPosM(0, 0, 0);
                           BlockPosM var19 = new BlockPosM(0, 0, 0);

                           for(int var20 = var14; var20 < var16; ++var20) {
                              for(int var21 = var15; var21 < var17; ++var21) {
                                 var18.setXyz(var20, 0, var21);
                                 BlockPos var22 = var1.getPrecipitationHeight(var18);

                                 for(int var23 = 0; var23 < var22.getY(); ++var23) {
                                    var19.setXyz(var20, var23, var21);
                                    IBlockState var24 = var1.getBlockState(var19);
                                    if (var24.getBlock().getMaterial() == Material.water) {
                                       var1.markBlocksDirtyVertical(var20, var21, var19.getY(), var22.getY());
                                       ++var10;
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (var10 > 0) {
                  String var26 = "server";
                  if (Config.isMinecraftThread()) {
                     var26 = "client";
                  }

                  Config.dbg("ClearWater (" + var26 + ") relighted " + var10 + " chunks");
               }
            }
         }
      }

   }
}
