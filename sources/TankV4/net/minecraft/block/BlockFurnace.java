package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer {
   private static boolean keepInventory;
   public static final PropertyDirection FACING;
   private final boolean isBurning;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

   private void setDefaultFacing(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         Block var4 = var1.getBlockState(var2.north()).getBlock();
         Block var5 = var1.getBlockState(var2.south()).getBlock();
         Block var6 = var1.getBlockState(var2.west()).getBlock();
         Block var7 = var1.getBlockState(var2.east()).getBlock();
         EnumFacing var8 = (EnumFacing)var3.getValue(FACING);
         if (var8 == EnumFacing.NORTH && var4.isFullBlock() && !var5.isFullBlock()) {
            var8 = EnumFacing.SOUTH;
         } else if (var8 == EnumFacing.SOUTH && var5.isFullBlock() && !var4.isFullBlock()) {
            var8 = EnumFacing.NORTH;
         } else if (var8 == EnumFacing.WEST && var6.isFullBlock() && !var7.isFullBlock()) {
            var8 = EnumFacing.EAST;
         } else if (var8 == EnumFacing.EAST && var7.isFullBlock() && !var6.isFullBlock()) {
            var8 = EnumFacing.WEST;
         }

         var1.setBlockState(var2, var3.withProperty(FACING, var8), 2);
      }

   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityFurnace();
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!keepInventory) {
         TileEntity var4 = var1.getTileEntity(var2);
         if (var4 instanceof TileEntityFurnace) {
            InventoryHelper.dropInventoryItems(var1, var2, (TileEntityFurnace)var4);
            var1.updateComparatorOutputLevel(var2, this);
         }
      }

      super.breakBlock(var1, var2, var3);
   }

   public int getMetaFromState(IBlockState var1) {
      return ((EnumFacing)var1.getValue(FACING)).getIndex();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING});
   }

   public static void setState(boolean var0, World var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      TileEntity var4 = var1.getTileEntity(var2);
      keepInventory = true;
      if (var0) {
         var1.setBlockState(var2, Blocks.lit_furnace.getDefaultState().withProperty(FACING, (EnumFacing)var3.getValue(FACING)), 3);
         var1.setBlockState(var2, Blocks.lit_furnace.getDefaultState().withProperty(FACING, (EnumFacing)var3.getValue(FACING)), 3);
      } else {
         var1.setBlockState(var2, Blocks.furnace.getDefaultState().withProperty(FACING, (EnumFacing)var3.getValue(FACING)), 3);
         var1.setBlockState(var2, Blocks.furnace.getDefaultState().withProperty(FACING, (EnumFacing)var3.getValue(FACING)), 3);
      }

      keepInventory = false;
      if (var4 != null) {
         var4.validate();
         var1.setTileEntity(var2, var4);
      }

   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, var8.getHorizontalFacing().getOpposite());
   }

   public int getRenderType() {
      return 3;
   }

   public Item getItem(World var1, BlockPos var2) {
      return Item.getItemFromBlock(Blocks.furnace);
   }

   public IBlockState getStateFromMeta(int var1) {
      EnumFacing var2 = EnumFacing.getFront(var1);
      if (var2.getAxis() == EnumFacing.Axis.Y) {
         var2 = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, var2);
   }

   public int getComparatorInputOverride(World var1, BlockPos var2) {
      return Container.calcRedstone(var1.getTileEntity(var2));
   }

   protected BlockFurnace(boolean var1) {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.isBurning = var1;
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   public IBlockState getStateForEntityRender(IBlockState var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.setDefaultFacing(var1, var2, var3);
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if (var9 instanceof TileEntityFurnace) {
            var4.displayGUIChest((TileEntityFurnace)var9);
            var4.triggerAchievement(StatList.field_181741_Y);
         }

         return true;
      }
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      var1.setBlockState(var2, var3.withProperty(FACING, var4.getHorizontalFacing().getOpposite()), 2);
      if (var5.hasDisplayName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if (var6 instanceof TileEntityFurnace) {
            ((TileEntityFurnace)var6).setCustomInventoryName(var5.getDisplayName());
         }
      }

   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.furnace);
   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this.isBurning) {
         EnumFacing var5 = (EnumFacing)var3.getValue(FACING);
         double var6 = (double)var2.getX() + 0.5D;
         double var8 = (double)var2.getY() + var4.nextDouble() * 6.0D / 16.0D;
         double var10 = (double)var2.getZ() + 0.5D;
         double var12 = 0.52D;
         double var14 = var4.nextDouble() * 0.6D - 0.3D;
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var5.ordinal()]) {
         case 3:
            var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14, var8, var10 - var12, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle(EnumParticleTypes.FLAME, var6 + var14, var8, var10 - var12, 0.0D, 0.0D, 0.0D);
            break;
         case 4:
            var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14, var8, var10 + var12, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle(EnumParticleTypes.FLAME, var6 + var14, var8, var10 + var12, 0.0D, 0.0D, 0.0D);
            break;
         case 5:
            var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 - var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle(EnumParticleTypes.FLAME, var6 - var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D);
            break;
         case 6:
            var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle(EnumParticleTypes.FLAME, var6 + var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D);
         }
      }

   }
}
