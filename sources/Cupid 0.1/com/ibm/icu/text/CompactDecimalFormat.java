package com.ibm.icu.text;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CompactDecimalFormat extends DecimalFormat {
  private static final long serialVersionUID = 4716293295276629682L;
  
  private static final int POSITIVE_PREFIX = 0;
  
  private static final int POSITIVE_SUFFIX = 1;
  
  private static final int AFFIX_SIZE = 2;
  
  private static final CompactDecimalDataCache cache = new CompactDecimalDataCache();
  
  private final Map<String, DecimalFormat.Unit[]> units;
  
  private final long[] divisor;
  
  private final String[] currencyAffixes;
  
  private final PluralRules pluralRules;
  
  public enum CompactStyle {
    SHORT, LONG;
  }
  
  public static CompactDecimalFormat getInstance(ULocale locale, CompactStyle style) {
    return new CompactDecimalFormat(locale, style);
  }
  
  public static CompactDecimalFormat getInstance(Locale locale, CompactStyle style) {
    return new CompactDecimalFormat(ULocale.forLocale(locale), style);
  }
  
  CompactDecimalFormat(ULocale locale, CompactStyle style) {
    DecimalFormat format = (DecimalFormat)NumberFormat.getInstance(locale);
    CompactDecimalDataCache.Data data = getData(locale, style);
    this.units = data.units;
    this.divisor = data.divisors;
    applyPattern(format.toPattern());
    setDecimalFormatSymbols(format.getDecimalFormatSymbols());
    setMaximumSignificantDigits(3);
    setSignificantDigitsUsed(true);
    if (style == CompactStyle.SHORT)
      setGroupingUsed(false); 
    this.pluralRules = PluralRules.forLocale(locale);
    DecimalFormat currencyFormat = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
    this.currencyAffixes = new String[2];
    this.currencyAffixes[0] = currencyFormat.getPositivePrefix();
    this.currencyAffixes[1] = currencyFormat.getPositiveSuffix();
    setCurrency((Currency)null);
  }
  
  public CompactDecimalFormat(String pattern, DecimalFormatSymbols formatSymbols, String[] prefix, String[] suffix, long[] divisor, Collection<String> debugCreationErrors, CompactStyle style, String[] currencyAffixes) {
    if (prefix.length < 15)
      recordError(debugCreationErrors, "Must have at least 15 prefix items."); 
    if (prefix.length != suffix.length || prefix.length != divisor.length)
      recordError(debugCreationErrors, "Prefix, suffix, and divisor arrays must have the same length."); 
    long oldDivisor = 0L;
    Map<String, Integer> seen = new HashMap<String, Integer>();
    for (int i = 0; i < prefix.length; i++) {
      if (prefix[i] == null || suffix[i] == null)
        recordError(debugCreationErrors, "Prefix or suffix is null for " + i); 
      int log = (int)Math.log10(divisor[i]);
      if (log > i)
        recordError(debugCreationErrors, "Divisor[" + i + "] must be less than or equal to 10^" + i + ", but is: " + divisor[i]); 
      long roundTrip = (long)Math.pow(10.0D, log);
      if (roundTrip != divisor[i])
        recordError(debugCreationErrors, "Divisor[" + i + "] must be a power of 10, but is: " + divisor[i]); 
      String key = prefix[i] + "￿" + suffix[i] + "￿" + (i - log);
      Integer old = seen.get(key);
      if (old != null) {
        recordError(debugCreationErrors, "Collision between values for " + i + " and " + old + " for [prefix/suffix/index-log(divisor)" + key.replace('￿', ';'));
      } else {
        seen.put(key, Integer.valueOf(i));
      } 
      if (divisor[i] < oldDivisor)
        recordError(debugCreationErrors, "Bad divisor, the divisor for 10E" + i + "(" + divisor[i] + ") is less than the divisor for the divisor for 10E" + (i - 1) + "(" + oldDivisor + ")"); 
      oldDivisor = divisor[i];
    } 
    this.units = otherPluralVariant(prefix, suffix);
    this.divisor = (long[])divisor.clone();
    applyPattern(pattern);
    setDecimalFormatSymbols(formatSymbols);
    setMaximumSignificantDigits(2);
    setSignificantDigitsUsed(true);
    setGroupingUsed(false);
    this.currencyAffixes = (String[])currencyAffixes.clone();
    this.pluralRules = null;
    setCurrency((Currency)null);
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (!super.equals(obj))
      return false; 
    CompactDecimalFormat other = (CompactDecimalFormat)obj;
    return (mapsAreEqual(this.units, other.units) && Arrays.equals(this.divisor, other.divisor) && Arrays.equals((Object[])this.currencyAffixes, (Object[])other.currencyAffixes) && this.pluralRules.equals(other.pluralRules));
  }
  
  private boolean mapsAreEqual(Map<String, DecimalFormat.Unit[]> lhs, Map<String, DecimalFormat.Unit[]> rhs) {
    if (lhs.size() != rhs.size())
      return false; 
    for (Map.Entry<String, DecimalFormat.Unit[]> entry : lhs.entrySet()) {
      DecimalFormat.Unit[] value = rhs.get(entry.getKey());
      if (value == null || !Arrays.equals((Object[])entry.getValue(), (Object[])value))
        return false; 
    } 
    return true;
  }
  
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
    Amount amount = toAmount(number);
    DecimalFormat.Unit unit = amount.getUnit();
    unit.writePrefix(toAppendTo);
    super.format(amount.getQty(), toAppendTo, pos);
    unit.writeSuffix(toAppendTo);
    return toAppendTo;
  }
  
  public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
    if (!(obj instanceof Number))
      throw new IllegalArgumentException(); 
    Number number = (Number)obj;
    Amount amount = toAmount(number.doubleValue());
    return formatToCharacterIterator(Double.valueOf(amount.getQty()), amount.getUnit());
  }
  
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(number, toAppendTo, pos);
  }
  
  public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(number.doubleValue(), toAppendTo, pos);
  }
  
  public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(number.doubleValue(), toAppendTo, pos);
  }
  
  public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
    return format(number.doubleValue(), toAppendTo, pos);
  }
  
  public Number parse(String text, ParsePosition parsePosition) {
    throw new UnsupportedOperationException();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    throw new NotSerializableException();
  }
  
  private void readObject(ObjectInputStream in) throws IOException {
    throw new NotSerializableException();
  }
  
  private Amount toAmount(double number) {
    boolean negative = isNumberNegative(number);
    number = adjustNumberAsInFormatting(number);
    int base = (number <= 1.0D) ? 0 : (int)Math.log10(number);
    if (base >= 15)
      base = 14; 
    number /= this.divisor[base];
    String pluralVariant = getPluralForm(number);
    if (negative)
      number = -number; 
    return new Amount(number, CompactDecimalDataCache.getUnit(this.units, pluralVariant, base));
  }
  
  private void recordError(Collection<String> creationErrors, String errorMessage) {
    if (creationErrors == null)
      throw new IllegalArgumentException(errorMessage); 
    creationErrors.add(errorMessage);
  }
  
  private Map<String, DecimalFormat.Unit[]> otherPluralVariant(String[] prefix, String[] suffix) {
    Map<String, DecimalFormat.Unit[]> result = (Map)new HashMap<String, DecimalFormat.Unit>();
    DecimalFormat.Unit[] units = new DecimalFormat.Unit[prefix.length];
    for (int i = 0; i < units.length; i++)
      units[i] = new DecimalFormat.Unit(prefix[i], suffix[i]); 
    result.put("other", units);
    return result;
  }
  
  private String getPluralForm(double number) {
    if (this.pluralRules == null)
      return "other"; 
    return this.pluralRules.select(number);
  }
  
  private CompactDecimalDataCache.Data getData(ULocale locale, CompactStyle style) {
    CompactDecimalDataCache.DataBundle bundle = cache.get(locale);
    switch (style) {
      case SHORT:
        return bundle.shortData;
      case LONG:
        return bundle.longData;
    } 
    return bundle.shortData;
  }
  
  private static class Amount {
    private final double qty;
    
    private final DecimalFormat.Unit unit;
    
    public Amount(double qty, DecimalFormat.Unit unit) {
      this.qty = qty;
      this.unit = unit;
    }
    
    public double getQty() {
      return this.qty;
    }
    
    public DecimalFormat.Unit getUnit() {
      return this.unit;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CompactDecimalFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */