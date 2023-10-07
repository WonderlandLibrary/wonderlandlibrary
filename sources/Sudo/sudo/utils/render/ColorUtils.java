package sudo.utils.render;

import java.awt.*;

import sudo.module.ModuleManager;

public class ColorUtils {
	
	public static String colorChar = "\247";
	public static String purple = "\2475";
	public static String red = "\247c";
	public static String aqua = "\247b";
	public static String green = "\247a";
	public static String blue = "\2479";
	public static String darkGreen = "\2472";
	public static String darkBlue = "\2471";
	public static String black = "\2470";
	public static String darkRed = "\2474";
	public static String darkAqua = "\2473";
	public static String lightPurple = "\247d";
	public static String yellow = "\247e";
	public static String white = "\247f";
	public static String gray = "\2477";
	public static String darkGray = "\2478";
	public static String reset = "\247r";
	public static String pingle = "#FF1464";

	public static Color hexToRgb(String hex) {
        try {
            return Color.decode("#" + hex.replace("#", ""));
        } catch(NumberFormatException e) {
            System.err.println("Invalid hex string!");
            return Color.WHITE;
        }
    }
    
    public static int rainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	
	public static int rainbow(float seconds, float saturation, float brigtness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, 1);
		return color;
	}
	
	public static Color fade(Color color, int index, int count) {
	      float[] hsb = new float[3];
	      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
	      float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
	      brightness = 0.5F + 0.5F * brightness;
	      hsb[2] = brightness % 2.0F;
	      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	 }
	
	public static Color fade(Color color, Color color2, int index, int count) {
	      float[] hsb = new float[3];
	      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
	      float brightness = Math.abs(((float)(System.currentTimeMillis() + index % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
	      brightness = 0.5F + 0.5F * brightness;
	      hsb[2] = brightness % 2.0F;
	      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	 }

	public static Color getColor(int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        return new Color(red, green, blue, alpha);
    }
		
	public static int transparent(int rgb, int opacity) {
		Color color = new Color(rgb);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity).getRGB();
	}
	
	public static int transparent(int opacity) {
		Color color = Color.BLACK;
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity).getRGB();
	}
	
	public static int blendColours(final int[] colours, final double progress) {
        final int size = colours.length;
        if (progress == 1.f) return colours[0];
        else if (progress == 0.f) return colours[size - 1];
        final double mulProgress = Math.max(0, (1 - progress) * (size - 1));
        final int index = (int) mulProgress;
        return fadeBetween(colours[index], colours[index + 1], mulProgress - index);
    }
    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1) progress = 1 - progress % 1;
        return fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int startColour, int endColour, long offset) {
        return fadeBetween(startColour, endColour, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return fadeBetween(startColour, endColour, 0L);
    }

    public static int fadeTo(int startColour, int endColour, double progress) {
        double invert = 1.0 - progress;
        int r = (int) ((startColour >> 16 & 0xFF) * invert +
                (endColour >> 16 & 0xFF) * progress);
        int g = (int) ((startColour >> 8 & 0xFF) * invert +
                (endColour >> 8 & 0xFF) * progress);
        int b = (int) ((startColour & 0xFF) * invert +
                (endColour & 0xFF) * progress);
        int a = (int) ((startColour >> 24 & 0xFF) * invert +
                (endColour >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }
    
    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
    
    public static Color getCuteColor(int index) {

        int size = ModuleManager.INSTANCE.getEnabledModules().size();

        Color light_blue = new Color(91, 206, 250);
        Color white = Color.WHITE;
        Color pink = new Color(245, 169, 184);

        int part = (int) ((float) index / size * 5);

        if (part == 0) {
            return light_blue;
        }
        if (part == 1) {
            return pink;
        }
        if (part == 2) {
            return white;
        }
        if (part == 3) {
            return pink;
        }
        if (part == 4) {
            return light_blue;
        }

        return light_blue; //fail
    }
    
    public static Color mixColors(final Color color1, final Color color2, final double percent) {
        final double inverse_percent = 1.0 - percent;
        final int red = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        final int green = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        final int blue = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(red, green, blue);
    }
    
    public static Color blend2colors(final Color color1, final Color color2, double offset) {
        final float hue = System.currentTimeMillis();

        offset += hue;

        if (offset > 1) {
            final double left = offset % 1;
            final int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        final double inversePercent = 1 - offset;

        final int red = (int) (color1.getRed() * inversePercent + color2.getRed() * offset);
        final int green = (int) (color1.getGreen() * inversePercent + color2.getGreen() * offset);
        final int blue = (int) (color1.getBlue() * inversePercent + color2.getBlue() * offset);
        return new Color(red, green, blue);
    }
    
    public static Color mixColorsAnimated(float colorOffset, final float timeMultiplier, final Color color1, final Color color2) {
        final double timer = (System.currentTimeMillis() / 1E+8 * timeMultiplier) * 4E+5;
        final double factor = (Math.sin(timer + colorOffset * 0.55f) + 1) * 0.5f;
        return mixColors(color1, color2, factor);
    }
}
