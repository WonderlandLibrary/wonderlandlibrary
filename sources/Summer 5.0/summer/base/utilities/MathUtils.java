package summer.base.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MathUtils {
	public static double roundToPlace(final double value, final int places)
    {
        if (places < 0)
        {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double getIncremental(final double val, final double inc)
    {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }

    public static boolean isInteger(final Double variable)
    {
        return variable == Math.floor(variable) && !Double.isInfinite(variable);
    }

	public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;

        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }

	public static double getBaseMovementSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

	public static double secRanDouble(double min, double max) {

		SecureRandom rand = new SecureRandom();

		return rand.nextDouble() * (max - min) + min;
	}
}
