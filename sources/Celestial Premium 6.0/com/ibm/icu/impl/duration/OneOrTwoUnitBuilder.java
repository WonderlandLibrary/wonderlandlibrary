/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodBuilderFactory;
import com.ibm.icu.impl.duration.Period;
import com.ibm.icu.impl.duration.PeriodBuilder;
import com.ibm.icu.impl.duration.PeriodBuilderImpl;
import com.ibm.icu.impl.duration.TimeUnit;

class OneOrTwoUnitBuilder
extends PeriodBuilderImpl {
    OneOrTwoUnitBuilder(BasicPeriodBuilderFactory.Settings settings) {
        super(settings);
    }

    public static OneOrTwoUnitBuilder get(BasicPeriodBuilderFactory.Settings settings) {
        if (settings == null) {
            return null;
        }
        return new OneOrTwoUnitBuilder(settings);
    }

    protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
        return OneOrTwoUnitBuilder.get(settingsToUse);
    }

    protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
        Period period = null;
        short uset = this.settings.effectiveSet();
        for (int i = 0; i < TimeUnit.units.length; ++i) {
            TimeUnit unit;
            long unitDuration;
            if (0 == (uset & 1 << i) || duration < (unitDuration = this.approximateDurationOf(unit = TimeUnit.units[i])) && period == null) continue;
            double count = (double)duration / (double)unitDuration;
            if (period == null) {
                if (count >= 2.0) {
                    period = Period.at((float)count, unit);
                    break;
                }
                period = Period.at(1.0f, unit).inPast(inPast);
                duration -= unitDuration;
                continue;
            }
            if (!(count >= 1.0)) break;
            period = period.and((float)count, unit);
            break;
        }
        return period;
    }
}

