/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHangingEntity
extends Item {
    private final Class<? extends EntityHanging> hangingEntityClass;

    private EntityHanging createEntity(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return this.hangingEntityClass == EntityPainting.class ? new EntityPainting(world, blockPos, enumFacing) : (this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(world, blockPos, enumFacing) : null);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (enumFacing == EnumFacing.DOWN) {
            return false;
        }
        if (enumFacing == EnumFacing.UP) {
            return false;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        if (!entityPlayer.canPlayerEdit(blockPos2, enumFacing, itemStack)) {
            return false;
        }
        EntityHanging entityHanging = this.createEntity(world, blockPos2, enumFacing);
        if (entityHanging != null && entityHanging.onValidSurface()) {
            if (!world.isRemote) {
                world.spawnEntityInWorld(entityHanging);
            }
            --itemStack.stackSize;
        }
        return true;
    }

    public ItemHangingEntity(Class<? extends EntityHanging> clazz) {
        this.hangingEntityClass = clazz;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
}

