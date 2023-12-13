package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree {
   double scaleWidth;
   double branchSlope;
   int trunkSize;
   List field_175948_j;
   private BlockPos basePos;
   double leafDensity;
   double heightAttenuation;
   int height;
   int heightLimit;
   int leafDistanceLimit;
   private Random rand;
   private World world;
   int heightLimitLimit;

   void func_175937_a(BlockPos var1, BlockPos var2, Block var3) {
      BlockPos var4 = var2.add(-var1.getX(), -var1.getY(), -var1.getZ());
      int var5 = this.getGreatestDistance(var4);
      float var6 = (float)var4.getX() / (float)var5;
      float var7 = (float)var4.getY() / (float)var5;
      float var8 = (float)var4.getZ() / (float)var5;

      for(int var9 = 0; var9 <= var5; ++var9) {
         BlockPos var10 = var1.add((double)(0.5F + (float)var9 * var6), (double)(0.5F + (float)var9 * var7), (double)(0.5F + (float)var9 * var8));
         BlockLog.EnumAxis var11 = this.func_175938_b(var1, var10);
         this.setBlockAndNotifyAdequately(this.world, var10, var3.getDefaultState().withProperty(BlockLog.LOG_AXIS, var11));
      }

   }

   void generateLeafNodeBases() {
      Iterator var2 = this.field_175948_j.iterator();

      while(var2.hasNext()) {
         WorldGenBigTree.FoliageCoordinates var1 = (WorldGenBigTree.FoliageCoordinates)var2.next();
         int var3 = var1.func_177999_q();
         BlockPos var4 = new BlockPos(this.basePos.getX(), var3, this.basePos.getZ());
         if (!var4.equals(var1) && var3 - this.basePos.getY() >= 0) {
            this.func_175937_a(var4, var1, Blocks.log);
         }
      }

   }

   void generateTrunk() {
      BlockPos var1 = this.basePos;
      BlockPos var2 = this.basePos.up(this.height);
      Block var3 = Blocks.log;
      this.func_175937_a(var1, var2, var3);
      if (this.trunkSize == 2) {
         this.func_175937_a(var1.east(), var2.east(), var3);
         this.func_175937_a(var1.east().south(), var2.east().south(), var3);
         this.func_175937_a(var1.south(), var2.south(), var3);
      }

   }

   void func_181631_a(BlockPos var1, float var2, IBlockState var3) {
      int var4 = (int)((double)var2 + 0.618D);

      for(int var5 = -var4; var5 <= var4; ++var5) {
         for(int var6 = -var4; var6 <= var4; ++var6) {
            if (Math.pow((double)Math.abs(var5) + 0.5D, 2.0D) + Math.pow((double)Math.abs(var6) + 0.5D, 2.0D) <= (double)(var2 * var2)) {
               BlockPos var7 = var1.add(var5, 0, var6);
               Material var8 = this.world.getBlockState(var7).getBlock().getMaterial();
               if (var8 == Material.air || var8 == Material.leaves) {
                  this.setBlockAndNotifyAdequately(this.world, var7, var3);
               }
            }
         }
      }

   }

   void generateLeaves() {
      Iterator var2 = this.field_175948_j.iterator();

      while(var2.hasNext()) {
         WorldGenBigTree.FoliageCoordinates var1 = (WorldGenBigTree.FoliageCoordinates)var2.next();
         this.generateLeafNode(var1);
      }

   }

   public void func_175904_e() {
      this.leafDistanceLimit = 5;
   }

   private int getGreatestDistance(BlockPos var1) {
      int var2 = MathHelper.abs_int(var1.getX());
      int var3 = MathHelper.abs_int(var1.getY());
      int var4 = MathHelper.abs_int(var1.getZ());
      return var4 > var2 && var4 > var3 ? var4 : (var3 > var2 ? var3 : var2);
   }

   private BlockLog.EnumAxis func_175938_b(BlockPos var1, BlockPos var2) {
      BlockLog.EnumAxis var3 = BlockLog.EnumAxis.Y;
      int var4 = Math.abs(var2.getX() - var1.getX());
      int var5 = Math.abs(var2.getZ() - var1.getZ());
      int var6 = Math.max(var4, var5);
      if (var6 > 0) {
         if (var4 == var6) {
            var3 = BlockLog.EnumAxis.X;
         } else if (var5 == var6) {
            var3 = BlockLog.EnumAxis.Z;
         }
      }

      return var3;
   }

   public boolean generate(World param1, Random param2, BlockPos param3) {
      // $FF: Couldn't be decompiled
   }

   float layerSize(int var1) {
      if ((float)var1 < (float)this.heightLimit * 0.3F) {
         return -1.0F;
      } else {
         float var2 = (float)this.heightLimit / 2.0F;
         float var3 = var2 - (float)var1;
         float var4 = MathHelper.sqrt_float(var2 * var2 - var3 * var3);
         if (var3 == 0.0F) {
            var4 = var2;
         } else if (Math.abs(var3) >= var2) {
            return 0.0F;
         }

         return var4 * 0.5F;
      }
   }

   int checkBlockLine(BlockPos var1, BlockPos var2) {
      BlockPos var3 = var2.add(-var1.getX(), -var1.getY(), -var1.getZ());
      int var4 = this.getGreatestDistance(var3);
      float var5 = (float)var3.getX() / (float)var4;
      float var6 = (float)var3.getY() / (float)var4;
      float var7 = (float)var3.getZ() / (float)var4;
      if (var4 == 0) {
         return -1;
      } else {
         for(int var8 = 0; var8 <= var4; ++var8) {
            BlockPos var9 = var1.add((double)(0.5F + (float)var8 * var5), (double)(0.5F + (float)var8 * var6), (double)(0.5F + (float)var8 * var7));
            if (!this.func_150523_a(this.world.getBlockState(var9).getBlock())) {
               return var8;
            }
         }

         return -1;
      }
   }

   void generateLeafNode(BlockPos var1) {
      for(int var2 = 0; var2 < this.leafDistanceLimit; ++var2) {
         this.func_181631_a(var1.up(var2), this.leafSize(var2), Blocks.leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false));
      }

   }

   float leafSize(int var1) {
      return var1 >= 0 && var1 < this.leafDistanceLimit ? (var1 != 0 && var1 != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
   }

   public WorldGenBigTree(boolean var1) {
      super(var1);
      this.basePos = BlockPos.ORIGIN;
      this.heightAttenuation = 0.618D;
      this.branchSlope = 0.381D;
      this.scaleWidth = 1.0D;
      this.leafDensity = 1.0D;
      this.trunkSize = 1;
      this.heightLimitLimit = 12;
      this.leafDistanceLimit = 4;
   }

   void generateLeafNodeList() {
      this.height = (int)((double)this.heightLimit * this.heightAttenuation);
      if (this.height >= this.heightLimit) {
         this.height = this.heightLimit - 1;
      }

      int var1 = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));
      if (var1 < 1) {
         var1 = 1;
      }

      int var2 = this.basePos.getY() + this.height;
      int var3 = this.heightLimit - this.leafDistanceLimit;
      this.field_175948_j = Lists.newArrayList();
      this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(this.basePos.up(var3), var2));

      for(; var3 >= 0; --var3) {
         float var4 = this.layerSize(var3);
         if (var4 >= 0.0F) {
            for(int var5 = 0; var5 < var1; ++var5) {
               double var6 = this.scaleWidth * (double)var4 * ((double)this.rand.nextFloat() + 0.328D);
               double var8 = (double)(this.rand.nextFloat() * 2.0F) * 3.141592653589793D;
               double var10 = var6 * Math.sin(var8) + 0.5D;
               double var12 = var6 * Math.cos(var8) + 0.5D;
               BlockPos var14 = this.basePos.add(var10, (double)(var3 - 1), var12);
               BlockPos var15 = var14.up(this.leafDistanceLimit);
               if (this.checkBlockLine(var14, var15) == -1) {
                  int var16 = this.basePos.getX() - var14.getX();
                  int var17 = this.basePos.getZ() - var14.getZ();
                  double var18 = (double)var14.getY() - Math.sqrt((double)(var16 * var16 + var17 * var17)) * this.branchSlope;
                  int var20 = var18 > (double)var2 ? var2 : (int)var18;
                  BlockPos var21 = new BlockPos(this.basePos.getX(), var20, this.basePos.getZ());
                  if (this.checkBlockLine(var21, var14) == -1) {
                     this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(var14, var21.getY()));
                  }
               }
            }
         }
      }

   }

   static class FoliageCoordinates extends BlockPos {
      private final int field_178000_b;

      public FoliageCoordinates(BlockPos var1, int var2) {
         super(var1.getX(), var1.getY(), var1.getZ());
         this.field_178000_b = var2;
      }

      public int func_177999_q() {
         return this.field_178000_b;
      }
   }
}
