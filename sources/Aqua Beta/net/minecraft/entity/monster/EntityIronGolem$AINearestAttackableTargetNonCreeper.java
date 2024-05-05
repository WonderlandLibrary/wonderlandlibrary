package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

static class EntityIronGolem.AINearestAttackableTargetNonCreeper<T extends EntityLivingBase>
extends EntityAINearestAttackableTarget<T> {
    public EntityIronGolem.AINearestAttackableTargetNonCreeper(EntityCreature creature, Class<T> classTarget, int chance, boolean p_i45858_4_, boolean p_i45858_5_, Predicate<? super T> p_i45858_6_) {
        super(creature, classTarget, chance, p_i45858_4_, p_i45858_5_, p_i45858_6_);
        this.targetEntitySelector = new /* Unavailable Anonymous Inner Class!! */;
    }

    static /* synthetic */ double access$000(EntityIronGolem.AINearestAttackableTargetNonCreeper x0) {
        return x0.getTargetDistance();
    }

    static /* synthetic */ boolean access$100(EntityIronGolem.AINearestAttackableTargetNonCreeper x0, EntityLivingBase x1, boolean x2) {
        return x0.isSuitableTarget(x1, x2);
    }
}
