/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.EventAir;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAir
extends Block {
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        EventAir eventAir = new EventAir();
        eventAir.call();
        Exodus.onEvent(eventAir);
        if (eventAir.isCancelled()) {
            double d = blockPos.getX();
            Minecraft.getMinecraft();
            double d2 = d - Minecraft.thePlayer.posX;
            double d3 = blockPos.getY();
            double d4 = blockPos.getZ();
            Minecraft.getMinecraft();
            double d5 = d4 - Minecraft.thePlayer.posZ;
            double d6 = blockPos.getX() + 1;
            Minecraft.getMinecraft();
            double d7 = d6 + Minecraft.thePlayer.posX;
            double d8 = blockPos.getY() + 1;
            double d9 = blockPos.getZ();
            Minecraft.getMinecraft();
            return new AxisAlignedBB(d2, d3, d5, d7, d8, d9 + Minecraft.thePlayer.posZ);
        }
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
    }

    protected BlockAir() {
        super(Material.air);
    }

    @Override
    public boolean canCollideCheck(IBlockState iBlockState, boolean bl) {
        return false;
    }

    @Override
    public boolean isReplaceable(World world, BlockPos blockPos) {
        return true;
    }

    @Override
    public int getRenderType() {
        return -1;
    }
}

