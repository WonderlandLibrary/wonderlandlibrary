// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.UUID;
import java.util.Random;

public class MathHelper
{
    public static final float field_180189_a;
    private static final int SIN_BITS = 12;
    private static final int SIN_MASK = 4095;
    private static final int SIN_COUNT = 4096;
    public static final float PI = 3.1415927f;
    public static final float PI2 = 6.2831855f;
    public static final float PId2 = 1.5707964f;
    private static final float radFull = 6.2831855f;
    private static final float degFull = 360.0f;
    private static final float radToIndex = 651.8986f;
    private static final float degToIndex = 11.377778f;
    public static final float deg2Rad = 0.017453292f;
    private static final float[] SIN_TABLE_FAST;
    public static boolean fastMath;
    private static final float[] SIN_TABLE;
    private static final int[] multiplyDeBruijnBitPosition;
    private static final String __OBFID = "CL_00001496";
    
    public static float sin(final float p_76126_0_) {
        return MathHelper.fastMath ? MathHelper.SIN_TABLE_FAST[(int)(p_76126_0_ * 651.8986f) & 0xFFF] : MathHelper.SIN_TABLE[(int)(p_76126_0_ * 10430.378f) & 0xFFFF];
    }
    
    public static float cos(final float p_76134_0_) {
        return MathHelper.fastMath ? MathHelper.SIN_TABLE_FAST[(int)((p_76134_0_ + 1.5707964f) * 651.8986f) & 0xFFF] : MathHelper.SIN_TABLE[(int)(p_76134_0_ * 10430.378f + 16384.0f) & 0xFFFF];
    }
    
    public static float sqrt_float(final float p_76129_0_) {
        return (float)Math.sqrt(p_76129_0_);
    }
    
    public static float sqrt_double(final double p_76133_0_) {
        return (float)Math.sqrt(p_76133_0_);
    }
    
    public static int floor_float(final float p_76141_0_) {
        final int var1 = (int)p_76141_0_;
        return (p_76141_0_ < var1) ? (var1 - 1) : var1;
    }
    
    public static int truncateDoubleToInt(final double p_76140_0_) {
        return (int)(p_76140_0_ + 1024.0) - 1024;
    }
    
    public static int floor_double(final double p_76128_0_) {
        final int var2 = (int)p_76128_0_;
        return (p_76128_0_ < var2) ? (var2 - 1) : var2;
    }
    
    public static long floor_double_long(final double p_76124_0_) {
        final long var2 = (long)p_76124_0_;
        return (p_76124_0_ < var2) ? (var2 - 1L) : var2;
    }
    
    public static int func_154353_e(final double p_154353_0_) {
        return (int)((p_154353_0_ >= 0.0) ? p_154353_0_ : (-p_154353_0_ + 1.0));
    }
    
    public static float abs(final float p_76135_0_) {
        return (p_76135_0_ >= 0.0f) ? p_76135_0_ : (-p_76135_0_);
    }
    
    public static int abs_int(final int p_76130_0_) {
        return (p_76130_0_ >= 0) ? p_76130_0_ : (-p_76130_0_);
    }
    
    public static int ceiling_float_int(final float p_76123_0_) {
        final int var1 = (int)p_76123_0_;
        return (p_76123_0_ > var1) ? (var1 + 1) : var1;
    }
    
    public static int ceiling_double_int(final double p_76143_0_) {
        final int var2 = (int)p_76143_0_;
        return (p_76143_0_ > var2) ? (var2 + 1) : var2;
    }
    
    public static int clamp_int(final int p_76125_0_, final int p_76125_1_, final int p_76125_2_) {
        return (p_76125_0_ < p_76125_1_) ? p_76125_1_ : ((p_76125_0_ > p_76125_2_) ? p_76125_2_ : p_76125_0_);
    }
    
    public static float clamp_float(final float p_76131_0_, final float p_76131_1_, final float p_76131_2_) {
        return (p_76131_0_ < p_76131_1_) ? p_76131_1_ : ((p_76131_0_ > p_76131_2_) ? p_76131_2_ : p_76131_0_);
    }
    
