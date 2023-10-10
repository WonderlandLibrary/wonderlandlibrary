package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBoat extends Item {
   public ItemBoat() {
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.tabTransport);
   }

   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      float var4 = 1.0F;
      float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var4;
      float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var4;
      double var7 = var3.prevPosX + (var3.posX - var3.prevPosX) * (double)var4;
      double var9 = var3.prevPosY + (var3.posY - var3.prevPosY) * (double)var4 + (double)var3.getEyeHeight();
      double var11 = var3.prevPosZ + (var3.posZ - var3.prevPosZ) * (double)var4;
      Vec3 var13 = new Vec3(var7, var9, var11);
      float var14 = MathHelper.cos(-var6 * 0.017453292F - 3.1415927F);
      float var15 = MathHelper.sin(-var6 * 0.017453292F - 3.1415927F);
      float var16 = -MathHelper.cos(-var5 * 0.017453292F);
      float var17 = MathHelper.sin(-var5 * 0.017453292F);
      float var18 = var15 * var16;
      float var19 = var14 * var16;
      double var20 = 5.0D;
      Vec3 var22 = var13.addVector((double)var18 * var20, (double)var17 * var20, (double)var19 * var20);
      MovingObjectPosition var23 = var2.rayTraceBlocks(var13, var22, true);
      if (var23 == null) {
         return var1;
      } else {
         Vec3 var24 = var3.getLook(var4);
         boolean var25 = false;
         float var26 = 1.0F;
         List var27 = var2.getEntitiesWithinAABBExcludingEntity(var3, var3.getEntityBoundingBox().addCoord(var24.xCoord * var20, var24.yCoord * var20, var24.zCoord * var20).expand((double)var26, (double)var26, (double)var26));

         for(int var28 = 0; var28 < var27.size(); ++var28) {
            Entity var29 = (Entity)var27.get(var28);
            if (var29.canBeCollidedWith()) {
               float var30 = var29.getCollisionBorderSize();
               AxisAlignedBB var31 = var29.getEntityBoundingBox().expand((double)var30, (double)var30, (double)var30);
               if (var31.isVecInside(var13)) {
                  var25 = true;
               }
            }
         }

         if (var25) {
            return var1;
         } else {
            if (var23.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
               BlockPos var33 = var23.getBlockPos();
               if (var2.getBlockState(var33).getBlock() == Blocks.snow_layer) {
                  var33 = var33.down();
               }

               EntityBoat var34 = new EntityBoat(var2, (double)((float)var33.getX() + 0.5F), (double)((float)var33.getY() + 1.0F), (double)((float)var33.getZ() + 0.5F));
               var34.rotationYaw = (float)(((MathHelper.floor_double((double)(var3.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
               if (!var2.getCollidingBoundingBoxes(var34, var34.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                  return var1;
               }

               if (!var2.isRemote) {
                  var2.spawnEntityInWorld(var34);
               }

               if (!var3.capabilities.isCreativeMode) {
                  --var1.stackSize;
               }

               var3.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }

            return var1;
         }
      }
   }
}
