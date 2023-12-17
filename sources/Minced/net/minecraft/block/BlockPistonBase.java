// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;

public class BlockPistonBase extends BlockDirectional
{
    public static final PropertyBool EXTENDED;
    protected static final AxisAlignedBB PISTON_BASE_EAST_AABB;
    protected static final AxisAlignedBB PISTON_BASE_WEST_AABB;
    protected static final AxisAlignedBB PISTON_BASE_SOUTH_AABB;
    protected static final AxisAlignedBB PISTON_BASE_NORTH_AABB;
    protected static final AxisAlignedBB PISTON_BASE_UP_AABB;
    protected static final AxisAlignedBB PISTON_BASE_DOWN_AABB;
    private final boolean isSticky;
    
    public BlockPistonBase(final boolean isSticky) {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, false));
        this.isSticky = isSticky;
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5f);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    @Deprecated
    public boolean causesSuffocation(final IBlockState state) {
        return !state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        if (!state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            return BlockPistonBase.FULL_BLOCK_AABB;
        }
        switch (state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING)) {
            case DOWN: {
                return BlockPistonBase.PISTON_BASE_DOWN_AABB;
            }
            default: {
                return BlockPistonBase.PISTON_BASE_UP_AABB;
            }
            case NORTH: {
                return BlockPistonBase.PISTON_BASE_NORTH_AABB;
            }
            case SOUTH: {
                return BlockPistonBase.PISTON_BASE_SOUTH_AABB;
            }
            case WEST: {
                return BlockPistonBase.PISTON_BASE_WEST_AABB;
            }
            case EAST: {
                return BlockPistonBase.PISTON_BASE_EAST_AABB;
            }
        }
    }
    
    @Override
    @Deprecated
    public boolean isTopSolid(final IBlockState state) {
        return !state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED) || state.getValue((IProperty<Comparable>)BlockPistonBase.FACING) == EnumFacing.DOWN;
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, false);
    }
    
    private void checkForMove(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING);
        final boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
        if (flag && !state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            if (new BlockPistonStructureHelper(worldIn, pos, enumfacing, true).canMove()) {
                worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
            }
        }
        else if (!flag && state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
        }
    }
    
    private boolean shouldBeExtended(final World worldIn, final BlockPos pos, final EnumFacing facing) {
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing)) {
                return true;
            }
        }
        if (worldIn.isSidePowered(pos, EnumFacing.DOWN)) {
            return true;
        }
        final BlockPos blockpos = pos.up();
        for (final EnumFacing enumfacing2 : EnumFacing.values()) {
            if (enumfacing2 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing2), enumfacing2)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    @Deprecated
    public boolean eventReceived(final IBlockState state, final World worldIn, final BlockPos pos, final int id, final int param) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING);
        if (!worldIn.isRemote) {
            final boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
            if (flag && id == 1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true), 2);
                return false;
            }
            if (!flag && id == 0) {
                return false;
            }
        }
        if (id == 0) {
            if (!this.doMove(worldIn, pos, enumfacing, true)) {
                return false;
            }
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true), 3);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f, worldIn.rand.nextFloat() * 0.25f + 0.6f);
        }
        else if (id == 1) {
            final TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
            if (tileentity1 instanceof TileEntityPiston) {
                ((TileEntityPiston)tileentity1).clearPistonTileEntity();
            }
            worldIn.setBlockState(pos, Blocks.PISTON_EXTENSION.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, enumfacing).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.createTilePiston(this.getStateFromMeta(param), enumfacing, false, true));
            if (this.isSticky) {
                final BlockPos blockpos = pos.add(enumfacing.getXOffset() * 2, enumfacing.getYOffset() * 2, enumfacing.getZOffset() * 2);
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                boolean flag2 = false;
                if (block == Blocks.PISTON_EXTENSION) {
                    final TileEntity tileentity2 = worldIn.getTileEntity(blockpos);
                    if (tileentity2 instanceof TileEntityPiston) {
                        final TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity2;
                        if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
                            tileentitypiston.clearPistonTileEntity();
                            flag2 = true;
                        }
                    }
                }
                if (!flag2 && iblockstate.getMaterial() != Material.AIR && canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false, enumfacing) && (iblockstate.getPushReaction() == EnumPushReaction.NORMAL || block == Blocks.PISTON || block == Blocks.STICKY_PISTON)) {
                    this.doMove(worldIn, pos, enumfacing, false);
                }
            }
            else {
                worldIn.setBlockToAir(pos.offset(enumfacing));
            }
            worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5f, worldIn.rand.nextFloat() * 0.15f + 0.6f);
        }
        return true;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Nullable
    public static EnumFacing getFacing(final int meta) {
        final int i = meta & 0x7;
        return (i > 5) ? null : EnumFacing.byIndex(i);
    }
    
    public static boolean canPush(final IBlockState blockStateIn, final World worldIn, final BlockPos pos, final EnumFacing facing, final boolean destroyBlocks, final EnumFacing p_185646_5_) {
        final Block block = blockStateIn.getBlock();
        if (block == Blocks.OBSIDIAN) {
            return false;
        }
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        if (pos.getY() < 0 || (facing == EnumFacing.DOWN && pos.getY() == 0)) {
            return false;
        }
        if (pos.getY() <= worldIn.getHeight() - 1 && (facing != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1)) {
            if (block != Blocks.PISTON && block != Blocks.STICKY_PISTON) {
                if (blockStateIn.getBlockHardness(worldIn, pos) == -1.0f) {
                    return false;
                }
                switch (blockStateIn.getPushReaction()) {
                    case BLOCK: {
                        return false;
                    }
                    case DESTROY: {
                        return destroyBlocks;
                    }
                    case PUSH_ONLY: {
                        return facing == p_185646_5_;
                    }
                }
            }
            else if (blockStateIn.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
                return false;
            }
            return !block.hasTileEntity();
        }
        return false;
    }
    
    private boolean doMove(final World worldIn, final BlockPos pos, final EnumFacing direction, final boolean extending) {
        if (!extending) {
            worldIn.setBlockToAir(pos.offset(direction));
        }
        final BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
        if (!blockpistonstructurehelper.canMove()) {
            return false;
        }
        final List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
        final List<IBlockState> list2 = (List<IBlockState>)Lists.newArrayList();
        for (int i = 0; i < list.size(); ++i) {
            final BlockPos blockpos = list.get(i);
            list2.add(worldIn.getBlockState(blockpos).getActualState(worldIn, blockpos));
        }
        final List<BlockPos> list3 = blockpistonstructurehelper.getBlocksToDestroy();
        int k = list.size() + list3.size();
        final IBlockState[] aiblockstate = new IBlockState[k];
        final EnumFacing enumfacing = extending ? direction : direction.getOpposite();
        for (int j = list3.size() - 1; j >= 0; --j) {
            final BlockPos blockpos2 = list3.get(j);
            final IBlockState iblockstate = worldIn.getBlockState(blockpos2);
            iblockstate.getBlock().dropBlockAsItem(worldIn, blockpos2, iblockstate, 0);
            worldIn.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 4);
            --k;
            aiblockstate[k] = iblockstate;
        }
        for (int l = list.size() - 1; l >= 0; --l) {
            BlockPos blockpos3 = list.get(l);
            final IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
            worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
            blockpos3 = blockpos3.offset(enumfacing);
            worldIn.setBlockState(blockpos3, Blocks.PISTON_EXTENSION.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, direction), 4);
            worldIn.setTileEntity(blockpos3, BlockPistonMoving.createTilePiston(list2.get(l), direction, extending, false));
            --k;
            aiblockstate[k] = iblockstate2;
        }
        final BlockPos blockpos4 = pos.offset(direction);
        if (extending) {
            final BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
            final IBlockState iblockstate3 = Blocks.PISTON_HEAD.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
            final IBlockState iblockstate4 = Blocks.PISTON_EXTENSION.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
            worldIn.setBlockState(blockpos4, iblockstate4, 4);
            worldIn.setTileEntity(blockpos4, BlockPistonMoving.createTilePiston(iblockstate3, direction, true, true));
        }
        for (int i2 = list3.size() - 1; i2 >= 0; --i2) {
            worldIn.notifyNeighborsOfStateChange(list3.get(i2), aiblockstate[k++].getBlock(), false);
        }
        for (int j2 = list.size() - 1; j2 >= 0; --j2) {
            worldIn.notifyNeighborsOfStateChange(list.get(j2), aiblockstate[k++].getBlock(), false);
        }
        if (extending) {
            worldIn.notifyNeighborsOfStateChange(blockpos4, Blocks.PISTON_HEAD, false);
        }
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPistonBase.FACING, getFacing(meta)).withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockPistonBase.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockPistonBase.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPistonBase.FACING, BlockPistonBase.EXTENDED });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, IBlockState state, final BlockPos pos, final EnumFacing face) {
        state = this.getActualState(state, worldIn, pos);
        return (state.getValue((IProperty<Comparable>)BlockPistonBase.FACING) != face.getOpposite() && state.getValue((IProperty<Boolean>)BlockPistonBase.EXTENDED)) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
    
    static {
        EXTENDED = PropertyBool.create("extended");
        PISTON_BASE_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0);
        PISTON_BASE_WEST_AABB = new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0);
        PISTON_BASE_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75);
        PISTON_BASE_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0);
        PISTON_BASE_UP_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
        PISTON_BASE_DOWN_AABB = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
    }
}
