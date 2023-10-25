/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

public interface DurationFormatter {
    public String formatDurationFromNowTo(Date var1);

    public String formatDurationFromNow(long var1);

    public String formatDurationFrom(long var1, long var3);

    public DurationFormatter withLocale(String var1);

    public DurationFormatter withTimeZone(TimeZone var1);
}

