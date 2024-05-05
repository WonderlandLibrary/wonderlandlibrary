package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAITarget;

/*
 * Exception performing whole class analysis ignored.
 */
class EntityAIFindEntityNearest.1
implements Predicate<EntityLivingBase> {
    EntityAIFindEntityNearest.1() {
    }

    public boolean apply(EntityLivingBase p_apply_1_) {
        double d0 = EntityAIFindEntityNearest.this.getFollowRange();
        if (p_apply_1_.isSneaking()) {
            d0 *= (double)0.8f;
        }
        return p_apply_1_.isInvisible() ? false : ((double)p_apply_1_.getDistanceToEntity((Entity)EntityAIFindEntityNearest.access$000((EntityAIFindEntityNearest)EntityAIFindEntityNearest.this)) > d0 ? false : EntityAITarget.isSuitableTarget((EntityLiving)EntityAIFindEntityNearest.access$000((EntityAIFindEntityNearest)EntityAIFindEntityNearest.this), (EntityLivingBase)p_apply_1_, (boolean)false, (boolean)true));
    }
}
