// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.BlockRenderLayer;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;

public class BlockTripWire extends Block
{
    public static final PropertyBool POWERED;
    public static final PropertyBool ATTACHED;
    public static final PropertyBool DISARMED;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    protected static final AxisAlignedBB AABB;
    protected static final AxisAlignedBB TRIP_WRITE_ATTACHED_AABB;
    
    public BlockTripWire() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTripWire.POWERED, false).withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, false).withProperty((IProperty<Comparable>)BlockTripWire.DISARMED, false).withProperty((IProperty<Comparable>)BlockTripWire.NORTH, false).withProperty((IProperty<Comparable>)BlockTripWire.EAST, false).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, false).withProperty((IProperty<Comparable>)BlockTripWire.WEST, false));
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return state.getValue((IProperty<Boolean>)BlockTripWire.ATTACHED) ? BlockTripWire.AABB : BlockTripWire.TRIP_WRITE_ATTACHED_AABB;
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockTripWire.NORTH, isConnectedTo(worldIn, pos, state, EnumFacing.NORTH)).withProperty((IProperty<Comparable>)BlockTripWire.EAST, isConnectedTo(worldIn, pos, state, EnumFacing.EAST)).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH)).withProperty((IProperty<Comparable>)BlockTripWire.WEST, isConnectedTo(worldIn, pos, state, EnumFacing.WEST));
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockTripWire.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.STRING;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.STRING);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.setBlockState(pos, state, 3);
        this.notifyHook(worldIn, pos, state);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.notifyHook(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTripWire.POWERED, true));
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (!worldIn.isRemote && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == Items.SHEARS) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockTripWire.DISARMED, true), 4);
        }
    }
    
    private void notifyHook(final World worldIn, final BlockPos pos, final IBlockState state) {
        for (final EnumFacing enumfacing : new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }) {
            int i = 1;
            while (i < 42) {
                final BlockPos blockpos = pos.offset(enumfacing, i);
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK) {
                    if (iblockstate.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == enumfacing.getOpposite()) {
                        Blocks.TRIPWIRE_HOOK.calculateState(worldIn, blockpos, iblockstate, false, true, i, state);
                        break;
                    }
                    break;
                }
                else {
                    if (iblockstate.getBlock() != Blocks.TRIPWIRE) {
                        break;
                    }
                    ++i;
                }
            }
        }
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && !state.getValue((IProperty<Boolean>)BlockTripWire.POWERED)) {
            this.updateState(worldIn, pos);
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockTripWire.POWERED)) {
            this.updateState(worldIn, pos);
        }
    }
    
    private void updateState(final World worldIn, final BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        final boolean flag = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.POWERED);
        boolean flag2 = false;
        final List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, iblockstate.getBoundingBox(worldIn, pos).offset(pos));
        if (!list.isEmpty()) {
            for (final Entity entity : list) {
                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    flag2 = true;
                    break;
                }
            }
        }
        if (flag2 != flag) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTripWire.POWERED, flag2);
            worldIn.setBlockState(pos, iblockstate, 3);
            this.notifyHook(worldIn, pos, iblockstate);
        }
        if (flag2) {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }
    
    public static boolean isConnectedTo(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing direction) {
        final BlockPos blockpos = pos.offset(direction);
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.TRIPWIRE_HOOK) {
            final EnumFacing enumfacing = direction.getOpposite();
            return iblockstate.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == enumfacing;
        }
        return block == Blocks.TRIPWIRE;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.POWERED, (meta & 0x1) > 0).withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, (meta & 0x4) > 0).withProperty((IProperty<Comparable>)BlockTripWire.DISARMED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue((IProperty<Boolean>)BlockTripWire.POWERED)) {
            i |= 0x1;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWire.ATTACHED)) {
            i |= 0x4;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWire.DISARMED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                return state.withProperty((IProperty<Comparable>)BlockTripWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.SOUTH)).withProperty((IProperty<Comparable>)BlockTripWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.WEST)).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.NORTH)).withProperty((IProperty<Comparable>)BlockTripWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return state.withProperty((IProperty<Comparable>)BlockTripWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.EAST)).withProperty((IProperty<Comparable>)BlockTripWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.SOUTH)).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.WEST)).withProperty((IProperty<Comparable>)BlockTripWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.NORTH));
            }
            case CLOCKWISE_90: {
                return state.withProperty((IProperty<Comparable>)BlockTripWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.WEST)).withProperty((IProperty<Comparable>)BlockTripWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.NORTH)).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.EAST)).withProperty((IProperty<Comparable>)BlockTripWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.SOUTH));
            }
            default: {
                return state;
            }
        }
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                return state.withProperty((IProperty<Comparable>)BlockTripWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.SOUTH)).withProperty((IProperty<Comparable>)BlockTripWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockTripWire.NORTH));
            }
            case FRONT_BACK: {
                return state.withProperty((IProperty<Comparable>)BlockTripWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.WEST)).withProperty((IProperty<Comparable>)BlockTripWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockTripWire.EAST));
            }
            default: {
                return super.withMirror(state, mirrorIn);
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockTripWire.POWERED, BlockTripWire.ATTACHED, BlockTripWire.DISARMED, BlockTripWire.NORTH, BlockTripWire.EAST, BlockTripWire.WEST, BlockTripWire.SOUTH });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        POWERED = PropertyBool.create("powered");
        ATTACHED = PropertyBool.create("attached");
        DISARMED = PropertyBool.create("disarmed");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        AABB = new AxisAlignedBB(0.0, 0.0625, 0.0, 1.0, 0.15625, 1.0);
        TRIP_WRITE_ATTACHED_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    }
}
