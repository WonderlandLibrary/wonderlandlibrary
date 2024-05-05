package Velo.api.Util.Other;

import org.apache.commons.lang3.RandomUtils;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

public class MathUtils {

	
	public static SecureRandom random = new SecureRandom();

	public static Color getRandomColor() {
		float r = randomFloat(0, 1);
		float g = randomFloat(0, 1);
		float b = randomFloat(0, 1);
		return new Color(r, g, b);
	}

	public static Color getRandomLightColor() {
		float r = (randomFloat(0f, 0.5f) + randomFloat(0f, 0.5f)) / 2f + 0.5f;
		float g = (randomFloat(0f, 0.5f) + randomFloat(0f, 0.5f)) / 2f + 0.5f;
		float b = (randomFloat(0f, 0.5f) + randomFloat(0f, 0.5f)) / 2f + 0.5f;
		return new Color(r, g, b);
	}

	public static int randomInt(int min, int max) {
		return randomInts(min, max, 1)[0];
	}

	public static int[] randomInts(int min, int max, int amount) {
		int[] results = new int[amount];
		for (int i = 0; i < amount; i++)
			results[i] = random.ints(min, (max + 1)).limit(1).findFirst().getAsInt();

		return results;
	}

	public static double randomDouble(double min, double max) {
		return randomDoubles(min, max, 1)[0];
	}

	public static double[] randomDoubles(double min, double max, int amount) {
		double[] results = new double[amount];
		for (int i = 0; i < amount; i++)
			results[i] = min + (max - min) * random.nextDouble();

		return results;
	}

	public static float randomFloat(float min, float max) {
		return randomFloats(min, max, 1)[0];
	}

	public static float[] randomFloats(float min, float max, int amount) {
		float[] results = new float[amount];
		for (int i = 0; i < amount; i++)
			results[i] = min + (max - min) * random.nextFloat();

		return results;
	}

	public static boolean randomBoolean() {
		return random.nextBoolean();
	}
	
	
	
    public static double getIncremental(double val, double inc) {
		 double one = 1.0D / inc;
		 return (double)Math.round(val * one) / one;
    }

    public static double preciseRound(double value, double precision) {
        double scale = Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

	public static double square(double d) {
		return d * d;
	}
	
	public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

	   public static boolean isInteger(Double variable) {
	      return variable.doubleValue() == Math.floor(variable.doubleValue()) && !Double.isInfinite(variable.doubleValue());
	   }
	

    private static Random rng = new Random();

    public static Random getRNG() {
        return rng;
    }

    public static double radians(double degress){
        return degress * Math.PI / 180;
    }

    public static int customRandInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static double toDecimalLength(double in, int places) {
        return Double.parseDouble(String.format("%." + places + "f", in));
    }

    public static double roundToDecimalPlace(double value, double inc) {
        final double halfOfInc = inc / 2.0D;
        final double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc)
            return new BigDecimal(Math.ceil(value / inc) * inc, MathContext.DECIMAL64).
                    stripTrailingZeros()
                    .doubleValue();
        else
            return new BigDecimal(floored, MathContext.DECIMAL64)
                    .stripTrailingZeros()
                    .doubleValue();
    }

    public static int getRandom(int i) {
        return RandomUtils.nextInt(0, i);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }



    public boolean isEven(int number) {
        double n2 = number / 2.0D;
        int n3 = (int) (number / 2.0D);
        if (n3 - n2 == 0.0D)
            return true;
        return false;
    }

    public static float secRanFloat(float min, float max) {

        SecureRandom rand = new SecureRandom();

        return rand.nextFloat() * (max - min) + min;
    }

    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted;
    }

    public static int getRandom(int floor, int cap) {
        return floor + getRNG().nextInt(cap - floor + 1);
    }

    public static double setRandom(double min, double max) {
        Random random = new Random();
        return min + random.nextDouble() * (max - min);
    }

    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }

    public static int randomNumber(int max, int min) {
        return Math.round(min + (float)Math.random() * (max - min));
    }

    public static float randomFloatValue() {
        return (float) getRandomInRange(0.000000296219, 0.00000913303);
    }
}
