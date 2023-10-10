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

public class BlockColored extends Block {
   public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);

   public int getMetaFromState(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMetadata();
   }

   public MapColor getMapColor(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMapColor();
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(var1));
   }

   public int damageDropped(IBlockState var1) {
      return ((EnumDyeColor)var1.getValue(COLOR)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{COLOR});
   }

   public BlockColored(Material var1) {
      super(var1);
      this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
      this.setCreativeTab(CreativeTabs.tabBlock);
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
