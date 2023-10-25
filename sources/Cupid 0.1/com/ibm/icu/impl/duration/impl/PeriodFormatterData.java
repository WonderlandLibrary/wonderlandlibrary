package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.TimeUnit;
import java.util.Arrays;

public class PeriodFormatterData {
  final DataRecord dr;
  
  String localeName;
  
  public static boolean trace = false;
  
  private static final int FORM_PLURAL = 0;
  
  private static final int FORM_SINGULAR = 1;
  
  private static final int FORM_DUAL = 2;
  
  private static final int FORM_PAUCAL = 3;
  
  private static final int FORM_SINGULAR_SPELLED = 4;
  
  private static final int FORM_SINGULAR_NO_OMIT = 5;
  
  private static final int FORM_HALF_SPELLED = 6;
  
  public PeriodFormatterData(String localeName, DataRecord dr) {
    this.dr = dr;
    this.localeName = localeName;
    if (localeName == null)
      throw new NullPointerException("localename is null"); 
    if (dr == null)
      throw new NullPointerException("data record is null"); 
  }
  
  public int pluralization() {
    return this.dr.pl;
  }
  
  public boolean allowZero() {
    return this.dr.allowZero;
  }
  
  public boolean weeksAloneOnly() {
    return this.dr.weeksAloneOnly;
  }
  
  public int useMilliseconds() {
    return this.dr.useMilliseconds;
  }
  
  public boolean appendPrefix(int tl, int td, StringBuffer sb) {
    if (this.dr.scopeData != null) {
      int ix = tl * 3 + td;
      DataRecord.ScopeData sd = this.dr.scopeData[ix];
      if (sd != null) {
        String prefix = sd.prefix;
        if (prefix != null) {
          sb.append(prefix);
          return sd.requiresDigitPrefix;
        } 
      } 
    } 
    return false;
  }
  
  public void appendSuffix(int tl, int td, StringBuffer sb) {
    if (this.dr.scopeData != null) {
      int ix = tl * 3 + td;
      DataRecord.ScopeData sd = this.dr.scopeData[ix];
      if (sd != null) {
        String suffix = sd.suffix;
        if (suffix != null) {
          if (trace)
            System.out.println("appendSuffix '" + suffix + "'"); 
          sb.append(suffix);
        } 
      } 
    } 
  }
  
  public boolean appendUnit(TimeUnit unit, int count, int cv, int uv, boolean useCountSep, boolean useDigitPrefix, boolean multiple, boolean last, boolean wasSkipped, StringBuffer sb) {
    int px = unit.ordinal();
    boolean willRequireSkipMarker = false;
    if (this.dr.requiresSkipMarker != null && this.dr.requiresSkipMarker[px] && this.dr.skippedUnitMarker != null) {
      if (!wasSkipped && last)
        sb.append(this.dr.skippedUnitMarker); 
      willRequireSkipMarker = true;
    } 
    if (uv != 0) {
      boolean useMedium = (uv == 1);
      String[] names = useMedium ? this.dr.mediumNames : this.dr.shortNames;
      if (names == null || names[px] == null)
        names = useMedium ? this.dr.shortNames : this.dr.mediumNames; 
      if (names != null && names[px] != null) {
        appendCount(unit, false, false, count, cv, useCountSep, names[px], last, sb);
        return false;
      } 
    } 
    if (cv == 2 && this.dr.halfSupport != null)
      switch (this.dr.halfSupport[px]) {
        case 2:
          if (count > 1000)
            break; 
        case 1:
          count = count / 500 * 500;
          cv = 3;
          break;
      }  
    String name = null;
    int form = computeForm(unit, count, cv, (multiple && last));
    if (form == 4) {
      if (this.dr.singularNames == null) {
        form = 1;
        name = this.dr.pluralNames[px][form];
      } else {
        name = this.dr.singularNames[px];
      } 
    } else if (form == 5) {
      name = this.dr.pluralNames[px][1];
    } else if (form == 6) {
      name = this.dr.halfNames[px];
    } else {
      try {
        name = this.dr.pluralNames[px][form];
      } catch (NullPointerException e) {
        System.out.println("Null Pointer in PeriodFormatterData[" + this.localeName + "].au px: " + px + " form: " + form + " pn: " + Arrays.toString((Object[])this.dr.pluralNames));
        throw e;
      } 
    } 
    if (name == null) {
      form = 0;
      name = this.dr.pluralNames[px][form];
    } 
    boolean omitCount = (form == 4 || form == 6 || (this.dr.omitSingularCount && form == 1) || (this.dr.omitDualCount && form == 2));
    int suffixIndex = appendCount(unit, omitCount, useDigitPrefix, count, cv, useCountSep, name, last, sb);
    if (last && suffixIndex >= 0) {
      String suffix = null;
      if (this.dr.rqdSuffixes != null && suffixIndex < this.dr.rqdSuffixes.length)
        suffix = this.dr.rqdSuffixes[suffixIndex]; 
      if (suffix == null && this.dr.optSuffixes != null && suffixIndex < this.dr.optSuffixes.length)
        suffix = this.dr.optSuffixes[suffixIndex]; 
      if (suffix != null)
        sb.append(suffix); 
    } 
    return willRequireSkipMarker;
  }
  
