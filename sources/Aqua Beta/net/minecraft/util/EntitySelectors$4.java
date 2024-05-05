package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

static final class EntitySelectors.4
implements Predicate<Entity> {
    EntitySelectors.4() {
    }

    public boolean apply(Entity p_apply_1_) {
        return !(p_apply_1_ instanceof EntityPlayer) || !((EntityPlayer)p_apply_1_).isSpectator();
    }
}
