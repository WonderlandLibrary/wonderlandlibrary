/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater
extends BlockRedstoneDiode {
    public static final PropertyBool LOCKED = PropertyBool.create("locked");
    public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);

    protected BlockRedstoneRepeater(boolean powered) {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DELAY, 1).withProperty(LOCKED, false));
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("item.diode.name");
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(LOCKED, this.isLocked(worldIn, pos, state));
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        worldIn.setBlockState(pos, state.cycleProperty(DELAY), 3);
        return true;
    }

    @Override
    protected int getDelay(IBlockState state) {
        return state.getValue(DELAY) * 2;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Integer integer = unpoweredState.getValue(DELAY);
        Boolean obool = unpoweredState.getValue(LOCKED);
        EnumFacing enumfacing = unpoweredState.getValue(FACING);
        return Blocks.POWERED_REPEATER.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Integer integer = poweredState.getValue(DELAY);
        Boolean obool = poweredState.getValue(LOCKED);
        EnumFacing enumfacing = poweredState.getValue(FACING);
        return Blocks.UNPOWERED_REPEATER.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.REPEATER;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Items.REPEATER);
    }

    @Override
    public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        return this.getPowerOnSides(worldIn, pos, state) > 0;
    }

    @Override
    protected boolean isAlternateInput(IBlockState state) {
        return BlockRedstoneRepeater.isDiode(state);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isRepeaterPowered) {
            EnumFacing enumfacing = stateIn.getValue(FACING);
            double d0 = (double)((float)pos.getX() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double d1 = (double)((float)pos.getY() + 0.4f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double d2 = (double)((float)pos.getZ() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            float f = -5.0f;
            if (rand.nextBoolean()) {
                f = stateIn.getValue(DELAY) * 2 - 1;
            }
            double d3 = (f /= 16.0f) * (float)enumfacing.getXOffset();
            double d4 = f * (float)enumfacing.getZOffset();
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        this.notifyNeighbors(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta)).withProperty(LOCKED, false).withProperty(DELAY, 1 + (meta >> 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getHorizontalIndex();
        return i |= state.getValue(DELAY) - 1 << 2;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, DELAY, LOCKED);
    }
}

