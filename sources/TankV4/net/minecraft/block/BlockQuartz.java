package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockQuartz extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockQuartz.EnumType.class);
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;

   public MapColor getMapColor(IBlockState var1) {
      return MapColor.quartzColor;
   }

   public BlockQuartz() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public int damageDropped(IBlockState var1) {
      BlockQuartz.EnumType var2 = (BlockQuartz.EnumType)var1.getValue(VARIANT);
      return var2 != BlockQuartz.EnumType.LINES_X && var2 != BlockQuartz.EnumType.LINES_Z ? var2.getMetadata() : BlockQuartz.EnumType.LINES_Y.getMetadata();
   }

   protected ItemStack createStackedBlock(IBlockState var1) {
      BlockQuartz.EnumType var2 = (BlockQuartz.EnumType)var1.getValue(VARIANT);
      return var2 != BlockQuartz.EnumType.LINES_X && var2 != BlockQuartz.EnumType.LINES_Z ? super.createStackedBlock(var1) : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetadata());
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.byMetadata(var1));
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.Axis.values().length];

         try {
            var0[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis = var0;
         return var0;
      }
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (var7 == BlockQuartz.EnumType.LINES_Y.getMetadata()) {
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis()[var3.getAxis().ordinal()]) {
         case 1:
            return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
         case 2:
         default:
            return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Y);
         case 3:
            return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
         }
      } else {
         return var7 == BlockQuartz.EnumType.CHISELED.getMetadata() ? this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.CHISELED) : this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT);
      }
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockQuartz.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
      var3.add(new ItemStack(var1, 1, BlockQuartz.EnumType.CHISELED.getMetadata()));
      var3.add(new ItemStack(var1, 1, BlockQuartz.EnumType.LINES_Y.getMetadata()));
   }

   public static enum EnumType implements IStringSerializable {
      LINES_Y(2, "lines_y", "lines");

      private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[values().length];
      LINES_X(3, "lines_x", "lines"),
      LINES_Z(4, "lines_z", "lines"),
      DEFAULT(0, "default", "default");

      private final String field_176805_h;
      private static final BlockQuartz.EnumType[] ENUM$VALUES = new BlockQuartz.EnumType[]{DEFAULT, CHISELED, LINES_Y, LINES_X, LINES_Z};
      private final String unlocalizedName;
      CHISELED(1, "chiseled", "chiseled");

      private final int meta;

      public static BlockQuartz.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      private EnumType(int var3, String var4, String var5) {
         this.meta = var3;
         this.field_176805_h = var4;
         this.unlocalizedName = var5;
      }

      public String toString() {
         return this.unlocalizedName;
      }

      public String getName() {
         return this.field_176805_h;
      }

      static {
         BlockQuartz.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockQuartz.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      public int getMetadata() {
         return this.meta;
      }
   }
}
