/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

public class AnimationUtil {
    public static MSTimer TimerUtils = new MSTimer();

    public static float fastmax(float a, float b) {
        return a >= b ? a : b;
    }

    public static float fastmin(float a, float b) {
        return a <= b ? a : b;
    }

    public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = AnimationUtil.fastmax(minSpeed, movement);
            movement = AnimationUtil.fastmin(end - current, movement);
        } else if (movement < 0.0f) {
            movement = AnimationUtil.fastmin(-minSpeed, movement);
            movement = AnimationUtil.fastmax(end - current, movement);
        }
        return current + movement;
    }

    public static int moveUDl(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return (int)(current + movement);
    }

    public static float calculateCompensation(float target, float current, float f, float g) {
        float diff = current - target;
        if (f < 1.0f) {
            f = 1.0f;
        }
        if (diff > g) {
            double xD = (double)(g * f / 16.0f) < 0.25 ? 0.5 : (double)(g * f / 16.0f);
            if ((current -= (float)xD) < target) {
                current = target;
            }
        } else if (diff < -g) {
            double xD = (double)(g * f / 16.0f) < 0.25 ? 0.5 : (double)(g * f / 16.0f);
            if ((current += (float)xD) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > (float)speed) {
            double xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
            if ((current -= (float)xD) < target) {
                current = target;
            }
        } else if (diff < (float)(-speed)) {
            double xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
            if ((current += (float)xD) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)(0.01 * speed);
        animation = animation < finalState ? Math.min(animation + (double)add, finalState) : Math.max(animation - (double)add, finalState);
        return animation;
    }

    public static float getAnimationState(float animation, float finalState, float speed) {
        float add = (float)(0.01 * (double)speed);
        animation = animation < finalState ? Math.min(animation + add, finalState) : Math.max(animation - add, finalState);
        return animation;
    }

    public static int animatel(float target, float current, float speed) {
        if (TimerUtils.hasTimePassed(4L)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if ((double)speed > 1.0) {
                speed = 1.0f;
            }
            float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = larger ? current + factor : current - factor;
            TimerUtils.reset();
        }
        if ((double)Math.abs(current - target) < 0.2) {
            return (int)target;
        }
        return (int)current;
    }

    public static float animate(float target, float current, float speed) {
        if (TimerUtils.hasTimePassed(4L)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if ((double)speed > 1.0) {
                speed = 1.0f;
            }
            float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = larger ? current + factor : current - factor;
            TimerUtils.reset();
        }
        if ((double)Math.abs(current - target) < 0.2) {
            return target;
        }
        return current;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = Math.max(dif * speed, 1.0);
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current + factor > target ? target : (current += factor)) : (current - factor < target ? target : (current -= factor));
        return current;
    }

    public static Color getColorAnimationState(Color animation, Color finalState, double speed) {
        float add = (float)(0.01 * speed);
        float animationr = animation.getRed();
        float animationg = animation.getGreen();
        float animationb = animation.getBlue();
        float finalStater = finalState.getRed();
        float finalStateg = finalState.getGreen();
        float finalStateb = finalState.getBlue();
        float finalStatea = finalState.getAlpha();
        animationr = animationr < finalStater ? (animationr + add < finalStater ? (animationr += add) : finalStater) : (animationr - add > finalStater ? (animationr -= add) : finalStater);
        animationg = animationg < finalStateg ? (animationg + add < finalStateg ? (animationg += add) : finalStateg) : (animationg - add > finalStateg ? (animationg -= add) : finalStateg);
        animationb = animationb < finalStateb ? (animationb + add < finalStateb ? (animationb += add) : finalStateb) : (animationb - add > finalStateb ? (animationb -= add) : finalStateb);
        animationr /= 255.0f;
        animationg /= 255.0f;
        animationb /= 255.0f;
        finalStatea /= 255.0f;
        if (animationr > 1.0f) {
            animationr = 1.0f;
        }
        if (animationg > 1.0f) {
            animationg = 1.0f;
        }
        if (animationb > 1.0f) {
            animationb = 1.0f;
        }
        if (finalStatea > 1.0f) {
            finalStatea = 1.0f;
        }
        return new Color(animationr, animationg, animationb, finalStatea);
    }

    public static float clamp(float number, float min, float max) {
        return number < min ? min : Math.min(number, max);
    }
}

