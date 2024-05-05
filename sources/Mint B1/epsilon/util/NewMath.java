package epsilon.util;

import java.util.Random;

public class NewMath {
	
	private static Random rand = new Random();

    private static final float DEGREES_TO_RADIUS = 3.1415926f / 180.0f;
    private static final float RADIUS_TO_DEGREES = 180.0f / 3.1415926f;

    public static double randomInRange(final double min, final double max) {
        return rand.nextInt((int) (max - min + 1.0)) + max;
    }
    
    public static double random() {
    	return rand.nextDouble();
    }
    

}
