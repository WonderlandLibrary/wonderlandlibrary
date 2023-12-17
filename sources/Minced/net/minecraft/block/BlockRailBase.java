// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.IStringSerializable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class BlockRailBase extends Block
{
    protected static final AxisAlignedBB FLAT_AABB;
    protected static final AxisAlignedBB ASCENDING_AABB;
    protected final boolean isPowered;
    
    public static boolean isRailBlock(final World worldIn, final BlockPos pos) {
        return isRailBlock(worldIn.getBlockState(pos));
    }
    
    public static boolean isRailBlock(final IBlockState state) {
        final Block block = state.getBlock();
        return block == Blocks.RAIL || block == Blocks.GOLDEN_RAIL || block == Blocks.DETECTOR_RAIL || block == Blocks.ACTIVATOR_RAIL;
    }
    
    protected BlockRailBase(final boolean isPowered) {
        super(Material.CIRCUITS);
        this.isPowered = isPowered;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockRailBase.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        final EnumRailDirection blockrailbase$enumraildirection = (state.getBlock() == this) ? state.getValue(this.getShapeProperty()) : null;
        return (blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending()) ? BlockRailBase.ASCENDING_AABB : BlockRailBase.FLAT_AABB;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid();
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            state = this.updateDir(worldIn, pos, state, true);
            if (this.isPowered) {
                state.neighborChanged(worldIn, pos, this, pos);
            }
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final EnumRailDirection blockrailbase$enumraildirection = state.getValue(this.getShapeProperty());
            boolean flag = false;
            if (!worldIn.getBlockState(pos.down()).isTopSolid()) {
                flag = true;
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_EAST && !worldIn.getBlockState(pos.east()).isTopSolid()) {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_WEST && !worldIn.getBlockState(pos.west()).isTopSolid()) {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_NORTH && !worldIn.getBlockState(pos.north()).isTopSolid()) {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_SOUTH && !worldIn.getBlockState(pos.south()).isTopSolid()) {
                flag = true;
            }
            if (flag && !worldIn.isAirBlock(pos)) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else {
                this.updateState(state, worldIn, pos, blockIn);
            }
        }
    }
    
    protected void updateState(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn) {
    }
    
    protected IBlockState updateDir(final World worldIn, final BlockPos pos, final IBlockState state, final boolean initialPlacement) {
        return worldIn.isRemote ? state : new Rail(worldIn, pos, state).place(worldIn.isBlockPowered(pos), initialPlacement).getBlockState();
    }
    
    @Override
    @Deprecated
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return EnumPushReaction.NORMAL;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (state.getValue(this.getShapeProperty()).isAscending()) {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this, false);
        }
        if (this.isPowered) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
        }
    }
    
    public abstract IProperty<EnumRailDirection> getShapeProperty();
    
    static {
        FLAT_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        ASCENDING_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    }
    
    public enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"), 
        EAST_WEST(1, "east_west"), 
        ASCENDING_EAST(2, "ascending_east"), 
        ASCENDING_WEST(3, "ascending_west"), 
        ASCENDING_NORTH(4, "ascending_north"), 
        ASCENDING_SOUTH(5, "ascending_south"), 
        SOUTH_EAST(6, "south_east"), 
        SOUTH_WEST(7, "south_west"), 
        NORTH_WEST(8, "north_west"), 
        NORTH_EAST(9, "north_east");
        
        private static final EnumRailDirection[] META_LOOKUP;
        private final int meta;
        private final String name;
        
        private EnumRailDirection(final int meta, final String name) {
            this.meta = meta;
            this.name = name;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public boolean isAscending() {
            return this == EnumRailDirection.ASCENDING_NORTH || this == EnumRailDirection.ASCENDING_EAST || this == EnumRailDirection.ASCENDING_SOUTH || this == EnumRailDirection.ASCENDING_WEST;
        }
        
        public static EnumRailDirection byMetadata(int meta) {
            if (meta < 0 || meta >= EnumRailDirection.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumRailDirection.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        static {
            META_LOOKUP = new EnumRailDirection[values().length];
            for (final EnumRailDirection blockrailbase$enumraildirection : values()) {
                EnumRailDirection.META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
            }
        }
    }
    
    public class Rail
    {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> connectedRails;
        
        public Rail(final World worldIn, final BlockPos pos, final IBlockState state) {
            this.connectedRails = (List<BlockPos>)Lists.newArrayList();
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BlockRailBase)state.getBlock();
            final EnumRailDirection blockrailbase$enumraildirection = state.getValue(this.block.getShapeProperty());
            this.isPowered = this.block.isPowered;
            this.updateConnectedRails(blockrailbase$enumraildirection);
        }
        
        public List<BlockPos> getConnectedRails() {
            return this.connectedRails;
        }
        
        private void updateConnectedRails(final EnumRailDirection railDirection) {
            this.connectedRails.clear();
            switch (railDirection) {
                case NORTH_SOUTH: {
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case EAST_WEST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east());
                    break;
                }
                case ASCENDING_EAST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east().up());
                    break;
                }
                case ASCENDING_WEST: {
                    this.connectedRails.add(this.pos.west().up());
                    this.connectedRails.add(this.pos.east());
                    break;
                }
                case ASCENDING_NORTH: {
                    this.connectedRails.add(this.pos.north().up());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case ASCENDING_SOUTH: {
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south().up());
                    break;
                }
                case SOUTH_EAST: {
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case SOUTH_WEST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.south());
                    break;
                }
                case NORTH_WEST: {
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.north());
                    break;
                }
                case NORTH_EAST: {
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.north());
                    break;
                }
            }
        }
        
        private void removeSoftConnections() {
            for (int i = 0; i < this.connectedRails.size(); ++i) {
                final Rail blockrailbase$rail = this.findRailAt(this.connectedRails.get(i));
                if (blockrailbase$rail != null && blockrailbase$rail.isConnectedToRail(this)) {
                    this.connectedRails.set(i, blockrailbase$rail.pos);
                }
                else {
                    this.connectedRails.remove(i--);
                }
            }
        }
        
        private boolean hasRailAt(final BlockPos pos) {
            return BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up()) || BlockRailBase.isRailBlock(this.world, pos.down());
        }
        
        @Nullable
        private Rail findRailAt(final BlockPos pos) {
            IBlockState iblockstate = this.world.getBlockState(pos);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                return new Rail(this.world, pos, iblockstate);
            }
            BlockPos lvt_2_1_ = pos.up();
            iblockstate = this.world.getBlockState(lvt_2_1_);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                return new Rail(this.world, lvt_2_1_, iblockstate);
            }
            lvt_2_1_ = pos.down();
            iblockstate = this.world.getBlockState(lvt_2_1_);
            return BlockRailBase.isRailBlock(iblockstate) ? new Rail(this.world, lvt_2_1_, iblockstate) : null;
        }
        
        private boolean isConnectedToRail(final Rail rail) {
            return this.isConnectedTo(rail.pos);
        }
        
        private boolean isConnectedTo(final BlockPos posIn) {
            for (int i = 0; i < this.connectedRails.size(); ++i) {
                final BlockPos blockpos = this.connectedRails.get(i);
                if (blockpos.getX() == posIn.getX() && blockpos.getZ() == posIn.getZ()) {
                    return true;
                }
            }
            return false;
        }
        
        protected int countAdjacentRails() {
            int i = 0;
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (this.hasRailAt(this.pos.offset(enumfacing))) {
                    ++i;
                }
            }
            return i;
        }
        
        private boolean canConnectTo(final Rail rail) {
            return this.isConnectedToRail(rail) || this.connectedRails.size() != 2;
        }
        
        private void connectTo(final Rail rail) {
            this.connectedRails.add(rail.pos);
            final BlockPos blockpos = this.pos.north();
            final BlockPos blockpos2 = this.pos.south();
            final BlockPos blockpos3 = this.pos.west();
            final BlockPos blockpos4 = this.pos.east();
            final boolean flag = this.isConnectedTo(blockpos);
            final boolean flag2 = this.isConnectedTo(blockpos2);
            final boolean flag3 = this.isConnectedTo(blockpos3);
            final boolean flag4 = this.isConnectedTo(blockpos4);
            EnumRailDirection blockrailbase$enumraildirection = null;
            if (flag || flag2) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            if (flag3 || flag4) {
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (flag2 && flag4 && !flag && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                }
                if (flag2 && flag3 && !flag && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                }
                if (flag && flag3 && !flag2 && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                }
                if (flag && flag4 && !flag2 && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockpos4.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            this.world.setBlockState(this.pos, this.state, 3);
        }
        
        private boolean hasNeighborRail(final BlockPos posIn) {
            final Rail blockrailbase$rail = this.findRailAt(posIn);
            if (blockrailbase$rail == null) {
                return false;
            }
            blockrailbase$rail.removeSoftConnections();
            return blockrailbase$rail.canConnectTo(this);
        }
        
        public Rail place(final boolean powered, final boolean initialPlacement) {
            final BlockPos blockpos = this.pos.north();
            final BlockPos blockpos2 = this.pos.south();
            final BlockPos blockpos3 = this.pos.west();
            final BlockPos blockpos4 = this.pos.east();
            final boolean flag = this.hasNeighborRail(blockpos);
            final boolean flag2 = this.hasNeighborRail(blockpos2);
            final boolean flag3 = this.hasNeighborRail(blockpos3);
            final boolean flag4 = this.hasNeighborRail(blockpos4);
            EnumRailDirection blockrailbase$enumraildirection = null;
            if ((flag || flag2) && !flag3 && !flag4) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            if ((flag3 || flag4) && !flag && !flag2) {
                blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (flag2 && flag4 && !flag && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                }
                if (flag2 && flag3 && !flag && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                }
                if (flag && flag3 && !flag2 && !flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                }
                if (flag && flag4 && !flag2 && !flag3) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                if (flag || flag2) {
                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                }
                if (flag3 || flag4) {
                    blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                }
                if (!this.isPowered) {
                    if (powered) {
                        if (flag2 && flag4) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                        }
                        if (flag3 && flag2) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                        }
                        if (flag4 && flag) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                        }
                        if (flag && flag3) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                        }
                    }
                    else {
                        if (flag && flag3) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_WEST;
                        }
                        if (flag4 && flag) {
                            blockrailbase$enumraildirection = EnumRailDirection.NORTH_EAST;
                        }
                        if (flag3 && flag2) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_WEST;
                        }
                        if (flag2 && flag4) {
                            blockrailbase$enumraildirection = EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos2.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (blockrailbase$enumraildirection == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockpos4.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up())) {
                    blockrailbase$enumraildirection = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (blockrailbase$enumraildirection == null) {
                blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }
            this.updateConnectedRails(blockrailbase$enumraildirection);
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
            if (initialPlacement || this.world.getBlockState(this.pos) != this.state) {
                this.world.setBlockState(this.pos, this.state, 3);
                for (int i = 0; i < this.connectedRails.size(); ++i) {
                    final Rail blockrailbase$rail = this.findRailAt(this.connectedRails.get(i));
                    if (blockrailbase$rail != null) {
                        blockrailbase$rail.removeSoftConnections();
                        if (blockrailbase$rail.canConnectTo(this)) {
                            blockrailbase$rail.connectTo(this);
                        }
                    }
                }
            }
            return this;
        }
        
        public IBlockState getBlockState() {
            return this.state;
        }
    }
}
