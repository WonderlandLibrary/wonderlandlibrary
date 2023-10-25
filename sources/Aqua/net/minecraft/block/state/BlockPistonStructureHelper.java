package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {
    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;
    private final List<BlockPos> toMove = Lists.newArrayList();
    private final List<BlockPos> toDestroy = Lists.newArrayList();

    public BlockPistonStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending) {
        this.world = worldIn;
        this.pistonPos = posIn;
        if (extending) {
            this.moveDirection = pistonFacing;
            this.blockToMove = posIn.offset(pistonFacing);
        } else {
            this.moveDirection = pistonFacing.getOpposite();
            this.blockToMove = posIn.offset(pistonFacing, 2);
        }
    }

    public boolean canMove() {
        this.toMove.clear();
        this.toDestroy.clear();
        Block block = this.world.getBlockState(this.blockToMove).getBlock();
        if (!BlockPistonBase.canPush((Block)block, (World)this.world, (BlockPos)this.blockToMove, (EnumFacing)this.moveDirection, (boolean)false)) {
            if (block.getMobilityFlag() != 1) {
                return false;
            }
            this.toDestroy.add((Object)this.blockToMove);
            return true;
        }
        if (!this.func_177251_a(this.blockToMove)) {
            return false;
        }
        for (int i = 0; i < this.toMove.size(); ++i) {
            BlockPos blockpos = (BlockPos)this.toMove.get(i);
            if (this.world.getBlockState(blockpos).getBlock() != Blocks.slime_block || this.func_177250_b(blockpos)) continue;
            return false;
        }
        return true;
    }

    private boolean func_177251_a(BlockPos origin) {
        BlockPos blockpos;
        Block block = this.world.getBlockState(origin).getBlock();
        if (block.getMaterial() == Material.air) {
            return true;
        }
        if (!BlockPistonBase.canPush((Block)block, (World)this.world, (BlockPos)origin, (EnumFacing)this.moveDirection, (boolean)false)) {
            return true;
        }
        if (origin.equals((Object)this.pistonPos)) {
            return true;
        }
        if (this.toMove.contains((Object)origin)) {
            return true;
        }
        int i = 1;
        if (i + this.toMove.size() > 12) {
            return false;
        }
        while (block == Blocks.slime_block && (block = this.world.getBlockState(blockpos = origin.offset(this.moveDirection.getOpposite(), i)).getBlock()).getMaterial() != Material.air && BlockPistonBase.canPush((Block)block, (World)this.world, (BlockPos)blockpos, (EnumFacing)this.moveDirection, (boolean)false) && !blockpos.equals((Object)this.pistonPos)) {
            if (++i + this.toMove.size() <= 12) continue;
            return false;
        }
        int i1 = 0;
        for (int j = i - 1; j >= 0; --j) {
            this.toMove.add((Object)origin.offset(this.moveDirection.getOpposite(), j));
            ++i1;
        }
        int j1 = 1;
        while (true) {
            BlockPos blockpos1;
            int k;
            if ((k = this.toMove.indexOf((Object)(blockpos1 = origin.offset(this.moveDirection, j1)))) > -1) {
                this.func_177255_a(i1, k);
                for (int l = 0; l <= k + i1; ++l) {
                    BlockPos blockpos2 = (BlockPos)this.toMove.get(l);
                    if (this.world.getBlockState(blockpos2).getBlock() != Blocks.slime_block || this.func_177250_b(blockpos2)) continue;
                    return false;
                }
                return true;
            }
            block = this.world.getBlockState(blockpos1).getBlock();
            if (block.getMaterial() == Material.air) {
                return true;
            }
            if (!BlockPistonBase.canPush((Block)block, (World)this.world, (BlockPos)blockpos1, (EnumFacing)this.moveDirection, (boolean)true) || blockpos1.equals((Object)this.pistonPos)) {
                return false;
            }
            if (block.getMobilityFlag() == 1) {
                this.toDestroy.add((Object)blockpos1);
                return true;
            }
            if (this.toMove.size() >= 12) {
                return false;
            }
            this.toMove.add((Object)blockpos1);
            ++i1;
            ++j1;
        }
    }

    private void func_177255_a(int p_177255_1_, int p_177255_2_) {
        ArrayList list = Lists.newArrayList();
        ArrayList list1 = Lists.newArrayList();
        ArrayList list2 = Lists.newArrayList();
        list.addAll((Collection)this.toMove.subList(0, p_177255_2_));
        list1.addAll((Collection)this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
        list2.addAll((Collection)this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
        this.toMove.clear();
        this.toMove.addAll((Collection)list);
        this.toMove.addAll((Collection)list1);
        this.toMove.addAll((Collection)list2);
    }

    private boolean func_177250_b(BlockPos p_177250_1_) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing.getAxis() == this.moveDirection.getAxis() || this.func_177251_a(p_177250_1_.offset(enumfacing))) continue;
            return false;
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
