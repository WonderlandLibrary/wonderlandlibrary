/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBoat
extends Item {
    private static final String __OBFID = "CL_00001774";

    public ItemBoat() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        float var20;
        float var17;
        double var21;
        float var16;
        float var4 = 1.0f;
        float var5 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * var4;
        float var6 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * var4;
        double var7 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * (double)var4;
        double var9 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * (double)var4 + (double)playerIn.getEyeHeight();
        double var11 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * (double)var4;
        Vec3 var13 = new Vec3(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * ((float)Math.PI / 180) - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * ((float)Math.PI / 180) - (float)Math.PI);
        float var18 = var15 * (var16 = -MathHelper.cos(-var5 * ((float)Math.PI / 180)));
        Vec3 var23 = var13.addVector((double)var18 * (var21 = 5.0), (double)(var17 = MathHelper.sin(-var5 * ((float)Math.PI / 180))) * var21, (double)(var20 = var14 * var16) * var21);
        MovingObjectPosition var24 = worldIn.rayTraceBlocks(var13, var23, true);
        if (var24 == null) {
            return itemStackIn;
        }
        Vec3 var25 = playerIn.getLook(var4);
        boolean var26 = false;
        float var27 = 1.0f;
        List var28 = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().addCoord(var25.xCoord * var21, var25.yCoord * var21, var25.zCoord * var21).expand(var27, var27, var27));
        for (int var29 = 0; var29 < var28.size(); ++var29) {
            Entity var30 = (Entity)var28.get(var29);
            if (!var30.canBeCollidedWith()) continue;
            float var31 = var30.getCollisionBorderSize();
            AxisAlignedBB var32 = var30.getEntityBoundingBox().expand(var31, var31, var31);
            if (!var32.isVecInside(var13)) continue;
            var26 = true;
        }
        if (var26) {
            return itemStackIn;
        }
        if (var24.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos var33 = var24.func_178782_a();
            if (worldIn.getBlockState(var33).getBlock() == Blocks.snow_layer) {
                var33 = var33.offsetDown();
            }
            EntityBoat var34 = new EntityBoat(worldIn, (float)var33.getX() + 0.5f, (float)var33.getY() + 1.0f, (float)var33.getZ() + 0.5f);
            var34.rotationYaw = ((MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0f / 360.0f) + 0.5) & 3) - 1) * 90;
            if (!worldIn.getCollidingBoundingBoxes(var34, var34.getEntityBoundingBox().expand(-0.1, -0.1, -0.1)).isEmpty()) {
                return itemStackIn;
            }
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(var34);
            }
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStackIn;
    }
}

