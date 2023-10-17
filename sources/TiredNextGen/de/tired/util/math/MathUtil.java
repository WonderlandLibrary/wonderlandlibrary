package de.tired.util.math;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.security.SecureRandom;
import java.util.Random;

public class MathUtil {
	private static final Random rand = new Random();

	public static int getRandom(int min, int max) {
		return min + (int) (Math.random() * (double) (max - min + 1));
	}

	public static double getRandom(double min, double max) {
		return rand.nextDouble() * (max - min) + min;
	}
	public static String random(final int length, final String chars) {
		return random(length, chars.toCharArray());
	}
	public static String random(final int length, final char[] chars) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++)
			stringBuilder.append(chars[new Random().nextInt(chars.length)]);
		return stringBuilder.toString();
	}

	public static double round(double number, int decimals) {
		number *= Math.pow(10, decimals);
		number = Math.round(number);
		return number / Math.pow(10, decimals);
	}


	public static float limitAngleChange(float current, float intended, float maxChange) {
		float change = MathHelper.wrapAngleTo180_float(intended - current);
		change = MathHelper.clamp_float(change, -maxChange, maxChange);
		return MathHelper.wrapAngleTo180_float(current + change);
	}

	public static float calculateGaussianValue(float x, float sigma) {
		double PI = Math.PI;
		double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
		return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
	}
	public static Vec3 getVectorForRotation(float yaw, float pitch) {
		float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
	}

	public static double getRandomSin(double min, double max, double timeFactor) {
		double random = Math.sin((double) System.currentTimeMillis() / timeFactor) * (max - min);
		if (random < 0.0D) {
			random = Math.abs(random);
		}

		return random + min;
	}


}
