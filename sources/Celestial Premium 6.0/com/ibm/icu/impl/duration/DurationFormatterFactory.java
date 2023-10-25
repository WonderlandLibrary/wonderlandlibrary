/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.DateFormatter;
import com.ibm.icu.impl.duration.DurationFormatter;
import com.ibm.icu.impl.duration.PeriodBuilder;
import com.ibm.icu.impl.duration.PeriodFormatter;
import java.util.TimeZone;

public interface DurationFormatterFactory {
    public DurationFormatterFactory setPeriodFormatter(PeriodFormatter var1);

    public DurationFormatterFactory setPeriodBuilder(PeriodBuilder var1);

    public DurationFormatterFactory setFallback(DateFormatter var1);

    public DurationFormatterFactory setFallbackLimit(long var1);

    public DurationFormatterFactory setLocale(String var1);

    public DurationFormatterFactory setTimeZone(TimeZone var1);

    public DurationFormatter getFormatter();
}

