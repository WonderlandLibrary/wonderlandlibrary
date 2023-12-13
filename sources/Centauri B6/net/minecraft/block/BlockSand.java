package net.minecraft.block;

import java.util.List;
import net.minecraft.block.BlockFalling;
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

   public BlockSand() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSand.EnumType.SAND));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockSand.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public int damageDropped(IBlockState state) {
      return ((BlockSand.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockSand.EnumType.byMetadata(meta));
   }

   public MapColor getMapColor(IBlockState state) {
      return ((BlockSand.EnumType)state.getValue(VARIANT)).getMapColor();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      for(BlockSand.EnumType blocksand$enumtype : BlockSand.EnumType.values()) {
         list.add(new ItemStack(itemIn, 1, blocksand$enumtype.getMetadata()));
      }

   }

   public static enum EnumType implements IStringSerializable {
      SAND(0, "sand", "default", MapColor.sandColor),
      RED_SAND(1, "red_sand", "red", MapColor.adobeColor);

      private static final BlockSand.EnumType[] META_LOOKUP = new BlockSand.EnumType[values().length];
      private final int meta;
      private final String name;
      private final MapColor mapColor;
      private final String unlocalizedName;

      private EnumType(int meta, String name, String unlocalizedName, MapColor mapColor) {
         this.meta = meta;
         this.name = name;
         this.mapColor = mapColor;
         this.unlocalizedName = unlocalizedName;
      }

      static {
         for(BlockSand.EnumType blocksand$enumtype : values()) {
            META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
         }

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

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public MapColor getMapColor() {
         return this.mapColor;
      }

      public static BlockSand.EnumType byMetadata(int meta) {
         if(meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }
   }
}
