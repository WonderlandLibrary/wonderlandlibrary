/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndGateway
extends BlockContainer {
    protected BlockEndGateway(Material p_i46687_1_) {
        super(p_i46687_1_);
        this.setLightLevel(1.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEndGateway();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return !iblockstate.isOpaqueCube() && block != Blocks.END_GATEWAY;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityEndGateway) {
            int i = ((TileEntityEndGateway)tileentity).getParticleAmount();
            for (int j = 0; j < i; ++j) {
                double d0 = (float)pos.getX() + rand.nextFloat();
                double d1 = (float)pos.getY() + rand.nextFloat();
                double d2 = (float)pos.getZ() + rand.nextFloat();
                double d3 = ((double)rand.nextFloat() - 0.5) * 0.5;
                double d4 = ((double)rand.nextFloat() - 0.5) * 0.5;
                double d5 = ((double)rand.nextFloat() - 0.5) * 0.5;
                int k = rand.nextInt(2) * 2 - 1;
                if (rand.nextBoolean()) {
                    d2 = (double)pos.getZ() + 0.5 + 0.25 * (double)k;
                    d5 = rand.nextFloat() * 2.0f * (float)k;
                } else {
                    d0 = (double)pos.getX() + 0.5 + 0.25 * (double)k;
                    d3 = rand.nextFloat() * 2.0f * (float)k;
                }
                worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
            }
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.field_190927_a;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
        return MapColor.BLACK;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}

