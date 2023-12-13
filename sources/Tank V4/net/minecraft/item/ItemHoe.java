package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHoe extends Item {
   protected Item.ToolMaterial theToolMaterial;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockDirt$DirtType;

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockDirt$DirtType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockDirt$DirtType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockDirt.DirtType.values().length];

         try {
            var0[BlockDirt.DirtType.COARSE_DIRT.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockDirt.DirtType.DIRT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockDirt.DirtType.PODZOL.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockDirt$DirtType = var0;
         return var0;
      }
   }

   public ItemHoe(Item.ToolMaterial var1) {
      this.theToolMaterial = var1;
      this.maxStackSize = 1;
      this.setMaxDamage(var1.getMaxUses());
      this.setCreativeTab(CreativeTabs.tabTools);
   }

   public String getMaterialName() {
      return this.theToolMaterial.toString();
   }

   protected boolean useHoe(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, IBlockState var5) {
      var3.playSoundEffect((double)((float)var4.getX() + 0.5F), (double)((float)var4.getY() + 0.5F), (double)((float)var4.getZ() + 0.5F), var5.getBlock().stepSound.getStepSound(), (var5.getBlock().stepSound.getVolume() + 1.0F) / 2.0F, var5.getBlock().stepSound.getFrequency() * 0.8F);
      if (var3.isRemote) {
         return true;
      } else {
         var3.setBlockState(var4, var5);
         var1.damageItem(1, var2);
         return true;
      }
   }

   public boolean isFull3D() {
      return true;
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (!var2.canPlayerEdit(var4.offset(var5), var5, var1)) {
         return false;
      } else {
         IBlockState var9 = var3.getBlockState(var4);
         Block var10 = var9.getBlock();
         if (var5 != EnumFacing.DOWN && var3.getBlockState(var4.up()).getBlock().getMaterial() == Material.air) {
            if (var10 == Blocks.grass) {
               return this.useHoe(var1, var2, var3, var4, Blocks.farmland.getDefaultState());
            }

            if (var10 == Blocks.dirt) {
               switch($SWITCH_TABLE$net$minecraft$block$BlockDirt$DirtType()[((BlockDirt.DirtType)var9.getValue(BlockDirt.VARIANT)).ordinal()]) {
               case 1:
                  return this.useHoe(var1, var2, var3, var4, Blocks.farmland.getDefaultState());
               case 2:
                  return this.useHoe(var1, var2, var3, var4, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
               }
            }
         }

         return false;
      }
   }
}
