package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockPlanks;

static final class BlockNewLog.1
implements Predicate<BlockPlanks.EnumType> {
    BlockNewLog.1() {
    }

    public boolean apply(BlockPlanks.EnumType p_apply_1_) {
        return p_apply_1_.getMetadata() >= 4;
    }
}
