package net.optifine;

import com.google.common.collect.AbstractIterator;
import net.optifine.BlockPosM;

class BlockPosM.1
extends AbstractIterator {
    private BlockPosM theBlockPosM = null;

    BlockPosM.1() {
    }

    protected BlockPosM computeNext0() {
        if (this.theBlockPosM == null) {
            this.theBlockPosM = new BlockPosM(val$blockpos.getX(), val$blockpos.getY(), val$blockpos.getZ(), 3);
            return this.theBlockPosM;
        }
        if (this.theBlockPosM.equals((Object)val$blockpos1)) {
            return (BlockPosM)this.endOfData();
        }
        int i = this.theBlockPosM.getX();
        int j = this.theBlockPosM.getY();
        int k = this.theBlockPosM.getZ();
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
        this.theBlockPosM.setXyz(i, j, k);
        return this.theBlockPosM;
    }

    protected Object computeNext() {
        return this.computeNext0();
    }
}
