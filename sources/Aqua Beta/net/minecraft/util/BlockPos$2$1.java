package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.BlockPos;

/*
 * Exception performing whole class analysis ignored.
 */
class BlockPos.1
extends AbstractIterator<BlockPos.MutableBlockPos> {
    private BlockPos.MutableBlockPos theBlockPos = null;

    BlockPos.1() {
    }

    protected BlockPos.MutableBlockPos computeNext() {
        if (this.theBlockPos == null) {
            this.theBlockPos = new BlockPos.MutableBlockPos(val$blockpos.getX(), val$blockpos.getY(), val$blockpos.getZ());
            return this.theBlockPos;
        }
        if (this.theBlockPos.equals((Object)val$blockpos1)) {
            return (BlockPos.MutableBlockPos)this.endOfData();
        }
        int i = this.theBlockPos.getX();
        int j = this.theBlockPos.getY();
        int k = this.theBlockPos.getZ();
        if (i < val$blockpos1.getX()) {
            ++i;
        } else if (j < val$blockpos1.getY()) {
            i = val$blockpos.getX();
            ++j;
        } else if (k < val$blockpos1.getZ()) {
            i = val$blockpos.getX();
            j = val$blockpos.getY();
            ++k;
        }
        BlockPos.MutableBlockPos.access$002((BlockPos.MutableBlockPos)this.theBlockPos, (int)i);
        BlockPos.MutableBlockPos.access$102((BlockPos.MutableBlockPos)this.theBlockPos, (int)j);
        BlockPos.MutableBlockPos.access$202((BlockPos.MutableBlockPos)this.theBlockPos, (int)k);
        return this.theBlockPos;
    }
}
