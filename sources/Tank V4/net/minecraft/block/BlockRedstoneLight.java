package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneLight extends Block {
   private final boolean isOn;

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.isRemote) {
         if (this.isOn && !var1.isBlockPowered(var2)) {
            var1.scheduleUpdate(var2, this, 4);
         } else if (!this.isOn && var1.isBlockPowered(var2)) {
            var1.setBlockState(var2, Blocks.lit_redstone_lamp.getDefaultState(), 2);
         }
      }

   }

   public BlockRedstoneLight(boolean var1) {
      super(Material.redstoneLight);
      this.isOn = var1;
      if (var1) {
         this.setLightLevel(1.0F);
      }

   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         if (this.isOn && !var1.isBlockPowered(var2)) {
            var1.setBlockState(var2, Blocks.redstone_lamp.getDefaultState(), 2);
         } else if (!this.isOn && var1.isBlockPowered(var2)) {
            var1.setBlockState(var2, Blocks.lit_redstone_lamp.getDefaultState(), 2);
         }
      }

   }

   public Item getItem(World var1, BlockPos var2) {
      return Item.getItemFromBlock(Blocks.redstone_lamp);
   }

   protected ItemStack createStackedBlock(IBlockState var1) {
      return new ItemStack(Blocks.redstone_lamp);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote && this.isOn && !var1.isBlockPowered(var2)) {
         var1.setBlockState(var2, Blocks.redstone_lamp.getDefaultState(), 2);
      }

   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.redstone_lamp);
   }
}
