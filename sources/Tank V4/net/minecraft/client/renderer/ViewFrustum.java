package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum {
   protected int countChunksX;
   public RenderChunk[] renderChunks;
   protected final RenderGlobal renderGlobal;
   private static final String __OBFID = "CL_00002531";
   protected int countChunksZ;
   protected final World world;
   protected int countChunksY;

   public RenderChunk getRenderChunk(BlockPos var1) {
      int var2 = var1.getX() >> 4;
      int var3 = var1.getY() >> 4;
      int var4 = var1.getZ() >> 4;
      if (var3 >= 0 && var3 < this.countChunksY) {
         var2 %= this.countChunksX;
         if (var2 < 0) {
            var2 += this.countChunksX;
         }

         var4 %= this.countChunksZ;
         if (var4 < 0) {
            var4 += this.countChunksZ;
         }

         int var5 = (var4 * this.countChunksY + var3) * this.countChunksX + var2;
         return this.renderChunks[var5];
      } else {
         return null;
      }
   }

   public void deleteGlResources() {
      RenderChunk[] var4;
      int var3 = (var4 = this.renderChunks).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         RenderChunk var1 = var4[var2];
         var1.deleteGlResources();
      }

   }

   public void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = MathHelper.bucketInt(var1, 16);
      int var8 = MathHelper.bucketInt(var2, 16);
      int var9 = MathHelper.bucketInt(var3, 16);
      int var10 = MathHelper.bucketInt(var4, 16);
      int var11 = MathHelper.bucketInt(var5, 16);
      int var12 = MathHelper.bucketInt(var6, 16);

      for(int var13 = var7; var13 <= var10; ++var13) {
         int var14 = var13 % this.countChunksX;
         if (var14 < 0) {
            var14 += this.countChunksX;
         }

         for(int var15 = var8; var15 <= var11; ++var15) {
            int var16 = var15 % this.countChunksY;
            if (var16 < 0) {
               var16 += this.countChunksY;
            }

            for(int var17 = var9; var17 <= var12; ++var17) {
               int var18 = var17 % this.countChunksZ;
               if (var18 < 0) {
                  var18 += this.countChunksZ;
               }

               int var19 = (var18 * this.countChunksY + var16) * this.countChunksX + var14;
               RenderChunk var20 = this.renderChunks[var19];
               var20.setNeedsUpdate(true);
            }
         }
      }

   }

   private int func_178157_a(int var1, int var2, int var3) {
      int var4 = var3 * 16;
      int var5 = var4 - var1 + var2 / 2;
      if (var5 < 0) {
         var5 -= var2 - 1;
      }

      return var4 - var5 / var2 * var2;
   }

   public void updateChunkPositions(double var1, double var3) {
      int var5 = MathHelper.floor_double(var1) - 8;
      int var6 = MathHelper.floor_double(var3) - 8;
      int var7 = this.countChunksX * 16;

      for(int var8 = 0; var8 < this.countChunksX; ++var8) {
         int var9 = this.func_178157_a(var5, var7, var8);

         for(int var10 = 0; var10 < this.countChunksZ; ++var10) {
            int var11 = this.func_178157_a(var6, var7, var10);

            for(int var12 = 0; var12 < this.countChunksY; ++var12) {
               int var13 = var12 * 16;
               RenderChunk var14 = this.renderChunks[(var10 * this.countChunksY + var12) * this.countChunksX + var8];
               BlockPos var15 = var14.getPosition();
               if (var15.getX() != var9 || var15.getY() != var13 || var15.getZ() != var11) {
                  BlockPos var16 = new BlockPos(var9, var13, var11);
                  if (!var16.equals(var14.getPosition())) {
                     var14.setPosition(var16);
                  }
               }
            }
         }
      }

   }

   public ViewFrustum(World var1, int var2, RenderGlobal var3, IRenderChunkFactory var4) {
      this.renderGlobal = var3;
      this.world = var1;
      this.setCountChunksXYZ(var2);
      this.createRenderChunks(var4);
   }

   protected void setCountChunksXYZ(int var1) {
      int var2 = var1 * 2 + 1;
      this.countChunksX = var2;
      this.countChunksY = 16;
      this.countChunksZ = var2;
   }

   protected void createRenderChunks(IRenderChunkFactory var1) {
      int var2 = this.countChunksX * this.countChunksY * this.countChunksZ;
      this.renderChunks = new RenderChunk[var2];
      int var3 = 0;

      for(int var4 = 0; var4 < this.countChunksX; ++var4) {
         for(int var5 = 0; var5 < this.countChunksY; ++var5) {
            for(int var6 = 0; var6 < this.countChunksZ; ++var6) {
               int var7 = (var6 * this.countChunksY + var5) * this.countChunksX + var4;
               BlockPos var8 = new BlockPos(var4 * 16, var5 * 16, var6 * 16);
               this.renderChunks[var7] = var1.makeRenderChunk(this.world, this.renderGlobal, var8, var3++);
            }
         }
      }

   }
}
