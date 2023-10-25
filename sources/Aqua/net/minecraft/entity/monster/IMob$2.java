package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

static final class IMob.2
implements Predicate<Entity> {
    IMob.2() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible();
    }
}
