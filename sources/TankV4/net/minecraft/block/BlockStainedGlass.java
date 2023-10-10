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
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockStainedGlass extends BlockBreakable {
   public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         BlockBeacon.updateColorAsync(var1, var2);
      }

   }

   public int getMetaFromState(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMetadata();
   }

   public int quantityDropped(Random var1) {
      return 0;
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         BlockBeacon.updateColorAsync(var1, var2);
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{COLOR});
   }

   public boolean isFullCube() {
      return false;
   }

   protected boolean canSilkHarvest() {
      return true;
   }

   public BlockStainedGlass(Material var1) {
      super(var1, false);
      this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public MapColor getMapColor(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMapColor();
   }

   public int damageDropped(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMetadata();
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.TRANSLUCENT;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(var1));
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      EnumDyeColor[] var7;
      int var6 = (var7 = EnumDyeColor.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         EnumDyeColor var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }
}
