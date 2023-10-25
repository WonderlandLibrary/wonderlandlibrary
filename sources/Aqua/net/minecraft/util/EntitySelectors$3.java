package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;

static final class EntitySelectors.3
implements Predicate<Entity> {
    EntitySelectors.3() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof IInventory && p_apply_1_.isEntityAlive();
    }
}
