/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public final class MathUtils {
    private static final Random rng = new Random();

    public static double roundToPlace(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return false;
        }
    }

    public static double getIncremental(double d, double d2) {
        double d3 = 1.0 / d2;
        return (double)Math.round(d * d3) / d3;
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return false;
        }
    }

    public static boolean isInteger(Double d) {
        return d == Math.floor(d) && !Double.isInfinite(d);
    }

    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return false;
        }
    }

    public static double randomNumber(double d, double d2) {
        return Math.random() * (d - d2) + d2;
    }

    public static int getRandom(int n, int n2) {
        return n + rng.nextInt(n2 - n + 1);
    }

    public static Random getRng() {
        return rng;
    }

    public static double secRanDouble(double d, double d2) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextDouble() * (d2 - d) + d;
    }

    public static double meme(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static boolean contains(float f, float f2, float f3, float f4, float f5, float f6) {
        return f > f3 && f < f5 && f2 > f4 && f2 < f6;
    }

    public static float getRandom() {
        return rng.nextFloat();
    }

    public static double round(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static int getMid(int n, int n2) {
        return (n + n2) / 2;
    }

    public static float clampValue(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        return f > f3 ? f3 : f;
    }

    public static double round(double d, double d2) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale((int)d2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static float map(float f, float f2, float f3, float f4, float f5) {
        return (f - f2) / (f3 - f2) * (f5 - f4) + f4;
    }

    public static double getRandomInRange(double d, double d2) {
        double d3;
        Random random = new Random();
        double d4 = d2 - d;
        double d5 = random.nextDouble() * d4;
        if (d5 > d2) {
            d5 = d2;
        }
        if ((d3 = d5 + d) > d2) {
            d3 = d2;
        }
        return d3;
    }

    public static double getBaseMovementSpeed() {
        double d = 0.2873;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            int n = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            d *= 1.0 + 0.2 * (double)(n + 1);
        }
        return d;
    }

    public static double getDifference(double d, double d2) {
        double d3 = d >= d2 ? d - d2 : d2 - d;
        return d3;
    }

    public static int customRandInt(int n, int n2) {
        return new Random().nextInt(n2 - n + 1) + n;
    }

    public static int getRandom(int n) {
        return rng.nextInt(n);
    }

    public static float getSimilarity(String string, String string2) {
        int n = Math.min(string.length(), string2.length()) / 2 + Math.min(string.length(), string2.length()) % 2;
        StringBuffer stringBuffer = MathUtils.getCommonCharacters(string, string2, n);
        StringBuffer stringBuffer2 = MathUtils.getCommonCharacters(string2, string, n);
        if (stringBuffer.length() != 0 && stringBuffer2.length() != 0) {
            if (stringBuffer.length() != stringBuffer2.length()) {
                return 0.0f;
            }
            int n2 = 0;
            int n3 = stringBuffer.length();
            int n4 = 0;
            while (n4 < n3) {
                if (stringBuffer.charAt(n4) != stringBuffer2.charAt(n4)) {
                    ++n2;
                }
                ++n4;
            }
            return (float)(stringBuffer.length() / string.length() + stringBuffer2.length() / string2.length() + (stringBuffer.length() - (n2 /= 2)) / stringBuffer.length()) / 3.0f;
        }
        return 0.0f;
    }

    public static int randInt(int n, int n2) {
        return new Random().nextInt(n2 - n + 1) + n;
    }

    private static StringBuffer getCommonCharacters(String string, String string2, int n) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer(string2);
        int n2 = string.length();
        int n3 = string2.length();
        int n4 = 0;
        while (n4 < n2) {
            char c = string.charAt(n4);
            boolean bl = false;
            int n5 = Math.max(0, n4 - n);
            while (!bl && n5 < Math.min(n4 + n, n3 - 1)) {
                if (stringBuffer2.charAt(n5) == c) {
                    bl = true;
                    stringBuffer.append(c);
                    stringBuffer2.setCharAt(n5, '\u0000');
                }
                ++n5;
            }
            ++n4;
        }
        return stringBuffer;
    }
}

