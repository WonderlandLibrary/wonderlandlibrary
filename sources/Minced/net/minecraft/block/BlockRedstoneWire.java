// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import java.util.EnumSet;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRedstoneWire extends Block
{
    public static final PropertyEnum<EnumAttachPosition> NORTH;
    public static final PropertyEnum<EnumAttachPosition> EAST;
    public static final PropertyEnum<EnumAttachPosition> SOUTH;
    public static final PropertyEnum<EnumAttachPosition> WEST;
    public static final PropertyInteger POWER;
    protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB;
    private boolean canProvidePower;
    private final Set<BlockPos> blocksNeedingUpdate;
    
    public BlockRedstoneWire() {
        super(Material.CIRCUITS);
        this.canProvidePower = true;
        this.blocksNeedingUpdate = (Set<BlockPos>)Sets.newHashSet();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedstoneWire.NORTH, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.EAST, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.SOUTH, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.WEST, EnumAttachPosition.NONE).withProperty((IProperty<Comparable>)BlockRedstoneWire.POWER, 0));
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockRedstoneWire.REDSTONE_WIRE_AABB[getAABBIndex(state.getActualState(source, pos))];
    }
    
    private static int getAABBIndex(final IBlockState state) {
        int i = 0;
        final boolean flag = state.getValue(BlockRedstoneWire.NORTH) != EnumAttachPosition.NONE;
        final boolean flag2 = state.getValue(BlockRedstoneWire.EAST) != EnumAttachPosition.NONE;
        final boolean flag3 = state.getValue(BlockRedstoneWire.SOUTH) != EnumAttachPosition.NONE;
        final boolean flag4 = state.getValue(BlockRedstoneWire.WEST) != EnumAttachPosition.NONE;
        if (flag || (flag3 && !flag && !flag2 && !flag4)) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }
        if (flag2 || (flag4 && !flag && !flag2 && !flag3)) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }
        if (flag3 || (flag && !flag2 && !flag3 && !flag4)) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }
        if (flag4 || (flag2 && !flag && !flag3 && !flag4)) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }
        return i;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        state = state.withProperty(BlockRedstoneWire.WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(BlockRedstoneWire.EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(BlockRedstoneWire.NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(BlockRedstoneWire.SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }
    
    private EnumAttachPosition getAttachPosition(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing direction) {
        final BlockPos blockpos = pos.offset(direction);
        final IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));
        if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (iblockstate.isNormalCube() || !canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))) {
            final IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
            if (!iblockstate2.isNormalCube()) {
                final boolean flag = worldIn.getBlockState(blockpos).isTopSolid() || worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;
                if (flag && canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) {
                    if (iblockstate.isBlockNormalCube()) {
                        return EnumAttachPosition.UP;
                    }
                    return EnumAttachPosition.SIDE;
                }
            }
            return EnumAttachPosition.NONE;
        }
        return EnumAttachPosition.SIDE;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockRedstoneWire.NULL_AABB;
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid() || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }
    
    private IBlockState updateSurroundingRedstone(final World worldIn, final BlockPos pos, IBlockState state) {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList((Iterable)this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (final BlockPos blockpos : list) {
            worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
        }
        return state;
    }
    
    private IBlockState calculateCurrentChanges(final World worldIn, final BlockPos pos1, final BlockPos pos2, IBlockState state) {
        final IBlockState iblockstate = state;
        final int i = state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        int j = 0;
        j = this.getMaxCurrentStrength(worldIn, pos2, j);
        this.canProvidePower = false;
        final int k = worldIn.getRedstonePowerFromNeighbors(pos1);
        this.canProvidePower = true;
        if (k > 0 && k > j - 1) {
            j = k;
        }
        int l = 0;
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos1.offset(enumfacing);
            final boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();
            if (flag) {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }
            if (worldIn.getBlockState(blockpos).isNormalCube() && !worldIn.getBlockState(pos1.up()).isNormalCube()) {
                if (!flag || pos1.getY() < pos2.getY()) {
                    continue;
                }
                l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
            }
            else {
                if (worldIn.getBlockState(blockpos).isNormalCube() || !flag || pos1.getY() > pos2.getY()) {
                    continue;
                }
                l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
            }
        }
        if (l > j) {
            j = l - 1;
        }
        else if (j > 0) {
            --j;
        }
        else {
            j = 0;
        }
        if (k > j - 1) {
            j = k;
        }
        if (i != j) {
            state = state.withProperty((IProperty<Comparable>)BlockRedstoneWire.POWER, j);
            if (worldIn.getBlockState(pos1) == iblockstate) {
                worldIn.setBlockState(pos1, state, 2);
            }
            this.blocksNeedingUpdate.add(pos1);
            for (final EnumFacing enumfacing2 : EnumFacing.values()) {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing2));
            }
        }
        return state;
    }
    
    private void notifyWireNeighborsOfStateChange(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (final EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
            for (final EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing2));
            }
            for (final EnumFacing enumfacing3 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos blockpos = pos.offset(enumfacing3);
                if (worldIn.getBlockState(blockpos).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (!worldIn.isRemote) {
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (final EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing2));
            }
            for (final EnumFacing enumfacing3 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos blockpos = pos.offset(enumfacing3);
                if (worldIn.getBlockState(blockpos).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }
    
    private int getMaxCurrentStrength(final World worldIn, final BlockPos pos, final int strength) {
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return strength;
        }
        final int i = worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        return (i > strength) ? i : strength;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            if (this.canPlaceBlockAt(worldIn, pos)) {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.REDSTONE;
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return this.canProvidePower ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        final int i = blockState.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        if (i == 0) {
            return 0;
        }
        if (side == EnumFacing.UP) {
            return i;
        }
        final EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.isPowerSourceAt(blockAccess, pos, enumfacing)) {
                enumset.add(enumfacing);
            }
        }
        if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
            return i;
        }
        if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY())) {
            return i;
        }
        return 0;
    }
    
    private boolean isPowerSourceAt(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final BlockPos blockpos = pos.offset(side);
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final boolean flag = iblockstate.isNormalCube();
        final boolean flag2 = worldIn.getBlockState(pos.up()).isNormalCube();
        return (!flag2 && flag && canConnectUpwardsTo(worldIn, blockpos.up())) || canConnectTo(iblockstate, side) || (iblockstate.getBlock() == Blocks.POWERED_REPEATER && iblockstate.getValue((IProperty<Comparable>)BlockRedstoneDiode.FACING) == side) || (!flag && canConnectUpwardsTo(worldIn, blockpos.down()));
    }
    
    protected static boolean canConnectUpwardsTo(final IBlockAccess worldIn, final BlockPos pos) {
        return canConnectUpwardsTo(worldIn.getBlockState(pos));
    }
    
    protected static boolean canConnectUpwardsTo(final IBlockState state) {
        return canConnectTo(state, null);
    }
    
    protected static boolean canConnectTo(final IBlockState blockState, @Nullable final EnumFacing side) {
        final Block block = blockState.getBlock();
        if (block == Blocks.REDSTONE_WIRE) {
            return true;
        }
        if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState)) {
            final EnumFacing enumfacing = blockState.getValue((IProperty<EnumFacing>)BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }
        if (Blocks.OBSERVER == blockState.getBlock()) {
            return side == blockState.getValue((IProperty<EnumFacing>)BlockObserver.FACING);
        }
        return blockState.canProvidePower() && side != null;
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return this.canProvidePower;
    }
    
    public static int colorMultiplier(final int p_176337_0_) {
        final float f = p_176337_0_ / 15.0f;
        float f2 = f * 0.6f + 0.4f;
        if (p_176337_0_ == 0) {
            f2 = 0.3f;
        }
        float f3 = f * f * 0.7f - 0.5f;
        float f4 = f * f * 0.6f - 0.7f;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        final int i = MathHelper.clamp((int)(f2 * 255.0f), 0, 255);
        final int j = MathHelper.clamp((int)(f3 * 255.0f), 0, 255);
        final int k = MathHelper.clamp((int)(f4 * 255.0f), 0, 255);
        return 0xFF000000 | i << 16 | j << 8 | k;
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final int i = stateIn.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
        if (i != 0) {
            final double d0 = pos.getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
            final double d2 = pos.getY() + 0.0625f;
            final double d3 = pos.getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
            final float f = i / 15.0f;
            final float f2 = f * 0.6f + 0.4f;
            final float f3 = Math.max(0.0f, f * f * 0.7f - 0.5f);
            final float f4 = Math.max(0.0f, f * f * 0.6f - 0.7f);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d2, d3, f2, f3, f4, new int[0]);
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.REDSTONE);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneWire.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER);
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                return state.withProperty(BlockRedstoneWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.SOUTH)).withProperty(BlockRedstoneWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.WEST)).withProperty(BlockRedstoneWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.NORTH)).withProperty(BlockRedstoneWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return state.withProperty(BlockRedstoneWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.EAST)).withProperty(BlockRedstoneWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.SOUTH)).withProperty(BlockRedstoneWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.WEST)).withProperty(BlockRedstoneWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.NORTH));
            }
            case CLOCKWISE_90: {
                return state.withProperty(BlockRedstoneWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.WEST)).withProperty(BlockRedstoneWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.NORTH)).withProperty(BlockRedstoneWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.EAST)).withProperty(BlockRedstoneWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.SOUTH));
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
                return state.withProperty(BlockRedstoneWire.NORTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.SOUTH)).withProperty(BlockRedstoneWire.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.NORTH));
            }
            case FRONT_BACK: {
                return state.withProperty(BlockRedstoneWire.EAST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.WEST)).withProperty(BlockRedstoneWire.WEST, (Comparable)state.getValue((IProperty<V>)BlockRedstoneWire.EAST));
            }
            default: {
                return super.withMirror(state, mirrorIn);
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockRedstoneWire.NORTH, BlockRedstoneWire.EAST, BlockRedstoneWire.SOUTH, BlockRedstoneWire.WEST, BlockRedstoneWire.POWER });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
        EAST = PropertyEnum.create("east", EnumAttachPosition.class);
        SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
        WEST = PropertyEnum.create("west", EnumAttachPosition.class);
        POWER = PropertyInteger.create("power", 0, 15);
        REDSTONE_WIRE_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 0.8125), new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 1.0), new AxisAlignedBB(0.0, 0.0, 0.1875, 0.8125, 0.0625, 0.8125), new AxisAlignedBB(0.0, 0.0, 0.1875, 0.8125, 0.0625, 1.0), new AxisAlignedBB(0.1875, 0.0, 0.0, 0.8125, 0.0625, 0.8125), new AxisAlignedBB(0.1875, 0.0, 0.0, 0.8125, 0.0625, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 0.8125, 0.0625, 0.8125), new AxisAlignedBB(0.0, 0.0, 0.0, 0.8125, 0.0625, 1.0), new AxisAlignedBB(0.1875, 0.0, 0.1875, 1.0, 0.0625, 0.8125), new AxisAlignedBB(0.1875, 0.0, 0.1875, 1.0, 0.0625, 1.0), new AxisAlignedBB(0.0, 0.0, 0.1875, 1.0, 0.0625, 0.8125), new AxisAlignedBB(0.0, 0.0, 0.1875, 1.0, 0.0625, 1.0), new AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 0.0625, 0.8125), new AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 0.0625, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 0.8125), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0) };
    }
    
    enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"), 
        SIDE("side"), 
        NONE("none");
        
        private final String name;
        
        private EnumAttachPosition(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
