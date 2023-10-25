package net.minecraft.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;

/*
 * Exception performing whole class analysis ignored.
 */
static final class StatBase.1
implements IStatType {
    StatBase.1() {
    }

    public String format(int number) {
        return StatBase.access$000().format((long)number);
    }
}
