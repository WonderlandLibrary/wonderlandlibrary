/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodBuilderFactory;
import com.ibm.icu.impl.duration.Period;
import com.ibm.icu.impl.duration.PeriodBuilder;
import com.ibm.icu.impl.duration.PeriodBuilderImpl;
import com.ibm.icu.impl.duration.TimeUnit;

class MultiUnitBuilder
extends PeriodBuilderImpl {
    private int nPeriods;

    MultiUnitBuilder(int nPeriods, BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
        this.nPeriods = nPeriods;
    }

    public static MultiUnitBuilder get(int nPeriods, BasicPeriodBuilderFactory.Settings settings) {
        if (nPeriods > 0 && settings != null) {
            return new MultiUnitBuilder(nPeriods, settings);
        }
        return null;
    }

    @Override
    protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
        return MultiUnitBuilder.get(this.nPeriods, settingsToUse);
    }

    @Override
    protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
        Period period = null;
        int n2 = 0;
        short uset = this.settings.effectiveSet();
        for (int i2 = 0; i2 < TimeUnit.units.length; ++i2) {
            if (0 == (uset & 1 << i2)) continue;
            TimeUnit unit = TimeUnit.units[i2];
            if (n2 == this.nPeriods) break;
            long unitDuration = this.approximateDurationOf(unit);
            if (duration < unitDuration && n2 <= 0) continue;
            double count = (double)duration / (double)unitDuration;
            if (++n2 < this.nPeriods) {
                count = Math.floor(count);
                duration -= (long)(count * (double)unitDuration);
            }
            period = period == null ? Period.at((float)count, unit).inPast(inPast) : period.and((float)count, unit);
        }
        return period;
    }
}