  public int appendCount(TimeUnit unit, boolean omitCount, boolean useDigitPrefix, int count, int cv, boolean useSep, String name, boolean last, StringBuffer sb) {
    int val, v, decimals;
    if (cv == 2 && this.dr.halves == null)
      cv = 0; 
    if (!omitCount && useDigitPrefix && this.dr.digitPrefix != null)
      sb.append(this.dr.digitPrefix); 
    int index = unit.ordinal();
    switch (cv) {
      case 0:
        if (!omitCount)
          appendInteger(count / 1000, 1, 10, sb); 
        break;
      case 1:
        val = count / 1000;
        if (unit == TimeUnit.MINUTE && (this.dr.fiveMinutes != null || this.dr.fifteenMinutes != null))
          if (val != 0 && val % 5 == 0) {
            if (this.dr.fifteenMinutes != null && (val == 15 || val == 45)) {
              val = (val == 15) ? 1 : 3;
              if (!omitCount)
                appendInteger(val, 1, 10, sb); 
              name = this.dr.fifteenMinutes;
              index = 8;
              break;
            } 
            if (this.dr.fiveMinutes != null) {
              val /= 5;
              if (!omitCount)
                appendInteger(val, 1, 10, sb); 
              name = this.dr.fiveMinutes;
              index = 9;
              break;
            } 
          }  
        if (!omitCount)
          appendInteger(val, 1, 10, sb); 
        break;
      case 2:
        v = count / 500;
        if (v != 1 && 
          !omitCount)
          appendCountValue(count, 1, 0, sb); 
        if ((v & 0x1) == 1) {
          if (v == 1 && this.dr.halfNames != null && this.dr.halfNames[index] != null) {
            sb.append(name);
            return last ? index : -1;
          } 
          int solox = (v == 1) ? 0 : 1;
          if (this.dr.genders != null && this.dr.halves.length > 2 && 
            this.dr.genders[index] == 1)
            solox += 2; 
          int hp = (this.dr.halfPlacements == null) ? 0 : this.dr.halfPlacements[solox & 0x1];
          String half = this.dr.halves[solox];
          String measure = (this.dr.measures == null) ? null : this.dr.measures[index];
          switch (hp) {
            case 0:
              sb.append(half);
              break;
            case 1:
              if (measure != null) {
                sb.append(measure);
                sb.append(half);
                if (useSep && !omitCount)
                  sb.append(this.dr.countSep); 
                sb.append(name);
              } else {
                sb.append(name);
                sb.append(half);
                return last ? index : -1;
              } 
              return -1;
            case 2:
              if (measure != null)
                sb.append(measure); 
              if (useSep && !omitCount)
                sb.append(this.dr.countSep); 
              sb.append(name);
              sb.append(half);
              return last ? index : -1;
          } 
        } 
        break;
      default:
        decimals = 1;
        switch (cv) {
          case 4:
            decimals = 2;
            break;
          case 5:
            decimals = 3;
            break;
        } 
        if (!omitCount)
          appendCountValue(count, 1, decimals, sb); 
        break;
    } 
    if (!omitCount && useSep)
      sb.append(this.dr.countSep); 
    if (!omitCount && this.dr.measures != null && index < this.dr.measures.length) {
      String measure = this.dr.measures[index];
      if (measure != null)
        sb.append(measure); 
    } 
    sb.append(name);
    return last ? index : -1;
  }
  
  public void appendCountValue(int count, int integralDigits, int decimalDigits, StringBuffer sb) {
    int ival = count / 1000;
    if (decimalDigits == 0) {
      appendInteger(ival, integralDigits, 10, sb);
      return;
    } 
    if (this.dr.requiresDigitSeparator && sb.length() > 0)
      sb.append(' '); 
    appendDigits(ival, integralDigits, 10, sb);
    int dval = count % 1000;
    if (decimalDigits == 1) {
      dval /= 100;
    } else if (decimalDigits == 2) {
      dval /= 10;
    } 
    sb.append(this.dr.decimalSep);
    appendDigits(dval, decimalDigits, decimalDigits, sb);
    if (this.dr.requiresDigitSeparator)
      sb.append(' '); 
  }
  
