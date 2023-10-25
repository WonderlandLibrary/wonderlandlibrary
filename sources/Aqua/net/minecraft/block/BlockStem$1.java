package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;

static final class BlockStem.1
implements Predicate<EnumFacing> {
    BlockStem.1() {
    }

    public boolean apply(EnumFacing p_apply_1_) {
        return p_apply_1_ != EnumFacing.DOWN;
    }
}
