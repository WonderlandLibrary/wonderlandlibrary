package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPotato extends BlockCrops {
   protected Item getCrop() {
      return Items.potato;
   }

   protected Item getSeed() {
      return Items.potato;
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5);
      if (!var1.isRemote && (Integer)var3.getValue(AGE) >= 7 && var1.rand.nextInt(50) == 0) {
         spawnAsEntity(var1, var2, new ItemStack(Items.poisonous_potato));
      }

   }
}
