package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;

static final class EntityHorse.1
implements Predicate<Entity> {
    EntityHorse.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof EntityHorse && ((EntityHorse)p_apply_1_).isBreeding();
    }
}
