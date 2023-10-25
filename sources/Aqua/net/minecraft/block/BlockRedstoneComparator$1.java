package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

class BlockRedstoneComparator.1
implements Predicate<Entity> {
    final /* synthetic */ EnumFacing val$facing;

    BlockRedstoneComparator.1(EnumFacing enumFacing) {
        this.val$facing = enumFacing;
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == this.val$facing;
    }
}
