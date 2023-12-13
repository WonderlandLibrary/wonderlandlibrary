// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.stats.StatList;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBoat extends Item
{
    private static final String __OBFID = "CL_00001774";
    
    public ItemBoat() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final float var4 = 1.0f;
        final float var5 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * var4;
        final float var6 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * var4;
        final double var7 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * var4;
        final double var8 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * var4 + playerIn.getEyeHeight();
        final double var9 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * var4;
        final Vec3 var10 = new Vec3(var7, var8, var9);
        final float var11 = MathHelper.cos(-var6 * 0.017453292f - 3.1415927f);
        final float var12 = MathHelper.sin(-var6 * 0.017453292f - 3.1415927f);
        final float var13 = -MathHelper.cos(-var5 * 0.017453292f);
        final float var14 = MathHelper.sin(-var5 * 0.017453292f);
        final float var15 = var12 * var13;
        final float var16 = var11 * var13;
        final double var17 = 5.0;
        final Vec3 var18 = var10.addVector(var15 * var17, var14 * var17, var16 * var17);
        final MovingObjectPosition var19 = worldIn.rayTraceBlocks(var10, var18, true);
        if (var19 == null) {
            return itemStackIn;
        }
        final Vec3 var20 = playerIn.getLook(var4);
        boolean var21 = false;
        final float var22 = 1.0f;
        final List var23 = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().addCoord(var20.xCoord * var17, var20.yCoord * var17, var20.zCoord * var17).expand(var22, var22, var22));
        for (int var24 = 0; var24 < var23.size(); ++var24) {
            final Entity var25 = var23.get(var24);
            if (var25.canBeCollidedWith()) {
                final float var26 = var25.getCollisionBorderSize();
                final AxisAlignedBB var27 = var25.getEntityBoundingBox().expand(var26, var26, var26);
                if (var27.isVecInside(var10)) {
                    var21 = true;
                }
            }
        }
        if (var21) {
            return itemStackIn;
        }
        if (var19.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var28 = var19.getBlockPos();
            if (worldIn.getBlockState(var28).getBlock() == Blocks.snow_layer) {
                var28 = var28.offsetDown();
            }
            final EntityBoat var29 = new EntityBoat(worldIn, var28.getX() + 0.5f, var28.getY() + 1.0f, var28.getZ() + 0.5f);
            var29.rotationYaw = ((MathHelper.floor_double(playerIn.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3) - 1) * 90;
            if (!worldIn.getCollidingBoundingBoxes(var29, var29.getEntityBoundingBox().expand(-0.1, -0.1, -0.1)).isEmpty()) {
                return itemStackIn;
            }
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(var29);
            }
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStackIn;
    }
}
