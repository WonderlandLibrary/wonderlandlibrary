package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockStoneBrick extends Block {
   public static final int CRACKED_META;
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockStoneBrick.EnumType.class);
   public static final int DEFAULT_META;
   public static final int CHISELED_META;
   public static final int MOSSY_META;

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockStoneBrick.EnumType[] var7;
      int var6 = (var7 = BlockStoneBrick.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockStoneBrick.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockStoneBrick.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   static {
      DEFAULT_META = BlockStoneBrick.EnumType.DEFAULT.getMetadata();
      MOSSY_META = BlockStoneBrick.EnumType.MOSSY.getMetadata();
      CRACKED_META = BlockStoneBrick.EnumType.CRACKED.getMetadata();
      CHISELED_META = BlockStoneBrick.EnumType.CHISELED.getMetadata();
   }

   public BlockStoneBrick() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.byMetadata(var1));
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockStoneBrick.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public static enum EnumType implements IStringSerializable {
      CRACKED(2, "cracked_stonebrick", "cracked");

      private static final BlockStoneBrick.EnumType[] ENUM$VALUES = new BlockStoneBrick.EnumType[]{DEFAULT, MOSSY, CRACKED, CHISELED};
      CHISELED(3, "chiseled_stonebrick", "chiseled"),
      MOSSY(1, "mossy_stonebrick", "mossy");

      private static final BlockStoneBrick.EnumType[] META_LOOKUP = new BlockStoneBrick.EnumType[values().length];
      DEFAULT(0, "stonebrick", "default");

      private final String name;
      private final int meta;
      private final String unlocalizedName;

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      static {
         BlockStoneBrick.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockStoneBrick.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      public int getMetadata() {
         return this.meta;
      }

      public static BlockStoneBrick.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public String getName() {
         return this.name;
      }

      private EnumType(int var3, String var4, String var5) {
         this.meta = var3;
         this.name = var4;
         this.unlocalizedName = var5;
      }

      public String toString() {
         return this.name;
      }
   }
}
