package com.ibm.icu.impl.duration;

public interface PeriodFormatterFactory {
  PeriodFormatterFactory setLocale(String paramString);
  
  PeriodFormatterFactory setDisplayLimit(boolean paramBoolean);
  
  PeriodFormatterFactory setDisplayPastFuture(boolean paramBoolean);
  
  PeriodFormatterFactory setSeparatorVariant(int paramInt);
  
  PeriodFormatterFactory setUnitVariant(int paramInt);
  
  PeriodFormatterFactory setCountVariant(int paramInt);
  
  PeriodFormatter getFormatter();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\PeriodFormatterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */