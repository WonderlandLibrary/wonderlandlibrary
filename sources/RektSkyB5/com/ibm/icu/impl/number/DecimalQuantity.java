/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.text.PluralRules;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.FieldPosition;

public interface DecimalQuantity
extends PluralRules.IFixedDecimal {
    public void setIntegerLength(int var1, int var2);

    public void setFractionLength(int var1, int var2);

    public void roundToIncrement(BigDecimal var1, MathContext var2);

    public void roundToMagnitude(int var1, MathContext var2);

    public void roundToInfinity();

    public void multiplyBy(BigDecimal var1);

    public void negate();

    public void adjustMagnitude(int var1);

    public int getMagnitude() throws ArithmeticException;

    public boolean isZero();

    public boolean isNegative();

    public int signum();

    @Override
    public boolean isInfinite();

    @Override
    public boolean isNaN();

    public double toDouble();

    public BigDecimal toBigDecimal();

    public void setToBigDecimal(BigDecimal var1);

    public int maxRepresentableDigits();

    public StandardPlural getStandardPlural(PluralRules var1);

    public byte getDigit(int var1);

    public int getUpperDisplayMagnitude();

    public int getLowerDisplayMagnitude();

    public String toPlainString();

    public DecimalQuantity createCopy();

    public void copyFrom(DecimalQuantity var1);

    public long getPositionFingerprint();

    public void populateUFieldPosition(FieldPosition var1);
}

