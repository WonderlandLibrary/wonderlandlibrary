package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

static final class PlayerSelector.11
implements Predicate<Entity> {
    final /* synthetic */ AxisAlignedBB val$axisalignedbb;

    PlayerSelector.11(AxisAlignedBB axisAlignedBB) {
        this.val$axisalignedbb = axisAlignedBB;
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.posX >= this.val$axisalignedbb.minX && p_apply_1_.posY >= this.val$axisalignedbb.minY && p_apply_1_.posZ >= this.val$axisalignedbb.minZ ? p_apply_1_.posX < this.val$axisalignedbb.maxX && p_apply_1_.posY < this.val$axisalignedbb.maxY && p_apply_1_.posZ < this.val$axisalignedbb.maxZ : false;
    }
}
