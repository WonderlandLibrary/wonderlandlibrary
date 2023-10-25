package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

class EntityAINearestAttackableTarget.1
implements Predicate<T> {
    final /* synthetic */ Predicate val$targetSelector;

    EntityAINearestAttackableTarget.1(Predicate predicate) {
        this.val$targetSelector = predicate;
    }

    public boolean apply(T p_apply_1_) {
        if (this.val$targetSelector != null && !this.val$targetSelector.apply(p_apply_1_)) {
            return false;
        }
        if (p_apply_1_ instanceof EntityPlayer) {
            double d0 = EntityAINearestAttackableTarget.this.getTargetDistance();
            if (p_apply_1_.isSneaking()) {
                d0 *= (double)0.8f;
            }
            if (p_apply_1_.isInvisible()) {
                float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
                if (f < 0.1f) {
                    f = 0.1f;
                }
                d0 *= (double)(0.7f * f);
            }
            if ((double)p_apply_1_.getDistanceToEntity((Entity)EntityAINearestAttackableTarget.this.taskOwner) > d0) {
                return false;
            }
        }
        return EntityAINearestAttackableTarget.this.isSuitableTarget(p_apply_1_, false);
    }
}
