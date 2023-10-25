/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockBreakable
extends Block {
    private boolean ignoreSimilarity;

    protected BlockBreakable(Material material, boolean bl, MapColor mapColor) {
        super(material, mapColor);
        this.ignoreSimilarity = bl;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (this == Blocks.glass || this == Blocks.stained_glass) {
            if (iBlockAccess.getBlockState(blockPos.offset(enumFacing.getOpposite())) != iBlockState) {
                return true;
            }
            if (block == this) {
                return false;
            }
        }
        return !this.ignoreSimilarity && block == this ? false : super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing);
    }

    protected BlockBreakable(Material material, boolean bl) {
        this(material, bl, material.getMaterialMapColor());
    }
}

