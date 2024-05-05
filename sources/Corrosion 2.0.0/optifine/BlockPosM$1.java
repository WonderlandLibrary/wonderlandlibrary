package optifine;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.BlockPos;

final class BlockPosM$1 implements Iterable {
    // $FF: synthetic field
    final BlockPos val$blockpos;
    // $FF: synthetic field
    final BlockPos val$blockpos1;

    BlockPosM$1(BlockPos var1, BlockPos var2) {
        this.val$blockpos = var1;
        this.val$blockpos1 = var2;
    }

    public Iterator iterator() {
        return new AbstractIterator() {
            private BlockPosM theBlockPosM = null;

            protected BlockPosM computeNext0() {
                if (this.theBlockPosM == null) {
                    this.theBlockPosM = new BlockPosM(BlockPosM$1.this.val$blockpos.getX(), BlockPosM$1.this.val$blockpos.getY(), BlockPosM$1.this.val$blockpos.getZ(), 3);
                    return this.theBlockPosM;
                } else if (this.theBlockPosM.equals(BlockPosM$1.this.val$blockpos1)) {
                    return (BlockPosM)this.endOfData();
                } else {
                    int i = this.theBlockPosM.getX();
                    int j = this.theBlockPosM.getY();
                    int k = this.theBlockPosM.getZ();
                    if (i < BlockPosM$1.this.val$blockpos1.getX()) {
                        ++i;
                    } else if (j < BlockPosM$1.this.val$blockpos1.getY()) {
                        i = BlockPosM$1.this.val$blockpos.getX();
                        ++j;
                    } else if (k < BlockPosM$1.this.val$blockpos1.getZ()) {
                        i = BlockPosM$1.this.val$blockpos.getX();
                        j = BlockPosM$1.this.val$blockpos.getY();
                        ++k;
                    }

                    this.theBlockPosM.setXyz(i, j, k);
                    return this.theBlockPosM;
                }
            }

            protected Object computeNext() {
                return this.computeNext0();
            }
        };
    }
}
