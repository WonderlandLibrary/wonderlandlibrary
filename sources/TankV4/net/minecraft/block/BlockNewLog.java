package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockNewLog extends BlockLog {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
      public boolean apply(Object var1) {
         return this.apply((BlockPlanks.EnumType)var1);
      }

      public boolean apply(BlockPlanks.EnumType var1) {
         return var1.getMetadata() >= 4;
      }
   });
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockLog$EnumAxis;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType;

   protected ItemStack createStackedBlock(IBlockState var1) {
      return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)var1.getValue(VARIANT)).getMetadata() - 4);
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockPlanks.EnumType)var1.getValue(VARIANT)).getMetadata() - 4;
   }

   public BlockNewLog() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
   }

   public MapColor getMapColor(IBlockState var1) {
      BlockPlanks.EnumType var2 = (BlockPlanks.EnumType)var1.getValue(VARIANT);
      switch($SWITCH_TABLE$net$minecraft$block$BlockLog$EnumAxis()[((BlockLog.EnumAxis)var1.getValue(LOG_AXIS)).ordinal()]) {
      case 1:
      case 3:
      case 4:
      default:
         switch($SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType()[var2.ordinal()]) {
         case 5:
         default:
            return MapColor.stoneColor;
         case 6:
            return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
         }
      case 2:
         return var2.func_181070_c();
      }
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockLog$EnumAxis() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockLog$EnumAxis;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockLog.EnumAxis.values().length];

         try {
            var0[BlockLog.EnumAxis.NONE.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockLog.EnumAxis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockLog.EnumAxis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockLog.EnumAxis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockLog$EnumAxis = var0;
         return var0;
      }
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
      var3.add(new ItemStack(var1, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT, LOG_AXIS});
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockPlanks.EnumType.values().length];

         try {
            var0[BlockPlanks.EnumType.ACACIA.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockPlanks.EnumType.BIRCH.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockPlanks.EnumType.JUNGLE.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockPlanks.EnumType.OAK.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockPlanks.EnumType.SPRUCE.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType = var0;
         return var0;
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      IBlockState var2 = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((var1 & 3) + 4));
      switch(var1 & 12) {
      case 0:
         var2 = var2.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
         break;
      case 4:
         var2 = var2.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
         break;
      case 8:
         var2 = var2.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
         break;
      default:
         var2 = var2.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
      }

      return var2;
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockPlanks.EnumType)var1.getValue(VARIANT)).getMetadata() - 4;
      switch($SWITCH_TABLE$net$minecraft$block$BlockLog$EnumAxis()[((BlockLog.EnumAxis)var1.getValue(LOG_AXIS)).ordinal()]) {
      case 1:
         var3 |= 4;
      case 2:
      default:
         break;
      case 3:
         var3 |= 8;
         break;
      case 4:
         var3 |= 12;
      }

      return var3;
   }
}
