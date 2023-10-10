package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand extends Item {
   public ItemArmorStand() {
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   private void applyRandomRotations(EntityArmorStand var1, Random var2) {
      Rotations var3 = var1.getHeadRotation();
      float var4 = var2.nextFloat() * 5.0F;
      float var5 = var2.nextFloat() * 20.0F - 10.0F;
      Rotations var6 = new Rotations(var3.getX() + var4, var3.getY() + var5, var3.getZ());
      var1.setHeadRotation(var6);
      var3 = var1.getBodyRotation();
      var4 = var2.nextFloat() * 10.0F - 5.0F;
      var6 = new Rotations(var3.getX(), var3.getY() + var4, var3.getZ());
      var1.setBodyRotation(var6);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var5 == EnumFacing.DOWN) {
         return false;
      } else {
         boolean var9 = var3.getBlockState(var4).getBlock().isReplaceable(var3, var4);
         BlockPos var10 = var9 ? var4 : var4.offset(var5);
         if (!var2.canPlayerEdit(var10, var5, var1)) {
            return false;
         } else {
            BlockPos var11 = var10.up();
            boolean var12 = !var3.isAirBlock(var10) && !var3.getBlockState(var10).getBlock().isReplaceable(var3, var10);
            var12 |= !var3.isAirBlock(var11) && !var3.getBlockState(var11).getBlock().isReplaceable(var3, var11);
            if (var12) {
               return false;
            } else {
               double var13 = (double)var10.getX();
               double var15 = (double)var10.getY();
               double var17 = (double)var10.getZ();
               List var19 = var3.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.fromBounds(var13, var15, var17, var13 + 1.0D, var15 + 2.0D, var17 + 1.0D));
               if (var19.size() > 0) {
                  return false;
               } else {
                  if (!var3.isRemote) {
                     var3.setBlockToAir(var10);
                     var3.setBlockToAir(var11);
                     EntityArmorStand var20 = new EntityArmorStand(var3, var13 + 0.5D, var15, var17 + 0.5D);
                     float var21 = (float)MathHelper.floor_float((MathHelper.wrapAngleTo180_float(var2.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                     var20.setLocationAndAngles(var13 + 0.5D, var15, var17 + 0.5D, var21, 0.0F);
                     this.applyRandomRotations(var20, var3.rand);
                     NBTTagCompound var22 = var1.getTagCompound();
                     if (var22 != null && var22.hasKey("EntityTag", 10)) {
                        NBTTagCompound var23 = new NBTTagCompound();
                        var20.writeToNBTOptional(var23);
                        var23.merge(var22.getCompoundTag("EntityTag"));
                        var20.readFromNBT(var23);
                     }

                     var3.spawnEntityInWorld(var20);
                  }

                  --var1.stackSize;
                  return true;
               }
            }
         }
      }
   }
}
