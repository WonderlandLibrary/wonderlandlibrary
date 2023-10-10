package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection;
   public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
      public boolean apply(Object var1) {
         return this.apply((BlockRailBase.EnumRailDirection)var1);
      }

      public boolean apply(BlockRailBase.EnumRailDirection var1) {
         return var1 != BlockRailBase.EnumRailDirection.NORTH_EAST && var1 != BlockRailBase.EnumRailDirection.NORTH_WEST && var1 != BlockRailBase.EnumRailDirection.SOUTH_EAST && var1 != BlockRailBase.EnumRailDirection.SOUTH_WEST;
      }
   });

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{SHAPE, POWERED});
   }

   public IProperty getShapeProperty() {
      return SHAPE;
   }

   protected void onNeighborChangedInternal(World var1, BlockPos var2, IBlockState var3, Block var4) {
      boolean var5 = (Boolean)var3.getValue(POWERED);
      BlockRailPowered var10000;
      if (!var1.isBlockPowered(var2)) {
         var10000 = this;
         boolean var10008 = false;
      } else {
         var10000 = true;
      }

      BlockRailPowered var6 = var10000;
      if (var6 != var5) {
         var1.setBlockState(var2, var3.withProperty(POWERED, Boolean.valueOf((boolean)var6)), 3);
         var1.notifyNeighborsOfStateChange(var2.down(), this);
         if (((BlockRailBase.EnumRailDirection)var3.getValue(SHAPE)).isAscending()) {
            var1.notifyNeighborsOfStateChange(var2.up(), this);
         }
      }

   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockRailBase.EnumRailDirection.values().length];

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
         } catch (NoSuchFieldError var10) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
         } catch (NoSuchFieldError var9) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.NORTH_EAST.ordinal()] = 10;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.NORTH_WEST.ordinal()] = 9;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection = var0;
         return var0;
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(var1 & 7)).withProperty(POWERED, (var1 & 8) > 0);
   }

   protected BlockRailPowered() {
      super(true);
      this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, false));
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockRailBase.EnumRailDirection)var1.getValue(SHAPE)).getMetadata();
      if ((Boolean)var1.getValue(POWERED)) {
         var3 |= 8;
      }

      return var3;
   }
}
