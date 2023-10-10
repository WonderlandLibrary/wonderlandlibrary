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

public class BlockPlanks extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(var1));
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockPlanks.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockPlanks.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public BlockPlanks() {
      super(Material.wood);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockPlanks.EnumType[] var7;
      int var6 = (var7 = BlockPlanks.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockPlanks.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public MapColor getMapColor(IBlockState var1) {
      return ((BlockPlanks.EnumType)var1.getValue(VARIANT)).func_181070_c();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public static enum EnumType implements IStringSerializable {
      SPRUCE(1, "spruce", MapColor.obsidianColor);

      private final int meta;
      BIRCH(2, "birch", MapColor.sandColor);

      private final String unlocalizedName;
      private static final BlockPlanks.EnumType[] ENUM$VALUES = new BlockPlanks.EnumType[]{OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK};
      DARK_OAK(5, "dark_oak", "big_oak", MapColor.brownColor),
      JUNGLE(3, "jungle", MapColor.dirtColor),
      OAK(0, "oak", MapColor.woodColor);

      private final String name;
      private static final BlockPlanks.EnumType[] META_LOOKUP = new BlockPlanks.EnumType[values().length];
      private final MapColor field_181071_k;
      ACACIA(4, "acacia", MapColor.adobeColor);

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      static {
         BlockPlanks.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockPlanks.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      public String getName() {
         return this.name;
      }

      public int getMetadata() {
         return this.meta;
      }

      public MapColor func_181070_c() {
         return this.field_181071_k;
      }

      public static BlockPlanks.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public String toString() {
         return this.name;
      }

      private EnumType(int var3, String var4, String var5, MapColor var6) {
         this.meta = var3;
         this.name = var4;
         this.unlocalizedName = var5;
         this.field_181071_k = var6;
      }

      private EnumType(int var3, String var4, MapColor var5) {
         this(var3, var4, var4, var5);
      }
   }
}
