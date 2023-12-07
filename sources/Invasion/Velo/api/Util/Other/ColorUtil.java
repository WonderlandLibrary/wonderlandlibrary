package Velo.api.Util.Other;

import java.awt.Color;

import Velo.impl.Modules.visuals.hud.HUD;
import net.minecraft.util.MathHelper;

public class ColorUtil {
	
	
	public static float getClickGUIColor(){
		
		return (float)ColorUtil.getGradientOffset(new Color((int) HUD.red2.getValue(), (int) HUD.green2.getValue(), (int) HUD.blue2.getValue(), 255), new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB();

	}
	
	public static int getRainbow(float seconds, float saturation, float brightness, long index) {
		float hue = (((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000));
		float hue5 = (((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000));
		
		float hue2 = MathHelper.sin(MathHelper.sqrt_float(hue5) * (float)Math.PI);
		
		float hue3 = MathHelper.sin(hue5 * hue5 * (float)Math.PI);
		
		int color = Color.HSBtoRGB(hue, HUD.rainbowsaturation.getValueFloat(), brightness);
		
		int color2 = Color.HSBtoRGB(hue3/7, HUD.rainbowsaturation.getValueFloat(), brightness);
		
		return color2;
	}
	
	public static Color getRainbow2(Color color1, Color color2, double offset) {
		
		float hue2 = (((System.currentTimeMillis() + 0) % (int)(2 * 1000)) / (float)(2 * 1000));
		
		hue2 -= hue2/2;
		
		color1 = new Color(hue2, 0, 0);
		
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }
	
	 public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	 }
	 public static int rainbow(int delay, float brightness, float saturation) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), saturation, brightness).getRGB();
	  }
	 
	 public static int getRainbow(int speed, int offset) {
	      float hue = (System.currentTimeMillis() + offset) % speed;
	      hue /= speed;
	        return Color.getHSBColor(hue, 1f, 1f).getRGB(); 
	 }
	 public static int getRainbow(int speed, int offset,  float brightness, float saturation) {
	      float hue = (System.currentTimeMillis() + offset) % speed;
	      hue /= speed;
	        return Color.getHSBColor(hue, saturation, brightness).getRGB(); 
	 }
	
	public static Color pulseBrightness(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
        brightness = 0.5F + 0.5F * brightness;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness % 2.0F));
     }
    
    public static Color pulseSaturation(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
        brightness = 0.8F * brightness;
        hsb[1] = brightness % 2.0F;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
     }
    

    public static String textColor(String color) {
        if (color.equalsIgnoreCase("BLACK")) {
            return "§0";
        } else if (color.equalsIgnoreCase("DBLUE")) {
            return "§1";
        } else if (color.equalsIgnoreCase("DGREEN")) {
            return "§2";
        } else if (color.equalsIgnoreCase("DAQUA")) {
            return "§3";
        } else if (color.equalsIgnoreCase("DRED")) {
            return "§4";
        } else if (color.equalsIgnoreCase("DPURPLE")) {
            return "§5";
        } else if (color.equalsIgnoreCase("GOLD")) {
            return "§6";
        } else if (color.equalsIgnoreCase("GRAY")) {
            return "§7";
        } else if (color.equalsIgnoreCase("DGRAY")) {
            return "§8";
        } else if (color.equalsIgnoreCase("BLUE")) {
            return "§9";
        } else if (color.equalsIgnoreCase("GREEN")) {
            return "§a";
        } else if (color.equalsIgnoreCase("AQUA")) {
            return "§b";
        } else if (color.equalsIgnoreCase("RED")) {
            return "§c";
        } else if (color.equalsIgnoreCase("LPURPLE")) {
            return "§d";
        } else if (color.equalsIgnoreCase("YELLOW")) {
            return "§e";
        } else if (color.equalsIgnoreCase("WHITE")) {
            return "§f";
        } else return "";
    }
	
	   public static Color getHealthColor(float health, float maxHealth) {
		      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
		      Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
		      float progress = health / maxHealth;
		      return blendColors(fractions, colors, progress).brighter();
		   }
	   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
		      if (fractions.length == colors.length) {
		         int[] indices = getFractionIndices(fractions, progress);
		         float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
		         Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
		         float max = range[1] - range[0];
		         float value = progress - range[0];
		         float weight = value / max;
		         Color color = blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
		         return color;
		      } else {
		         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
		      }
		   }
	   
	   
	   public static int[] getFractionIndices(float[] fractions, float progress) {
		      int[] range = new int[2];

		      int startPoint;
		      for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
		      }

		      if (startPoint >= fractions.length) {
		         startPoint = fractions.length - 1;
		      }

		      range[0] = startPoint - 1;
		      range[1] = startPoint;
		      return range;
		   }

		   public static Color blend(Color color1, Color color2, double ratio) {
		      float r = (float)ratio;
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
	
	public static int moonColors(int yOffset, int yTotal) {
        float speed = 10000F;
        float hue = (float) (MathHelper.sin(System.currentTimeMillis()) % (int)speed) + ((yTotal - yOffset) * 9);
        
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 1) {
            hue = 1F - (hue - 1f);
        }
        hue += 1F;
        return Color.HSBtoRGB(hue, 0.5f, 1F);
	}
	
	public static Color fade(long offset, float fade) {
        float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
                16);
        Color c = new Color((int) color);
        return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
                c.getAlpha() / 155.0F);
    }
	
	public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }
}
