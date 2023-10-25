package com.ibm.icu.impl.duration;

class SingleUnitBuilder extends PeriodBuilderImpl {
  SingleUnitBuilder(BasicPeriodBuilderFactory.Settings settings) {
    super(settings);
  }
  
  public static SingleUnitBuilder get(BasicPeriodBuilderFactory.Settings settings) {
    if (settings == null)
      return null; 
    return new SingleUnitBuilder(settings);
  }
  
  protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings settingsToUse) {
    return get(settingsToUse);
  }
  
  protected Period handleCreate(long duration, long referenceDate, boolean inPast) {
    short uset = this.settings.effectiveSet();
    for (int i = 0; i < TimeUnit.units.length; i++) {
      if (0 != (uset & 1 << i)) {
        TimeUnit unit = TimeUnit.units[i];
        long unitDuration = approximateDurationOf(unit);
        if (duration >= unitDuration)
          return Period.at((float)(duration / unitDuration), unit).inPast(inPast); 
      } 
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\SingleUnitBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */