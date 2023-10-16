package com.enjoytheban.utils.math;

import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.enjoytheban.utils.Helper;

public class MathUtil {

	public static Random random = new Random();
	/**
	 * Returns a number to a set number of decimal places
	 */
	public static double toDecimalLength(double in, int places) {
		return Double.parseDouble(String.format("%." + places + "f", in));
	}
	
	/**
	 * Rounding method
	 */
	public static double round(double in, int places) {
		places = (int) MathHelper.clamp_double(places, 0, Integer.MAX_VALUE);
		return Double.parseDouble(String.format("%." + places + "f", in));
	}
	
	/**
	 * Check if a String ({@code s}) is parsable as a specified number type
	 */
	public static boolean parsable(String s, byte type) {
		try {
			switch (type) {
			case NumberType.SHORT:
				Short.parseShort(s);
				break;
			case NumberType.BYTE:
				Byte.parseByte(s);
				break;
			case NumberType.INT:
				Integer.parseInt(s);
				break;
			case NumberType.FLOAT:
				Float.parseFloat(s);
				break;
			case NumberType.DOUBLE:
				Double.parseDouble(s);
				break;
			case NumberType.LONG:
				Long.parseLong(s);
				break;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//method that returns a squared value
	public static double square(double in) {
		return in * in;
	}

    public static class NumberType {
		public static final byte SHORT = 0, BYTE = 1, INT = 2, FLOAT = 3, DOUBLE = 4, LONG = 5;

		/**
		 * Get the NumberType by a number class
		 */
		public static byte getByType(Class cls) {
			if (cls == Short.class) {
				return SHORT;
			} else if (cls == Byte.class) {
				return BYTE;
			} else if (cls == Integer.class) {
				return INT;
			} else if (cls == Float.class) {
				return FLOAT;
			} else if (cls == Double.class) {
				return DOUBLE;
			} else if (cls == Long.class) {
				return LONG;
			} else {
				return -1;
			}
		}
	}

	/**
	 * Generate a clamped random double
	 */
	public static double randomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	// returns the entites base movement speed? i guess this kinda counts as math related to ill throw it in here
	public static double getBaseMovementSpeed() {
		double baseSpeed = 0.2873;
		if (Helper.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = Helper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}
	
	//returns the highest offset  depenting on the players collision boxes
	public static double getHighestOffset(double max) {
		for (double i = 0.0; i < max; i += 0.01) {
			for (int offset : new int[] { -2, -1, 0, 1, 2 }) {
				if (Helper.mc.theWorld
						.getCollidingBoundingBoxes(Helper.mc.thePlayer, Helper.mc.thePlayer.getEntityBoundingBox().offset(
								Helper.mc.thePlayer.motionX * (double) offset, i, Helper.mc.thePlayer.motionZ * (double) offset))
						.size() > 0) {
					return i - 0.01;
				}
			}
		}
		return max;
	}
}