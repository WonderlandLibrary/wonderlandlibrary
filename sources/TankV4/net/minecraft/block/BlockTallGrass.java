package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {
   public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockTallGrass.EnumType.class);

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      for(int var4 = 1; var4 < 3; ++var4) {
         var3.add(new ItemStack(var1, 1, var4));
      }

   }

   protected BlockTallGrass() {
      super(Material.vine);
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
      float var1 = 0.4F;
      this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
   }

   public int getBlockColor() {
      return ColorizerGrass.getGrassColor(0.5D, 1.0D);
   }

   public int getRenderColor(IBlockState var1) {
      if (var1.getBlock() != this) {
         return super.getRenderColor(var1);
      } else {
         BlockTallGrass.EnumType var2 = (BlockTallGrass.EnumType)var1.getValue(TYPE);
         return var2 == BlockTallGrass.EnumType.DEAD_BUSH ? 16777215 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(TYPE, BlockTallGrass.EnumType.byMetadata(var1));
   }

   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, TileEntity var5) {
      if (!var1.isRemote && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().getItem() == Items.shears) {
         var2.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
         spawnAsEntity(var1, var3, new ItemStack(Blocks.tallgrass, 1, ((BlockTallGrass.EnumType)var4.getValue(TYPE)).getMeta()));
      } else {
         super.harvestBlock(var1, var2, var3, var4, var5);
      }

   }

   public boolean isReplaceable(World var1, BlockPos var2) {
      return true;
   }

   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      return this.canPlaceBlockOn(var1.getBlockState(var2.down()).getBlock());
   }

   public int colorMultiplier(IBlockAccess var1, BlockPos var2, int var3) {
      return var1.getBiomeGenForCoords(var2).getGrassColorAtPos(var2);
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return var2.nextInt(8) == 0 ? Items.wheat_seeds : null;
   }

   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return var3.getValue(TYPE) != BlockTallGrass.EnumType.DEAD_BUSH;
   }

   public int quantityDroppedWithBonus(int var1, Random var2) {
      return 1 + var2.nextInt(var1 * 2 + 1);
   }

   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.XYZ;
   }

   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      BlockDoublePlant.EnumPlantType var5 = BlockDoublePlant.EnumPlantType.GRASS;
      if (var4.getValue(TYPE) == BlockTallGrass.EnumType.FERN) {
         var5 = BlockDoublePlant.EnumPlantType.FERN;
      }

      if (Blocks.double_plant.canPlaceBlockAt(var1, var3)) {
         Blocks.double_plant.placeAt(var1, var3, var5, 2);
      }

   }

   public int getDamageValue(World var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      return var3.getBlock().getMetaFromState(var3);
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockTallGrass.EnumType)var1.getValue(TYPE)).getMeta();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{TYPE});
   }

   public static enum EnumType implements IStringSerializable {
      GRASS(1, "tall_grass");

      private final String name;
      private final int meta;
      private static final BlockTallGrass.EnumType[] META_LOOKUP = new BlockTallGrass.EnumType[values().length];
      private static final BlockTallGrass.EnumType[] ENUM$VALUES = new BlockTallGrass.EnumType[]{DEAD_BUSH, GRASS, FERN};
      DEAD_BUSH(0, "dead_bush"),
      FERN(2, "fern");

      public static BlockTallGrass.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      static {
         BlockTallGrass.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockTallGrass.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMeta()] = var0;
         }

      }

      private EnumType(int var3, String var4) {
         this.meta = var3;
         this.name = var4;
      }

      public int getMeta() {
         return this.meta;
      }
   }
}
