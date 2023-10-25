package optifine;

import com.google.common.collect.AbstractIterator;

class BlockPosM$1$1 extends AbstractIterator {
    private BlockPosM theBlockPosM;
    // $FF: synthetic field
    final <undefinedtype> this$0;

    BlockPosM$1$1(Object this$0) {
        this.this$0 = this$0;
        this.theBlockPosM = null;
    }

    protected BlockPosM computeNext0() {
        if (this.theBlockPosM == null) {
            this.theBlockPosM = new BlockPosM(this.this$0.val$blockpos.getX(), this.this$0.val$blockpos.getY(), this.this$0.val$blockpos.getZ(), 3);
            return this.theBlockPosM;
        } else if (this.theBlockPosM.equals(this.this$0.val$blockpos1)) {
            return (BlockPosM)this.endOfData();
        } else {
            int i = this.theBlockPosM.getX();
            int j = this.theBlockPosM.getY();
            int k = this.theBlockPosM.getZ();
            if (i < this.this$0.val$blockpos1.getX()) {
                ++i;
            } else if (j < this.this$0.val$blockpos1.getY()) {
                i = this.this$0.val$blockpos.getX();
                ++j;
            } else if (k < this.this$0.val$blockpos1.getZ()) {
                i = this.this$0.val$blockpos.getX();
                j = this.this$0.val$blockpos.getY();
                ++k;
            }

            this.theBlockPosM.setXyz(i, j, k);
            return this.theBlockPosM;
        }
    }

    protected Object computeNext() {
        return this.computeNext0();
    }
}
