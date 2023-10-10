package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block {
   public static boolean fallInstantly;

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      var1.scheduleUpdate(var2, this, this.tickRate(var1));
   }

   private void checkFallable(World var1, BlockPos var2) {
      if (var2.down() != false && var2.getY() >= 0) {
         byte var3 = 32;
         if (!fallInstantly && var1.isAreaLoaded(var2.add(-var3, -var3, -var3), var2.add(var3, var3, var3))) {
            if (!var1.isRemote) {
               EntityFallingBlock var5 = new EntityFallingBlock(var1, (double)var2.getX() + 0.5D, (double)var2.getY(), (double)var2.getZ() + 0.5D, var1.getBlockState(var2));
               this.onStartFalling(var5);
               var1.spawnEntityInWorld(var5);
            }
         } else {
            var1.setBlockToAir(var2);

            BlockPos var4;
            for(var4 = var2.down(); var4 != false && var4.getY() > 0; var4 = var4.down()) {
            }

            if (var4.getY() > 0) {
               var1.setBlockState(var4.up(), this.getDefaultState());
            }
         }
      }

   }

   public int tickRate(World var1) {
      return 2;
   }

   public BlockFalling(Material var1) {
      super(var1);
   }

   public BlockFalling() {
      super(Material.sand);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   protected void onStartFalling(EntityFallingBlock var1) {
   }

   public void onEndFalling(World var1, BlockPos var2) {
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      var1.scheduleUpdate(var2, this, this.tickRate(var1));
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote) {
         this.checkFallable(var1, var2);
      }

   }
}
