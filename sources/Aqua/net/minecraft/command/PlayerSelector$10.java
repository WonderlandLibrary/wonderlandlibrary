package net.minecraft.command;

import com.google.common.base.Predicate;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;

/*
 * Exception performing whole class analysis ignored.
 */
static final class PlayerSelector.10
implements Predicate<Entity> {
    final /* synthetic */ int val$k;
    final /* synthetic */ int val$l;

    PlayerSelector.10(int n, int n2) {
        this.val$k = n;
        this.val$l = n2;
    }

    public boolean apply(Entity p_apply_1_) {
        int i1 = PlayerSelector.func_179650_a((int)((int)Math.floor((double)p_apply_1_.rotationPitch)));
        return this.val$k > this.val$l ? i1 >= this.val$k || i1 <= this.val$l : i1 >= this.val$k && i1 <= this.val$l;
    }
}
