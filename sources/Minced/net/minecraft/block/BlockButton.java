// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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

public abstract class BlockButton extends BlockDirectional
{
    public static final PropertyBool POWERED;
    protected static final AxisAlignedBB AABB_DOWN_OFF;
    protected static final AxisAlignedBB AABB_UP_OFF;
    protected static final AxisAlignedBB AABB_NORTH_OFF;
    protected static final AxisAlignedBB AABB_SOUTH_OFF;
    protected static final AxisAlignedBB AABB_WEST_OFF;
    protected static final AxisAlignedBB AABB_EAST_OFF;
    protected static final AxisAlignedBB AABB_DOWN_ON;
    protected static final AxisAlignedBB AABB_UP_ON;
    protected static final AxisAlignedBB AABB_NORTH_ON;
    protected static final AxisAlignedBB AABB_SOUTH_ON;
    protected static final AxisAlignedBB AABB_WEST_ON;
    protected static final AxisAlignedBB AABB_EAST_ON;
    private final boolean wooden;
    
    protected BlockButton(final boolean wooden) {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockButton.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockButton.POWERED, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.wooden = wooden;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockButton.NULL_AABB;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return this.wooden ? 30 : 20;
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
        return canPlaceBlock(worldIn, pos, side);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            if (canPlaceBlock(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    
    protected static boolean canPlaceBlock(final World worldIn, final BlockPos pos, final EnumFacing direction) {
        final BlockPos blockpos = pos.offset(direction.getOpposite());
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final boolean flag = iblockstate.getBlockFaceShape(worldIn, blockpos, direction) == BlockFaceShape.SOLID;
        final Block block = iblockstate.getBlock();
        if (direction == EnumFacing.UP) {
            return block == Blocks.HOPPER || (!Block.isExceptionBlockForAttaching(block) && flag);
        }
        return !Block.isExceptBlockForAttachWithPiston(block) && flag;
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return canPlaceBlock(worldIn, pos, facing) ? this.getDefaultState().withProperty((IProperty<Comparable>)BlockButton.FACING, facing).withProperty((IProperty<Comparable>)BlockButton.POWERED, false) : this.getDefaultState().withProperty((IProperty<Comparable>)BlockButton.FACING, EnumFacing.DOWN).withProperty((IProperty<Comparable>)BlockButton.POWERED, false);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (this.checkForDrop(worldIn, pos, state) && !canPlaceBlock(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING))) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean checkForDrop(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.canPlaceBlockAt(worldIn, pos)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        return false;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockButton.FACING);
        final boolean flag = state.getValue((IProperty<Boolean>)BlockButton.POWERED);
        switch (enumfacing) {
            case EAST: {
                return flag ? BlockButton.AABB_EAST_ON : BlockButton.AABB_EAST_OFF;
            }
            case WEST: {
                return flag ? BlockButton.AABB_WEST_ON : BlockButton.AABB_WEST_OFF;
            }
            case SOUTH: {
                return flag ? BlockButton.AABB_SOUTH_ON : BlockButton.AABB_SOUTH_OFF;
            }
            default: {
                return flag ? BlockButton.AABB_NORTH_ON : BlockButton.AABB_NORTH_OFF;
            }
            case UP: {
                return flag ? BlockButton.AABB_UP_ON : BlockButton.AABB_UP_OFF;
            }
            case DOWN: {
                return flag ? BlockButton.AABB_DOWN_ON : BlockButton.AABB_DOWN_OFF;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            return true;
        }
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, true), 3);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        this.playClickSound(playerIn, worldIn, pos);
        this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        return true;
    }
    
    protected abstract void playClickSound(final EntityPlayer p0, final World p1, final BlockPos p2);
    
    protected abstract void playReleaseSound(final World p0, final BlockPos p1);
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    @Deprecated
    public int getWeakPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return blockState.getValue((IProperty<Boolean>)BlockButton.POWERED) ? 15 : 0;
    }
    
    @Override
    @Deprecated
    public int getStrongPower(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        if (!blockState.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            return 0;
        }
        return (blockState.getValue((IProperty<Comparable>)BlockButton.FACING) == side) ? 15 : 0;
    }
    
