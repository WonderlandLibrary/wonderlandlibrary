package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

class EntityAIAvoidEntity.1
implements Predicate<Entity> {
    EntityAIAvoidEntity.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_apply_1_);
    }
}
