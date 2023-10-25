/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.PeriodBuilder;
import com.ibm.icu.impl.duration.TimeUnit;
import java.util.TimeZone;

public interface PeriodBuilderFactory {
    public PeriodBuilderFactory setAvailableUnitRange(TimeUnit var1, TimeUnit var2);

    public PeriodBuilderFactory setUnitIsAvailable(TimeUnit var1, boolean var2);

    public PeriodBuilderFactory setMaxLimit(float var1);

    public PeriodBuilderFactory setMinLimit(float var1);

    public PeriodBuilderFactory setAllowZero(boolean var1);

    public PeriodBuilderFactory setWeeksAloneOnly(boolean var1);

    public PeriodBuilderFactory setAllowMilliseconds(boolean var1);

    public PeriodBuilderFactory setLocale(String var1);

    public PeriodBuilderFactory setTimeZone(TimeZone var1);

    public PeriodBuilder getFixedUnitBuilder(TimeUnit var1);

    public PeriodBuilder getSingleUnitBuilder();

    public PeriodBuilder getOneOrTwoUnitBuilder();

    public PeriodBuilder getMultiUnitBuilder(int var1);
}

