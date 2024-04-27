/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.ccbluex.liquidbounce.utils.FastMathLiteralArrays
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.FastMathLiteralArrays;
import net.ccbluex.liquidbounce.utils.Precision;

public class ApacheMath {
    private static final double F_11_12 = 0.9166666666666666;
    private static final boolean RECOMPUTE_TABLES_AT_RUNTIME = false;
    private static final double LN_2_A = 0.6931470632553101;
    private static final double F_7_8 = 0.875;
    private static final long[] RECIP_2PI;
    private static final double F_1_9 = 0.1111111111111111;
    private static final double[] TANGENT_TABLE_B;
    private static final double F_1_4 = 0.25;
    private static final int MASK_NON_SIGN_INT;
    private static final double[] TANGENT_TABLE_A;
    private static final double F_1_7 = 0.14285714285714285;
    private static final long IMPLICIT_HIGH_BIT = 0x10000000000000L;
    private static final double F_3_4 = 0.75;
    private static final long[] PI_O_4_BITS;
    private static final double[][] LN_QUICK_COEF;
    private static final long MASK_30BITS = -1073741824L;
    private static final double[] SINE_TABLE_B;
    private static final double F_9_10 = 0.9;
    private static final double[] CBRTTWO;
    static final int EXP_FRAC_TABLE_LEN;
    public static final double E = Math.E;
    private static final int SINE_TABLE_LEN;
    private static final double F_1_3 = 0.3333333333333333;
    private static final double F_15_16 = 0.9375;
    static final int EXP_INT_TABLE_LEN;
    private static final double F_5_6 = 0.8333333333333334;
    private static final double F_1_15 = 0.06666666666666667;
    public static final double PI = Math.PI;
    private static final double LOG_MAX_VALUE;
    private static final double TWO_POWER_52 = 4.503599627370496E15;
    private static final double[] EIGHTHS;
    private static final double[] SINE_TABLE_A;
    private static final double F_1_17 = 0.058823529411764705;
    private static final long HEX_40000000 = 0x40000000L;
    private static final double F_1_5 = 0.2;
    private static final long MASK_DOUBLE_EXPONENT = 0x7FF0000000000000L;
    private static final double LN_2_B = 1.1730463525082348E-7;
    private static final double[] COSINE_TABLE_B;
    static final int EXP_INT_TABLE_MAX_INDEX;
    private static final double F_13_14 = 0.9285714285714286;
    private static final double F_1_11 = 0.09090909090909091;
    static final int LN_MANT_LEN;
    private static final long MASK_NON_SIGN_LONG = Long.MAX_VALUE;
    private static final long MASK_DOUBLE_MANTISSA = 0xFFFFFFFFFFFFFL;
    private static final double F_1_13 = 0.07692307692307693;
    private static final double[][] LN_HI_PREC_COEF;
    private static final double[] COSINE_TABLE_A;
    private static final double F_1_2 = 0.5;

    public static int min(int n, int n2) {
        return n <= n2 ? n : n2;
    }

    public static double atan2(double d, double d2) {
        double d3;
        if (Double.isNaN(d2) || Double.isNaN(d)) {
            return Double.NaN;
        }
        if (d == 0.0) {
            double d4 = d2 * d;
            double d5 = 1.0 / d2;
            double d6 = 1.0 / d;
            if (d5 == 0.0) {
                if (d2 > 0.0) {
                    return d;
                }
                return ApacheMath.copySign(Math.PI, d);
            }
            if (d2 < 0.0 || d5 < 0.0) {
                if (d < 0.0 || d6 < 0.0) {
                    return -Math.PI;
                }
                return Math.PI;
            }
            return d4;
        }
        if (d == Double.POSITIVE_INFINITY) {
            if (d2 == Double.POSITIVE_INFINITY) {
                return 0.7853981633974483;
            }
            if (d2 == Double.NEGATIVE_INFINITY) {
                return 2.356194490192345;
            }
            return 1.5707963267948966;
        }
        if (d == Double.NEGATIVE_INFINITY) {
            if (d2 == Double.POSITIVE_INFINITY) {
                return -0.7853981633974483;
            }
            if (d2 == Double.NEGATIVE_INFINITY) {
                return -2.356194490192345;
            }
            return -1.5707963267948966;
        }
        if (d2 == Double.POSITIVE_INFINITY) {
            if (d > 0.0 || 1.0 / d > 0.0) {
                return 0.0;
            }
            if (d < 0.0 || 1.0 / d < 0.0) {
                return -0.0;
            }
        }
        if (d2 == Double.NEGATIVE_INFINITY) {
            if (d > 0.0 || 1.0 / d > 0.0) {
                return Math.PI;
            }
            if (d < 0.0 || 1.0 / d < 0.0) {
                return -Math.PI;
            }
        }
        if (d2 == 0.0) {
            if (d > 0.0 || 1.0 / d > 0.0) {
                return 1.5707963267948966;
            }
            if (d < 0.0 || 1.0 / d < 0.0) {
                return -1.5707963267948966;
            }
        }
        if (Double.isInfinite(d3 = d / d2)) {
            return ApacheMath.atan(d3, 0.0, d2 < 0.0);
        }
        double d7 = ApacheMath.doubleHighPart(d3);
        double d8 = d3 - d7;
        double d9 = ApacheMath.doubleHighPart(d2);
        double d10 = d2 - d9;
        d8 += (d - d7 * d9 - d7 * d10 - d8 * d9 - d8 * d10) / d2;
        double d11 = d7 + d8;
        d8 = -(d11 - d7 - d8);
        d7 = d11;
        if (d7 == 0.0) {
            d7 = ApacheMath.copySign(0.0, d);
        }
        return ApacheMath.atan(d7, d8, d2 < 0.0);
    }

    public static double copySign(double d, double d2) {
        long l;
        long l2 = Double.doubleToRawLongBits(d);
        if ((l2 ^ (l = Double.doubleToRawLongBits(d2))) >= 0L) {
            return d;
        }
        return -d;
    }

    public static long min(long l, long l2) {
        return l <= l2 ? l : l2;
    }

    private static double polyCosine(double d) {
        double d2 = d * d;
        double d3 = 2.479773539153719E-5;
        d3 = d3 * d2 + -0.0013888888689039883;
        d3 = d3 * d2 + 0.041666666666621166;
        d3 = d3 * d2 + -0.49999999999999994;
        return d3 *= d2;
    }

    public static double asin(double d) {
        if (Double.isNaN(d)) {
            return Double.NaN;
        }
        if (d > 1.0 || d < -1.0) {
            return Double.NaN;
        }
        if (d == 1.0) {
            return 1.5707963267948966;
        }
        if (d == -1.0) {
            return -1.5707963267948966;
        }
        if (d == 0.0) {
            return d;
        }
        double d2 = d * 1.073741824E9;
        double d3 = d + d2 - d2;
        double d4 = d - d3;
        double d5 = d3 * d3;
        double d6 = d3 * d4 * 2.0 + d4 * d4;
        d5 = -d5;
        d6 = -d6;
        double d7 = 1.0 + d5;
        double d8 = -(d7 - 1.0 - d5);
        d2 = d7 + d6;
        d8 += -(d2 - d7 - d6);
        d7 = d2;
        double d9 = ApacheMath.sqrt(d7);
        d2 = d9 * 1.073741824E9;
        d5 = d9 + d2 - d2;
        d6 = d9 - d5;
        d6 += (d7 - d5 * d5 - 2.0 * d5 * d6 - d6 * d6) / (2.0 * d9);
        double d10 = d8 / (2.0 * d9);
        double d11 = d / d9;
        d2 = d11 * 1.073741824E9;
        double d12 = d11 + d2 - d2;
        double d13 = d11 - d12;
        d13 += (d - d12 * d5 - d12 * d6 - d13 * d5 - d13 * d6) / d9;
        d2 = d12 + (d13 += -d * d10 / d9 / d9);
        d13 = -(d2 - d12 - d13);
        d12 = d2;
        return ApacheMath.atan(d12, d13, false);
    }

    public static int max(int n, int n2) {
        return n <= n2 ? n2 : n;
    }

    private ApacheMath() {
    }

    public static double hypot(double d, double d2) {
        int n;
        if (Double.isInfinite(d) || Double.isInfinite(d2)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(d) || Double.isNaN(d2)) {
            return Double.NaN;
        }
        int n2 = ApacheMath.getExponent(d);
        if (n2 > (n = ApacheMath.getExponent(d2)) + 27) {
            return ApacheMath.abs(d);
        }
        if (n > n2 + 27) {
            return ApacheMath.abs(d2);
        }
        int n3 = (n2 + n) / 2;
        double d3 = ApacheMath.scalb(d, -n3);
        double d4 = ApacheMath.scalb(d2, -n3);
        double d5 = ApacheMath.sqrt(d3 * d3 + d4 * d4);
        return ApacheMath.scalb(d5, n3);
    }

    public static double atan(double d) {
        return ApacheMath.atan(d, 0.0, false);
    }

