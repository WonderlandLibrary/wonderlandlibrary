/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.MissingResourceException;

public final class DateNumberFormat
extends NumberFormat {
    private static final long serialVersionUID = -6315692826916346953L;
    private char[] digits;
    private char zeroDigit;
    private char minusSign;
    private boolean positiveOnly = false;
    private static final int DECIMAL_BUF_SIZE = 20;
    private transient char[] decimalBuf = new char[20];
    private static SimpleCache<ULocale, char[]> CACHE = new SimpleCache();
    private int maxIntDigits;
    private int minIntDigits;
    private static final long PARSE_THRESHOLD = 0xCCCCCCCCCCCCCCBL;

    public DateNumberFormat(ULocale loc, String digitString, String nsName) {
        if (digitString.length() > 10) {
            throw new UnsupportedOperationException("DateNumberFormat does not support digits out of BMP.");
        }
        this.initialize(loc, digitString, nsName);
    }

    public DateNumberFormat(ULocale loc, char zeroDigit, String nsName) {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < 10; ++i2) {
            buf.append((char)(zeroDigit + i2));
        }
        this.initialize(loc, buf.toString(), nsName);
    }

    private void initialize(ULocale loc, String digitString, String nsName) {
        char[] elems = CACHE.get(loc);
        if (elems == null) {
            String minusString;
            ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", loc);
            try {
                minusString = rb.getStringWithFallback("NumberElements/" + nsName + "/symbols/minusSign");
            }
            catch (MissingResourceException ex) {
                if (!nsName.equals("latn")) {
                    try {
                        minusString = rb.getStringWithFallback("NumberElements/latn/symbols/minusSign");
                    }
                    catch (MissingResourceException ex1) {
                        minusString = "-";
                    }
                }
                minusString = "-";
            }
            elems = new char[11];
            for (int i2 = 0; i2 < 10; ++i2) {
                elems[i2] = digitString.charAt(i2);
            }
            elems[10] = minusString.charAt(0);
            CACHE.put(loc, elems);
        }
        this.digits = new char[10];
        System.arraycopy(elems, 0, this.digits, 0, 10);
        this.zeroDigit = this.digits[0];
        this.minusSign = elems[10];
    }

    @Override
    public void setMaximumIntegerDigits(int newValue) {
        this.maxIntDigits = newValue;
    }

    @Override
    public int getMaximumIntegerDigits() {
        return this.maxIntDigits;
    }

    @Override
    public void setMinimumIntegerDigits(int newValue) {
        this.minIntDigits = newValue;
    }

    @Override
    public int getMinimumIntegerDigits() {
        return this.minIntDigits;
    }

    public void setParsePositiveOnly(boolean isPositiveOnly) {
        this.positiveOnly = isPositiveOnly;
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public void setZeroDigit(char zero) {
        this.zeroDigit = zero;
        if (this.digits == null) {
            this.digits = new char[10];
        }
        this.digits[0] = zero;
        for (int i2 = 1; i2 < 10; ++i2) {
            this.digits[i2] = (char)(zero + i2);
        }
    }

    public char[] getDigits() {
        return (char[])this.digits.clone();
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(double, StringBuffer, FieldPostion) is not implemented");
    }

    @Override
    public StringBuffer format(long numberL, StringBuffer toAppendTo, FieldPosition pos) {
        if (numberL < 0L) {
            toAppendTo.append(this.minusSign);
            numberL = -numberL;
        }
        int number = (int)numberL;
        int limit = this.decimalBuf.length < this.maxIntDigits ? this.decimalBuf.length : this.maxIntDigits;
        int index = limit - 1;
        while (true) {
            this.decimalBuf[index] = this.digits[number % 10];
            if (index == 0 || (number /= 10) == 0) break;
            --index;
        }
        for (int padding = this.minIntDigits - (limit - index); padding > 0; --padding) {
            this.decimalBuf[--index] = this.digits[0];
        }
        int length = limit - index;
        toAppendTo.append(this.decimalBuf, index, length);
        pos.setBeginIndex(0);
        if (pos.getField() == 0) {
            pos.setEndIndex(length);
        } else {
            pos.setEndIndex(0);
        }
        return toAppendTo;
    }

    @Override
    public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(BigInteger, StringBuffer, FieldPostion) is not implemented");
    }

    @Override
    public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
    }

    @Override
    public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
    }

    @Override
    public Number parse(String text, ParsePosition parsePosition) {
        long num = 0L;
        boolean sawNumber = false;
        boolean negative = false;
        int base = parsePosition.getIndex();
        int offset = 0;
        while (base + offset < text.length()) {
            char ch = text.charAt(base + offset);
            if (offset == 0 && ch == this.minusSign) {
                if (this.positiveOnly) break;
                negative = true;
            } else {
                int digit = ch - this.digits[0];
                if (digit < 0 || 9 < digit) {
                    digit = UCharacter.digit(ch);
                }
                if (digit < 0 || 9 < digit) {
                    for (digit = 0; digit < 10 && ch != this.digits[digit]; ++digit) {
                    }
                }
                if (0 > digit || digit > 9 || num >= 0xCCCCCCCCCCCCCCBL) break;
                sawNumber = true;
                num = num * 10L + (long)digit;
            }
            ++offset;
        }
        Long result = null;
        if (sawNumber) {
            num = negative ? num * -1L : num;
            result = num;
            parsePosition.setIndex(base + offset);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !super.equals(obj) || !(obj instanceof DateNumberFormat)) {
            return false;
        }
        DateNumberFormat other = (DateNumberFormat)obj;
        return this.maxIntDigits == other.maxIntDigits && this.minIntDigits == other.minIntDigits && this.minusSign == other.minusSign && this.positiveOnly == other.positiveOnly && Arrays.equals(this.digits, other.digits);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.digits == null) {
            this.setZeroDigit(this.zeroDigit);
        }
        this.decimalBuf = new char[20];
    }

    @Override
    public Object clone() {
        DateNumberFormat dnfmt = (DateNumberFormat)super.clone();
        dnfmt.digits = (char[])this.digits.clone();
        dnfmt.decimalBuf = new char[20];
        return dnfmt;
    }
}

