package net.minecraft.command;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

static final class PlayerSelector.12
implements Comparator<Entity> {
    final /* synthetic */ BlockPos val$p_179658_5_;

    PlayerSelector.12(BlockPos blockPos) {
        this.val$p_179658_5_ = blockPos;
    }

    public int compare(Entity p_compare_1_, Entity p_compare_2_) {
        return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(this.val$p_179658_5_), p_compare_2_.getDistanceSq(this.val$p_179658_5_)).result();
    }
}
