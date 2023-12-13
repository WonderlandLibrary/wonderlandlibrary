package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.ChoiceFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;

public class DecimalFormatSymbols implements Cloneable, Serializable {
   public static final int CURRENCY_SPC_CURRENCY_MATCH = 0;
   public static final int CURRENCY_SPC_SURROUNDING_MATCH = 1;
   public static final int CURRENCY_SPC_INSERT = 2;
   private String[] currencySpcBeforeSym;
   private String[] currencySpcAfterSym;
   private char zeroDigit;
   private char[] digits;
   private char groupingSeparator;
   private char decimalSeparator;
   private char perMill;
   private char percent;
   private char digit;
   private char sigDigit;
   private char patternSeparator;
   private String infinity;
   private String NaN;
   private char minusSign;
   private String currencySymbol;
   private String intlCurrencySymbol;
   private char monetarySeparator;
   private char monetaryGroupingSeparator;
   private char exponential;
   private String exponentSeparator;
   private char padEscape;
   private char plusSign;
   private Locale requestedLocale;
   private ULocale ulocale;
   private static final long serialVersionUID = 5772796243397350300L;
   private static final int currentSerialVersion = 6;
   private int serialVersionOnStream = 6;
   private static final ICUCache cachedLocaleData = new SimpleCache();
   private String currencyPattern = null;
   private ULocale validLocale;
   private ULocale actualLocale;
   private transient Currency currency;

