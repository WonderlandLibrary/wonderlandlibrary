package wtf.monsoon.api.util.render;

import java.awt.Color;

public class ColorUtil {

	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 4000f);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	
	public static int getRGB(float seconds, float saturation, float brightness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}

	 public static Color novo(int index, int speed, float saturation, float brightness, float opacity) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        float hue = angle / 360f;

        int color = Color.HSBtoRGB(brightness, saturation, hue);
        Color obj = new Color(color);
        return new Color(obj.getRed(), obj.getGreen(), obj.getBlue(), Math.max(0, Math.min(255, (int) (opacity * 255))));
    }

    //Credits to Plexter C#1339 for this :)
	public static int astolfoColors(int yOffset, int yTotal) {
		float speed = 2900F;
		float hue = (float) (System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 9);
		while (hue > speed) {
			hue -= speed;
		}
		hue /= speed;
		if (hue > 0.5) {
			hue = 0.5F - (hue - 0.5f);
		}
		hue += 0.5F;
		return Color.HSBtoRGB(hue, 0.5f, 1F);
	}


	/**
	 * Returns the int with the color the {@linkplain Color} represents
	 * @param color
	 * @return
	 */
	public static int fromColor(Color color) {
		return color.getRGB();
	}

	/**
	 * Returns the color with it's alpha set to the specified one
	 * @param colorIn old color
	 * @param newAlpha New alpha value for the color, range 0-255
	 * @return the Color with the alpha changed
	 */
	public static Color changeAlpha(Color colorIn, int newAlpha) {
		return new Color(colorIn.getRed(), colorIn.getGreen(), colorIn.getBlue(), newAlpha);
	}

	/**
	 * Returns the color with it's red value set to the specified one
	 * @param colorIn old color
	 * @param newRed New red value for the color, range 0-255
	 * @return the Color with its red value changed
	 */
	public static Color changeRed(Color colorIn, int newRed) {
		return new Color(newRed, colorIn.getGreen(), colorIn.getBlue(), colorIn.getAlpha());
	}

	/**
	 * Returns the color with it's green value set to the specified one
	 * @param colorIn old color
	 * @param newGreen New green value for the color, range 0-255
	 * @return the Color with its green value changed
	 */
	public static Color changeGreen(Color colorIn, int newGreen) {
		return new Color(colorIn.getRed(), newGreen, colorIn.getBlue(), colorIn.getAlpha());
	}

	/**
	 * Returns the color with it's blue value set to the specified one
	 * @param colorIn old color
	 * @param newBlue New blue value for the color, range 0-255
	 * @return the Color with its blue value changed
	 */
	public static Color changeBlue(Color colorIn, int newBlue) {
		return new Color(colorIn.getBlue(), colorIn.getGreen(), newBlue, colorIn.getAlpha());
	}

	/**
	 * Returns the color with it's alpha set to the specified one
	 * @param colorIn old color
	 * @param newAlpha New alpha value for the color, range 0-1
	 * @return the Color with the alpha changed
	 */
	public static Color changeAlpha(Color colorIn, float newAlpha) {
		return new Color(colorIn.getRed(), colorIn.getGreen(), colorIn.getBlue(), (int)(newAlpha * 255));
	}

	/**
	 * Returns the color with it's red value set to the specified one
	 * @param colorIn old color
	 * @param newRed New red value for the color, range 0-1
	 * @return the Color with its red value changed
	 */
	public static Color changeRed(Color colorIn, float newRed) {
		return new Color((int)(newRed * 255), colorIn.getGreen(), colorIn.getBlue(), colorIn.getAlpha());
	}

	/**
	 * Returns the color with it's green value set to the specified one
	 * @param colorIn old color
	 * @param newGreen New green value for the color, range 0-1
	 * @return the Color with its green value changed
	 */
	public static Color changeGreen(Color colorIn, float newGreen) {
		return new Color(colorIn.getRed(), (int)(newGreen * 255), colorIn.getBlue(), colorIn.getAlpha());
	}

	/**
	 * Returns the color with it's blue value set to the specified one
	 * @param colorIn old color
	 * @param newBlue New blue value for the color, range 0-1
	 * @return the Color with its blue value changed
	 */
	public static Color changeBlue(Color colorIn, float newBlue) {
		return new Color(colorIn.getBlue(), colorIn.getGreen(), (int)(newBlue * 255), colorIn.getAlpha());
	}

	/**
	 * Returns a linear (rate doesn't change) color transition from the first to second color taking the specified amount of seconds
	 * @param startColor color the transition starts from
	 * @param endColor color the transition ends on
	 * @param millis specifies how long the transition takes
	 * @param startMillis specifies when the transition started to calculate the elapsed time
	 * @return The current color during the transition
	 * @author Jan_Lukas#9238 on Discord
	 */
	public static Color colorTransitionLinear(Color startColor, Color endColor, long millis, long startMillis) {
		long elapsedMillis = System.currentTimeMillis() - startMillis;
		if (elapsedMillis >= millis)
			return endColor;

		float currentRed = -((((startColor.getRed() - endColor.getRed()) / 255.0f) / millis) * elapsedMillis) + (float) (startColor.getRed() / 255);
		float currentGreen = -((((startColor.getGreen() - endColor.getGreen()) / 255.0f) / millis) * elapsedMillis) + (float) (startColor.getGreen() / 255);
		float currentBlue = -((((startColor.getBlue() - endColor.getBlue()) / 255.0f) / millis) * elapsedMillis) + (float) (startColor.getBlue() / 255);
		float currentAlpha = -((((startColor.getAlpha() - endColor.getAlpha()) / 255.0f) / millis) * elapsedMillis) + (float) (startColor.getAlpha() / 255);

		try {
			return new Color(currentRed, currentGreen, currentBlue, currentAlpha);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return endColor;
		}
	}

	/**
	 * @author tascord#2680 on Discord
	 * @param current
	 * @param target
	 * @param amount 0-255 range, 255 means full target, 0 means full current
	 * @return the lerped color
	 */
	public static Color ColorLerp(Color current, Color target, int amount) {
		amount = Math.min(Math.max(amount, 0), 255);

		float thresh = 0.5f;

		int[] currentArray = {current.getRed(), current.getGreen(), current.getBlue()};
		int[] targetArray = {target.getRed(),  target.getGreen(),  target.getBlue()};

		if(currentArray[0] + thresh < targetArray[0]) currentArray[0] += amount;
		if(currentArray[1] + thresh < targetArray[1]) currentArray[1] += amount;
		if(currentArray[2] + thresh < targetArray[2]) currentArray[2] += amount;

		if(currentArray[0] - thresh > targetArray[0]) currentArray[0] -= amount;
		if(currentArray[1] - thresh > targetArray[1]) currentArray[1] -= amount;
		if(currentArray[2] - thresh > targetArray[2]) currentArray[2] -= amount;

		return new Color(currentArray[0], currentArray[1], currentArray[2]);
	}

	/**
	 * @author Eric Golde https://www.youtube.com/user/egold555
	 * @param start
	 * @param end
	 * @param ratio 0-1 range, 0 means full end, 1 means full start
	 * @return the lerped color
	 */
	public static Color colorLerpv2(Color start, Color end, float ratio) {
		ratio = Math.min(Math.max(ratio, 0.0f), 1.0f);

		int red = (int)Math.abs((ratio * start.getRed()) + ((1 - ratio) * end.getRed()));
		int green = (int)Math.abs((ratio * start.getGreen()) + ((1 - ratio) * end.getGreen()));
		int blue = (int)Math.abs((ratio * start.getBlue()) + ((1 - ratio) * end.getBlue()));

		return new Color(red, green, blue);
	}

	/**
	 * @param rgb
	 * @return the {@linkplain Color} object made from the specified int
	 */
	public static Color fromRGB(int rgb) {
		return new Color(rgb);
	}

}
