package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

static final class PlayerSelector.1
implements Predicate<Entity> {
    PlayerSelector.1() {
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof EntityPlayer;
    }
}
