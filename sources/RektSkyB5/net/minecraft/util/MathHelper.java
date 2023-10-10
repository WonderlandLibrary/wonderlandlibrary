/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Random;
import java.util.UUID;
import net.minecraft.util.Vec3i;

public class MathHelper {
    public static final float SQRT_2 = MathHelper.sqrt_float(2.0f);
    private static final int SIN_BITS = 12;
    private static final int SIN_MASK = 4095;
    private static final int SIN_COUNT = 4096;
    public static final float PI = (float)Math.PI;
    public static final float PI2 = (float)Math.PI * 2;
    public static final float PId2 = 1.5707964f;
    private static final float radFull = (float)Math.PI * 2;
    private static final float degFull = 360.0f;
    private static final float radToIndex = 651.8986f;
    private static final float degToIndex = 11.377778f;
    public static final float deg2Rad = (float)Math.PI / 180;
    private static final float[] SIN_TABLE_FAST = new float[4096];
    public static boolean fastMath = false;
    private static final float[] SIN_TABLE = new float[65536];
    private static final int[] multiplyDeBruijnBitPosition;
    private static final double field_181163_d;
    private static final double[] field_181164_e;
    private static final double[] field_181165_f;
    private static final String __OBFID = "CL_00001496";

    public static float sin(float p_76126_0_) {
        return fastMath ? SIN_TABLE_FAST[(int)(p_76126_0_ * 651.8986f) & 0xFFF] : SIN_TABLE[(int)(p_76126_0_ * 10430.378f) & 0xFFFF];
    }

