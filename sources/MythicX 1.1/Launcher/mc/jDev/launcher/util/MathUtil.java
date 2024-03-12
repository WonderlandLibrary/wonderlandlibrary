package mc.jDev.launcher.util;

public class MathUtil {
    public static String betadl;
    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
    public static int clamp(int a, int b) {
        if(a > b) {
            return b;
        }
        return a;
    }
}
