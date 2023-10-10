package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {
   public final int chestType;
   public static final PropertyDirection FACING;

   public int getComparatorInputOverride(World var1, BlockPos var2) {
      return Container.calcRedstoneFromInventory(this.getLockableContainer(var1, var2));
   }

   public boolean isOpaqueCube() {
      return false;
   }

   protected BlockChest(int var1) {
      super(Material.wood);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.chestType = var1;
      this.setCreativeTab(CreativeTabs.tabDecorations);
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      int var3 = 0;
      BlockPos var4 = var2.west();
      BlockPos var5 = var2.east();
      BlockPos var6 = var2.north();
      BlockPos var7 = var2.south();
      if (var1.getBlockState(var4).getBlock() == this) {
         if (var4 != false) {
            return false;
         }

         ++var3;
      }

      if (var1.getBlockState(var5).getBlock() == this) {
         if (var5 != false) {
            return false;
         }

         ++var3;
      }

      if (var1.getBlockState(var6).getBlock() == this) {
         if (var6 != false) {
            return false;
         }

         ++var3;
      }

      if (var1.getBlockState(var7).getBlock() == this) {
         if (var7 != false) {
            return false;
         }

         ++var3;
      }

      return var3 <= 1;
   }

   public int getMetaFromState(IBlockState var1) {
      return ((EnumFacing)var1.getValue(FACING)).getIndex();
   }

   public ILockableContainer getLockableContainer(World var1, BlockPos var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      if (!(var3 instanceof TileEntityChest)) {
         return null;
      } else {
         Object var4 = (TileEntityChest)var3;
         if (var2 == false) {
            return null;
         } else {
            Iterator var6 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(true) {
               while(true) {
                  Object var5;
                  TileEntity var9;
                  do {
                     BlockPos var7;
                     Block var8;
                     do {
                        if (!var6.hasNext()) {
                           return (ILockableContainer)var4;
                        }

                        var5 = var6.next();
                        var7 = var2.offset((EnumFacing)var5);
                        var8 = var1.getBlockState(var7).getBlock();
                     } while(var8 != this);

                     if (var7 == false) {
                        return null;
                     }

                     var9 = var1.getTileEntity(var7);
                  } while(!(var9 instanceof TileEntityChest));

                  if (var5 != EnumFacing.WEST && var5 != EnumFacing.NORTH) {
                     var4 = new InventoryLargeChest("container.chestDouble", (ILockableContainer)var4, (TileEntityChest)var9);
                  } else {
                     var4 = new InventoryLargeChest("container.chestDouble", (TileEntityChest)var9, (ILockableContainer)var4);
                  }
               }
            }
         }
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      EnumFacing var2 = EnumFacing.getFront(var1);
      if (var2.getAxis() == EnumFacing.Axis.Y) {
         var2 = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, var2);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityChest();
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.checkForSurroundingChests(var1, var2, var3);
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var5.hasNext()) {
         Object var4 = var5.next();
         BlockPos var6 = var2.offset((EnumFacing)var4);
         IBlockState var7 = var1.getBlockState(var6);
         if (var7.getBlock() == this) {
            this.checkForSurroundingChests(var1, var6, var7);
         }
      }

   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if (var4 instanceof IInventory) {
         InventoryHelper.dropInventoryItems(var1, var2, (IInventory)var4);
         var1.updateComparatorOutputLevel(var2, this);
      }

      super.breakBlock(var1, var2, var3);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING});
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      EnumFacing var6 = EnumFacing.getHorizontal(MathHelper.floor_double((double)(var4.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
      var3 = var3.withProperty(FACING, var6);
      BlockPos var7 = var2.north();
      BlockPos var8 = var2.south();
      BlockPos var9 = var2.west();
      BlockPos var10 = var2.east();
      boolean var11 = this == var1.getBlockState(var7).getBlock();
      boolean var12 = this == var1.getBlockState(var8).getBlock();
      boolean var13 = this == var1.getBlockState(var9).getBlock();
      boolean var14 = this == var1.getBlockState(var10).getBlock();
      if (!var11 && !var12 && !var13 && !var14) {
         var1.setBlockState(var2, var3, 3);
      } else if (var6.getAxis() == EnumFacing.Axis.X && (var11 || var12)) {
         if (var11) {
            var1.setBlockState(var7, var3, 3);
         } else {
            var1.setBlockState(var8, var3, 3);
         }

         var1.setBlockState(var2, var3, 3);
      } else if (var6.getAxis() == EnumFacing.Axis.Z && (var13 || var14)) {
         if (var13) {
            var1.setBlockState(var9, var3, 3);
         } else {
            var1.setBlockState(var10, var3, 3);
         }

         var1.setBlockState(var2, var3, 3);
      }

      if (var5.hasDisplayName()) {
         TileEntity var15 = var1.getTileEntity(var2);
         if (var15 instanceof TileEntityChest) {
            ((TileEntityChest)var15).setCustomName(var5.getDisplayName());
         }
      }

   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      if (var1.getBlockState(var2.north()).getBlock() == this) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
      } else if (var1.getBlockState(var2.south()).getBlock() == this) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
      } else if (var1.getBlockState(var2.west()).getBlock() == this) {
         this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      } else if (var1.getBlockState(var2.east()).getBlock() == this) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
      } else {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      }

   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      if (this != false) {
         return 0;
      } else {
         int var5 = 0;
         TileEntity var6 = var1.getTileEntity(var2);
         if (var6 instanceof TileEntityChest) {
            var5 = ((TileEntityChest)var6).numPlayersUsing;
         }

         return MathHelper.clamp_int(var5, 0, 15);
      }
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   private boolean isBelowSolidBlock(World var1, BlockPos var2) {
      return var1.getBlockState(var2.up()).getBlock().isNormalCube();
   }

   public IBlockState correctFacing(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing var4 = null;
      Iterator var6 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var6.hasNext()) {
         Object var5 = var6.next();
         IBlockState var7 = var1.getBlockState(var2.offset((EnumFacing)var5));
         if (var7.getBlock() == this) {
            return var3;
         }

         if (var7.getBlock().isFullBlock()) {
            if (var4 != null) {
               var4 = null;
               break;
            }

            var4 = (EnumFacing)var5;
         }
      }

      if (var4 != null) {
         return var3.withProperty(FACING, var4.getOpposite());
      } else {
         EnumFacing var8 = (EnumFacing)var3.getValue(FACING);
         if (var1.getBlockState(var2.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.getOpposite();
         }

         if (var1.getBlockState(var2.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.rotateY();
         }

         if (var1.getBlockState(var2.offset(var8)).getBlock().isFullBlock()) {
            var8 = var8.getOpposite();
         }

         return var3.withProperty(FACING, var8);
      }
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         ILockableContainer var9 = this.getLockableContainer(var1, var2);
         if (var9 != null) {
            var4.displayGUIChest(var9);
            if (this.chestType == 0) {
               var4.triggerAchievement(StatList.field_181723_aa);
            } else if (this.chestType == 1) {
               var4.triggerAchievement(StatList.field_181737_U);
            }
         }

         return true;
      }
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      super.onNeighborBlockChange(var1, var2, var3, var4);
      TileEntity var5 = var1.getTileEntity(var2);
      if (var5 instanceof TileEntityChest) {
         var5.updateContainingBlockInfo();
      }

   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
   }

   public IBlockState checkForSurroundingChests(World var1, BlockPos var2, IBlockState var3) {
      if (var1.isRemote) {
         return var3;
      } else {
         IBlockState var4 = var1.getBlockState(var2.north());
         IBlockState var5 = var1.getBlockState(var2.south());
         IBlockState var6 = var1.getBlockState(var2.west());
         IBlockState var7 = var1.getBlockState(var2.east());
         EnumFacing var8 = (EnumFacing)var3.getValue(FACING);
         Block var9 = var4.getBlock();
         Block var10 = var5.getBlock();
         Block var11 = var6.getBlock();
         Block var12 = var7.getBlock();
         if (var9 != this && var10 != this) {
            boolean var21 = var9.isFullBlock();
            boolean var22 = var10.isFullBlock();
            if (var11 == this || var12 == this) {
               BlockPos var23 = var11 == this ? var2.west() : var2.east();
               IBlockState var24 = var1.getBlockState(var23.north());
               IBlockState var25 = var1.getBlockState(var23.south());
               var8 = EnumFacing.SOUTH;
               EnumFacing var26;
               if (var11 == this) {
                  var26 = (EnumFacing)var6.getValue(FACING);
               } else {
                  var26 = (EnumFacing)var7.getValue(FACING);
               }

               if (var26 == EnumFacing.NORTH) {
                  var8 = EnumFacing.NORTH;
               }

               Block var19 = var24.getBlock();
               Block var20 = var25.getBlock();
               if ((var21 || var19.isFullBlock()) && !var22 && !var20.isFullBlock()) {
                  var8 = EnumFacing.SOUTH;
               }

               if ((var22 || var20.isFullBlock()) && !var21 && !var19.isFullBlock()) {
                  var8 = EnumFacing.NORTH;
               }
            }
         } else {
            BlockPos var13 = var9 == this ? var2.north() : var2.south();
            IBlockState var14 = var1.getBlockState(var13.west());
            IBlockState var15 = var1.getBlockState(var13.east());
            var8 = EnumFacing.EAST;
            EnumFacing var16;
            if (var9 == this) {
               var16 = (EnumFacing)var4.getValue(FACING);
            } else {
               var16 = (EnumFacing)var5.getValue(FACING);
            }

            if (var16 == EnumFacing.WEST) {
               var8 = EnumFacing.WEST;
            }

            Block var17 = var14.getBlock();
            Block var18 = var15.getBlock();
            if ((var11.isFullBlock() || var17.isFullBlock()) && !var12.isFullBlock() && !var18.isFullBlock()) {
               var8 = EnumFacing.EAST;
            }

            if ((var12.isFullBlock() || var18.isFullBlock()) && !var11.isFullBlock() && !var17.isFullBlock()) {
               var8 = EnumFacing.WEST;
            }
         }

         var3 = var3.withProperty(FACING, var8);
         var1.setBlockState(var2, var3, 3);
         return var3;
      }
   }

   public int getRenderType() {
      return 2;
   }

   public boolean isFullCube() {
      return false;
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return var4 == EnumFacing.UP ? this.getWeakPower(var1, var2, var3, var4) : 0;
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, var8.getHorizontalFacing());
   }
}
