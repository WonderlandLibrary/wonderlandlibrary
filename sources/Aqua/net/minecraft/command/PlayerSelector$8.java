package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

static final class PlayerSelector.8
implements Predicate<Entity> {
    final /* synthetic */ BlockPos val$p_180698_1_;
    final /* synthetic */ int val$i;
    final /* synthetic */ int val$k;
    final /* synthetic */ int val$j;
    final /* synthetic */ int val$l;

    PlayerSelector.8(BlockPos blockPos, int n, int n2, int n3, int n4) {
        this.val$p_180698_1_ = blockPos;
        this.val$i = n;
        this.val$k = n2;
        this.val$j = n3;
        this.val$l = n4;
    }

    public boolean apply(Entity p_apply_1_) {
        int i1 = (int)p_apply_1_.getDistanceSqToCenter(this.val$p_180698_1_);
        return !(this.val$i >= 0 && i1 < this.val$k || this.val$j >= 0 && i1 > this.val$l);
    }
}