    private static double expm1(double d, double[] dArray) {
        if (Double.isNaN(d) || d == 0.0) {
            return d;
        }
        if (d <= -1.0 || d >= 1.0) {
            double[] dArray2 = new double[2];
            ApacheMath.exp(d, 0.0, dArray2);
            if (d > 0.0) {
                return -1.0 + dArray2[0] + dArray2[1];
            }
            double d2 = -1.0 + dArray2[0];
            double d3 = -(d2 + 1.0 - dArray2[0]);
            return d2 + (d3 += dArray2[1]);
        }
        boolean bl = false;
        if (d < 0.0) {
            d = -d;
            bl = true;
        }
        int n = (int)(d * 1024.0);
        double d4 = ExpFracTable.access$200()[n] - 1.0;
        double d5 = ExpFracTable.access$300()[n];
        double d6 = d4 + d5;
        d5 = -(d6 - d4 - d5);
        d4 = d6;
        d6 = d4 * 1.073741824E9;
        double d7 = d4 + d6 - d6;
        double d8 = d5 + (d4 - d7);
        double d9 = d - (double)n / 1024.0;
        double d10 = 0.008336750013465571;
        d10 = d10 * d9 + 0.041666663879186654;
        d10 = d10 * d9 + 0.16666666666745392;
        d10 = d10 * d9 + 0.49999999999999994;
        d10 *= d9;
        double d11 = d9;
        double d12 = d11 + (d10 *= d9);
        d10 = -(d12 - d11 - d10);
        d11 = d12;
        d12 = d11 * 1.073741824E9;
        d12 = d11 + d12 - d12;
        d10 += d11 - d12;
        d11 = d12;
        double d13 = d11 * d7;
        d12 = d13 + d11 * d8;
        double d14 = -(d12 - d13 - d11 * d8);
        d13 = d12;
        d12 = d13 + d10 * d7;
        d14 += -(d12 - d13 - d10 * d7);
        d13 = d12;
        d12 = d13 + d10 * d8;
        d14 += -(d12 - d13 - d10 * d8);
        d13 = d12;
        d12 = d13 + d7;
        d14 += -(d12 - d7 - d13);
        d13 = d12;
        d12 = d13 + d11;
        d14 += -(d12 - d13 - d11);
        d13 = d12;
        d12 = d13 + d8;
        d14 += -(d12 - d13 - d8);
        d13 = d12;
        d12 = d13 + d10;
        d14 += -(d12 - d13 - d10);
        d13 = d12;
        if (bl) {
            double d15 = 1.0 + d13;
            double d16 = 1.0 / d15;
            double d17 = -(d15 - 1.0 - d13) + d14;
            double d18 = d13 * d16;
            d12 = d18 * 1.073741824E9;
            double d19 = d18 + d12 - d12;
            double d20 = d18 - d19;
            d12 = d15 * 1.073741824E9;
            d11 = d15 + d12 - d12;
            d10 = d15 - d11;
            d20 += (d13 - d11 * d19 - d11 * d20 - d10 * d19 - d10 * d20) * d16;
            d20 += d14 * d16;
            d20 += -d13 * d17 * d16 * d16;
            d13 = -d19;
            d14 = -d20;
        }
        if (dArray != null) {
            dArray[0] = d13;
            dArray[1] = d14;
        }
        return d13 + d14;
    }

    public static double log10(double d) {
        double[] dArray = new double[2];
        double d2 = ApacheMath.log(d, dArray);
        if (Double.isInfinite(d2)) {
            return d2;
        }
        double d3 = dArray[0] * 1.073741824E9;
        double d4 = dArray[0] + d3 - d3;
        double d5 = dArray[0] - d4 + dArray[1];
        double d6 = 0.4342944622039795;
        double d7 = 1.9699272335463627E-8;
        return 1.9699272335463627E-8 * d5 + 1.9699272335463627E-8 * d4 + 0.4342944622039795 * d5 + 0.4342944622039795 * d4;
    }

    public static int abs(int n) {
        int n2 = n >>> 31;
        return (n ^ ~n2 + 1) + n2;
    }

    public static double ceil(double d) {
        if (Double.isNaN(d)) {
            return d;
        }
        double d2 = ApacheMath.floor(d);
        if (d2 == d) {
            return d2;
        }
        if ((d2 += 1.0) == 0.0) {
            return d * d2;
        }
        return d2;
    }

    public static float nextUp(float f) {
        return ApacheMath.nextAfter(f, Double.POSITIVE_INFINITY);
    }

    public static double toDegrees(double d) {
        if (Double.isInfinite(d) || d == 0.0) {
            return d;
        }
        double d2 = 57.2957763671875;
        double d3 = 3.145894820876798E-6;
        double d4 = ApacheMath.doubleHighPart(d);
        double d5 = d - d4;
        return d5 * 3.145894820876798E-6 + d5 * 57.2957763671875 + d4 * 3.145894820876798E-6 + d4 * 57.2957763671875;
    }

    public static double nextDown(double d) {
        return ApacheMath.nextAfter(d, Double.NEGATIVE_INFINITY);
    }

    public static double tan(double d) {
        Object object;
        boolean bl = false;
        int n = 0;
        double d2 = d;
        if (d < 0.0) {
            bl = true;
            d2 = -d2;
        }
        if (d2 == 0.0) {
            long l = Double.doubleToRawLongBits(d);
            if (l < 0L) {
                return -0.0;
            }
            return 0.0;
        }
        if (d2 != d2 || d2 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double d3 = 0.0;
        if (d2 > 3294198.0) {
            object = new double[3];
            ApacheMath.reducePayneHanek(d2, (double[])object);
            n = (int)object[0] & 3;
            d2 = (double)object[1];
            d3 = (double)object[2];
        } else if (d2 > 1.5707963267948966) {
            object = new CodyWaite(d2);
            n = ((CodyWaite)object).getK() & 3;
            d2 = ((CodyWaite)object).getRemA();
            d3 = ((CodyWaite)object).getRemB();
        }
        if (d2 > 1.5) {
            double d4 = 1.5707963267948966;
            double d5 = 6.123233995736766E-17;
            double d6 = 1.5707963267948966 - d2;
            double d7 = -(d6 - 1.5707963267948966 + d2);
            d2 = d6 + (d7 += 6.123233995736766E-17 - d3);
            d3 = -(d2 - d6 - d7);
            n ^= 1;
            bl ^= true;
        }
        double d8 = !(n & true) ? ApacheMath.tanQ(d2, d3, false) : -ApacheMath.tanQ(d2, d3, true);
        if (bl) {
            d8 = -d8;
        }
        return d8;
    }

    public static double floor(double d) {
        if (Double.isNaN(d)) {
            return d;
        }
        if (d >= 4.503599627370496E15 || d <= -4.503599627370496E15) {
            return d;
        }
        long l = (long)d;
        if (d < 0.0 && (double)l != d) {
            --l;
        }
        if (l == 0L) {
            return d * (double)l;
        }
        return l;
    }

    private static double polySine(double d) {
        double d2 = d * d;
        double d3 = 2.7553817452272217E-6;
        d3 = d3 * d2 + -1.9841269659586505E-4;
        d3 = d3 * d2 + 0.008333333333329196;
        d3 = d3 * d2 + -0.16666666666666666;
        d3 = d3 * d2 * d;
        return d3;
    }

    private static double cosQ(double d, double d2) {
        double d3 = 1.5707963267948966;
        double d4 = 6.123233995736766E-17;
        double d5 = 1.5707963267948966 - d;
        double d6 = -(d5 - 1.5707963267948966 + d);
        return ApacheMath.sinQ(d5, d6 += 6.123233995736766E-17 - d2);
    }

    static {
        EXP_INT_TABLE_MAX_INDEX = 750;
        MASK_NON_SIGN_INT = Integer.MAX_VALUE;
        SINE_TABLE_LEN = 14;
        EXP_INT_TABLE_LEN = 1500;
        LN_MANT_LEN = 1024;
        EXP_FRAC_TABLE_LEN = 1025;
        LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);
        LN_QUICK_COEF = new double[][]{{1.0, 5.669184079525E-24}, {-0.25, -0.25}, {0.3333333134651184, 1.986821492305628E-8}, {-0.25, -6.663542893624021E-14}, {0.19999998807907104, 1.1921056801463227E-8}, {-0.1666666567325592, -7.800414592973399E-9}, {0.1428571343421936, 5.650007086920087E-9}, {-0.1250253f, -7.44321345601866E-11}, {0.11113807559013367, 9.219544613762692E-9}};
        LN_HI_PREC_COEF = new double[][]{{1.0, -6.032174644509064E-23}, {-0.25, -0.25}, {0.3333333134651184, 1.9868161777724352E-8}, {-0.2499999701976776, -2.957007209750105E-8}, {0.19999954104423523, 1.5830993332061267E-10}, {-0.1662488f, -2.6033824355191673E-8}};
        SINE_TABLE_A = new double[]{0.0, 0.1246747374534607, 0.24740394949913025, 0.366272509098053, 0.4794255495071411, 0.5850973129272461, 0.6816387176513672, 0.7675435543060303, 0.8414709568023682, 0.902267575263977, 0.9489846229553223, 0.980893f, 0.9974949359893799, 0.9985313415527344};
        SINE_TABLE_B = new double[]{0.0, -4.068233003401932E-9, 9.755392680573412E-9, 1.9987994582857286E-8, -1.0902938113007961E-8, -3.9986783938944604E-8, 4.23719669792332E-8, -5.207000323380292E-8, 2.800552834259E-8, 1.883511811213715E-8, -3.5997360512765566E-9, 4.116164446561962E-8, 5.0614674548127384E-8, -1.0129027912496858E-9};
        COSINE_TABLE_A = new double[]{1.0, 0.9921976327896118, 0.9689123630523682, 0.9305076599121094, 0.8775825500488281, 0.8109631538391113, 0.7316888570785522, 0.6409968137741089, 0.5403022766113281, 0.4311765432357788, 0.3153223395347595, 0.19454771280288696, 0.0707372f, -0.05417713522911072};
        COSINE_TABLE_B = new double[]{0.0, 3.4439717236742845E-8, 5.865827662008209E-8, -3.7999795083850525E-8, 1.184154459111628E-8, -3.43338934259355E-8, 1.1795268640216787E-8, 4.438921624363781E-8, 2.925681159240093E-8, -2.6437112632041807E-8, 2.2860509143963117E-8, -4.813899778443457E-9, 3.6725170580355583E-9, 2.0217439756338078E-10};
        TANGENT_TABLE_A = new double[]{0.0, 0.1256551444530487, 0.25534194707870483, 0.3936265707015991, 0.5463024377822876, 0.7214844226837158, 0.9315965175628662, 1.1974215507507324, 1.5574076175689697, 2.092571258544922, 3.0095696449279785, 5.041914939880371, 14.101419448852539, -18.430862426757812};
        TANGENT_TABLE_B = new double[]{0.0, -7.877917738262007E-9, -2.5857668567479893E-8, 5.2240336371356666E-9, 5.206150291559893E-8, 1.8307188599677033E-8, -5.7618793749770706E-8, 7.848361555046424E-8, 1.0708593250394448E-7, 1.7827257129423813E-8, 2.893485277253286E-8, 3.1660099222737955E-7, 4.983191803254889E-7, -3.356118100840571E-7};
        RECIP_2PI = new long[]{2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L};
        PI_O_4_BITS = new long[]{-3958705157555305932L, -4267615245585081135L};
        EIGHTHS = new double[]{0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625};
        CBRTTWO = new double[]{0.6299605249474366, 0.7937005259840998, 1.0, 1.2599210498948732, 1.5874010519681994};
    }

