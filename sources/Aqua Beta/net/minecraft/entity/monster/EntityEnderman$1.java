package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.monster.EntityEndermite;

class EntityEnderman.1
implements Predicate<EntityEndermite> {
    EntityEnderman.1() {
    }

    public boolean apply(EntityEndermite p_apply_1_) {
        return p_apply_1_.isSpawnedByPlayer();
    }
}
