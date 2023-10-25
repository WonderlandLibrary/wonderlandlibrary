package net.minecraft.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;

/*
 * Exception performing whole class analysis ignored.
 */
static final class StatBase.4
implements IStatType {
    StatBase.4() {
    }

    public String format(int number) {
        return StatBase.access$100().format((double)number * 0.1);
    }
}
