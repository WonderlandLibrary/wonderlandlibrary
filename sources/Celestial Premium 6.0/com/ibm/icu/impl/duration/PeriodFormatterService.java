/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.DurationFormatterFactory;
import com.ibm.icu.impl.duration.PeriodBuilderFactory;
import com.ibm.icu.impl.duration.PeriodFormatterFactory;
import java.util.Collection;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface PeriodFormatterService {
    public DurationFormatterFactory newDurationFormatterFactory();

    public PeriodFormatterFactory newPeriodFormatterFactory();

    public PeriodBuilderFactory newPeriodBuilderFactory();

    public Collection<String> getAvailableLocaleNames();
}

