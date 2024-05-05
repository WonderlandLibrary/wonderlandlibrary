package epsilon.util;

import java.awt.Color;

import epsilon.Epsilon;
import epsilon.modules.render.Theme;

public class ColorUtil {
	public static int astolfocount;
	
	private static Timer timer = new Timer();
	private int color;
	
	
	private ColorUtil(int color) {
		this.color = color;
	}
	
	public static int getColorAsInt(float colorOffset, final String type, final float timeMultiplier) {
		
		
		return getColor(colorOffset, type, timeMultiplier).hashCode();
		
	}
	
	public static Color mixColors(final Color color1, final Color color2, final double percent) {
        final double inverse_percent = 1.0 - percent;
        final int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        final int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        final int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
	
	public static int getStitchColors() {
		return 1;
	}
	
	private static Color getColor(final String mode) {
		
		Color color = new Color (255, 255, 255, 255);
		
		switch(mode) {
		
		case "Stitch":
			
			color = new Color(0, 106, 255, 255);
			
			break;
		
		case "Tena":
		case "Tenacity":

			color = new Color(255, 102, 250, 255);
			
			break;
		
		
		}
		return color;
	}
	
	public static Color getColor(float colorOffset, final String type, final float timeMultiplier) {
		Color color = new Color(255,255,255,255);
        if (timer.hasTimeElapsed(250, true)) {
            color = getColor(type);
        }


        float colorOffsetMultiplier = 1;

        switch (type) {
        	
        case "Stitch":
        	
        	colorOffsetMultiplier = 2.5f;
        	
        	break;
        	
        case "Tena":
        case "Tenacity":

        	colorOffsetMultiplier = 2;
        	
        	break;
        
        }
        

        colorOffset *= colorOffsetMultiplier;

        final double timer = (System.currentTimeMillis() / 1E+8 * timeMultiplier) * 4E+5;

        final double factor = (Math.sin(timer + colorOffset * 0.5f) + 1) * 0.5f;
        switch (type) {

        case "Custom":
        	
        	Color color1 = new Color(0, 251, 255, 255), color2 = new Color(0, 0, 255, 255);
        	
        	switch(Theme.color1.getMode()) {
        	case "Red":
        		color1 = new Color(255, 0, 0, 255);
        		break;
        	case"Orange":
        		color1 = new Color(255, 110, 0, 255);
        		break;
        	case "Yellow":
        		color1 = new Color(255, 255, 0, 255);
        		break;
        	case "Green":
        		color1 = new Color(0, 255, 0, 255);
        		break;
        	case "LightBlue":
        		color1 = new Color(0, 251, 255, 255);
        		break;
        	case "Blue":
        		color1 = new Color(0, 0, 255, 255);
        		break;
        	case "Purple":
        		color1 = new Color(221, 0, 255, 255);
        		break;
        	case "Pink":
        		color1 = new Color(255, 0, 179, 255);
        		break;
        	case "White":
        		color1 = new Color(220, 220, 220, 255);
        		break;
        	}
        	switch(Theme.color2.getMode()) {
        	case "Red":
        		color2 = new Color(255, 0, 0, 255);
        		break;
        	case"Orange":
        		color2 = new Color(255, 110, 0, 255);
        		break;
        	case "Yellow":
        		color2 = new Color(255, 255, 0, 255);
        		break;
        	case "Green":
        		color2 = new Color(0, 255, 0, 255);
        		break;
        	case "LightBlue":
        		color2 = new Color(0, 251, 255, 255);
        		break;
        	case "Blue":
        		color2 = new Color(0, 0, 255, 255);
        		break;
        	case "Purple":
        		color2 = new Color(221, 0, 255, 255);
        		break;
        	case "Pink":
        		color2 = new Color(255, 0, 179, 255);
        		break;
        	case "White":
        		color2 = new Color(220, 220, 220, 255);
        		break;
        	}

        	color = mixColors(color1, color2, factor);
        	break;

            case "Stitch":
            	
            	color = mixColors(new Color(0, 106, 255, 255), new Color(0, 251, 255, 255), factor);
            	
            	break;
            	
            case "CottonCandy":
            case "CC":
            	
            	color = mixColors(new Color(255, 102, 250, 255), new Color(71, 200, 255, 255), factor);
            	
            	break;
            	
            case "Tena":
            case "Tenacity":

            	color = mixColors(new Color(186, 83, 212, 255), new Color(4, 103, 209, 255), factor);
            	
            	break;
            	
            case "Xeno":

            	color = mixColors(new Color(78, 84, 200, 255), new Color(143, 148, 251, 255), factor);
            	
            	break;
            	
            case "Superhero":
            	
            	color = mixColors(new Color(0, 119, 255, 255), new Color(255, 0, 25, 255), factor);
            	
            	break;
            	
            case "Spongebob":

            	color = mixColors(new Color(255, 230, 0, 255), new Color(102, 62, 9, 255), factor);
            	
            	break;
            	
            case "Grass":

            	color = mixColors(new Color(43, 199, 0, 255), new Color(102, 62, 9, 255), factor);
            	
            	break;
            	
            case "PissWater":

            	color = mixColors(new Color(255, 230, 0, 255), new Color(23, 170, 255, 255), factor);
            	
            	break;
            	
            case "YellowSnow":

            	color = mixColors(new Color(255, 204, 0, 255), new Color(243, 255, 199, 255), factor);
            	
            	break;
        
        }

        return color;
    }
	
	public static int getRainbow(float seconds, float saturation, float brightness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	/*public static int getPurple(float seconds, float saturation, float brightness, long index) {
		
	}*/
	
	public static Color getMixer(float seconds, Color color1, Color color2) {
		Color colorFinal = new Color(0,0,0,0);
		return colorFinal;
		
	}
	
	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	public static int OldgetRainbow(float seconds, float saturation, float brightness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	
	public static int getAstolfoRainbow(float seconds, float saturation, float brightness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);

		 if(hue >= 0.5){
			hue = 0.5f + (1-hue);
		} else if(hue < 0.5){
			hue += 0.5;
		}


		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}

}