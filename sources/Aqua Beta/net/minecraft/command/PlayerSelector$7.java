package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

static final class PlayerSelector.7
implements Predicate<Entity> {
    final /* synthetic */ String val$s_f;
    final /* synthetic */ boolean val$flag;

    PlayerSelector.7(String string, boolean bl) {
        this.val$s_f = string;
        this.val$flag = bl;
    }

    public boolean apply(Entity p_apply_1_) {
        return p_apply_1_.getName().equals((Object)this.val$s_f) != this.val$flag;
    }
}
