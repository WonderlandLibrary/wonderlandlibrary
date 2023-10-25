/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Random;
import java.util.UUID;
import net.minecraft.util.Vec3i;

public class MathHelper {
    private static final double field_181163_d;
    public static final float SQRT_2;
    private static final double[] field_181164_e;
    private static final int[] multiplyDeBruijnBitPosition;
    private static final double[] field_181165_f;
    private static final float[] SIN_TABLE;

    public static int parseIntWithDefault(String string, int n) {
        try {
            return Integer.parseInt(string);
        }
        catch (Throwable throwable) {
            return n;
        }
    }

    public static int truncateDoubleToInt(double d) {
        return (int)(d + 1024.0) - 1024;
    }

    private static int calculateLogBaseTwoDeBruijn(int n) {
        n = MathHelper.isPowerOfTwo(n) ? n : MathHelper.roundUpToPowerOfTwo(n);
        return multiplyDeBruijnBitPosition[(int)((long)n * 125613361L >> 27) & 0x1F];
    }

    public static int ceiling_double_int(double d) {
        int n = (int)d;
        return d > (double)n ? n + 1 : n;
    }

    public static UUID getRandomUuid(Random random) {
        long l = random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
        long l2 = random.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
        return new UUID(l, l2);
    }

    public static double clamp_double(double d, double d2, double d3) {
        return d < d2 ? d2 : (d > d3 ? d3 : d);
    }

    public static int bucketInt(int n, int n2) {
        return n < 0 ? -((-n - 1) / n2) - 1 : n / n2;
    }

    public static int func_180188_d(int n, int n2) {
        int n3 = (n & 0xFF0000) >> 16;
        int n4 = (n2 & 0xFF0000) >> 16;
        int n5 = (n & 0xFF00) >> 8;
        int n6 = (n2 & 0xFF00) >> 8;
        int n7 = (n & 0xFF) >> 0;
        int n8 = (n2 & 0xFF) >> 0;
        int n9 = (int)((float)n3 * (float)n4 / 255.0f);
        int n10 = (int)((float)n5 * (float)n6 / 255.0f);
        int n11 = (int)((float)n7 * (float)n8 / 255.0f);
        return n & 0xFF000000 | n9 << 16 | n10 << 8 | n11;
    }

    public static int normalizeAngle(int n, int n2) {
        return (n % n2 + n2) % n2;
    }

    public static float sin(float f) {
        return SIN_TABLE[(int)(f * 10430.378f) & 0xFFFF];
    }

    public static int roundUpToPowerOfTwo(int n) {
        int n2 = n - 1;
        n2 |= n2 >> 1;
        n2 |= n2 >> 2;
        n2 |= n2 >> 4;
        n2 |= n2 >> 8;
        n2 |= n2 >> 16;
        return n2 + 1;
    }

    public static int clamp_int(int n, int n2, int n3) {
        return n < n2 ? n2 : (n > n3 ? n3 : n);
    }

    public static int floor_float(float f) {
        int n = (int)f;
        return f < (float)n ? n - 1 : n;
    }

    public static double abs_max(double d, double d2) {
        if (d < 0.0) {
            d = -d;
        }
        if (d2 < 0.0) {
            d2 = -d2;
        }
        return d > d2 ? d : d2;
    }

    public static int ceiling_float_int(float f) {
        int n = (int)f;
        return f > (float)n ? n + 1 : n;
    }

    private static boolean isPowerOfTwo(int n) {
        return n != 0 && (n & n - 1) == 0;
    }

    public static double average(long[] lArray) {
        long l = 0L;
        long[] lArray2 = lArray;
        int n = lArray.length;
        int n2 = 0;
        while (n2 < n) {
            long l2 = lArray2[n2];
            l += l2;
            ++n2;
        }
        return (double)l / (double)lArray.length;
    }

    public static float abs(float f) {
        return f >= 0.0f ? f : -f;
    }

    public static int calculateLogBaseTwo(int n) {
        return MathHelper.calculateLogBaseTwoDeBruijn(n) - (MathHelper.isPowerOfTwo(n) ? 0 : 1);
    }

    public static double parseDoubleWithDefault(String string, double d) {
        try {
            return Double.parseDouble(string);
        }
        catch (Throwable throwable) {
            return d;
        }
    }

    public static double getRandomDoubleInRange(Random random, double d, double d2) {
        return d >= d2 ? d : random.nextDouble() * (d2 - d) + d;
    }

    static {
        SQRT_2 = MathHelper.sqrt_float(2.0f);
        SIN_TABLE = new float[65536];
        int n = 0;
        while (n < 65536) {
            MathHelper.SIN_TABLE[n] = (float)Math.sin((double)n * Math.PI * 2.0 / 65536.0);
            ++n;
        }
        int[] nArray = new int[32];
        nArray[1] = 1;
        nArray[2] = 28;
        nArray[3] = 2;
        nArray[4] = 29;
        nArray[5] = 14;
        nArray[6] = 24;
        nArray[7] = 3;
        nArray[8] = 30;
        nArray[9] = 22;
        nArray[10] = 20;
        nArray[11] = 15;
        nArray[12] = 25;
        nArray[13] = 17;
        nArray[14] = 4;
        nArray[15] = 8;
        nArray[16] = 31;
        nArray[17] = 27;
        nArray[18] = 13;
        nArray[19] = 23;
        nArray[20] = 21;
        nArray[21] = 19;
        nArray[22] = 16;
        nArray[23] = 7;
        nArray[24] = 26;
        nArray[25] = 12;
        nArray[26] = 18;
        nArray[27] = 6;
        nArray[28] = 11;
        nArray[29] = 5;
        nArray[30] = 10;
        nArray[31] = 9;
        multiplyDeBruijnBitPosition = nArray;
        field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        field_181164_e = new double[257];
        field_181165_f = new double[257];
        n = 0;
        while (n < 257) {
            double d = (double)n / 256.0;
            double d2 = Math.asin(d);
            MathHelper.field_181165_f[n] = Math.cos(d2);
            MathHelper.field_181164_e[n] = d2;
            ++n;
        }
    }

    public static double wrapAngleTo180_double(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }

    public static int floor_double(double d) {
        int n = (int)d;
        return d < (double)n ? n - 1 : n;
    }

    public static float sqrt_double(double d) {
        return (float)Math.sqrt(d);
    }

    public static long getCoordinateRandom(int n, int n2, int n3) {
        long l = (long)(n * 3129871) ^ (long)n3 * 116129781L ^ (long)n2;
        l = l * l * 42317861L + l * 11L;
        return l;
    }

    public static float sqrt_float(float f) {
        return (float)Math.sqrt(f);
    }

    public static double func_181160_c(double d, double d2, double d3) {
        return (d - d2) / (d3 - d2);
    }

    public static int parseIntWithDefaultAndMax(String string, int n, int n2) {
        return Math.max(n2, MathHelper.parseIntWithDefault(string, n));
    }

    public static double func_181159_b(double d, double d2) {
        double d3;
        boolean bl;
        boolean bl2;
        boolean bl3;
        double d4 = d2 * d2 + d * d;
        if (Double.isNaN(d4)) {
            return Double.NaN;
        }
        boolean bl4 = bl3 = d < 0.0;
        if (bl3) {
            d = -d;
        }
        boolean bl5 = bl2 = d2 < 0.0;
        if (bl2) {
            d2 = -d2;
        }
        boolean bl6 = bl = d > d2;
        if (bl) {
            d3 = d2;
            d2 = d;
            d = d3;
        }
        d3 = MathHelper.func_181161_i(d4);
        d2 *= d3;
        double d5 = field_181163_d + (d *= d3);
        int n = (int)Double.doubleToRawLongBits(d5);
        double d6 = field_181164_e[n];
        double d7 = field_181165_f[n];
        double d8 = d5 - field_181163_d;
        double d9 = d * d7 - d2 * d8;
        double d10 = (6.0 + d9 * d9) * d9 * 0.16666666666666666;
        double d11 = d6 + d10;
        if (bl) {
            d11 = 1.5707963267948966 - d11;
        }
        if (bl2) {
            d11 = Math.PI - d11;
        }
        if (bl3) {
            d11 = -d11;
        }
        return d11;
    }

    public static float clamp_float(float f, float f2, float f3) {
        return f < f2 ? f2 : (f > f3 ? f3 : f);
    }

