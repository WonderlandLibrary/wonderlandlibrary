package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

class EntityRenderer.1
implements Predicate<Entity> {
    EntityRenderer.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.canBeCollidedWith();
    }
}
