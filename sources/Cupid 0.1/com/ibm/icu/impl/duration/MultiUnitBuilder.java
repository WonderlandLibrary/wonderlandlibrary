package com.ibm.icu.impl.duration;

class MultiUnitBuilder extends PeriodBuilderImpl {
  private int nPeriods;
  
  MultiUnitBuilder(int nPeriods, BasicPeriodBuilderFactory.Settings settings) {
    super(settings);
    this.nPeriods = nPeriods;
  }
  
  public static MultiUnitBuilder get(int nPeriods, BasicPeriodBuilderFactory.Settings settings) {
    if (nPeriods > 0 && settings != null)
      return new MultiUnitBuilder(nPeriods, settings); 
    return null;
  }
  
  protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
    return get(this.nPeriods, settingsToUse);
  }
  
  protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
    Period period = null;
    int n = 0;
    short uset = this.settings.effectiveSet();
    for (int i = 0; i < TimeUnit.units.length; i++) {
      if (0 != (uset & 1 << i)) {
        TimeUnit unit = TimeUnit.units[i];
        if (n == this.nPeriods)
          break; 
        long unitDuration = approximateDurationOf(unit);
        if (duration >= unitDuration || n > 0) {
          n++;
          double count = duration / unitDuration;
          if (n < this.nPeriods) {
            count = Math.floor(count);
            duration -= (long)(count * unitDuration);
          } 
          if (period == null) {
            period = Period.at((float)count, unit).inPast(inPast);
          } else {
            period = period.and((float)count, unit);
          } 
        } 
      } 
    } 
    return period;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\MultiUnitBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */