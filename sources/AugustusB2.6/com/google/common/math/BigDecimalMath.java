// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.math.RoundingMode;
import java.math.BigDecimal;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public class BigDecimalMath
{
    private BigDecimalMath() {
    }
    
    public static double roundToDouble(final BigDecimal x, final RoundingMode mode) {
        return BigDecimalToDoubleRounder.INSTANCE.roundToDouble(x, mode);
    }
    
    private static class BigDecimalToDoubleRounder extends ToDoubleRounder<BigDecimal>
    {
        static final BigDecimalToDoubleRounder INSTANCE;
        
        @Override
        double roundToDoubleArbitrarily(final BigDecimal bigDecimal) {
            return bigDecimal.doubleValue();
        }
        
        @Override
        int sign(final BigDecimal bigDecimal) {
            return bigDecimal.signum();
        }
        
        @Override
        BigDecimal toX(final double d, final RoundingMode mode) {
            return new BigDecimal(d);
        }
        
        @Override
        BigDecimal minus(final BigDecimal a, final BigDecimal b) {
            return a.subtract(b);
        }
        
        static {
            INSTANCE = new BigDecimalToDoubleRounder();
        }
    }
}
