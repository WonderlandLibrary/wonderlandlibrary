package me.aquavit.liquidsense.utils.render;

import net.minecraft.client.Minecraft;

import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;

public class AnimationUtils {

	public static double animate(double target, double current, double speed) {
		boolean larger = (target > current);

		if (speed < 0.0D) {
			speed = 0.0D;
		} else if (speed > 1.0D) {
			speed = 1.0D;
		}

		double dif = Math.max(target, current) - Math.min(target, current);
		double factor = dif * speed;

		if (factor <= 0.1D)
			factor = 0.1D;

		if (larger) {
			current += factor;
		} else {
			current -= factor;
		}

		return current;
	}

	public static float lstransition(float now, float desired, double speed) {
		final double dif = Math.abs(desired - now);
		float a = (float) Math.abs((desired - (desired - (Math.abs(desired - now)))) / (100 - (speed * 10)));
		float x = now;

		if (dif > 0) {
			if (now < desired)
				x += a * RenderUtils.deltaTime;
			else if (now > desired)
				x -= a * RenderUtils.deltaTime;
		} else
			x = desired;

		if(Math.abs(desired - x) < 10.0E-3 && x != desired)
			x = desired;

		return x;
	}

	public static double Anim(double now, double desired, double speed) {
		double dif = Math.abs(now - desired);
		int fps = Minecraft.getDebugFPS();
		if (dif > 0.0D) {
			double animationSpeed = roundToDecimalPlace(Math.min(10.0D, Math.max(0.05D, 144.0D / fps * dif / 10.0D * speed)), 0.05D);
			if (dif != 0.0D && dif < animationSpeed) animationSpeed = dif;
			if (now < desired) return now + animationSpeed;
			if (now > desired) return now - animationSpeed;
		}
		return now;
	}

	public static Color getHealthColor(float health, float maxHealth) {
		float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
		Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
		float progress = health / maxHealth;
		return blendColors(fractions, colors, progress).brighter();
	}

  /*  public final void reset() {
        ms = getCurrentMS();
    }*/

	public static Color blendColors(float[] fractions, Color[] colors, float progress) {
		if (fractions.length == colors.length) {
			int[] indices = getFractionIndices(fractions, progress);
			float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
			Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
			float max = range[1] - range[0];
			float value = progress - range[0];
			float weight = value / max;
			Color color = blend(colorRange[0], colorRange[1], 1.0F - weight);
			return color;
		} else {
			throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
		}
	}

	public static int[] getFractionIndices(float[] fractions, float progress) {
		int[] range = new int[2];

		int startPoint;
		for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
		}

		if (startPoint >= fractions.length) {
			startPoint = fractions.length - 1;
		}

		range[0] = startPoint - 1;
		range[1] = startPoint;
		return range;
	}

	public static Color blend(Color color1, Color color2, double ratio) {
		float r = (float) ratio;
		float ir = 1.0F - r;
		float[] rgb1 = color1.getColorComponents(new float[3]);
		float[] rgb2 = color2.getColorComponents(new float[3]);
		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;

		if (red < 0.0F) {
			red = 0.0F;
		} else if (red > 255.0F) {
			red = 255.0F;
		}

		if (green < 0.0F) {
			green = 0.0F;
		} else if (green > 255.0F) {
			green = 255.0F;
		}

		if (blue < 0.0F) {
			blue = 0.0F;
		} else if (blue > 255.0F) {
			blue = 255.0F;
		}

		Color color3 = null;

		try {
			color3 = new Color(red, green, blue);
		} catch (IllegalArgumentException var13) {
		}
		return color3;
	}

	public static double roundToDecimalPlace(double value, double inc) {
		double halfOfInc = inc / 2.0D;
		double floored = StrictMath.floor(value / inc) * inc;

		if (value >= floored + halfOfInc)
			return (new BigDecimal(StrictMath.ceil(value / inc) * inc, MathContext.DECIMAL64)).stripTrailingZeros().doubleValue();
		return (new BigDecimal(floored, MathContext.DECIMAL64)).stripTrailingZeros().doubleValue();
	}

	private long getCurrentMS() {
		return System.currentTimeMillis();
	}
}