    public static double denormalizeClamp(double d, double d2, double d3) {
        return d3 < 0.0 ? d : (d3 > 1.0 ? d2 : d + (d2 - d) * d3);
    }

    public static int func_154354_b(int n, int n2) {
        int n3;
        if (n2 == 0) {
            return 0;
        }
        if (n == 0) {
            return n2;
        }
        if (n < 0) {
            n2 *= -1;
        }
        return (n3 = n % n2) == 0 ? n : n + n2 - n3;
    }

    public static int abs_int(int n) {
        return n >= 0 ? n : -n;
    }

    public static int func_180183_b(float f, float f2, float f3) {
        return MathHelper.func_180181_b(MathHelper.floor_float(f * 255.0f), MathHelper.floor_float(f2 * 255.0f), MathHelper.floor_float(f3 * 255.0f));
    }

    public static float cos(float f) {
        return SIN_TABLE[(int)(f * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static int func_154353_e(double d) {
        return (int)(d >= 0.0 ? d : -d + 1.0);
    }

    public static int func_180181_b(int n, int n2, int n3) {
        int n4 = (n << 8) + n2;
        n4 = (n4 << 8) + n3;
        return n4;
    }

    public static long floor_double_long(double d) {
        long l = (long)d;
        return d < (double)l ? l - 1L : l;
    }

    public static boolean epsilonEquals(float f, float f2) {
        return MathHelper.abs(f2 - f) < 1.0E-5f;
    }

    public static double parseDoubleWithDefaultAndMax(String string, double d, double d2) {
        return Math.max(d2, MathHelper.parseDoubleWithDefault(string, d));
    }

    public static float randomFloatClamp(Random random, float f, float f2) {
        return f >= f2 ? f : random.nextFloat() * (f2 - f) + f;
    }

    public static long getPositionRandom(Vec3i vec3i) {
        return MathHelper.getCoordinateRandom(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static float wrapAngleTo180_float(float f) {
        if ((f %= 360.0f) >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static double func_181161_i(double d) {
        double d2 = 0.5 * d;
        long l = Double.doubleToRawLongBits(d);
        l = 6910469410427058090L - (l >> 1);
        d = Double.longBitsToDouble(l);
        d *= 1.5 - d2 * d * d;
        return d;
    }

    public static double func_181162_h(double d) {
        return d - Math.floor(d);
    }

    public static int getRandomIntegerInRange(Random random, int n, int n2) {
        return n >= n2 ? n : random.nextInt(n2 - n + 1) + n;
    }

    public static int func_181758_c(float f, float f2, float f3) {
        float f4;
        float f5;
        float f6;
        int n = (int)(f * 6.0f) % 6;
        float f7 = f * 6.0f - (float)n;
        float f8 = f3 * (1.0f - f2);
        float f9 = f3 * (1.0f - f7 * f2);
        float f10 = f3 * (1.0f - (1.0f - f7) * f2);
        switch (n) {
            case 0: {
                f6 = f3;
                f5 = f10;
                f4 = f8;
                break;
            }
            case 1: {
                f6 = f9;
                f5 = f3;
                f4 = f8;
                break;
            }
            case 2: {
                f6 = f8;
                f5 = f3;
                f4 = f10;
                break;
            }
            case 3: {
                f6 = f8;
                f5 = f9;
                f4 = f3;
                break;
            }
            case 4: {
                f6 = f10;
                f5 = f8;
                f4 = f3;
                break;
            }
            case 5: {
                f6 = f3;
                f5 = f8;
                f4 = f9;
                break;
            }
            default: {
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + f + ", " + f2 + ", " + f3);
            }
        }
        int n2 = MathHelper.clamp_int((int)(f6 * 255.0f), 0, 255);
        int n3 = MathHelper.clamp_int((int)(f5 * 255.0f), 0, 255);
        int n4 = MathHelper.clamp_int((int)(f4 * 255.0f), 0, 255);
        return n2 << 16 | n3 << 8 | n4;
    }

    public static float wrapAngleTo360_float(float f) {
        if ((f %= 180.0f) >= 360.0f) {
            f -= 180.0f;
        }
        if (f < -360.0f) {
            f += 180.0f;
        }
        return f;
    }
}