    @Override
    @Deprecated
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }
    
    @Override
    public void randomTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random random) {
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            if (this.wooden) {
                this.checkPressed(state, worldIn, pos);
            }
            else {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, false));
                this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
                this.playReleaseSound(worldIn, pos);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
            }
        }
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && this.wooden && !state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            this.checkPressed(state, worldIn, pos);
        }
    }
    
    private void checkPressed(final IBlockState state, final World worldIn, final BlockPos pos) {
        final List<? extends Entity> list = worldIn.getEntitiesWithinAABB((Class<? extends Entity>)EntityArrow.class, state.getBoundingBox(worldIn, pos).offset(pos));
        final boolean flag = !list.isEmpty();
        final boolean flag2 = state.getValue((IProperty<Boolean>)BlockButton.POWERED);
        if (flag && !flag2) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, true));
            this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            this.playClickSound(null, worldIn, pos);
        }
        if (!flag && flag2) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockButton.POWERED, false));
            this.notifyNeighbors(worldIn, pos, state.getValue((IProperty<EnumFacing>)BlockButton.FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            this.playReleaseSound(worldIn, pos);
        }
        if (flag) {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }
    
    private void notifyNeighbors(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this, false);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = null;
        switch (meta & 0x7) {
            case 0: {
                enumfacing = EnumFacing.DOWN;
                break;
            }
            case 1: {
                enumfacing = EnumFacing.EAST;
                break;
            }
            case 2: {
                enumfacing = EnumFacing.WEST;
                break;
            }
            case 3: {
                enumfacing = EnumFacing.SOUTH;
                break;
            }
            case 4: {
                enumfacing = EnumFacing.NORTH;
                break;
            }
            default: {
                enumfacing = EnumFacing.UP;
                break;
            }
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockButton.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockButton.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        switch (state.getValue((IProperty<EnumFacing>)BlockButton.FACING)) {
            case EAST: {
                i = 1;
                break;
            }
            case WEST: {
                i = 2;
                break;
            }
            case SOUTH: {
                i = 3;
                break;
            }
            case NORTH: {
                i = 4;
                break;
            }
            default: {
                i = 5;
                break;
            }
            case DOWN: {
                i = 0;
                break;
            }
        }
        if (state.getValue((IProperty<Boolean>)BlockButton.POWERED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockButton.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockButton.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockButton.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockButton.FACING, BlockButton.POWERED });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        POWERED = PropertyBool.create("powered");
        AABB_DOWN_OFF = new AxisAlignedBB(0.3125, 0.875, 0.375, 0.6875, 1.0, 0.625);
        AABB_UP_OFF = new AxisAlignedBB(0.3125, 0.0, 0.375, 0.6875, 0.125, 0.625);
        AABB_NORTH_OFF = new AxisAlignedBB(0.3125, 0.375, 0.875, 0.6875, 0.625, 1.0);
        AABB_SOUTH_OFF = new AxisAlignedBB(0.3125, 0.375, 0.0, 0.6875, 0.625, 0.125);
        AABB_WEST_OFF = new AxisAlignedBB(0.875, 0.375, 0.3125, 1.0, 0.625, 0.6875);
        AABB_EAST_OFF = new AxisAlignedBB(0.0, 0.375, 0.3125, 0.125, 0.625, 0.6875);
        AABB_DOWN_ON = new AxisAlignedBB(0.3125, 0.9375, 0.375, 0.6875, 1.0, 0.625);
        AABB_UP_ON = new AxisAlignedBB(0.3125, 0.0, 0.375, 0.6875, 0.0625, 0.625);
        AABB_NORTH_ON = new AxisAlignedBB(0.3125, 0.375, 0.9375, 0.6875, 0.625, 1.0);
        AABB_SOUTH_ON = new AxisAlignedBB(0.3125, 0.375, 0.0, 0.6875, 0.625, 0.0625);
        AABB_WEST_ON = new AxisAlignedBB(0.9375, 0.375, 0.3125, 1.0, 0.625, 0.6875);
        AABB_EAST_ON = new AxisAlignedBB(0.0, 0.375, 0.3125, 0.0625, 0.625, 0.6875);
    }
}
