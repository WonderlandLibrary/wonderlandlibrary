package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable {
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (var1.getLightFor(EnumSkyBlock.BLOCK, var2) > 11 - this.getLightOpacity()) {
         if (var1.provider.doesWaterVaporize()) {
            var1.setBlockToAir(var2);
         } else {
            this.dropBlockAsItem(var1, var2, var1.getBlockState(var2), 0);
            var1.setBlockState(var2, Blocks.water.getDefaultState());
         }
      }

   }

   public int quantityDropped(Random var1) {
      return 0;
   }

   public int getMobilityFlag() {
      return 0;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.TRANSLUCENT;
   }

   public BlockIce() {
      super(Material.ice, false);
      this.slipperiness = 0.98F;
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, TileEntity var5) {
      var2.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
      var2.addExhaustion(0.025F);
      if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(var2)) {
         ItemStack var8 = this.createStackedBlock(var4);
         if (var8 != null) {
            spawnAsEntity(var1, var3, var8);
         }
      } else {
         if (var1.provider.doesWaterVaporize()) {
            var1.setBlockToAir(var3);
            return;
         }

         int var6 = EnchantmentHelper.getFortuneModifier(var2);
         this.dropBlockAsItem(var1, var3, var4, var6);
         Material var7 = var1.getBlockState(var3.down()).getBlock().getMaterial();
         if (var7.blocksMovement() || var7.isLiquid()) {
            var1.setBlockState(var3, Blocks.flowing_water.getDefaultState());
         }
      }

   }
}
