/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase
extends BlockDirectional {
    public static final PropertyBool EXTENDED = PropertyBool.create("extended");
    protected static final AxisAlignedBB PISTON_BASE_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_BASE_WEST_AABB = new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_BASE_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75);
    protected static final AxisAlignedBB PISTON_BASE_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_BASE_UP_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
    protected static final AxisAlignedBB PISTON_BASE_DOWN_AABB = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
    private final boolean isSticky;

    public BlockPistonBase(boolean isSticky) {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, false));
        this.isSticky = isSticky;
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5f);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public boolean causesSuffocation(IBlockState p_176214_1_) {
        return p_176214_1_.getValue(EXTENDED) == false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(EXTENDED).booleanValue()) {
            switch (state.getValue(FACING)) {
                case DOWN: {
                    return PISTON_BASE_DOWN_AABB;
                }
                default: {
                    return PISTON_BASE_UP_AABB;
                }
                case NORTH: {
                    return PISTON_BASE_NORTH_AABB;
                }
                case SOUTH: {
                    return PISTON_BASE_SOUTH_AABB;
                }
                case WEST: {
                    return PISTON_BASE_WEST_AABB;
                }
                case EAST: 
            }
            return PISTON_BASE_EAST_AABB;
        }
        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return state.getValue(EXTENDED) == false || state.getValue(FACING) == EnumFacing.DOWN;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        BlockPistonBase.addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.func_190914_a(pos, placer)), 2);
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            this.checkForMove(worldIn, pos, state);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.func_190914_a(pos, placer)).withProperty(EXTENDED, false);
    }

    private void checkForMove(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING);
        boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
        if (flag && !state.getValue(EXTENDED).booleanValue()) {
            if (new BlockPistonStructureHelper(worldIn, pos, enumfacing, true).canMove()) {
                worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
            }
        } else if (!flag && state.getValue(EXTENDED).booleanValue()) {
            worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
        }
    }

    private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing == facing || !worldIn.isSidePowered(pos.offset(enumfacing), enumfacing)) continue;
            return true;
        }
        if (worldIn.isSidePowered(pos, EnumFacing.DOWN)) {
            return true;
        }
        BlockPos blockpos = pos.up();
        for (EnumFacing enumfacing1 : EnumFacing.values()) {
            if (enumfacing1 == EnumFacing.DOWN || !worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        EnumFacing enumfacing = state.getValue(FACING);
        if (!worldIn.isRemote) {
            boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
            if (flag && id == 1) {
                worldIn.setBlockState(pos, state.withProperty(EXTENDED, true), 2);
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
            worldIn.setBlockState(pos, state.withProperty(EXTENDED, true), 3);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5f, worldIn.rand.nextFloat() * 0.25f + 0.6f);
        } else if (id == 1) {
            TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
            if (tileentity1 instanceof TileEntityPiston) {
                ((TileEntityPiston)tileentity1).clearPistonTileEntity();
            }
            worldIn.setBlockState(pos, Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING, enumfacing).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.createTilePiston(this.getStateFromMeta(param), enumfacing, false, true));
            if (this.isSticky) {
                TileEntityPiston tileentitypiston;
                TileEntity tileentity;
                BlockPos blockpos = pos.add(enumfacing.getXOffset() * 2, enumfacing.getYOffset() * 2, enumfacing.getZOffset() * 2);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                boolean flag1 = false;
                if (block == Blocks.PISTON_EXTENSION && (tileentity = worldIn.getTileEntity(blockpos)) instanceof TileEntityPiston && (tileentitypiston = (TileEntityPiston)tileentity).getFacing() == enumfacing && tileentitypiston.isExtending()) {
                    tileentitypiston.clearPistonTileEntity();
                    flag1 = true;
                }
                if (!flag1 && iblockstate.getMaterial() != Material.AIR && BlockPistonBase.canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false, enumfacing) && (iblockstate.getMobilityFlag() == EnumPushReaction.NORMAL || block == Blocks.PISTON || block == Blocks.STICKY_PISTON)) {
                    this.doMove(worldIn, pos, enumfacing, false);
                }
            } else {
                worldIn.setBlockToAir(pos.offset(enumfacing));
            }
            worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5f, worldIn.rand.nextFloat() * 0.15f + 0.6f);
        }
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nullable
    public static EnumFacing getFacing(int meta) {
        int i = meta & 7;
        return i > 5 ? null : EnumFacing.getFront(i);
    }

    public static boolean canPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing p_185646_5_) {
        Block block = blockStateIn.getBlock();
        if (block == Blocks.OBSIDIAN) {
            return false;
        }
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        if (pos.getY() >= 0 && (facing != EnumFacing.DOWN || pos.getY() != 0)) {
            if (pos.getY() <= worldIn.getHeight() - 1 && (facing != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1)) {
                if (block != Blocks.PISTON && block != Blocks.STICKY_PISTON) {
                    if (blockStateIn.getBlockHardness(worldIn, pos) == -1.0f) {
                        return false;
                    }
                    switch (blockStateIn.getMobilityFlag()) {
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
                } else if (blockStateIn.getValue(EXTENDED).booleanValue()) {
                    return false;
                }
                return !block.hasTileEntity();
            }
            return false;
        }
        return false;
    }

    private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending) {
        BlockPistonStructureHelper blockpistonstructurehelper;
        if (!extending) {
            worldIn.setBlockToAir(pos.offset(direction));
        }
        if (!(blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending)).canMove()) {
            return false;
        }
        List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
        ArrayList<IBlockState> list1 = Lists.newArrayList();
        for (int i = 0; i < list.size(); ++i) {
            BlockPos blockpos = list.get(i);
            list1.add(worldIn.getBlockState(blockpos).getActualState(worldIn, blockpos));
        }
        List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
        int k = list.size() + list2.size();
        IBlockState[] aiblockstate = new IBlockState[k];
        EnumFacing enumfacing = extending ? direction : direction.getOpposite();
        for (int j = list2.size() - 1; j >= 0; --j) {
            BlockPos blockpos1 = list2.get(j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos1);
            iblockstate.getBlock().dropBlockAsItem(worldIn, blockpos1, iblockstate, 0);
            worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 4);
            aiblockstate[--k] = iblockstate;
        }
        for (int l = list.size() - 1; l >= 0; --l) {
            BlockPos blockpos3 = list.get(l);
            IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
            worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
            blockpos3 = blockpos3.offset(enumfacing);
            worldIn.setBlockState(blockpos3, Blocks.PISTON_EXTENSION.getDefaultState().withProperty(FACING, direction), 4);
            worldIn.setTileEntity(blockpos3, BlockPistonMoving.createTilePiston((IBlockState)list1.get(l), direction, extending, false));
            aiblockstate[--k] = iblockstate2;
        }
        BlockPos blockpos2 = pos.offset(direction);
        if (extending) {
            BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
            IBlockState iblockstate3 = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
            IBlockState iblockstate1 = Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
            worldIn.setBlockState(blockpos2, iblockstate1, 4);
            worldIn.setTileEntity(blockpos2, BlockPistonMoving.createTilePiston(iblockstate3, direction, true, true));
        }
        for (int i1 = list2.size() - 1; i1 >= 0; --i1) {
            worldIn.notifyNeighborsOfStateChange(list2.get(i1), aiblockstate[k++].getBlock(), false);
        }
        for (int j1 = list.size() - 1; j1 >= 0; --j1) {
            worldIn.notifyNeighborsOfStateChange(list.get(j1), aiblockstate[k++].getBlock(), false);
        }
        if (extending) {
            worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.PISTON_HEAD, false);
        }
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacing(meta)).withProperty(EXTENDED, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getIndex();
        if (state.getValue(EXTENDED).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, EXTENDED);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return (p_193383_2_ = this.getActualState(p_193383_2_, p_193383_1_, p_193383_3_)).getValue(FACING) != p_193383_4_.getOpposite() && p_193383_2_.getValue(EXTENDED) != false ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}

