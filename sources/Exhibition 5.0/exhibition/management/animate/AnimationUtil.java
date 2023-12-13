// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.animate;

public class AnimationUtil
{
    public static float calculateCompensation(final float target, float current, long delta, final int speed) {
        final float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > speed) {
            final double xD = (speed * delta / 16L < 0.5) ? 0.5 : (speed * delta / 16L);
            current -= (float)xD;
            if (current < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            final double xD = (speed * delta / 16L < 0.5) ? 0.5 : (speed * delta / 16L);
            current += (float)xD;
            if (current > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
}
