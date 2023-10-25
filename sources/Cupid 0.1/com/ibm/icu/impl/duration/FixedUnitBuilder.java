package com.ibm.icu.impl.duration;

class FixedUnitBuilder extends PeriodBuilderImpl {
  private TimeUnit unit;
  
  public static FixedUnitBuilder get(TimeUnit unit, BasicPeriodBuilderFactory.Settings settingsToUse) {
    if (settingsToUse != null && (settingsToUse.effectiveSet() & 1 << unit.ordinal) != 0)
      return new FixedUnitBuilder(unit, settingsToUse); 
    return null;
  }
  
  FixedUnitBuilder(TimeUnit unit, BasicPeriodBuilderFactory.Settings settings) {
    super(settings);
    this.unit = unit;
  }
  
  protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
    return get(this.unit, settingsToUse);
  }
  
  protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
    if (this.unit == null)
      return null; 
    long unitDuration = approximateDurationOf(this.unit);
    return Period.at((float)(duration / unitDuration), this.unit).inPast(inPast);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\FixedUnitBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */