package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

static final class EntitySelectors.2
implements Predicate<Entity> {
    EntitySelectors.2() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.isEntityAlive() && p_apply_1_.riddenByEntity == null && p_apply_1_.ridingEntity == null;
    }
}
