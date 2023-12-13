package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
      public boolean apply(BlockRailBase.EnumRailDirection var1) {
         return var1 != BlockRailBase.EnumRailDirection.NORTH_EAST && var1 != BlockRailBase.EnumRailDirection.NORTH_WEST && var1 != BlockRailBase.EnumRailDirection.SOUTH_EAST && var1 != BlockRailBase.EnumRailDirection.SOUTH_WEST;
      }

      public boolean apply(Object var1) {
         return this.apply((BlockRailBase.EnumRailDirection)var1);
      }
   });

   public BlockRailDetector() {
      super(true);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
      this.setTickRandomly(true);
   }

   public int tickRate(World var1) {
      return 20;
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockRailBase.EnumRailDirection)var1.getValue(SHAPE)).getMetadata();
      if ((Boolean)var1.getValue(POWERED)) {
         var3 |= 8;
      }

      return var3;
   }

   protected List findMinecarts(World var1, BlockPos var2, Class var3, Predicate... var4) {
      AxisAlignedBB var5 = this.getDectectionBox(var2);
      return var4.length != 1 ? var1.getEntitiesWithinAABB(var3, var5) : var1.getEntitiesWithinAABB(var3, var5, var4[0]);
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(var1, var2, var3);
      this.updatePoweredState(var1, var2, var3);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote && (Boolean)var3.getValue(POWERED)) {
         this.updatePoweredState(var1, var2, var3);
      }

   }

   public int getComparatorInputOverride(World var1, BlockPos var2) {
      if ((Boolean)var1.getBlockState(var2).getValue(POWERED)) {
         List var3 = this.findMinecarts(var1, var2, EntityMinecartCommandBlock.class);
         if (!var3.isEmpty()) {
            return ((EntityMinecartCommandBlock)var3.get(0)).getCommandBlockLogic().getSuccessCount();
         }

         List var4 = this.findMinecarts(var1, var2, EntityMinecart.class, EntitySelectors.selectInventories);
         if (!var4.isEmpty()) {
            return Container.calcRedstoneFromInventory((IInventory)var4.get(0));
         }
      }

      return 0;
   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!var1.isRemote && !(Boolean)var3.getValue(POWERED)) {
         this.updatePoweredState(var1, var2, var3);
      }

   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{SHAPE, POWERED});
   }

   public IProperty getShapeProperty() {
      return SHAPE;
   }

   private void updatePoweredState(World var1, BlockPos var2, IBlockState var3) {
      boolean var4 = (Boolean)var3.getValue(POWERED);
      boolean var5 = false;
      List var6 = this.findMinecarts(var1, var2, EntityMinecart.class);
      if (!var6.isEmpty()) {
         var5 = true;
      }

      if (var5 && !var4) {
         var1.setBlockState(var2, var3.withProperty(POWERED, true), 3);
         var1.notifyNeighborsOfStateChange(var2, this);
         var1.notifyNeighborsOfStateChange(var2.down(), this);
         var1.markBlockRangeForRenderUpdate(var2, var2);
      }

      if (!var5 && var4) {
         var1.setBlockState(var2, var3.withProperty(POWERED, false), 3);
         var1.notifyNeighborsOfStateChange(var2, this);
         var1.notifyNeighborsOfStateChange(var2.down(), this);
         var1.markBlockRangeForRenderUpdate(var2, var2);
      }

      if (var5) {
         var1.scheduleUpdate(var2, this, this.tickRate(var1));
      }

      var1.updateComparatorOutputLevel(var2, this);
   }

   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   private AxisAlignedBB getDectectionBox(BlockPos var1) {
      float var2 = 0.2F;
      return new AxisAlignedBB((double)((float)var1.getX() + 0.2F), (double)var1.getY(), (double)((float)var1.getZ() + 0.2F), (double)((float)(var1.getX() + 1) - 0.2F), (double)((float)(var1.getY() + 1) - 0.2F), (double)((float)(var1.getZ() + 1) - 0.2F));
   }

   public boolean canProvidePower() {
      return true;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(var1 & 7)).withProperty(POWERED, (var1 & 8) > 0);
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return !(Boolean)var3.getValue(POWERED) ? 0 : (var4 == EnumFacing.UP ? 15 : 0);
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return (Boolean)var3.getValue(POWERED) ? 15 : 0;
   }
}
