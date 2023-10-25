package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

static final class PlayerSelector.3
implements Predicate<Entity> {
    final /* synthetic */ int val$i;
    final /* synthetic */ int val$j;

    PlayerSelector.3(int n, int n2) {
        this.val$i = n;
        this.val$j = n2;
    }

    public boolean apply(Entity p_apply_1_) {
        if (!(p_apply_1_ instanceof EntityPlayerMP)) {
            return false;
        }
        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
        return !(this.val$i > -1 && entityplayermp.experienceLevel < this.val$i || this.val$j > -1 && entityplayermp.experienceLevel > this.val$j);
    }
}
