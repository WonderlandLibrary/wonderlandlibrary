package summer.base.utilities;

public enum AnimationUtil {

    INSTANCE;

    public float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }

        double xD;
        if (diff > (float) speed) {
            xD = (double) ((long) speed * delta / 16L) < 0.25D ? 0.5D : (double) ((long) speed * delta / 16L);
            current = (float) ((double) current - xD);
            if (current < target) {
                current = target;
            }
        } else if (diff < (float) (-speed)) {
            xD = (double) ((long) speed * delta / 16L) < 0.25D ? 0.5D : (double) ((long) speed * delta / 16L);
            current = (float) ((double) current + xD);
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }

        return current;
    }

    public double animate(final double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }

    public float value(long startTime) {
        return Math.min(1.0F, (float) Math.pow((double) (System.currentTimeMillis() - startTime) / 10.0D, 1.4D) / 80.0F);
    }

}