    public static double clamp_double(final double p_151237_0_, final double p_151237_2_, final double p_151237_4_) {
        return (p_151237_0_ < p_151237_2_) ? p_151237_2_ : ((p_151237_0_ > p_151237_4_) ? p_151237_4_ : p_151237_0_);
    }
    
    public static double denormalizeClamp(final double p_151238_0_, final double p_151238_2_, final double p_151238_4_) {
        return (p_151238_4_ < 0.0) ? p_151238_0_ : ((p_151238_4_ > 1.0) ? p_151238_2_ : (p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_));
    }
    
    public static double abs_max(double p_76132_0_, double p_76132_2_) {
        if (p_76132_0_ < 0.0) {
            p_76132_0_ = -p_76132_0_;
        }
        if (p_76132_2_ < 0.0) {
            p_76132_2_ = -p_76132_2_;
        }
        return (p_76132_0_ > p_76132_2_) ? p_76132_0_ : p_76132_2_;
    }
    
    public static int bucketInt(final int p_76137_0_, final int p_76137_1_) {
        return (p_76137_0_ < 0) ? (-((-p_76137_0_ - 1) / p_76137_1_) - 1) : (p_76137_0_ / p_76137_1_);
    }
    
    public static int getRandomIntegerInRange(final Random p_76136_0_, final int p_76136_1_, final int p_76136_2_) {
        return (p_76136_1_ >= p_76136_2_) ? p_76136_1_ : (p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_);
    }
    
    public static float randomFloatClamp(final Random p_151240_0_, final float p_151240_1_, final float p_151240_2_) {
        return (p_151240_1_ >= p_151240_2_) ? p_151240_1_ : (p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_);
    }
    
    public static double getRandomDoubleInRange(final Random p_82716_0_, final double p_82716_1_, final double p_82716_3_) {
        return (p_82716_1_ >= p_82716_3_) ? p_82716_1_ : (p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_);
    }
    
    public static double average(final long[] p_76127_0_) {
        long var1 = 0L;
        for (final long var4 : p_76127_0_) {
            var1 += var4;
        }
        return var1 / p_76127_0_.length;
    }
    
    public static boolean func_180185_a(final float p_180185_0_, final float p_180185_1_) {
        return abs(p_180185_1_ - p_180185_0_) < 1.0E-5f;
    }
    
    public static int func_180184_b(final int p_180184_0_, final int p_180184_1_) {
        return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
    }
    
