package com.masterof13fps.manager.clickguimanager.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 */
public class MathUtil {

	/**
	 * @param toRound
	 * @param scale
	 * @return
	 */
	public static float round(float toRound, int scale) {
		return new BigDecimal(toRound).setScale(scale, RoundingMode.HALF_EVEN).floatValue();
	}

	/**
	 * @param toRound
	 * @param scale
	 * @return
	 */
	public static double round(double toRound, int scale) {
		return new BigDecimal(toRound).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
	}

}
