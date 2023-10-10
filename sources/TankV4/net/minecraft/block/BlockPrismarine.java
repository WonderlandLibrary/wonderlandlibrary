package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;

public class BlockPrismarine extends Block {
   public static final int ROUGH_META;
   public static final int DARK_META;
   public static final int BRICKS_META;
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPrismarine.EnumType.class);

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, ROUGH_META));
      var3.add(new ItemStack(var1, 1, BRICKS_META));
      var3.add(new ItemStack(var1, 1, DARK_META));
   }

   static {
      ROUGH_META = BlockPrismarine.EnumType.ROUGH.getMetadata();
      BRICKS_META = BlockPrismarine.EnumType.BRICKS.getMetadata();
      DARK_META = BlockPrismarine.EnumType.DARK.getMetadata();
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockPrismarine.EnumType.byMetadata(var1));
   }

   public MapColor getMapColor(IBlockState var1) {
      return var1.getValue(VARIANT) == BlockPrismarine.EnumType.ROUGH ? MapColor.cyanColor : MapColor.diamondColor;
   }

   public BlockPrismarine() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPrismarine.EnumType.ROUGH));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockPrismarine.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + "." + BlockPrismarine.EnumType.ROUGH.getUnlocalizedName() + ".name");
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockPrismarine.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public static enum EnumType implements IStringSerializable {
      ROUGH(0, "prismarine", "rough");

      private final String unlocalizedName;
      DARK(2, "dark_prismarine", "dark");

      private final int meta;
      BRICKS(1, "prismarine_bricks", "bricks");

      private static final BlockPrismarine.EnumType[] META_LOOKUP = new BlockPrismarine.EnumType[values().length];
      private static final BlockPrismarine.EnumType[] ENUM$VALUES = new BlockPrismarine.EnumType[]{ROUGH, BRICKS, DARK};
      private final String name;

      static {
         BlockPrismarine.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockPrismarine.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      public String toString() {
         return this.name;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public String getName() {
         return this.name;
      }

      private EnumType(int var3, String var4, String var5) {
         this.meta = var3;
         this.name = var4;
         this.unlocalizedName = var5;
      }

      public static BlockPrismarine.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }
   }
}
