package wtf.evolution.helpers.math;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {

    //millis to HH:MM:SS
    public static String format(long millis) {
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        long seconds = ((millis % 360000) % 60000) / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }
    // interpolate
    public static double interpolate (double current, double old, double scale) {
        return old + (current - old) * scale;
    }
    public static float interpolate (float current, float old, double scale) {
        return (float) interpolate((double) current, (double) old, scale);
    }
    public static int interpolate (int current, int old, double scale) {
        return (int) interpolate((double) current, (double) old, scale);
    }
    public static double round(double num, double increment) {
        double v = (double)Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static int getRandomNumberBetween(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static double getRandomNumberBetween(double min, double max) {
        return Math.random() * (max - min) + min;
    }



}
