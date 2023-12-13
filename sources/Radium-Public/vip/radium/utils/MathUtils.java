package vip.radium.utils;

import java.math.BigDecimal;
import java.math.MathContext;

public final class MathUtils {

    private MathUtils() {
    }

    public static double roundToDecimalPlace(double value, double inc) {
        final double halfOfInc = inc / 2.0D;
        final double floored = StrictMath.floor(value / inc) * inc;
        if (value >= floored + halfOfInc)
            return new BigDecimal(StrictMath.ceil(value / inc) * inc, MathContext.DECIMAL64).
                    stripTrailingZeros()
                    .doubleValue();
        else
            return new BigDecimal(floored, MathContext.DECIMAL64)
                    .stripTrailingZeros()
                    .doubleValue();
    }
}