    public static float wrapAngleTo180_float(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public static float wrapAngleTo90_float(float angle) {
        angle %= 180.0f;
        if (angle >= 90.0f) {
            angle -= 180.0f;
        }
        if (angle < -90.0f) {
            angle += 180.0f;
        }
        return angle;
    }
    
    public static double wrapAngleTo180_double(double p_76138_0_) {
        p_76138_0_ %= 360.0;
        if (p_76138_0_ >= 180.0) {
            p_76138_0_ -= 360.0;
        }
        if (p_76138_0_ < -180.0) {
            p_76138_0_ += 360.0;
        }
        return p_76138_0_;
    }
    
    public static int parseIntWithDefault(final String p_82715_0_, final int p_82715_1_) {
        try {
            return Integer.parseInt(p_82715_0_);
        }
        catch (Throwable var3) {
            return p_82715_1_;
        }
    }
    
    public static int parseIntWithDefaultAndMax(final String p_82714_0_, final int p_82714_1_, final int p_82714_2_) {
        return Math.max(p_82714_2_, parseIntWithDefault(p_82714_0_, p_82714_1_));
    }
    
    public static double parseDoubleWithDefault(final String p_82712_0_, final double p_82712_1_) {
        try {
            return Double.parseDouble(p_82712_0_);
        }
        catch (Throwable var4) {
            return p_82712_1_;
        }
    }
    
    public static double parseDoubleWithDefaultAndMax(final String p_82713_0_, final double p_82713_1_, final double p_82713_3_) {
        return Math.max(p_82713_3_, parseDoubleWithDefault(p_82713_0_, p_82713_1_));
    }
    
    public static int roundUpToPowerOfTwo(final int p_151236_0_) {
        int var1 = p_151236_0_ - 1;
        var1 |= var1 >> 1;
        var1 |= var1 >> 2;
        var1 |= var1 >> 4;
        var1 |= var1 >> 8;
        var1 |= var1 >> 16;
        return var1 + 1;
    }
    
    private static boolean isPowerOfTwo(final int p_151235_0_) {
        return p_151235_0_ != 0 && (p_151235_0_ & p_151235_0_ - 1) == 0x0;
    }
    
    private static int calculateLogBaseTwoDeBruijn(int p_151241_0_) {
        p_151241_0_ = (isPowerOfTwo(p_151241_0_) ? p_151241_0_ : roundUpToPowerOfTwo(p_151241_0_));
        return MathHelper.multiplyDeBruijnBitPosition[(int)(p_151241_0_ * 125613361L >> 27) & 0x1F];
    }
    
    public static int calculateLogBaseTwo(final int p_151239_0_) {
        return calculateLogBaseTwoDeBruijn(p_151239_0_) - (isPowerOfTwo(p_151239_0_) ? 0 : 1);
    }
    
    public static int func_154354_b(final int p_154354_0_, int p_154354_1_) {
        if (p_154354_1_ == 0) {
            return 0;
        }
        if (p_154354_0_ == 0) {
            return p_154354_1_;
        }
        if (p_154354_0_ < 0) {
            p_154354_1_ *= -1;
        }
        final int var2 = p_154354_0_ % p_154354_1_;
        return (var2 == 0) ? p_154354_0_ : (p_154354_0_ + p_154354_1_ - var2);
    }
    
    public static int func_180183_b(final float p_180183_0_, final float p_180183_1_, final float p_180183_2_) {
        return func_180181_b(floor_float(p_180183_0_ * 255.0f), floor_float(p_180183_1_ * 255.0f), floor_float(p_180183_2_ * 255.0f));
    }
    
    public static int func_180181_b(final int p_180181_0_, final int p_180181_1_, final int p_180181_2_) {
        int var3 = (p_180181_0_ << 8) + p_180181_1_;
        var3 = (var3 << 8) + p_180181_2_;
        return var3;
    }
    
    public static int func_180188_d(final int p_180188_0_, final int p_180188_1_) {
        final int var2 = (p_180188_0_ & 0xFF0000) >> 16;
        final int var3 = (p_180188_1_ & 0xFF0000) >> 16;
        final int var4 = (p_180188_0_ & 0xFF00) >> 8;
        final int var5 = (p_180188_1_ & 0xFF00) >> 8;
        final int var6 = p_180188_0_ & 0xFF;
        final int var7 = p_180188_1_ & 0xFF;
        final int var8 = (int)(var2 * var3 / 255.0f);
        final int var9 = (int)(var4 * var5 / 255.0f);
        final int var10 = (int)(var6 * var7 / 255.0f);
        return (p_180188_0_ & 0xFF000000) | var8 << 16 | var9 << 8 | var10;
    }
    
    public static long func_180186_a(final Vec3i pos) {
        return func_180187_c(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static long func_180187_c(final int x, final int y, final int z) {
        long var3 = x * 3129871 ^ z * 116129781L ^ y;
        var3 = var3 * var3 * 42317861L + var3 * 11L;
        return var3;
    }
    
    public static UUID func_180182_a(final Random p_180182_0_) {
        final long var1 = (p_180182_0_.nextLong() & 0xFFFFFFFFFFFF0FFFL) | 0x4000L;
        final long var2 = (p_180182_0_.nextLong() & 0x3FFFFFFFFFFFFFFFL) | Long.MIN_VALUE;
        return new UUID(var1, var2);
    }
    
    static {
        field_180189_a = sqrt_float(2.0f);
        SIN_TABLE_FAST = new float[4096];
        MathHelper.fastMath = false;
        SIN_TABLE = new float[65536];
        for (int i = 0; i < 65536; ++i) {
            MathHelper.SIN_TABLE[i] = (float)Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
        multiplyDeBruijnBitPosition = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
        for (int i = 0; i < 4096; ++i) {
            MathHelper.SIN_TABLE_FAST[i] = (float)Math.sin((i + 0.5f) / 4096.0f * 6.2831855f);
        }
        for (int i = 0; i < 360; i += 90) {
            MathHelper.SIN_TABLE_FAST[(int)(i * 11.377778f) & 0xFFF] = (float)Math.sin(i * 0.017453292f);
        }
    }
}
