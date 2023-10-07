package dev.rise.util.math;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilities for generating random numbers or strings. Every RandomRange method is <b>inclusive</b>
 *
 * @author Strikeless
 * @since 14/06/2021
 */
@UtilityClass
public class RandomUtil {

    public char[] QWERTY_CHARSET = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".toCharArray();

    public double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public double randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public double randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public double randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public double randomRange(double min, double max) {
        if (max < min) {
            final double oldMin = min;
            min = max;
            max = oldMin;
        }
        return ThreadLocalRandom.current().nextDouble(min, max + 1);
    }

    public float randomRange(float min, float max) {
        if (max < min) {
            final float oldMin = min;
            min = max;
            max = oldMin;
        }
        return (float) ThreadLocalRandom.current().nextDouble(min, max + 1);
    }

    public int randomRange(int min, int max) {
        if (max < min) {
            final int oldMin = min;
            min = max;
            max = oldMin;
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public long randomRange(long min, long max) {
        if (max < min) {
            final long oldMin = min;
            min = max;
            max = oldMin;
        }
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public String randomStringOfSize(final int size, final char[] chars) {
        final StringBuilder builder = new StringBuilder(size);
        while (builder.length() < size) {
            builder.append(chars[randomRange(0, chars.length - 1)]);
        }
        return builder.toString();
    }

    public String randomStringOfBytes(final int bytes) {
        // Shamelessly copy&pasta'd straight from stackoverflow
        final byte[] random = new byte[bytes];
        ThreadLocalRandom.current().nextBytes(random);
        return Base64.encodeBase64String(random);
    }

    public String randomString(final int size) {
        return randomStringOfSize(size, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray());
    }

    public static long randomClickDelay(final int minCPS, final int maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
    }
}
