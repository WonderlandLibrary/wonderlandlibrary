package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;

/*
 * Exception performing whole class analysis ignored.
 */
static final class PlayerSelector.9
implements Predicate<Entity> {
    final /* synthetic */ int val$i;
    final /* synthetic */ int val$j;

    PlayerSelector.9(int n, int n2) {
        this.val$i = n;
        this.val$j = n2;
    }

    public boolean apply(Entity p_apply_1_) {
        int i1 = PlayerSelector.func_179650_a((int)((int)Math.floor((double)p_apply_1_.rotationYaw)));
        return this.val$i > this.val$j ? i1 >= this.val$i || i1 <= this.val$j : i1 >= this.val$i && i1 <= this.val$j;
    }
}
