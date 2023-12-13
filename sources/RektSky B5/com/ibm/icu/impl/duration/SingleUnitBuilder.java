/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodBuilderFactory;
import com.ibm.icu.impl.duration.Period;
import com.ibm.icu.impl.duration.PeriodBuilder;
import com.ibm.icu.impl.duration.PeriodBuilderImpl;
import com.ibm.icu.impl.duration.TimeUnit;

class SingleUnitBuilder
extends PeriodBuilderImpl {
    SingleUnitBuilder(BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
    }

    public static SingleUnitBuilder get(BasicPeriodBuilderFactory.Settings settings) {
        if (settings == null) {
            return null;
        }
        return new SingleUnitBuilder(settings);
    }

    @Override
    protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
        return SingleUnitBuilder.get(settingsToUse);
    }

    @Override
    protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
        short uset = this.settings.effectiveSet();
        for (int i2 = 0; i2 < TimeUnit.units.length; ++i2) {
            TimeUnit unit;
            long unitDuration;
            if (0 == (uset & 1 << i2) || duration < (unitDuration = this.approximateDurationOf(unit = TimeUnit.units[i2]))) continue;
            return Period.at((float)((double)duration / (double)unitDuration), unit).inPast(inPast);
        }
        return null;
    }
}

