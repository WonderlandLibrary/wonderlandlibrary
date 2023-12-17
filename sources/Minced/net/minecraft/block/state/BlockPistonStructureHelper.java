// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state;

import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.BlockPistonBase;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper
{
    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;
    private final List<BlockPos> toMove;
    private final List<BlockPos> toDestroy;
    
    public BlockPistonStructureHelper(final World worldIn, final BlockPos posIn, final EnumFacing pistonFacing, final boolean extending) {
        this.toMove = (List<BlockPos>)Lists.newArrayList();
        this.toDestroy = (List<BlockPos>)Lists.newArrayList();
        this.world = worldIn;
        this.pistonPos = posIn;
        if (extending) {
            this.moveDirection = pistonFacing;
            this.blockToMove = posIn.offset(pistonFacing);
        }
        else {
            this.moveDirection = pistonFacing.getOpposite();
            this.blockToMove = posIn.offset(pistonFacing, 2);
        }
    }
    
    public boolean canMove() {
        this.toMove.clear();
        this.toDestroy.clear();
        final IBlockState iblockstate = this.world.getBlockState(this.blockToMove);
        if (!BlockPistonBase.canPush(iblockstate, this.world, this.blockToMove, this.moveDirection, false, this.moveDirection)) {
            if (iblockstate.getPushReaction() == EnumPushReaction.DESTROY) {
                this.toDestroy.add(this.blockToMove);
                return true;
            }
            return false;
        }
        else {
            if (!this.addBlockLine(this.blockToMove, this.moveDirection)) {
                return false;
            }
            for (int i = 0; i < this.toMove.size(); ++i) {
                final BlockPos blockpos = this.toMove.get(i);
                if (this.world.getBlockState(blockpos).getBlock() == Blocks.SLIME_BLOCK && !this.addBranchingBlocks(blockpos)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private boolean addBlockLine(final BlockPos origin, final EnumFacing p_177251_2_) {
        IBlockState iblockstate = this.world.getBlockState(origin);
        Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() == Material.AIR) {
            return true;
        }
        if (!BlockPistonBase.canPush(iblockstate, this.world, origin, this.moveDirection, false, p_177251_2_)) {
            return true;
        }
        if (origin.equals(this.pistonPos)) {
            return true;
        }
        if (this.toMove.contains(origin)) {
            return true;
        }
        int i = 1;
        if (i + this.toMove.size() > 12) {
            return false;
        }
        while (block == Blocks.SLIME_BLOCK) {
            final BlockPos blockpos = origin.offset(this.moveDirection.getOpposite(), i);
            iblockstate = this.world.getBlockState(blockpos);
            block = iblockstate.getBlock();
            if (iblockstate.getMaterial() == Material.AIR || !BlockPistonBase.canPush(iblockstate, this.world, blockpos, this.moveDirection, false, this.moveDirection.getOpposite())) {
                break;
            }
            if (blockpos.equals(this.pistonPos)) {
                break;
            }
            if (++i + this.toMove.size() > 12) {
                return false;
            }
        }
        int i2 = 0;
        for (int j = i - 1; j >= 0; --j) {
            this.toMove.add(origin.offset(this.moveDirection.getOpposite(), j));
            ++i2;
        }
        int j2 = 1;
        while (true) {
            final BlockPos blockpos2 = origin.offset(this.moveDirection, j2);
            final int k = this.toMove.indexOf(blockpos2);
            if (k > -1) {
                this.reorderListAtCollision(i2, k);
                for (int l = 0; l <= k + i2; ++l) {
                    final BlockPos blockpos3 = this.toMove.get(l);
                    if (this.world.getBlockState(blockpos3).getBlock() == Blocks.SLIME_BLOCK && !this.addBranchingBlocks(blockpos3)) {
                        return false;
                    }
                }
                return true;
            }
            iblockstate = this.world.getBlockState(blockpos2);
            if (iblockstate.getMaterial() == Material.AIR) {
                return true;
            }
            if (!BlockPistonBase.canPush(iblockstate, this.world, blockpos2, this.moveDirection, true, this.moveDirection) || blockpos2.equals(this.pistonPos)) {
                return false;
            }
            if (iblockstate.getPushReaction() == EnumPushReaction.DESTROY) {
                this.toDestroy.add(blockpos2);
                return true;
            }
            if (this.toMove.size() >= 12) {
                return false;
            }
            this.toMove.add(blockpos2);
            ++i2;
            ++j2;
        }
    }
    
    private void reorderListAtCollision(final int p_177255_1_, final int p_177255_2_) {
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList();
        final List<BlockPos> list2 = (List<BlockPos>)Lists.newArrayList();
        final List<BlockPos> list3 = (List<BlockPos>)Lists.newArrayList();
        list.addAll(this.toMove.subList(0, p_177255_2_));
        list2.addAll(this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
        list3.addAll(this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
        this.toMove.clear();
        this.toMove.addAll(list);
        this.toMove.addAll(list2);
        this.toMove.addAll(list3);
    }
    
    private boolean addBranchingBlocks(final BlockPos fromPos) {
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing.getAxis() != this.moveDirection.getAxis() && !this.addBlockLine(fromPos.offset(enumfacing), enumfacing)) {
                return false;
            }
        }
        return true;
    }
    
    public List<BlockPos> getBlocksToMove() {
        return this.toMove;
    }
    
    public List<BlockPos> getBlocksToDestroy() {
        return this.toDestroy;
    }
}