  public void appendInteger(int num, int mindigits, int maxdigits, StringBuffer sb) {
    if (this.dr.numberNames != null && num < this.dr.numberNames.length) {
      String name = this.dr.numberNames[num];
      if (name != null) {
        sb.append(name);
        return;
      } 
    } 
    if (this.dr.requiresDigitSeparator && sb.length() > 0)
      sb.append(' '); 
    switch (this.dr.numberSystem) {
      case 0:
        appendDigits(num, mindigits, maxdigits, sb);
        break;
      case 1:
        sb.append(Utils.chineseNumber(num, Utils.ChineseDigits.TRADITIONAL));
        break;
      case 2:
        sb.append(Utils.chineseNumber(num, Utils.ChineseDigits.SIMPLIFIED));
        break;
      case 3:
        sb.append(Utils.chineseNumber(num, Utils.ChineseDigits.KOREAN));
        break;
    } 
    if (this.dr.requiresDigitSeparator)
      sb.append(' '); 
  }
  
  public void appendDigits(long num, int mindigits, int maxdigits, StringBuffer sb) {
    char[] buf = new char[maxdigits];
    int ix = maxdigits;
    while (ix > 0 && num > 0L) {
      buf[--ix] = (char)(int)(this.dr.zero + num % 10L);
      num /= 10L;
    } 
    for (int e = maxdigits - mindigits; ix > e;)
      buf[--ix] = this.dr.zero; 
    sb.append(buf, ix, maxdigits - ix);
  }
  
  public void appendSkippedUnit(StringBuffer sb) {
    if (this.dr.skippedUnitMarker != null)
      sb.append(this.dr.skippedUnitMarker); 
  }
  
  public boolean appendUnitSeparator(TimeUnit unit, boolean longSep, boolean afterFirst, boolean beforeLast, StringBuffer sb) {
    if ((longSep && this.dr.unitSep != null) || this.dr.shortUnitSep != null) {
      if (longSep && this.dr.unitSep != null) {
        int ix = (afterFirst ? 2 : 0) + (beforeLast ? 1 : 0);
        sb.append(this.dr.unitSep[ix]);
        return (this.dr.unitSepRequiresDP != null && this.dr.unitSepRequiresDP[ix]);
      } 
      sb.append(this.dr.shortUnitSep);
    } 
    return false;
  }
  
  private int computeForm(TimeUnit unit, int count, int cv, boolean lastOfMultiple) {
    int v, i;
    if (trace) {
      System.err.println("pfd.cf unit: " + unit + " count: " + count + " cv: " + cv + " dr.pl: " + this.dr.pl);
      Thread.dumpStack();
    } 
    if (this.dr.pl == 0)
      return 0; 
    int val = count / 1000;
    switch (cv) {
      case 0:
      case 1:
        break;
      case 2:
        switch (this.dr.fractionHandling) {
          case 0:
            return 0;
          case 1:
          case 2:
            v = count / 500;
            if (v == 1) {
              if (this.dr.halfNames != null && this.dr.halfNames[unit.ordinal()] != null)
                return 6; 
              return 5;
            } 
            if ((v & 0x1) == 1) {
              if (this.dr.pl == 5 && v > 21)
                return 5; 
              if (v == 3 && this.dr.pl == 1 && this.dr.fractionHandling != 2)
                return 0; 
            } 
            break;
          case 3:
            v = count / 500;
            if (v == 1 || v == 3)
              return 3; 
            break;
        } 
        throw new IllegalStateException();
      default:
        switch (this.dr.decimalHandling) {
          case 1:
            return 5;
          case 2:
            if (count < 1000)
              return 5; 
            break;
          case 3:
            if (this.dr.pl == 3)
              return 3; 
            break;
        } 
        return 0;
    } 
    if (trace && count == 0)
      System.err.println("EZeroHandling = " + this.dr.zeroHandling); 
    if (count == 0 && this.dr.zeroHandling == 1)
      return 4; 
    int form = 0;
    switch (this.dr.pl) {
      case 0:
        return form;
      case 1:
        if (val == 1)
          form = 4; 
      case 2:
        if (val == 2) {
          form = 2;
        } else if (val == 1) {
          form = 1;
        } 
      case 3:
        i = val;
        i %= 100;
        if (i > 20)
          i %= 10; 
        if (i == 1) {
          form = 1;
        } else if (i > 1 && i < 5) {
          form = 3;
        } 
      case 4:
        if (val == 2) {
          form = 2;
        } else if (val == 1) {
          if (lastOfMultiple) {
            form = 4;
          } else {
            form = 1;
          } 
        } else if (unit == TimeUnit.YEAR && val > 11) {
          form = 5;
        } 
      case 5:
        if (val == 2) {
          form = 2;
        } else if (val == 1) {
          form = 1;
        } else if (val > 10) {
          form = 5;
        } 
    } 
    System.err.println("dr.pl is " + this.dr.pl);
    throw new IllegalStateException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\PeriodFormatterData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */