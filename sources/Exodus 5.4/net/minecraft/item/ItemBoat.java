/*
 * Decompiled with CFR 0.152.
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
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        Entity entity;
        float f;
        float f2;
        double d;
        float f3;
        float f4 = 1.0f;
        float f5 = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch) * f4;
        float f6 = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * f4;
        double d2 = entityPlayer.prevPosX + (entityPlayer.posX - entityPlayer.prevPosX) * (double)f4;
        double d3 = entityPlayer.prevPosY + (entityPlayer.posY - entityPlayer.prevPosY) * (double)f4 + (double)entityPlayer.getEyeHeight();
        double d4 = entityPlayer.prevPosZ + (entityPlayer.posZ - entityPlayer.prevPosZ) * (double)f4;
        Vec3 vec3 = new Vec3(d2, d3, d4);
        float f7 = MathHelper.cos(-f6 * ((float)Math.PI / 180) - (float)Math.PI);
        float f8 = MathHelper.sin(-f6 * ((float)Math.PI / 180) - (float)Math.PI);
        float f9 = f8 * (f3 = -MathHelper.cos(-f5 * ((float)Math.PI / 180)));
        Vec3 vec32 = vec3.addVector((double)f9 * (d = 5.0), (double)(f2 = MathHelper.sin(-f5 * ((float)Math.PI / 180))) * d, (double)(f = f7 * f3) * d);
        MovingObjectPosition movingObjectPosition = world.rayTraceBlocks(vec3, vec32, true);
        if (movingObjectPosition == null) {
            return itemStack;
        }
        Vec3 vec33 = entityPlayer.getLook(f4);
        boolean bl = false;
        float f10 = 1.0f;
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().addCoord(vec33.xCoord * d, vec33.yCoord * d, vec33.zCoord * d).expand(f10, f10, f10));
        int n = 0;
        while (n < list.size()) {
            entity = list.get(n);
            if (entity.canBeCollidedWith()) {
                float f11 = entity.getCollisionBorderSize();
                AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(f11, f11, f11);
                if (axisAlignedBB.isVecInside(vec3)) {
                    bl = true;
                }
            }
            ++n;
        }
        if (bl) {
            return itemStack;
        }
        if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos blockPos = movingObjectPosition.getBlockPos();
            if (world.getBlockState(blockPos).getBlock() == Blocks.snow_layer) {
                blockPos = blockPos.down();
            }
            entity = new EntityBoat(world, (float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 1.0f, (float)blockPos.getZ() + 0.5f);
            ((EntityBoat)entity).rotationYaw = ((MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0f / 360.0f) + 0.5) & 3) - 1) * 90;
            if (!world.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox().expand(-0.1, -0.1, -0.1)).isEmpty()) {
                return itemStack;
            }
            if (!world.isRemote) {
                world.spawnEntityInWorld(entity);
            }
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        }
        return itemStack;
    }

    public ItemBoat() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
}

