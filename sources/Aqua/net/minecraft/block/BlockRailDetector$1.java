package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockRailBase;

static final class BlockRailDetector.1
implements Predicate<BlockRailBase.EnumRailDirection> {
    BlockRailDetector.1() {
    }

    public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_) {
        return p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
    }
}
