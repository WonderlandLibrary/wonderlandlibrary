/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.text.CurrencyPluralInfo;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.text.NumberingSystem;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.text.UFormat;
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
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public abstract class NumberFormat
extends UFormat {
    public static final int NUMBERSTYLE = 0;
    public static final int CURRENCYSTYLE = 1;
    public static final int PERCENTSTYLE = 2;
    public static final int SCIENTIFICSTYLE = 3;
    public static final int INTEGERSTYLE = 4;
    public static final int ISOCURRENCYSTYLE = 5;
    public static final int PLURALCURRENCYSTYLE = 6;
    public static final int ACCOUNTINGCURRENCYSTYLE = 7;
    public static final int CASHCURRENCYSTYLE = 8;
    public static final int STANDARDCURRENCYSTYLE = 9;
    public static final int INTEGER_FIELD = 0;
    public static final int FRACTION_FIELD = 1;
    private static NumberFormatShim shim;
    private static final char[] doubleCurrencySign;
    private static final String doubleCurrencyStr;
    private boolean groupingUsed = true;
    private byte maxIntegerDigits = (byte)40;
    private byte minIntegerDigits = 1;
    private byte maxFractionDigits = (byte)3;
    private byte minFractionDigits = 0;
    private boolean parseIntegerOnly = false;
    private int maximumIntegerDigits = 40;
    private int minimumIntegerDigits = 1;
    private int maximumFractionDigits = 3;
    private int minimumFractionDigits = 0;
    private Currency currency;
    static final int currentSerialVersion = 2;
    private int serialVersionOnStream = 2;
    private static final long serialVersionUID = -2308460125733713944L;
    private boolean parseStrict;
    private DisplayContext capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;

    @Override
    public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
        if (number instanceof Long) {
            return this.format((Long)number, toAppendTo, pos);
        }
        if (number instanceof BigInteger) {
            return this.format((BigInteger)number, toAppendTo, pos);
        }
        if (number instanceof BigDecimal) {
            return this.format((BigDecimal)number, toAppendTo, pos);
        }
        if (number instanceof com.ibm.icu.math.BigDecimal) {
            return this.format((com.ibm.icu.math.BigDecimal)number, toAppendTo, pos);
        }
        if (number instanceof CurrencyAmount) {
            return this.format((CurrencyAmount)number, toAppendTo, pos);
        }
        if (number instanceof Number) {
            return this.format(((Number)number).doubleValue(), toAppendTo, pos);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Number");
    }

    @Override
    public final Object parseObject(String source, ParsePosition parsePosition) {
        return this.parse(source, parsePosition);
    }

    public final String format(double number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public final String format(long number) {
        StringBuffer buf = new StringBuffer(19);
        FieldPosition pos = new FieldPosition(0);
        this.format(number, buf, pos);
        return buf.toString();
    }

    public final String format(BigInteger number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public final String format(BigDecimal number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public final String format(com.ibm.icu.math.BigDecimal number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public final String format(CurrencyAmount currAmt) {
        return this.format(currAmt, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public abstract StringBuffer format(double var1, StringBuffer var3, FieldPosition var4);

    public abstract StringBuffer format(long var1, StringBuffer var3, FieldPosition var4);

    public abstract StringBuffer format(BigInteger var1, StringBuffer var2, FieldPosition var3);

    public abstract StringBuffer format(BigDecimal var1, StringBuffer var2, FieldPosition var3);

    public abstract StringBuffer format(com.ibm.icu.math.BigDecimal var1, StringBuffer var2, FieldPosition var3);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public StringBuffer format(CurrencyAmount currAmt, StringBuffer toAppendTo, FieldPosition pos) {
        NumberFormat numberFormat = this;
        synchronized (numberFormat) {
            Currency save = this.getCurrency();
            Currency curr = currAmt.getCurrency();
            boolean same = curr.equals(save);
            if (!same) {
                this.setCurrency(curr);
            }
            this.format(currAmt.getNumber(), toAppendTo, pos);
            if (!same) {
                this.setCurrency(save);
            }
        }
        return toAppendTo;
    }

    public abstract Number parse(String var1, ParsePosition var2);

    public Number parse(String text) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Number result = this.parse(text, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new ParseException("Unparseable number: \"" + text + '\"', parsePosition.getErrorIndex());
        }
        return result;
    }

    public CurrencyAmount parseCurrency(CharSequence text, ParsePosition pos) {
        Number n2 = this.parse(text.toString(), pos);
        return n2 == null ? null : new CurrencyAmount(n2, this.getEffectiveCurrency());
    }

    public boolean isParseIntegerOnly() {
        return this.parseIntegerOnly;
    }

    public void setParseIntegerOnly(boolean value) {
        this.parseIntegerOnly = value;
    }

    public void setParseStrict(boolean value) {
        this.parseStrict = value;
    }

    public boolean isParseStrict() {
        return this.parseStrict;
    }

    public void setContext(DisplayContext context) {
        if (context.type() == DisplayContext.Type.CAPITALIZATION) {
            this.capitalizationSetting = context;
        }
    }

    public DisplayContext getContext(DisplayContext.Type type) {
        return type == DisplayContext.Type.CAPITALIZATION && this.capitalizationSetting != null ? this.capitalizationSetting : DisplayContext.CAPITALIZATION_NONE;
    }

    public static final NumberFormat getInstance() {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 0);
    }

    public static NumberFormat getInstance(Locale inLocale) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), 0);
    }

    public static NumberFormat getInstance(ULocale inLocale) {
        return NumberFormat.getInstance(inLocale, 0);
    }

    public static final NumberFormat getInstance(int style) {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), style);
    }

    public static NumberFormat getInstance(Locale inLocale, int style) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), style);
    }

    public static final NumberFormat getNumberInstance() {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 0);
    }

    public static NumberFormat getNumberInstance(Locale inLocale) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), 0);
    }

    public static NumberFormat getNumberInstance(ULocale inLocale) {
        return NumberFormat.getInstance(inLocale, 0);
    }

    public static final NumberFormat getIntegerInstance() {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 4);
    }

    public static NumberFormat getIntegerInstance(Locale inLocale) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), 4);
    }

    public static NumberFormat getIntegerInstance(ULocale inLocale) {
        return NumberFormat.getInstance(inLocale, 4);
    }

    public static final NumberFormat getCurrencyInstance() {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 1);
    }

    public static NumberFormat getCurrencyInstance(Locale inLocale) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), 1);
    }

    public static NumberFormat getCurrencyInstance(ULocale inLocale) {
        return NumberFormat.getInstance(inLocale, 1);
    }

    public static final NumberFormat getPercentInstance() {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 2);
    }

    public static NumberFormat getPercentInstance(Locale inLocale) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), 2);
    }

    public static NumberFormat getPercentInstance(ULocale inLocale) {
        return NumberFormat.getInstance(inLocale, 2);
    }

    public static final NumberFormat getScientificInstance() {
        return NumberFormat.getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 3);
    }

    public static NumberFormat getScientificInstance(Locale inLocale) {
        return NumberFormat.getInstance(ULocale.forLocale(inLocale), 3);
    }

    public static NumberFormat getScientificInstance(ULocale inLocale) {
        return NumberFormat.getInstance(inLocale, 3);
    }

    private static NumberFormatShim getShim() {
        if (shim == null) {
            try {
                Class<?> cls = Class.forName("com.ibm.icu.text.NumberFormatServiceShim");
                shim = (NumberFormatShim)cls.newInstance();
            }
            catch (MissingResourceException e2) {
                throw e2;
            }
            catch (Exception e3) {
                throw new RuntimeException(e3.getMessage());
            }
        }
        return shim;
    }

    public static Locale[] getAvailableLocales() {
        if (shim == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return NumberFormat.getShim().getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        if (shim == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return NumberFormat.getShim().getAvailableULocales();
    }

    public static Object registerFactory(NumberFormatFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null");
        }
        return NumberFormat.getShim().registerFactory(factory);
    }

    public static boolean unregister(Object registryKey) {
        if (registryKey == null) {
            throw new IllegalArgumentException("registryKey must not be null");
        }
        if (shim == null) {
            return false;
        }
        return shim.unregister(registryKey);
    }

    public int hashCode() {
        return this.maximumIntegerDigits * 37 + this.maxFractionDigits;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NumberFormat other = (NumberFormat)obj;
        return this.maximumIntegerDigits == other.maximumIntegerDigits && this.minimumIntegerDigits == other.minimumIntegerDigits && this.maximumFractionDigits == other.maximumFractionDigits && this.minimumFractionDigits == other.minimumFractionDigits && this.groupingUsed == other.groupingUsed && this.parseIntegerOnly == other.parseIntegerOnly && this.parseStrict == other.parseStrict && this.capitalizationSetting == other.capitalizationSetting;
    }

    @Override
    public Object clone() {
        NumberFormat other = (NumberFormat)super.clone();
        return other;
    }

    public boolean isGroupingUsed() {
        return this.groupingUsed;
    }

    public void setGroupingUsed(boolean newValue) {
        this.groupingUsed = newValue;
    }

    public int getMaximumIntegerDigits() {
        return this.maximumIntegerDigits;
    }

    public void setMaximumIntegerDigits(int newValue) {
        this.maximumIntegerDigits = Math.max(0, newValue);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.minimumIntegerDigits = this.maximumIntegerDigits;
        }
    }

    public int getMinimumIntegerDigits() {
        return this.minimumIntegerDigits;
    }

    public void setMinimumIntegerDigits(int newValue) {
        this.minimumIntegerDigits = Math.max(0, newValue);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.maximumIntegerDigits = this.minimumIntegerDigits;
        }
    }

    public int getMaximumFractionDigits() {
        return this.maximumFractionDigits;
    }

    public void setMaximumFractionDigits(int newValue) {
        this.maximumFractionDigits = Math.max(0, newValue);
        if (this.maximumFractionDigits < this.minimumFractionDigits) {
            this.minimumFractionDigits = this.maximumFractionDigits;
        }
    }

    public int getMinimumFractionDigits() {
        return this.minimumFractionDigits;
    }

    public void setMinimumFractionDigits(int newValue) {
        this.minimumFractionDigits = Math.max(0, newValue);
        if (this.maximumFractionDigits < this.minimumFractionDigits) {
            this.maximumFractionDigits = this.minimumFractionDigits;
        }
    }

    public void setCurrency(Currency theCurrency) {
        this.currency = theCurrency;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    @Deprecated
    protected Currency getEffectiveCurrency() {
        Currency c2 = this.getCurrency();
        if (c2 == null) {
            ULocale uloc = this.getLocale(ULocale.VALID_LOCALE);
            if (uloc == null) {
                uloc = ULocale.getDefault(ULocale.Category.FORMAT);
            }
            c2 = Currency.getInstance(uloc);
        }
        return c2;
    }

    public int getRoundingMode() {
        throw new UnsupportedOperationException("getRoundingMode must be implemented by the subclass implementation.");
    }

    public void setRoundingMode(int roundingMode) {
        throw new UnsupportedOperationException("setRoundingMode must be implemented by the subclass implementation.");
    }

    public static NumberFormat getInstance(ULocale desiredLocale, int choice) {
        if (choice < 0 || choice > 9) {
            throw new IllegalArgumentException("choice should be from NUMBERSTYLE to STANDARDCURRENCYSTYLE");
        }
        return NumberFormat.getShim().createInstance(desiredLocale, choice);
    }

    static NumberFormat createInstance(ULocale desiredLocale, int choice) {
        NumberFormat format;
        NumberingSystem ns;
        String temp;
        String pattern = NumberFormat.getPattern(desiredLocale, choice);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(desiredLocale);
        if ((choice == 1 || choice == 5 || choice == 7 || choice == 8 || choice == 9) && (temp = symbols.getCurrencyPattern()) != null) {
            pattern = temp;
        }
        if (choice == 5) {
            pattern = pattern.replace("\u00a4", doubleCurrencyStr);
        }
        if ((ns = NumberingSystem.getInstance(desiredLocale)) == null) {
            return null;
        }
        if (ns != null && ns.isAlgorithmic()) {
            ULocale nsLoc;
            String nsRuleSetName;
            int desiredRulesType = 4;
            String nsDesc = ns.getDescription();
            int firstSlash = nsDesc.indexOf("/");
            int lastSlash = nsDesc.lastIndexOf("/");
            if (lastSlash > firstSlash) {
                String nsLocID = nsDesc.substring(0, firstSlash);
                String nsRuleSetGroup = nsDesc.substring(firstSlash + 1, lastSlash);
                nsRuleSetName = nsDesc.substring(lastSlash + 1);
                nsLoc = new ULocale(nsLocID);
                if (nsRuleSetGroup.equals("SpelloutRules")) {
                    desiredRulesType = 1;
                }
            } else {
                nsLoc = desiredLocale;
                nsRuleSetName = nsDesc;
            }
            RuleBasedNumberFormat r2 = new RuleBasedNumberFormat(nsLoc, desiredRulesType);
            r2.setDefaultRuleSet(nsRuleSetName);
            format = r2;
        } else {
            DecimalFormat f2 = new DecimalFormat(pattern, symbols, choice);
            if (choice == 4) {
                f2.setMaximumFractionDigits(0);
                f2.setDecimalSeparatorAlwaysShown(false);
                f2.setParseIntegerOnly(true);
            }
            if (choice == 8) {
                f2.setCurrencyUsage(Currency.CurrencyUsage.CASH);
            }
            if (choice == 6) {
                f2.setCurrencyPluralInfo(CurrencyPluralInfo.getInstance(desiredLocale));
            }
            format = f2;
        }
        ULocale valid = symbols.getLocale(ULocale.VALID_LOCALE);
        ULocale actual = symbols.getLocale(ULocale.ACTUAL_LOCALE);
        format.setLocale(valid, actual);
        return format;
    }

    @Deprecated
    protected static String getPattern(Locale forLocale, int choice) {
        return NumberFormat.getPattern(ULocale.forLocale(forLocale), choice);
    }

    protected static String getPattern(ULocale forLocale, int choice) {
        return NumberFormat.getPatternForStyle(forLocale, choice);
    }

    @Deprecated
    public static String getPatternForStyle(ULocale forLocale, int choice) {
        NumberingSystem ns = NumberingSystem.getInstance(forLocale);
        String nsName = ns.getName();
        return NumberFormat.getPatternForStyleAndNumberingSystem(forLocale, nsName, choice);
    }

    @Deprecated
    public static String getPatternForStyleAndNumberingSystem(ULocale forLocale, String nsName, int choice) {
        String patternKey = null;
        switch (choice) {
            case 0: 
            case 4: 
            case 6: {
                patternKey = "decimalFormat";
                break;
            }
            case 1: {
                String cfKeyValue = forLocale.getKeywordValue("cf");
                patternKey = cfKeyValue != null && cfKeyValue.equals("account") ? "accountingFormat" : "currencyFormat";
                break;
            }
            case 5: 
            case 8: 
            case 9: {
                patternKey = "currencyFormat";
                break;
            }
            case 2: {
                patternKey = "percentFormat";
                break;
            }
            case 3: {
                patternKey = "scientificFormat";
                break;
            }
            case 7: {
                patternKey = "accountingFormat";
                break;
            }
            default: {
                assert (false);
                patternKey = "decimalFormat";
            }
        }
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", forLocale);
        String result = rb.findStringWithFallback("NumberElements/" + nsName + "/patterns/" + patternKey);
        if (result == null) {
            result = rb.getStringWithFallback("NumberElements/latn/patterns/" + patternKey);
        }
        return result;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            this.maximumIntegerDigits = this.maxIntegerDigits;
            this.minimumIntegerDigits = this.minIntegerDigits;
            this.maximumFractionDigits = this.maxFractionDigits;
            this.minimumFractionDigits = this.minFractionDigits;
        }
        if (this.serialVersionOnStream < 2) {
            this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
        }
        if (this.minimumIntegerDigits > this.maximumIntegerDigits || this.minimumFractionDigits > this.maximumFractionDigits || this.minimumIntegerDigits < 0 || this.minimumFractionDigits < 0) {
            throw new InvalidObjectException("Digit count range invalid");
        }
        this.serialVersionOnStream = 2;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        this.maxIntegerDigits = (byte)(this.maximumIntegerDigits > 127 ? 127 : (byte)this.maximumIntegerDigits);
        this.minIntegerDigits = (byte)(this.minimumIntegerDigits > 127 ? 127 : (byte)this.minimumIntegerDigits);
        this.maxFractionDigits = (byte)(this.maximumFractionDigits > 127 ? 127 : (byte)this.maximumFractionDigits);
        this.minFractionDigits = (byte)(this.minimumFractionDigits > 127 ? 127 : (byte)this.minimumFractionDigits);
        stream.defaultWriteObject();
    }

    static {
        doubleCurrencySign = new char[]{'\u00a4', '\u00a4'};
        doubleCurrencyStr = new String(doubleCurrencySign);
    }

    public static class Field
    extends Format.Field {
        static final long serialVersionUID = -4516273749929385842L;
        public static final Field SIGN = new Field("sign");
        public static final Field INTEGER = new Field("integer");
        public static final Field FRACTION = new Field("fraction");
        public static final Field EXPONENT = new Field("exponent");
        public static final Field EXPONENT_SIGN = new Field("exponent sign");
        public static final Field EXPONENT_SYMBOL = new Field("exponent symbol");
        public static final Field DECIMAL_SEPARATOR = new Field("decimal separator");
        public static final Field GROUPING_SEPARATOR = new Field("grouping separator");
        public static final Field PERCENT = new Field("percent");
        public static final Field PERMILLE = new Field("per mille");
        public static final Field CURRENCY = new Field("currency");

        protected Field(String fieldName) {
            super(fieldName);
        }

        @Override
        protected Object readResolve() throws InvalidObjectException {
            if (this.getName().equals(INTEGER.getName())) {
                return INTEGER;
            }
            if (this.getName().equals(FRACTION.getName())) {
                return FRACTION;
            }
            if (this.getName().equals(EXPONENT.getName())) {
                return EXPONENT;
            }
            if (this.getName().equals(EXPONENT_SIGN.getName())) {
                return EXPONENT_SIGN;
            }
            if (this.getName().equals(EXPONENT_SYMBOL.getName())) {
                return EXPONENT_SYMBOL;
            }
            if (this.getName().equals(CURRENCY.getName())) {
                return CURRENCY;
            }
            if (this.getName().equals(DECIMAL_SEPARATOR.getName())) {
                return DECIMAL_SEPARATOR;
            }
            if (this.getName().equals(GROUPING_SEPARATOR.getName())) {
                return GROUPING_SEPARATOR;
            }
            if (this.getName().equals(PERCENT.getName())) {
                return PERCENT;
            }
            if (this.getName().equals(PERMILLE.getName())) {
                return PERMILLE;
            }
            if (this.getName().equals(SIGN.getName())) {
                return SIGN;
            }
            throw new InvalidObjectException("An invalid object.");
        }
    }

    static abstract class NumberFormatShim {
        NumberFormatShim() {
        }

        abstract Locale[] getAvailableLocales();

        abstract ULocale[] getAvailableULocales();

        abstract Object registerFactory(NumberFormatFactory var1);

        abstract boolean unregister(Object var1);

        abstract NumberFormat createInstance(ULocale var1, int var2);
    }

    public static abstract class SimpleNumberFormatFactory
    extends NumberFormatFactory {
        final Set<String> localeNames;
        final boolean visible;

        public SimpleNumberFormatFactory(Locale locale) {
            this(locale, true);
        }

        public SimpleNumberFormatFactory(Locale locale, boolean visible) {
            this.localeNames = Collections.singleton(ULocale.forLocale(locale).getBaseName());
            this.visible = visible;
        }

        public SimpleNumberFormatFactory(ULocale locale) {
            this(locale, true);
        }

        public SimpleNumberFormatFactory(ULocale locale, boolean visible) {
            this.localeNames = Collections.singleton(locale.getBaseName());
            this.visible = visible;
        }

        @Override
        public final boolean visible() {
            return this.visible;
        }

        @Override
        public final Set<String> getSupportedLocaleNames() {
            return this.localeNames;
        }
    }

    public static abstract class NumberFormatFactory {
        public static final int FORMAT_NUMBER = 0;
        public static final int FORMAT_CURRENCY = 1;
        public static final int FORMAT_PERCENT = 2;
        public static final int FORMAT_SCIENTIFIC = 3;
        public static final int FORMAT_INTEGER = 4;

        public boolean visible() {
            return true;
        }

        public abstract Set<String> getSupportedLocaleNames();

        public NumberFormat createFormat(ULocale loc, int formatType) {
            return this.createFormat(loc.toLocale(), formatType);
        }

        public NumberFormat createFormat(Locale loc, int formatType) {
            return this.createFormat(ULocale.forLocale(loc), formatType);
        }

        protected NumberFormatFactory() {
        }
    }
}

