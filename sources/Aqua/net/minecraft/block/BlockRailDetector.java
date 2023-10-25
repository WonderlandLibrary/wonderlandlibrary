package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
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
    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create((String)"shape", BlockRailBase.EnumRailDirection.class, (Predicate)new /* Unavailable Anonymous Inner Class!! */);
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");

    public BlockRailDetector() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty(SHAPE, (Comparable)BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
    }

    public int tickRate(World worldIn) {
        return 20;
    }

    public boolean canProvidePower() {
        return true;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            this.updatePoweredState(worldIn, pos, state);
        }
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            this.updatePoweredState(worldIn, pos, state);
        }
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue((IProperty)POWERED) != false ? 15 : 0;
    }

    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return (Boolean)state.getValue((IProperty)POWERED) == false ? 0 : (side == EnumFacing.UP ? 15 : 0);
    }

    private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state) {
        boolean flag = (Boolean)state.getValue((IProperty)POWERED);
        boolean flag1 = false;
        List<EntityMinecart> list = this.findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[0]);
        if (!list.isEmpty()) {
            flag1 = true;
        }
        if (flag1 && !flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)), 3);
            worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), (Block)this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (!flag1 && flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)), 3);
            worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), (Block)this);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        if (flag1) {
            worldIn.scheduleUpdate(pos, (Block)this, this.tickRate(worldIn));
        }
        worldIn.updateComparatorOutputLevel(pos, (Block)this);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.updatePoweredState(worldIn, pos, state);
    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue()) {
            List<EntityMinecartCommandBlock> list = this.findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, new Predicate[0]);
            if (!list.isEmpty()) {
                return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount();
            }
            List<EntityMinecart> list1 = this.findMinecarts(worldIn, pos, EntityMinecart.class, EntitySelectors.selectInventories);
            if (!list1.isEmpty()) {
                return Container.calcRedstoneFromInventory((IInventory)((IInventory)list1.get(0)));
            }
        }
        return 0;
    }

    protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity> ... filter) {
        AxisAlignedBB axisalignedbb = this.getDectectionBox(pos);
        return filter.length != 1 ? worldIn.getEntitiesWithinAABB(clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
    }

    private AxisAlignedBB getDectectionBox(BlockPos pos) {
        float f = 0.2f;
        return new AxisAlignedBB((double)((float)pos.getX() + 0.2f), (double)pos.getY(), (double)((float)pos.getZ() + 0.2f), (double)((float)(pos.getX() + 1) - 0.2f), (double)((float)(pos.getY() + 1) - 0.2f), (double)((float)(pos.getZ() + 1) - 0.2f));
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(SHAPE, (Comparable)BlockRailBase.EnumRailDirection.byMetadata((int)(meta & 7))).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).getMetadata();
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{SHAPE, POWERED});
    }
}
