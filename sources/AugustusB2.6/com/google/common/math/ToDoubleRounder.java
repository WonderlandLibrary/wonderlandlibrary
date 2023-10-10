// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.RoundingMode;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class ToDoubleRounder<X extends Number>
{
    abstract double roundToDoubleArbitrarily(final X p0);
    
    abstract int sign(final X p0);
    
    abstract X toX(final double p0, final RoundingMode p1);
    
    abstract X minus(final X p0, final X p1);
    
    final double roundToDouble(final X x, final RoundingMode mode) {
        Preconditions.checkNotNull(x, (Object)"x");
        Preconditions.checkNotNull(mode, (Object)"mode");
        final double roundArbitrarily = this.roundToDoubleArbitrarily(x);
        if (Double.isInfinite(roundArbitrarily)) {
            switch (mode) {
                case DOWN:
                case HALF_EVEN:
                case HALF_DOWN:
                case HALF_UP: {
                    return Double.MAX_VALUE * this.sign(x);
                }
                case FLOOR: {
                    return (roundArbitrarily == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : Double.NEGATIVE_INFINITY;
                }
                case CEILING: {
                    return (roundArbitrarily == Double.POSITIVE_INFINITY) ? Double.POSITIVE_INFINITY : -1.7976931348623157E308;
                }
                case UP: {
                    return roundArbitrarily;
                }
                case UNNECESSARY: {
                    final String value = String.valueOf(x);
                    throw new ArithmeticException(new StringBuilder(44 + String.valueOf(value).length()).append(value).append(" cannot be represented precisely as a double").toString());
                }
            }
        }
        final X roundArbitrarilyAsX = this.toX(roundArbitrarily, RoundingMode.UNNECESSARY);
        final int cmpXToRoundArbitrarily = ((Comparable)x).compareTo(roundArbitrarilyAsX);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(cmpXToRoundArbitrarily == 0);
                return roundArbitrarily;
            }
            case FLOOR: {
                return (cmpXToRoundArbitrarily >= 0) ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
            }
            case CEILING: {
                return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
            }
            case DOWN: {
                if (this.sign(x) >= 0) {
                    return (cmpXToRoundArbitrarily >= 0) ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
                }
                return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
            }
            case UP: {
                if (this.sign(x) >= 0) {
                    return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
                }
                return (cmpXToRoundArbitrarily >= 0) ? roundArbitrarily : DoubleUtils.nextDown(roundArbitrarily);
            }
            case HALF_EVEN:
            case HALF_DOWN:
            case HALF_UP: {
                double roundFloorAsDouble;
                X roundFloor;
                double roundCeilingAsDouble;
                X roundCeiling;
                if (cmpXToRoundArbitrarily >= 0) {
                    roundFloorAsDouble = roundArbitrarily;
                    roundFloor = roundArbitrarilyAsX;
                    roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
                    if (roundCeilingAsDouble == Double.POSITIVE_INFINITY) {
                        return roundFloorAsDouble;
                    }
                    roundCeiling = this.toX(roundCeilingAsDouble, RoundingMode.CEILING);
                }
                else {
                    roundCeilingAsDouble = roundArbitrarily;
                    roundCeiling = roundArbitrarilyAsX;
                    roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
                    if (roundFloorAsDouble == Double.NEGATIVE_INFINITY) {
                        return roundCeilingAsDouble;
                    }
                    roundFloor = this.toX(roundFloorAsDouble, RoundingMode.FLOOR);
                }
                final X deltaToFloor = this.minus(x, roundFloor);
                final X deltaToCeiling = this.minus(roundCeiling, x);
                final int diff = ((Comparable)deltaToFloor).compareTo(deltaToCeiling);
                if (diff < 0) {
                    return roundFloorAsDouble;
                }
                if (diff > 0) {
                    return roundCeilingAsDouble;
                }
                switch (mode) {
                    case HALF_EVEN: {
                        return ((Double.doubleToRawLongBits(roundFloorAsDouble) & 0x1L) == 0x0L) ? roundFloorAsDouble : roundCeilingAsDouble;
                    }
                    case HALF_DOWN: {
                        return (this.sign(x) >= 0) ? roundFloorAsDouble : roundCeilingAsDouble;
                    }
                    case HALF_UP: {
                        return (this.sign(x) >= 0) ? roundCeilingAsDouble : roundFloorAsDouble;
                    }
                    default: {
                        throw new AssertionError((Object)"impossible");
                    }
                }
                break;
            }
            default: {
                throw new AssertionError((Object)"impossible");
            }
        }
    }
}
