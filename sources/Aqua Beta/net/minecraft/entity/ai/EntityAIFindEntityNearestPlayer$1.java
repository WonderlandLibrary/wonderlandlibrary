package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

/*
 * Exception performing whole class analysis ignored.
 */
class EntityAIFindEntityNearestPlayer.1
implements Predicate<Entity> {
    EntityAIFindEntityNearestPlayer.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        if (!(p_apply_1_ instanceof EntityPlayer)) {
            return false;
        }
        if (((EntityPlayer)p_apply_1_).capabilities.disableDamage) {
            return false;
        }
        double d0 = EntityAIFindEntityNearestPlayer.this.maxTargetRange();
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
        return (double)p_apply_1_.getDistanceToEntity((Entity)EntityAIFindEntityNearestPlayer.access$000((EntityAIFindEntityNearestPlayer)EntityAIFindEntityNearestPlayer.this)) > d0 ? false : EntityAITarget.isSuitableTarget((EntityLiving)EntityAIFindEntityNearestPlayer.access$000((EntityAIFindEntityNearestPlayer)EntityAIFindEntityNearestPlayer.this), (EntityLivingBase)((EntityLivingBase)p_apply_1_), (boolean)false, (boolean)true);
    }
}
