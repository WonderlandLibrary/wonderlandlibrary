package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemDye extends Item {
   public static final int[] dyeColors = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

   public String getUnlocalizedName(ItemStack var1) {
      int var2 = var1.getMetadata();
      return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(var2).getUnlocalizedName();
   }

   public boolean itemInteractionForEntity(ItemStack var1, EntityPlayer var2, EntityLivingBase var3) {
      if (var3 instanceof EntitySheep) {
         EntitySheep var4 = (EntitySheep)var3;
         EnumDyeColor var5 = EnumDyeColor.byDyeDamage(var1.getMetadata());
         if (!var4.getSheared() && var4.getFleeceColor() != var5) {
            var4.setFleeceColor(var5);
            --var1.stackSize;
         }

         return true;
      } else {
         return false;
      }
   }

   public static void spawnBonemealParticles(World var0, BlockPos var1, int var2) {
      if (var2 == 0) {
         var2 = 15;
      }

      Block var3 = var0.getBlockState(var1).getBlock();
      if (var3.getMaterial() != Material.air) {
         var3.setBlockBoundsBasedOnState(var0, var1);

         for(int var4 = 0; var4 < var2; ++var4) {
            double var5 = itemRand.nextGaussian() * 0.02D;
            double var7 = itemRand.nextGaussian() * 0.02D;
            double var9 = itemRand.nextGaussian() * 0.02D;
            var0.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double)((float)var1.getX() + itemRand.nextFloat()), (double)var1.getY() + (double)itemRand.nextFloat() * var3.getBlockBoundsMaxY(), (double)((float)var1.getZ() + itemRand.nextFloat()), var5, var7, var9);
         }
      }

   }

   public void getSubItems(Item var1, CreativeTabs var2, List var3) {
      for(int var4 = 0; var4 < 16; ++var4) {
         var3.add(new ItemStack(var1, 1, var4));
      }

   }

   public ItemDye() {
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(CreativeTabs.tabMaterials);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (!var2.canPlayerEdit(var4.offset(var5), var5, var1)) {
         return false;
      } else {
         EnumDyeColor var9 = EnumDyeColor.byDyeDamage(var1.getMetadata());
         if (var9 == EnumDyeColor.WHITE) {
            if (var4 != false) {
               if (!var3.isRemote) {
                  var3.playAuxSFX(2005, var4, 0);
               }

               return true;
            }
         } else if (var9 == EnumDyeColor.BROWN) {
            IBlockState var10 = var3.getBlockState(var4);
            Block var11 = var10.getBlock();
            if (var11 == Blocks.log && var10.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
               if (var5 == EnumFacing.DOWN) {
                  return false;
               }

               if (var5 == EnumFacing.UP) {
                  return false;
               }

               var4 = var4.offset(var5);
               if (var3.isAirBlock(var4)) {
                  IBlockState var12 = Blocks.cocoa.onBlockPlaced(var3, var4, var5, var6, var7, var8, 0, var2);
                  var3.setBlockState(var4, var12, 2);
                  if (!var2.capabilities.isCreativeMode) {
                     --var1.stackSize;
                  }
               }

               return true;
            }
         }

         return false;
      }
   }
}
