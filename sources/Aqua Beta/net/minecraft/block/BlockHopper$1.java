package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;

static final class BlockHopper.1
implements Predicate<EnumFacing> {
    BlockHopper.1() {
    }

    public boolean apply(EnumFacing p_apply_1_) {
        return p_apply_1_ != EnumFacing.UP;
    }
}
