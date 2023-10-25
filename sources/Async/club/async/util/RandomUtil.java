package club.async.util;

import java.util.Random;

public final class RandomUtil {

    public static String randomNumber(int length) {
        return random(length, "0123456789".toCharArray());
    }

    public static double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    public static String randomString(int length) {
        return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray());
    }

    private static String random(int length, char[] chars) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++)
            stringBuilder.append(chars[(new Random()).nextInt(chars.length)]);
        return stringBuilder.toString();
    }

}
