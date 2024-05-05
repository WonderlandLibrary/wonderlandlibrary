package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;

/*
 * Exception performing whole class analysis ignored.
 */
class EntityIronGolem.AINearestAttackableTargetNonCreeper.1
implements Predicate<T> {
    final /* synthetic */ Predicate val$p_i45858_6_;
    final /* synthetic */ EntityCreature val$creature;

    EntityIronGolem.AINearestAttackableTargetNonCreeper.1(Predicate predicate, EntityCreature entityCreature) {
        this.val$p_i45858_6_ = predicate;
        this.val$creature = entityCreature;
    }

    public boolean apply(T p_apply_1_) {
        if (this.val$p_i45858_6_ != null && !this.val$p_i45858_6_.apply(p_apply_1_)) {
            return false;
        }
        if (p_apply_1_ instanceof EntityCreeper) {
            return false;
        }
        if (p_apply_1_ instanceof EntityPlayer) {
            double d0 = EntityIronGolem.AINearestAttackableTargetNonCreeper.access$000((EntityIronGolem.AINearestAttackableTargetNonCreeper)AINearestAttackableTargetNonCreeper.this);
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
            if ((double)p_apply_1_.getDistanceToEntity((Entity)this.val$creature) > d0) {
                return false;
            }
        }
        return EntityIronGolem.AINearestAttackableTargetNonCreeper.access$100((EntityIronGolem.AINearestAttackableTargetNonCreeper)AINearestAttackableTargetNonCreeper.this, p_apply_1_, (boolean)false);
    }
}
