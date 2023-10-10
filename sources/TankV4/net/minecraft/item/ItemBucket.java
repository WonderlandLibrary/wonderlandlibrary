package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemBucket extends Item {
   private Block isFull;

   private ItemStack fillBucket(ItemStack var1, EntityPlayer var2, Item var3) {
      if (var2.capabilities.isCreativeMode) {
         return var1;
      } else if (--var1.stackSize <= 0) {
         return new ItemStack(var3);
      } else {
         if (!var2.inventory.addItemStackToInventory(new ItemStack(var3))) {
            var2.dropPlayerItemWithRandomChoice(new ItemStack(var3, 1, 0), false);
         }

         return var1;
      }
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      boolean var4 = this.isFull == Blocks.air;
      MovingObjectPosition var5 = this.getMovingObjectPositionFromPlayer(var2, var3, var4);
      if (var5 == null) {
         return var1;
      } else {
         if (var5.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var6 = var5.getBlockPos();
            if (!var2.isBlockModifiable(var3, var6)) {
               return var1;
            }

            if (var4) {
               if (!var3.canPlayerEdit(var6.offset(var5.sideHit), var5.sideHit, var1)) {
                  return var1;
               }

               IBlockState var7 = var2.getBlockState(var6);
               Material var8 = var7.getBlock().getMaterial();
               if (var8 == Material.water && (Integer)var7.getValue(BlockLiquid.LEVEL) == 0) {
                  var2.setBlockToAir(var6);
                  var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                  return this.fillBucket(var1, var3, Items.water_bucket);
               }

               if (var8 == Material.lava && (Integer)var7.getValue(BlockLiquid.LEVEL) == 0) {
                  var2.setBlockToAir(var6);
                  var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                  return this.fillBucket(var1, var3, Items.lava_bucket);
               }
            } else {
               if (this.isFull == Blocks.air) {
                  return new ItemStack(Items.bucket);
               }

               BlockPos var9 = var6.offset(var5.sideHit);
               if (!var3.canPlayerEdit(var9, var5.sideHit, var1)) {
                  return var1;
               }

               if (var2 == var9 && !var3.capabilities.isCreativeMode) {
                  var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                  return new ItemStack(Items.bucket);
               }
            }
         }

         return var1;
      }
   }

   public ItemBucket(Block var1) {
      this.maxStackSize = 1;
      this.isFull = var1;
      this.setCreativeTab(CreativeTabs.tabMisc);
   }
}
