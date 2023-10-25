package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

static final class PlayerSelector.2
implements Predicate<Entity> {
    final /* synthetic */ String val$s_f;
    final /* synthetic */ boolean val$flag;

    PlayerSelector.2(String string, boolean bl) {
        this.val$s_f = string;
        this.val$flag = bl;
    }

    public boolean apply(Entity p_apply_1_) {
        return EntityList.isStringEntityName((Entity)p_apply_1_, (String)this.val$s_f) != this.val$flag;
    }
}
