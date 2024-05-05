/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector
extends BlockRailBase {
    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>(){

        @Override
        public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_) {
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.NORTH_EAST) return false;
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.NORTH_WEST) return false;
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.SOUTH_EAST) return false;
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.SOUTH_WEST) return false;
            return true;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockRailDetector() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
    }

    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote) return;
        if (state.getValue(POWERED) != false) return;
        this.updatePoweredState(worldIn, pos, state);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;
        if (state.getValue(POWERED) == false) return;
        this.updatePoweredState(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (state.getValue(POWERED) == false) return 0;
        return 15;
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!state.getValue(POWERED).booleanValue()) {
            return 0;
        }
        if (side != EnumFacing.UP) return 0;
        return 15;
    }

    private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state) {
        boolean flag = state.getValue(POWERED);
        boolean flag1 = false;
        List<EntityMinecart> list = this.findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[0]);
        if (!list.isEmpty()) {
            flag1 = true;
        }
        if (flag1 && !flag) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, true), 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!flag1 && flag) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, false), 3);
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (flag1) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
        worldIn.updateComparatorOutputLevel(pos, this);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.updatePoweredState(worldIn, pos, state);
    }

    @Override
    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue(POWERED) == false) return 0;
        List<EntityMinecartCommandBlock> list = this.findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, new Predicate[0]);
        if (!list.isEmpty()) {
            return list.get(0).getCommandBlockLogic().getSuccessCount();
        }
        List<EntityMinecart> list1 = this.findMinecarts(worldIn, pos, EntityMinecart.class, EntitySelectors.selectInventories);
        if (list1.isEmpty()) return 0;
        return Container.calcRedstoneFromInventory((IInventory)((Object)list1.get(0)));
    }

    protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity> ... filter) {
        List<Object> list;
        AxisAlignedBB axisalignedbb = this.getDectectionBox(pos);
        if (filter.length != 1) {
            list = worldIn.getEntitiesWithinAABB(clazz, axisalignedbb);
            return list;
        }
        list = worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
        return list;
    }

    private AxisAlignedBB getDectectionBox(BlockPos pos) {
        return new AxisAlignedBB((float)pos.getX() + 0.2f, pos.getY(), (float)pos.getZ() + 0.2f, (float)(pos.getX() + 1) - 0.2f, (float)(pos.getY() + 1) - 0.2f, (float)(pos.getZ() + 1) - 0.2f);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7));
        if ((meta & 8) > 0) {
            bl = true;
            return iBlockState.withProperty(POWERED, bl);
        }
        bl = false;
        return iBlockState.withProperty(POWERED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(POWERED) == false) return i |= state.getValue(SHAPE).getMetadata();
        i |= 8;
        return i |= state.getValue(SHAPE).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SHAPE, POWERED);
    }
}

