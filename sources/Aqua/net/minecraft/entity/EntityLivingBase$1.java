package net.minecraft.entity;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

class EntityLivingBase.1
implements Predicate<Entity> {
    EntityLivingBase.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.canBePushed();
    }
}
