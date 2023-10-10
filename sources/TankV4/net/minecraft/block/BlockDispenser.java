package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer {
   public static final RegistryDefaulted dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
   public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
   public static final PropertyDirection FACING = PropertyDirection.create("facing");
   protected Random rand = new Random();

   public IBlockState getStateForEntityRender(IBlockState var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if (var4 instanceof TileEntityDispenser) {
         InventoryHelper.dropInventoryItems(var1, var2, (TileEntityDispenser)var4);
         var1.updateComparatorOutputLevel(var2, this);
      }

      super.breakBlock(var1, var2, var3);
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)var1.getValue(FACING)).getIndex();
      if ((Boolean)var1.getValue(TRIGGERED)) {
         var3 |= 8;
      }

      return var3;
   }

   public static IPosition getDispensePosition(IBlockSource var0) {
      EnumFacing var1 = getFacing(var0.getBlockMetadata());
      double var2 = var0.getX() + 0.7D * (double)var1.getFrontOffsetX();
      double var4 = var0.getY() + 0.7D * (double)var1.getFrontOffsetY();
      double var6 = var0.getZ() + 0.7D * (double)var1.getFrontOffsetZ();
      return new PositionImpl(var2, var4, var6);
   }

   private void setDefaultDirection(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
         boolean var5 = var1.getBlockState(var2.north()).getBlock().isFullBlock();
         boolean var6 = var1.getBlockState(var2.south()).getBlock().isFullBlock();
         if (var4 == EnumFacing.NORTH && var5 && !var6) {
            var4 = EnumFacing.SOUTH;
         } else if (var4 == EnumFacing.SOUTH && var6 && !var5) {
            var4 = EnumFacing.NORTH;
         } else {
            boolean var7 = var1.getBlockState(var2.west()).getBlock().isFullBlock();
            boolean var8 = var1.getBlockState(var2.east()).getBlock().isFullBlock();
            if (var4 == EnumFacing.WEST && var7 && !var8) {
               var4 = EnumFacing.EAST;
            } else if (var4 == EnumFacing.EAST && var8 && !var7) {
               var4 = EnumFacing.WEST;
            }
         }

         var1.setBlockState(var2, var3.withProperty(FACING, var4).withProperty(TRIGGERED, false), 2);
      }

   }

   public int tickRate(World var1) {
      return 4;
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World var1, BlockPos var2) {
      return Container.calcRedstone(var1.getTileEntity(var2));
   }

   public int getRenderType() {
      return 3;
   }

   protected void dispense(World var1, BlockPos var2) {
      BlockSourceImpl var3 = new BlockSourceImpl(var1, var2);
      TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();
      if (var4 != null) {
         int var5 = var4.getDispenseSlot();
         if (var5 < 0) {
            var1.playAuxSFX(1001, var2, 0);
         } else {
            ItemStack var6 = var4.getStackInSlot(var5);
            IBehaviorDispenseItem var7 = this.getBehavior(var6);
            if (var7 != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
               ItemStack var8 = var7.dispense(var3, var6);
               var4.setInventorySlotContents(var5, var8.stackSize <= 0 ? null : var8);
            }
         }
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, TRIGGERED});
   }

   protected IBehaviorDispenseItem getBehavior(ItemStack var1) {
      return (IBehaviorDispenseItem)dispenseBehaviorRegistry.getObject(var1 == null ? null : var1.getItem());
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      var1.setBlockState(var2, var3.withProperty(FACING, BlockPistonBase.getFacingFromEntity(var1, var2, var4)), 2);
      if (var5.hasDisplayName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if (var6 instanceof TileEntityDispenser) {
            ((TileEntityDispenser)var6).setCustomName(var5.getDisplayName());
         }
      }

   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(var1, var2, var3);
      this.setDefaultDirection(var1, var2, var3);
   }

   public static EnumFacing getFacing(int var0) {
      return EnumFacing.getFront(var0 & 7);
   }

   protected BlockDispenser() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, false));
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityDispenser();
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(var1, var2, var8)).withProperty(TRIGGERED, false);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, getFacing(var1)).withProperty(TRIGGERED, (var1 & 8) > 0);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote) {
         this.dispense(var1, var2);
      }

   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if (var9 instanceof TileEntityDispenser) {
            var4.displayGUIChest((TileEntityDispenser)var9);
            if (var9 instanceof TileEntityDropper) {
               var4.triggerAchievement(StatList.field_181731_O);
            } else {
               var4.triggerAchievement(StatList.field_181733_Q);
            }
         }

         return true;
      }
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      boolean var5 = var1.isBlockPowered(var2) || var1.isBlockPowered(var2.up());
      boolean var6 = (Boolean)var3.getValue(TRIGGERED);
      if (var5 && !var6) {
         var1.scheduleUpdate(var2, this, this.tickRate(var1));
         var1.setBlockState(var2, var3.withProperty(TRIGGERED, true), 4);
      } else if (!var5 && var6) {
         var1.setBlockState(var2, var3.withProperty(TRIGGERED, false), 4);
      }

   }
}
