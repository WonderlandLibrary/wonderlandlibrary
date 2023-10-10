/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.number.Scale;
import java.math.MathContext;
import java.math.RoundingMode;

public class RoundingUtils {
    public static final int SECTION_LOWER = 1;
    public static final int SECTION_MIDPOINT = 2;
    public static final int SECTION_UPPER = 3;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final int MAX_INT_FRAC_SIG = 999;
    private static final MathContext[] MATH_CONTEXT_BY_ROUNDING_MODE_UNLIMITED = new MathContext[RoundingMode.values().length];
    private static final MathContext[] MATH_CONTEXT_BY_ROUNDING_MODE_34_DIGITS = new MathContext[RoundingMode.values().length];
    public static final MathContext DEFAULT_MATH_CONTEXT_UNLIMITED;
    public static final MathContext DEFAULT_MATH_CONTEXT_34_DIGITS;

    public static boolean getRoundingDirection(boolean isEven, boolean isNegative, int section, int roundingMode, Object reference) {
        switch (roundingMode) {
            case 0: {
                return false;
            }
            case 1: {
                return true;
            }
            case 2: {
                return isNegative;
            }
            case 3: {
                return !isNegative;
            }
            case 4: {
                switch (section) {
                    case 2: {
                        return false;
                    }
                    case 1: {
                        return true;
                    }
                    case 3: {
                        return false;
                    }
                }
                break;
            }
            case 5: {
                switch (section) {
                    case 2: {
                        return true;
                    }
                    case 1: {
                        return true;
                    }
                    case 3: {
                        return false;
                    }
                }
                break;
            }
            case 6: {
                switch (section) {
                    case 2: {
                        return isEven;
                    }
                    case 1: {
                        return true;
                    }
                    case 3: {
                        return false;
                    }
                }
            }
        }
        throw new ArithmeticException("Rounding is required on " + reference.toString());
    }

    public static boolean roundsAtMidpoint(int roundingMode) {
        switch (roundingMode) {
            case 0: 
            case 1: 
            case 2: 
            case 3: {
                return false;
            }
        }
        return true;
    }

    public static MathContext getMathContextOrUnlimited(DecimalFormatProperties properties) {
        MathContext mathContext = properties.getMathContext();
        if (mathContext == null) {
            RoundingMode roundingMode = properties.getRoundingMode();
            if (roundingMode == null) {
                roundingMode = RoundingMode.HALF_EVEN;
            }
            mathContext = MATH_CONTEXT_BY_ROUNDING_MODE_UNLIMITED[roundingMode.ordinal()];
        }
        return mathContext;
    }

    public static MathContext getMathContextOr34Digits(DecimalFormatProperties properties) {
        MathContext mathContext = properties.getMathContext();
        if (mathContext == null) {
            RoundingMode roundingMode = properties.getRoundingMode();
            if (roundingMode == null) {
                roundingMode = RoundingMode.HALF_EVEN;
            }
            mathContext = MATH_CONTEXT_BY_ROUNDING_MODE_34_DIGITS[roundingMode.ordinal()];
        }
        return mathContext;
    }

    public static MathContext mathContextUnlimited(RoundingMode roundingMode) {
        return MATH_CONTEXT_BY_ROUNDING_MODE_UNLIMITED[roundingMode.ordinal()];
    }

    public static Scale scaleFromProperties(DecimalFormatProperties properties) {
        MathContext mc = RoundingUtils.getMathContextOr34Digits(properties);
        if (properties.getMagnitudeMultiplier() != 0) {
            return Scale.powerOfTen(properties.getMagnitudeMultiplier()).withMathContext(mc);
        }
        if (properties.getMultiplier() != null) {
            return Scale.byBigDecimal(properties.getMultiplier()).withMathContext(mc);
        }
        return null;
    }

    static {
        for (int i2 = 0; i2 < MATH_CONTEXT_BY_ROUNDING_MODE_34_DIGITS.length; ++i2) {
            RoundingUtils.MATH_CONTEXT_BY_ROUNDING_MODE_UNLIMITED[i2] = new MathContext(0, RoundingMode.valueOf(i2));
            RoundingUtils.MATH_CONTEXT_BY_ROUNDING_MODE_34_DIGITS[i2] = new MathContext(34);
        }
        DEFAULT_MATH_CONTEXT_UNLIMITED = MATH_CONTEXT_BY_ROUNDING_MODE_UNLIMITED[DEFAULT_ROUNDING_MODE.ordinal()];
        DEFAULT_MATH_CONTEXT_34_DIGITS = MATH_CONTEXT_BY_ROUNDING_MODE_34_DIGITS[DEFAULT_ROUNDING_MODE.ordinal()];
    }
}