    public static float cos(float value) {
        return fastMath ? SIN_TABLE_FAST[(int)((value + 1.5707964f) * 651.8986f) & 0xFFF] : SIN_TABLE[(int)(value * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static float sqrt_float(float value) {
        return (float)Math.sqrt(value);
    }

    public static float sqrt_double(double value) {
        return (float)Math.sqrt(value);
    }

    public static int floor_float(float value) {
        int i2 = (int)value;
        return value < (float)i2 ? i2 - 1 : i2;
    }

    public static int truncateDoubleToInt(double value) {
        return (int)(value + 1024.0) - 1024;
    }

    public static int floor_double(double value) {
        int i2 = (int)value;
        return value < (double)i2 ? i2 - 1 : i2;
    }

    public static long floor_double_long(double value) {
        long i2 = (long)value;
        return value < (double)i2 ? i2 - 1L : i2;
    }

    public static int func_154353_e(double value) {
        return (int)(value >= 0.0 ? value : -value + 1.0);
    }

    public static float abs(float value) {
        return value >= 0.0f ? value : -value;
    }

    public static int abs_int(int value) {
        return value >= 0 ? value : -value;
    }

    public static int ceiling_float_int(float value) {
        int i2 = (int)value;
        return value > (float)i2 ? i2 + 1 : i2;
    }

    public static int ceiling_double_int(double value) {
        int i2 = (int)value;
        return value > (double)i2 ? i2 + 1 : i2;
    }

    public static int clamp_int(int num, int min, int max) {
        return num < min ? min : (num > max ? max : num);
    }

    public static float clamp_float(float num, float min, float max) {
        return num < min ? min : (num > max ? max : num);
    }

    public static double clamp_double(double num, double min, double max) {
        return num < min ? min : (num > max ? max : num);
    }

    public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_) {
        return p_151238_4_ < 0.0 ? p_151238_0_ : (p_151238_4_ > 1.0 ? p_151238_2_ : p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_);
    }

    public static double abs_max(double p_76132_0_, double p_76132_2_) {
        if (p_76132_0_ < 0.0) {
            p_76132_0_ = -p_76132_0_;
        }
        if (p_76132_2_ < 0.0) {
            p_76132_2_ = -p_76132_2_;
        }
        return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
    }

    public static int bucketInt(int p_76137_0_, int p_76137_1_) {
        return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
    }

    public static int getRandomIntegerInRange(Random p_76136_0_, int p_76136_1_, int p_76136_2_) {
        return p_76136_1_ >= p_76136_2_ ? p_76136_1_ : p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_;
    }

    public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_) {
        return p_151240_1_ >= p_151240_2_ ? p_151240_1_ : p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
    }

    public static double getRandomDoubleInRange(Random p_82716_0_, double p_82716_1_, double p_82716_3_) {
        return p_82716_1_ >= p_82716_3_ ? p_82716_1_ : p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_;
    }

    public static double average(long[] values) {
        long i2 = 0L;
        for (long j2 : values) {
            i2 += j2;
        }
        return (double)i2 / (double)values.length;
    }

    public static boolean epsilonEquals(float p_180185_0_, float p_180185_1_) {
        return MathHelper.abs(p_180185_1_ - p_180185_0_) < 1.0E-5f;
    }

    public static int normalizeAngle(int p_180184_0_, int p_180184_1_) {
        return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
    }

    public static float wrapAngleTo180_float(float value) {
        if ((value %= 360.0f) >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    public static double wrapAngleTo180_double(double value) {
        if ((value %= 360.0) >= 180.0) {
            value -= 360.0;
        }
        if (value < -180.0) {
            value += 360.0;
        }
        return value;
    }

    public static int parseIntWithDefault(String p_82715_0_, int p_82715_1_) {
        try {
            return Integer.parseInt(p_82715_0_);
        }
        catch (Throwable var3) {
            return p_82715_1_;
        }
    }

    public static int parseIntWithDefaultAndMax(String p_82714_0_, int p_82714_1_, int p_82714_2_) {
        return Math.max(p_82714_2_, MathHelper.parseIntWithDefault(p_82714_0_, p_82714_1_));
    }

    public static double parseDoubleWithDefault(String p_82712_0_, double p_82712_1_) {
        try {
            return Double.parseDouble(p_82712_0_);
        }
        catch (Throwable var4) {
            return p_82712_1_;
        }
    }

    public static double parseDoubleWithDefaultAndMax(String p_82713_0_, double p_82713_1_, double p_82713_3_) {
        return Math.max(p_82713_3_, MathHelper.parseDoubleWithDefault(p_82713_0_, p_82713_1_));
    }

    public static int roundUpToPowerOfTwo(int value) {
        int i2 = value - 1;
        i2 |= i2 >> 1;
        i2 |= i2 >> 2;
        i2 |= i2 >> 4;
        i2 |= i2 >> 8;
        i2 |= i2 >> 16;
        return i2 + 1;
    }

    private static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    private static int calculateLogBaseTwoDeBruijn(int value) {
        value = MathHelper.isPowerOfTwo(value) ? value : MathHelper.roundUpToPowerOfTwo(value);
        return multiplyDeBruijnBitPosition[(int)((long)value * 125613361L >> 27) & 0x1F];
    }

    public static int calculateLogBaseTwo(int value) {
        return MathHelper.calculateLogBaseTwoDeBruijn(value) - (MathHelper.isPowerOfTwo(value) ? 0 : 1);
    }

    public static int func_154354_b(int p_154354_0_, int p_154354_1_) {
        int i2;
        if (p_154354_1_ == 0) {
            return 0;
        }
        if (p_154354_0_ == 0) {
            return p_154354_1_;
        }
        if (p_154354_0_ < 0) {
            p_154354_1_ *= -1;
        }
        return (i2 = p_154354_0_ % p_154354_1_) == 0 ? p_154354_0_ : p_154354_0_ + p_154354_1_ - i2;
    }

    public static int func_180183_b(float p_180183_0_, float p_180183_1_, float p_180183_2_) {
        return MathHelper.func_180181_b(MathHelper.floor_float(p_180183_0_ * 255.0f), MathHelper.floor_float(p_180183_1_ * 255.0f), MathHelper.floor_float(p_180183_2_ * 255.0f));
    }

    public static int func_180181_b(int p_180181_0_, int p_180181_1_, int p_180181_2_) {
        int i2 = (p_180181_0_ << 8) + p_180181_1_;
        i2 = (i2 << 8) + p_180181_2_;
        return i2;
    }

    public static int func_180188_d(int p_180188_0_, int p_180188_1_) {
        int i2 = (p_180188_0_ & 0xFF0000) >> 16;
        int j2 = (p_180188_1_ & 0xFF0000) >> 16;
        int k2 = (p_180188_0_ & 0xFF00) >> 8;
        int l2 = (p_180188_1_ & 0xFF00) >> 8;
        int i1 = (p_180188_0_ & 0xFF) >> 0;
        int j1 = (p_180188_1_ & 0xFF) >> 0;
        int k1 = (int)((float)i2 * (float)j2 / 255.0f);
        int l1 = (int)((float)k2 * (float)l2 / 255.0f);
        int i22 = (int)((float)i1 * (float)j1 / 255.0f);
        return p_180188_0_ & 0xFF000000 | k1 << 16 | l1 << 8 | i22;
    }

    public static double func_181162_h(double p_181162_0_) {
        return p_181162_0_ - Math.floor(p_181162_0_);
    }

    public static long getPositionRandom(Vec3i pos) {
        return MathHelper.getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
    }

    public static long getCoordinateRandom(int x2, int y2, int z2) {
        long i2 = (long)(x2 * 3129871) ^ (long)z2 * 116129781L ^ (long)y2;
        i2 = i2 * i2 * 42317861L + i2 * 11L;
        return i2;
    }

    public static UUID getRandomUuid(Random rand) {
        long i2 = rand.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
        long j2 = rand.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
        return new UUID(i2, j2);
    }

    public static double func_181160_c(double p_181160_0_, double p_181160_2_, double p_181160_4_) {
        return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
    }

    public static double func_181159_b(double p_181159_0_, double p_181159_2_) {
        boolean flag2;
        boolean flag1;
        boolean flag;
        double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
        if (Double.isNaN(d0)) {
            return Double.NaN;
        }
        boolean bl = flag = p_181159_0_ < 0.0;
        if (flag) {
            p_181159_0_ = -p_181159_0_;
        }
        boolean bl2 = flag1 = p_181159_2_ < 0.0;
        if (flag1) {
            p_181159_2_ = -p_181159_2_;
        }
        boolean bl3 = flag2 = p_181159_0_ > p_181159_2_;
        if (flag2) {
            double d1 = p_181159_2_;
            p_181159_2_ = p_181159_0_;
            p_181159_0_ = d1;
        }
        double d9 = MathHelper.func_181161_i(d0);
        p_181159_2_ *= d9;
        double d2 = field_181163_d + (p_181159_0_ *= d9);
        int i2 = (int)Double.doubleToRawLongBits(d2);
        double d3 = field_181164_e[i2];
        double d4 = field_181165_f[i2];
        double d5 = d2 - field_181163_d;
        double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
        double d7 = (6.0 + d6 * d6) * d6 * 0.16666666666666666;
        double d8 = d3 + d7;
        if (flag2) {
            d8 = 1.5707963267948966 - d8;
        }
        if (flag1) {
            d8 = Math.PI - d8;
        }
        if (flag) {
            d8 = -d8;
        }
        return d8;
    }

    public static double func_181161_i(double p_181161_0_) {
        double d0 = 0.5 * p_181161_0_;
        long i2 = Double.doubleToRawLongBits(p_181161_0_);
        i2 = 6910469410427058090L - (i2 >> 1);
        p_181161_0_ = Double.longBitsToDouble(i2);
        p_181161_0_ *= 1.5 - d0 * p_181161_0_ * p_181161_0_;
        return p_181161_0_;
    }

    public static int func_181758_c(float p_181758_0_, float p_181758_1_, float p_181758_2_) {
        float f6;
        float f5;
        float f4;
        int i2 = (int)(p_181758_0_ * 6.0f) % 6;
        float f2 = p_181758_0_ * 6.0f - (float)i2;
        float f1 = p_181758_2_ * (1.0f - p_181758_1_);
        float f22 = p_181758_2_ * (1.0f - f2 * p_181758_1_);
        float f3 = p_181758_2_ * (1.0f - (1.0f - f2) * p_181758_1_);
        switch (i2) {
            case 0: {
                f4 = p_181758_2_;
                f5 = f3;
                f6 = f1;
                break;
            }
            case 1: {
                f4 = f22;
                f5 = p_181758_2_;
                f6 = f1;
                break;
            }
            case 2: {
                f4 = f1;
                f5 = p_181758_2_;
                f6 = f3;
                break;
            }
            case 3: {
                f4 = f1;
                f5 = f22;
                f6 = p_181758_2_;
                break;
            }
            case 4: {
                f4 = f3;
                f5 = f1;
                f6 = p_181758_2_;
                break;
            }
            case 5: {
                f4 = p_181758_2_;
                f5 = f1;
                f6 = f22;
                break;
            }
            default: {
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + p_181758_0_ + ", " + p_181758_1_ + ", " + p_181758_2_);
            }
        }
        int j2 = MathHelper.clamp_int((int)(f4 * 255.0f), 0, 255);
        int k2 = MathHelper.clamp_int((int)(f5 * 255.0f), 0, 255);
        int l2 = MathHelper.clamp_int((int)(f6 * 255.0f), 0, 255);
        return j2 << 16 | k2 << 8 | l2;
    }

    static {
        for (int i2 = 0; i2 < 65536; ++i2) {
            MathHelper.SIN_TABLE[i2] = (float)Math.sin((double)i2 * Math.PI * 2.0 / 65536.0);
        }
        for (int j2 = 0; j2 < 4096; ++j2) {
            MathHelper.SIN_TABLE_FAST[j2] = (float)Math.sin(((float)j2 + 0.5f) / 4096.0f * ((float)Math.PI * 2));
        }
        for (int l2 = 0; l2 < 360; l2 += 90) {
            MathHelper.SIN_TABLE_FAST[(int)((float)l2 * 11.377778f) & 0xFFF] = (float)Math.sin((float)l2 * ((float)Math.PI / 180));
        }
        multiplyDeBruijnBitPosition = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        field_181164_e = new double[257];
        field_181165_f = new double[257];
        for (int k2 = 0; k2 < 257; ++k2) {
            double d1 = (double)k2 / 256.0;
            double d0 = Math.asin(d1);
            MathHelper.field_181165_f[k2] = Math.cos(d0);
            MathHelper.field_181164_e[k2] = d0;
        }
    }
}

