// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import com.google.common.base.Predicate;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRailDetector extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE;
    public static final PropertyBool POWERED;
    
    public BlockRailDetector() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, false).withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 20;
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && !state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            this.updatePoweredState(worldIn, pos, state);
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            this.updatePoweredState(worldIn, pos, state);
        }
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getValue((IProperty<Boolean>)BlockRailDetector.POWERED) ? 15 : 0;
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!blockState.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            return 0;
        }
        return (side == EnumFacing.UP) ? 15 : 0;
    }
    
    private void updatePoweredState(final World worldIn, final BlockPos pos, final IBlockState state) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED);
        boolean flag2 = false;
        final List<EntityMinecart> list = this.findMinecarts(worldIn, pos, EntityMinecart.class, (Predicate<Entity>[])new Predicate[0]);
        if (!list.isEmpty()) {
            flag2 = true;
        }
        if (flag2 && !flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, true), 3);
            this.updateConnectedRails(worldIn, pos, state, true);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!flag2 && flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, false), 3);
            this.updateConnectedRails(worldIn, pos, state, false);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (flag2) {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }
        worldIn.updateComparatorOutputLevel(pos, this);
    }
    
    protected void updateConnectedRails(final World worldIn, final BlockPos pos, final IBlockState state, final boolean powered) {
        final Rail blockrailbase$rail = new Rail(worldIn, pos, state);
        for (final BlockPos blockpos : blockrailbase$rail.getConnectedRails()) {
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate != null) {
                iblockstate.neighborChanged(worldIn, blockpos, iblockstate.getBlock(), pos);
            }
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.updatePoweredState(worldIn, pos, state);
    }
    
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return BlockRailDetector.SHAPE;
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        if (blockState.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            final List<EntityMinecartCommandBlock> list = this.findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, (Predicate<Entity>[])new Predicate[0]);
            if (!list.isEmpty()) {
                return list.get(0).getCommandBlockLogic().getSuccessCount();
            }
            final List<EntityMinecart> list2 = this.findMinecarts(worldIn, pos, EntityMinecart.class, EntitySelectors.HAS_INVENTORY);
            if (!list2.isEmpty()) {
                return Container.calcRedstoneFromInventory((IInventory)list2.get(0));
            }
        }
        return 0;
    }
    
    protected <T extends EntityMinecart> List<T> findMinecarts(final World worldIn, final BlockPos pos, final Class<T> clazz, final Predicate<Entity>... filter) {
        final AxisAlignedBB axisalignedbb = this.getDectectionBox(pos);
        return (filter.length != 1) ? worldIn.getEntitiesWithinAABB((Class<? extends T>)clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB((Class<? extends T>)clazz, axisalignedbb, (com.google.common.base.Predicate<? super T>)filter[0]);
    }
    
    private AxisAlignedBB getDectectionBox(final BlockPos pos) {
        final float f = 0.2f;
        return new AxisAlignedBB(pos.getX() + 0.2f, pos.getY(), pos.getZ() + 0.2f, pos.getX() + 1 - 0.2f, pos.getY() + 1 - 0.2f, pos.getZ() + 1 - 0.2f);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRailDetector.SHAPE, EnumRailDirection.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockRailDetector.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockRailDetector.SHAPE).getMetadata();
        if (state.getValue((IProperty<Boolean>)BlockRailDetector.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        Label_0406: {
            switch (rot) {
                case CLOCKWISE_180: {
                    switch (state.getValue(BlockRailDetector.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (state.getValue(BlockRailDetector.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_SOUTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_SOUTH);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (state.getValue(BlockRailDetector.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_SOUTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_SOUTH);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
            }
        }
        return state;
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        final EnumRailDirection blockrailbase$enumraildirection = state.getValue(BlockRailDetector.SHAPE);
        Label_0317: {
            switch (mirrorIn) {
                case LEFT_RIGHT: {
                    switch (blockrailbase$enumraildirection) {
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        default: {
                            return super.withMirror(state, mirrorIn);
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    switch (blockrailbase$enumraildirection) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        default: {
                            break Label_0317;
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRailDetector.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                    }
                    break;
                }
            }
        }
        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockRailDetector.SHAPE, BlockRailDetector.POWERED });
    }
    
    static {
        SHAPE = PropertyEnum.create("shape", EnumRailDirection.class, (com.google.common.base.Predicate<EnumRailDirection>)new Predicate<EnumRailDirection>() {
            public boolean apply(@Nullable final EnumRailDirection p_apply_1_) {
                return p_apply_1_ != EnumRailDirection.NORTH_EAST && p_apply_1_ != EnumRailDirection.NORTH_WEST && p_apply_1_ != EnumRailDirection.SOUTH_EAST && p_apply_1_ != EnumRailDirection.SOUTH_WEST;
            }
        });
        POWERED = PropertyBool.create("powered");
    }
}