    public static float nextDown(float f) {
        return ApacheMath.nextAfter(f, Double.NEGATIVE_INFINITY);
    }

    public static double sin(double d) {
        boolean bl = false;
        int n = 0;
        double d2 = 0.0;
        double d3 = d;
        if (d < 0.0) {
            bl = true;
            d3 = -d3;
        }
        if (d3 == 0.0) {
            long l = Double.doubleToRawLongBits(d);
            if (l < 0L) {
                return -0.0;
            }
            return 0.0;
        }
        if (d3 != d3 || d3 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        if (d3 > 3294198.0) {
            double[] dArray = new double[3];
            ApacheMath.reducePayneHanek(d3, dArray);
            n = (int)dArray[0] & 3;
            d3 = dArray[1];
            d2 = dArray[2];
        } else if (d3 > 1.5707963267948966) {
            CodyWaite codyWaite = new CodyWaite(d3);
            n = codyWaite.getK() & 3;
            d3 = codyWaite.getRemA();
            d2 = codyWaite.getRemB();
        }
        if (bl) {
            n ^= 2;
        }
        switch (n) {
            case 0: {
                return ApacheMath.sinQ(d3, d2);
            }
            case 1: {
                return ApacheMath.cosQ(d3, d2);
            }
            case 2: {
                return -ApacheMath.sinQ(d3, d2);
            }
            case 3: {
                return -ApacheMath.cosQ(d3, d2);
            }
        }
        return Double.NaN;
    }

    public static float min(float f, float f2) {
        if (f > f2) {
            return f2;
        }
        if (f < f2) {
            return f;
        }
        if (f != f2) {
            return Float.NaN;
        }
        int n = Float.floatToRawIntBits(f);
        if (n == Integer.MIN_VALUE) {
            return f;
        }
        return f2;
    }

    private static double tanQ(double d, double d2, boolean bl) {
        double d3;
        int n = (int)(d * 8.0 + 0.5);
        double d4 = d - EIGHTHS[n];
        double d5 = SINE_TABLE_A[n];
        double d6 = SINE_TABLE_B[n];
        double d7 = COSINE_TABLE_A[n];
        double d8 = COSINE_TABLE_B[n];
        double d9 = d4;
        double d10 = ApacheMath.polySine(d4);
        double d11 = 1.0;
        double d12 = ApacheMath.polyCosine(d4);
        double d13 = d9 * 1.073741824E9;
        double d14 = d9 + d13 - d13;
        d10 += d9 - d14;
        d9 = d14;
        double d15 = 0.0;
        double d16 = 0.0;
        double d17 = d5;
        double d18 = d15 + d17;
        double d19 = -(d18 - d15 - d17);
        d15 = d18;
        d16 += d19;
        d17 = d7 * d9;
        d18 = d15 + d17;
        d19 = -(d18 - d15 - d17);
        d15 = d18;
        d16 += d19;
        d16 += d5 * d12 + d7 * d10;
        double d20 = d15 + (d16 += d6 + d8 * d9 + d6 * d12 + d8 * d10);
        double d21 = -(d20 - d15 - d16);
        d19 = 0.0;
        d18 = 0.0;
        d16 = 0.0;
        d15 = 0.0;
        d17 = d7 * 1.0;
        d18 = d15 + d17;
        d19 = -(d18 - d15 - d17);
        d15 = d18;
        d16 += d19;
        d17 = -d5 * d9;
        d18 = d15 + d17;
        d19 = -(d18 - d15 - d17);
        d15 = d18;
        d16 += d19;
        d16 += d8 * 1.0 + d7 * d12 + d8 * d12;
        double d22 = d15 + (d16 -= d6 * d9 + d5 * d10 + d6 * d10);
        double d23 = -(d22 - d15 - d16);
        if (bl) {
            d3 = d22;
            d22 = d20;
            d20 = d3;
            d3 = d23;
            d23 = d21;
            d21 = d3;
        }
        d3 = d20 / d22;
        d13 = d3 * 1.073741824E9;
        double d24 = d3 + d13 - d13;
        double d25 = d3 - d24;
        d13 = d22 * 1.073741824E9;
        double d26 = d22 + d13 - d13;
        double d27 = d22 - d26;
        double d28 = (d20 - d24 * d26 - d24 * d27 - d25 * d26 - d25 * d27) / d22;
        d28 += d21 / d22;
        d28 += -d20 * d23 / d22 / d22;
        if (d2 != 0.0) {
            double d29 = d2 + d3 * d3 * d2;
            if (bl) {
                d29 = -d29;
            }
            d28 += d29;
        }
        return d3 + d28;
    }

    public static double signum(double d) {
        return d < 0.0 ? -1.0 : (d > 0.0 ? 1.0 : d);
    }

    private static void reducePayneHanek(double d, double[] dArray) {
        boolean bl;
        long l;
        long l2;
        long l3;
        int n;
        int n2;
        long l4 = Double.doubleToRawLongBits(d);
        int n3 = (int)(l4 >> 52 & 0x7FFL) - 1023;
        l4 &= 0xFFFFFFFFFFFFFL;
        l4 |= 0x10000000000000L;
        l4 <<= 11;
        if ((n2 = ++n3 - ((n = n3 >> 6) << 6)) != 0) {
            l3 = n == 0 ? 0L : RECIP_2PI[n - 1] << n2;
            l3 |= RECIP_2PI[n] >>> 64 - n2;
            l2 = RECIP_2PI[n] << n2 | RECIP_2PI[n + 1] >>> 64 - n2;
            l = RECIP_2PI[n + 1] << n2 | RECIP_2PI[n + 2] >>> 64 - n2;
        } else {
            l3 = n == 0 ? 0L : RECIP_2PI[n - 1];
            l2 = RECIP_2PI[n];
            l = RECIP_2PI[n + 1];
        }
        long l5 = l4 >>> 32;
        long l6 = l4 & 0xFFFFFFFFL;
        long l7 = l2 >>> 32;
        long l8 = l2 & 0xFFFFFFFFL;
        long l9 = l5 * l7;
        long l10 = l6 * l8;
        long l11 = l6 * l7;
        long l12 = l5 * l8;
        long l13 = l10 + (l12 << 32);
        long l14 = l9 + (l12 >>> 32);
        boolean bl2 = (l10 & Long.MIN_VALUE) != 0L;
        boolean bl3 = (l12 & 0x80000000L) != 0L;
        boolean bl4 = bl = (l13 & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l14;
        }
        bl2 = (l13 & Long.MIN_VALUE) != 0L;
        bl3 = (l11 & 0x80000000L) != 0L;
        l14 += l11 >>> 32;
        boolean bl5 = bl = ((l13 += l11 << 32) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l14;
        }
        l7 = l >>> 32;
        l8 = l & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l11 = l6 * l7;
        l12 = l5 * l8;
        bl2 = (l13 & Long.MIN_VALUE) != 0L;
        bl3 = ((l9 += l11 + l12 >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl6 = bl = ((l13 += l9) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l14;
        }
        l7 = l3 >>> 32;
        l8 = l3 & 0xFFFFFFFFL;
        l10 = l6 * l8;
        l11 = l6 * l7;
        l12 = l5 * l8;
        int n4 = (int)((l14 += l10 + (l11 + l12 << 32)) >>> 62);
        l14 <<= 2;
        l14 |= l13 >>> 62;
        l13 <<= 2;
        l5 = l14 >>> 32;
        l6 = l14 & 0xFFFFFFFFL;
        l7 = PI_O_4_BITS[0] >>> 32;
        l8 = PI_O_4_BITS[0] & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l10 = l6 * l8;
        l11 = l6 * l7;
        l12 = l5 * l8;
        long l15 = l10 + (l12 << 32);
        long l16 = l9 + (l12 >>> 32);
        bl2 = (l10 & Long.MIN_VALUE) != 0L;
        bl3 = (l12 & 0x80000000L) != 0L;
        boolean bl7 = bl = (l15 & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        bl2 = (l15 & Long.MIN_VALUE) != 0L;
        bl3 = (l11 & 0x80000000L) != 0L;
        l16 += l11 >>> 32;
        boolean bl8 = bl = ((l15 += l11 << 32) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        l7 = PI_O_4_BITS[1] >>> 32;
        l8 = PI_O_4_BITS[1] & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l11 = l6 * l7;
        l12 = l5 * l8;
        bl2 = (l15 & Long.MIN_VALUE) != 0L;
        bl3 = ((l9 += l11 + l12 >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl9 = bl = ((l15 += l9) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        l5 = l13 >>> 32;
        l6 = l13 & 0xFFFFFFFFL;
        l7 = PI_O_4_BITS[0] >>> 32;
        l8 = PI_O_4_BITS[0] & 0xFFFFFFFFL;
        l9 = l5 * l7;
        l11 = l6 * l7;
        l12 = l5 * l8;
        bl2 = (l15 & Long.MIN_VALUE) != 0L;
        bl3 = ((l9 += l11 + l12 >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl10 = bl = ((l15 += l9) & Long.MIN_VALUE) != 0L;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l16;
        }
        double d2 = (double)(l16 >>> 12) / 4.503599627370496E15;
        double d3 = (double)(((l16 & 0xFFFL) << 40) + (l15 >>> 24)) / 4.503599627370496E15 / 4.503599627370496E15;
        double d4 = d2 + d3;
        double d5 = -(d4 - d2 - d3);
        dArray[0] = n4;
        dArray[1] = d4 * 2.0;
        dArray[2] = d5 * 2.0;
    }

    public static double pow(double d, double d2) {
        if (d2 == 0.0) {
            return 1.0;
        }
        long l = Double.doubleToRawLongBits(d2);
        int n = (int)((l & 0x7FF0000000000000L) >> 52);
        long l2 = l & 0xFFFFFFFFFFFFFL;
        long l3 = Double.doubleToRawLongBits(d);
        int n2 = (int)((l3 & 0x7FF0000000000000L) >> 52);
        long l4 = l3 & 0xFFFFFFFFFFFFFL;
        if (n > 1085) {
            if (n == 2047 && l2 != 0L || n2 == 2047 && l4 != 0L) {
                return Double.NaN;
            }
            if (n2 == 1023 && l4 == 0L) {
                if (n == 2047) {
                    return Double.NaN;
                }
                return 1.0;
            }
            if (d2 > 0.0 ^ n2 < 1023) {
                return Double.POSITIVE_INFINITY;
            }
            return 0.0;
        }
        if (n >= 1023) {
            long l5 = 0x10000000000000L | l2;
            if (n < 1075) {
                long l6 = -1L << 1075 - n;
                if ((l5 & l6) == l5) {
                    long l7 = l5 >> 1075 - n;
                    return ApacheMath.pow(d, d2 < 0.0 ? -l7 : l7);
                }
            } else {
                long l8 = l5 << n - 1075;
                return ApacheMath.pow(d, d2 < 0.0 ? -l8 : l8);
            }
        }
        if (d == 0.0) {
            return d2 < 0.0 ? Double.POSITIVE_INFINITY : 0.0;
        }
        if (n2 == 2047) {
            if (l4 == 0L) {
                return d2 < 0.0 ? 0.0 : Double.POSITIVE_INFINITY;
            }
            return Double.NaN;
        }
        if (d < 0.0) {
            return Double.NaN;
        }
        double d3 = d2 * 1.073741824E9;
        double d4 = d2 + d3 - d3;
        double d5 = d2 - d4;
        double[] dArray = new double[2];
        double d6 = ApacheMath.log(d, dArray);
        if (Double.isInfinite(d6)) {
            return d6;
        }
        double d7 = dArray[0];
        double d8 = dArray[1];
        double d9 = d7 * 1.073741824E9;
        double d10 = d7 + d9 - d9;
        d8 += d7 - d10;
        d7 = d10;
        double d11 = d7 * d4;
        double d12 = d7 * d5 + d8 * d4 + d8 * d5;
        d7 = d11 + d12;
        d8 = -(d7 - d11 - d12);
        double d13 = 0.008333333333333333;
        d13 = d13 * d8 + 0.041666666666666664;
        d13 = d13 * d8 + 0.16666666666666666;
        d13 = d13 * d8 + 0.5;
        d13 = d13 * d8 + 1.0;
        double d14 = ApacheMath.exp(d7, d13 *= d8, null);
        return d14;
    }

    public static double pow(double d, int n) {
        return ApacheMath.pow(d, (long)n);
    }

    private static double doubleHighPart(double d) {
        if (d > -Precision.SAFE_MIN && d < Precision.SAFE_MIN) {
            return d;
        }
        long l = Double.doubleToRawLongBits(d);
        return Double.longBitsToDouble(l &= 0xFFFFFFFFC0000000L);
    }

    public static double expm1(double d) {
        return ApacheMath.expm1(d, null);
    }

    public static int round(float f) {
        int n = Float.floatToRawIntBits(f);
        int n2 = n >> 23 & 0xFF;
        int n3 = 149 - n2;
        if ((n3 & 0xFFFFFFE0) == 0) {
            int n4 = 0x800000 | n & 0x7FFFFF;
            if (n < 0) {
                n4 = -n4;
            }
            return (n4 >> n3) + 1 >> 1;
        }
        return (int)f;
    }

    public static double toRadians(double d) {
        if (Double.isInfinite(d) || d == 0.0) {
            return d;
        }
        double d2 = 0.01745329052209854;
        double d3 = 1.997844754509471E-9;
        double d4 = ApacheMath.doubleHighPart(d);
        double d5 = d - d4;
        double d6 = d5 * 1.997844754509471E-9 + d5 * 0.01745329052209854 + d4 * 1.997844754509471E-9 + d4 * 0.01745329052209854;
        if (d6 == 0.0) {
            d6 *= d;
        }
        return d6;
    }

    public static long max(long l, long l2) {
        return l <= l2 ? l2 : l;
    }

    public static double log(double d, double d2) {
        return ApacheMath.log(d2) / ApacheMath.log(d);
    }

    public static double max(double d, double d2) {
        if (d > d2) {
            return d;
        }
        if (d < d2) {
            return d2;
        }
        if (d != d2) {
            return Double.NaN;
        }
        long l = Double.doubleToRawLongBits(d);
        if (l == Long.MIN_VALUE) {
            return d2;
        }
        return d;
    }

    public static double random() {
        return Math.random();
    }

    private static double sinQ(double d, double d2) {
        int n = (int)(d * 8.0 + 0.5);
        double d3 = d - EIGHTHS[n];
        double d4 = SINE_TABLE_A[n];
        double d5 = SINE_TABLE_B[n];
        double d6 = COSINE_TABLE_A[n];
        double d7 = COSINE_TABLE_B[n];
        double d8 = d3;
        double d9 = ApacheMath.polySine(d3);
        double d10 = 1.0;
        double d11 = ApacheMath.polyCosine(d3);
        double d12 = d8 * 1.073741824E9;
        double d13 = d8 + d12 - d12;
        d9 += d8 - d13;
        d8 = d13;
        double d14 = 0.0;
        double d15 = 0.0;
        double d16 = d4;
        double d17 = d14 + d16;
        double d18 = -(d17 - d14 - d16);
        d14 = d17;
        d15 += d18;
        d16 = d6 * d8;
        d17 = d14 + d16;
        d18 = -(d17 - d14 - d16);
        d14 = d17;
        d15 += d18;
        d15 = d15 + d4 * d11 + d6 * d9;
        d15 = d15 + d5 + d7 * d8 + d5 * d11 + d7 * d9;
        if (d2 != 0.0) {
            d16 = ((d6 + d7) * (1.0 + d11) - (d4 + d5) * (d8 + d9)) * d2;
            d17 = d14 + d16;
            d18 = -(d17 - d14 - d16);
            d14 = d17;
            d15 += d18;
        }
        double d19 = d14 + d15;
        return d19;
    }

    public static double rint(double d) {
        double d2 = ApacheMath.floor(d);
        double d3 = d - d2;
        if (d3 > 0.5) {
            if (d2 == -1.0) {
                return -0.0;
            }
            return d2 + 1.0;
        }
        if (d3 < 0.5) {
            return d2;
        }
        long l = (long)d2;
        return (l & 1L) == 0L ? d2 : d2 + 1.0;
    }

    public static double acos(double d) {
        if (Double.isNaN(d)) {
            return Double.NaN;
        }
        if (d > 1.0 || d < -1.0) {
            return Double.NaN;
        }
        if (d == -1.0) {
            return Math.PI;
        }
        if (d == 1.0) {
            return 0.0;
        }
        if (d == 0.0) {
            return 1.5707963267948966;
        }
        double d2 = d * 1.073741824E9;
        double d3 = d + d2 - d2;
        double d4 = d - d3;
        double d5 = d3 * d3;
        double d6 = d3 * d4 * 2.0 + d4 * d4;
        d5 = -d5;
        d6 = -d6;
        double d7 = 1.0 + d5;
        double d8 = -(d7 - 1.0 - d5);
        d2 = d7 + d6;
        d8 += -(d2 - d7 - d6);
        d7 = d2;
        double d9 = ApacheMath.sqrt(d7);
        d2 = d9 * 1.073741824E9;
        d5 = d9 + d2 - d2;
        d6 = d9 - d5;
        d6 += (d7 - d5 * d5 - 2.0 * d5 * d6 - d6 * d6) / (2.0 * d9);
        d6 += d8 / (2.0 * d9);
        d9 = d5 + d6;
        d6 = -(d9 - d5 - d6);
        double d10 = d9 / d;
        if (Double.isInfinite(d10)) {
            return 1.5707963267948966;
        }
        double d11 = ApacheMath.doubleHighPart(d10);
        double d12 = d10 - d11;
        d12 += (d9 - d11 * d3 - d11 * d4 - d12 * d3 - d12 * d4) / d;
        d2 = d11 + (d12 += d6 / d);
        d12 = -(d2 - d11 - d12);
        d11 = d2;
        return ApacheMath.atan(d11, d12, d < 0.0);
    }

    public static double nextUp(double d) {
        return ApacheMath.nextAfter(d, Double.POSITIVE_INFINITY);
    }

    public static float scalb(float f, int n) {
        if (n > -127 && n < 128) {
            return f * Float.intBitsToFloat(n + 127 << 23);
        }
        if (Float.isNaN(f) || Float.isInfinite(f) || f == 0.0f) {
            return f;
        }
        if (n < -277) {
            return f > 0.0f ? 0.0f : -0.0f;
        }
        if (n > 276) {
            return f > 0.0f ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        int n2 = Float.floatToIntBits(f);
        int n3 = n2 & Integer.MIN_VALUE;
        int n4 = n2 >>> 23 & 0xFF;
        int n5 = n2 & 0x7FFFFF;
        int n6 = n4 + n;
        if (n < 0) {
            if (n6 > 0) {
                return Float.intBitsToFloat(n3 | n6 << 23 | n5);
            }
            if (n6 > -24) {
                int n7 = (n5 |= 0x800000) & 1 << -n6;
                n5 >>>= 1 - n6;
                if (n7 != 0) {
                    ++n5;
                }
                return Float.intBitsToFloat(n3 | n5);
            }
            return n3 == 0 ? 0.0f : -0.0f;
        }
        if (n4 == 0) {
            while (n5 >>> 23 != 1) {
                n5 <<= 1;
                --n6;
            }
            n5 &= 0x7FFFFF;
            if (++n6 < 255) {
                return Float.intBitsToFloat(n3 | n6 << 23 | n5);
            }
            return n3 == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        if (n6 < 255) {
            return Float.intBitsToFloat(n3 | n6 << 23 | n5);
        }
        return n3 == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    }

    public static double sqrt(double d) {
        return Math.sqrt(d);
    }

    public static long abs(long l) {
        long l2 = l >>> 63;
        return (l ^ (l2 ^ 0xFFFFFFFFFFFFFFFFL) + 1L) + l2;
    }

    public static double ulp(double d) {
        if (Double.isInfinite(d)) {
            return Double.POSITIVE_INFINITY;
        }
        return ApacheMath.abs(d - Double.longBitsToDouble(Double.doubleToRawLongBits(d) ^ 1L));
    }

    public static double log(double d) {
        return ApacheMath.log(d, null);
    }

    public static double abs(double d) {
        return Double.longBitsToDouble(Long.MAX_VALUE & Double.doubleToRawLongBits(d));
    }

    public static float max(float f, float f2) {
        if (f > f2) {
            return f;
        }
        if (f < f2) {
            return f2;
        }
        if (f != f2) {
            return Float.NaN;
        }
        int n = Float.floatToRawIntBits(f);
        if (n == Integer.MIN_VALUE) {
            return f2;
        }
        return f;
    }

    public static double cosh(double d) {
        if (Double.isNaN(d)) {
            return d;
        }
        if (d > 20.0) {
            if (d >= LOG_MAX_VALUE) {
                double d2 = ApacheMath.exp(0.5 * d);
                return 0.5 * d2 * d2;
            }
            return 0.5 * ApacheMath.exp(d);
        }
        if (d < -20.0) {
            if (d <= -LOG_MAX_VALUE) {
                double d3 = ApacheMath.exp(-0.5 * d);
                return 0.5 * d3 * d3;
            }
            return 0.5 * ApacheMath.exp(-d);
        }
        double[] dArray = new double[2];
        if (d < 0.0) {
            d = -d;
        }
        ApacheMath.exp(d, 0.0, dArray);
        double d4 = dArray[0] + dArray[1];
        double d5 = -(d4 - dArray[0] - dArray[1]);
        double d6 = d4 * 1.073741824E9;
        double d7 = d4 + d6 - d6;
        double d8 = d4 - d7;
        double d9 = 1.0 / d4;
        d6 = d9 * 1.073741824E9;
        double d10 = d9 + d6 - d6;
        double d11 = d9 - d10;
        d11 += (1.0 - d7 * d10 - d7 * d11 - d8 * d10 - d8 * d11) * d9;
        d11 += -d5 * d9 * d9;
        d6 = d4 + d10;
        d5 += -(d6 - d4 - d10);
        d4 = d6;
        d6 = d4 + d11;
        d5 += -(d6 - d4 - d11);
        d4 = d6;
        double d12 = d4 + d5;
        return d12 *= 0.5;
    }

    public static double log1p(double d) {
        if (d == -1.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (d == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (d > 1.0E-6 || d < -1.0E-6) {
            double d2 = 1.0 + d;
            double d3 = -(d2 - 1.0 - d);
            double[] dArray = new double[2];
            double d4 = ApacheMath.log(d2, dArray);
            if (Double.isInfinite(d4)) {
                return d4;
            }
            double d5 = d3 / d2;
            double d6 = 0.5 * d5 + 1.0;
            return d6 * d5 + dArray[1] + dArray[0];
        }
        double d7 = (d * 0.3333333333333333 - 0.5) * d + 1.0;
        return d7 * d;
    }

    public static double exp(double d) {
        return ApacheMath.exp(d, 0.0, null);
    }

    public static double atanh(double d) {
        double d2;
        boolean bl = false;
        if (d < 0.0) {
            bl = true;
            d = -d;
        }
        if (d > 0.15) {
            d2 = 0.5 * ApacheMath.log((1.0 + d) / (1.0 - d));
        } else {
            double d3 = d * d;
            d2 = d > 0.087 ? d * (1.0 + d3 * (0.3333333333333333 + d3 * (0.2 + d3 * (0.14285714285714285 + d3 * (0.1111111111111111 + d3 * (0.09090909090909091 + d3 * (0.07692307692307693 + d3 * (0.06666666666666667 + d3 * 0.058823529411764705)))))))) : (d > 0.031 ? d * (1.0 + d3 * (0.3333333333333333 + d3 * (0.2 + d3 * (0.14285714285714285 + d3 * (0.1111111111111111 + d3 * (0.09090909090909091 + d3 * 0.07692307692307693)))))) : (d > 0.003 ? d * (1.0 + d3 * (0.3333333333333333 + d3 * (0.2 + d3 * (0.14285714285714285 + d3 * 0.1111111111111111)))) : d * (1.0 + d3 * (0.3333333333333333 + d3 * 0.2))));
        }
        return bl ? -d2 : d2;
    }

    public static double cos(double d) {
        int n = 0;
        double d2 = d;
        if (d < 0.0) {
            d2 = -d2;
        }
        if (d2 != d2 || d2 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double d3 = 0.0;
        if (d2 > 3294198.0) {
            double[] dArray = new double[3];
            ApacheMath.reducePayneHanek(d2, dArray);
            n = (int)dArray[0] & 3;
            d2 = dArray[1];
            d3 = dArray[2];
        } else if (d2 > 1.5707963267948966) {
            CodyWaite codyWaite = new CodyWaite(d2);
            n = codyWaite.getK() & 3;
            d2 = codyWaite.getRemA();
            d3 = codyWaite.getRemB();
        }
        switch (n) {
            case 0: {
                return ApacheMath.cosQ(d2, d3);
            }
            case 1: {
                return -ApacheMath.sinQ(d2, d3);
            }
            case 2: {
                return -ApacheMath.cosQ(d2, d3);
            }
            case 3: {
                return ApacheMath.sinQ(d2, d3);
            }
        }
        return Double.NaN;
    }

    private static double atan(double d, double d2, boolean bl) {
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        int n;
        boolean bl2;
        if (d == 0.0) {
            return bl ? ApacheMath.copySign(Math.PI, d) : d;
        }
        if (d < 0.0) {
            d = -d;
            d2 = -d2;
            bl2 = true;
        } else {
            bl2 = false;
        }
        if (d > 1.633123935319537E16) {
            return bl2 ^ bl ? -1.5707963267948966 : 1.5707963267948966;
        }
        if (d < 1.0) {
            n = (int)((-1.7168146928204135 * d * d + 8.0) * d + 0.5);
        } else {
            d10 = 1.0 / d;
            n = (int)(-((-1.7168146928204135 * d10 * d10 + 8.0) * d10) + 13.07);
        }
        d10 = TANGENT_TABLE_A[n];
        double d11 = TANGENT_TABLE_B[n];
        double d12 = d - d10;
        double d13 = -(d12 - d + d10);
        double d14 = d12 + (d13 += d2 - d11);
        d13 = -(d14 - d12 - d13);
        d12 = d14;
        d14 = d * 1.073741824E9;
        double d15 = d + d14 - d14;
        double d16 = d2 + d - d15;
        d = d15;
        d2 += d16;
        if (n == 0) {
            d9 = 1.0 / (1.0 + (d + d2) * (d10 + d11));
            d15 = d12 * d9;
            d16 = d13 * d9;
        } else {
            d9 = d * d10;
            d8 = 1.0 + d9;
            d7 = -(d8 - 1.0 - d9);
            d9 = d2 * d10 + d * d11;
            d14 = d8 + d9;
            d7 += -(d14 - d8 - d9);
            d8 = d14;
            d7 += d2 * d11;
            d15 = d12 / d8;
            d14 = d15 * 1.073741824E9;
            d6 = d15 + d14 - d14;
            d5 = d15 - d6;
            d14 = d8 * 1.073741824E9;
            d4 = d8 + d14 - d14;
            d3 = d8 - d4;
            d16 = (d12 - d6 * d4 - d6 * d3 - d5 * d4 - d5 * d3) / d8;
            d16 += -d12 * d7 / d8 / d8;
            d16 += d13 / d8;
        }
        d12 = d15;
        d13 = d16;
        d9 = d12 * d12;
        d16 = 0.07490822288864472;
        d16 = d16 * d9 - 0.09088450866185192;
        d16 = d16 * d9 + 0.11111095942313305;
        d16 = d16 * d9 - 0.1428571423679182;
        d16 = d16 * d9 + 0.19999999999923582;
        d16 = d16 * d9 - 0.33333333333333287;
        d16 = d16 * d9 * d12;
        d15 = d12;
        d14 = d15 + d16;
        d16 = -(d14 - d15 - d16);
        d15 = d14;
        d8 = EIGHTHS[n];
        d7 = d8 + d15;
        d6 = -(d7 - d8 - d15);
        d14 = d7 + (d16 += d13 / (1.0 + d12 * d12));
        d6 += -(d14 - d7 - d16);
        d7 = d14;
        d5 = d7 + d6;
        if (bl) {
            d4 = -(d5 - d7 - d6);
            d3 = Math.PI;
            double d17 = 1.2246467991473532E-16;
            d7 = Math.PI - d5;
            d6 = -(d7 - Math.PI + d5);
            d5 = d7 + (d6 += 1.2246467991473532E-16 - d4);
        }
        if (bl2 ^ bl) {
            d5 = -d5;
        }
        return d5;
    }

    public static long round(double d) {
        long l = Double.doubleToRawLongBits(d);
        int n = (int)(l >> 52) & 0x7FF;
        int n2 = 1074 - n;
        if ((n2 & 0xFFFFFFC0) == 0) {
            long l2 = 0x10000000000000L | l & 0xFFFFFFFFFFFFFL;
            if (l < 0L) {
                l2 = -l2;
            }
            return (l2 >> n2) + 1L >> 1;
        }
        return (long)d;
    }

    public static int getExponent(double d) {
        return (int)(Double.doubleToRawLongBits(d) >>> 52 & 0x7FFL) - 1023;
    }

    public static double asinh(double d) {
        double d2;
        boolean bl = false;
        if (d < 0.0) {
            bl = true;
            d = -d;
        }
        if (d > 0.167) {
            d2 = ApacheMath.log(ApacheMath.sqrt(d * d + 1.0) + d);
        } else {
            double d3 = d * d;
            d2 = d > 0.097 ? d * (1.0 - d3 * (0.3333333333333333 - d3 * (0.2 - d3 * (0.14285714285714285 - d3 * (0.1111111111111111 - d3 * (0.09090909090909091 - d3 * (0.07692307692307693 - d3 * (0.06666666666666667 - d3 * 0.058823529411764705 * 0.9375) * 0.9285714285714286) * 0.9166666666666666) * 0.9) * 0.875) * 0.8333333333333334) * 0.75) * 0.5) : (d > 0.036 ? d * (1.0 - d3 * (0.3333333333333333 - d3 * (0.2 - d3 * (0.14285714285714285 - d3 * (0.1111111111111111 - d3 * (0.09090909090909091 - d3 * 0.07692307692307693 * 0.9166666666666666) * 0.9) * 0.875) * 0.8333333333333334) * 0.75) * 0.5) : (d > 0.0036 ? d * (1.0 - d3 * (0.3333333333333333 - d3 * (0.2 - d3 * (0.14285714285714285 - d3 * 0.1111111111111111 * 0.875) * 0.8333333333333334) * 0.75) * 0.5) : d * (1.0 - d3 * (0.3333333333333333 - d3 * 0.2 * 0.75) * 0.5)));
        }
        return bl ? -d2 : d2;
    }

    public static double sinh(double d) {
        double d2;
        boolean bl = false;
        if (Double.isNaN(d)) {
            return d;
        }
        if (d > 20.0) {
            if (d >= LOG_MAX_VALUE) {
                double d3 = ApacheMath.exp(0.5 * d);
                return 0.5 * d3 * d3;
            }
            return 0.5 * ApacheMath.exp(d);
        }
        if (d < -20.0) {
            if (d <= -LOG_MAX_VALUE) {
                double d4 = ApacheMath.exp(-0.5 * d);
                return -0.5 * d4 * d4;
            }
            return -0.5 * ApacheMath.exp(-d);
        }
        if (d == 0.0) {
            return d;
        }
        if (d < 0.0) {
            d = -d;
            bl = true;
        }
        if (d > 0.25) {
            double[] dArray = new double[2];
            ApacheMath.exp(d, 0.0, dArray);
            double d5 = dArray[0] + dArray[1];
            double d6 = -(d5 - dArray[0] - dArray[1]);
            double d7 = d5 * 1.073741824E9;
            double d8 = d5 + d7 - d7;
            double d9 = d5 - d8;
            double d10 = 1.0 / d5;
            d7 = d10 * 1.073741824E9;
            double d11 = d10 + d7 - d7;
            double d12 = d10 - d11;
            d12 += (1.0 - d8 * d11 - d8 * d12 - d9 * d11 - d9 * d12) * d10;
            d12 += -d6 * d10 * d10;
            d11 = -d11;
            d12 = -d12;
            d7 = d5 + d11;
            d6 += -(d7 - d5 - d11);
            d5 = d7;
            d7 = d5 + d12;
            d6 += -(d7 - d5 - d12);
            d5 = d7;
            d2 = d5 + d6;
            d2 *= 0.5;
        } else {
            double[] dArray = new double[2];
            ApacheMath.expm1(d, dArray);
            double d13 = dArray[0] + dArray[1];
            double d14 = -(d13 - dArray[0] - dArray[1]);
            double d15 = 1.0 + d13;
            double d16 = 1.0 / d15;
            double d17 = -(d15 - 1.0 - d13) + d14;
            double d18 = d13 * d16;
            double d19 = d18 * 1.073741824E9;
            double d20 = d18 + d19 - d19;
            double d21 = d18 - d20;
            d19 = d15 * 1.073741824E9;
            double d22 = d15 + d19 - d19;
            double d23 = d15 - d22;
            d21 += (d13 - d22 * d20 - d22 * d21 - d23 * d20 - d23 * d21) * d16;
            d21 += d14 * d16;
            d21 += -d13 * d17 * d16 * d16;
            d19 = d13 + d20;
            d14 += -(d19 - d13 - d20);
            d13 = d19;
            d19 = d13 + d21;
            d14 += -(d19 - d13 - d21);
            d13 = d19;
            d2 = d13 + d14;
            d2 *= 0.5;
        }
        if (bl) {
            d2 = -d2;
        }
        return d2;
    }

    public static float signum(float f) {
        return f < 0.0f ? -1.0f : (f > 0.0f ? 1.0f : f);
    }

    private static double log(double d, double[] dArray) {
        double d2;
        double d3;
        double d4;
        double d5;
        if (d == 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        long l = Double.doubleToRawLongBits(d);
        if (((l & Long.MIN_VALUE) != 0L || Double.isNaN(d)) && d != 0.0) {
            if (dArray != null) {
                dArray[0] = Double.NaN;
            }
            return Double.NaN;
        }
        if (d == Double.POSITIVE_INFINITY) {
            if (dArray != null) {
                dArray[0] = Double.POSITIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        int n = (int)(l >> 52) - 1023;
        if ((l & 0x7FF0000000000000L) == 0L) {
            if (d == 0.0) {
                if (dArray != null) {
                    dArray[0] = Double.NEGATIVE_INFINITY;
                }
                return Double.NEGATIVE_INFINITY;
            }
            l <<= 1;
            while ((l & 0x10000000000000L) == 0L) {
                --n;
                l <<= 1;
            }
        }
        if ((n == -1 || n == 0) && d < 1.01 && d > 0.99 && dArray == null) {
            double d6 = d - 1.0;
            double d7 = d6 - d + 1.0;
            double d8 = d6 * 1.073741824E9;
            double d9 = d6 + d8 - d8;
            double d10 = d6 - d9;
            d6 = d9;
            d7 = d10;
            double[] dArray2 = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
            double d11 = dArray2[0];
            double d12 = dArray2[1];
            for (int i = LN_QUICK_COEF.length - 2; i >= 0; --i) {
                d9 = d11 * d6;
                d10 = d11 * d7 + d12 * d6 + d12 * d7;
                d8 = d9 * 1.073741824E9;
                d11 = d9 + d8 - d8;
                d12 = d9 - d11 + d10;
                double[] dArray3 = LN_QUICK_COEF[i];
                d9 = d11 + dArray3[0];
                d10 = d12 + dArray3[1];
                d8 = d9 * 1.073741824E9;
                d11 = d9 + d8 - d8;
                d12 = d9 - d11 + d10;
            }
            d9 = d11 * d6;
            d10 = d11 * d7 + d12 * d6 + d12 * d7;
            d8 = d9 * 1.073741824E9;
            d11 = d9 + d8 - d8;
            d12 = d9 - d11 + d10;
            return d11 + d12;
        }
        double[] dArray4 = lnMant.access$400()[(int)((l & 0xFFC0000000000L) >> 42)];
        double d13 = (double)(l & 0x3FFFFFFFFFFL) / (4.503599627370496E15 + (double)(l & 0xFFC0000000000L));
        double d14 = 0.0;
        double d15 = 0.0;
        if (dArray != null) {
            d5 = d13 * 1.073741824E9;
            d4 = d13 + d5 - d5;
            d3 = d13 - d4;
            d2 = d4;
            double d16 = d3;
            double d17 = l & 0x3FFFFFFFFFFL;
            double d18 = 4.503599627370496E15 + (double)(l & 0xFFC0000000000L);
            d4 = d17 - d2 * d18 - d16 * d18;
            d16 += d4 / d18;
            double[] dArray5 = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length - 1];
            double d19 = dArray5[0];
            double d20 = dArray5[1];
            for (int i = LN_HI_PREC_COEF.length - 2; i >= 0; --i) {
                d4 = d19 * d2;
                d3 = d19 * d16 + d20 * d2 + d20 * d16;
                d5 = d4 * 1.073741824E9;
                d19 = d4 + d5 - d5;
                d20 = d4 - d19 + d3;
                double[] dArray6 = LN_HI_PREC_COEF[i];
                d4 = d19 + dArray6[0];
                d3 = d20 + dArray6[1];
                d5 = d4 * 1.073741824E9;
                d19 = d4 + d5 - d5;
                d20 = d4 - d19 + d3;
            }
            d4 = d19 * d2;
            d3 = d19 * d16 + d20 * d2 + d20 * d16;
            d14 = d4 + d3;
            d15 = -(d14 - d4 - d3);
        } else {
            d14 = -0.16624882440418567;
            d14 = d14 * d13 + 0.19999954120254515;
            d14 = d14 * d13 + -0.2499999997677497;
            d14 = d14 * d13 + 0.3333333333332802;
            d14 = d14 * d13 + -0.5;
            d14 = d14 * d13 + 1.0;
            d14 *= d13;
        }
        d5 = 0.6931470632553101 * (double)n;
        d4 = 0.0;
        d3 = d5 + dArray4[0];
        d2 = -(d3 - d5 - dArray4[0]);
        d5 = d3;
        d4 += d2;
        d3 = d5 + d14;
        d2 = -(d3 - d5 - d14);
        d5 = d3;
        d4 += d2;
        d3 = d5 + 1.1730463525082348E-7 * (double)n;
        d2 = -(d3 - d5 - 1.1730463525082348E-7 * (double)n);
        d5 = d3;
        d4 += d2;
        d3 = d5 + dArray4[1];
        d2 = -(d3 - d5 - dArray4[1]);
        d5 = d3;
        d4 += d2;
        d3 = d5 + d15;
        d2 = -(d3 - d5 - d15);
        d5 = d3;
        d4 += d2;
        if (dArray != null) {
            dArray[0] = d5;
            dArray[1] = d4;
        }
        return d5 + d4;
    }

    public static double acosh(double d) {
        return ApacheMath.log(d + ApacheMath.sqrt(d * d - 1.0));
    }

    public static double scalb(double d, int n) {
        if (n > -1023 && n < 1024) {
            return d * Double.longBitsToDouble((long)(n + 1023) << 52);
        }
        if (Double.isNaN(d) || Double.isInfinite(d) || d == 0.0) {
            return d;
        }
        if (n < -2098) {
            return d > 0.0 ? 0.0 : -0.0;
        }
        if (n > 2097) {
            return d > 0.0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        long l = Double.doubleToRawLongBits(d);
        long l2 = l & Long.MIN_VALUE;
        int n2 = (int)(l >>> 52) & 0x7FF;
        long l3 = l & 0xFFFFFFFFFFFFFL;
        int n3 = n2 + n;
        if (n < 0) {
            if (n3 > 0) {
                return Double.longBitsToDouble(l2 | (long)n3 << 52 | l3);
            }
            if (n3 > -53) {
                long l4 = (l3 |= 0x10000000000000L) & 1L << -n3;
                l3 >>>= 1 - n3;
                if (l4 != 0L) {
                    ++l3;
                }
                return Double.longBitsToDouble(l2 | l3);
            }
            return l2 == 0L ? 0.0 : -0.0;
        }
        if (n2 == 0) {
            while (l3 >>> 52 != 1L) {
                l3 <<= 1;
                --n3;
            }
            l3 &= 0xFFFFFFFFFFFFFL;
            if (++n3 < 2047) {
                return Double.longBitsToDouble(l2 | (long)n3 << 52 | l3);
            }
            return l2 == 0L ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        if (n3 < 2047) {
            return Double.longBitsToDouble(l2 | (long)n3 << 52 | l3);
        }
        return l2 == 0L ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public static double pow(double d, long l) {
        if (l == 0L) {
            return 1.0;
        }
        if (l > 0L) {
            return Split.access$600(Split.access$500(new Split(d), l));
        }
        return Split.access$600(Split.access$500(new Split(d).reciprocal(), -l));
    }

    public static double nextAfter(double d, double d2) {
        long l;
        long l2;
        if (Double.isNaN(d) || Double.isNaN(d2)) {
            return Double.NaN;
        }
        if (d == d2) {
            return d2;
        }
        if (Double.isInfinite(d)) {
            return d < 0.0 ? -1.7976931348623157E308 : Double.MAX_VALUE;
        }
        if (d == 0.0) {
            return d2 < 0.0 ? -4.9E-324 : Double.MIN_VALUE;
        }
        if (d2 < d ^ (l2 = (l = Double.doubleToRawLongBits(d)) & Long.MIN_VALUE) == 0L) {
            return Double.longBitsToDouble(l2 | (l & Long.MAX_VALUE) + 1L);
        }
        return Double.longBitsToDouble(l2 | (l & Long.MAX_VALUE) - 1L);
    }

    public static float nextAfter(float f, double d) {
        int n;
        int n2;
        if (Double.isNaN(f) || Double.isNaN(d)) {
            return Float.NaN;
        }
        if ((double)f == d) {
            return (float)d;
        }
        if (Float.isInfinite(f)) {
            return f < 0.0f ? -3.4028235E38f : Float.MAX_VALUE;
        }
        if (f == 0.0f) {
            return d < 0.0 ? -1.4E-45f : Float.MIN_VALUE;
        }
        if (d < (double)f ^ (n2 = (n = Float.floatToIntBits(f)) & Integer.MIN_VALUE) == 0) {
            return Float.intBitsToFloat(n2 | (n & Integer.MAX_VALUE) + 1);
        }
        return Float.intBitsToFloat(n2 | (n & Integer.MAX_VALUE) - 1);
    }

    public static double tanh(double d) {
        double d2;
        boolean bl = false;
        if (Double.isNaN(d)) {
            return d;
        }
        if (d > 20.0) {
            return 1.0;
        }
        if (d < -20.0) {
            return -1.0;
        }
        if (d == 0.0) {
            return d;
        }
        if (d < 0.0) {
            d = -d;
            bl = true;
        }
        if (d >= 0.5) {
            double[] dArray = new double[2];
            ApacheMath.exp(d * 2.0, 0.0, dArray);
            double d3 = dArray[0] + dArray[1];
            double d4 = -(d3 - dArray[0] - dArray[1]);
            double d5 = -1.0 + d3;
            double d6 = -(d5 + 1.0 - d3);
            double d7 = d5 + d4;
            d6 += -(d7 - d5 - d4);
            d5 = d7;
            double d8 = 1.0 + d3;
            double d9 = -(d8 - 1.0 - d3);
            d7 = d8 + d4;
            d9 += -(d7 - d8 - d4);
            d8 = d7;
            d7 = d8 * 1.073741824E9;
            double d10 = d8 + d7 - d7;
            double d11 = d8 - d10;
            double d12 = d5 / d8;
            d7 = d12 * 1.073741824E9;
            double d13 = d12 + d7 - d7;
            double d14 = d12 - d13;
            d14 += (d5 - d10 * d13 - d10 * d14 - d11 * d13 - d11 * d14) / d8;
            d14 += d6 / d8;
            d2 = d13 + (d14 += -d9 * d5 / d8 / d8);
        } else {
            double[] dArray = new double[2];
            ApacheMath.expm1(d * 2.0, dArray);
            double d15 = dArray[0] + dArray[1];
            double d16 = -(d15 - dArray[0] - dArray[1]);
            double d17 = d15;
            double d18 = d16;
            double d19 = 2.0 + d15;
            double d20 = -(d19 - 2.0 - d15);
            double d21 = d19 + d16;
            d20 += -(d21 - d19 - d16);
            d19 = d21;
            d21 = d19 * 1.073741824E9;
            double d22 = d19 + d21 - d21;
            double d23 = d19 - d22;
            double d24 = d17 / d19;
            d21 = d24 * 1.073741824E9;
            double d25 = d24 + d21 - d21;
            double d26 = d24 - d25;
            d26 += (d17 - d22 * d25 - d22 * d26 - d23 * d25 - d23 * d26) / d19;
            d26 += d18 / d19;
            d2 = d25 + (d26 += -d20 * d17 / d19 / d19);
        }
        if (bl) {
            d2 = -d2;
        }
        return d2;
    }

    private static double exp(double d, double d2, double[] dArray) {
        int n = (int)d;
        if (d < 0.0) {
            if (d < -746.0) {
                if (dArray != null) {
                    dArray[0] = 0.0;
                    dArray[1] = 0.0;
                }
                return 0.0;
            }
            if (n < -709) {
                double d3 = ApacheMath.exp(d + 40.19140625, d2, dArray) / 2.85040095144011776E17;
                if (dArray != null) {
                    dArray[0] = dArray[0] / 2.85040095144011776E17;
                    dArray[1] = dArray[1] / 2.85040095144011776E17;
                }
                return d3;
            }
            if (n == -709) {
                double d4 = ApacheMath.exp(d + 1.494140625, d2, dArray) / 4.455505956692757;
                if (dArray != null) {
                    dArray[0] = dArray[0] / 4.455505956692757;
                    dArray[1] = dArray[1] / 4.455505956692757;
                }
                return d4;
            }
            --n;
        } else if (n > 709) {
            if (dArray != null) {
                dArray[0] = Double.POSITIVE_INFINITY;
                dArray[1] = 0.0;
            }
            return Double.POSITIVE_INFINITY;
        }
        double d5 = ExpIntTable.access$000()[750 + n];
        double d6 = ExpIntTable.access$100()[750 + n];
        int n2 = (int)((d - (double)n) * 1024.0);
        double d7 = ExpFracTable.access$200()[n2];
        double d8 = ExpFracTable.access$300()[n2];
        double d9 = d - ((double)n + (double)n2 / 1024.0);
        double d10 = 0.04168701738764507;
        d10 = d10 * d9 + 0.1666666505023083;
        d10 = d10 * d9 + 0.5000000000042687;
        d10 = d10 * d9 + 1.0;
        d10 = d10 * d9 + -3.940510424527919E-20;
        double d11 = d5 * d7;
        double d12 = d5 * d8 + d6 * d7 + d6 * d8;
        double d13 = d12 + d11;
        if (d13 == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        double d14 = d2 != 0.0 ? d13 * d2 * d10 + d13 * d2 + d13 * d10 + d12 + d11 : d13 * d10 + d12 + d11;
        if (dArray != null) {
            dArray[0] = d11;
            dArray[1] = d13 * d2 * d10 + d13 * d2 + d13 * d10 + d12;
        }
        return d14;
    }

    public static double min(double d, double d2) {
        if (d > d2) {
            return d2;
        }
        if (d < d2) {
            return d;
        }
        if (d != d2) {
            return Double.NaN;
        }
        long l = Double.doubleToRawLongBits(d);
        if (l == Long.MIN_VALUE) {
            return d;
        }
        return d2;
    }

    public static float abs(float f) {
        return Float.intBitsToFloat(Integer.MAX_VALUE & Float.floatToRawIntBits(f));
    }

    public static double cbrt(double d) {
        long l = Double.doubleToRawLongBits(d);
        int n = (int)(l >> 52 & 0x7FFL) - 1023;
        boolean bl = false;
        if (n == -1023) {
            if (d == 0.0) {
                return d;
            }
            bl = true;
            l = Double.doubleToRawLongBits(d *= 1.8014398509481984E16);
            n = (int)(l >> 52 & 0x7FFL) - 1023;
        }
        if (n == 1024) {
            return d;
        }
        int n2 = n / 3;
        double d2 = Double.longBitsToDouble(l & Long.MIN_VALUE | (long)(n2 + 1023 & 0x7FF) << 52);
        double d3 = Double.longBitsToDouble(l & 0xFFFFFFFFFFFFFL | 0x3FF0000000000000L);
        double d4 = -0.010714690733195933;
        d4 = d4 * d3 + 0.0875862700108075;
        d4 = d4 * d3 + -0.3058015757857271;
        d4 = d4 * d3 + 0.7249995199969751;
        d4 = d4 * d3 + 0.5039018405998233;
        d4 *= CBRTTWO[n % 3 + 2];
        double d5 = d / (d2 * d2 * d2);
        d4 += (d5 - d4 * d4 * d4) / (3.0 * d4 * d4);
        d4 += (d5 - d4 * d4 * d4) / (3.0 * d4 * d4);
        double d6 = d4 * 1.073741824E9;
        double d7 = d4 + d6 - d6;
        double d8 = d4 - d7;
        double d9 = d7 * d7;
        double d10 = d7 * d8 * 2.0 + d8 * d8;
        d6 = d9 * 1.073741824E9;
        double d11 = d9 + d6 - d6;
        d10 += d9 - d11;
        d9 = d11;
        d10 = d9 * d8 + d7 * d10 + d10 * d8;
        double d12 = d5 - (d9 *= d7);
        double d13 = -(d12 - d5 + d9);
        d4 += (d12 + (d13 -= d10)) / (3.0 * d4 * d4);
        d4 *= d2;
        if (bl) {
            d4 *= 3.814697265625E-6;
        }
        return d4;
    }

    public static float ulp(float f) {
        if (Float.isInfinite(f)) {
            return Float.POSITIVE_INFINITY;
        }
        return ApacheMath.abs(f - Float.intBitsToFloat(Float.floatToIntBits(f) ^ 1));
    }

    private static class lnMant {
        private static final double[][] LN_MANT = FastMathLiteralArrays.loadLnMant();

        private lnMant() {
        }

        static double[][] access$400() {
            return LN_MANT;
        }
    }

    private static class CodyWaite {
        private final double finalRemB;
        private final double finalRemA;
        private final int finalK;

        double getRemA() {
            return this.finalRemA;
        }

        double getRemB() {
            return this.finalRemB;
        }

        CodyWaite(double d) {
            double d2;
            double d3;
            int n = (int)(d * 0.6366197723675814);
            while (true) {
                double d4 = (double)(-n) * 1.570796251296997;
                d3 = d + d4;
                d2 = -(d3 - d - d4);
                d4 = (double)(-n) * 7.549789948768648E-8;
                double d5 = d3;
                d3 = d4 + d5;
                d2 += -(d3 - d5 - d4);
                d4 = (double)(-n) * 6.123233995736766E-17;
                d5 = d3;
                d3 = d4 + d5;
                d2 += -(d3 - d5 - d4);
                if (d3 > 0.0) break;
                --n;
            }
            this.finalK = n;
            this.finalRemA = d3;
            this.finalRemB = d2;
        }

        int getK() {
            return this.finalK;
        }
    }

    private static class Split {
        public static final Split POSITIVE_INFINITY;
        public static final Split NAN;
        private final double low;
        public static final Split NEGATIVE_INFINITY;
        private final double full;
        private final double high;

        Split(double d) {
            this.full = d;
            this.high = Double.longBitsToDouble(Double.doubleToRawLongBits(d) & 0xFFFFFFFFF8000000L);
            this.low = d - this.high;
        }

        public Split reciprocal() {
            double d = 1.0 / this.full;
            Split split = new Split(d);
            Split split2 = this.multiply(split);
            double d2 = split2.high - 1.0 + split2.low;
            return Double.isNaN(d2) ? split : new Split(split.high, split.low - d2 / this.full);
        }

        static {
            NAN = new Split(Double.NaN, 0.0);
            POSITIVE_INFINITY = new Split(Double.POSITIVE_INFINITY, 0.0);
            NEGATIVE_INFINITY = new Split(Double.NEGATIVE_INFINITY, 0.0);
        }

        public Split multiply(Split split) {
            Split split2 = new Split(this.full * split.full);
            double d = this.low * split.low - (split2.full - this.high * split.high - this.low * split.high - this.high * split.low);
            return new Split(split2.high, split2.low + d);
        }

        Split(double d, double d2) {
            this(d == 0.0 ? (d2 == 0.0 && Double.doubleToRawLongBits(d) == Long.MIN_VALUE ? -0.0 : d2) : d + d2, d, d2);
        }

        static Split access$500(Split split, long l) {
            return split.pow(l);
        }

        static double access$600(Split split) {
            return split.full;
        }

        private Split pow(long l) {
            Split split = new Split(1.0);
            Split split2 = new Split(this.full, this.high, this.low);
            for (long i = l; i != 0L; i >>>= 1) {
                if ((i & 1L) != 0L) {
                    split = split.multiply(split2);
                }
                split2 = split2.multiply(split2);
            }
            if (Double.isNaN(split.full)) {
                if (Double.isNaN(this.full)) {
                    return NAN;
                }
                if (ApacheMath.abs(this.full) < 1.0) {
                    return new Split(ApacheMath.copySign(0.0, this.full), 0.0);
                }
                if (this.full < 0.0 && (l & 1L) == 1L) {
                    return NEGATIVE_INFINITY;
                }
                return POSITIVE_INFINITY;
            }
            return split;
        }

        Split(double d, double d2, double d3) {
            this.full = d;
            this.high = d2;
            this.low = d3;
        }
    }

    private static class ExpFracTable {
        private static final double[] EXP_FRAC_TABLE_B;
        private static final double[] EXP_FRAC_TABLE_A;

        private ExpFracTable() {
        }

        static double[] access$300() {
            return EXP_FRAC_TABLE_B;
        }

        static {
            EXP_FRAC_TABLE_A = FastMathLiteralArrays.loadExpFracA();
            EXP_FRAC_TABLE_B = FastMathLiteralArrays.loadExpFracB();
        }

        static double[] access$200() {
            return EXP_FRAC_TABLE_A;
        }
    }

    private static class ExpIntTable {
        private static final double[] EXP_INT_TABLE_B;
        private static final double[] EXP_INT_TABLE_A;

        static double[] access$000() {
            return EXP_INT_TABLE_A;
        }

        static double[] access$100() {
            return EXP_INT_TABLE_B;
        }

        private ExpIntTable() {
        }

        static {
            EXP_INT_TABLE_A = FastMathLiteralArrays.loadExpIntA();
            EXP_INT_TABLE_B = FastMathLiteralArrays.loadExpIntB();
        }
    }
}

