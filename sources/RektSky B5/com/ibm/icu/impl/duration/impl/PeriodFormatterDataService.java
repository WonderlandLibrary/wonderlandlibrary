/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import java.util.Collection;

public abstract class PeriodFormatterDataService {
    public abstract PeriodFormatterData get(String var1);

    public abstract Collection<String> getAvailableLocales();
}

