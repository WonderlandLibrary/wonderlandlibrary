/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodBuilderFactory;
import com.ibm.icu.impl.duration.Period;
import com.ibm.icu.impl.duration.PeriodBuilder;
import com.ibm.icu.impl.duration.PeriodBuilderImpl;
import com.ibm.icu.impl.duration.TimeUnit;

class FixedUnitBuilder
extends PeriodBuilderImpl {
    private TimeUnit unit;

    public static FixedUnitBuilder get(TimeUnit unit, BasicPeriodBuilderFactory.Settings settingsToUse) {
        if (settingsToUse != null && (settingsToUse.effectiveSet() & 1 << unit.ordinal) != 0) {
            return new FixedUnitBuilder(unit, settingsToUse);
        }
        return null;
    }

    FixedUnitBuilder(TimeUnit unit, BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
        this.unit = unit;
    }

    protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
        return FixedUnitBuilder.get(this.unit, settingsToUse);
    }

    protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
        if (this.unit == null) {
            return null;
        }
        long unitDuration = this.approximateDurationOf(this.unit);
        return Period.at((float)((double)duration / (double)unitDuration), this.unit).inPast(inPast);
    }
}

