package xyz.cucumber.base.utils.render;

import java.awt.Color;

import xyz.cucumber.base.module.settings.ColorSettings;

public class ColorUtils {
	
	public static int mix(int c1, int c2, double size, double max) {
		int f3 = (c1 >> 24 & 255);
		int f4 = (c1 >> 24 & 255);
		
        Color col1 = new Color(c1);
        Color col2 = new Color(c2);

        int diffR = (int) (col1.getRed()-(col1.getRed()-col2.getRed())/max*size);
        int diffG = (int) (col1.getGreen()-(col1.getGreen()-col2.getGreen())/max*size);
        int diffB = (int) (col1.getBlue()-(col1.getBlue()-col2.getBlue())/max*size);
        if(diffR > 255) diffR = 255;
        if(diffR < 0)diffR = 0;
        if(diffG > 255) diffG = 255;
        if(diffG < 0)diffG = 0;
        if(diffB > 255) diffB = 255;
        if(diffB < 0)diffB = 0;
        
        

        return new Color(diffR, diffG, diffB).getRGB();
    }
	
	public static int skyRainbow(double offset, float time, double speed) {
		return Color.HSBtoRGB(((float)((int)(time/speed + offset)) % 360) /360f ,0.5f, 1);
    }
	
	public static int rainbow(double speed, double offset) {
		return Color.HSBtoRGB(((float)(System.nanoTime()/1000000/speed + offset) % 360) /360f ,1, 1);
	}
	public static int rainbow(double speed, double offset, double milis) {
		return Color.HSBtoRGB(((float)(milis/speed + offset) % 360) /360f ,1, 1);
	}
	public static int getAlphaColor(int color, int alpha) {
		return new Color(new Color(color).getRed(),new Color(color).getGreen(),new Color(color).getBlue(), (int)((255f/100f)*(float)(alpha))).getRGB();
	}
	public static int getColor(ColorSettings color, double milis,double offset, double speed) {
		
		int c = getAlphaColor(color.getMainColor(), color.getAlpha());
		
		switch(color.getMode().toLowerCase()) {
		case "rainbow":
			c = getAlphaColor(rainbow(speed,offset,milis), color.getAlpha());
			break;
		case "mix":
			c = getAlphaColor(mix(color.getMainColor(), color.getSecondaryColor(), 1+Math.cos(Math.toRadians(milis/speed + offset)), 2) , color.getAlpha());
			break;
		case "sky":
			c = getAlphaColor(skyRainbow(offset, (float) milis, speed) , color.getAlpha());
			break;
		}
		
		return c;
	}
	
}
