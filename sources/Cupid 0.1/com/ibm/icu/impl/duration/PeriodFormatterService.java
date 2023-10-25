package com.ibm.icu.impl.duration;

import java.util.Collection;

public interface PeriodFormatterService {
  DurationFormatterFactory newDurationFormatterFactory();
  
  PeriodFormatterFactory newPeriodFormatterFactory();
  
  PeriodBuilderFactory newPeriodBuilderFactory();
  
  Collection<String> getAvailableLocaleNames();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\PeriodFormatterService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */