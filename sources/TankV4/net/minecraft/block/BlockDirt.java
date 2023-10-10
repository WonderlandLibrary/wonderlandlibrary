package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block {
   public static final PropertyBool SNOWY = PropertyBool.create("snowy");
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockDirt.DirtType.class);

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.getMetadata()));
      var3.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
      var3.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.getMetadata()));
   }

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (var1.getValue(VARIANT) == BlockDirt.DirtType.PODZOL) {
         Block var4 = var2.getBlockState(var3.up()).getBlock();
         var1 = var1.withProperty(SNOWY, var4 == Blocks.snow || var4 == Blocks.snow_layer);
      }

      return var1;
   }

   public int getDamageValue(World var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      return var3.getBlock() != this ? 0 : ((BlockDirt.DirtType)var3.getValue(VARIANT)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT, SNOWY});
   }

   public int damageDropped(IBlockState var1) {
      BlockDirt.DirtType var2 = (BlockDirt.DirtType)var1.getValue(VARIANT);
      if (var2 == BlockDirt.DirtType.PODZOL) {
         var2 = BlockDirt.DirtType.DIRT;
      }

      return var2.getMetadata();
   }

   public MapColor getMapColor(IBlockState var1) {
      return ((BlockDirt.DirtType)var1.getValue(VARIANT)).func_181066_d();
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockDirt.DirtType.byMetadata(var1));
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockDirt.DirtType)var1.getValue(VARIANT)).getMetadata();
   }

   protected BlockDirt() {
      super(Material.ground);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockDirt.DirtType.DIRT).withProperty(SNOWY, false));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public static enum DirtType implements IStringSerializable {
      COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.dirtColor),
      DIRT(0, "dirt", "default", MapColor.dirtColor);

      private static final BlockDirt.DirtType[] ENUM$VALUES = new BlockDirt.DirtType[]{DIRT, COARSE_DIRT, PODZOL};
      private final String name;
      private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[values().length];
      private final int metadata;
      PODZOL(2, "podzol", MapColor.obsidianColor);

      private final String unlocalizedName;
      private final MapColor field_181067_h;

      public MapColor func_181066_d() {
         return this.field_181067_h;
      }

      public static BlockDirt.DirtType byMetadata(int var0) {
         if (var0 < 0 || var0 >= METADATA_LOOKUP.length) {
            var0 = 0;
         }

         return METADATA_LOOKUP[var0];
      }

      public int getMetadata() {
         return this.metadata;
      }

      public String toString() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      private DirtType(int var3, String var4, String var5, MapColor var6) {
         this.metadata = var3;
         this.name = var4;
         this.unlocalizedName = var5;
         this.field_181067_h = var6;
      }

      public String getName() {
         return this.name;
      }

      static {
         BlockDirt.DirtType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockDirt.DirtType var0 = var3[var1];
            METADATA_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      private DirtType(int var3, String var4, MapColor var5) {
         this(var3, var4, var4, var5);
      }
   }
}
