package net.minecraft.item;

import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemEnderEye extends Item {
   public ItemEnderEye() {
      this.setCreativeTab(CreativeTabs.tabMisc);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState var9 = var3.getBlockState(var4);
      if (var2.canPlayerEdit(var4.offset(var5), var5, var1) && var9.getBlock() == Blocks.end_portal_frame && !(Boolean)var9.getValue(BlockEndPortalFrame.EYE)) {
         if (var3.isRemote) {
            return true;
         } else {
            var3.setBlockState(var4, var9.withProperty(BlockEndPortalFrame.EYE, true), 2);
            var3.updateComparatorOutputLevel(var4, Blocks.end_portal_frame);
            --var1.stackSize;

            for(int var10 = 0; var10 < 16; ++var10) {
               double var11 = (double)((float)var4.getX() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
               double var13 = (double)((float)var4.getY() + 0.8125F);
               double var15 = (double)((float)var4.getZ() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
               double var17 = 0.0D;
               double var19 = 0.0D;
               double var21 = 0.0D;
               var3.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var11, var13, var15, var17, var19, var21);
            }

            EnumFacing var23 = (EnumFacing)var9.getValue(BlockEndPortalFrame.FACING);
            int var24 = 0;
            int var12 = 0;
            boolean var25 = false;
            boolean var14 = true;
            EnumFacing var26 = var23.rotateY();

            for(int var16 = -2; var16 <= 2; ++var16) {
               BlockPos var28 = var4.offset(var26, var16);
               IBlockState var18 = var3.getBlockState(var28);
               if (var18.getBlock() == Blocks.end_portal_frame) {
                  if (!(Boolean)var18.getValue(BlockEndPortalFrame.EYE)) {
                     var14 = false;
                     break;
                  }

                  var12 = var16;
                  if (!var25) {
                     var24 = var16;
                     var25 = true;
                  }
               }
            }

            if (var14 && var12 == var24 + 2) {
               BlockPos var27 = var4.offset(var23, 4);

               int var29;
               for(var29 = var24; var29 <= var12; ++var29) {
                  BlockPos var30 = var27.offset(var26, var29);
                  IBlockState var32 = var3.getBlockState(var30);
                  if (var32.getBlock() != Blocks.end_portal_frame || !(Boolean)var32.getValue(BlockEndPortalFrame.EYE)) {
                     var14 = false;
                     break;
                  }
               }

               int var31;
               BlockPos var33;
               for(var29 = var24 - 1; var29 <= var12 + 1; var29 += 4) {
                  var27 = var4.offset(var26, var29);

                  for(var31 = 1; var31 <= 3; ++var31) {
                     var33 = var27.offset(var23, var31);
                     IBlockState var20 = var3.getBlockState(var33);
                     if (var20.getBlock() != Blocks.end_portal_frame || !(Boolean)var20.getValue(BlockEndPortalFrame.EYE)) {
                        var14 = false;
                        break;
                     }
                  }
               }

               if (var14) {
                  for(var29 = var24; var29 <= var12; ++var29) {
                     var27 = var4.offset(var26, var29);

                     for(var31 = 1; var31 <= 3; ++var31) {
                        var33 = var27.offset(var23, var31);
                        var3.setBlockState(var33, Blocks.end_portal.getDefaultState(), 2);
                     }
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(var2, var3, false);
      if (var4 != null && var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && var2.getBlockState(var4.getBlockPos()).getBlock() == Blocks.end_portal_frame) {
         return var1;
      } else {
         if (!var2.isRemote) {
            BlockPos var5 = var2.getStrongholdPos("Stronghold", new BlockPos(var3));
            if (var5 != null) {
               EntityEnderEye var6 = new EntityEnderEye(var2, var3.posX, var3.posY, var3.posZ);
               var6.moveTowards(var5);
               var2.spawnEntityInWorld(var6);
               var2.playSoundAtEntity(var3, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
               var2.playAuxSFXAtEntity((EntityPlayer)null, 1002, new BlockPos(var3), 0);
               if (!var3.capabilities.isCreativeMode) {
                  --var1.stackSize;
               }

               var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }
         }

         return var1;
      }
   }
}
