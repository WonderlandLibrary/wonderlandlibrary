/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmOverloads
 *  kotlin.jvm.JvmStatic
 *  kotlin.ranges.RangesKt
 */
package net.ccbluex.liquidbounce.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.ranges.RangesKt;

public final class MathUtils {
    public static final double DEGREES_TO_RADIANS = Math.PI / 180;
    public static final double RADIANS_TO_DEGREES = 57.29577951308232;
    public static final MathUtils INSTANCE;

    @JvmStatic
    @JvmOverloads
    public static final Double[][] simplifyPoints(Double[][] doubleArray, double d, int n, int n2) {
        return MathUtils.simplifyPoints$default(doubleArray, d, n, n2, null, 16, null);
    }

    public final double radians(double d) {
        return d * Math.PI / (double)180;
    }

    private MathUtils() {
    }

    public final double toDegrees(double d) {
        return d * 57.29577951308232;
    }

    public final double distanceToSegmentSq(Double[] doubleArray, Double[] doubleArray2, Double[] doubleArray3) {
        double d = this.distanceSq(doubleArray2, doubleArray3);
        if (d == 0.0) {
            return this.distanceSq(doubleArray, doubleArray2);
        }
        return this.distanceSq(doubleArray, this.lerp(doubleArray2, doubleArray3, RangesKt.coerceAtLeast((double)RangesKt.coerceAtMost((double)(((doubleArray[0] - doubleArray2[0]) * (doubleArray3[0] - doubleArray2[0]) + (doubleArray[1] - doubleArray2[1]) * (doubleArray3[1] - doubleArray2[1])) / d), (double)1.0), (double)0.0)));
    }

    public final boolean inRange(float f, float f2, float f3) {
        float f4 = f2 - f3;
        float f5 = f2 + f3;
        float f6 = f;
        return f6 >= f4 && f6 <= f5;
    }

    public final boolean inRange(int n, int n2, int n3) {
        int n4 = n;
        return n2 - n3 <= n4 && n2 + n3 >= n4;
    }

