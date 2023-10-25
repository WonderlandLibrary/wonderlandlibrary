package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockRailBase.Rail {
    private final World world;
    private final BlockPos pos;
    private final BlockRailBase block;
    private IBlockState state;
    private final boolean isPowered;
    private final List<BlockPos> field_150657_g = Lists.newArrayList();

    public BlockRailBase.Rail(World worldIn, BlockPos pos, IBlockState state) {
        this.world = worldIn;
        this.pos = pos;
        this.state = state;
        this.block = (BlockRailBase)state.getBlock();
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(BlockRailBase.this.getShapeProperty());
        this.isPowered = this.block.isPowered;
        this.func_180360_a(blockrailbase$enumraildirection);
    }

    private void func_180360_a(BlockRailBase.EnumRailDirection p_180360_1_) {
        this.field_150657_g.clear();
        switch (BlockRailBase.1.$SwitchMap$net$minecraft$block$BlockRailBase$EnumRailDirection[p_180360_1_.ordinal()]) {
            case 1: {
                this.field_150657_g.add((Object)this.pos.north());
                this.field_150657_g.add((Object)this.pos.south());
                break;
            }
            case 2: {
                this.field_150657_g.add((Object)this.pos.west());
                this.field_150657_g.add((Object)this.pos.east());
                break;
            }
            case 3: {
                this.field_150657_g.add((Object)this.pos.west());
                this.field_150657_g.add((Object)this.pos.east().up());
                break;
            }
            case 4: {
                this.field_150657_g.add((Object)this.pos.west().up());
                this.field_150657_g.add((Object)this.pos.east());
                break;
            }
            case 5: {
                this.field_150657_g.add((Object)this.pos.north().up());
                this.field_150657_g.add((Object)this.pos.south());
                break;
            }
            case 6: {
                this.field_150657_g.add((Object)this.pos.north());
                this.field_150657_g.add((Object)this.pos.south().up());
                break;
            }
            case 7: {
                this.field_150657_g.add((Object)this.pos.east());
                this.field_150657_g.add((Object)this.pos.south());
                break;
            }
            case 8: {
                this.field_150657_g.add((Object)this.pos.west());
                this.field_150657_g.add((Object)this.pos.south());
                break;
            }
            case 9: {
                this.field_150657_g.add((Object)this.pos.west());
                this.field_150657_g.add((Object)this.pos.north());
                break;
            }
            case 10: {
                this.field_150657_g.add((Object)this.pos.east());
                this.field_150657_g.add((Object)this.pos.north());
            }
        }
    }

    private void func_150651_b() {
        for (int i = 0; i < this.field_150657_g.size(); ++i) {
            BlockRailBase.Rail blockrailbase$rail = this.findRailAt((BlockPos)this.field_150657_g.get(i));
            if (blockrailbase$rail != null && blockrailbase$rail.func_150653_a(this)) {
                this.field_150657_g.set(i, (Object)blockrailbase$rail.pos);
                continue;
            }
            this.field_150657_g.remove(i--);
        }
    }

    private boolean hasRailAt(BlockPos pos) {
        return BlockRailBase.isRailBlock((World)this.world, (BlockPos)pos) || BlockRailBase.isRailBlock((World)this.world, (BlockPos)pos.up()) || BlockRailBase.isRailBlock((World)this.world, (BlockPos)pos.down());
    }

    private BlockRailBase.Rail findRailAt(BlockPos pos) {
        BlockRailBase.Rail rail;
        IBlockState iblockstate = this.world.getBlockState(pos);
        if (BlockRailBase.isRailBlock((IBlockState)iblockstate)) {
            BlockRailBase blockRailBase = BlockRailBase.this;
            blockRailBase.getClass();
            return blockRailBase.new BlockRailBase.Rail(this.world, pos, iblockstate);
        }
        BlockPos lvt_2_1_ = pos.up();
        iblockstate = this.world.getBlockState(lvt_2_1_);
        if (BlockRailBase.isRailBlock((IBlockState)iblockstate)) {
            BlockRailBase blockRailBase = BlockRailBase.this;
            blockRailBase.getClass();
            return blockRailBase.new BlockRailBase.Rail(this.world, lvt_2_1_, iblockstate);
        }
        lvt_2_1_ = pos.down();
        iblockstate = this.world.getBlockState(lvt_2_1_);
        if (BlockRailBase.isRailBlock((IBlockState)iblockstate)) {
            BlockRailBase blockRailBase = BlockRailBase.this;
            blockRailBase.getClass();
            rail = blockRailBase.new BlockRailBase.Rail(this.world, lvt_2_1_, iblockstate);
        } else {
            rail = null;
        }
        return rail;
    }

    private boolean func_150653_a(BlockRailBase.Rail p_150653_1_) {
        return this.func_180363_c(p_150653_1_.pos);
    }

    private boolean func_180363_c(BlockPos p_180363_1_) {
        for (int i = 0; i < this.field_150657_g.size(); ++i) {
            BlockPos blockpos = (BlockPos)this.field_150657_g.get(i);
            if (blockpos.getX() != p_180363_1_.getX() || blockpos.getZ() != p_180363_1_.getZ()) continue;
            return true;
        }
        return false;
    }

    protected int countAdjacentRails() {
        int i = 0;
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (!this.hasRailAt(this.pos.offset(enumfacing))) continue;
            ++i;
        }
        return i;
    }

    private boolean func_150649_b(BlockRailBase.Rail rail) {
        return this.func_150653_a(rail) || this.field_150657_g.size() != 2;
    }

    private void func_150645_c(BlockRailBase.Rail p_150645_1_) {
        this.field_150657_g.add((Object)p_150645_1_.pos);
        BlockPos blockpos = this.pos.north();
        BlockPos blockpos1 = this.pos.south();
        BlockPos blockpos2 = this.pos.west();
        BlockPos blockpos3 = this.pos.east();
        boolean flag = this.func_180363_c(blockpos);
        boolean flag1 = this.func_180363_c(blockpos1);
        boolean flag2 = this.func_180363_c(blockpos2);
        boolean flag3 = this.func_180363_c(blockpos3);
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
        if (flag || flag1) {
            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        }
        if (flag2 || flag3) {
            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
        }
        if (!this.isPowered) {
            if (flag1 && flag3 && !flag && !flag2) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
            }
            if (flag1 && flag2 && !flag && !flag3) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
            }
            if (flag && flag2 && !flag1 && !flag3) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
            }
            if (flag && flag3 && !flag1 && !flag2) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
            }
        }
        if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
            }
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos1.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
            }
        }
        if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos3.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
            }
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos2.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
            }
        }
        if (blockrailbase$enumraildirection == null) {
            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        }
        this.state = this.state.withProperty(this.block.getShapeProperty(), (Comparable)blockrailbase$enumraildirection);
        this.world.setBlockState(this.pos, this.state, 3);
    }

    private boolean func_180361_d(BlockPos p_180361_1_) {
        BlockRailBase.Rail blockrailbase$rail = this.findRailAt(p_180361_1_);
        if (blockrailbase$rail == null) {
            return false;
        }
        blockrailbase$rail.func_150651_b();
        return blockrailbase$rail.func_150649_b(this);
    }

    public BlockRailBase.Rail func_180364_a(boolean p_180364_1_, boolean p_180364_2_) {
        BlockPos blockpos = this.pos.north();
        BlockPos blockpos1 = this.pos.south();
        BlockPos blockpos2 = this.pos.west();
        BlockPos blockpos3 = this.pos.east();
        boolean flag = this.func_180361_d(blockpos);
        boolean flag1 = this.func_180361_d(blockpos1);
        boolean flag2 = this.func_180361_d(blockpos2);
        boolean flag3 = this.func_180361_d(blockpos3);
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
        if ((flag || flag1) && !flag2 && !flag3) {
            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        }
        if ((flag2 || flag3) && !flag && !flag1) {
            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
        }
        if (!this.isPowered) {
            if (flag1 && flag3 && !flag && !flag2) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
            }
            if (flag1 && flag2 && !flag && !flag3) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
            }
            if (flag && flag2 && !flag1 && !flag3) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
            }
            if (flag && flag3 && !flag1 && !flag2) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
            }
        }
        if (blockrailbase$enumraildirection == null) {
            if (flag || flag1) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }
            if (flag2 || flag3) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
            }
            if (!this.isPowered) {
                if (p_180364_1_) {
                    if (flag1 && flag3) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                    }
                    if (flag2 && flag1) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                    }
                    if (flag3 && flag) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                    }
                    if (flag && flag2) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                    }
                } else {
                    if (flag && flag2) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                    }
                    if (flag3 && flag) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                    }
                    if (flag2 && flag1) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                    }
                    if (flag1 && flag3) {
                        blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                    }
                }
            }
        }
        if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
            }
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos1.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
            }
        }
        if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos3.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
            }
            if (BlockRailBase.isRailBlock((World)this.world, (BlockPos)blockpos2.up())) {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
            }
        }
        if (blockrailbase$enumraildirection == null) {
            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        }
        this.func_180360_a(blockrailbase$enumraildirection);
        this.state = this.state.withProperty(this.block.getShapeProperty(), (Comparable)blockrailbase$enumraildirection);
        if (p_180364_2_ || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlockState(this.pos, this.state, 3);
            for (int i = 0; i < this.field_150657_g.size(); ++i) {
                BlockRailBase.Rail blockrailbase$rail = this.findRailAt((BlockPos)this.field_150657_g.get(i));
                if (blockrailbase$rail == null) continue;
                blockrailbase$rail.func_150651_b();
                if (!blockrailbase$rail.func_150649_b(this)) continue;
                blockrailbase$rail.func_150645_c(this);
            }
        }
        return this;
    }

    public IBlockState getBlockState() {
        return this.state;
    }
}
