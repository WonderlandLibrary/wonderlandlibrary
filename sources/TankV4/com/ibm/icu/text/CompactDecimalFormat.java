package com.ibm.icu.text;

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
   private final Map units;
   private final long[] divisor;
   private final String[] currencyAffixes;
   private final PluralRules pluralRules;

   public static CompactDecimalFormat getInstance(ULocale var0, CompactDecimalFormat.CompactStyle var1) {
      return new CompactDecimalFormat(var0, var1);
   }

   public static CompactDecimalFormat getInstance(Locale var0, CompactDecimalFormat.CompactStyle var1) {
      return new CompactDecimalFormat(ULocale.forLocale(var0), var1);
   }

   CompactDecimalFormat(ULocale var1, CompactDecimalFormat.CompactStyle var2) {
      DecimalFormat var3 = (DecimalFormat)NumberFormat.getInstance(var1);
      CompactDecimalDataCache.Data var4 = this.getData(var1, var2);
      this.units = var4.units;
      this.divisor = var4.divisors;
      this.applyPattern(var3.toPattern());
      this.setDecimalFormatSymbols(var3.getDecimalFormatSymbols());
      this.setMaximumSignificantDigits(3);
      this.setSignificantDigitsUsed(true);
      if (var2 == CompactDecimalFormat.CompactStyle.SHORT) {
         this.setGroupingUsed(false);
      }

      this.pluralRules = PluralRules.forLocale(var1);
      DecimalFormat var5 = (DecimalFormat)NumberFormat.getCurrencyInstance(var1);
      this.currencyAffixes = new String[2];
      this.currencyAffixes[0] = var5.getPositivePrefix();
      this.currencyAffixes[1] = var5.getPositiveSuffix();
      this.setCurrency((Currency)null);
   }

   /** @deprecated */
   public CompactDecimalFormat(String var1, DecimalFormatSymbols var2, String[] var3, String[] var4, long[] var5, Collection var6, CompactDecimalFormat.CompactStyle var7, String[] var8) {
      if (var3.length < 15) {
         this.recordError(var6, "Must have at least 15 prefix items.");
      }

      if (var3.length != var4.length || var3.length != var5.length) {
         this.recordError(var6, "Prefix, suffix, and divisor arrays must have the same length.");
      }

      long var9 = 0L;
      HashMap var11 = new HashMap();

      for(int var12 = 0; var12 < var3.length; ++var12) {
         if (var3[var12] == null || var4[var12] == null) {
            this.recordError(var6, "Prefix or suffix is null for " + var12);
         }

         int var13 = (int)Math.log10((double)var5[var12]);
         if (var13 > var12) {
            this.recordError(var6, "Divisor[" + var12 + "] must be less than or equal to 10^" + var12 + ", but is: " + var5[var12]);
         }

         long var14 = (long)Math.pow(10.0D, (double)var13);
         if (var14 != var5[var12]) {
            this.recordError(var6, "Divisor[" + var12 + "] must be a power of 10, but is: " + var5[var12]);
         }

         String var16 = var3[var12] + "\uffff" + var4[var12] + "\uffff" + (var12 - var13);
         Integer var17 = (Integer)var11.get(var16);
         if (var17 != null) {
            this.recordError(var6, "Collision between values for " + var12 + " and " + var17 + " for [prefix/suffix/index-log(divisor)" + var16.replace('\uffff', ';'));
         } else {
            var11.put(var16, var12);
         }

         if (var5[var12] < var9) {
            this.recordError(var6, "Bad divisor, the divisor for 10E" + var12 + "(" + var5[var12] + ") is less than the divisor for the divisor for 10E" + (var12 - 1) + "(" + var9 + ")");
         }

         var9 = var5[var12];
      }

      this.units = this.otherPluralVariant(var3, var4);
      this.divisor = (long[])var5.clone();
      this.applyPattern(var1);
      this.setDecimalFormatSymbols(var2);
      this.setMaximumSignificantDigits(2);
      this.setSignificantDigitsUsed(true);
      this.setGroupingUsed(false);
      this.currencyAffixes = (String[])var8.clone();
      this.pluralRules = null;
      this.setCurrency((Currency)null);
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!super.equals(var1)) {
         return false;
      } else {
         CompactDecimalFormat var2 = (CompactDecimalFormat)var1;
         return this.units != var2.units && Arrays.equals(this.divisor, var2.divisor) && Arrays.equals(this.currencyAffixes, var2.currencyAffixes) && this.pluralRules.equals(var2.pluralRules);
      }
   }

   public StringBuffer format(double var1, StringBuffer var3, FieldPosition var4) {
      CompactDecimalFormat.Amount var5 = this.toAmount(var1);
      DecimalFormat.Unit var6 = var5.getUnit();
      var6.writePrefix(var3);
      super.format(var5.getQty(), var3, var4);
      var6.writeSuffix(var3);
      return var3;
   }

   public AttributedCharacterIterator formatToCharacterIterator(Object var1) {
      if (!(var1 instanceof Number)) {
         throw new IllegalArgumentException();
      } else {
         Number var2 = (Number)var1;
         CompactDecimalFormat.Amount var3 = this.toAmount(var2.doubleValue());
         return super.formatToCharacterIterator(var3.getQty(), var3.getUnit());
      }
   }

   public StringBuffer format(long var1, StringBuffer var3, FieldPosition var4) {
      return this.format((double)var1, var3, var4);
   }

   public StringBuffer format(BigInteger var1, StringBuffer var2, FieldPosition var3) {
      return this.format(var1.doubleValue(), var2, var3);
   }

   public StringBuffer format(BigDecimal var1, StringBuffer var2, FieldPosition var3) {
      return this.format(var1.doubleValue(), var2, var3);
   }

   public StringBuffer format(com.ibm.icu.math.BigDecimal var1, StringBuffer var2, FieldPosition var3) {
      return this.format(var1.doubleValue(), var2, var3);
   }

   public Number parse(String var1, ParsePosition var2) {
      throw new UnsupportedOperationException();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      throw new NotSerializableException();
   }

   private void readObject(ObjectInputStream var1) throws IOException {
      throw new NotSerializableException();
   }

   private CompactDecimalFormat.Amount toAmount(double var1) {
      boolean var3 = this.isNumberNegative(var1);
      var1 = this.adjustNumberAsInFormatting(var1);
      int var4 = var1 <= 1.0D ? 0 : (int)Math.log10(var1);
      if (var4 >= 15) {
         var4 = 14;
      }

      var1 /= (double)this.divisor[var4];
      String var5 = this.getPluralForm(var1);
      if (var3) {
         var1 = -var1;
      }

      return new CompactDecimalFormat.Amount(var1, CompactDecimalDataCache.getUnit(this.units, var5, var4));
   }

   private void recordError(Collection var1, String var2) {
      if (var1 == null) {
         throw new IllegalArgumentException(var2);
      } else {
         var1.add(var2);
      }
   }

   private Map otherPluralVariant(String[] var1, String[] var2) {
      HashMap var3 = new HashMap();
      DecimalFormat.Unit[] var4 = new DecimalFormat.Unit[var1.length];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = new DecimalFormat.Unit(var1[var5], var2[var5]);
      }

      var3.put("other", var4);
      return var3;
   }

   private String getPluralForm(double var1) {
      return this.pluralRules == null ? "other" : this.pluralRules.select(var1);
   }

   private CompactDecimalDataCache.Data getData(ULocale var1, CompactDecimalFormat.CompactStyle var2) {
      CompactDecimalDataCache.DataBundle var3 = cache.get(var1);
      switch(var2) {
      case SHORT:
         return var3.shortData;
      case LONG:
         return var3.longData;
      default:
         return var3.shortData;
      }
   }

   private static class Amount {
      private final double qty;
      private final DecimalFormat.Unit unit;

      public Amount(double var1, DecimalFormat.Unit var3) {
         this.qty = var1;
         this.unit = var3;
      }

      public double getQty() {
         return this.qty;
      }

      public DecimalFormat.Unit getUnit() {
         return this.unit;
      }
   }

   public static enum CompactStyle {
      SHORT,
      LONG;

      private static final CompactDecimalFormat.CompactStyle[] $VALUES = new CompactDecimalFormat.CompactStyle[]{SHORT, LONG};
   }
}
