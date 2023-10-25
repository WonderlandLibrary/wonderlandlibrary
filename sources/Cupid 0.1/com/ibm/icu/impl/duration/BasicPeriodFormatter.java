package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;

class BasicPeriodFormatter implements PeriodFormatter {
  private BasicPeriodFormatterFactory factory;
  
  private String localeName;
  
  private PeriodFormatterData data;
  
  private BasicPeriodFormatterFactory.Customizations customs;
  
  BasicPeriodFormatter(BasicPeriodFormatterFactory factory, String localeName, PeriodFormatterData data, BasicPeriodFormatterFactory.Customizations customs) {
    this.factory = factory;
    this.localeName = localeName;
    this.data = data;
    this.customs = customs;
  }
  
  public String format(Period period) {
    if (!period.isSet())
      throw new IllegalArgumentException("period is not set"); 
    return format(period.timeLimit, period.inFuture, period.counts);
  }
  
  public PeriodFormatter withLocale(String locName) {
    if (!this.localeName.equals(locName)) {
      PeriodFormatterData newData = this.factory.getData(locName);
      return new BasicPeriodFormatter(this.factory, locName, newData, this.customs);
    } 
    return this;
  }
  
  private String format(int tl, boolean inFuture, int[] counts) {
    int td, mask = 0;
    int i;
    for (i = 0; i < counts.length; i++) {
      if (counts[i] > 0)
        mask |= 1 << i; 
    } 
    if (!this.data.allowZero()) {
      int n;
      for (i = 0, n = 1; i < counts.length; i++, n <<= 1) {
        if ((mask & n) != 0 && counts[i] == 1)
          mask &= n ^ 0xFFFFFFFF; 
      } 
      if (mask == 0)
        return null; 
    } 
    boolean forceD3Seconds = false;
    if (this.data.useMilliseconds() != 0 && (mask & 1 << TimeUnit.MILLISECOND.ordinal) != 0) {
      int sx = TimeUnit.SECOND.ordinal;
      int mx = TimeUnit.MILLISECOND.ordinal;
      int sf = 1 << sx;
      int mf = 1 << mx;
      switch (this.data.useMilliseconds()) {
        case 2:
          if ((mask & sf) != 0) {
            counts[sx] = counts[sx] + (counts[mx] - 1) / 1000;
            mask &= mf ^ 0xFFFFFFFF;
            forceD3Seconds = true;
          } 
          break;
        case 1:
          if ((mask & sf) == 0) {
            mask |= sf;
            counts[sx] = 1;
          } 
          counts[sx] = counts[sx] + (counts[mx] - 1) / 1000;
          mask &= mf ^ 0xFFFFFFFF;
          forceD3Seconds = true;
          break;
      } 
    } 
    int first = 0;
    int last = counts.length - 1;
    for (; first < counts.length && (mask & 1 << first) == 0; first++);
    for (; last > first && (mask & 1 << last) == 0; last--);
    boolean isZero = true;
    for (int k = first; k <= last; k++) {
      if ((mask & 1 << k) != 0 && counts[k] > 1) {
        isZero = false;
        break;
      } 
    } 
    StringBuffer sb = new StringBuffer();
    if (!this.customs.displayLimit || isZero)
      tl = 0; 
    if (!this.customs.displayDirection || isZero) {
      td = 0;
    } else {
      td = inFuture ? 2 : 1;
    } 
    boolean useDigitPrefix = this.data.appendPrefix(tl, td, sb);
    boolean multiple = (first != last);
    boolean wasSkipped = true;
    boolean skipped = false;
    boolean countSep = (this.customs.separatorVariant != 0);
    int m;
    for (int j = m = first; m <= last; m = j) {
      if (skipped) {
        this.data.appendSkippedUnit(sb);
        skipped = false;
        wasSkipped = true;
      } 
      while (++j < last && (mask & 1 << j) == 0)
        skipped = true; 
      TimeUnit unit = TimeUnit.units[m];
      int count = counts[m] - 1;
      int cv = this.customs.countVariant;
      if (m == last) {
        if (forceD3Seconds)
          cv = 5; 
      } else {
        cv = 0;
      } 
      boolean isLast = (m == last);
      boolean mustSkip = this.data.appendUnit(unit, count, cv, this.customs.unitVariant, countSep, useDigitPrefix, multiple, isLast, wasSkipped, sb);
      skipped |= mustSkip;
      wasSkipped = false;
      if (this.customs.separatorVariant != 0 && j <= last) {
        boolean afterFirst = (m == first);
        boolean beforeLast = (j == last);
        boolean fullSep = (this.customs.separatorVariant == 2);
        useDigitPrefix = this.data.appendUnitSeparator(unit, fullSep, afterFirst, beforeLast, sb);
      } else {
        useDigitPrefix = false;
      } 
    } 
    this.data.appendSuffix(tl, td, sb);
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\BasicPeriodFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */