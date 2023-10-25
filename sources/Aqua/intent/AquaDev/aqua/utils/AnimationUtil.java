package intent.AquaDev.aqua.utils;

public final class AnimationUtil {
    public static float calculateCompensation(float target, float current, long delta, double speed) {
        double d0;
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        double d = d0 = speed * (double)delta / 16.0 < 0.5 ? 0.5 : speed * (double)delta / 16.0;
        if ((double)diff > speed) {
            double xD = d0;
            if ((current -= (float)xD) < target) {
                current = target;
            }
        } else if ((double)diff < -speed) {
            double xD = d0;
            if ((current += (float)xD) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static double animate(double current, double target, double speed) {
        double animated = current;
        if (current < target) {
            animated = current + speed > target ? target : (animated += speed);
        }
        if (current > target) {
            animated = current - speed < target ? target : (animated -= speed);
        }
        return animated;
    }
}
