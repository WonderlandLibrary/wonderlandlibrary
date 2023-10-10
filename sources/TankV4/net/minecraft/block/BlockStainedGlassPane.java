package net.minecraft.block;

import java.util.List;
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

public class BlockStainedGlassPane extends BlockPane {
   public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

   public MapColor getMapColor(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMapColor();
   }

   public int getMetaFromState(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMetadata();
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         BlockBeacon.updateColorAsync(var1, var2);
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.TRANSLUCENT;
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         BlockBeacon.updateColorAsync(var1, var2);
      }

   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(var1));
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      for(int var4 = 0; var4 < EnumDyeColor.values().length; ++var4) {
         var3.add(new ItemStack(var1, 1, var4));
      }

   }

   public int damageDropped(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{NORTH, EAST, WEST, SOUTH, COLOR});
   }

   public BlockStainedGlassPane() {
      super(Material.glass, false);
      this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(COLOR, EnumDyeColor.WHITE));
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }
}
