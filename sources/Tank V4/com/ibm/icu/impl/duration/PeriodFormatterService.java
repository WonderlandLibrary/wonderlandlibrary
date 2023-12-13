package com.ibm.icu.impl.duration;

import java.util.Collection;

public interface PeriodFormatterService {
   DurationFormatterFactory newDurationFormatterFactory();

   PeriodFormatterFactory newPeriodFormatterFactory();

   PeriodBuilderFactory newPeriodBuilderFactory();

   Collection getAvailableLocaleNames();
}
