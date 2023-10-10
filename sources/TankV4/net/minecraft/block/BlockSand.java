package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockSand extends BlockFalling {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockSand.EnumType.class);

   public MapColor getMapColor(IBlockState var1) {
      return ((BlockSand.EnumType)var1.getValue(VARIANT)).getMapColor();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockSand.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockSand.EnumType[] var7;
      int var6 = (var7 = BlockSand.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockSand.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockSand.EnumType.byMetadata(var1));
   }

   public BlockSand() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSand.EnumType.SAND));
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockSand.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public static enum EnumType implements IStringSerializable {
      SAND(0, "sand", "default", MapColor.sandColor);

      private static final BlockSand.EnumType[] ENUM$VALUES = new BlockSand.EnumType[]{SAND, RED_SAND};
      private static final BlockSand.EnumType[] META_LOOKUP = new BlockSand.EnumType[values().length];
      RED_SAND(1, "red_sand", "red", MapColor.adobeColor);

      private final MapColor mapColor;
      private final int meta;
      private final String name;
      private final String unlocalizedName;

      public MapColor getMapColor() {
         return this.mapColor;
      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      public int getMetadata() {
         return this.meta;
      }

      static {
         BlockSand.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockSand.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      private EnumType(int var3, String var4, String var5, MapColor var6) {
         this.meta = var3;
         this.name = var4;
         this.mapColor = var6;
         this.unlocalizedName = var5;
      }

      public static BlockSand.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }
   }
}
