/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase
extends Block {
    protected boolean fancyGraphics;

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return !this.fancyGraphics && iBlockAccess.getBlockState(blockPos).getBlock() == this ? false : super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    protected BlockLeavesBase(Material material, boolean bl) {
        super(material);
        this.fancyGraphics = bl;
    }
}

