// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import java.util.Random;
import ru.tuskevich.util.Utility;

public class RandomUtility implements Utility
{
    public static String randomString(final int length) {
        final StringBuilder builder = new StringBuilder();
        final char[] buffer = "qwertyuiopasdfghjklzxcvbnm1234567890".toCharArray();
        for (int i = 0; i < length; ++i) {
            final Random rand = new Random();
            final String s = new String(new char[] { buffer[rand.nextInt(buffer.length)] });
            builder.append(rand.nextBoolean() ? s : s.toUpperCase());
        }
        return builder.toString();
    }
    
    public static String randomNumber(final int length) {
        final StringBuilder builder = new StringBuilder();
        final char[] buffer = "1234567890".toCharArray();
        for (int i = 0; i < length; ++i) {
            final Random rand = new Random();
            final String s = new String(new char[] { buffer[rand.nextInt(buffer.length)] });
            builder.append(rand.nextBoolean() ? s : s.toUpperCase());
        }
        return builder.toString();
    }
    
    public static int intRandom(final int min, final int max) {
        return (int)(Math.random() * (max - min)) + min;
    }
    
    public static float floatRandom(final float min, final float max) {
        return (float)(min + (max - min) * Math.random());
    }
}
