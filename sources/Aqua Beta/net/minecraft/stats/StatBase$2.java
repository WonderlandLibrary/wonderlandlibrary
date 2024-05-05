package net.minecraft.stats;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;

/*
 * Exception performing whole class analysis ignored.
 */
static final class StatBase.2
implements IStatType {
    StatBase.2() {
    }

    public String format(int number) {
        double d0 = (double)number / 20.0;
        double d1 = d0 / 60.0;
        double d2 = d1 / 60.0;
        double d3 = d2 / 24.0;
        double d4 = d3 / 365.0;
        return d4 > 0.5 ? StatBase.access$100().format(d4) + " y" : (d3 > 0.5 ? StatBase.access$100().format(d3) + " d" : (d2 > 0.5 ? StatBase.access$100().format(d2) + " h" : (d1 > 0.5 ? StatBase.access$100().format(d1) + " m" : d0 + " s")));
    }
}
