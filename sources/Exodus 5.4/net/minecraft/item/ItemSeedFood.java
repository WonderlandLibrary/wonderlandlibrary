/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeedFood
extends ItemFood {
    private Block soilId;
    private Block crops;

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (enumFacing != EnumFacing.UP) {
            return false;
        }
        if (!entityPlayer.canPlayerEdit(blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        if (world.getBlockState(blockPos).getBlock() == this.soilId && world.isAirBlock(blockPos.up())) {
            world.setBlockState(blockPos.up(), this.crops.getDefaultState());
            --itemStack.stackSize;
            return true;
        }
        return false;
    }

    public ItemSeedFood(int n, float f, Block block, Block block2) {
        super(n, f, false);
        this.crops = block;
        this.soilId = block2;
    }
}

