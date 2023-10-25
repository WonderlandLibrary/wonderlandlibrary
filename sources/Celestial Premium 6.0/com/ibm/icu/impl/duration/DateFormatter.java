/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

public interface DateFormatter {
    public String format(Date var1);

    public String format(long var1);

    public DateFormatter withLocale(String var1);

    public DateFormatter withTimeZone(TimeZone var1);
}

