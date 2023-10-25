package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

static final class IMob.1
implements Predicate<Entity> {
    IMob.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof IMob;
    }
}
