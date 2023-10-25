/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BlockContainer
extends Block
implements ITileEntityProvider {
    @Override
    public boolean onBlockEventReceived(World world, BlockPos blockPos, IBlockState iBlockState, int n, int n2) {
        super.onBlockEventReceived(world, blockPos, iBlockState, n, n2);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity == null ? false : tileEntity.receiveClientEvent(n, n2);
    }

    protected boolean func_181086_a(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return world.getBlockState(blockPos.offset(enumFacing)).getBlock().getMaterial() == Material.cactus;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    protected boolean func_181087_e(World world, BlockPos blockPos) {
        return this.func_181086_a(world, blockPos, EnumFacing.NORTH) || this.func_181086_a(world, blockPos, EnumFacing.SOUTH) || this.func_181086_a(world, blockPos, EnumFacing.WEST) || this.func_181086_a(world, blockPos, EnumFacing.EAST);
    }

    protected BlockContainer(Material material) {
        this(material, material.getMaterialMapColor());
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.breakBlock(world, blockPos, iBlockState);
        world.removeTileEntity(blockPos);
    }

    protected BlockContainer(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.isBlockContainer = true;
    }
}