   public DecimalFormatSymbols() {
      this.initialize(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public DecimalFormatSymbols(Locale var1) {
      this.initialize(ULocale.forLocale(var1));
   }

   public DecimalFormatSymbols(ULocale var1) {
      this.initialize(var1);
   }

   public static DecimalFormatSymbols getInstance() {
      return new DecimalFormatSymbols();
   }

   public static DecimalFormatSymbols getInstance(Locale var0) {
      return new DecimalFormatSymbols(var0);
   }

   public static DecimalFormatSymbols getInstance(ULocale var0) {
      return new DecimalFormatSymbols(var0);
   }

   public static Locale[] getAvailableLocales() {
      return ICUResourceBundle.getAvailableLocales();
   }

   public static ULocale[] getAvailableULocales() {
      return ICUResourceBundle.getAvailableULocales();
   }

   public char getZeroDigit() {
      return this.digits != null ? this.digits[0] : this.zeroDigit;
   }

   public char[] getDigits() {
      if (this.digits != null) {
         return (char[])this.digits.clone();
      } else {
         char[] var1 = new char[10];

         for(int var2 = 0; var2 < 10; ++var2) {
            var1[var2] = (char)(this.zeroDigit + var2);
         }

         return var1;
      }
   }

   char[] getDigitsLocal() {
      if (this.digits != null) {
         return this.digits;
      } else {
         char[] var1 = new char[10];

         for(int var2 = 0; var2 < 10; ++var2) {
            var1[var2] = (char)(this.zeroDigit + var2);
         }

         return var1;
      }
   }

   public void setZeroDigit(char var1) {
      if (this.digits != null) {
         this.digits[0] = var1;
         if (Character.digit(var1, 10) == 0) {
            for(int var2 = 1; var2 < 10; ++var2) {
               this.digits[var2] = (char)(var1 + var2);
            }
         }
      } else {
         this.zeroDigit = var1;
      }

   }

   public char getSignificantDigit() {
      return this.sigDigit;
   }

   public void setSignificantDigit(char var1) {
      this.sigDigit = var1;
   }

   public char getGroupingSeparator() {
      return this.groupingSeparator;
   }

   public void setGroupingSeparator(char var1) {
      this.groupingSeparator = var1;
   }

   public char getDecimalSeparator() {
      return this.decimalSeparator;
   }

   public void setDecimalSeparator(char var1) {
      this.decimalSeparator = var1;
   }

   public char getPerMill() {
      return this.perMill;
   }

   public void setPerMill(char var1) {
      this.perMill = var1;
   }

   public char getPercent() {
      return this.percent;
   }

   public void setPercent(char var1) {
      this.percent = var1;
   }

   public char getDigit() {
      return this.digit;
   }

   public void setDigit(char var1) {
      this.digit = var1;
   }

   public char getPatternSeparator() {
      return this.patternSeparator;
   }

   public void setPatternSeparator(char var1) {
      this.patternSeparator = var1;
   }

   public String getInfinity() {
      return this.infinity;
   }

   public void setInfinity(String var1) {
      this.infinity = var1;
   }

   public String getNaN() {
      return this.NaN;
   }

   public void setNaN(String var1) {
      this.NaN = var1;
   }

   public char getMinusSign() {
      return this.minusSign;
   }

   public void setMinusSign(char var1) {
      this.minusSign = var1;
   }

   public String getCurrencySymbol() {
      return this.currencySymbol;
   }

   public void setCurrencySymbol(String var1) {
      this.currencySymbol = var1;
   }

   public String getInternationalCurrencySymbol() {
      return this.intlCurrencySymbol;
   }

   public void setInternationalCurrencySymbol(String var1) {
      this.intlCurrencySymbol = var1;
   }

   public Currency getCurrency() {
      return this.currency;
   }

   public void setCurrency(Currency var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.currency = var1;
         this.intlCurrencySymbol = var1.getCurrencyCode();
         this.currencySymbol = var1.getSymbol(this.requestedLocale);
      }
   }

   public char getMonetaryDecimalSeparator() {
      return this.monetarySeparator;
   }

   public char getMonetaryGroupingSeparator() {
      return this.monetaryGroupingSeparator;
   }

   String getCurrencyPattern() {
      return this.currencyPattern;
   }

   public void setMonetaryDecimalSeparator(char var1) {
      this.monetarySeparator = var1;
   }

   public void setMonetaryGroupingSeparator(char var1) {
      this.monetaryGroupingSeparator = var1;
   }

   public String getExponentSeparator() {
      return this.exponentSeparator;
   }

   public void setExponentSeparator(String var1) {
      this.exponentSeparator = var1;
   }

   public char getPlusSign() {
      return this.plusSign;
   }

   public void setPlusSign(char var1) {
      this.plusSign = var1;
   }

   public char getPadEscape() {
      return this.padEscape;
   }

   public void setPadEscape(char var1) {
      this.padEscape = var1;
   }

   public String getPatternForCurrencySpacing(int var1, boolean var2) {
      if (var1 >= 0 && var1 <= 2) {
         return var2 ? this.currencySpcBeforeSym[var1] : this.currencySpcAfterSym[var1];
      } else {
         throw new IllegalArgumentException("unknown currency spacing: " + var1);
      }
   }

   public void setPatternForCurrencySpacing(int var1, boolean var2, String var3) {
      if (var1 >= 0 && var1 <= 2) {
         if (var2) {
            this.currencySpcBeforeSym[var1] = var3;
         } else {
            this.currencySpcAfterSym[var1] = var3;
         }

      } else {
         throw new IllegalArgumentException("unknown currency spacing: " + var1);
      }
   }

   public Locale getLocale() {
      return this.requestedLocale;
   }

   public ULocale getULocale() {
      return this.ulocale;
   }

   public Object clone() {
      try {
         return (DecimalFormatSymbols)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException();
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DecimalFormatSymbols)) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         DecimalFormatSymbols var2 = (DecimalFormatSymbols)var1;

         int var3;
         for(var3 = 0; var3 <= 2; ++var3) {
            if (!this.currencySpcBeforeSym[var3].equals(var2.currencySpcBeforeSym[var3])) {
               return false;
            }

            if (!this.currencySpcAfterSym[var3].equals(var2.currencySpcAfterSym[var3])) {
               return false;
            }
         }

         if (var2.digits == null) {
            for(var3 = 0; var3 < 10; ++var3) {
               if (this.digits[var3] != var2.zeroDigit + var3) {
                  return false;
               }
            }
         } else if (!Arrays.equals(this.digits, var2.digits)) {
            return false;
         }

         return this.groupingSeparator == var2.groupingSeparator && this.decimalSeparator == var2.decimalSeparator && this.percent == var2.percent && this.perMill == var2.perMill && this.digit == var2.digit && this.minusSign == var2.minusSign && this.patternSeparator == var2.patternSeparator && this.infinity.equals(var2.infinity) && this.NaN.equals(var2.NaN) && this.currencySymbol.equals(var2.currencySymbol) && this.intlCurrencySymbol.equals(var2.intlCurrencySymbol) && this.padEscape == var2.padEscape && this.plusSign == var2.plusSign && this.exponentSeparator.equals(var2.exponentSeparator) && this.monetarySeparator == var2.monetarySeparator && this.monetaryGroupingSeparator == var2.monetaryGroupingSeparator;
      }
   }

   public int hashCode() {
      char var1 = this.digits[0];
      int var2 = var1 * 37 + this.groupingSeparator;
      var2 = var2 * 37 + this.decimalSeparator;
      return var2;
   }

   private void initialize(ULocale var1) {
      this.requestedLocale = var1.toLocale();
      this.ulocale = var1;
      NumberingSystem var3 = NumberingSystem.getInstance(var1);
      this.digits = new char[10];
      String var2;
      if (var3 != null && var3.getRadix() == 10 && !var3.isAlgorithmic() && NumberingSystem.isValidDigitString(var3.getDescription())) {
         String var4 = var3.getDescription();
         this.digits[0] = var4.charAt(0);
         this.digits[1] = var4.charAt(1);
         this.digits[2] = var4.charAt(2);
         this.digits[3] = var4.charAt(3);
         this.digits[4] = var4.charAt(4);
         this.digits[5] = var4.charAt(5);
         this.digits[6] = var4.charAt(6);
         this.digits[7] = var4.charAt(7);
         this.digits[8] = var4.charAt(8);
         this.digits[9] = var4.charAt(9);
         var2 = var3.getName();
      } else {
         this.digits[0] = '0';
         this.digits[1] = '1';
         this.digits[2] = '2';
         this.digits[3] = '3';
         this.digits[4] = '4';
         this.digits[5] = '5';
         this.digits[6] = '6';
         this.digits[7] = '7';
         this.digits[8] = '8';
         this.digits[9] = '9';
         var2 = "latn";
      }

      String[][] var18 = (String[][])cachedLocaleData.get(var1);
      ICUResourceBundle var6;
      String var9;
      if (var18 == null) {
         var18 = new String[1][];
         var6 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var1);
         boolean var7 = var2.equals("latn");
         String var8 = "NumberElements/" + var2 + "/symbols/";
         var9 = "NumberElements/latn/symbols/";
         String[] var10 = new String[]{"decimal", "group", "list", "percentSign", "minusSign", "plusSign", "exponential", "perMille", "infinity", "nan", "currencyDecimal", "currencyGroup"};
         String[] var11 = new String[]{".", ",", ";", "%", "-", "+", "E", "‰", "∞", "NaN", null, null};
         String[] var12 = new String[var10.length];

         for(int var13 = 0; var13 < var10.length; ++var13) {
            try {
               var12[var13] = var6.getStringWithFallback(var8 + var10[var13]);
            } catch (MissingResourceException var17) {
               if (!var7) {
                  try {
                     var12[var13] = var6.getStringWithFallback(var9 + var10[var13]);
                  } catch (MissingResourceException var16) {
                     var12[var13] = var11[var13];
                  }
               } else {
                  var12[var13] = var11[var13];
               }
            }
         }

         var18[0] = var12;
         cachedLocaleData.put(var1, var18);
      }

      String[] var5 = var18[0];
      var6 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var1);
      ULocale var19 = var6.getULocale();
      this.setLocale(var19, var19);
      this.decimalSeparator = var5[0].charAt(0);
      this.groupingSeparator = var5[1].charAt(0);
      this.patternSeparator = var5[2].charAt(0);
      this.percent = var5[3].charAt(0);
      this.minusSign = var5[4].charAt(0);
      this.plusSign = var5[5].charAt(0);
      this.exponentSeparator = var5[6];
      this.perMill = var5[7].charAt(0);
      this.infinity = var5[8];
      this.NaN = var5[9];
      if (var5[10] != null) {
         this.monetarySeparator = var5[10].charAt(0);
      } else {
         this.monetarySeparator = this.decimalSeparator;
      }

      if (var5[11] != null) {
         this.monetaryGroupingSeparator = var5[11].charAt(0);
      } else {
         this.monetaryGroupingSeparator = this.groupingSeparator;
      }

      this.digit = '#';
      this.padEscape = '*';
      this.sigDigit = '@';
      CurrencyData.CurrencyDisplayInfo var20 = CurrencyData.provider.getInstance(var1, true);
      var9 = null;
      this.currency = Currency.getInstance(var1);
      if (this.currency != null) {
         this.intlCurrencySymbol = this.currency.getCurrencyCode();
         boolean[] var21 = new boolean[1];
         var9 = this.currency.getName((ULocale)var1, 0, var21);
         this.currencySymbol = var21[0] ? (new ChoiceFormat(var9)).format(2.0D) : var9;
         CurrencyData.CurrencyFormatInfo var22 = var20.getFormatInfo(this.intlCurrencySymbol);
         if (var22 != null) {
            this.currencyPattern = var22.currencyPattern;
            this.monetarySeparator = var22.monetarySeparator;
            this.monetaryGroupingSeparator = var22.monetaryGroupingSeparator;
         }
      } else {
         this.intlCurrencySymbol = "XXX";
         this.currencySymbol = "¤";
      }

      this.currencySpcBeforeSym = new String[3];
      this.currencySpcAfterSym = new String[3];
      this.initSpacingInfo(var20.getSpacingInfo());
   }

   private void initSpacingInfo(CurrencyData.CurrencySpacingInfo var1) {
      this.currencySpcBeforeSym[0] = var1.beforeCurrencyMatch;
      this.currencySpcBeforeSym[1] = var1.beforeContextMatch;
      this.currencySpcBeforeSym[2] = var1.beforeInsert;
      this.currencySpcAfterSym[0] = var1.afterCurrencyMatch;
      this.currencySpcAfterSym[1] = var1.afterContextMatch;
      this.currencySpcAfterSym[2] = var1.afterInsert;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.serialVersionOnStream < 1) {
         this.monetarySeparator = this.decimalSeparator;
         this.exponential = 'E';
      }

      if (this.serialVersionOnStream < 2) {
         this.padEscape = '*';
         this.plusSign = '+';
         this.exponentSeparator = String.valueOf(this.exponential);
      }

      if (this.serialVersionOnStream < 3) {
         this.requestedLocale = Locale.getDefault();
      }

      if (this.serialVersionOnStream < 4) {
         this.ulocale = ULocale.forLocale(this.requestedLocale);
      }

      if (this.serialVersionOnStream < 5) {
         this.monetaryGroupingSeparator = this.groupingSeparator;
      }

      if (this.serialVersionOnStream < 6) {
         if (this.currencySpcBeforeSym == null) {
            this.currencySpcBeforeSym = new String[3];
         }

         if (this.currencySpcAfterSym == null) {
            this.currencySpcAfterSym = new String[3];
         }

         this.initSpacingInfo(CurrencyData.CurrencySpacingInfo.DEFAULT);
      }

      this.serialVersionOnStream = 6;
      this.currency = Currency.getInstance(this.intlCurrencySymbol);
   }

   public final ULocale getLocale(ULocale.Type var1) {
      return var1 == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
   }

   final void setLocale(ULocale var1, ULocale var2) {
      if (var1 == null != (var2 == null)) {
         throw new IllegalArgumentException();
      } else {
         this.validLocale = var1;
         this.actualLocale = var2;
      }
   }
}
