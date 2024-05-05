package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

static final class EntitySelectors.1
implements Predicate<Entity> {
    EntitySelectors.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.isEntityAlive();
    }
}
