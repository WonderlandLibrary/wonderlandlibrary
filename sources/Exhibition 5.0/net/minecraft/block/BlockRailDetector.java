// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityMinecartCommandBlock;
import java.util.List;
import com.google.common.base.Predicate;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRailDetector extends BlockRailBase
{
    public static final PropertyEnum field_176573_b;
    public static final PropertyBool field_176574_M;
    private static final String __OBFID = "CL_00000225";
    
    public BlockRailDetector() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRailDetector.field_176574_M, false).withProperty(BlockRailDetector.field_176573_b, EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 20;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && !(boolean)state.getValue(BlockRailDetector.field_176574_M)) {
            this.func_176570_e(worldIn, pos, state);
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && (boolean)state.getValue(BlockRailDetector.field_176574_M)) {
            this.func_176570_e(worldIn, pos, state);
        }
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockRailDetector.field_176574_M) ? 15 : 0;
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockRailDetector.field_176574_M) ? ((side == EnumFacing.UP) ? 15 : 0) : 0;
    }
    
    private void func_176570_e(final World worldIn, final BlockPos p_176570_2_, final IBlockState p_176570_3_) {
        final boolean var4 = (boolean)p_176570_3_.getValue(BlockRailDetector.field_176574_M);
        boolean var5 = false;
        final List var6 = this.func_176571_a(worldIn, p_176570_2_, EntityMinecart.class, new Predicate[0]);
        if (!var6.isEmpty()) {
            var5 = true;
        }
        if (var5 && !var4) {
            worldIn.setBlockState(p_176570_2_, p_176570_3_.withProperty(BlockRailDetector.field_176574_M, true), 3);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_, this);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_.offsetDown(), this);
            worldIn.markBlockRangeForRenderUpdate(p_176570_2_, p_176570_2_);
        }
        if (!var5 && var4) {
            worldIn.setBlockState(p_176570_2_, p_176570_3_.withProperty(BlockRailDetector.field_176574_M, false), 3);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_, this);
            worldIn.notifyNeighborsOfStateChange(p_176570_2_.offsetDown(), this);
            worldIn.markBlockRangeForRenderUpdate(p_176570_2_, p_176570_2_);
        }
        if (var5) {
            worldIn.scheduleUpdate(p_176570_2_, this, this.tickRate(worldIn));
        }
        worldIn.updateComparatorOutputLevel(p_176570_2_, this);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.func_176570_e(worldIn, pos, state);
    }
    
    @Override
    public IProperty func_176560_l() {
        return BlockRailDetector.field_176573_b;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue(BlockRailDetector.field_176574_M)) {
            final List var3 = this.func_176571_a(worldIn, pos, EntityMinecartCommandBlock.class, new Predicate[0]);
            if (!var3.isEmpty()) {
                return var3.get(0).func_145822_e().getSuccessCount();
            }
            final List var4 = this.func_176571_a(worldIn, pos, EntityMinecart.class, IEntitySelector.selectInventories);
            if (!var4.isEmpty()) {
                return Container.calcRedstoneFromInventory(var4.get(0));
            }
        }
        return 0;
    }
    
    protected List func_176571_a(final World worldIn, final BlockPos p_176571_2_, final Class p_176571_3_, final Predicate... p_176571_4_) {
        final AxisAlignedBB var5 = this.func_176572_a(p_176571_2_);
        return (p_176571_4_.length != 1) ? worldIn.getEntitiesWithinAABB(p_176571_3_, var5) : worldIn.func_175647_a(p_176571_3_, var5, p_176571_4_[0]);
    }
    
    private AxisAlignedBB func_176572_a(final BlockPos p_176572_1_) {
        final float var2 = 0.2f;
        return new AxisAlignedBB(p_176572_1_.getX() + 0.2f, p_176572_1_.getY(), p_176572_1_.getZ() + 0.2f, p_176572_1_.getX() + 1 - 0.2f, p_176572_1_.getY() + 1 - 0.2f, p_176572_1_.getZ() + 1 - 0.2f);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRailDetector.field_176573_b, EnumRailDirection.func_177016_a(meta & 0x7)).withProperty(BlockRailDetector.field_176574_M, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumRailDirection)state.getValue(BlockRailDetector.field_176573_b)).func_177015_a();
        if (state.getValue(BlockRailDetector.field_176574_M)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRailDetector.field_176573_b, BlockRailDetector.field_176574_M });
    }
    
    static {
        field_176573_b = PropertyEnum.create("shape", EnumRailDirection.class, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002126";
            
            public boolean func_180344_a(final EnumRailDirection p_180344_1_) {
                return p_180344_1_ != EnumRailDirection.NORTH_EAST && p_180344_1_ != EnumRailDirection.NORTH_WEST && p_180344_1_ != EnumRailDirection.SOUTH_EAST && p_180344_1_ != EnumRailDirection.SOUTH_WEST;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_180344_a((EnumRailDirection)p_apply_1_);
            }
        });
        field_176574_M = PropertyBool.create("powered");
    }
}
