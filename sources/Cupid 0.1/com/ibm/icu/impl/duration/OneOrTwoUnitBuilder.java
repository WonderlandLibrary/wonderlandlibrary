package com.ibm.icu.impl.duration;

class OneOrTwoUnitBuilder extends PeriodBuilderImpl {
  OneOrTwoUnitBuilder(BasicPeriodBuilderFactory.Settings settings) {
    super(settings);
  }
  
  public static OneOrTwoUnitBuilder get(BasicPeriodBuilderFactory.Settings settings) {
    if (settings == null)
      return null; 
    return new OneOrTwoUnitBuilder(settings);
  }
  
  protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
    return get(settingsToUse);
  }
  
  protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
    Period period = null;
    short uset = this.settings.effectiveSet();
    for (int i = 0; i < TimeUnit.units.length; i++) {
      if (0 != (uset & 1 << i)) {
        TimeUnit unit = TimeUnit.units[i];
        long unitDuration = approximateDurationOf(unit);
        if (duration >= unitDuration || period != null) {
          double count = duration / unitDuration;
          if (period == null) {
            if (count >= 2.0D) {
              period = Period.at((float)count, unit);
              break;
            } 
            period = Period.at(1.0F, unit).inPast(inPast);
            duration -= unitDuration;
          } else {
            if (count >= 1.0D)
              period = period.and((float)count, unit); 
            break;
          } 
        } 
      } 
    } 
    return period;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\OneOrTwoUnitBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */