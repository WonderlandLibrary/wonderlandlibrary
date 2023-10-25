/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension
extends BlockDirectional {
    public static final PropertyEnum<EnumPistonType> TYPE = PropertyEnum.create("type", EnumPistonType.class);
    public static final PropertyBool SHORT = PropertyBool.create("short");
    protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75, 0.0, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.25, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.75, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.25);
    protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
    protected static final AxisAlignedBB UP_ARM_AABB = new AxisAlignedBB(0.375, -0.25, 0.375, 0.625, 0.75, 0.625);
    protected static final AxisAlignedBB DOWN_ARM_AABB = new AxisAlignedBB(0.375, 0.25, 0.375, 0.625, 1.25, 0.625);
    protected static final AxisAlignedBB SOUTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, -0.25, 0.625, 0.625, 0.75);
    protected static final AxisAlignedBB NORTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.25, 0.625, 0.625, 1.25);
    protected static final AxisAlignedBB EAST_ARM_AABB = new AxisAlignedBB(-0.25, 0.375, 0.375, 0.75, 0.625, 0.625);
    protected static final AxisAlignedBB WEST_ARM_AABB = new AxisAlignedBB(0.25, 0.375, 0.375, 1.25, 0.625, 0.625);
    protected static final AxisAlignedBB field_190964_J = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625);
    protected static final AxisAlignedBB field_190965_K = new AxisAlignedBB(0.375, 0.25, 0.375, 0.625, 1.0, 0.625);
    protected static final AxisAlignedBB field_190966_L = new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 0.75);
    protected static final AxisAlignedBB field_190967_M = new AxisAlignedBB(0.375, 0.375, 0.25, 0.625, 0.625, 1.0);
    protected static final AxisAlignedBB field_190968_N = new AxisAlignedBB(0.0, 0.375, 0.375, 0.75, 0.625, 0.625);
    protected static final AxisAlignedBB field_190969_O = new AxisAlignedBB(0.25, 0.375, 0.375, 1.0, 0.625, 0.625);

    public BlockPistonExtension() {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, EnumPistonType.DEFAULT).withProperty(SHORT, false));
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            default: {
                return PISTON_EXTENSION_DOWN_AABB;
            }
            case UP: {
                return PISTON_EXTENSION_UP_AABB;
            }
            case NORTH: {
                return PISTON_EXTENSION_NORTH_AABB;
            }
            case SOUTH: {
                return PISTON_EXTENSION_SOUTH_AABB;
            }
            case WEST: {
                return PISTON_EXTENSION_WEST_AABB;
            }
            case EAST: 
        }
        return PISTON_EXTENSION_EAST_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        BlockPistonExtension.addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
        BlockPistonExtension.addCollisionBoxToList(pos, entityBox, collidingBoxes, this.getArmShape(state));
    }

    private AxisAlignedBB getArmShape(IBlockState state) {
        boolean flag = state.getValue(SHORT);
        switch (state.getValue(FACING)) {
            default: {
                return flag ? field_190965_K : DOWN_ARM_AABB;
            }
            case UP: {
                return flag ? field_190964_J : UP_ARM_AABB;
            }
            case NORTH: {
                return flag ? field_190967_M : NORTH_ARM_AABB;
            }
            case SOUTH: {
                return flag ? field_190966_L : SOUTH_ARM_AABB;
            }
            case WEST: {
                return flag ? field_190969_O : WEST_ARM_AABB;
            }
            case EAST: 
        }
        return flag ? field_190968_N : EAST_ARM_AABB;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return state.getValue(FACING) == EnumFacing.UP;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos blockpos;
        Block block;
        if (player.capabilities.isCreativeMode && ((block = worldIn.getBlockState(blockpos = pos.offset(state.getValue(FACING).getOpposite())).getBlock()) == Blocks.PISTON || block == Blocks.STICKY_PISTON)) {
            worldIn.setBlockToAir(blockpos);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        pos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if ((iblockstate.getBlock() == Blocks.PISTON || iblockstate.getBlock() == Blocks.STICKY_PISTON) && iblockstate.getValue(BlockPistonBase.EXTENDED).booleanValue()) {
            iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() != Blocks.PISTON && iblockstate.getBlock() != Blocks.STICKY_PISTON) {
            worldIn.setBlockToAir(pos);
        } else {
            iblockstate.neighborChanged(worldIn, blockpos, blockIn, p_189540_5_);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Nullable
    public static EnumFacing getFacing(int meta) {
        int i = meta & 7;
        return i > 5 ? null : EnumFacing.getFront(i);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(state.getValue(TYPE) == EnumPistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(meta)).withProperty(TYPE, (meta & 8) > 0 ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getIndex();
        if (state.getValue(TYPE) == EnumPistonType.STICKY) {
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
        return new BlockStateContainer((Block)this, FACING, TYPE, SHORT);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return p_193383_4_ == p_193383_2_.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("normal"),
        STICKY("sticky");

        private final String VARIANT;

        private EnumPistonType(String name) {
            this.VARIANT = name;
        }

        public String toString() {
            return this.VARIANT;
        }

        @Override
        public String getName() {
            return this.VARIANT;
        }
    }
}

