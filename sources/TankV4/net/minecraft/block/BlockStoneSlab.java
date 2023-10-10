package net.minecraft.block;

import java.util.List;
import java.util.Random;
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
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockStoneSlab.EnumType.class);
   public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      if (var1 != Item.getItemFromBlock(Blocks.double_stone_slab)) {
         BlockStoneSlab.EnumType[] var7;
         int var6 = (var7 = BlockStoneSlab.EnumType.values()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            BlockStoneSlab.EnumType var4 = var7[var5];
            if (var4 != BlockStoneSlab.EnumType.WOOD) {
               var3.add(new ItemStack(var1, 1, var4.getMetadata()));
            }
         }
      }

   }

   public Item getItem(World var1, BlockPos var2) {
      return Item.getItemFromBlock(Blocks.stone_slab);
   }

   public BlockStoneSlab() {
      super(Material.rock);
      IBlockState var1 = this.blockState.getBaseState();
      if (this.isDouble()) {
         var1 = var1.withProperty(SEAMLESS, false);
      } else {
         var1 = var1.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      }

      this.setDefaultState(var1.withProperty(VARIANT, BlockStoneSlab.EnumType.STONE));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public IProperty getVariantProperty() {
      return VARIANT;
   }

   public IBlockState getStateFromMeta(int var1) {
      IBlockState var2 = this.getDefaultState().withProperty(VARIANT, BlockStoneSlab.EnumType.byMetadata(var1 & 7));
      if (this.isDouble()) {
         var2 = var2.withProperty(SEAMLESS, (var1 & 8) != 0);
      } else {
         var2 = var2.withProperty(HALF, (var1 & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
      }

      return var2;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.stone_slab);
   }

   public MapColor getMapColor(IBlockState var1) {
      return ((BlockStoneSlab.EnumType)var1.getValue(VARIANT)).func_181074_c();
   }

   public Object getVariant(ItemStack var1) {
      return BlockStoneSlab.EnumType.byMetadata(var1.getMetadata() & 7);
   }

   public String getUnlocalizedName(int var1) {
      return super.getUnlocalizedName() + "." + BlockStoneSlab.EnumType.byMetadata(var1).getUnlocalizedName();
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockStoneSlab.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockStoneSlab.EnumType)var1.getValue(VARIANT)).getMetadata();
      if (this.isDouble()) {
         if ((Boolean)var1.getValue(SEAMLESS)) {
            var3 |= 8;
         }
      } else if (var1.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockState createBlockState() {
      return this.isDouble() ? new BlockState(this, new IProperty[]{SEAMLESS, VARIANT}) : new BlockState(this, new IProperty[]{HALF, VARIANT});
   }

   public static enum EnumType implements IStringSerializable {
      private final String name;
      private final int meta;
      COBBLESTONE(3, MapColor.stoneColor, "cobblestone", "cobble");

      private final String unlocalizedName;
      STONE(0, MapColor.stoneColor, "stone"),
      SAND(1, MapColor.sandColor, "sandstone", "sand");

      private final MapColor field_181075_k;
      BRICK(4, MapColor.redColor, "brick"),
      QUARTZ(7, MapColor.quartzColor, "quartz"),
      SMOOTHBRICK(5, MapColor.stoneColor, "stone_brick", "smoothStoneBrick"),
      NETHERBRICK(6, MapColor.netherrackColor, "nether_brick", "netherBrick");

      private static final BlockStoneSlab.EnumType[] ENUM$VALUES = new BlockStoneSlab.EnumType[]{STONE, SAND, WOOD, COBBLESTONE, BRICK, SMOOTHBRICK, NETHERBRICK, QUARTZ};
      WOOD(2, MapColor.woodColor, "wood_old", "wood");

      private static final BlockStoneSlab.EnumType[] META_LOOKUP = new BlockStoneSlab.EnumType[values().length];

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getName() {
         return this.name;
      }

      public static BlockStoneSlab.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public String toString() {
         return this.name;
      }

      private EnumType(int var3, MapColor var4, String var5) {
         this(var3, var4, var5, var5);
      }

      static {
         BlockStoneSlab.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockStoneSlab.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      private EnumType(int var3, MapColor var4, String var5, String var6) {
         this.meta = var3;
         this.field_181075_k = var4;
         this.name = var5;
         this.unlocalizedName = var6;
      }

      public MapColor func_181074_c() {
         return this.field_181075_k;
      }
   }
}
