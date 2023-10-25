/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater
extends BlockRedstoneDiode {
    public static final PropertyInteger DELAY;
    public static final PropertyBool LOCKED;

    protected BlockRedstoneRepeater(boolean bl) {
        super(bl);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DELAY, 1).withProperty(LOCKED, false));
    }

    static {
        LOCKED = PropertyBool.create("locked");
        DELAY = PropertyInteger.create("delay", 1, 4);
    }

    @Override
    protected int getDelay(IBlockState iBlockState) {
        return iBlockState.getValue(DELAY) * 2;
    }

    @Override
    public boolean isLocked(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState) {
        return this.getPowerOnSides(iBlockAccess, blockPos, iBlockState) > 0;
    }

    @Override
    protected boolean canPowerSide(Block block) {
        return BlockRedstoneRepeater.isRedstoneRepeaterBlockID(block);
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.diode.name");
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.breakBlock(world, blockPos, iBlockState);
        this.notifyNeighbors(world, blockPos, iBlockState);
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.repeater;
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState iBlockState) {
        Integer n = iBlockState.getValue(DELAY);
        Boolean bl = iBlockState.getValue(LOCKED);
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        return Blocks.unpowered_repeater.getDefaultState().withProperty(FACING, enumFacing).withProperty(DELAY, n).withProperty(LOCKED, bl);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (!entityPlayer.capabilities.allowEdit) {
            return false;
        }
        world.setBlockState(blockPos, iBlockState.cycleProperty(DELAY), 3);
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(n)).withProperty(LOCKED, false).withProperty(DELAY, 1 + (n >> 2));
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.repeater;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (this.isRepeaterPowered) {
            EnumFacing enumFacing = iBlockState.getValue(FACING);
            double d = (double)((float)blockPos.getX() + 0.5f) + (double)(random.nextFloat() - 0.5f) * 0.2;
            double d2 = (double)((float)blockPos.getY() + 0.4f) + (double)(random.nextFloat() - 0.5f) * 0.2;
            double d3 = (double)((float)blockPos.getZ() + 0.5f) + (double)(random.nextFloat() - 0.5f) * 0.2;
            float f = -5.0f;
            if (random.nextBoolean()) {
                f = iBlockState.getValue(DELAY) * 2 - 1;
            }
            double d4 = (f /= 16.0f) * (float)enumFacing.getFrontOffsetX();
            double d5 = f * (float)enumFacing.getFrontOffsetZ();
            world.spawnParticle(EnumParticleTypes.REDSTONE, d + d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockState.withProperty(LOCKED, this.isLocked(iBlockAccess, blockPos, iBlockState));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, DELAY, LOCKED);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getHorizontalIndex();
        return n |= iBlockState.getValue(DELAY) - 1 << 2;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState iBlockState) {
        Integer n = iBlockState.getValue(DELAY);
        Boolean bl = iBlockState.getValue(LOCKED);
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        return Blocks.powered_repeater.getDefaultState().withProperty(FACING, enumFacing).withProperty(DELAY, n).withProperty(LOCKED, bl);
    }
}

