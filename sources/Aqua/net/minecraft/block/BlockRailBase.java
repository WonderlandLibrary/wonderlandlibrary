package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase
extends Block {
    protected final boolean isPowered;

    public static boolean isRailBlock(World worldIn, BlockPos pos) {
        return BlockRailBase.isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.rail || block == Blocks.golden_rail || block == Blocks.detector_rail || block == Blocks.activator_rail;
    }

    protected BlockRailBase(boolean isPowered) {
        super(Material.circuits);
        this.isPowered = isPowered;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumRailDirection blockrailbase$enumraildirection;
        IBlockState iblockstate = worldIn.getBlockState(pos);
        EnumRailDirection enumRailDirection = blockrailbase$enumraildirection = iblockstate.getBlock() == this ? (EnumRailDirection)iblockstate.getValue(this.getShapeProperty()) : null;
        if (blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        }
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down());
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            state = this.func_176564_a(worldIn, pos, state, true);
            if (this.isPowered) {
                this.onNeighborBlockChange(worldIn, pos, state, this);
            }
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            EnumRailDirection blockrailbase$enumraildirection = (EnumRailDirection)state.getValue(this.getShapeProperty());
            boolean flag = false;
            if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down())) {
                flag = true;
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.east())) {
                flag = true;
            } else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.west())) {
                flag = true;
            } else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.north())) {
                flag = true;
            } else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.south())) {
                flag = true;
            }
            if (flag) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            } else {
                this.onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
            }
        }
    }

    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    }

    protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_) {
        return worldIn.isRemote ? p_176564_3_ : new Rail(this, worldIn, p_176564_2_, p_176564_3_).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
    }

    public int getMobilityFlag() {
        return 0;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (((EnumRailDirection)state.getValue(this.getShapeProperty())).isAscending()) {
            worldIn.notifyNeighborsOfStateChange(pos.up(), (Block)this);
        }
        if (this.isPowered) {
            worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), (Block)this);
        }
    }

    public abstract IProperty<EnumRailDirection> getShapeProperty();
}
