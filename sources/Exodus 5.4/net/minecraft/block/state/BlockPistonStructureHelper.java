/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {
    private final BlockPos blockToMove;
    private final List<BlockPos> toMove = Lists.newArrayList();
    private final BlockPos pistonPos;
    private final EnumFacing moveDirection;
    private final List<BlockPos> toDestroy = Lists.newArrayList();
    private final World world;

    private boolean func_177250_b(BlockPos blockPos) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            if (enumFacing.getAxis() != this.moveDirection.getAxis() && !this.func_177251_a(blockPos.offset(enumFacing))) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public BlockPistonStructureHelper(World world, BlockPos blockPos, EnumFacing enumFacing, boolean bl) {
        this.world = world;
        this.pistonPos = blockPos;
        if (bl) {
            this.moveDirection = enumFacing;
            this.blockToMove = blockPos.offset(enumFacing);
        } else {
            this.moveDirection = enumFacing.getOpposite();
            this.blockToMove = blockPos.offset(enumFacing, 2);
        }
    }

    public List<BlockPos> getBlocksToDestroy() {
        return this.toDestroy;
    }

    /*
     * Unable to fully structure code
     */
    private boolean func_177251_a(BlockPos var1_1) {
        var2_2 = this.world.getBlockState(var1_1).getBlock();
        if (var2_2.getMaterial() == Material.air) {
            return true;
        }
        if (!BlockPistonBase.canPush(var2_2, this.world, var1_1, this.moveDirection, false)) {
            return true;
        }
        if (var1_1.equals(this.pistonPos)) {
            return true;
        }
        if (this.toMove.contains(var1_1)) {
            return true;
        }
        var3_3 = 1;
        if (var3_3 + this.toMove.size() <= 12) ** GOTO lbl16
        return false;
        while ((var2_2 = this.world.getBlockState(var4_4 = var1_1.offset(this.moveDirection.getOpposite(), var3_3)).getBlock()).getMaterial() != Material.air && BlockPistonBase.canPush(var2_2, this.world, var4_4, this.moveDirection, false) && !var4_4.equals(this.pistonPos)) {
            if (++var3_3 + this.toMove.size() > 12) {
                return false;
            }
lbl16:
            // 3 sources

            if (var2_2 == Blocks.slime_block) continue;
        }
        var4_5 = 0;
        var5_6 = var3_3 - 1;
        while (var5_6 >= 0) {
            this.toMove.add(var1_1.offset(this.moveDirection.getOpposite(), var5_6));
            ++var4_5;
            --var5_6;
        }
        var5_6 = 1;
        while (true) {
            if ((var7_8 = this.toMove.indexOf(var6_7 = var1_1.offset(this.moveDirection, var5_6))) > -1) {
                this.func_177255_a(var4_5, var7_8);
                var8_9 = 0;
                while (var8_9 <= var7_8 + var4_5) {
                    var9_10 = this.toMove.get(var8_9);
                    if (this.world.getBlockState(var9_10).getBlock() == Blocks.slime_block && !this.func_177250_b(var9_10)) {
                        return false;
                    }
                    ++var8_9;
                }
                return true;
            }
            var2_2 = this.world.getBlockState(var6_7).getBlock();
            if (var2_2.getMaterial() == Material.air) {
                return true;
            }
            if (!BlockPistonBase.canPush(var2_2, this.world, var6_7, this.moveDirection, true) || var6_7.equals(this.pistonPos)) {
                return false;
            }
            if (var2_2.getMobilityFlag() == 1) {
                this.toDestroy.add(var6_7);
                return true;
            }
            if (this.toMove.size() >= 12) {
                return false;
            }
            this.toMove.add(var6_7);
            ++var4_5;
            ++var5_6;
        }
    }

    public boolean canMove() {
        this.toMove.clear();
        this.toDestroy.clear();
        Block block = this.world.getBlockState(this.blockToMove).getBlock();
        if (!BlockPistonBase.canPush(block, this.world, this.blockToMove, this.moveDirection, false)) {
            if (block.getMobilityFlag() != 1) {
                return false;
            }
            this.toDestroy.add(this.blockToMove);
            return true;
        }
        if (!this.func_177251_a(this.blockToMove)) {
            return false;
        }
        int n = 0;
        while (n < this.toMove.size()) {
            BlockPos blockPos = this.toMove.get(n);
            if (this.world.getBlockState(blockPos).getBlock() == Blocks.slime_block && !this.func_177250_b(blockPos)) {
                return false;
            }
            ++n;
        }
        return true;
    }

    private void func_177255_a(int n, int n2) {
        ArrayList arrayList = Lists.newArrayList();
        ArrayList arrayList2 = Lists.newArrayList();
        ArrayList arrayList3 = Lists.newArrayList();
        arrayList.addAll(this.toMove.subList(0, n2));
        arrayList2.addAll(this.toMove.subList(this.toMove.size() - n, this.toMove.size()));
        arrayList3.addAll(this.toMove.subList(n2, this.toMove.size() - n));
        this.toMove.clear();
        this.toMove.addAll(arrayList);
        this.toMove.addAll(arrayList2);
        this.toMove.addAll(arrayList3);
    }

    public List<BlockPos> getBlocksToMove() {
        return this.toMove;
    }
}

