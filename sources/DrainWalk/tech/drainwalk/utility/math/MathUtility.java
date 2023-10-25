package tech.drainwalk.utility.math;

import tech.drainwalk.utility.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtility extends Utility {

    public static float interpolate(float prev, float to, float value) {
        return prev + (to - prev) * value;
    }

    public static double round(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static int randomNumber(int min, int max) {
        return (int) (min + (double) (max - min) * Math.random());
    }

    public static float clamp(float value,float min, float max) {
        if(value <= min) {
            return min;
        }
        if(value >= max) {
            return max;
        }
        return value;
    }
}
