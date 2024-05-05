package io.github.raze.utilities.collection.math;

import java.util.Random;

public class RandomUtil {

	private static final String[] characters = new String[] {
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ",
			"abcdefghijklmnopqrstuvwxyz",
			"0123456789"
	};

	public static double randomNumber(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	public static String randomString(int length) {
		StringBuilder string = new StringBuilder(length);
		Random random = new Random(System.nanoTime());

		for (int index = 0; index < length; index += 1) {
			String character = characters[random.nextInt(characters.length)];
			int position = random.nextInt(character.length());
			string.append(character.charAt(position));
		}

		return new String(string);
	}

	private static final Random random = new Random();

	public static int nextInt(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		return random.nextInt(max - min) + min;
	}

}
