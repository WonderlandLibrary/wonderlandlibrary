package net.minecraft.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;

/*
 * Exception performing whole class analysis ignored.
 */
static final class StatBase.3
implements IStatType {
    StatBase.3() {
    }

    public String format(int number) {
        double d0 = (double)number / 100.0;
        double d1 = d0 / 1000.0;
        return d1 > 0.5 ? StatBase.access$100().format(d1) + " km" : (d0 > 0.5 ? StatBase.access$100().format(d0) + " m" : number + " cm");
    }
}
