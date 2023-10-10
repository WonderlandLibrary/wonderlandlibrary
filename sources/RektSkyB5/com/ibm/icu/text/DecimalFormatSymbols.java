/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.text.NumberingSystem;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;

public class DecimalFormatSymbols
implements Cloneable,
Serializable {
    public static final int CURRENCY_SPC_CURRENCY_MATCH = 0;
    public static final int CURRENCY_SPC_SURROUNDING_MATCH = 1;
    public static final int CURRENCY_SPC_INSERT = 2;
    private String[] currencySpcBeforeSym;
    private String[] currencySpcAfterSym;
    private static final String[] SYMBOL_KEYS = new String[]{"decimal", "group", "percentSign", "minusSign", "plusSign", "exponential", "perMille", "infinity", "nan", "currencyDecimal", "currencyGroup", "superscriptingExponent"};
    private static final String[] DEF_DIGIT_STRINGS_ARRAY = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final char[] DEF_DIGIT_CHARS_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final char DEF_DECIMAL_SEPARATOR = '.';
    private static final char DEF_GROUPING_SEPARATOR = ',';
    private static final char DEF_PERCENT = '%';
    private static final char DEF_MINUS_SIGN = '-';
    private static final char DEF_PLUS_SIGN = '+';
    private static final char DEF_PERMILL = '\u2030';
    private static final String[] SYMBOL_DEFAULTS = new String[]{String.valueOf('.'), String.valueOf(','), String.valueOf('%'), String.valueOf('-'), String.valueOf('+'), "E", String.valueOf('\u2030'), "\u221e", "NaN", null, null, "\u00d7"};
    private static final String LATIN_NUMBERING_SYSTEM = "latn";
    private static final String NUMBER_ELEMENTS = "NumberElements";
    private static final String SYMBOLS = "symbols";
    private char zeroDigit;
    private char[] digits;
    private String[] digitStrings;
    private transient int codePointZero;
    private char groupingSeparator;
    private String groupingSeparatorString;
    private char decimalSeparator;
    private String decimalSeparatorString;
    private char perMill;
    private String perMillString;
    private char percent;
    private String percentString;
    private char digit;
    private char sigDigit;
    private char patternSeparator;
    private String infinity;
    private String NaN;
    private char minusSign;
    private String minusString;
    private char plusSign;
    private String plusString;
    private String currencySymbol;
    private String intlCurrencySymbol;
    private char monetarySeparator;
    private String monetarySeparatorString;
    private char monetaryGroupingSeparator;
    private String monetaryGroupingSeparatorString;
    private char exponential;
    private String exponentSeparator;
    private char padEscape;
    private Locale requestedLocale;
    private ULocale ulocale;
    private String exponentMultiplicationSign = null;
    private static final long serialVersionUID = 5772796243397350300L;
    private static final int currentSerialVersion = 8;
    private int serialVersionOnStream = 8;
    private static final CacheBase<ULocale, CacheData, Void> cachedLocaleData = new SoftCache<ULocale, CacheData, Void>(){

        @Override
        protected CacheData createInstance(ULocale locale, Void unused) {
            return DecimalFormatSymbols.loadData(locale);
        }
    };
    private String currencyPattern = null;
    private ULocale validLocale;
    private ULocale actualLocale;
    private transient Currency currency;

    public DecimalFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public DecimalFormatSymbols(Locale locale) {
        this(ULocale.forLocale(locale));
    }

    public DecimalFormatSymbols(ULocale locale) {
        this.initialize(locale, null);
    }

    private DecimalFormatSymbols(Locale locale, NumberingSystem ns) {
        this(ULocale.forLocale(locale), ns);
    }

    private DecimalFormatSymbols(ULocale locale, NumberingSystem ns) {
        this.initialize(locale, ns);
    }

    public static DecimalFormatSymbols getInstance() {
        return new DecimalFormatSymbols();
    }

    public static DecimalFormatSymbols getInstance(Locale locale) {
        return new DecimalFormatSymbols(locale);
    }

    public static DecimalFormatSymbols getInstance(ULocale locale) {
        return new DecimalFormatSymbols(locale);
    }

    public static DecimalFormatSymbols forNumberingSystem(Locale locale, NumberingSystem ns) {
        return new DecimalFormatSymbols(locale, ns);
    }

    public static DecimalFormatSymbols forNumberingSystem(ULocale locale, NumberingSystem ns) {
        return new DecimalFormatSymbols(locale, ns);
    }

    public static Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales();
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public char[] getDigits() {
        return (char[])this.digits.clone();
    }

    public void setZeroDigit(char zeroDigit) {
        this.zeroDigit = zeroDigit;
        this.digitStrings = (String[])this.digitStrings.clone();
        this.digits = (char[])this.digits.clone();
        this.digitStrings[0] = String.valueOf(zeroDigit);
        this.digits[0] = zeroDigit;
        for (int i2 = 1; i2 < 10; ++i2) {
            char d2 = (char)(zeroDigit + i2);
            this.digitStrings[i2] = String.valueOf(d2);
            this.digits[i2] = d2;
        }
        this.codePointZero = zeroDigit;
    }

    public String[] getDigitStrings() {
        return (String[])this.digitStrings.clone();
    }

    @Deprecated
    public String[] getDigitStringsLocal() {
        return this.digitStrings;
    }

    @Deprecated
    public int getCodePointZero() {
        return this.codePointZero;
    }

    public void setDigitStrings(String[] digitStrings) {
        if (digitStrings == null) {
            throw new NullPointerException("The input digit string array is null");
        }
        if (digitStrings.length != 10) {
            throw new IllegalArgumentException("Number of digit strings is not 10");
        }
        String[] tmpDigitStrings = new String[10];
        char[] tmpDigits = new char[10];
        int tmpCodePointZero = -1;
        for (int i2 = 0; i2 < 10; ++i2) {
            int cc;
            int cp;
            String digitStr = digitStrings[i2];
            if (digitStr == null) {
                throw new IllegalArgumentException("The input digit string array contains a null element");
            }
            tmpDigitStrings[i2] = digitStr;
            if (digitStr.length() == 0) {
                cp = -1;
                cc = 0;
            } else {
                cp = Character.codePointAt(digitStrings[i2], 0);
                cc = Character.charCount(cp);
            }
            if (cc == digitStr.length()) {
                if (cc == 1 && tmpDigits != null) {
                    tmpDigits[i2] = (char)cp;
                } else {
                    tmpDigits = null;
                }
                if (i2 == 0) {
                    tmpCodePointZero = cp;
                    continue;
                }
                if (cp == tmpCodePointZero + i2) continue;
                tmpCodePointZero = -1;
                continue;
            }
            tmpCodePointZero = -1;
            tmpDigits = null;
        }
        this.digitStrings = tmpDigitStrings;
        this.codePointZero = tmpCodePointZero;
        if (tmpDigits == null) {
            this.zeroDigit = DEF_DIGIT_CHARS_ARRAY[0];
            this.digits = DEF_DIGIT_CHARS_ARRAY;
        } else {
            this.zeroDigit = tmpDigits[0];
            this.digits = tmpDigits;
        }
    }

    public char getSignificantDigit() {
        return this.sigDigit;
    }

    public void setSignificantDigit(char sigDigit) {
        this.sigDigit = sigDigit;
    }

    public char getGroupingSeparator() {
        return this.groupingSeparator;
    }

    public void setGroupingSeparator(char groupingSeparator) {
        this.groupingSeparator = groupingSeparator;
        this.groupingSeparatorString = String.valueOf(groupingSeparator);
    }

    public String getGroupingSeparatorString() {
        return this.groupingSeparatorString;
    }

    public void setGroupingSeparatorString(String groupingSeparatorString) {
        if (groupingSeparatorString == null) {
            throw new NullPointerException("The input grouping separator is null");
        }
        this.groupingSeparatorString = groupingSeparatorString;
        this.groupingSeparator = groupingSeparatorString.length() == 1 ? groupingSeparatorString.charAt(0) : (char)44;
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public void setDecimalSeparator(char decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
        this.decimalSeparatorString = String.valueOf(decimalSeparator);
    }

    public String getDecimalSeparatorString() {
        return this.decimalSeparatorString;
    }

    public void setDecimalSeparatorString(String decimalSeparatorString) {
        if (decimalSeparatorString == null) {
            throw new NullPointerException("The input decimal separator is null");
        }
        this.decimalSeparatorString = decimalSeparatorString;
        this.decimalSeparator = decimalSeparatorString.length() == 1 ? decimalSeparatorString.charAt(0) : (char)46;
    }

    public char getPerMill() {
        return this.perMill;
    }

    public void setPerMill(char perMill) {
        this.perMill = perMill;
        this.perMillString = String.valueOf(perMill);
    }

    public String getPerMillString() {
        return this.perMillString;
    }

    public void setPerMillString(String perMillString) {
        if (perMillString == null) {
            throw new NullPointerException("The input permille string is null");
        }
        this.perMillString = perMillString;
        this.perMill = perMillString.length() == 1 ? perMillString.charAt(0) : (char)8240;
    }

    public char getPercent() {
        return this.percent;
    }

    public void setPercent(char percent) {
        this.percent = percent;
        this.percentString = String.valueOf(percent);
    }

    public String getPercentString() {
        return this.percentString;
    }

    public void setPercentString(String percentString) {
        if (percentString == null) {
            throw new NullPointerException("The input percent sign is null");
        }
        this.percentString = percentString;
        this.percent = percentString.length() == 1 ? percentString.charAt(0) : (char)37;
    }

    public char getDigit() {
        return this.digit;
    }

    public void setDigit(char digit) {
        this.digit = digit;
    }

    public char getPatternSeparator() {
        return this.patternSeparator;
    }

    public void setPatternSeparator(char patternSeparator) {
        this.patternSeparator = patternSeparator;
    }

    public String getInfinity() {
        return this.infinity;
    }

    public void setInfinity(String infinity) {
        this.infinity = infinity;
    }

    public String getNaN() {
        return this.NaN;
    }

    public void setNaN(String NaN) {
        this.NaN = NaN;
    }

    public char getMinusSign() {
        return this.minusSign;
    }

    public void setMinusSign(char minusSign) {
        this.minusSign = minusSign;
        this.minusString = String.valueOf(minusSign);
    }

    public String getMinusSignString() {
        return this.minusString;
    }

    public void setMinusSignString(String minusSignString) {
        if (minusSignString == null) {
            throw new NullPointerException("The input minus sign is null");
        }
        this.minusString = minusSignString;
        this.minusSign = minusSignString.length() == 1 ? minusSignString.charAt(0) : (char)45;
    }

    public char getPlusSign() {
        return this.plusSign;
    }

    public void setPlusSign(char plus) {
        this.plusSign = plus;
        this.plusString = String.valueOf(plus);
    }

    public String getPlusSignString() {
        return this.plusString;
    }

    public void setPlusSignString(String plusSignString) {
        if (plusSignString == null) {
            throw new NullPointerException("The input plus sign is null");
        }
        this.plusString = plusSignString;
        this.plusSign = plusSignString.length() == 1 ? plusSignString.charAt(0) : (char)43;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String currency) {
        this.currencySymbol = currency;
    }

    public String getInternationalCurrencySymbol() {
        return this.intlCurrencySymbol;
    }

    public void setInternationalCurrencySymbol(String currency) {
        this.intlCurrencySymbol = currency;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        if (currency == null) {
            throw new NullPointerException();
        }
        this.currency = currency;
        this.intlCurrencySymbol = currency.getCurrencyCode();
        this.currencySymbol = currency.getSymbol(this.requestedLocale);
    }

    public char getMonetaryDecimalSeparator() {
        return this.monetarySeparator;
    }

    public void setMonetaryDecimalSeparator(char sep) {
        this.monetarySeparator = sep;
        this.monetarySeparatorString = String.valueOf(sep);
    }

    public String getMonetaryDecimalSeparatorString() {
        return this.monetarySeparatorString;
    }

    public void setMonetaryDecimalSeparatorString(String sep) {
        if (sep == null) {
            throw new NullPointerException("The input monetary decimal separator is null");
        }
        this.monetarySeparatorString = sep;
        this.monetarySeparator = sep.length() == 1 ? sep.charAt(0) : (char)46;
    }

    public char getMonetaryGroupingSeparator() {
        return this.monetaryGroupingSeparator;
    }

    public void setMonetaryGroupingSeparator(char sep) {
        this.monetaryGroupingSeparator = sep;
        this.monetaryGroupingSeparatorString = String.valueOf(sep);
    }

    public String getMonetaryGroupingSeparatorString() {
        return this.monetaryGroupingSeparatorString;
    }

    public void setMonetaryGroupingSeparatorString(String sep) {
        if (sep == null) {
            throw new NullPointerException("The input monetary grouping separator is null");
        }
        this.monetaryGroupingSeparatorString = sep;
        this.monetaryGroupingSeparator = sep.length() == 1 ? sep.charAt(0) : (char)44;
    }

    String getCurrencyPattern() {
        return this.currencyPattern;
    }

    public String getExponentMultiplicationSign() {
        return this.exponentMultiplicationSign;
    }

    public void setExponentMultiplicationSign(String exponentMultiplicationSign) {
        this.exponentMultiplicationSign = exponentMultiplicationSign;
    }

    public String getExponentSeparator() {
        return this.exponentSeparator;
    }

    public void setExponentSeparator(String exp) {
        this.exponentSeparator = exp;
    }

    public char getPadEscape() {
        return this.padEscape;
    }

    public void setPadEscape(char c2) {
        this.padEscape = c2;
    }

    public String getPatternForCurrencySpacing(int itemType, boolean beforeCurrency) {
        if (itemType < 0 || itemType > 2) {
            throw new IllegalArgumentException("unknown currency spacing: " + itemType);
        }
        if (beforeCurrency) {
            return this.currencySpcBeforeSym[itemType];
        }
        return this.currencySpcAfterSym[itemType];
    }

    public void setPatternForCurrencySpacing(int itemType, boolean beforeCurrency, String pattern) {
        if (itemType < 0 || itemType > 2) {
            throw new IllegalArgumentException("unknown currency spacing: " + itemType);
        }
        if (beforeCurrency) {
            this.currencySpcBeforeSym[itemType] = pattern;
        } else {
            this.currencySpcAfterSym[itemType] = pattern;
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
            return super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    public boolean equals(Object obj) {
        int i2;
        if (!(obj instanceof DecimalFormatSymbols)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        DecimalFormatSymbols other = (DecimalFormatSymbols)obj;
        for (i2 = 0; i2 <= 2; ++i2) {
            if (!this.currencySpcBeforeSym[i2].equals(other.currencySpcBeforeSym[i2])) {
                return false;
            }
            if (this.currencySpcAfterSym[i2].equals(other.currencySpcAfterSym[i2])) continue;
            return false;
        }
        if (other.digits == null) {
            for (i2 = 0; i2 < 10; ++i2) {
                if (this.digits[i2] == other.zeroDigit + i2) continue;
                return false;
            }
        } else if (!Arrays.equals(this.digits, other.digits)) {
            return false;
        }
        return this.groupingSeparator == other.groupingSeparator && this.decimalSeparator == other.decimalSeparator && this.percent == other.percent && this.perMill == other.perMill && this.digit == other.digit && this.minusSign == other.minusSign && this.minusString.equals(other.minusString) && this.patternSeparator == other.patternSeparator && this.infinity.equals(other.infinity) && this.NaN.equals(other.NaN) && this.currencySymbol.equals(other.currencySymbol) && this.intlCurrencySymbol.equals(other.intlCurrencySymbol) && this.padEscape == other.padEscape && this.plusSign == other.plusSign && this.plusString.equals(other.plusString) && this.exponentSeparator.equals(other.exponentSeparator) && this.monetarySeparator == other.monetarySeparator && this.monetaryGroupingSeparator == other.monetaryGroupingSeparator && this.exponentMultiplicationSign.equals(other.exponentMultiplicationSign);
    }

    public int hashCode() {
        int result = this.digits[0];
        result = result * 37 + this.groupingSeparator;
        result = result * 37 + this.decimalSeparator;
        return result;
    }

    private void initialize(ULocale locale, NumberingSystem ns) {
        this.requestedLocale = locale.toLocale();
        this.ulocale = locale;
        ULocale keyLocale = ns == null ? locale : locale.setKeywordValue("numbers", ns.getName());
        CacheData data = cachedLocaleData.getInstance(keyLocale, null);
        this.setLocale(data.validLocale, data.validLocale);
        this.setDigitStrings(data.digits);
        String[] numberElements = data.numberElements;
        this.setDecimalSeparatorString(numberElements[0]);
        this.setGroupingSeparatorString(numberElements[1]);
        this.patternSeparator = (char)59;
        this.setPercentString(numberElements[2]);
        this.setMinusSignString(numberElements[3]);
        this.setPlusSignString(numberElements[4]);
        this.setExponentSeparator(numberElements[5]);
        this.setPerMillString(numberElements[6]);
        this.setInfinity(numberElements[7]);
        this.setNaN(numberElements[8]);
        this.setMonetaryDecimalSeparatorString(numberElements[9]);
        this.setMonetaryGroupingSeparatorString(numberElements[10]);
        this.setExponentMultiplicationSign(numberElements[11]);
        this.digit = (char)35;
        this.padEscape = (char)42;
        this.sigDigit = (char)64;
        CurrencyData.CurrencyDisplayInfo info = CurrencyData.provider.getInstance(locale, true);
        this.currency = Currency.getInstance(locale);
        if (this.currency != null) {
            this.intlCurrencySymbol = this.currency.getCurrencyCode();
            this.currencySymbol = this.currency.getName(locale, 0, null);
            CurrencyData.CurrencyFormatInfo fmtInfo = info.getFormatInfo(this.intlCurrencySymbol);
            if (fmtInfo != null) {
                this.currencyPattern = fmtInfo.currencyPattern;
                this.setMonetaryDecimalSeparatorString(fmtInfo.monetaryDecimalSeparator);
                this.setMonetaryGroupingSeparatorString(fmtInfo.monetaryGroupingSeparator);
            }
        } else {
            this.intlCurrencySymbol = "XXX";
            this.currencySymbol = "\u00a4";
        }
        this.initSpacingInfo(info.getSpacingInfo());
    }

    private static CacheData loadData(ULocale locale) {
        String nsName;
        NumberingSystem ns = NumberingSystem.getInstance(locale);
        String[] digits = new String[10];
        if (ns != null && ns.getRadix() == 10 && !ns.isAlgorithmic() && NumberingSystem.isValidDigitString(ns.getDescription())) {
            String digitString = ns.getDescription();
            int offset = 0;
            for (int i2 = 0; i2 < 10; ++i2) {
                int cp = digitString.codePointAt(offset);
                int nextOffset = offset + Character.charCount(cp);
                digits[i2] = digitString.substring(offset, nextOffset);
                offset = nextOffset;
            }
            nsName = ns.getName();
        } else {
            digits = DEF_DIGIT_STRINGS_ARRAY;
            nsName = LATIN_NUMBERING_SYSTEM;
        }
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
        ULocale validLocale = rb.getULocale();
        String[] numberElements = new String[SYMBOL_KEYS.length];
        DecFmtDataSink sink = new DecFmtDataSink(numberElements);
        try {
            rb.getAllItemsWithFallback("NumberElements/" + nsName + "/" + SYMBOLS, sink);
        }
        catch (MissingResourceException nextOffset) {
            // empty catch block
        }
        boolean hasNull = false;
        for (String entry : numberElements) {
            if (entry != null) continue;
            hasNull = true;
            break;
        }
        if (hasNull && !nsName.equals(LATIN_NUMBERING_SYSTEM)) {
            rb.getAllItemsWithFallback("NumberElements/latn/symbols", sink);
        }
        for (int i3 = 0; i3 < SYMBOL_KEYS.length; ++i3) {
            if (numberElements[i3] != null) continue;
            numberElements[i3] = SYMBOL_DEFAULTS[i3];
        }
        if (numberElements[9] == null) {
            numberElements[9] = numberElements[0];
        }
        if (numberElements[10] == null) {
            numberElements[10] = numberElements[1];
        }
        return new CacheData(validLocale, digits, numberElements);
    }

    private void initSpacingInfo(CurrencyData.CurrencySpacingInfo spcInfo) {
        this.currencySpcBeforeSym = spcInfo.getBeforeSymbols();
        this.currencySpcAfterSym = spcInfo.getAfterSymbols();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            this.monetarySeparator = this.decimalSeparator;
            this.exponential = (char)69;
        }
        if (this.serialVersionOnStream < 2) {
            this.padEscape = (char)42;
            this.plusSign = (char)43;
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
        if (this.serialVersionOnStream < 7) {
            if (this.minusString == null) {
                this.minusString = String.valueOf(this.minusSign);
            }
            if (this.plusString == null) {
                this.plusString = String.valueOf(this.plusSign);
            }
        }
        if (this.serialVersionOnStream < 8 && this.exponentMultiplicationSign == null) {
            this.exponentMultiplicationSign = "\u00d7";
        }
        if (this.serialVersionOnStream < 9) {
            if (this.digitStrings == null) {
                this.digitStrings = new String[10];
                if (this.digits != null && this.digits.length == 10) {
                    this.zeroDigit = this.digits[0];
                    for (int i2 = 0; i2 < 10; ++i2) {
                        this.digitStrings[i2] = String.valueOf(this.digits[i2]);
                    }
                } else {
                    char digit = this.zeroDigit;
                    if (this.digits == null) {
                        this.digits = new char[10];
                    }
                    for (int i3 = 0; i3 < 10; ++i3) {
                        this.digits[i3] = digit;
                        this.digitStrings[i3] = String.valueOf(digit);
                        digit = (char)(digit + '\u0001');
                    }
                }
            }
            if (this.decimalSeparatorString == null) {
                this.decimalSeparatorString = String.valueOf(this.decimalSeparator);
            }
            if (this.groupingSeparatorString == null) {
                this.groupingSeparatorString = String.valueOf(this.groupingSeparator);
            }
            if (this.percentString == null) {
                this.percentString = String.valueOf(this.percent);
            }
            if (this.perMillString == null) {
                this.perMillString = String.valueOf(this.perMill);
            }
            if (this.monetarySeparatorString == null) {
                this.monetarySeparatorString = String.valueOf(this.monetarySeparator);
            }
            if (this.monetaryGroupingSeparatorString == null) {
                this.monetaryGroupingSeparatorString = String.valueOf(this.monetaryGroupingSeparator);
            }
        }
        this.serialVersionOnStream = 8;
        this.currency = Currency.getInstance(this.intlCurrencySymbol);
        this.setDigitStrings(this.digitStrings);
    }

    public final ULocale getLocale(ULocale.Type type) {
        return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
    }

    final void setLocale(ULocale valid, ULocale actual) {
        if (valid == null != (actual == null)) {
            throw new IllegalArgumentException();
        }
        this.validLocale = valid;
        this.actualLocale = actual;
    }

    private static class CacheData {
        final ULocale validLocale;
        final String[] digits;
        final String[] numberElements;

        public CacheData(ULocale loc, String[] digits, String[] numberElements) {
            this.validLocale = loc;
            this.digits = digits;
            this.numberElements = numberElements;
        }
    }

    private static final class DecFmtDataSink
    extends UResource.Sink {
        private String[] numberElements;

        public DecFmtDataSink(String[] numberElements) {
            this.numberElements = numberElements;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table symbolsTable = value.getTable();
            int j2 = 0;
            while (symbolsTable.getKeyAndValue(j2, key, value)) {
                for (int i2 = 0; i2 < SYMBOL_KEYS.length; ++i2) {
                    if (!key.contentEquals(SYMBOL_KEYS[i2])) continue;
                    if (this.numberElements[i2] != null) break;
                    this.numberElements[i2] = value.toString();
                    break;
                }
                ++j2;
            }
        }
    }
}

