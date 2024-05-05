package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;

class EntityWolf.1
implements Predicate<Entity> {
    EntityWolf.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
    }
}
