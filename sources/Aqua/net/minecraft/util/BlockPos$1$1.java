package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.BlockPos;

class BlockPos.1
extends AbstractIterator<BlockPos> {
    private BlockPos lastReturned = null;

    BlockPos.1() {
    }

    protected BlockPos computeNext() {
        if (this.lastReturned == null) {
            this.lastReturned = val$blockpos;
            return this.lastReturned;
        }
        if (this.lastReturned.equals((Object)val$blockpos1)) {
            return (BlockPos)this.endOfData();
        }
        int i = this.lastReturned.getX();
        int j = this.lastReturned.getY();
        int k = this.lastReturned.getZ();
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
        this.lastReturned = new BlockPos(i, j, k);
        return this.lastReturned;
    }
}
