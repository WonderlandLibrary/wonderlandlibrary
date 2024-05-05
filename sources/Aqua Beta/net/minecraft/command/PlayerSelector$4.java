package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

static final class PlayerSelector.4
implements Predicate<Entity> {
    final /* synthetic */ int val$i;

    PlayerSelector.4(int n) {
        this.val$i = n;
    }

    public boolean apply(Entity p_apply_1_) {
        if (!(p_apply_1_ instanceof EntityPlayerMP)) {
            return false;
        }
        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
        return entityplayermp.theItemInWorldManager.getGameType().getID() == this.val$i;
    }
}
