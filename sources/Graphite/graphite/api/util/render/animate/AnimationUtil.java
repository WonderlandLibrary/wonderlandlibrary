package graphite.api.util.render.animate;

import net.minecraft.client.Minecraft;

public class AnimationUtil {
	// based for use with system timer
    public static float calculateCompensation(float current, float target, long delta, int speed) {
        float diff = current - target;

        if (delta < 1) {
            delta = 1;
        }

        if (diff > speed) {
            double xD = (double) (speed * delta) / 16 < 0.25 ? 0.5 : (double) (speed * delta) / 16;
            current = (float) (current - xD);
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double xD = (double) (speed * delta) / 16 < 0.25 ? 0.5 : (double) (speed * delta) / 16;
            current = (float) (current + xD);
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }

        return current;
    }
    
    // based on mc fps
    public static double animateProgress(final double current, final double target, final double speed) {
        if (current < target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (target - current < inc) {
                return target;
            } else {
                return current + inc;
            }
        } else if (current > target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (current - target < inc) {
                return target;
            } else {
                return current - inc;
            }
        }

        return current;
    }
}