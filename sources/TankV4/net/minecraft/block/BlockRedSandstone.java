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

public class BlockRedSandstone extends Block {
   public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockRedSandstone.EnumType.class);

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockRedSandstone.EnumType[] var7;
      int var6 = (var7 = BlockRedSandstone.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockRedSandstone.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public int damageDropped(IBlockState var1) {
      return ((BlockRedSandstone.EnumType)var1.getValue(TYPE)).getMetadata();
   }

   public BlockRedSandstone() {
      super(Material.rock, BlockSand.EnumType.RED_SAND.getMapColor());
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockRedSandstone.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE});
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(TYPE, BlockRedSandstone.EnumType.byMetadata(var1));
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockRedSandstone.EnumType)var1.getValue(TYPE)).getMetadata();
   }

   public static enum EnumType implements IStringSerializable {
      SMOOTH(2, "smooth_red_sandstone", "smooth");

      private static final BlockRedSandstone.EnumType[] META_LOOKUP = new BlockRedSandstone.EnumType[values().length];
      DEFAULT(0, "red_sandstone", "default");

      private final String unlocalizedName;
      private final String name;
      CHISELED(1, "chiseled_red_sandstone", "chiseled");

      private static final BlockRedSandstone.EnumType[] ENUM$VALUES = new BlockRedSandstone.EnumType[]{DEFAULT, CHISELED, SMOOTH};
      private final int meta;

      private EnumType(int var3, String var4, String var5) {
         this.meta = var3;
         this.name = var4;
         this.unlocalizedName = var5;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      static {
         BlockRedSandstone.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockRedSandstone.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      public static BlockRedSandstone.EnumType byMetadata(int var0) {
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
