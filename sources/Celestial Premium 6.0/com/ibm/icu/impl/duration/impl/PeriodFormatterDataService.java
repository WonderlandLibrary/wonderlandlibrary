/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import java.util.Collection;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class PeriodFormatterDataService {
    public abstract PeriodFormatterData get(String var1);

    public abstract Collection<String> getAvailableLocales();
}

