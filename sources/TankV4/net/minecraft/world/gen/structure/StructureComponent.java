package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public abstract class StructureComponent {
   protected EnumFacing coordBaseMode;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   protected StructureBoundingBox boundingBox;
   protected int componentType;

   protected abstract void writeStructureToNBT(NBTTagCompound var1);

   protected void fillWithAir(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      for(int var9 = var4; var9 <= var7; ++var9) {
         for(int var10 = var3; var10 <= var6; ++var10) {
            for(int var11 = var5; var11 <= var8; ++var11) {
               this.setBlockState(var1, Blocks.air.getDefaultState(), var10, var9, var11, var2);
            }
         }
      }

   }

   protected void clearCurrentPositionBlocksUpwards(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      BlockPos var6 = new BlockPos(this.getXWithOffset(var2, var4), this.getYWithOffset(var3), this.getZWithOffset(var2, var4));
      if (var5.isVecInside(var6)) {
         while(!var1.isAirBlock(var6) && var6.getY() < 255) {
            var1.setBlockState(var6, Blocks.air.getDefaultState(), 2);
            var6 = var6.up();
         }
      }

   }

   protected void randomlyPlaceBlock(World var1, StructureBoundingBox var2, Random var3, float var4, int var5, int var6, int var7, IBlockState var8) {
      if (var3.nextFloat() < var4) {
         this.setBlockState(var1, var8, var5, var6, var7, var2);
      }

   }

   protected boolean isLiquidInStructureBoundingBox(World var1, StructureBoundingBox var2) {
      int var3 = Math.max(this.boundingBox.minX - 1, var2.minX);
      int var4 = Math.max(this.boundingBox.minY - 1, var2.minY);
      int var5 = Math.max(this.boundingBox.minZ - 1, var2.minZ);
      int var6 = Math.min(this.boundingBox.maxX + 1, var2.maxX);
      int var7 = Math.min(this.boundingBox.maxY + 1, var2.maxY);
      int var8 = Math.min(this.boundingBox.maxZ + 1, var2.maxZ);
      BlockPos.MutableBlockPos var9 = new BlockPos.MutableBlockPos();

      int var10;
      int var11;
      for(var10 = var3; var10 <= var6; ++var10) {
         for(var11 = var5; var11 <= var8; ++var11) {
            if (var1.getBlockState(var9.func_181079_c(var10, var4, var11)).getBlock().getMaterial().isLiquid()) {
               return true;
            }

            if (var1.getBlockState(var9.func_181079_c(var10, var7, var11)).getBlock().getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      for(var10 = var3; var10 <= var6; ++var10) {
         for(var11 = var4; var11 <= var7; ++var11) {
            if (var1.getBlockState(var9.func_181079_c(var10, var11, var5)).getBlock().getMaterial().isLiquid()) {
               return true;
            }

            if (var1.getBlockState(var9.func_181079_c(var10, var11, var8)).getBlock().getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      for(var10 = var5; var10 <= var8; ++var10) {
         for(var11 = var4; var11 <= var7; ++var11) {
            if (var1.getBlockState(var9.func_181079_c(var3, var11, var10)).getBlock().getMaterial().isLiquid()) {
               return true;
            }

            if (var1.getBlockState(var9.func_181079_c(var6, var11, var10)).getBlock().getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      return false;
   }

   public NBTTagCompound createStructureBaseNBT() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.setString("id", MapGenStructureIO.getStructureComponentName(this));
      var1.setTag("BB", this.boundingBox.toNBTTagIntArray());
      var1.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
      var1.setInteger("GD", this.componentType);
      this.writeStructureToNBT(var1);
      return var1;
   }

   public abstract boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3);

   protected boolean generateDispenserContents(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, int var7, List var8, int var9) {
      BlockPos var10 = new BlockPos(this.getXWithOffset(var4, var6), this.getYWithOffset(var5), this.getZWithOffset(var4, var6));
      if (var2.isVecInside(var10) && var1.getBlockState(var10).getBlock() != Blocks.dispenser) {
         var1.setBlockState(var10, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, var7)), 2);
         TileEntity var11 = var1.getTileEntity(var10);
         if (var11 instanceof TileEntityDispenser) {
            WeightedRandomChestContent.generateDispenserContents(var3, var8, (TileEntityDispenser)var11, var9);
         }

         return true;
      } else {
         return false;
      }
   }

   protected StructureComponent(int var1) {
      this.componentType = var1;
   }

   protected int getMetadataWithOffset(Block var1, int var2) {
      if (var1 == Blocks.rail) {
         if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST) {
            return var2 == 1 ? 0 : 1;
         }
      } else if (var1 instanceof BlockDoor) {
         if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (var2 == 0) {
               return 2;
            }

            if (var2 == 2) {
               return 0;
            }
         } else {
            if (this.coordBaseMode == EnumFacing.WEST) {
               return var2 + 1 & 3;
            }

            if (this.coordBaseMode == EnumFacing.EAST) {
               return var2 + 3 & 3;
            }
         }
      } else if (var1 != Blocks.stone_stairs && var1 != Blocks.oak_stairs && var1 != Blocks.nether_brick_stairs && var1 != Blocks.stone_brick_stairs && var1 != Blocks.sandstone_stairs) {
         if (var1 == Blocks.ladder) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
               if (var2 == EnumFacing.NORTH.getIndex()) {
                  return EnumFacing.SOUTH.getIndex();
               }

               if (var2 == EnumFacing.SOUTH.getIndex()) {
                  return EnumFacing.NORTH.getIndex();
               }
            } else if (this.coordBaseMode == EnumFacing.WEST) {
               if (var2 == EnumFacing.NORTH.getIndex()) {
                  return EnumFacing.WEST.getIndex();
               }

               if (var2 == EnumFacing.SOUTH.getIndex()) {
                  return EnumFacing.EAST.getIndex();
               }

               if (var2 == EnumFacing.WEST.getIndex()) {
                  return EnumFacing.NORTH.getIndex();
               }

               if (var2 == EnumFacing.EAST.getIndex()) {
                  return EnumFacing.SOUTH.getIndex();
               }
            } else if (this.coordBaseMode == EnumFacing.EAST) {
               if (var2 == EnumFacing.NORTH.getIndex()) {
                  return EnumFacing.EAST.getIndex();
               }

               if (var2 == EnumFacing.SOUTH.getIndex()) {
                  return EnumFacing.WEST.getIndex();
               }

               if (var2 == EnumFacing.WEST.getIndex()) {
                  return EnumFacing.NORTH.getIndex();
               }

               if (var2 == EnumFacing.EAST.getIndex()) {
                  return EnumFacing.SOUTH.getIndex();
               }
            }
         } else if (var1 == Blocks.stone_button) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
               if (var2 == 3) {
                  return 4;
               }

               if (var2 == 4) {
                  return 3;
               }
            } else if (this.coordBaseMode == EnumFacing.WEST) {
               if (var2 == 3) {
                  return 1;
               }

               if (var2 == 4) {
                  return 2;
               }

               if (var2 == 2) {
                  return 3;
               }

               if (var2 == 1) {
                  return 4;
               }
            } else if (this.coordBaseMode == EnumFacing.EAST) {
               if (var2 == 3) {
                  return 2;
               }

               if (var2 == 4) {
                  return 1;
               }

               if (var2 == 2) {
                  return 3;
               }

               if (var2 == 1) {
                  return 4;
               }
            }
         } else if (var1 != Blocks.tripwire_hook && !(var1 instanceof BlockDirectional)) {
            if (var1 == Blocks.piston || var1 == Blocks.sticky_piston || var1 == Blocks.lever || var1 == Blocks.dispenser) {
               if (this.coordBaseMode == EnumFacing.SOUTH) {
                  if (var2 == EnumFacing.NORTH.getIndex() || var2 == EnumFacing.SOUTH.getIndex()) {
                     return EnumFacing.getFront(var2).getOpposite().getIndex();
                  }
               } else if (this.coordBaseMode == EnumFacing.WEST) {
                  if (var2 == EnumFacing.NORTH.getIndex()) {
                     return EnumFacing.WEST.getIndex();
                  }

                  if (var2 == EnumFacing.SOUTH.getIndex()) {
                     return EnumFacing.EAST.getIndex();
                  }

                  if (var2 == EnumFacing.WEST.getIndex()) {
                     return EnumFacing.NORTH.getIndex();
                  }

                  if (var2 == EnumFacing.EAST.getIndex()) {
                     return EnumFacing.SOUTH.getIndex();
                  }
               } else if (this.coordBaseMode == EnumFacing.EAST) {
                  if (var2 == EnumFacing.NORTH.getIndex()) {
                     return EnumFacing.EAST.getIndex();
                  }

                  if (var2 == EnumFacing.SOUTH.getIndex()) {
                     return EnumFacing.WEST.getIndex();
                  }

                  if (var2 == EnumFacing.WEST.getIndex()) {
                     return EnumFacing.NORTH.getIndex();
                  }

                  if (var2 == EnumFacing.EAST.getIndex()) {
                     return EnumFacing.SOUTH.getIndex();
                  }
               }
            }
         } else {
            EnumFacing var3 = EnumFacing.getHorizontal(var2);
            if (this.coordBaseMode == EnumFacing.SOUTH) {
               if (var3 == EnumFacing.SOUTH || var3 == EnumFacing.NORTH) {
                  return var3.getOpposite().getHorizontalIndex();
               }
            } else if (this.coordBaseMode == EnumFacing.WEST) {
               if (var3 == EnumFacing.NORTH) {
                  return EnumFacing.WEST.getHorizontalIndex();
               }

               if (var3 == EnumFacing.SOUTH) {
                  return EnumFacing.EAST.getHorizontalIndex();
               }

               if (var3 == EnumFacing.WEST) {
                  return EnumFacing.NORTH.getHorizontalIndex();
               }

               if (var3 == EnumFacing.EAST) {
                  return EnumFacing.SOUTH.getHorizontalIndex();
               }
            } else if (this.coordBaseMode == EnumFacing.EAST) {
               if (var3 == EnumFacing.NORTH) {
                  return EnumFacing.EAST.getHorizontalIndex();
               }

               if (var3 == EnumFacing.SOUTH) {
                  return EnumFacing.WEST.getHorizontalIndex();
               }

               if (var3 == EnumFacing.WEST) {
                  return EnumFacing.NORTH.getHorizontalIndex();
               }

               if (var3 == EnumFacing.EAST) {
                  return EnumFacing.SOUTH.getHorizontalIndex();
               }
            }
         }
      } else if (this.coordBaseMode == EnumFacing.SOUTH) {
         if (var2 == 2) {
            return 3;
         }

         if (var2 == 3) {
            return 2;
         }
      } else if (this.coordBaseMode == EnumFacing.WEST) {
         if (var2 == 0) {
            return 2;
         }

         if (var2 == 1) {
            return 3;
         }

         if (var2 == 2) {
            return 0;
         }

         if (var2 == 3) {
            return 1;
         }
      } else if (this.coordBaseMode == EnumFacing.EAST) {
         if (var2 == 0) {
            return 2;
         }

         if (var2 == 1) {
            return 3;
         }

         if (var2 == 2) {
            return 1;
         }

         if (var2 == 3) {
            return 0;
         }
      }

      return var2;
   }

   protected void fillWithBlocks(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockState var9, IBlockState var10, boolean var11) {
      for(int var12 = var4; var12 <= var7; ++var12) {
         for(int var13 = var3; var13 <= var6; ++var13) {
            for(int var14 = var5; var14 <= var8; ++var14) {
               if (!var11 || this.getBlockStateFromPos(var1, var13, var12, var14, var2).getBlock().getMaterial() != Material.air) {
                  if (var12 != var4 && var12 != var7 && var13 != var3 && var13 != var6 && var14 != var5 && var14 != var8) {
                     this.setBlockState(var1, var10, var13, var12, var14, var2);
                  } else {
                     this.setBlockState(var1, var9, var13, var12, var14, var2);
                  }
               }
            }
         }
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   protected int getZWithOffset(int var1, int var2) {
      if (this.coordBaseMode == null) {
         return var2;
      } else {
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
         case 3:
            return this.boundingBox.maxZ - var2;
         case 4:
            return this.boundingBox.minZ + var2;
         case 5:
         case 6:
            return this.boundingBox.minZ + var1;
         default:
            return var2;
         }
      }
   }

   protected void func_175805_a(World var1, StructureBoundingBox var2, Random var3, float var4, int var5, int var6, int var7, int var8, int var9, int var10, IBlockState var11, IBlockState var12, boolean var13) {
      for(int var14 = var6; var14 <= var9; ++var14) {
         for(int var15 = var5; var15 <= var8; ++var15) {
            for(int var16 = var7; var16 <= var10; ++var16) {
               if (var3.nextFloat() <= var4 && (!var13 || this.getBlockStateFromPos(var1, var15, var14, var16, var2).getBlock().getMaterial() != Material.air)) {
                  if (var14 != var6 && var14 != var9 && var15 != var5 && var15 != var8 && var16 != var7 && var16 != var10) {
                     this.setBlockState(var1, var12, var15, var14, var16, var2);
                  } else {
                     this.setBlockState(var1, var11, var15, var14, var16, var2);
                  }
               }
            }
         }
      }

   }

   public StructureBoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   protected int getXWithOffset(int var1, int var2) {
      if (this.coordBaseMode == null) {
         return var1;
      } else {
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
         case 3:
         case 4:
            return this.boundingBox.minX + var1;
         case 5:
            return this.boundingBox.maxX - var2;
         case 6:
            return this.boundingBox.minX + var2;
         default:
            return var1;
         }
      }
   }

   public void buildComponent(StructureComponent var1, List var2, Random var3) {
   }

   protected void placeDoorCurrentPosition(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, EnumFacing var7) {
      BlockPos var8 = new BlockPos(this.getXWithOffset(var4, var6), this.getYWithOffset(var5), this.getZWithOffset(var4, var6));
      if (var2.isVecInside(var8)) {
         ItemDoor.placeDoor(var1, var8, var7.rotateYCCW(), Blocks.oak_door);
      }

   }

   public void readStructureBaseNBT(World var1, NBTTagCompound var2) {
      if (var2.hasKey("BB")) {
         this.boundingBox = new StructureBoundingBox(var2.getIntArray("BB"));
      }

      int var3 = var2.getInteger("O");
      this.coordBaseMode = var3 == -1 ? null : EnumFacing.getHorizontal(var3);
      this.componentType = var2.getInteger("GD");
      this.readStructureFromNBT(var2);
   }

   public BlockPos getBoundingBoxCenter() {
      return new BlockPos(this.boundingBox.getCenter());
   }

   protected void setBlockState(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
      BlockPos var7 = new BlockPos(this.getXWithOffset(var3, var5), this.getYWithOffset(var4), this.getZWithOffset(var3, var5));
      if (var6.isVecInside(var7)) {
         var1.setBlockState(var7, var2, 2);
      }

   }

   protected int getYWithOffset(int var1) {
      return this.coordBaseMode == null ? var1 : var1 + this.boundingBox.minY;
   }

   protected void fillWithRandomizedBlocks(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, Random var10, StructureComponent.BlockSelector var11) {
      for(int var12 = var4; var12 <= var7; ++var12) {
         for(int var13 = var3; var13 <= var6; ++var13) {
            for(int var14 = var5; var14 <= var8; ++var14) {
               if (!var9 || this.getBlockStateFromPos(var1, var13, var12, var14, var2).getBlock().getMaterial() != Material.air) {
                  var11.selectBlocks(var10, var13, var12, var14, var12 == var4 || var12 == var7 || var13 == var3 || var13 == var6 || var14 == var5 || var14 == var8);
                  this.setBlockState(var1, var11.getBlockState(), var13, var12, var14, var2);
               }
            }
         }
      }

   }

   public int getComponentType() {
      return this.componentType;
   }

   protected IBlockState getBlockStateFromPos(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      int var6 = this.getXWithOffset(var2, var4);
      int var7 = this.getYWithOffset(var3);
      int var8 = this.getZWithOffset(var2, var4);
      BlockPos var9 = new BlockPos(var6, var7, var8);
      return !var5.isVecInside(var9) ? Blocks.air.getDefaultState() : var1.getBlockState(var9);
   }

   protected void randomlyRareFillWithBlocks(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockState var9, boolean var10) {
      float var11 = (float)(var6 - var3 + 1);
      float var12 = (float)(var7 - var4 + 1);
      float var13 = (float)(var8 - var5 + 1);
      float var14 = (float)var3 + var11 / 2.0F;
      float var15 = (float)var5 + var13 / 2.0F;

      for(int var16 = var4; var16 <= var7; ++var16) {
         float var17 = (float)(var16 - var4) / var12;

         for(int var18 = var3; var18 <= var6; ++var18) {
            float var19 = ((float)var18 - var14) / (var11 * 0.5F);

            for(int var20 = var5; var20 <= var8; ++var20) {
               float var21 = ((float)var20 - var15) / (var13 * 0.5F);
               if (!var10 || this.getBlockStateFromPos(var1, var18, var16, var20, var2).getBlock().getMaterial() != Material.air) {
                  float var22 = var19 * var19 + var17 * var17 + var21 * var21;
                  if (var22 <= 1.05F) {
                     this.setBlockState(var1, var9, var18, var16, var20, var2);
                  }
               }
            }
         }
      }

   }

   public void func_181138_a(int var1, int var2, int var3) {
      this.boundingBox.offset(var1, var2, var3);
   }

   protected void replaceAirAndLiquidDownwards(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
      int var7 = this.getXWithOffset(var3, var5);
      int var8 = this.getYWithOffset(var4);
      int var9 = this.getZWithOffset(var3, var5);
      if (var6.isVecInside(new BlockPos(var7, var8, var9))) {
         while((var1.isAirBlock(new BlockPos(var7, var8, var9)) || var1.getBlockState(new BlockPos(var7, var8, var9)).getBlock().getMaterial().isLiquid()) && var8 > 1) {
            var1.setBlockState(new BlockPos(var7, var8, var9), var2, 2);
            --var8;
         }
      }

   }

   public static StructureComponent findIntersecting(List var0, StructureBoundingBox var1) {
      Iterator var3 = var0.iterator();

      StructureComponent var2;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var2 = (StructureComponent)var3.next();
      } while(var2.getBoundingBox() == null || !var2.getBoundingBox().intersectsWith(var1));

      return var2;
   }

   public StructureComponent() {
   }

   protected boolean generateChestContents(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, List var7, int var8) {
      BlockPos var9 = new BlockPos(this.getXWithOffset(var4, var6), this.getYWithOffset(var5), this.getZWithOffset(var4, var6));
      if (var2.isVecInside(var9) && var1.getBlockState(var9).getBlock() != Blocks.chest) {
         IBlockState var10 = Blocks.chest.getDefaultState();
         var1.setBlockState(var9, Blocks.chest.correctFacing(var1, var9, var10), 2);
         TileEntity var11 = var1.getTileEntity(var9);
         if (var11 instanceof TileEntityChest) {
            WeightedRandomChestContent.generateChestContents(var3, var7, (TileEntityChest)var11, var8);
         }

         return true;
      } else {
         return false;
      }
   }

   protected abstract void readStructureFromNBT(NBTTagCompound var1);

   public abstract static class BlockSelector {
      protected IBlockState blockstate;

      public BlockSelector() {
         this.blockstate = Blocks.air.getDefaultState();
      }

      public IBlockState getBlockState() {
         return this.blockstate;
      }

      public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);
   }
}
