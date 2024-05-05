package ru.smertnix.celestial.utils.math.animations;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class AnimationHelper {

    public static float animation(float animation, float target, float speedTarget) {
        float dif = (target - animation) / Math.max((float) Minecraft.getDebugFPS(), 5) * 15;

        if (dif > 0) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        } else if (dif < 0) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }

    public static double Interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float Move(float from, float to, float minstep, float maxstep, float factor) {

        float f = (to - from) * MathHelper.clamp(factor,0,1);

        if (f < 0)
            f = MathHelper.clamp(f, -maxstep, -minstep);
        else
            f = MathHelper.clamp(f, minstep, maxstep);

        if(Math.abs(f) > Math.abs(to - from))
            return to;

        return from + f;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        double dif = (Math.max(speed * delta / (1000 / 60F), 0.5));
        if (diff > speed) {
            current -= dif;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            current += dif;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }
}
