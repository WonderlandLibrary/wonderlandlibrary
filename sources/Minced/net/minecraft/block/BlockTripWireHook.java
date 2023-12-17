// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import com.google.common.base.MoreObjects;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTripWireHook extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyBool POWERED;
    public static final PropertyBool ATTACHED;
    protected static final AxisAlignedBB HOOK_NORTH_AABB;
    protected static final AxisAlignedBB HOOK_SOUTH_AABB;
    protected static final AxisAlignedBB HOOK_WEST_AABB;
    protected static final AxisAlignedBB HOOK_EAST_AABB;
    
    public BlockTripWireHook() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, false).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING)) {
            default: {
                return BlockTripWireHook.HOOK_EAST_AABB;
            }
            case WEST: {
                return BlockTripWireHook.HOOK_WEST_AABB;
            }
            case SOUTH: {
                return BlockTripWireHook.HOOK_SOUTH_AABB;
            }
            case NORTH: {
                return BlockTripWireHook.HOOK_NORTH_AABB;
            }
        }
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockTripWireHook.NULL_AABB;
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        final EnumFacing enumfacing = side.getOpposite();
        final BlockPos blockpos = pos.offset(enumfacing);
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final boolean flag = Block.isExceptBlockForAttachWithPiston(iblockstate.getBlock());
        return !flag && side.getAxis().isHorizontal() && iblockstate.getBlockFaceShape(worldIn, blockpos, side) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.canPlaceBlockOnSide(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, false).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, false);
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, facing);
        }
        return iblockstate;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        this.calculateState(worldIn, pos, state, false, false, -1, null);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (blockIn != this && this.checkForDrop(worldIn, pos, state)) {
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING);
            if (!this.canPlaceBlockOnSide(worldIn, pos, enumfacing)) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    public void calculateState(final World worldIn, final BlockPos pos, final IBlockState hookState, final boolean p_176260_4_, final boolean p_176260_5_, final int p_176260_6_, @Nullable final IBlockState p_176260_7_) {
        final EnumFacing enumfacing = hookState.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING);
        final boolean flag = hookState.getValue((IProperty<Boolean>)BlockTripWireHook.ATTACHED);
        final boolean flag2 = hookState.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED);
        boolean flag3 = !p_176260_4_;
        boolean flag4 = false;
        int i = 0;
        final IBlockState[] aiblockstate = new IBlockState[42];
        int j = 1;
        while (j < 42) {
            final BlockPos blockpos = pos.offset(enumfacing, j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK) {
                if (iblockstate.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == enumfacing.getOpposite()) {
                    i = j;
                    break;
                }
                break;
            }
            else {
                if (iblockstate.getBlock() != Blocks.TRIPWIRE && j != p_176260_6_) {
                    aiblockstate[j] = null;
                    flag3 = false;
                }
                else {
                    if (j == p_176260_6_) {
                        iblockstate = (IBlockState)MoreObjects.firstNonNull((Object)p_176260_7_, (Object)iblockstate);
                    }
                    final boolean flag5 = !iblockstate.getValue((IProperty<Boolean>)BlockTripWire.DISARMED);
                    final boolean flag6 = iblockstate.getValue((IProperty<Boolean>)BlockTripWire.POWERED);
                    flag4 |= (flag5 && flag6);
                    aiblockstate[j] = iblockstate;
                    if (j == p_176260_6_) {
                        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                        flag3 &= flag5;
                    }
                }
                ++j;
            }
        }
        flag3 &= (i > 1);
        flag4 &= flag3;
        final IBlockState iblockstate2 = this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, flag3).withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, flag4);
        if (i > 0) {
            final BlockPos blockpos2 = pos.offset(enumfacing, i);
            final EnumFacing enumfacing2 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, enumfacing2), 3);
            this.notifyNeighbors(worldIn, blockpos2, enumfacing2);
            this.playSound(worldIn, blockpos2, flag3, flag4, flag, flag2);
        }
        this.playSound(worldIn, pos, flag3, flag4, flag, flag2);
        if (!p_176260_4_) {
            worldIn.setBlockState(pos, iblockstate2.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, enumfacing), 3);
            if (p_176260_5_) {
                this.notifyNeighbors(worldIn, pos, enumfacing);
            }
        }
        if (flag != flag3) {
            for (int k = 1; k < i; ++k) {
                final BlockPos blockpos3 = pos.offset(enumfacing, k);
                final IBlockState iblockstate3 = aiblockstate[k];
                if (iblockstate3 != null && worldIn.getBlockState(blockpos3).getMaterial() != Material.AIR) {
                    worldIn.setBlockState(blockpos3, iblockstate3.withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, flag3), 3);
                }
            }
        }
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.calculateState(worldIn, pos, state, false, true, -1, null);
    }
    
    private void playSound(final World worldIn, final BlockPos pos, final boolean p_180694_3_, final boolean p_180694_4_, final boolean p_180694_5_, final boolean p_180694_6_) {
        if (p_180694_4_ && !p_180694_6_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4f, 0.6f);
        }
        else if (!p_180694_4_ && p_180694_6_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4f, 0.5f);
        }
        else if (p_180694_3_ && !p_180694_5_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4f, 0.7f);
        }
        else if (!p_180694_3_ && p_180694_5_) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4f, 1.2f / (worldIn.rand.nextFloat() * 0.2f + 0.9f));
        }
    }
    
    private void notifyNeighbors(final World worldIn, final BlockPos pos, final EnumFacing side) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.offset(side.getOpposite()), this, false);
    }
    
    private boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final boolean flag = state.getValue((IProperty<Boolean>)BlockTripWireHook.ATTACHED);
        final boolean flag2 = state.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED);
        if (flag || flag2) {
            this.calculateState(worldIn, pos, state, true, false, -1, null);
        }
        if (flag2) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.offset(state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING).getOpposite()), this, false);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED) ? 15 : 0;
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!blockState.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED)) {
            return 0;
        }
        return (blockState.getValue((IProperty<Comparable>)BlockTripWireHook.FACING) == side) ? 15 : 0;
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, EnumFacing.byHorizontalIndex(meta & 0x3)).withProperty((IProperty<Comparable>)BlockTripWireHook.POWERED, (meta & 0x8) > 0).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, (meta & 0x4) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockTripWireHook.POWERED)) {
            i |= 0x8;
        }
        if (state.getValue((IProperty<Boolean>)BlockTripWireHook.ATTACHED)) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockTripWireHook.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockTripWireHook.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockTripWireHook.FACING, BlockTripWireHook.POWERED, BlockTripWireHook.ATTACHED });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockHorizontal.FACING;
        POWERED = PropertyBool.create("powered");
        ATTACHED = PropertyBool.create("attached");
        HOOK_NORTH_AABB = new AxisAlignedBB(0.3125, 0.0, 0.625, 0.6875, 0.625, 1.0);
        HOOK_SOUTH_AABB = new AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.625, 0.375);
        HOOK_WEST_AABB = new AxisAlignedBB(0.625, 0.0, 0.3125, 1.0, 0.625, 0.6875);
        HOOK_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.3125, 0.375, 0.625, 0.6875);
    }
}
