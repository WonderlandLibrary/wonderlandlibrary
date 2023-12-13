/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndPortal
extends BlockContainer {
    private static final String __OBFID = "CL_00000236";

    protected BlockEndPortal(Material p_i45404_1_) {
        super(p_i45404_1_);
        this.setLightLevel(1.0f);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEndPortal();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.0625f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, var3, 1.0f);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? super.shouldSideBeRendered(worldIn, pos, side) : false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null && !worldIn.isRemote) {
            entityIn.travelToDimension(1);
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        double var5 = (float)pos.getX() + rand.nextFloat();
        double var7 = (float)pos.getY() + 0.8f;
        double var9 = (float)pos.getZ() + rand.nextFloat();
        double var11 = 0.0;
        double var13 = 0.0;
        double var15 = 0.0;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var7, var9, var11, var13, var15, new int[0]);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.obsidianColor;
    }
}

