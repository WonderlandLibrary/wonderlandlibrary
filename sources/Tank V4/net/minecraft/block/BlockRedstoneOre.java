package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockRedstoneOre extends Block {
   private final boolean isOn;

   protected ItemStack createStackedBlock(IBlockState var1) {
      return new ItemStack(Blocks.redstone_ore);
   }

   public BlockRedstoneOre(boolean var1) {
      super(Material.rock);
      if (var1) {
         this.setTickRandomly(true);
      }

      this.isOn = var1;
   }

   public int quantityDropped(Random var1) {
      return 4 + var1.nextInt(2);
   }

   public int tickRate(World var1) {
      return 30;
   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, Entity var3) {
      this.activate(var1, var2);
      super.onEntityCollidedWithBlock(var1, var2, var3);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this == Blocks.lit_redstone_ore) {
         var1.setBlockState(var2, Blocks.redstone_ore.getDefaultState());
      }

   }

   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
      this.activate(var1, var2);
      super.onBlockClicked(var1, var2, var3);
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      this.activate(var1, var2);
      return super.onBlockActivated(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5);
      if (this.getItemDropped(var3, var1.rand, var5) != Item.getItemFromBlock(this)) {
         int var6 = 1 + var1.rand.nextInt(5);
         this.dropXpOnBlockBreak(var1, var2, var6);
      }

   }

   private void spawnParticles(World var1, BlockPos var2) {
      Random var3 = var1.rand;
      double var4 = 0.0625D;

      for(int var6 = 0; var6 < 6; ++var6) {
         double var7 = (double)((float)var2.getX() + var3.nextFloat());
         double var9 = (double)((float)var2.getY() + var3.nextFloat());
         double var11 = (double)((float)var2.getZ() + var3.nextFloat());
         if (var6 == 0 && !var1.getBlockState(var2.up()).getBlock().isOpaqueCube()) {
            var9 = (double)var2.getY() + var4 + 1.0D;
         }

         if (var6 == 1 && !var1.getBlockState(var2.down()).getBlock().isOpaqueCube()) {
            var9 = (double)var2.getY() - var4;
         }

         if (var6 == 2 && !var1.getBlockState(var2.south()).getBlock().isOpaqueCube()) {
            var11 = (double)var2.getZ() + var4 + 1.0D;
         }

         if (var6 == 3 && !var1.getBlockState(var2.north()).getBlock().isOpaqueCube()) {
            var11 = (double)var2.getZ() - var4;
         }

         if (var6 == 4 && !var1.getBlockState(var2.east()).getBlock().isOpaqueCube()) {
            var7 = (double)var2.getX() + var4 + 1.0D;
         }

         if (var6 == 5 && !var1.getBlockState(var2.west()).getBlock().isOpaqueCube()) {
            var7 = (double)var2.getX() - var4;
         }

         if (var7 < (double)var2.getX() || var7 > (double)(var2.getX() + 1) || var9 < 0.0D || var9 > (double)(var2.getY() + 1) || var11 < (double)var2.getZ() || var11 > (double)(var2.getZ() + 1)) {
            var1.spawnParticle(EnumParticleTypes.REDSTONE, var7, var9, var11, 0.0D, 0.0D, 0.0D);
         }
      }

   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this.isOn) {
         this.spawnParticles(var1, var2);
      }

   }

   private void activate(World var1, BlockPos var2) {
      this.spawnParticles(var1, var2);
      if (this == Blocks.redstone_ore) {
         var1.setBlockState(var2, Blocks.lit_redstone_ore.getDefaultState());
      }

   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.redstone;
   }

   public int quantityDroppedWithBonus(int var1, Random var2) {
      return this.quantityDropped(var2) + var2.nextInt(var1 + 1);
   }
}
