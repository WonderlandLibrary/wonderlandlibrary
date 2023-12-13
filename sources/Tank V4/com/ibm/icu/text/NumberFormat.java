package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public abstract class NumberFormat extends UFormat {
   public static final int NUMBERSTYLE = 0;
   public static final int CURRENCYSTYLE = 1;
   public static final int PERCENTSTYLE = 2;
   public static final int SCIENTIFICSTYLE = 3;
   public static final int INTEGERSTYLE = 4;
   public static final int ISOCURRENCYSTYLE = 5;
   public static final int PLURALCURRENCYSTYLE = 6;
   public static final int INTEGER_FIELD = 0;
   public static final int FRACTION_FIELD = 1;
   private static NumberFormat.NumberFormatShim shim;
   private static final char[] doubleCurrencySign = new char[]{'¤', '¤'};
   private static final String doubleCurrencyStr;
   private boolean groupingUsed = true;
   private byte maxIntegerDigits = 40;
   private byte minIntegerDigits = 1;
   private byte maxFractionDigits = 3;
   private byte minFractionDigits = 0;
   private boolean parseIntegerOnly = false;
   private int maximumIntegerDigits = 40;
   private int minimumIntegerDigits = 1;
   private int maximumFractionDigits = 3;
   private int minimumFractionDigits = 0;
   private Currency currency;
   static final int currentSerialVersion = 1;
   private int serialVersionOnStream = 1;
   private static final long serialVersionUID = -2308460125733713944L;
   private boolean parseStrict;

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof Long) {
         return this.format((Long)var1, var2, var3);
      } else if (var1 instanceof BigInteger) {
         return this.format((BigInteger)var1, var2, var3);
      } else if (var1 instanceof BigDecimal) {
         return this.format((BigDecimal)var1, var2, var3);
      } else if (var1 instanceof com.ibm.icu.math.BigDecimal) {
         return this.format((com.ibm.icu.math.BigDecimal)var1, var2, var3);
      } else if (var1 instanceof CurrencyAmount) {
         return this.format((CurrencyAmount)var1, var2, var3);
      } else if (var1 instanceof Number) {
         return this.format(((Number)var1).doubleValue(), var2, var3);
      } else {
         throw new IllegalArgumentException("Cannot format given Object as a Number");
      }
   }

   public final Object parseObject(String var1, ParsePosition var2) {
      return this.parse(var1, var2);
   }

   public final String format(double var1) {
      return this.format(var1, new StringBuffer(), new FieldPosition(0)).toString();
   }

   public final String format(long var1) {
      StringBuffer var3 = new StringBuffer(19);
      FieldPosition var4 = new FieldPosition(0);
      this.format(var1, var3, var4);
      return var3.toString();
   }

   public final String format(BigInteger var1) {
      return this.format(var1, new StringBuffer(), new FieldPosition(0)).toString();
   }

   public final String format(BigDecimal var1) {
      return this.format(var1, new StringBuffer(), new FieldPosition(0)).toString();
   }

   public final String format(com.ibm.icu.math.BigDecimal var1) {
      return this.format(var1, new StringBuffer(), new FieldPosition(0)).toString();
   }

   public final String format(CurrencyAmount var1) {
      return this.format(var1, new StringBuffer(), new FieldPosition(0)).toString();
   }

   public abstract StringBuffer format(double var1, StringBuffer var3, FieldPosition var4);

   public abstract StringBuffer format(long var1, StringBuffer var3, FieldPosition var4);

   public abstract StringBuffer format(BigInteger var1, StringBuffer var2, FieldPosition var3);

   public abstract StringBuffer format(BigDecimal var1, StringBuffer var2, FieldPosition var3);

   public abstract StringBuffer format(com.ibm.icu.math.BigDecimal var1, StringBuffer var2, FieldPosition var3);

   public StringBuffer format(CurrencyAmount var1, StringBuffer var2, FieldPosition var3) {
      Currency var4 = this.getCurrency();
      Currency var5 = var1.getCurrency();
      boolean var6 = var5.equals(var4);
      if (!var6) {
         this.setCurrency(var5);
      }

      this.format((Object)var1.getNumber(), var2, var3);
      if (!var6) {
         this.setCurrency(var4);
      }

      return var2;
   }

   public abstract Number parse(String var1, ParsePosition var2);

   public Number parse(String var1) throws ParseException {
      ParsePosition var2 = new ParsePosition(0);
      Number var3 = this.parse(var1, var2);
      if (var2.getIndex() == 0) {
         throw new ParseException("Unparseable number: \"" + var1 + '"', var2.getErrorIndex());
      } else {
         return var3;
      }
   }

   public CurrencyAmount parseCurrency(CharSequence var1, ParsePosition var2) {
      Number var3 = this.parse(var1.toString(), var2);
      return var3 == null ? null : new CurrencyAmount(var3, this.getEffectiveCurrency());
   }

   public boolean isParseIntegerOnly() {
      return this.parseIntegerOnly;
   }

   public void setParseIntegerOnly(boolean var1) {
      this.parseIntegerOnly = var1;
   }

   public void setParseStrict(boolean var1) {
      this.parseStrict = var1;
   }

   public boolean isParseStrict() {
      return this.parseStrict;
   }

   public static final NumberFormat getInstance() {
      return getInstance((ULocale)ULocale.getDefault(ULocale.Category.FORMAT), 0);
   }

   public static NumberFormat getInstance(Locale var0) {
      return getInstance((ULocale)ULocale.forLocale(var0), 0);
   }

   public static NumberFormat getInstance(ULocale var0) {
      return getInstance((ULocale)var0, 0);
   }

   public static final NumberFormat getInstance(int var0) {
      return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), var0);
   }

   public static NumberFormat getInstance(Locale var0, int var1) {
      return getInstance(ULocale.forLocale(var0), var1);
   }

   public static final NumberFormat getNumberInstance() {
      return getInstance((ULocale)ULocale.getDefault(ULocale.Category.FORMAT), 0);
   }

   public static NumberFormat getNumberInstance(Locale var0) {
      return getInstance((ULocale)ULocale.forLocale(var0), 0);
   }

   public static NumberFormat getNumberInstance(ULocale var0) {
      return getInstance((ULocale)var0, 0);
   }

   public static final NumberFormat getIntegerInstance() {
      return getInstance((ULocale)ULocale.getDefault(ULocale.Category.FORMAT), 4);
   }

   public static NumberFormat getIntegerInstance(Locale var0) {
      return getInstance((ULocale)ULocale.forLocale(var0), 4);
   }

   public static NumberFormat getIntegerInstance(ULocale var0) {
      return getInstance((ULocale)var0, 4);
   }

   public static final NumberFormat getCurrencyInstance() {
      return getInstance((ULocale)ULocale.getDefault(ULocale.Category.FORMAT), 1);
   }

   public static NumberFormat getCurrencyInstance(Locale var0) {
      return getInstance((ULocale)ULocale.forLocale(var0), 1);
   }

   public static NumberFormat getCurrencyInstance(ULocale var0) {
      return getInstance((ULocale)var0, 1);
   }

   public static final NumberFormat getPercentInstance() {
      return getInstance((ULocale)ULocale.getDefault(ULocale.Category.FORMAT), 2);
   }

   public static NumberFormat getPercentInstance(Locale var0) {
      return getInstance((ULocale)ULocale.forLocale(var0), 2);
   }

   public static NumberFormat getPercentInstance(ULocale var0) {
      return getInstance((ULocale)var0, 2);
   }

   public static final NumberFormat getScientificInstance() {
      return getInstance((ULocale)ULocale.getDefault(ULocale.Category.FORMAT), 3);
   }

   public static NumberFormat getScientificInstance(Locale var0) {
      return getInstance((ULocale)ULocale.forLocale(var0), 3);
   }

   public static NumberFormat getScientificInstance(ULocale var0) {
      return getInstance((ULocale)var0, 3);
   }

   private static NumberFormat.NumberFormatShim getShim() {
      if (shim == null) {
         try {
            Class var0 = Class.forName("com.ibm.icu.text.NumberFormatServiceShim");
            shim = (NumberFormat.NumberFormatShim)var0.newInstance();
         } catch (MissingResourceException var1) {
            throw var1;
         } catch (Exception var2) {
            throw new RuntimeException(var2.getMessage());
         }
      }

      return shim;
   }

   public static Locale[] getAvailableLocales() {
      return shim == null ? ICUResourceBundle.getAvailableLocales() : getShim().getAvailableLocales();
   }

   public static ULocale[] getAvailableULocales() {
      return shim == null ? ICUResourceBundle.getAvailableULocales() : getShim().getAvailableULocales();
   }

   public static Object registerFactory(NumberFormat.NumberFormatFactory var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("factory must not be null");
      } else {
         return getShim().registerFactory(var0);
      }
   }

   public static boolean unregister(Object var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("registryKey must not be null");
      } else {
         return shim == null ? false : shim.unregister(var0);
      }
   }

   public int hashCode() {
      return this.maximumIntegerDigits * 37 + this.maxFractionDigits;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         NumberFormat var2 = (NumberFormat)var1;
         return this.maximumIntegerDigits == var2.maximumIntegerDigits && this.minimumIntegerDigits == var2.minimumIntegerDigits && this.maximumFractionDigits == var2.maximumFractionDigits && this.minimumFractionDigits == var2.minimumFractionDigits && this.groupingUsed == var2.groupingUsed && this.parseIntegerOnly == var2.parseIntegerOnly && this.parseStrict == var2.parseStrict;
      }
   }

   public Object clone() {
      NumberFormat var1 = (NumberFormat)super.clone();
      return var1;
   }

   public boolean isGroupingUsed() {
      return this.groupingUsed;
   }

   public void setGroupingUsed(boolean var1) {
      this.groupingUsed = var1;
   }

   public int getMaximumIntegerDigits() {
      return this.maximumIntegerDigits;
   }

   public void setMaximumIntegerDigits(int var1) {
      this.maximumIntegerDigits = Math.max(0, var1);
      if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
         this.minimumIntegerDigits = this.maximumIntegerDigits;
      }

   }

   public int getMinimumIntegerDigits() {
      return this.minimumIntegerDigits;
   }

   public void setMinimumIntegerDigits(int var1) {
      this.minimumIntegerDigits = Math.max(0, var1);
      if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
         this.maximumIntegerDigits = this.minimumIntegerDigits;
      }

   }

   public int getMaximumFractionDigits() {
      return this.maximumFractionDigits;
   }

   public void setMaximumFractionDigits(int var1) {
      this.maximumFractionDigits = Math.max(0, var1);
      if (this.maximumFractionDigits < this.minimumFractionDigits) {
         this.minimumFractionDigits = this.maximumFractionDigits;
      }

   }

   public int getMinimumFractionDigits() {
      return this.minimumFractionDigits;
   }

   public void setMinimumFractionDigits(int var1) {
      this.minimumFractionDigits = Math.max(0, var1);
      if (this.maximumFractionDigits < this.minimumFractionDigits) {
         this.maximumFractionDigits = this.minimumFractionDigits;
      }

   }

   public void setCurrency(Currency var1) {
      this.currency = var1;
   }

   public Currency getCurrency() {
      return this.currency;
   }

   /** @deprecated */
   @Deprecated
   protected Currency getEffectiveCurrency() {
      Currency var1 = this.getCurrency();
      if (var1 == null) {
         ULocale var2 = this.getLocale(ULocale.VALID_LOCALE);
         if (var2 == null) {
            var2 = ULocale.getDefault(ULocale.Category.FORMAT);
         }

         var1 = Currency.getInstance(var2);
      }

      return var1;
   }

   public int getRoundingMode() {
      throw new UnsupportedOperationException("getRoundingMode must be implemented by the subclass implementation.");
   }

   public void setRoundingMode(int var1) {
      throw new UnsupportedOperationException("setRoundingMode must be implemented by the subclass implementation.");
   }

   public static NumberFormat getInstance(ULocale var0, int var1) {
      if (var1 >= 0 && var1 <= 6) {
         return getShim().createInstance(var0, var1);
      } else {
         throw new IllegalArgumentException("choice should be from NUMBERSTYLE to PLURALCURRENCYSTYLE");
      }
   }

   static NumberFormat createInstance(ULocale var0, int var1) {
      String var2 = getPattern(var0, var1);
      DecimalFormatSymbols var3 = new DecimalFormatSymbols(var0);
      if (var1 == 1 || var1 == 5) {
         String var4 = var3.getCurrencyPattern();
         if (var4 != null) {
            var2 = var4;
         }
      }

      if (var1 == 5) {
         var2 = var2.replace("¤", doubleCurrencyStr);
      }

      NumberingSystem var14 = NumberingSystem.getInstance(var0);
      if (var14 == null) {
         return null;
      } else {
         Object var5;
         if (var14 != null && var14.isAlgorithmic()) {
            byte var10 = 4;
            String var15 = var14.getDescription();
            int var11 = var15.indexOf("/");
            int var12 = var15.lastIndexOf("/");
            String var8;
            ULocale var9;
            if (var12 > var11) {
               String var13 = var15.substring(0, var11);
               String var7 = var15.substring(var11 + 1, var12);
               var8 = var15.substring(var12 + 1);
               var9 = new ULocale(var13);
               if (var7.equals("SpelloutRules")) {
                  var10 = 1;
               }
            } else {
               var9 = var0;
               var8 = var15;
            }

            RuleBasedNumberFormat var18 = new RuleBasedNumberFormat(var9, var10);
            var18.setDefaultRuleSet(var8);
            var5 = var18;
         } else {
            DecimalFormat var6 = new DecimalFormat(var2, var3, var1);
            if (var1 == 4) {
               var6.setMaximumFractionDigits(0);
               var6.setDecimalSeparatorAlwaysShown(false);
               var6.setParseIntegerOnly(true);
            }

            var5 = var6;
         }

         ULocale var16 = var3.getLocale(ULocale.VALID_LOCALE);
         ULocale var17 = var3.getLocale(ULocale.ACTUAL_LOCALE);
         ((NumberFormat)var5).setLocale(var16, var17);
         return (NumberFormat)var5;
      }
   }

   /** @deprecated */
   @Deprecated
   protected static String getPattern(Locale var0, int var1) {
      return getPattern(ULocale.forLocale(var0), var1);
   }

   protected static String getPattern(ULocale var0, int var1) {
      int var2 = var1 == 4 ? 0 : (var1 != 5 && var1 != 6 ? var1 : 1);
      ICUResourceBundle var3 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      String[] var4 = new String[]{"decimalFormat", "currencyFormat", "percentFormat", "scientificFormat"};
      NumberingSystem var5 = NumberingSystem.getInstance(var0);
      String var6 = null;

      try {
         var6 = var3.getStringWithFallback("NumberElements/" + var5.getName() + "/patterns/" + var4[var2]);
      } catch (MissingResourceException var8) {
         var6 = var3.getStringWithFallback("NumberElements/latn/patterns/" + var4[var2]);
      }

      return var6;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.serialVersionOnStream < 1) {
         this.maximumIntegerDigits = this.maxIntegerDigits;
         this.minimumIntegerDigits = this.minIntegerDigits;
         this.maximumFractionDigits = this.maxFractionDigits;
         this.minimumFractionDigits = this.minFractionDigits;
      }

      if (this.minimumIntegerDigits <= this.maximumIntegerDigits && this.minimumFractionDigits <= this.maximumFractionDigits && this.minimumIntegerDigits >= 0 && this.minimumFractionDigits >= 0) {
         this.serialVersionOnStream = 1;
      } else {
         throw new InvalidObjectException("Digit count range invalid");
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      this.maxIntegerDigits = this.maximumIntegerDigits > 127 ? 127 : (byte)this.maximumIntegerDigits;
      this.minIntegerDigits = this.minimumIntegerDigits > 127 ? 127 : (byte)this.minimumIntegerDigits;
      this.maxFractionDigits = this.maximumFractionDigits > 127 ? 127 : (byte)this.maximumFractionDigits;
      this.minFractionDigits = this.minimumFractionDigits > 127 ? 127 : (byte)this.minimumFractionDigits;
      var1.defaultWriteObject();
   }

   static {
      doubleCurrencyStr = new String(doubleCurrencySign);
   }

   public static class Field extends java.text.Format.Field {
      static final long serialVersionUID = -4516273749929385842L;
      public static final NumberFormat.Field SIGN = new NumberFormat.Field("sign");
      public static final NumberFormat.Field INTEGER = new NumberFormat.Field("integer");
      public static final NumberFormat.Field FRACTION = new NumberFormat.Field("fraction");
      public static final NumberFormat.Field EXPONENT = new NumberFormat.Field("exponent");
      public static final NumberFormat.Field EXPONENT_SIGN = new NumberFormat.Field("exponent sign");
      public static final NumberFormat.Field EXPONENT_SYMBOL = new NumberFormat.Field("exponent symbol");
      public static final NumberFormat.Field DECIMAL_SEPARATOR = new NumberFormat.Field("decimal separator");
      public static final NumberFormat.Field GROUPING_SEPARATOR = new NumberFormat.Field("grouping separator");
      public static final NumberFormat.Field PERCENT = new NumberFormat.Field("percent");
      public static final NumberFormat.Field PERMILLE = new NumberFormat.Field("per mille");
      public static final NumberFormat.Field CURRENCY = new NumberFormat.Field("currency");

      protected Field(String var1) {
         super(var1);
      }

      protected Object readResolve() throws InvalidObjectException {
         if (this.getName().equals(INTEGER.getName())) {
            return INTEGER;
         } else if (this.getName().equals(FRACTION.getName())) {
            return FRACTION;
         } else if (this.getName().equals(EXPONENT.getName())) {
            return EXPONENT;
         } else if (this.getName().equals(EXPONENT_SIGN.getName())) {
            return EXPONENT_SIGN;
         } else if (this.getName().equals(EXPONENT_SYMBOL.getName())) {
            return EXPONENT_SYMBOL;
         } else if (this.getName().equals(CURRENCY.getName())) {
            return CURRENCY;
         } else if (this.getName().equals(DECIMAL_SEPARATOR.getName())) {
            return DECIMAL_SEPARATOR;
         } else if (this.getName().equals(GROUPING_SEPARATOR.getName())) {
            return GROUPING_SEPARATOR;
         } else if (this.getName().equals(PERCENT.getName())) {
            return PERCENT;
         } else if (this.getName().equals(PERMILLE.getName())) {
            return PERMILLE;
         } else if (this.getName().equals(SIGN.getName())) {
            return SIGN;
         } else {
            throw new InvalidObjectException("An invalid object.");
         }
      }
   }

   abstract static class NumberFormatShim {
      abstract Locale[] getAvailableLocales();

      abstract ULocale[] getAvailableULocales();

      abstract Object registerFactory(NumberFormat.NumberFormatFactory var1);

      abstract boolean unregister(Object var1);

      abstract NumberFormat createInstance(ULocale var1, int var2);
   }

   public abstract static class SimpleNumberFormatFactory extends NumberFormat.NumberFormatFactory {
      final Set localeNames;
      final boolean visible;

      public SimpleNumberFormatFactory(Locale var1) {
         this(var1, true);
      }

      public SimpleNumberFormatFactory(Locale var1, boolean var2) {
         this.localeNames = Collections.singleton(ULocale.forLocale(var1).getBaseName());
         this.visible = var2;
      }

      public SimpleNumberFormatFactory(ULocale var1) {
         this(var1, true);
      }

      public SimpleNumberFormatFactory(ULocale var1, boolean var2) {
         this.localeNames = Collections.singleton(var1.getBaseName());
         this.visible = var2;
      }

      public final boolean visible() {
         return this.visible;
      }

      public final Set getSupportedLocaleNames() {
         return this.localeNames;
      }
   }

   public abstract static class NumberFormatFactory {
      public static final int FORMAT_NUMBER = 0;
      public static final int FORMAT_CURRENCY = 1;
      public static final int FORMAT_PERCENT = 2;
      public static final int FORMAT_SCIENTIFIC = 3;
      public static final int FORMAT_INTEGER = 4;

      public boolean visible() {
         return true;
      }

      public abstract Set getSupportedLocaleNames();

      public NumberFormat createFormat(ULocale var1, int var2) {
         return this.createFormat(var1.toLocale(), var2);
      }

      public NumberFormat createFormat(Locale var1, int var2) {
         return this.createFormat(ULocale.forLocale(var1), var2);
      }

      protected NumberFormatFactory() {
      }
   }
}
