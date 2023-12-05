package koks.api.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 16:47
 */
public class RandomUtil {

    public int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public long getRandomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public float getRandomFloat(float min, float max) {
        return (float) ThreadLocalRandom.current().nextDouble(min, max);
    }

    public double getRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public double getRandomGaussian(double average) {
        return ThreadLocalRandom.current().nextGaussian() * average;
    }

}