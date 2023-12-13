package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;

public class BlockStone extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockStone.EnumType.class);

   public MapColor getMapColor(IBlockState var1) {
      return ((BlockStone.EnumType)var1.getValue(VARIANT)).func_181072_c();
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockStone.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockStone.EnumType.byMetadata(var1));
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockStone.EnumType[] var7;
      int var6 = (var7 = BlockStone.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockStone.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public int damageDropped(IBlockState var1) {
      return ((BlockStone.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + "." + BlockStone.EnumType.STONE.getUnlocalizedName() + ".name");
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return var1.getValue(VARIANT) == BlockStone.EnumType.STONE ? Item.getItemFromBlock(Blocks.cobblestone) : Item.getItemFromBlock(Blocks.stone);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public BlockStone() {
      super(Material.rock);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockStone.EnumType.STONE));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public static enum EnumType implements IStringSerializable {
      STONE(0, MapColor.stoneColor, "stone"),
      ANDESITE(5, MapColor.stoneColor, "andesite");

      private final int meta;
      private final String unlocalizedName;
      ANDESITE_SMOOTH(6, MapColor.stoneColor, "smooth_andesite", "andesiteSmooth");

      private static final BlockStone.EnumType[] META_LOOKUP = new BlockStone.EnumType[values().length];
      GRANITE_SMOOTH(2, MapColor.dirtColor, "smooth_granite", "graniteSmooth"),
      GRANITE(1, MapColor.dirtColor, "granite");

      private final MapColor field_181073_l;
      DIORITE_SMOOTH(4, MapColor.quartzColor, "smooth_diorite", "dioriteSmooth");

      private static final BlockStone.EnumType[] ENUM$VALUES = new BlockStone.EnumType[]{STONE, GRANITE, GRANITE_SMOOTH, DIORITE, DIORITE_SMOOTH, ANDESITE, ANDESITE_SMOOTH};
      DIORITE(3, MapColor.quartzColor, "diorite");

      private final String name;

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      private EnumType(int var3, MapColor var4, String var5) {
         this(var3, var4, var5, var5);
      }

      public static BlockStone.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public MapColor func_181072_c() {
         return this.field_181073_l;
      }

      static {
         BlockStone.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockStone.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      private EnumType(int var3, MapColor var4, String var5, String var6) {
         this.meta = var3;
         this.name = var5;
         this.unlocalizedName = var6;
         this.field_181073_l = var4;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }
   }
}
