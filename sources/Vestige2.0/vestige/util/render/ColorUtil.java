package vestige.util.render;

import java.awt.Color;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ColorUtil {
	
	public Color getRainbow(float seconds, float saturation, float brightness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return new Color(color);
	}
	
	public Color getGradient(Color color1, Color color2, double scale) {
		double red = color1.getRed() + (color2.getRed() - color1.getRed()) * scale;
		double green = color1.getGreen() + (color2.getGreen() - color1.getGreen()) * scale;
		double blue = color1.getBlue() + (color2.getBlue() - color1.getBlue()) * scale;
		
		return new Color((int) red, (int) green, (int) blue);
	}
	
	public Color getVestigeColors(float seconds, long index) {
		index += 40;
		
        float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;

        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }

        hue2 = Math.max(hue2, 0);
        hue2 = Math.min(hue2, 1);
        
        return getGradient(new Color(5, 138, 255), new Color(0, 36, 217), hue2);
	}
	
	public Color getVestigeColors2(float seconds, long index) {
        index += 40;
        
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }
        
        return getGradient(new Color(0, 200, 235), new Color(20, 75, 230), hue2);
    }
	
	public Color customColors(Color c1, Color c2, boolean rainbow, float seconds, long index) {
        index += 40;
        
        if(rainbow) {
        	return getRainbow(seconds, 0.9F, 0.9F, index);
        }
        
        float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }

        return getGradient(c1, c2, hue2);
    }
	
	public Color customColors2(Color c1, Color c2, boolean rainbow, float seconds, long index) {
        index += 40;
        
        if(rainbow) {
        	return getRainbow(seconds, 0.8F, 0.85F, index);
        }
        
        float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (seconds * 1000);
        float hue2 = hue * 2;
        if(hue2 > 1) {
            hue2 = 2 - hue2;
        }

        return getGradient(c1, c2, hue2);
    }
	
}