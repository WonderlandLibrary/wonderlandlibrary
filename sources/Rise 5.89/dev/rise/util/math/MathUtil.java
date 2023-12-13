package dev.rise.util.math;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public final class MathUtil {

    public final SecureRandom RANDOM = new SecureRandom();

    public double lerp(final double a, final double b, final double c) {
        return a + c * (b - a);
    }

    public float lerp(final float a, final float b, final float c) {
        return a + c * (b - a);
    }

    public boolean roughlyEquals(final double alpha, final double beta) {
        return Math.abs(alpha - beta) < 1.0E-4;
    }

    public double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        } else if (min > max) {
            final double d = min;
            min = max;
            max = d;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }


    public double getVariance(final Collection<? extends Number> data) {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        final double average;

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }

    public double getStandardDeviation(final Collection<? extends Number> data) {
        return Math.sqrt(getVariance(data));
    }


    public double getAverage(final Collection<? extends Number> data) {
        double sum = 0.0;

        for (final Number number : data) {
            sum += number.doubleValue();
        }

        return sum / data.size();
    }

    public double getCps(final Collection<? extends Number> data) {
        return 20.0D * getAverage(data);
    }
}
