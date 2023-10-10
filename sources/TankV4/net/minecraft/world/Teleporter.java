package net.minecraft.world;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;

public class Teleporter {
   private final List destinationCoordinateKeys = Lists.newArrayList();
   private final Random random;
   private final WorldServer worldServerInstance;
   private final LongHashMap destinationCoordinateCache = new LongHashMap();

   public void removeStalePortalLocations(long var1) {
      if (var1 % 100L == 0L) {
         Iterator var3 = this.destinationCoordinateKeys.iterator();
         long var4 = var1 - 300L;

         while(true) {
            Long var6;
            Teleporter.PortalPosition var7;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var6 = (Long)var3.next();
               var7 = (Teleporter.PortalPosition)this.destinationCoordinateCache.getValueByKey(var6);
            } while(var7 != null && var7.lastUpdateTime >= var4);

            var3.remove();
            this.destinationCoordinateCache.remove(var6);
         }
      }
   }

   public void placeInPortal(Entity var1, float var2) {
      if (this.worldServerInstance.provider.getDimensionId() != 1) {
         if (var2 != false) {
            this.makePortal(var1);
            this.placeInExistingPortal(var1, var2);
         }
      } else {
         int var3 = MathHelper.floor_double(var1.posX);
         int var4 = MathHelper.floor_double(var1.posY) - 1;
         int var5 = MathHelper.floor_double(var1.posZ);
         byte var6 = 1;
         byte var7 = 0;

         for(int var8 = -2; var8 <= 2; ++var8) {
            for(int var9 = -2; var9 <= 2; ++var9) {
               for(int var10 = -1; var10 < 3; ++var10) {
                  int var11 = var3 + var9 * var6 + var8 * var7;
                  int var12 = var4 + var10;
                  int var13 = var5 + var9 * var7 - var8 * var6;
                  boolean var14 = var10 < 0;
                  this.worldServerInstance.setBlockState(new BlockPos(var11, var12, var13), var14 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
               }
            }
         }

         var1.setLocationAndAngles((double)var3, (double)var4, (double)var5, var1.rotationYaw, 0.0F);
         var1.motionX = var1.motionY = var1.motionZ = 0.0D;
      }

   }

   public Teleporter(WorldServer var1) {
      this.worldServerInstance = var1;
      this.random = new Random(var1.getSeed());
   }

   public boolean makePortal(Entity var1) {
      byte var2 = 16;
      double var3 = -1.0D;
      int var5 = MathHelper.floor_double(var1.posX);
      int var6 = MathHelper.floor_double(var1.posY);
      int var7 = MathHelper.floor_double(var1.posZ);
      int var8 = var5;
      int var9 = var6;
      int var10 = var7;
      int var11 = 0;
      int var12 = this.random.nextInt(4);
      BlockPos.MutableBlockPos var13 = new BlockPos.MutableBlockPos();

      int var14;
      double var15;
      int var17;
      double var18;
      int var20;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      int var27;
      int var28;
      double var33;
      double var35;
      for(var14 = var5 - var2; var14 <= var5 + var2; ++var14) {
         var15 = (double)var14 + 0.5D - var1.posX;

         for(var17 = var7 - var2; var17 <= var7 + var2; ++var17) {
            var18 = (double)var17 + 0.5D - var1.posZ;

            label293:
            for(var20 = this.worldServerInstance.getActualHeight() - 1; var20 >= 0; --var20) {
               if (this.worldServerInstance.isAirBlock(var13.func_181079_c(var14, var20, var17))) {
                  while(var20 > 0 && this.worldServerInstance.isAirBlock(var13.func_181079_c(var14, var20 - 1, var17))) {
                     --var20;
                  }

                  for(var21 = var12; var21 < var12 + 4; ++var21) {
                     var22 = var21 % 2;
                     var23 = 1 - var22;
                     if (var21 % 4 >= 2) {
                        var22 = -var22;
                        var23 = -var23;
                     }

                     for(var24 = 0; var24 < 3; ++var24) {
                        for(var25 = 0; var25 < 4; ++var25) {
                           for(var26 = -1; var26 < 4; ++var26) {
                              var27 = var14 + (var25 - 1) * var22 + var24 * var23;
                              var28 = var20 + var26;
                              int var29 = var17 + (var25 - 1) * var23 - var24 * var22;
                              var13.func_181079_c(var27, var28, var29);
                              if (var26 < 0 && !this.worldServerInstance.getBlockState(var13).getBlock().getMaterial().isSolid() || var26 >= 0 && !this.worldServerInstance.isAirBlock(var13)) {
                                 continue label293;
                              }
                           }
                        }
                     }

                     var33 = (double)var20 + 0.5D - var1.posY;
                     var35 = var15 * var15 + var33 * var33 + var18 * var18;
                     if (var3 < 0.0D || var35 < var3) {
                        var3 = var35;
                        var8 = var14;
                        var9 = var20;
                        var10 = var17;
                        var11 = var21 % 4;
                     }
                  }
               }
            }
         }
      }

      if (var3 < 0.0D) {
         for(var14 = var5 - var2; var14 <= var5 + var2; ++var14) {
            var15 = (double)var14 + 0.5D - var1.posX;

            for(var17 = var7 - var2; var17 <= var7 + var2; ++var17) {
               var18 = (double)var17 + 0.5D - var1.posZ;

               label231:
               for(var20 = this.worldServerInstance.getActualHeight() - 1; var20 >= 0; --var20) {
                  if (this.worldServerInstance.isAirBlock(var13.func_181079_c(var14, var20, var17))) {
                     while(var20 > 0 && this.worldServerInstance.isAirBlock(var13.func_181079_c(var14, var20 - 1, var17))) {
                        --var20;
                     }

                     for(var21 = var12; var21 < var12 + 2; ++var21) {
                        var22 = var21 % 2;
                        var23 = 1 - var22;

                        for(var24 = 0; var24 < 4; ++var24) {
                           for(var25 = -1; var25 < 4; ++var25) {
                              var26 = var14 + (var24 - 1) * var22;
                              var27 = var20 + var25;
                              var28 = var17 + (var24 - 1) * var23;
                              var13.func_181079_c(var26, var27, var28);
                              if (var25 < 0 && !this.worldServerInstance.getBlockState(var13).getBlock().getMaterial().isSolid() || var25 >= 0 && !this.worldServerInstance.isAirBlock(var13)) {
                                 continue label231;
                              }
                           }
                        }

                        var33 = (double)var20 + 0.5D - var1.posY;
                        var35 = var15 * var15 + var33 * var33 + var18 * var18;
                        if (var3 < 0.0D || var35 < var3) {
                           var3 = var35;
                           var8 = var14;
                           var9 = var20;
                           var10 = var17;
                           var11 = var21 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      var14 = var8;
      int var30 = var9;
      int var16 = var10;
      var17 = var11 % 2;
      int var31 = 1 - var17;
      if (var11 % 4 >= 2) {
         var17 = -var17;
         var31 = -var31;
      }

      if (var3 < 0.0D) {
         var9 = MathHelper.clamp_int(var9, 70, this.worldServerInstance.getActualHeight() - 10);
         var30 = var9;

         for(int var19 = -1; var19 <= 1; ++var19) {
            for(var20 = 1; var20 < 3; ++var20) {
               for(var21 = -1; var21 < 3; ++var21) {
                  var22 = var14 + (var20 - 1) * var17 + var19 * var31;
                  var23 = var30 + var21;
                  var24 = var16 + (var20 - 1) * var31 - var19 * var17;
                  boolean var34 = var21 < 0;
                  this.worldServerInstance.setBlockState(new BlockPos(var22, var23, var24), var34 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
               }
            }
         }
      }

      IBlockState var32 = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, var17 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);

      for(var20 = 0; var20 < 4; ++var20) {
         for(var21 = 0; var21 < 4; ++var21) {
            for(var22 = -1; var22 < 4; ++var22) {
               var23 = var14 + (var21 - 1) * var17;
               var24 = var30 + var22;
               var25 = var16 + (var21 - 1) * var31;
               boolean var36 = var21 == 0 || var21 == 3 || var22 == -1 || var22 == 3;
               this.worldServerInstance.setBlockState(new BlockPos(var23, var24, var25), var36 ? Blocks.obsidian.getDefaultState() : var32, 2);
            }
         }

         for(var21 = 0; var21 < 4; ++var21) {
            for(var22 = -1; var22 < 4; ++var22) {
               var23 = var14 + (var21 - 1) * var17;
               var24 = var30 + var22;
               var25 = var16 + (var21 - 1) * var31;
               BlockPos var37 = new BlockPos(var23, var24, var25);
               this.worldServerInstance.notifyNeighborsOfStateChange(var37, this.worldServerInstance.getBlockState(var37).getBlock());
            }
         }
      }

      return true;
   }

   public class PortalPosition extends BlockPos {
      final Teleporter this$0;
      public long lastUpdateTime;

      public PortalPosition(Teleporter var1, BlockPos var2, long var3) {
         super(var2.getX(), var2.getY(), var2.getZ());
         this.this$0 = var1;
         this.lastUpdateTime = var3;
      }
   }
}
