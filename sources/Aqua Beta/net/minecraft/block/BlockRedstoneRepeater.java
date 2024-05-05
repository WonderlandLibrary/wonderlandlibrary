package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.properties.IProperty;
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
    public static final PropertyBool LOCKED = PropertyBool.create((String)"locked");
    public static final PropertyInteger DELAY = PropertyInteger.create((String)"delay", (int)1, (int)4);

    protected BlockRedstoneRepeater(boolean powered) {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)DELAY, (Comparable)Integer.valueOf((int)1)).withProperty((IProperty)LOCKED, (Comparable)Boolean.valueOf((boolean)false)));
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)"item.diode.name");
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty((IProperty)LOCKED, (Comparable)Boolean.valueOf((boolean)this.isLocked(worldIn, pos, state)));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        worldIn.setBlockState(pos, state.cycleProperty((IProperty)DELAY), 3);
        return true;
    }

    protected int getDelay(IBlockState state) {
        return (Integer)state.getValue((IProperty)DELAY) * 2;
    }

    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Integer integer = (Integer)unpoweredState.getValue((IProperty)DELAY);
        Boolean obool = (Boolean)unpoweredState.getValue((IProperty)LOCKED);
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue((IProperty)FACING);
        return Blocks.powered_repeater.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DELAY, (Comparable)integer).withProperty((IProperty)LOCKED, (Comparable)obool);
    }

    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Integer integer = (Integer)poweredState.getValue((IProperty)DELAY);
        Boolean obool = (Boolean)poweredState.getValue((IProperty)LOCKED);
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue((IProperty)FACING);
        return Blocks.unpowered_repeater.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DELAY, (Comparable)integer).withProperty((IProperty)LOCKED, (Comparable)obool);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.repeater;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.repeater;
    }

    public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        return this.getPowerOnSides(worldIn, pos, state) > 0;
    }

    protected boolean canPowerSide(Block blockIn) {
        return BlockRedstoneRepeater.isRedstoneRepeaterBlockID((Block)blockIn);
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.isRepeaterPowered) {
            EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
            double d0 = (double)((float)pos.getX() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double d1 = (double)((float)pos.getY() + 0.4f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            double d2 = (double)((float)pos.getZ() + 0.5f) + (double)(rand.nextFloat() - 0.5f) * 0.2;
            float f = -5.0f;
            if (rand.nextBoolean()) {
                f = (Integer)state.getValue((IProperty)DELAY) * 2 - 1;
            }
            double d3 = (f /= 16.0f) * (float)enumfacing.getFrontOffsetX();
            double d4 = f * (float)enumfacing.getFrontOffsetZ();
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        this.notifyNeighbors(worldIn, pos, state);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)meta)).withProperty((IProperty)LOCKED, (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)DELAY, (Comparable)Integer.valueOf((int)(1 + (meta >> 2))));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        return i |= (Integer)state.getValue((IProperty)DELAY) - 1 << 2;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, DELAY, LOCKED});
    }
}
