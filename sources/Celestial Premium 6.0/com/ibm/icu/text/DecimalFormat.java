/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.CurrencyPluralInfo;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.DigitList;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.ChoiceFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DecimalFormat
extends NumberFormat {
    private static double epsilon = 1.0E-11;
    private static final int CURRENCY_SIGN_COUNT_IN_SYMBOL_FORMAT = 1;
    private static final int CURRENCY_SIGN_COUNT_IN_ISO_FORMAT = 2;
    private static final int CURRENCY_SIGN_COUNT_IN_PLURAL_FORMAT = 3;
    private static final int STATUS_INFINITE = 0;
    private static final int STATUS_POSITIVE = 1;
    private static final int STATUS_UNDERFLOW = 2;
    private static final int STATUS_LENGTH = 3;
    private static final UnicodeSet dotEquivalents = new UnicodeSet(46, 46, 8228, 8228, 12290, 12290, 65042, 65042, 65106, 65106, 65294, 65294, 65377, 65377).freeze();
    private static final UnicodeSet commaEquivalents = new UnicodeSet(44, 44, 1548, 1548, 1643, 1643, 12289, 12289, 65040, 65041, 65104, 65105, 65292, 65292, 65380, 65380).freeze();
    private static final UnicodeSet strictDotEquivalents = new UnicodeSet(46, 46, 8228, 8228, 65106, 65106, 65294, 65294, 65377, 65377).freeze();
    private static final UnicodeSet strictCommaEquivalents = new UnicodeSet(44, 44, 1643, 1643, 65040, 65040, 65104, 65104, 65292, 65292).freeze();
    private static final UnicodeSet defaultGroupingSeparators = new UnicodeSet(32, 32, 39, 39, 44, 44, 46, 46, 160, 160, 1548, 1548, 1643, 1644, 8192, 8202, 8216, 8217, 8228, 8228, 8239, 8239, 8287, 8287, 12288, 12290, 65040, 65042, 65104, 65106, 65287, 65287, 65292, 65292, 65294, 65294, 65377, 65377, 65380, 65380).freeze();
    private static final UnicodeSet strictDefaultGroupingSeparators = new UnicodeSet(32, 32, 39, 39, 44, 44, 46, 46, 160, 160, 1643, 1644, 8192, 8202, 8216, 8217, 8228, 8228, 8239, 8239, 8287, 8287, 12288, 12288, 65040, 65040, 65104, 65104, 65106, 65106, 65287, 65287, 65292, 65292, 65294, 65294, 65377, 65377).freeze();
    private int PARSE_MAX_EXPONENT = 1000;
    static final double roundingIncrementEpsilon = 1.0E-9;
    private transient DigitList digitList = new DigitList();
    private String positivePrefix = "";
    private String positiveSuffix = "";
    private String negativePrefix = "-";
    private String negativeSuffix = "";
    private String posPrefixPattern;
    private String posSuffixPattern;
    private String negPrefixPattern;
    private String negSuffixPattern;
    private ChoiceFormat currencyChoice;
    private int multiplier = 1;
    private byte groupingSize = (byte)3;
    private byte groupingSize2 = 0;
    private boolean decimalSeparatorAlwaysShown = false;
    private DecimalFormatSymbols symbols = null;
    private boolean useSignificantDigits = false;
    private int minSignificantDigits = 1;
    private int maxSignificantDigits = 6;
    private boolean useExponentialNotation;
    private byte minExponentDigits;
    private boolean exponentSignAlwaysShown = false;
    private BigDecimal roundingIncrement = null;
    private transient com.ibm.icu.math.BigDecimal roundingIncrementICU = null;
    private transient double roundingDouble = 0.0;
    private transient double roundingDoubleReciprocal = 0.0;
    private int roundingMode = 6;
    private com.ibm.icu.math.MathContext mathContext = new com.ibm.icu.math.MathContext(0, 0);
    private int formatWidth = 0;
    private char pad = (char)32;
    private int padPosition = 0;
    private boolean parseBigDecimal = false;
    static final int currentSerialVersion = 3;
    private int serialVersionOnStream = 3;
    public static final int PAD_BEFORE_PREFIX = 0;
    public static final int PAD_AFTER_PREFIX = 1;
    public static final int PAD_BEFORE_SUFFIX = 2;
    public static final int PAD_AFTER_SUFFIX = 3;
    static final char PATTERN_ZERO_DIGIT = '0';
    static final char PATTERN_ONE_DIGIT = '1';
    static final char PATTERN_TWO_DIGIT = '2';
    static final char PATTERN_THREE_DIGIT = '3';
    static final char PATTERN_FOUR_DIGIT = '4';
    static final char PATTERN_FIVE_DIGIT = '5';
    static final char PATTERN_SIX_DIGIT = '6';
    static final char PATTERN_SEVEN_DIGIT = '7';
    static final char PATTERN_EIGHT_DIGIT = '8';
    static final char PATTERN_NINE_DIGIT = '9';
    static final char PATTERN_GROUPING_SEPARATOR = ',';
    static final char PATTERN_DECIMAL_SEPARATOR = '.';
    static final char PATTERN_DIGIT = '#';
    static final char PATTERN_SIGNIFICANT_DIGIT = '@';
    static final char PATTERN_EXPONENT = 'E';
    static final char PATTERN_PLUS_SIGN = '+';
    private static final char PATTERN_PER_MILLE = '\u2030';
    private static final char PATTERN_PERCENT = '%';
    static final char PATTERN_PAD_ESCAPE = '*';
    private static final char PATTERN_MINUS = '-';
    private static final char PATTERN_SEPARATOR = ';';
    private static final char CURRENCY_SIGN = '\u00a4';
    private static final char QUOTE = '\'';
    static final int DOUBLE_INTEGER_DIGITS = 309;
    static final int DOUBLE_FRACTION_DIGITS = 340;
    static final int MAX_SCIENTIFIC_INTEGER_DIGITS = 8;
    private static final long serialVersionUID = 864413376551465018L;
    private ArrayList<FieldPosition> attributes = new ArrayList();
    private String formatPattern = "";
    private int style = 0;
    private int currencySignCount = 0;
    private transient Set<AffixForCurrency> affixPatternsForCurrency = null;
    private transient boolean isReadyForParsing = false;
    private CurrencyPluralInfo currencyPluralInfo = null;
    static final Unit NULL_UNIT = new Unit("", "");

    public DecimalFormat() {
        ULocale def = ULocale.getDefault(ULocale.Category.FORMAT);
        String pattern = DecimalFormat.getPattern(def, 0);
        this.symbols = new DecimalFormatSymbols(def);
        this.setCurrency(Currency.getInstance(def));
        this.applyPatternWithoutExpandAffix(pattern, false);
        if (this.currencySignCount == 3) {
            this.currencyPluralInfo = new CurrencyPluralInfo(def);
        } else {
            this.expandAffixAdjustWidth(null);
        }
    }

    public DecimalFormat(String pattern) {
        ULocale def = ULocale.getDefault(ULocale.Category.FORMAT);
        this.symbols = new DecimalFormatSymbols(def);
        this.setCurrency(Currency.getInstance(def));
        this.applyPatternWithoutExpandAffix(pattern, false);
        if (this.currencySignCount == 3) {
            this.currencyPluralInfo = new CurrencyPluralInfo(def);
        } else {
            this.expandAffixAdjustWidth(null);
        }
    }

    public DecimalFormat(String pattern, DecimalFormatSymbols symbols) {
        this.createFromPatternAndSymbols(pattern, symbols);
    }

    private void createFromPatternAndSymbols(String pattern, DecimalFormatSymbols inputSymbols) {
        this.symbols = (DecimalFormatSymbols)inputSymbols.clone();
        this.setCurrencyForSymbols();
        this.applyPatternWithoutExpandAffix(pattern, false);
        if (this.currencySignCount == 3) {
            this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
        } else {
            this.expandAffixAdjustWidth(null);
        }
    }

    public DecimalFormat(String pattern, DecimalFormatSymbols symbols, CurrencyPluralInfo infoInput, int style) {
        CurrencyPluralInfo info = infoInput;
        if (style == 6) {
            info = (CurrencyPluralInfo)infoInput.clone();
        }
        this.create(pattern, symbols, info, style);
    }

    private void create(String pattern, DecimalFormatSymbols inputSymbols, CurrencyPluralInfo info, int inputStyle) {
        if (inputStyle != 6) {
            this.createFromPatternAndSymbols(pattern, inputSymbols);
        } else {
            this.symbols = (DecimalFormatSymbols)inputSymbols.clone();
            this.currencyPluralInfo = info;
            String currencyPluralPatternForOther = this.currencyPluralInfo.getCurrencyPluralPattern("other");
            this.applyPatternWithoutExpandAffix(currencyPluralPatternForOther, false);
            this.setCurrencyForSymbols();
        }
        this.style = inputStyle;
    }

    DecimalFormat(String pattern, DecimalFormatSymbols inputSymbols, int style) {
        CurrencyPluralInfo info = null;
        if (style == 6) {
            info = new CurrencyPluralInfo(inputSymbols.getULocale());
        }
        this.create(pattern, inputSymbols, info, style);
    }

    public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }

    private boolean isNegative(double number) {
        return number < 0.0 || number == 0.0 && 1.0 / number < 0.0;
    }

    private double round(double number) {
        boolean isNegative = this.isNegative(number);
        if (isNegative) {
            number = -number;
        }
        if (this.roundingDouble > 0.0) {
            return DecimalFormat.round(number, this.roundingDouble, this.roundingDoubleReciprocal, this.roundingMode, isNegative);
        }
        return number;
    }

    private double multiply(double number) {
        if (this.multiplier != 1) {
            return number * (double)this.multiplier;
        }
        return number;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        if (Double.isNaN(number)) {
            if (fieldPosition.getField() == 0) {
                fieldPosition.setBeginIndex(result.length());
            } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
                fieldPosition.setBeginIndex(result.length());
            }
            result.append(this.symbols.getNaN());
            if (parseAttr) {
                this.addAttribute(NumberFormat.Field.INTEGER, result.length() - this.symbols.getNaN().length(), result.length());
            }
            if (fieldPosition.getField() == 0) {
                fieldPosition.setEndIndex(result.length());
            } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
                fieldPosition.setEndIndex(result.length());
            }
            this.addPadding(result, fieldPosition, 0, 0);
            return result;
        }
        number = this.multiply(number);
        boolean isNegative = this.isNegative(number);
        if (Double.isInfinite(number = this.round(number))) {
            int prefixLen = this.appendAffix(result, isNegative, true, parseAttr);
            if (fieldPosition.getField() == 0) {
                fieldPosition.setBeginIndex(result.length());
            } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
                fieldPosition.setBeginIndex(result.length());
            }
            result.append(this.symbols.getInfinity());
            if (parseAttr) {
                this.addAttribute(NumberFormat.Field.INTEGER, result.length() - this.symbols.getInfinity().length(), result.length());
            }
            if (fieldPosition.getField() == 0) {
                fieldPosition.setEndIndex(result.length());
            } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
                fieldPosition.setEndIndex(result.length());
            }
            int suffixLen = this.appendAffix(result, isNegative, false, parseAttr);
            this.addPadding(result, fieldPosition, prefixLen, suffixLen);
            return result;
        }
        DigitList digitList = this.digitList;
        synchronized (digitList) {
            this.digitList.set(number, this.precision(false), !this.useExponentialNotation && !this.areSignificantDigitsUsed());
            return this.subformat(number, result, fieldPosition, isNegative, false, parseAttr);
        }
    }

    @Deprecated
    double adjustNumberAsInFormatting(double number) {
        if (Double.isNaN(number)) {
            return number;
        }
        if (Double.isInfinite(number = this.round(this.multiply(number)))) {
            return number;
        }
        DigitList dl = new DigitList();
        dl.set(number, this.precision(false), false);
        return dl.getDouble();
    }

    @Deprecated
    boolean isNumberNegative(double number) {
        if (Double.isNaN(number)) {
            return false;
        }
        return this.isNegative(this.multiply(number));
    }

    private static double round(double number, double roundingInc, double roundingIncReciprocal, int mode, boolean isNegative) {
        double div = roundingIncReciprocal == 0.0 ? number / roundingInc : number * roundingIncReciprocal;
        block0 : switch (mode) {
            case 2: {
                div = isNegative ? Math.floor(div + epsilon) : Math.ceil(div - epsilon);
                break;
            }
            case 3: {
                div = isNegative ? Math.ceil(div - epsilon) : Math.floor(div + epsilon);
                break;
            }
            case 1: {
                div = Math.floor(div + epsilon);
                break;
            }
            case 0: {
                div = Math.ceil(div - epsilon);
                break;
            }
            case 7: {
                if (div != Math.floor(div)) {
                    throw new ArithmeticException("Rounding necessary");
                }
                return number;
            }
            default: {
                double ceil = Math.ceil(div);
                double ceildiff = ceil - div;
                double floor = Math.floor(div);
                double floordiff = div - floor;
                switch (mode) {
                    case 6: {
                        if (floordiff + epsilon < ceildiff) {
                            div = floor;
                            break block0;
                        }
                        if (ceildiff + epsilon < floordiff) {
                            div = ceil;
                            break block0;
                        }
                        double testFloor = floor / 2.0;
                        div = testFloor == Math.floor(testFloor) ? floor : ceil;
                        break block0;
                    }
                    case 5: {
                        div = floordiff <= ceildiff + epsilon ? floor : ceil;
                        break block0;
                    }
                    case 4: {
                        div = ceildiff <= floordiff + epsilon ? ceil : floor;
                        break block0;
                    }
                }
                throw new IllegalArgumentException("Invalid rounding mode: " + mode);
            }
        }
        number = roundingIncReciprocal == 0.0 ? div * roundingInc : div / roundingIncReciprocal;
        return number;
    }

    public StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr) {
        boolean isNegative;
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        if (this.roundingIncrementICU != null) {
            return this.format(com.ibm.icu.math.BigDecimal.valueOf(number), result, fieldPosition);
        }
        boolean bl = isNegative = number < 0L;
        if (isNegative) {
            number = -number;
        }
        if (this.multiplier != 1) {
            boolean tooBig = false;
            if (number < 0L) {
                long cutoff = Long.MIN_VALUE / (long)this.multiplier;
                tooBig = number <= cutoff;
            } else {
                long cutoff = Long.MAX_VALUE / (long)this.multiplier;
                boolean bl2 = tooBig = number > cutoff;
            }
            if (tooBig) {
                return this.format(BigInteger.valueOf(isNegative ? -number : number), result, fieldPosition, parseAttr);
            }
        }
        number *= (long)this.multiplier;
        DigitList digitList = this.digitList;
        synchronized (digitList) {
            this.digitList.set(number, this.precision(true));
            return this.subformat(number, result, fieldPosition, isNegative, true, parseAttr);
        }
    }

    public StringBuffer format(BigInteger number, StringBuffer result, FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private StringBuffer format(BigInteger number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr) {
        if (this.roundingIncrementICU != null) {
            return this.format(new com.ibm.icu.math.BigDecimal(number), result, fieldPosition);
        }
        if (this.multiplier != 1) {
            number = number.multiply(BigInteger.valueOf(this.multiplier));
        }
        DigitList digitList = this.digitList;
        synchronized (digitList) {
            this.digitList.set(number, this.precision(true));
            return this.subformat(number.intValue(), result, fieldPosition, number.signum() < 0, true, parseAttr);
        }
    }

    public StringBuffer format(BigDecimal number, StringBuffer result, FieldPosition fieldPosition) {
        return this.format(number, result, fieldPosition, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private StringBuffer format(BigDecimal number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr) {
        if (this.multiplier != 1) {
            number = number.multiply(BigDecimal.valueOf(this.multiplier));
        }
        if (this.roundingIncrement != null) {
            number = number.divide(this.roundingIncrement, 0, this.roundingMode).multiply(this.roundingIncrement);
        }
        DigitList digitList = this.digitList;
        synchronized (digitList) {
            this.digitList.set(number, this.precision(false), !this.useExponentialNotation && !this.areSignificantDigitsUsed());
            return this.subformat(number.doubleValue(), result, fieldPosition, number.signum() < 0, false, parseAttr);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer result, FieldPosition fieldPosition) {
        if (this.multiplier != 1) {
            number = number.multiply(com.ibm.icu.math.BigDecimal.valueOf(this.multiplier), this.mathContext);
        }
        if (this.roundingIncrementICU != null) {
            number = number.divide(this.roundingIncrementICU, 0, this.roundingMode).multiply(this.roundingIncrementICU, this.mathContext);
        }
        DigitList digitList = this.digitList;
        synchronized (digitList) {
            this.digitList.set(number, this.precision(false), !this.useExponentialNotation && !this.areSignificantDigitsUsed());
            return this.subformat(number.doubleValue(), result, fieldPosition, number.signum() < 0, false, false);
        }
    }

    private boolean isGroupingPosition(int pos) {
        boolean result = false;
        if (this.isGroupingUsed() && pos > 0 && this.groupingSize > 0) {
            result = this.groupingSize2 > 0 && pos > this.groupingSize ? (pos - this.groupingSize) % this.groupingSize2 == 0 : pos % this.groupingSize == 0;
        }
        return result;
    }

    private int precision(boolean isIntegral) {
        if (this.areSignificantDigitsUsed()) {
            return this.getMaximumSignificantDigits();
        }
        if (this.useExponentialNotation) {
            return this.getMinimumIntegerDigits() + this.getMaximumFractionDigits();
        }
        return isIntegral ? 0 : this.getMaximumFractionDigits();
    }

    private StringBuffer subformat(int number, StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr) {
        if (this.currencySignCount == 3) {
            return this.subformat(this.currencyPluralInfo.select(number), result, fieldPosition, isNegative, isInteger, parseAttr);
        }
        return this.subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
    }

    private StringBuffer subformat(double number, StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr) {
        if (this.currencySignCount == 3) {
            return this.subformat(this.currencyPluralInfo.select(number), result, fieldPosition, isNegative, isInteger, parseAttr);
        }
        return this.subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
    }

    private StringBuffer subformat(String pluralCount, StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr) {
        String currencyPluralPattern;
        if (this.style == 6 && !this.formatPattern.equals(currencyPluralPattern = this.currencyPluralInfo.getCurrencyPluralPattern(pluralCount))) {
            this.applyPatternWithoutExpandAffix(currencyPluralPattern, false);
        }
        this.expandAffixAdjustWidth(pluralCount);
        return this.subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
    }

    private StringBuffer subformat(StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr) {
        if (this.digitList.isZero()) {
            this.digitList.decimalAt = 0;
        }
        int prefixLen = this.appendAffix(result, isNegative, true, parseAttr);
        if (this.useExponentialNotation) {
            this.subformatExponential(result, fieldPosition, parseAttr);
        } else {
            this.subformatFixed(result, fieldPosition, isInteger, parseAttr);
        }
        int suffixLen = this.appendAffix(result, isNegative, false, parseAttr);
        this.addPadding(result, fieldPosition, prefixLen, suffixLen);
        return result;
    }

    private void subformatFixed(StringBuffer result, FieldPosition fieldPosition, boolean isInteger, boolean parseAttr) {
        boolean fractionPresent;
        int i;
        int count;
        char[] digits = this.symbols.getDigitsLocal();
        char grouping = this.currencySignCount > 0 ? this.symbols.getMonetaryGroupingSeparator() : this.symbols.getGroupingSeparator();
        char decimal = this.currencySignCount > 0 ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
        boolean useSigDig = this.areSignificantDigitsUsed();
        int maxIntDig = this.getMaximumIntegerDigits();
        int minIntDig = this.getMinimumIntegerDigits();
        int intBegin = result.length();
        if (fieldPosition.getField() == 0) {
            fieldPosition.setBeginIndex(result.length());
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
            fieldPosition.setBeginIndex(result.length());
        }
        int sigCount = 0;
        int minSigDig = this.getMinimumSignificantDigits();
        int maxSigDig = this.getMaximumSignificantDigits();
        if (!useSigDig) {
            minSigDig = 0;
            maxSigDig = Integer.MAX_VALUE;
        }
        int n = count = useSigDig ? Math.max(1, this.digitList.decimalAt) : minIntDig;
        if (this.digitList.decimalAt > 0 && count < this.digitList.decimalAt) {
            count = this.digitList.decimalAt;
        }
        int digitIndex = 0;
        if (count > maxIntDig && maxIntDig >= 0) {
            count = maxIntDig;
            digitIndex = this.digitList.decimalAt - count;
        }
        int sizeBeforeIntegerPart = result.length();
        for (i = count - 1; i >= 0; --i) {
            if (i < this.digitList.decimalAt && digitIndex < this.digitList.count && sigCount < maxSigDig) {
                result.append(digits[this.digitList.getDigitValue(digitIndex++)]);
                ++sigCount;
            } else {
                result.append(digits[0]);
                if (sigCount > 0) {
                    ++sigCount;
                }
            }
            if (!this.isGroupingPosition(i)) continue;
            result.append(grouping);
            if (!parseAttr) continue;
            this.addAttribute(NumberFormat.Field.GROUPING_SEPARATOR, result.length() - 1, result.length());
        }
        if (fieldPosition.getField() == 0) {
            fieldPosition.setEndIndex(result.length());
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
            fieldPosition.setEndIndex(result.length());
        }
        boolean bl = !isInteger && digitIndex < this.digitList.count || (useSigDig ? sigCount < minSigDig : this.getMinimumFractionDigits() > 0) ? true : (fractionPresent = false);
        if (!fractionPresent && result.length() == sizeBeforeIntegerPart) {
            result.append(digits[0]);
        }
        if (parseAttr) {
            this.addAttribute(NumberFormat.Field.INTEGER, intBegin, result.length());
        }
        if (this.decimalSeparatorAlwaysShown || fractionPresent) {
            if (fieldPosition.getFieldAttribute() == NumberFormat.Field.DECIMAL_SEPARATOR) {
                fieldPosition.setBeginIndex(result.length());
            }
            result.append(decimal);
            if (fieldPosition.getFieldAttribute() == NumberFormat.Field.DECIMAL_SEPARATOR) {
                fieldPosition.setEndIndex(result.length());
            }
            if (parseAttr) {
                this.addAttribute(NumberFormat.Field.DECIMAL_SEPARATOR, result.length() - 1, result.length());
            }
        }
        if (fieldPosition.getField() == 1) {
            fieldPosition.setBeginIndex(result.length());
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
            fieldPosition.setBeginIndex(result.length());
        }
        int fracBegin = result.length();
        int n2 = count = useSigDig ? Integer.MAX_VALUE : this.getMaximumFractionDigits();
        if (useSigDig && (sigCount == maxSigDig || sigCount >= minSigDig && digitIndex == this.digitList.count)) {
            count = 0;
        }
        for (i = 0; i < count && (useSigDig || i < this.getMinimumFractionDigits() || !isInteger && digitIndex < this.digitList.count); ++i) {
            if (-1 - i > this.digitList.decimalAt - 1) {
                result.append(digits[0]);
                continue;
            }
            if (!isInteger && digitIndex < this.digitList.count) {
                result.append(digits[this.digitList.getDigitValue(digitIndex++)]);
            } else {
                result.append(digits[0]);
            }
            if (useSigDig && (++sigCount == maxSigDig || digitIndex == this.digitList.count && sigCount >= minSigDig)) break;
        }
        if (fieldPosition.getField() == 1) {
            fieldPosition.setEndIndex(result.length());
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
            fieldPosition.setEndIndex(result.length());
        }
        if (parseAttr && (this.decimalSeparatorAlwaysShown || fractionPresent)) {
            this.addAttribute(NumberFormat.Field.FRACTION, fracBegin, result.length());
        }
    }

    private void subformatExponential(StringBuffer result, FieldPosition fieldPosition, boolean parseAttr) {
        boolean negativeExponent;
        int i;
        char[] digits = this.symbols.getDigitsLocal();
        char decimal = this.currencySignCount > 0 ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
        boolean useSigDig = this.areSignificantDigitsUsed();
        int maxIntDig = this.getMaximumIntegerDigits();
        int minIntDig = this.getMinimumIntegerDigits();
        if (fieldPosition.getField() == 0) {
            fieldPosition.setBeginIndex(result.length());
            fieldPosition.setEndIndex(-1);
        } else if (fieldPosition.getField() == 1) {
            fieldPosition.setBeginIndex(-1);
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
            fieldPosition.setBeginIndex(result.length());
            fieldPosition.setEndIndex(-1);
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
            fieldPosition.setBeginIndex(-1);
        }
        int intBegin = result.length();
        int intEnd = -1;
        int fracBegin = -1;
        int minFracDig = 0;
        if (useSigDig) {
            minIntDig = 1;
            maxIntDig = 1;
            minFracDig = this.getMinimumSignificantDigits() - 1;
        } else {
            minFracDig = this.getMinimumFractionDigits();
            if (maxIntDig > 8 && (maxIntDig = 1) < minIntDig) {
                maxIntDig = minIntDig;
            }
            if (maxIntDig > minIntDig) {
                minIntDig = 1;
            }
        }
        int exponent = this.digitList.decimalAt;
        if (maxIntDig > 1 && maxIntDig != minIntDig) {
            exponent = exponent > 0 ? (exponent - 1) / maxIntDig : exponent / maxIntDig - 1;
            exponent *= maxIntDig;
        } else {
            exponent -= minIntDig > 0 || minFracDig > 0 ? minIntDig : 1;
        }
        int minimumDigits = minIntDig + minFracDig;
        int integerDigits = this.digitList.isZero() ? minIntDig : this.digitList.decimalAt - exponent;
        int totalDigits = this.digitList.count;
        if (minimumDigits > totalDigits) {
            totalDigits = minimumDigits;
        }
        if (integerDigits > totalDigits) {
            totalDigits = integerDigits;
        }
        for (i = 0; i < totalDigits; ++i) {
            if (i == integerDigits) {
                if (fieldPosition.getField() == 0) {
                    fieldPosition.setEndIndex(result.length());
                } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
                    fieldPosition.setEndIndex(result.length());
                }
                if (parseAttr) {
                    intEnd = result.length();
                    this.addAttribute(NumberFormat.Field.INTEGER, intBegin, result.length());
                }
                result.append(decimal);
                if (parseAttr) {
                    int decimalSeparatorBegin = result.length() - 1;
                    this.addAttribute(NumberFormat.Field.DECIMAL_SEPARATOR, decimalSeparatorBegin, result.length());
                    fracBegin = result.length();
                }
                if (fieldPosition.getField() == 1) {
                    fieldPosition.setBeginIndex(result.length());
                } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
                    fieldPosition.setBeginIndex(result.length());
                }
            }
            result.append(i < this.digitList.count ? digits[this.digitList.getDigitValue(i)] : digits[0]);
        }
        if (this.digitList.isZero() && totalDigits == 0) {
            result.append(digits[0]);
        }
        if (fieldPosition.getField() == 0) {
            if (fieldPosition.getEndIndex() < 0) {
                fieldPosition.setEndIndex(result.length());
            }
        } else if (fieldPosition.getField() == 1) {
            if (fieldPosition.getBeginIndex() < 0) {
                fieldPosition.setBeginIndex(result.length());
            }
            fieldPosition.setEndIndex(result.length());
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
            if (fieldPosition.getEndIndex() < 0) {
                fieldPosition.setEndIndex(result.length());
            }
        } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
            if (fieldPosition.getBeginIndex() < 0) {
                fieldPosition.setBeginIndex(result.length());
            }
            fieldPosition.setEndIndex(result.length());
        }
        if (parseAttr) {
            if (intEnd < 0) {
                this.addAttribute(NumberFormat.Field.INTEGER, intBegin, result.length());
            }
            if (fracBegin > 0) {
                this.addAttribute(NumberFormat.Field.FRACTION, fracBegin, result.length());
            }
        }
        result.append(this.symbols.getExponentSeparator());
        if (parseAttr) {
            this.addAttribute(NumberFormat.Field.EXPONENT_SYMBOL, result.length() - this.symbols.getExponentSeparator().length(), result.length());
        }
        if (this.digitList.isZero()) {
            exponent = 0;
        }
        boolean bl = negativeExponent = exponent < 0;
        if (negativeExponent) {
            exponent = -exponent;
            result.append(this.symbols.getMinusSign());
            if (parseAttr) {
                this.addAttribute(NumberFormat.Field.EXPONENT_SIGN, result.length() - 1, result.length());
            }
        } else if (this.exponentSignAlwaysShown) {
            result.append(this.symbols.getPlusSign());
            if (parseAttr) {
                int expSignBegin = result.length() - 1;
                this.addAttribute(NumberFormat.Field.EXPONENT_SIGN, expSignBegin, result.length());
            }
        }
        int expBegin = result.length();
        this.digitList.set(exponent);
        byte expDig = this.minExponentDigits;
        if (this.useExponentialNotation && expDig < 1) {
            expDig = 1;
        }
        for (i = this.digitList.decimalAt; i < expDig; ++i) {
            result.append(digits[0]);
        }
        for (i = 0; i < this.digitList.decimalAt; ++i) {
            result.append(i < this.digitList.count ? digits[this.digitList.getDigitValue(i)] : digits[0]);
        }
        if (parseAttr) {
            this.addAttribute(NumberFormat.Field.EXPONENT, expBegin, result.length());
        }
    }

    private final void addPadding(StringBuffer result, FieldPosition fieldPosition, int prefixLen, int suffixLen) {
        int len;
        if (this.formatWidth > 0 && (len = this.formatWidth - result.length()) > 0) {
            char[] padding = new char[len];
            for (int i = 0; i < len; ++i) {
                padding[i] = this.pad;
            }
            switch (this.padPosition) {
                case 1: {
                    result.insert(prefixLen, padding);
                    break;
                }
                case 0: {
                    result.insert(0, padding);
                    break;
                }
                case 2: {
                    result.insert(result.length() - suffixLen, padding);
                    break;
                }
                case 3: {
                    result.append(padding);
                }
            }
            if (this.padPosition == 0 || this.padPosition == 1) {
                fieldPosition.setBeginIndex(fieldPosition.getBeginIndex() + len);
                fieldPosition.setEndIndex(fieldPosition.getEndIndex() + len);
            }
        }
    }

    public Number parse(String text, ParsePosition parsePosition) {
        return (Number)this.parse(text, parsePosition, null);
    }

    public CurrencyAmount parseCurrency(CharSequence text, ParsePosition pos) {
        Currency[] currency = new Currency[1];
        return (CurrencyAmount)this.parse(((Object)text).toString(), pos, currency);
    }

    private Object parse(String text, ParsePosition parsePosition, Currency[] currency) {
        int backup;
        int i = backup = parsePosition.getIndex();
        if (this.formatWidth > 0 && (this.padPosition == 0 || this.padPosition == 1)) {
            i = this.skipPadding(text, i);
        }
        if (text.regionMatches(i, this.symbols.getNaN(), 0, this.symbols.getNaN().length())) {
            i += this.symbols.getNaN().length();
            if (this.formatWidth > 0 && (this.padPosition == 2 || this.padPosition == 3)) {
                i = this.skipPadding(text, i);
            }
            parsePosition.setIndex(i);
            return new Double(Double.NaN);
        }
        i = backup;
        boolean[] status = new boolean[3];
        if (this.currencySignCount > 0) {
            if (!this.parseForCurrency(text, parsePosition, currency, status)) {
                return null;
            }
        } else if (!this.subparse(text, parsePosition, this.digitList, status, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0)) {
            parsePosition.setIndex(backup);
            return null;
        }
        Number n = null;
        if (status[0]) {
            n = new Double(status[1] ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
        } else if (status[2]) {
            n = status[1] ? new Double("0.0") : new Double("-0.0");
        } else if (!status[1] && this.digitList.isZero()) {
            n = new Double("-0.0");
        } else {
            int mult = this.multiplier;
            while (mult % 10 == 0) {
                --this.digitList.decimalAt;
                mult /= 10;
            }
            if (!this.parseBigDecimal && mult == 1 && this.digitList.isIntegral()) {
                if (this.digitList.decimalAt < 12) {
                    long l = 0L;
                    if (this.digitList.count > 0) {
                        int nx = 0;
                        while (nx < this.digitList.count) {
                            l = l * 10L + (long)((char)this.digitList.digits[nx++]) - 48L;
                        }
                        while (nx++ < this.digitList.decimalAt) {
                            l *= 10L;
                        }
                        if (!status[1]) {
                            l = -l;
                        }
                    }
                    n = l;
                } else {
                    BigInteger big = this.digitList.getBigInteger(status[1]);
                    n = big.bitLength() < 64 ? Long.valueOf(big.longValue()) : big;
                }
            } else {
                com.ibm.icu.math.BigDecimal big = this.digitList.getBigDecimalICU(status[1]);
                n = big;
                if (mult != 1) {
                    n = big.divide(com.ibm.icu.math.BigDecimal.valueOf(mult), this.mathContext);
                }
            }
        }
        return currency != null ? new CurrencyAmount(n, currency[0]) : n;
    }

    private boolean parseForCurrency(String text, ParsePosition parsePosition, Currency[] currency, boolean[] status) {
        int origPos = parsePosition.getIndex();
        if (!this.isReadyForParsing) {
            int savedCurrencySignCount = this.currencySignCount;
            this.setupCurrencyAffixForAllPatterns();
            if (savedCurrencySignCount == 3) {
                this.applyPatternWithoutExpandAffix(this.formatPattern, false);
            } else {
                this.applyPattern(this.formatPattern, false);
            }
            this.isReadyForParsing = true;
        }
        int maxPosIndex = origPos;
        int maxErrorPos = -1;
        boolean[] savedStatus = null;
        boolean[] tmpStatus = new boolean[3];
        ParsePosition tmpPos = new ParsePosition(origPos);
        DigitList tmpDigitList = new DigitList();
        boolean found = this.style == 6 ? this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 1) : this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0);
        if (found) {
            if (tmpPos.getIndex() > maxPosIndex) {
                maxPosIndex = tmpPos.getIndex();
                savedStatus = tmpStatus;
                this.digitList = tmpDigitList;
            }
        } else {
            maxErrorPos = tmpPos.getErrorIndex();
        }
        Iterator<AffixForCurrency> i$ = this.affixPatternsForCurrency.iterator();
        while (i$.hasNext()) {
            tmpPos = new ParsePosition(origPos);
            tmpDigitList = new DigitList();
            tmpStatus = new boolean[3];
            AffixForCurrency affix = i$.next();
            boolean result = this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, affix.getNegPrefix(), affix.getNegSuffix(), affix.getPosPrefix(), affix.getPosSuffix(), affix.getPatternType());
            if (result) {
                found = true;
                if (tmpPos.getIndex() <= maxPosIndex) continue;
                maxPosIndex = tmpPos.getIndex();
                savedStatus = tmpStatus;
                this.digitList = tmpDigitList;
                continue;
            }
            maxErrorPos = tmpPos.getErrorIndex() > maxErrorPos ? tmpPos.getErrorIndex() : maxErrorPos;
        }
        tmpStatus = new boolean[3];
        tmpPos = new ParsePosition(origPos);
        tmpDigitList = new DigitList();
        int savedCurrencySignCount = this.currencySignCount;
        this.currencySignCount = -1;
        boolean result = this.subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negativePrefix, this.negativeSuffix, this.positivePrefix, this.positiveSuffix, 0);
        this.currencySignCount = savedCurrencySignCount;
        if (result) {
            if (tmpPos.getIndex() > maxPosIndex) {
                maxPosIndex = tmpPos.getIndex();
                savedStatus = tmpStatus;
                this.digitList = tmpDigitList;
            }
            found = true;
        } else {
            int n = maxErrorPos = tmpPos.getErrorIndex() > maxErrorPos ? tmpPos.getErrorIndex() : maxErrorPos;
        }
        if (!found) {
            parsePosition.setErrorIndex(maxErrorPos);
        } else {
            parsePosition.setIndex(maxPosIndex);
            parsePosition.setErrorIndex(-1);
            for (int index = 0; index < 3; ++index) {
                status[index] = savedStatus[index];
            }
        }
        return found;
    }

    private void setupCurrencyAffixForAllPatterns() {
        if (this.currencyPluralInfo == null) {
            this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
        }
        this.affixPatternsForCurrency = new HashSet<AffixForCurrency>();
        String savedFormatPattern = this.formatPattern;
        this.applyPatternWithoutExpandAffix(DecimalFormat.getPattern(this.symbols.getULocale(), 1), false);
        AffixForCurrency affixes = new AffixForCurrency(this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0);
        this.affixPatternsForCurrency.add(affixes);
        Iterator<String> iter = this.currencyPluralInfo.pluralPatternIterator();
        HashSet<String> currencyUnitPatternSet = new HashSet<String>();
        while (iter.hasNext()) {
            String pluralCount = iter.next();
            String currencyPattern = this.currencyPluralInfo.getCurrencyPluralPattern(pluralCount);
            if (currencyPattern == null || currencyUnitPatternSet.contains(currencyPattern)) continue;
            currencyUnitPatternSet.add(currencyPattern);
            this.applyPatternWithoutExpandAffix(currencyPattern, false);
            affixes = new AffixForCurrency(this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 1);
            this.affixPatternsForCurrency.add(affixes);
        }
        this.formatPattern = savedFormatPattern;
    }

    private final boolean subparse(String text, ParsePosition parsePosition, DigitList digits, boolean[] status, Currency[] currency, String negPrefix, String negSuffix, String posPrefix, String posSuffix, int type) {
        int position = parsePosition.getIndex();
        int oldStart = parsePosition.getIndex();
        if (this.formatWidth > 0 && this.padPosition == 0) {
            position = this.skipPadding(text, position);
        }
        int posMatch = this.compareAffix(text, position, false, true, posPrefix, type, currency);
        int negMatch = this.compareAffix(text, position, true, true, negPrefix, type, currency);
        if (posMatch >= 0 && negMatch >= 0) {
            if (posMatch > negMatch) {
                negMatch = -1;
            } else if (negMatch > posMatch) {
                posMatch = -1;
            }
        }
        if (posMatch >= 0) {
            position += posMatch;
        } else if (negMatch >= 0) {
            position += negMatch;
        } else {
            parsePosition.setErrorIndex(position);
            return false;
        }
        if (this.formatWidth > 0 && this.padPosition == 1) {
            position = this.skipPadding(text, position);
        }
        status[0] = false;
        if (text.regionMatches(position, this.symbols.getInfinity(), 0, this.symbols.getInfinity().length())) {
            position += this.symbols.getInfinity().length();
            status[0] = true;
        } else {
            UnicodeSet decimalEquiv;
            digits.count = 0;
            digits.decimalAt = 0;
            char[] digitSymbols = this.symbols.getDigitsLocal();
            char decimal = this.currencySignCount == 0 ? this.symbols.getDecimalSeparator() : this.symbols.getMonetaryDecimalSeparator();
            char grouping = this.symbols.getGroupingSeparator();
            String exponentSep = this.symbols.getExponentSeparator();
            boolean sawDecimal = false;
            boolean sawGrouping = false;
            boolean sawExponent = false;
            boolean sawDigit = false;
            long exponent = 0L;
            int digit = 0;
            boolean strictParse = this.isParseStrict();
            boolean strictFail = false;
            int lastGroup = -1;
            int digitStart = position;
            byte gs2 = this.groupingSize2 == 0 ? this.groupingSize : this.groupingSize2;
            boolean skipExtendedSeparatorParsing = ICUConfig.get("com.ibm.icu.text.DecimalFormat.SkipExtendedSeparatorParsing", "false").equals("true");
            UnicodeSet unicodeSet = decimalEquiv = skipExtendedSeparatorParsing ? UnicodeSet.EMPTY : this.getEquivalentDecimals(decimal, strictParse);
            UnicodeSet groupEquiv = skipExtendedSeparatorParsing ? UnicodeSet.EMPTY : (strictParse ? strictDefaultGroupingSeparators : defaultGroupingSeparators);
            int digitCount = 0;
            int backup = -1;
            while (position < text.length()) {
                int ch = UTF16.charAt(text, position);
                digit = ch - digitSymbols[0];
                if (digit < 0 || digit > 9) {
                    digit = UCharacter.digit(ch, 10);
                }
                if (digit < 0 || digit > 9) {
                    for (digit = 0; digit < 10 && ch != digitSymbols[digit]; ++digit) {
                    }
                }
                if (digit == 0) {
                    if (strictParse && backup != -1) {
                        if (lastGroup != -1 && this.countCodePoints(text, lastGroup, backup) - 1 != gs2 || lastGroup == -1 && this.countCodePoints(text, digitStart, position) - 1 > gs2) {
                            strictFail = true;
                            break;
                        }
                        lastGroup = backup;
                    }
                    backup = -1;
                    sawDigit = true;
                    if (digits.count == 0) {
                        if (sawDecimal) {
                            --digits.decimalAt;
                        }
                    } else {
                        ++digitCount;
                        digits.append((char)(digit + 48));
                    }
                } else if (digit > 0 && digit <= 9) {
                    if (strictParse && backup != -1) {
                        if (lastGroup != -1 && this.countCodePoints(text, lastGroup, backup) - 1 != gs2 || lastGroup == -1 && this.countCodePoints(text, digitStart, position) - 1 > gs2) {
                            strictFail = true;
                            break;
                        }
                        lastGroup = backup;
                    }
                    sawDigit = true;
                    ++digitCount;
                    digits.append((char)(digit + 48));
                    backup = -1;
                } else if (ch == decimal) {
                    if (strictParse && (backup != -1 || lastGroup != -1 && this.countCodePoints(text, lastGroup, position) != this.groupingSize + 1)) {
                        strictFail = true;
                        break;
                    }
                    if (this.isParseIntegerOnly() || sawDecimal) break;
                    digits.decimalAt = digitCount;
                    sawDecimal = true;
                } else if (this.isGroupingUsed() && ch == grouping) {
                    if (sawDecimal) break;
                    if (strictParse && (!sawDigit || backup != -1)) {
                        strictFail = true;
                        break;
                    }
                    backup = position;
                    sawGrouping = true;
                } else if (!sawDecimal && decimalEquiv.contains(ch)) {
                    if (strictParse && (backup != -1 || lastGroup != -1 && this.countCodePoints(text, lastGroup, position) != this.groupingSize + 1)) {
                        strictFail = true;
                        break;
                    }
                    if (this.isParseIntegerOnly()) break;
                    digits.decimalAt = digitCount;
                    decimal = (char)ch;
                    sawDecimal = true;
                } else if (this.isGroupingUsed() && !sawGrouping && groupEquiv.contains(ch)) {
                    if (sawDecimal) break;
                    if (strictParse && (!sawDigit || backup != -1)) {
                        strictFail = true;
                        break;
                    }
                    grouping = (char)ch;
                    backup = position;
                    sawGrouping = true;
                } else {
                    if (sawExponent || !text.regionMatches(true, position, exponentSep, 0, exponentSep.length())) break;
                    boolean negExp = false;
                    int pos = position + exponentSep.length();
                    if (pos < text.length()) {
                        ch = UTF16.charAt(text, pos);
                        if (ch == this.symbols.getPlusSign()) {
                            ++pos;
                        } else if (ch == this.symbols.getMinusSign()) {
                            ++pos;
                            negExp = true;
                        }
                    }
                    DigitList exponentDigits = new DigitList();
                    exponentDigits.count = 0;
                    while (pos < text.length()) {
                        digit = UTF16.charAt(text, pos) - digitSymbols[0];
                        if (digit < 0 || digit > 9) {
                            digit = UCharacter.digit(UTF16.charAt(text, pos), 10);
                        }
                        if (digit < 0 || digit > 9) break;
                        exponentDigits.append((char)(digit + 48));
                        pos += UTF16.getCharCount(UTF16.charAt(text, pos));
                    }
                    if (exponentDigits.count <= 0) break;
                    if (strictParse && (backup != -1 || lastGroup != -1)) {
                        strictFail = true;
                        break;
                    }
                    if (exponentDigits.count > 10) {
                        if (negExp) {
                            status[2] = true;
                        } else {
                            status[0] = true;
                        }
                    } else {
                        exponentDigits.decimalAt = exponentDigits.count;
                        exponent = exponentDigits.getLong();
                        if (negExp) {
                            exponent = -exponent;
                        }
                    }
                    position = pos;
                    sawExponent = true;
                    break;
                }
                position += UTF16.getCharCount(ch);
            }
            if (backup != -1) {
                position = backup;
            }
            if (!sawDecimal) {
                digits.decimalAt = digitCount;
            }
            if (strictParse && !sawDecimal && lastGroup != -1 && this.countCodePoints(text, lastGroup, position) != this.groupingSize + 1) {
                strictFail = true;
            }
            if (strictFail) {
                parsePosition.setIndex(oldStart);
                parsePosition.setErrorIndex(position);
                return false;
            }
            if ((exponent += (long)digits.decimalAt) < (long)(-this.getParseMaxDigits())) {
                status[2] = true;
            } else if (exponent > (long)this.getParseMaxDigits()) {
                status[0] = true;
            } else {
                digits.decimalAt = (int)exponent;
            }
            if (!sawDigit && digitCount == 0) {
                parsePosition.setIndex(oldStart);
                parsePosition.setErrorIndex(oldStart);
                return false;
            }
        }
        if (this.formatWidth > 0 && this.padPosition == 2) {
            position = this.skipPadding(text, position);
        }
        if (posMatch >= 0) {
            posMatch = this.compareAffix(text, position, false, false, posSuffix, type, currency);
        }
        if (negMatch >= 0) {
            negMatch = this.compareAffix(text, position, true, false, negSuffix, type, currency);
        }
        if (posMatch >= 0 && negMatch >= 0) {
            if (posMatch > negMatch) {
                negMatch = -1;
            } else if (negMatch > posMatch) {
                posMatch = -1;
            }
        }
        if (posMatch >= 0 == negMatch >= 0) {
            parsePosition.setErrorIndex(position);
            return false;
        }
        position += posMatch >= 0 ? posMatch : negMatch;
        if (this.formatWidth > 0 && this.padPosition == 3) {
            position = this.skipPadding(text, position);
        }
        parsePosition.setIndex(position);
        boolean bl = status[1] = posMatch >= 0;
        if (parsePosition.getIndex() == oldStart) {
            parsePosition.setErrorIndex(position);
            return false;
        }
        return true;
    }

    private int countCodePoints(String str, int start, int end) {
        int count = 0;
        for (int index = start; index < end; index += UTF16.getCharCount(UTF16.charAt(str, index))) {
            ++count;
        }
        return count;
    }

    private UnicodeSet getEquivalentDecimals(char decimal, boolean strictParse) {
        UnicodeSet equivSet = UnicodeSet.EMPTY;
        if (strictParse) {
            if (strictDotEquivalents.contains(decimal)) {
                equivSet = strictDotEquivalents;
            } else if (strictCommaEquivalents.contains(decimal)) {
                equivSet = strictCommaEquivalents;
            }
        } else if (dotEquivalents.contains(decimal)) {
            equivSet = dotEquivalents;
        } else if (commaEquivalents.contains(decimal)) {
            equivSet = commaEquivalents;
        }
        return equivSet;
    }

    private final int skipPadding(String text, int position) {
        while (position < text.length() && text.charAt(position) == this.pad) {
            ++position;
        }
        return position;
    }

    private int compareAffix(String text, int pos, boolean isNegative, boolean isPrefix, String affixPat, int type, Currency[] currency) {
        if (currency != null || this.currencyChoice != null || this.currencySignCount > 0) {
            return this.compareComplexAffix(affixPat, text, pos, type, currency);
        }
        if (isPrefix) {
            return DecimalFormat.compareSimpleAffix(isNegative ? this.negativePrefix : this.positivePrefix, text, pos);
        }
        return DecimalFormat.compareSimpleAffix(isNegative ? this.negativeSuffix : this.positiveSuffix, text, pos);
    }

    private static int compareSimpleAffix(String affix, String input, int pos) {
        int start = pos;
        int i = 0;
        while (i < affix.length()) {
            int c = UTF16.charAt(affix, i);
            int len = UTF16.getCharCount(c);
            if (PatternProps.isWhiteSpace(c)) {
                boolean literalMatch = false;
                while (pos < input.length() && UTF16.charAt(input, pos) == c) {
                    literalMatch = true;
                    pos += len;
                    if ((i += len) == affix.length()) break;
                    c = UTF16.charAt(affix, i);
                    len = UTF16.getCharCount(c);
                    if (PatternProps.isWhiteSpace(c)) continue;
                }
                i = DecimalFormat.skipPatternWhiteSpace(affix, i);
                int s = pos;
                if ((pos = DecimalFormat.skipUWhiteSpace(input, pos)) == s && !literalMatch) {
                    return -1;
                }
                i = DecimalFormat.skipUWhiteSpace(affix, i);
                continue;
            }
            if (pos < input.length() && UTF16.charAt(input, pos) == c) {
                i += len;
                pos += len;
                continue;
            }
            return -1;
        }
        return pos - start;
    }

    private static int skipPatternWhiteSpace(String text, int pos) {
        int c;
        while (pos < text.length() && PatternProps.isWhiteSpace(c = UTF16.charAt(text, pos))) {
            pos += UTF16.getCharCount(c);
        }
        return pos;
    }

    private static int skipUWhiteSpace(String text, int pos) {
        int c;
        while (pos < text.length() && UCharacter.isUWhiteSpace(c = UTF16.charAt(text, pos))) {
            pos += UTF16.getCharCount(c);
        }
        return pos;
    }

    private int compareComplexAffix(String affixPat, String text, int pos, int type, Currency[] currency) {
        int start = pos;
        int i = 0;
        block6: while (i < affixPat.length() && pos >= 0) {
            char c;
            if ((c = affixPat.charAt(i++)) == '\'') {
                while (true) {
                    int j;
                    if ((j = affixPat.indexOf(39, i)) == i) {
                        pos = DecimalFormat.match(text, pos, 39);
                        i = j + 1;
                        continue block6;
                    }
                    if (j <= i) break;
                    pos = DecimalFormat.match(text, pos, affixPat.substring(i, j));
                    i = j + 1;
                    if (i >= affixPat.length() || affixPat.charAt(i) != '\'') continue block6;
                    pos = DecimalFormat.match(text, pos, 39);
                    ++i;
                }
                throw new RuntimeException();
            }
            switch (c) {
                case '\u00a4': {
                    ParsePosition ppos;
                    String iso;
                    ULocale uloc;
                    boolean plural;
                    boolean intl;
                    boolean bl = intl = i < affixPat.length() && affixPat.charAt(i) == '\u00a4';
                    if (intl) {
                        ++i;
                    }
                    boolean bl2 = plural = i < affixPat.length() && affixPat.charAt(i) == '\u00a4';
                    if (plural) {
                        ++i;
                        intl = false;
                    }
                    if ((uloc = this.getLocale(ULocale.VALID_LOCALE)) == null) {
                        uloc = this.symbols.getLocale(ULocale.VALID_LOCALE);
                    }
                    if ((iso = Currency.parse(uloc, text, type, ppos = new ParsePosition(pos))) != null) {
                        if (currency != null) {
                            currency[0] = Currency.getInstance(iso);
                        } else {
                            Currency effectiveCurr = this.getEffectiveCurrency();
                            if (iso.compareTo(effectiveCurr.getCurrencyCode()) != 0) {
                                pos = -1;
                                continue block6;
                            }
                        }
                        pos = ppos.getIndex();
                        continue block6;
                    }
                    pos = -1;
                    continue block6;
                }
                case '%': {
                    c = this.symbols.getPercent();
                    break;
                }
                case '\u2030': {
                    c = this.symbols.getPerMill();
                    break;
                }
                case '-': {
                    c = this.symbols.getMinusSign();
                }
            }
            pos = DecimalFormat.match(text, pos, c);
            if (!PatternProps.isWhiteSpace(c)) continue;
            i = DecimalFormat.skipPatternWhiteSpace(affixPat, i);
        }
        return pos - start;
    }

    static final int match(String text, int pos, int ch) {
        if (pos >= text.length()) {
            return -1;
        }
        if (PatternProps.isWhiteSpace(ch)) {
            int s = pos;
            if ((pos = DecimalFormat.skipPatternWhiteSpace(text, pos)) == s) {
                return -1;
            }
            return pos;
        }
        return pos >= 0 && UTF16.charAt(text, pos) == ch ? pos + UTF16.getCharCount(ch) : -1;
    }

    static final int match(String text, int pos, String str) {
        int ch;
        for (int i = 0; i < str.length() && pos >= 0; i += UTF16.getCharCount(ch)) {
            ch = UTF16.charAt(str, i);
            pos = DecimalFormat.match(text, pos, ch);
            if (!PatternProps.isWhiteSpace(ch)) continue;
            i = DecimalFormat.skipPatternWhiteSpace(str, i);
        }
        return pos;
    }

    public DecimalFormatSymbols getDecimalFormatSymbols() {
        try {
            return (DecimalFormatSymbols)this.symbols.clone();
        }
        catch (Exception foo) {
            return null;
        }
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
        this.symbols = (DecimalFormatSymbols)newSymbols.clone();
        this.setCurrencyForSymbols();
        this.expandAffixes(null);
    }

    private void setCurrencyForSymbols() {
        DecimalFormatSymbols def = new DecimalFormatSymbols(this.symbols.getULocale());
        if (this.symbols.getCurrencySymbol().equals(def.getCurrencySymbol()) && this.symbols.getInternationalCurrencySymbol().equals(def.getInternationalCurrencySymbol())) {
            this.setCurrency(Currency.getInstance(this.symbols.getULocale()));
        } else {
            this.setCurrency(null);
        }
    }

    public String getPositivePrefix() {
        return this.positivePrefix;
    }

    public void setPositivePrefix(String newValue) {
        this.positivePrefix = newValue;
        this.posPrefixPattern = null;
    }

    public String getNegativePrefix() {
        return this.negativePrefix;
    }

    public void setNegativePrefix(String newValue) {
        this.negativePrefix = newValue;
        this.negPrefixPattern = null;
    }

    public String getPositiveSuffix() {
        return this.positiveSuffix;
    }

    public void setPositiveSuffix(String newValue) {
        this.positiveSuffix = newValue;
        this.posSuffixPattern = null;
    }

    public String getNegativeSuffix() {
        return this.negativeSuffix;
    }

    public void setNegativeSuffix(String newValue) {
        this.negativeSuffix = newValue;
        this.negSuffixPattern = null;
    }

    public int getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(int newValue) {
        if (newValue == 0) {
            throw new IllegalArgumentException("Bad multiplier: " + newValue);
        }
        this.multiplier = newValue;
    }

    public BigDecimal getRoundingIncrement() {
        if (this.roundingIncrementICU == null) {
            return null;
        }
        return this.roundingIncrementICU.toBigDecimal();
    }

    public void setRoundingIncrement(BigDecimal newValue) {
        if (newValue == null) {
            this.setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
        } else {
            this.setRoundingIncrement(new com.ibm.icu.math.BigDecimal(newValue));
        }
    }

    public void setRoundingIncrement(com.ibm.icu.math.BigDecimal newValue) {
        int i;
        int n = i = newValue == null ? 0 : newValue.compareTo(com.ibm.icu.math.BigDecimal.ZERO);
        if (i < 0) {
            throw new IllegalArgumentException("Illegal rounding increment");
        }
        if (i == 0) {
            this.setInternalRoundingIncrement(null);
        } else {
            this.setInternalRoundingIncrement(newValue);
        }
        this.setRoundingDouble();
    }

    public void setRoundingIncrement(double newValue) {
        if (newValue < 0.0) {
            throw new IllegalArgumentException("Illegal rounding increment");
        }
        this.roundingDouble = newValue;
        this.roundingDoubleReciprocal = 0.0;
        if (newValue == 0.0) {
            this.setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
        } else {
            this.roundingDouble = newValue;
            if (this.roundingDouble < 1.0) {
                double rawRoundedReciprocal = 1.0 / this.roundingDouble;
                this.setRoundingDoubleReciprocal(rawRoundedReciprocal);
            }
            this.setInternalRoundingIncrement(new com.ibm.icu.math.BigDecimal(newValue));
        }
    }

    private void setRoundingDoubleReciprocal(double rawRoundedReciprocal) {
        this.roundingDoubleReciprocal = Math.rint(rawRoundedReciprocal);
        if (Math.abs(rawRoundedReciprocal - this.roundingDoubleReciprocal) > 1.0E-9) {
            this.roundingDoubleReciprocal = 0.0;
        }
    }

    public int getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(int roundingMode) {
        if (roundingMode < 0 || roundingMode > 7) {
            throw new IllegalArgumentException("Invalid rounding mode: " + roundingMode);
        }
        this.roundingMode = roundingMode;
        if (this.getRoundingIncrement() == null) {
            this.setRoundingIncrement(Math.pow(10.0, -this.getMaximumFractionDigits()));
        }
    }

    public int getFormatWidth() {
        return this.formatWidth;
    }

    public void setFormatWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Illegal format width");
        }
        this.formatWidth = width;
    }

    public char getPadCharacter() {
        return this.pad;
    }

    public void setPadCharacter(char padChar) {
        this.pad = padChar;
    }

    public int getPadPosition() {
        return this.padPosition;
    }

    public void setPadPosition(int padPos) {
        if (padPos < 0 || padPos > 3) {
            throw new IllegalArgumentException("Illegal pad position");
        }
        this.padPosition = padPos;
    }

    public boolean isScientificNotation() {
        return this.useExponentialNotation;
    }

    public void setScientificNotation(boolean useScientific) {
        this.useExponentialNotation = useScientific;
    }

    public byte getMinimumExponentDigits() {
        return this.minExponentDigits;
    }

    public void setMinimumExponentDigits(byte minExpDig) {
        if (minExpDig < 1) {
            throw new IllegalArgumentException("Exponent digits must be >= 1");
        }
        this.minExponentDigits = minExpDig;
    }

    public boolean isExponentSignAlwaysShown() {
        return this.exponentSignAlwaysShown;
    }

    public void setExponentSignAlwaysShown(boolean expSignAlways) {
        this.exponentSignAlwaysShown = expSignAlways;
    }

    public int getGroupingSize() {
        return this.groupingSize;
    }

    public void setGroupingSize(int newValue) {
        this.groupingSize = (byte)newValue;
    }

    public int getSecondaryGroupingSize() {
        return this.groupingSize2;
    }

    public void setSecondaryGroupingSize(int newValue) {
        this.groupingSize2 = (byte)newValue;
    }

    public com.ibm.icu.math.MathContext getMathContextICU() {
        return this.mathContext;
    }

    public MathContext getMathContext() {
        try {
            return this.mathContext == null ? null : new MathContext(this.mathContext.getDigits(), RoundingMode.valueOf(this.mathContext.getRoundingMode()));
        }
        catch (Exception foo) {
            return null;
        }
    }

    public void setMathContextICU(com.ibm.icu.math.MathContext newValue) {
        this.mathContext = newValue;
    }

    public void setMathContext(MathContext newValue) {
        this.mathContext = new com.ibm.icu.math.MathContext(newValue.getPrecision(), 1, false, newValue.getRoundingMode().ordinal());
    }

    public boolean isDecimalSeparatorAlwaysShown() {
        return this.decimalSeparatorAlwaysShown;
    }

    public void setDecimalSeparatorAlwaysShown(boolean newValue) {
        this.decimalSeparatorAlwaysShown = newValue;
    }

    public CurrencyPluralInfo getCurrencyPluralInfo() {
        try {
            return this.currencyPluralInfo == null ? null : (CurrencyPluralInfo)this.currencyPluralInfo.clone();
        }
        catch (Exception foo) {
            return null;
        }
    }

    public void setCurrencyPluralInfo(CurrencyPluralInfo newInfo) {
        this.currencyPluralInfo = (CurrencyPluralInfo)newInfo.clone();
        this.isReadyForParsing = false;
    }

    public Object clone() {
        try {
            DecimalFormat other = (DecimalFormat)super.clone();
            other.symbols = (DecimalFormatSymbols)this.symbols.clone();
            other.digitList = new DigitList();
            if (this.currencyPluralInfo != null) {
                other.currencyPluralInfo = (CurrencyPluralInfo)this.currencyPluralInfo.clone();
            }
            other.attributes = new ArrayList();
            return other;
        }
        catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        DecimalFormat other = (DecimalFormat)obj;
        return this.currencySignCount == other.currencySignCount && (this.style != 6 || this.equals(this.posPrefixPattern, other.posPrefixPattern) && this.equals(this.posSuffixPattern, other.posSuffixPattern) && this.equals(this.negPrefixPattern, other.negPrefixPattern) && this.equals(this.negSuffixPattern, other.negSuffixPattern)) && this.multiplier == other.multiplier && this.groupingSize == other.groupingSize && this.groupingSize2 == other.groupingSize2 && this.decimalSeparatorAlwaysShown == other.decimalSeparatorAlwaysShown && this.useExponentialNotation == other.useExponentialNotation && (!this.useExponentialNotation || this.minExponentDigits == other.minExponentDigits) && this.useSignificantDigits == other.useSignificantDigits && (!this.useSignificantDigits || this.minSignificantDigits == other.minSignificantDigits && this.maxSignificantDigits == other.maxSignificantDigits) && this.symbols.equals(other.symbols) && Utility.objectEquals(this.currencyPluralInfo, other.currencyPluralInfo);
    }

    private boolean equals(String pat1, String pat2) {
        if (pat1 == null || pat2 == null) {
            return pat1 == null && pat2 == null;
        }
        if (pat1.equals(pat2)) {
            return true;
        }
        return this.unquote(pat1).equals(this.unquote(pat2));
    }

    private String unquote(String pat) {
        StringBuilder buf = new StringBuilder(pat.length());
        int i = 0;
        while (i < pat.length()) {
            char ch;
            if ((ch = pat.charAt(i++)) == '\'') continue;
            buf.append(ch);
        }
        return buf.toString();
    }

    public int hashCode() {
        return super.hashCode() * 37 + this.positivePrefix.hashCode();
    }

    public String toPattern() {
        if (this.style == 6) {
            return this.formatPattern;
        }
        return this.toPattern(false);
    }

    public String toLocalizedPattern() {
        if (this.style == 6) {
            return this.formatPattern;
        }
        return this.toPattern(true);
    }

    private void expandAffixes(String pluralCount) {
        this.currencyChoice = null;
        StringBuffer buffer = new StringBuffer();
        if (this.posPrefixPattern != null) {
            this.expandAffix(this.posPrefixPattern, pluralCount, buffer, false);
            this.positivePrefix = buffer.toString();
        }
        if (this.posSuffixPattern != null) {
            this.expandAffix(this.posSuffixPattern, pluralCount, buffer, false);
            this.positiveSuffix = buffer.toString();
        }
        if (this.negPrefixPattern != null) {
            this.expandAffix(this.negPrefixPattern, pluralCount, buffer, false);
            this.negativePrefix = buffer.toString();
        }
        if (this.negSuffixPattern != null) {
            this.expandAffix(this.negSuffixPattern, pluralCount, buffer, false);
            this.negativeSuffix = buffer.toString();
        }
    }

    /*
     * Exception decompiling
     */
    private void expandAffix(String pattern, String pluralCount, StringBuffer buffer, boolean doFormat) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:406)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:481)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    private int appendAffix(StringBuffer buf, boolean isNegative, boolean isPrefix, boolean parseAttr) {
        if (this.currencyChoice != null) {
            String affixPat = null;
            affixPat = isPrefix ? (isNegative ? this.negPrefixPattern : this.posPrefixPattern) : (isNegative ? this.negSuffixPattern : this.posSuffixPattern);
            StringBuffer affixBuf = new StringBuffer();
            this.expandAffix(affixPat, null, affixBuf, true);
            buf.append(affixBuf);
            return affixBuf.length();
        }
        String affix = null;
        if (isPrefix) {
            affix = isNegative ? this.negativePrefix : this.positivePrefix;
        } else {
            String string = affix = isNegative ? this.negativeSuffix : this.positiveSuffix;
        }
        if (parseAttr) {
            int offset = affix.indexOf(this.symbols.getCurrencySymbol());
            if (-1 == offset && -1 == (offset = affix.indexOf(this.symbols.getPercent()))) {
                offset = 0;
            }
            this.formatAffix2Attribute(affix, buf.length() + offset, buf.length() + affix.length());
        }
        buf.append(affix);
        return affix.length();
    }

    private void formatAffix2Attribute(String affix, int begin, int end) {
        if (affix.indexOf(this.symbols.getCurrencySymbol()) > -1) {
            this.addAttribute(NumberFormat.Field.CURRENCY, begin, end);
        } else if (affix.indexOf(this.symbols.getMinusSign()) > -1) {
            this.addAttribute(NumberFormat.Field.SIGN, begin, end);
        } else if (affix.indexOf(this.symbols.getPercent()) > -1) {
            this.addAttribute(NumberFormat.Field.PERCENT, begin, end);
        } else if (affix.indexOf(this.symbols.getPerMill()) > -1) {
            this.addAttribute(NumberFormat.Field.PERMILLE, begin, end);
        }
    }

    private void addAttribute(NumberFormat.Field field, int begin, int end) {
        FieldPosition pos = new FieldPosition(field);
        pos.setBeginIndex(begin);
        pos.setEndIndex(end);
        this.attributes.add(pos);
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return this.formatToCharacterIterator(obj, NULL_UNIT);
    }

    AttributedCharacterIterator formatToCharacterIterator(Object obj, Unit unit) {
        if (!(obj instanceof Number)) {
            throw new IllegalArgumentException();
        }
        Number number = (Number)obj;
        StringBuffer text = new StringBuffer();
        unit.writePrefix(text);
        this.attributes.clear();
        if (obj instanceof BigInteger) {
            this.format((BigInteger)number, text, new FieldPosition(0), true);
        } else if (obj instanceof BigDecimal) {
            this.format((BigDecimal)number, text, new FieldPosition(0), true);
        } else if (obj instanceof Double) {
            this.format(number.doubleValue(), text, new FieldPosition(0), true);
        } else if (obj instanceof Integer || obj instanceof Long) {
            this.format(number.longValue(), text, new FieldPosition(0), true);
        } else {
            throw new IllegalArgumentException();
        }
        unit.writeSuffix(text);
        AttributedString as = new AttributedString(text.toString());
        for (int i = 0; i < this.attributes.size(); ++i) {
            FieldPosition pos = this.attributes.get(i);
            Format.Field attribute = pos.getFieldAttribute();
            as.addAttribute(attribute, attribute, pos.getBeginIndex(), pos.getEndIndex());
        }
        return as.getIterator();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void appendAffixPattern(StringBuffer buffer, boolean isNegative, boolean isPrefix, boolean localized) {
        String affixPat = null;
        if (isPrefix) {
            affixPat = isNegative ? this.negPrefixPattern : this.posPrefixPattern;
        } else {
            String string = affixPat = isNegative ? this.negSuffixPattern : this.posSuffixPattern;
        }
        if (affixPat == null) {
            String affix = null;
            affix = isPrefix ? (isNegative ? this.negativePrefix : this.positivePrefix) : (isNegative ? this.negativeSuffix : this.positiveSuffix);
            buffer.append('\'');
            int i = 0;
            while (true) {
                if (i >= affix.length()) {
                    buffer.append('\'');
                    return;
                }
                char ch = affix.charAt(i);
                if (ch == '\'') {
                    buffer.append(ch);
                }
                buffer.append(ch);
                ++i;
            }
        }
        if (!localized) {
            buffer.append(affixPat);
            return;
        }
        int i = 0;
        while (i < affixPat.length()) {
            block17: {
                char ch = affixPat.charAt(i);
                switch (ch) {
                    case '\'': {
                        int j = affixPat.indexOf(39, i + 1);
                        if (j < 0) {
                            throw new IllegalArgumentException("Malformed affix pattern: " + affixPat);
                        }
                        buffer.append(affixPat.substring(i, j + 1));
                        i = j;
                        break block17;
                    }
                    case '\u2030': {
                        ch = this.symbols.getPerMill();
                        break;
                    }
                    case '%': {
                        ch = this.symbols.getPercent();
                        break;
                    }
                    case '-': {
                        ch = this.symbols.getMinusSign();
                    }
                }
                if (ch == this.symbols.getDecimalSeparator() || ch == this.symbols.getGroupingSeparator()) {
                    buffer.append('\'');
                    buffer.append(ch);
                    buffer.append('\'');
                } else {
                    buffer.append(ch);
                }
            }
            ++i;
        }
    }

    private String toPattern(boolean localized) {
        int i;
        String padSpec;
        int padPos;
        StringBuffer result = new StringBuffer();
        char zero = localized ? (char)this.symbols.getZeroDigit() : (char)'0';
        char digit = localized ? (char)this.symbols.getDigit() : (char)'#';
        char sigDigit = '\u0000';
        boolean useSigDig = this.areSignificantDigitsUsed();
        if (useSigDig) {
            sigDigit = localized ? (char)this.symbols.getSignificantDigit() : (char)'@';
        }
        char group = localized ? (char)this.symbols.getGroupingSeparator() : (char)',';
        int roundingDecimalPos = 0;
        String roundingDigits = null;
        int n = padPos = this.formatWidth > 0 ? this.padPosition : -1;
        String string = this.formatWidth > 0 ? new StringBuffer(2).append(localized ? this.symbols.getPadEscape() : (char)'*').append(this.pad).toString() : (padSpec = null);
        if (this.roundingIncrementICU != null) {
            i = this.roundingIncrementICU.scale();
            roundingDigits = this.roundingIncrementICU.movePointRight(i).toString();
            roundingDecimalPos = roundingDigits.length() - i;
        }
        for (int part = 0; part < 2; ++part) {
            int pos;
            int g;
            if (padPos == 0) {
                result.append(padSpec);
            }
            this.appendAffixPattern(result, part != 0, true, localized);
            if (padPos == 1) {
                result.append(padSpec);
            }
            int sub0Start = result.length();
            int n2 = g = this.isGroupingUsed() ? Math.max(0, this.groupingSize) : 0;
            if (g > 0 && this.groupingSize2 > 0 && this.groupingSize2 != this.groupingSize) {
                g += this.groupingSize2;
            }
            int maxDig = 0;
            int minDig = 0;
            int maxSigDig = 0;
            if (useSigDig) {
                minDig = this.getMinimumSignificantDigits();
                maxDig = maxSigDig = this.getMaximumSignificantDigits();
            } else {
                minDig = this.getMinimumIntegerDigits();
                maxDig = this.getMaximumIntegerDigits();
            }
            if (this.useExponentialNotation) {
                if (maxDig > 8) {
                    maxDig = 1;
                }
            } else {
                maxDig = useSigDig ? Math.max(maxDig, g + 1) : Math.max(Math.max(g, this.getMinimumIntegerDigits()), roundingDecimalPos) + 1;
            }
            for (i = maxDig; i > 0; --i) {
                if (!this.useExponentialNotation && i < maxDig && this.isGroupingPosition(i)) {
                    result.append(group);
                }
                if (useSigDig) {
                    result.append(maxSigDig >= i && i > maxSigDig - minDig ? sigDigit : digit);
                    continue;
                }
                if (roundingDigits != null && (pos = roundingDecimalPos - i) >= 0 && pos < roundingDigits.length()) {
                    result.append((char)(roundingDigits.charAt(pos) - 48 + zero));
                    continue;
                }
                result.append(i <= minDig ? zero : digit);
            }
            if (!useSigDig) {
                if (this.getMaximumFractionDigits() > 0 || this.decimalSeparatorAlwaysShown) {
                    result.append(localized ? this.symbols.getDecimalSeparator() : (char)'.');
                }
                pos = roundingDecimalPos;
                for (i = 0; i < this.getMaximumFractionDigits(); ++i) {
                    if (roundingDigits != null && pos < roundingDigits.length()) {
                        result.append(pos < 0 ? zero : (char)(roundingDigits.charAt(pos) - 48 + zero));
                        ++pos;
                        continue;
                    }
                    result.append(i < this.getMinimumFractionDigits() ? zero : digit);
                }
            }
            if (this.useExponentialNotation) {
                if (localized) {
                    result.append(this.symbols.getExponentSeparator());
                } else {
                    result.append('E');
                }
                if (this.exponentSignAlwaysShown) {
                    result.append(localized ? this.symbols.getPlusSign() : (char)'+');
                }
                for (i = 0; i < this.minExponentDigits; ++i) {
                    result.append(zero);
                }
            }
            if (padSpec != null && !this.useExponentialNotation) {
                int add = this.formatWidth - result.length() + sub0Start - (part == 0 ? this.positivePrefix.length() + this.positiveSuffix.length() : this.negativePrefix.length() + this.negativeSuffix.length());
                while (add > 0) {
                    result.insert(sub0Start, digit);
                    if (--add <= 1 || !this.isGroupingPosition(++maxDig)) continue;
                    result.insert(sub0Start, group);
                    --add;
                }
            }
            if (padPos == 2) {
                result.append(padSpec);
            }
            this.appendAffixPattern(result, part != 0, false, localized);
            if (padPos == 3) {
                result.append(padSpec);
            }
            if (part != 0) continue;
            if (this.negativeSuffix.equals(this.positiveSuffix) && this.negativePrefix.equals('-' + this.positivePrefix)) break;
            result.append(localized ? this.symbols.getPatternSeparator() : (char)';');
        }
        return result.toString();
    }

    public void applyPattern(String pattern) {
        this.applyPattern(pattern, false);
    }

    public void applyLocalizedPattern(String pattern) {
        this.applyPattern(pattern, true);
    }

    private void applyPattern(String pattern, boolean localized) {
        this.applyPatternWithoutExpandAffix(pattern, localized);
        this.expandAffixAdjustWidth(null);
    }

    private void expandAffixAdjustWidth(String pluralCount) {
        this.expandAffixes(pluralCount);
        if (this.formatWidth > 0) {
            this.formatWidth += this.positivePrefix.length() + this.positiveSuffix.length();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applyPatternWithoutExpandAffix(String pattern, boolean localized) {
        char zeroDigit = '0';
        char sigDigit = '@';
        char groupingSeparator = ',';
        char decimalSeparator = '.';
        char percent = '%';
        char perMill = '\u2030';
        char digit = '#';
        char separator = ';';
        String exponent = String.valueOf('E');
        char plus = '+';
        char padEscape = '*';
        char minus = '-';
        if (localized) {
            zeroDigit = this.symbols.getZeroDigit();
            sigDigit = this.symbols.getSignificantDigit();
            groupingSeparator = this.symbols.getGroupingSeparator();
            decimalSeparator = this.symbols.getDecimalSeparator();
            percent = this.symbols.getPercent();
            perMill = this.symbols.getPerMill();
            digit = this.symbols.getDigit();
            separator = this.symbols.getPatternSeparator();
            exponent = this.symbols.getExponentSeparator();
            plus = this.symbols.getPlusSign();
            padEscape = this.symbols.getPadEscape();
            minus = this.symbols.getMinusSign();
        }
        char nineDigit = (char)(zeroDigit + 9);
        boolean gotNegative = false;
        int pos = 0;
        int part = 0;
        while (true) {
            int start;
            StringBuilder affix;
            int currencySignCnt;
            boolean expSignAlways;
            int expDigits;
            long incrementVal;
            int incrementPos;
            char padChar;
            int padPos;
            int groupingCount2;
            int groupingCount;
            int sigDigitCount;
            int digitRightCount;
            int zeroDigitCount;
            int digitLeftCount;
            int multpl;
            int decimalPos;
            StringBuilder suffix;
            StringBuilder prefix;
            int sub2Limit;
            int sub0Limit;
            int sub0Start;
            int subpart;
            if (part < 2 && pos < pattern.length()) {
                subpart = 1;
                sub0Start = 0;
                sub0Limit = 0;
                sub2Limit = 0;
                prefix = new StringBuilder();
                suffix = new StringBuilder();
                decimalPos = -1;
                multpl = 1;
                digitLeftCount = 0;
                zeroDigitCount = 0;
                digitRightCount = 0;
                sigDigitCount = 0;
                groupingCount = -1;
                groupingCount2 = -1;
                padPos = -1;
                padChar = '\u0000';
                incrementPos = -1;
                incrementVal = 0L;
                expDigits = -1;
                expSignAlways = false;
                currencySignCnt = 0;
                affix = prefix;
                start = pos;
            } else {
                if (pattern.length() == 0) {
                    this.posSuffixPattern = "";
                    this.posPrefixPattern = "";
                    this.setMinimumIntegerDigits(0);
                    this.setMaximumIntegerDigits(309);
                    this.setMinimumFractionDigits(0);
                    this.setMaximumFractionDigits(340);
                }
                if (!gotNegative || this.negPrefixPattern.equals(this.posPrefixPattern) && this.negSuffixPattern.equals(this.posSuffixPattern)) {
                    this.negSuffixPattern = this.posSuffixPattern;
                    this.negPrefixPattern = '-' + this.posPrefixPattern;
                }
                this.setLocale(null, null);
                this.formatPattern = pattern;
                if (this.currencySignCount > 0) {
                    Currency theCurrency = this.getCurrency();
                    if (theCurrency != null) {
                        this.setRoundingIncrement(theCurrency.getRoundingIncrement());
                        int d = theCurrency.getDefaultFractionDigits();
                        this.setMinimumFractionDigits(d);
                        this.setMaximumFractionDigits(d);
                    }
                    if (this.currencySignCount == 3 && this.currencyPluralInfo == null) {
                        this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
                    }
                }
                return;
            }
            block6: while (pos < pattern.length()) {
                char ch = pattern.charAt(pos);
                switch (subpart) {
                    case 0: {
                        if (ch == digit) {
                            if (zeroDigitCount > 0 || sigDigitCount > 0) {
                                ++digitRightCount;
                            } else {
                                ++digitLeftCount;
                            }
                            if (groupingCount < 0 || decimalPos >= 0) break;
                            groupingCount = (byte)(groupingCount + 1);
                            break;
                        }
                        if (ch >= zeroDigit && ch <= nineDigit || ch == sigDigit) {
                            if (digitRightCount > 0) {
                                this.patternError("Unexpected '" + ch + '\'', pattern);
                            }
                            if (ch == sigDigit) {
                                ++sigDigitCount;
                            } else {
                                ++zeroDigitCount;
                                if (ch != zeroDigit) {
                                    int p = digitLeftCount + zeroDigitCount + digitRightCount;
                                    if (incrementPos >= 0) {
                                        while (incrementPos < p) {
                                            incrementVal *= 10L;
                                            ++incrementPos;
                                        }
                                    } else {
                                        incrementPos = p;
                                    }
                                    incrementVal += (long)(ch - zeroDigit);
                                }
                            }
                            if (groupingCount < 0 || decimalPos >= 0) break;
                            groupingCount = (byte)(groupingCount + 1);
                            break;
                        }
                        if (ch == groupingSeparator) {
                            char after;
                            if (ch == '\'' && pos + 1 < pattern.length() && (after = pattern.charAt(pos + 1)) != digit && (after < zeroDigit || after > nineDigit)) {
                                if (after == '\'') {
                                    ++pos;
                                } else {
                                    if (groupingCount < 0) {
                                        subpart = 3;
                                        break;
                                    }
                                    subpart = 2;
                                    affix = suffix;
                                    sub0Limit = pos--;
                                    break;
                                }
                            }
                            if (decimalPos >= 0) {
                                this.patternError("Grouping separator after decimal", pattern);
                            }
                            groupingCount2 = groupingCount;
                            groupingCount = 0;
                            break;
                        }
                        if (ch == decimalSeparator) {
                            if (decimalPos >= 0) {
                                this.patternError("Multiple decimal separators", pattern);
                            }
                            decimalPos = digitLeftCount + zeroDigitCount + digitRightCount;
                            break;
                        }
                        if (pattern.regionMatches(pos, exponent, 0, exponent.length())) {
                            if (expDigits >= 0) {
                                this.patternError("Multiple exponential symbols", pattern);
                            }
                            if (groupingCount >= 0) {
                                this.patternError("Grouping separator in exponential", pattern);
                            }
                            if ((pos += exponent.length()) < pattern.length() && pattern.charAt(pos) == plus) {
                                expSignAlways = true;
                                ++pos;
                            }
                            expDigits = 0;
                            while (pos < pattern.length() && pattern.charAt(pos) == zeroDigit) {
                                expDigits = (byte)(expDigits + 1);
                                ++pos;
                            }
                            if (digitLeftCount + zeroDigitCount < 1 && sigDigitCount + digitRightCount < 1 || sigDigitCount > 0 && digitLeftCount > 0 || expDigits < 1) {
                                this.patternError("Malformed exponential", pattern);
                            }
                        }
                        subpart = 2;
                        affix = suffix;
                        sub0Limit = pos--;
                        break;
                    }
                    case 1: 
                    case 2: {
                        if (ch == digit || ch == groupingSeparator || ch == decimalSeparator || ch >= zeroDigit && ch <= nineDigit || ch == sigDigit) {
                            if (subpart == 1) {
                                subpart = 0;
                                sub0Start = pos--;
                                break;
                            }
                            if (ch == '\'') {
                                if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                                    ++pos;
                                    affix.append(ch);
                                    break;
                                }
                                subpart += 2;
                                break;
                            }
                            this.patternError("Unquoted special character '" + ch + '\'', pattern);
                        } else if (ch == '\u00a4') {
                            boolean doubled;
                            boolean bl = doubled = pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\u00a4';
                            if (doubled) {
                                affix.append(ch);
                                if (++pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\u00a4') {
                                    ++pos;
                                    affix.append(ch);
                                    currencySignCnt = 3;
                                } else {
                                    currencySignCnt = 2;
                                }
                            } else {
                                currencySignCnt = 1;
                            }
                        } else if (ch == '\'') {
                            if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                                ++pos;
                                affix.append(ch);
                            } else {
                                subpart += 2;
                            }
                        } else {
                            if (ch == separator) {
                                if (subpart == 1 || part == 1) {
                                    this.patternError("Unquoted special character '" + ch + '\'', pattern);
                                }
                                sub2Limit = pos++;
                                break block6;
                            }
                            if (ch == percent || ch == perMill) {
                                if (multpl != 1) {
                                    this.patternError("Too many percent/permille characters", pattern);
                                }
                                multpl = ch == percent ? 100 : 1000;
                                ch = ch == percent ? (char)'%' : '\u2030';
                            } else if (ch == minus) {
                                ch = '-';
                            } else if (ch == padEscape) {
                                if (padPos >= 0) {
                                    this.patternError("Multiple pad specifiers", pattern);
                                }
                                if (pos + 1 == pattern.length()) {
                                    this.patternError("Invalid pad specifier", pattern);
                                }
                                padPos = pos++;
                                padChar = pattern.charAt(pos);
                                break;
                            }
                        }
                        affix.append(ch);
                        break;
                    }
                    case 3: 
                    case 4: {
                        if (ch == '\'') {
                            if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                                ++pos;
                                affix.append(ch);
                            } else {
                                subpart -= 2;
                            }
                        }
                        affix.append(ch);
                    }
                }
                ++pos;
            }
            if (subpart == 3 || subpart == 4) {
                this.patternError("Unterminated quote", pattern);
            }
            if (sub0Limit == 0) {
                sub0Limit = pattern.length();
            }
            if (sub2Limit == 0) {
                sub2Limit = pattern.length();
            }
            if (zeroDigitCount == 0 && sigDigitCount == 0 && digitLeftCount > 0 && decimalPos >= 0) {
                int n = decimalPos;
                if (n == 0) {
                    ++n;
                }
                digitRightCount = digitLeftCount - n;
                digitLeftCount = n - 1;
                zeroDigitCount = 1;
            }
            if (decimalPos < 0 && digitRightCount > 0 && sigDigitCount == 0 || decimalPos >= 0 && (sigDigitCount > 0 || decimalPos < digitLeftCount || decimalPos > digitLeftCount + zeroDigitCount) || groupingCount == 0 || groupingCount2 == 0 || sigDigitCount > 0 && zeroDigitCount > 0 || subpart > 2) {
                this.patternError("Malformed pattern", pattern);
            }
            if (padPos >= 0) {
                if (padPos == start) {
                    padPos = 0;
                } else if (padPos + 2 == sub0Start) {
                    padPos = 1;
                } else if (padPos == sub0Limit) {
                    padPos = 2;
                } else if (padPos + 2 == sub2Limit) {
                    padPos = 3;
                } else {
                    this.patternError("Illegal pad position", pattern);
                }
            }
            if (part == 0) {
                this.posPrefixPattern = this.negPrefixPattern = prefix.toString();
                this.posSuffixPattern = this.negSuffixPattern = suffix.toString();
                boolean bl = this.useExponentialNotation = expDigits >= 0;
                if (this.useExponentialNotation) {
                    this.minExponentDigits = (byte)expDigits;
                    this.exponentSignAlwaysShown = expSignAlways;
                }
                int digitTotalCount = digitLeftCount + zeroDigitCount + digitRightCount;
                int effectiveDecimalPos = decimalPos >= 0 ? decimalPos : digitTotalCount;
                boolean useSigDig = sigDigitCount > 0;
                this.setSignificantDigitsUsed(useSigDig);
                if (useSigDig) {
                    this.setMinimumSignificantDigits(sigDigitCount);
                    this.setMaximumSignificantDigits(sigDigitCount + digitRightCount);
                } else {
                    int minInt = effectiveDecimalPos - digitLeftCount;
                    this.setMinimumIntegerDigits(minInt);
                    this.setMaximumIntegerDigits(this.useExponentialNotation ? digitLeftCount + minInt : 309);
                    this.setMaximumFractionDigits(decimalPos >= 0 ? digitTotalCount - decimalPos : 0);
                    this.setMinimumFractionDigits(decimalPos >= 0 ? digitLeftCount + zeroDigitCount - decimalPos : 0);
                }
                this.setGroupingUsed(groupingCount > 0);
                this.groupingSize = (byte)(groupingCount > 0 ? groupingCount : 0);
                this.groupingSize2 = groupingCount2 > 0 && groupingCount2 != groupingCount ? groupingCount2 : 0;
                this.multiplier = multpl;
                this.setDecimalSeparatorAlwaysShown(decimalPos == 0 || decimalPos == digitTotalCount);
                if (padPos >= 0) {
                    this.padPosition = padPos;
                    this.formatWidth = sub0Limit - sub0Start;
                    this.pad = padChar;
                } else {
                    this.formatWidth = 0;
                }
                if (incrementVal != 0L) {
                    int scale = incrementPos - effectiveDecimalPos;
                    this.roundingIncrementICU = com.ibm.icu.math.BigDecimal.valueOf(incrementVal, scale > 0 ? scale : 0);
                    if (scale < 0) {
                        this.roundingIncrementICU = this.roundingIncrementICU.movePointRight(-scale);
                    }
                    this.setRoundingDouble();
                    this.roundingMode = 6;
                } else {
                    this.setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
                }
                this.currencySignCount = currencySignCnt;
            } else {
                this.negPrefixPattern = prefix.toString();
                this.negSuffixPattern = suffix.toString();
                gotNegative = true;
            }
            ++part;
        }
    }

    private void setRoundingDouble() {
        if (this.roundingIncrementICU == null) {
            this.roundingDouble = 0.0;
            this.roundingDoubleReciprocal = 0.0;
        } else {
            this.roundingDouble = this.roundingIncrementICU.doubleValue();
            this.setRoundingDoubleReciprocal(1.0 / this.roundingDouble);
        }
    }

    private void patternError(String msg, String pattern) {
        throw new IllegalArgumentException(msg + " in pattern \"" + pattern + '\"');
    }

    public void setMaximumIntegerDigits(int newValue) {
        super.setMaximumIntegerDigits(Math.min(newValue, 309));
    }

    public void setMinimumIntegerDigits(int newValue) {
        super.setMinimumIntegerDigits(Math.min(newValue, 309));
    }

    public int getMinimumSignificantDigits() {
        return this.minSignificantDigits;
    }

    public int getMaximumSignificantDigits() {
        return this.maxSignificantDigits;
    }

    public void setMinimumSignificantDigits(int min) {
        if (min < 1) {
            min = 1;
        }
        int max = Math.max(this.maxSignificantDigits, min);
        this.minSignificantDigits = min;
        this.maxSignificantDigits = max;
    }

    public void setMaximumSignificantDigits(int max) {
        int min;
        if (max < 1) {
            max = 1;
        }
        this.minSignificantDigits = min = Math.min(this.minSignificantDigits, max);
        this.maxSignificantDigits = max;
    }

    public boolean areSignificantDigitsUsed() {
        return this.useSignificantDigits;
    }

    public void setSignificantDigitsUsed(boolean useSignificantDigits) {
        this.useSignificantDigits = useSignificantDigits;
    }

    public void setCurrency(Currency theCurrency) {
        super.setCurrency(theCurrency);
        if (theCurrency != null) {
            boolean[] isChoiceFormat = new boolean[1];
            String s = theCurrency.getName(this.symbols.getULocale(), 0, isChoiceFormat);
            this.symbols.setCurrency(theCurrency);
            this.symbols.setCurrencySymbol(s);
        }
        if (this.currencySignCount > 0) {
            if (theCurrency != null) {
                this.setRoundingIncrement(theCurrency.getRoundingIncrement());
                int d = theCurrency.getDefaultFractionDigits();
                this.setMinimumFractionDigits(d);
                this.setMaximumFractionDigits(d);
            }
            if (this.currencySignCount != 3) {
                this.expandAffixes(null);
            }
        }
    }

    @Deprecated
    protected Currency getEffectiveCurrency() {
        Currency c = this.getCurrency();
        if (c == null) {
            c = Currency.getInstance(this.symbols.getInternationalCurrencySymbol());
        }
        return c;
    }

    public void setMaximumFractionDigits(int newValue) {
        super.setMaximumFractionDigits(Math.min(newValue, 340));
    }

    public void setMinimumFractionDigits(int newValue) {
        super.setMinimumFractionDigits(Math.min(newValue, 340));
    }

    public void setParseBigDecimal(boolean value) {
        this.parseBigDecimal = value;
    }

    public boolean isParseBigDecimal() {
        return this.parseBigDecimal;
    }

    public void setParseMaxDigits(int newValue) {
        if (newValue > 0) {
            this.PARSE_MAX_EXPONENT = newValue;
        }
    }

    public int getParseMaxDigits() {
        return this.PARSE_MAX_EXPONENT;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        this.attributes.clear();
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.getMaximumIntegerDigits() > 309) {
            this.setMaximumIntegerDigits(309);
        }
        if (this.getMaximumFractionDigits() > 340) {
            this.setMaximumFractionDigits(340);
        }
        if (this.serialVersionOnStream < 2) {
            this.exponentSignAlwaysShown = false;
            this.setInternalRoundingIncrement(null);
            this.setRoundingDouble();
            this.roundingMode = 6;
            this.formatWidth = 0;
            this.pad = (char)32;
            this.padPosition = 0;
            if (this.serialVersionOnStream < 1) {
                this.useExponentialNotation = false;
            }
        }
        if (this.serialVersionOnStream < 3) {
            this.setCurrencyForSymbols();
        }
        this.serialVersionOnStream = 3;
        this.digitList = new DigitList();
        if (this.roundingIncrement != null) {
            this.setInternalRoundingIncrement(new com.ibm.icu.math.BigDecimal(this.roundingIncrement));
            this.setRoundingDouble();
        }
    }

    private void setInternalRoundingIncrement(com.ibm.icu.math.BigDecimal value) {
        this.roundingIncrementICU = value;
        this.roundingIncrement = value == null ? null : value.toBigDecimal();
    }

    static class Unit {
        private final String prefix;
        private final String suffix;

        public Unit(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public void writeSuffix(StringBuffer toAppendTo) {
            toAppendTo.append(this.suffix);
        }

        public void writePrefix(StringBuffer toAppendTo) {
            toAppendTo.append(this.prefix);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Unit)) {
                return false;
            }
            Unit other = (Unit)obj;
            return this.prefix.equals(other.prefix) && this.suffix.equals(other.suffix);
        }
    }

    private static final class AffixForCurrency {
        private String negPrefixPatternForCurrency = null;
        private String negSuffixPatternForCurrency = null;
        private String posPrefixPatternForCurrency = null;
        private String posSuffixPatternForCurrency = null;
        private final int patternType;

        public AffixForCurrency(String negPrefix, String negSuffix, String posPrefix, String posSuffix, int type) {
            this.negPrefixPatternForCurrency = negPrefix;
            this.negSuffixPatternForCurrency = negSuffix;
            this.posPrefixPatternForCurrency = posPrefix;
            this.posSuffixPatternForCurrency = posSuffix;
            this.patternType = type;
        }

        public String getNegPrefix() {
            return this.negPrefixPatternForCurrency;
        }

        public String getNegSuffix() {
            return this.negSuffixPatternForCurrency;
        }

        public String getPosPrefix() {
            return this.posPrefixPatternForCurrency;
        }

        public String getPosSuffix() {
            return this.posSuffixPatternForCurrency;
        }

        public int getPatternType() {
            return this.patternType;
        }
    }
}

