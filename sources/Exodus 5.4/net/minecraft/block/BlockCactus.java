/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockCactus
extends Block {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(AGE);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!this.canBlockStay(world, blockPos)) {
            world.destroyBlock(blockPos, true);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        float f = 0.0625f;
        return new AxisAlignedBB((float)blockPos.getX() + f, blockPos.getY(), (float)blockPos.getZ() + f, (float)(blockPos.getX() + 1) - f, (float)(blockPos.getY() + 1) - f, (float)(blockPos.getZ() + 1) - f);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) ? this.canBlockStay(world, blockPos) : false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        float f = 0.0625f;
        return new AxisAlignedBB((float)blockPos.getX() + f, blockPos.getY(), (float)blockPos.getZ() + f, (float)(blockPos.getX() + 1) - f, blockPos.getY() + 1, (float)(blockPos.getZ() + 1) - f);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    protected BlockCactus() {
        super(Material.cactus);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(AGE, n);
    }

    public boolean canBlockStay(World world, BlockPos blockPos) {
        Object object2;
        for (Object object2 : EnumFacing.Plane.HORIZONTAL) {
            if (!world.getBlockState(blockPos.offset((EnumFacing)object2)).getBlock().getMaterial().isSolid()) continue;
            return false;
        }
        object2 = world.getBlockState(blockPos.down()).getBlock();
        return object2 == Blocks.cactus || object2 == Blocks.sand;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        BlockPos blockPos2 = blockPos.up();
        if (world.isAirBlock(blockPos2)) {
            int n = 1;
            while (world.getBlockState(blockPos.down(n)).getBlock() == this) {
                ++n;
            }
            if (n < 3) {
                int n2 = iBlockState.getValue(AGE);
                if (n2 == 15) {
                    world.setBlockState(blockPos2, this.getDefaultState());
                    IBlockState iBlockState2 = iBlockState.withProperty(AGE, 0);
                    world.setBlockState(blockPos, iBlockState2, 4);
                    this.onNeighborBlockChange(world, blockPos2, iBlockState2, this);
                } else {
                    world.setBlockState(blockPos, iBlockState.withProperty(AGE, n2 + 1), 4);
                }
            }
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
        entity.attackEntityFrom(DamageSource.cactus, 1.0f);
    }
}

