package best.azura.client.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtil {

	public static double round(double value, int scale, double inc) {
		final double halfOfInc = inc / 2.0;
		final double floored = Math.floor(value / inc) * inc;

		if (value >= floored + halfOfInc)
			return new BigDecimal(Math.ceil(value / inc) * inc)
					.setScale(scale, RoundingMode.HALF_UP)
					.doubleValue();
		else return new BigDecimal(floored)
				.setScale(scale, RoundingMode.HALF_UP)
				.doubleValue();
	}

	public static double getRandom(double min, double max) {
		if (min > max) return min;
		Random RANDOM = new Random();
		return RANDOM.nextDouble() * (max - min) + min;
	}

	public static float getRandom(float min, float max) {
		return (float) getRandom(min, (double) max);
	}

	public static int getRandom(int min, int max) {
		return (int) getRandom(min, (double) max);
	}

	public static double getRandom_double(double min, double max) {
		if (min > max) return min;
		Random RANDOM = new Random();
		return RANDOM.nextDouble() * (max - min) + min;
	}

	public static float getRandom_float(float min, float max) {
		if (min > max) return min;
		Random RANDOM = new Random();
		return RANDOM.nextFloat() * (max - min) + min;
	}

	public static long getRandom_long(long min, long max) {
		if (min > max) return min;
		Random RANDOM = new Random();
		return RANDOM.nextLong() * (max - min) + min;
	}

	public static int getRandom_int(int min, int max) {
		if (min > max) return min;
		Random RANDOM = new Random();
		return RANDOM.nextInt(max) + min;
	}

	public static byte getRandom_byte(byte min, byte max) {
		if (min > max) return min;
		Random RANDOM = new Random();
		return (byte) (RANDOM.nextInt(max) + min);
	}

	public static byte[] getRandomBytes(int minSize, int maxSize, byte min, byte max) {
		int size = getRandom_int(minSize, maxSize);
		final byte[] out = new byte[size];
		for (int i = 0; i < size; i++) {
			out[i] = getRandom_byte(min, max);
		}
		return out;
	}

	public static double getDistance(double x, double y, double x1, double y1) {
		return Math.sqrt((x - x1) + (y - y1));
	}

	public static double getDifference(double base, double yaw) {
		final double bigger;
		if (base >= yaw)
			bigger = base - yaw;
		else
			bigger = yaw - base;
		return bigger;
	}

	public static float getDifference(float base, float yaw) {
		float bigger;
		if (base >= yaw)
			bigger = base - yaw;
		else
			bigger = yaw - base;
		return bigger;
	}

	public static long getDifference(long base, long yaw) {
		long bigger;
		if (base >= yaw)
			bigger = base - yaw;
		else
			bigger = yaw - base;

		return bigger;
	}
}