    @JvmStatic
    public static final Double[][] getPointsOnCurve(Double[][] doubleArray, int n) {
        int n2 = 0;
        List list = new ArrayList();
        int n3 = n;
        for (n2 = 0; n2 < n3; ++n2) {
            double d = (double)n2 / ((double)n - 1.0);
            list.add(MathUtils.calcCurvePoint(doubleArray, d));
        }
        Collection collection = list;
        n3 = 0;
        Collection collection2 = collection;
        T[] TArray = collection2.toArray((T[])new Double[0][]);
        if (TArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (Double[][])TArray;
    }

    public final Double[] lerp(Double[] doubleArray, Double[] doubleArray2, double d) {
        return new Double[]{doubleArray[0] + (doubleArray2[0] - doubleArray[0]) * d, doubleArray[1] + (doubleArray2[1] - doubleArray[1]) * d};
    }

    @JvmStatic
    public static final Double[] calcCurvePoint(Double[][] doubleArray, double d) {
        Double[] doubleArray2;
        int n = 0;
        List list = new ArrayList();
        int n2 = ((Object[])doubleArray).length - 1;
        for (n = 0; n < n2; ++n) {
            list.add(INSTANCE.lerp(doubleArray[n], doubleArray[n + 1], d));
        }
        if (list.size() == 1) {
            doubleArray2 = (Double[])list.get(0);
        } else {
            Collection collection = list;
            n2 = 0;
            Collection collection2 = collection;
            T[] TArray = collection2.toArray((T[])new Double[0][]);
            if (TArray == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            doubleArray2 = MathUtils.calcCurvePoint((Double[][])TArray, d);
        }
        return doubleArray2;
    }

    @JvmStatic
    @JvmOverloads
    public static final Double[][] simplifyPoints(Double[][] doubleArray, double d, int n) {
        return MathUtils.simplifyPoints$default(doubleArray, d, n, 0, null, 24, null);
    }

    public final double distanceSq(Double[] doubleArray, Double[] doubleArray2) {
        double d = doubleArray[0] - doubleArray2[0];
        int n = 2;
        boolean bl = false;
        double d2 = Math.pow(d, n);
        d = doubleArray[1] - doubleArray2[1];
        n = 2;
        double d3 = d2;
        bl = false;
        double d4 = Math.pow(d, n);
        return d3 + d4;
    }

    static {
        MathUtils mathUtils;
        INSTANCE = mathUtils = new MathUtils();
    }

    @JvmStatic
    public static final double round(double d, double d2) {
        double d3;
        if (d2 == 0.0) {
            d3 = d;
        } else if (d2 == 1.0) {
            d3 = Math.round(d);
        } else {
            double d4 = d2 / 2.0;
            double d5 = Math.floor(d / d2) * d2;
            d3 = d >= d5 + d4 ? new BigDecimal(Math.ceil(d / d2) * d2).doubleValue() : new BigDecimal(d5).doubleValue();
        }
        return d3;
    }

    @JvmStatic
    public static final double roundToPlace(double d, int n) {
        boolean bl = n >= 0;
        boolean bl2 = false;
        boolean bl3 = false;
        bl3 = false;
        boolean bl4 = false;
        if (!bl) {
            boolean bl5 = false;
            String string = "Failed requirement.";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public final double toRadians(double d) {
        return d * (Math.PI / 180);
    }

    @JvmStatic
    @JvmOverloads
    public static final Double[][] simplifyPoints(Double[][] doubleArray, double d) {
        return MathUtils.simplifyPoints$default(doubleArray, d, 0, 0, null, 28, null);
    }

    @JvmStatic
    @JvmOverloads
    public static final Double[][] simplifyPoints(Double[][] doubleArray, double d, int n, int n2, List list) {
        Double[] doubleArray2 = doubleArray[n];
        Double[] doubleArray3 = doubleArray[n2 - 1];
        double d2 = 0.0;
        int n3 = 1;
        int n4 = n2 - 1;
        for (int i = n + 1; i < n4; ++i) {
            double d3 = INSTANCE.distanceToSegmentSq(doubleArray[i], doubleArray2, doubleArray3);
            if (!(d3 > d2)) continue;
            d2 = d3;
            n3 = i;
        }
        if (Math.sqrt(d2) > d) {
            MathUtils.simplifyPoints(doubleArray, d, n, n3 + 1, list);
            MathUtils.simplifyPoints(doubleArray, d, n3, n2, list);
        } else {
            list.add(doubleArray2);
            list.add(doubleArray3);
        }
        Collection collection = list;
        n4 = 0;
        Collection collection2 = collection;
        T[] TArray = collection2.toArray((T[])new Double[0][]);
        if (TArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (Double[][])TArray;
    }

    public final boolean inRange(double d, double d2, double d3) {
        double d4 = d2 - d3;
        double d5 = d2 + d3;
        double d6 = d;
        return d6 >= d4 && d6 <= d5;
    }

    public final double getDistance(double d, double d2, double d3, double d4) {
        double d5 = d - d3;
        double d6 = 2;
        boolean bl = false;
        double d7 = Math.pow(d5, d6);
        d5 = d2 - d4;
        d6 = 2;
        double d8 = d7;
        bl = false;
        double d9 = Math.pow(d5, d6);
        d5 = d8 + d9;
        boolean bl2 = false;
        return Math.sqrt(d5);
    }

    public static Double[][] simplifyPoints$default(Double[][] doubleArray, double d, int n, int n2, List list, int n3, Object object) {
        if ((n3 & 4) != 0) {
            n = 0;
        }
        if ((n3 & 8) != 0) {
            n2 = ((Object[])doubleArray).length;
        }
        if ((n3 & 0x10) != 0) {
            boolean bl = false;
            list = new ArrayList();
        }
        return MathUtils.simplifyPoints(doubleArray, d, n, n2, list);
    }

    @JvmStatic
    public static final double roundToHalf(double d) {
        return (double)Math.round(d * (double)2) / 2.0;
    }

    public final float gaussian(int n, float f) {
        float f2 = f * f * (float)2;
        float f3 = (float)Math.PI * f2;
        float f4 = 1.0f;
        boolean bl = false;
        float f5 = (float)Math.sqrt(f3);
        f3 = (float)(-(n * n)) / f2;
        f4 /= f5;
        bl = false;
        f5 = (float)Math.exp(f3);
        return f4 * f5;
    }
}

