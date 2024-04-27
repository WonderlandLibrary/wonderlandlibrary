/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

public class Precision {
    private static final double POSITIVE_ZERO = 0.0;
    private static final long EXPONENT_OFFSET = 1023L;
    private static final long NEGATIVE_ZERO_DOUBLE_BITS;
    private static final int NEGATIVE_ZERO_FLOAT_BITS;
    public static final double SAFE_MIN;
    private static final long POSITIVE_ZERO_DOUBLE_BITS;
    private static final long SGN_MASK = Long.MIN_VALUE;
    private static final int SGN_MASK_FLOAT;
    public static final double EPSILON;
    private static final int POSITIVE_ZERO_FLOAT_BITS;

    public static double representableDelta(double d, double d2) {
        return d + d2 - d;
    }

    private Precision() {
    }

    public static boolean equalsIncludingNaN(float f, float f2) {
        boolean bl;
        boolean bl2 = Float.isNaN(f);
        return bl2 | (bl = Float.isNaN(f2)) ? bl2 == bl : Precision.equals((float)f, (float)f2, (int)1);
    }

    public static boolean equals(float f, float f2) {
        return Precision.equals((float)f, (float)f2, (int)1);
    }

    public static DoubleEquivalence doubleEquivalenceOfEpsilon(double d) {
        if (!Double.isFinite(d) || d < 0.0) {
            throw new IllegalArgumentException("Invalid epsilon value: " + d);
        }
        return new DoubleEquivalence(d){
            private final double epsilon;
            final double val$eps;

            @Override
            public int compare(double d, double d2) {
                return Precision.compareTo(d, d2, this.epsilon);
            }
            {
                this.val$eps = d;
                this.epsilon = this.val$eps;
            }
        };
    }

    public static boolean equalsIncludingNaN(double d, double d2, double d3) {
        double d4 = d;
        return d2 == false || Math.abs(d2 - d) <= d3;
    }

    public static boolean equalsWithRelativeTolerance(double d, double d2, double d3) {
        double d4 = d;
        double d5 = d2;
        double d6 = Math.max(Math.abs(d), Math.abs(d2));
        double d7 = Math.abs((d - d2) / d6);
        return d7 <= d3;
    }

    public static int compareTo(double d, double d2, int n) {
        double d3 = d;
        double d4 = d2;
        if (n == 0) {
            return 0;
        }
        if (d < d2) {
            return -1;
        }
        return 1;
    }

    static {
        SGN_MASK_FLOAT = Integer.MIN_VALUE;
        POSITIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(0.0);
        NEGATIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(-0.0);
        POSITIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(0.0f);
        NEGATIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(-0.0f);
        EPSILON = Double.longBitsToDouble(4368491638549381120L);
        SAFE_MIN = Double.longBitsToDouble(0x10000000000000L);
    }

    public static boolean equals(double d, double d2) {
        return Precision.equals((double)d, (double)d2, (int)1);
    }

    public static int compareTo(double d, double d2, double d3) {
        double d4 = d;
        double d5 = d2;
        if (d3 <= 0) {
            return 0;
        }
        if (d < d2) {
            return -1;
        }
        if (d > d2) {
            return 1;
        }
        return Double.compare(d, d2);
    }

    public static boolean equals(float f, float f2, float f3) {
        float f4 = f;
        float f5 = f2;
        return true;
    }

    public static boolean equalsIncludingNaN(float f, float f2, float f3) {
        float f4 = f;
        float f5 = f2;
        return Math.abs(f2 - f) <= f3;
    }

    public static interface DoubleEquivalence {
        default public boolean eqZero(double d) {
            return this.eq(d, 0.0);
        }

        default public boolean eq(double d, double d2) {
            return this.compare(d, d2) == 0;
        }

        default public boolean lt(double d, double d2) {
            return this.compare(d, d2) < 0;
        }

        default public boolean gte(double d, double d2) {
            return this.compare(d, d2) >= 0;
        }

        public int compare(double var1, double var3);

        default public boolean lte(double d, double d2) {
            return this.compare(d, d2) <= 0;
        }

        default public double signum(double d) {
            return d == 0.0 || Double.isNaN(d) ? d : (this.eqZero(d) ? Math.copySign(0.0, d) : Math.copySign(1.0, d));
        }

        default public boolean gt(double d, double d2) {
            return this.compare(d, d2) > 0;
        }
    }
}

