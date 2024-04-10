package info.sigmaclient.sigma.utils.render.anims;

import info.sigmaclient.sigma.utils.TimerUtil;

public class AnimationUtils {
    private static float defaultSpeed = 0.125f;
    private TimerUtil timerUtil = new TimerUtil();

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        if ((double) diff > speed) {
            double xD = Math.max(speed * (double) delta / 16.0, 0.5);
            if ((current = (float) ((double) current - xD)) < target) {
                current = target;
            }
        } else if ((double) diff < -speed) {
            double xD = Math.max(speed * (double) delta / 16.0, 0.5);
            if ((current = (float) ((double) current + xD)) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public float mvoeUD(float current, float end, float minSpeed) {
        return moveUD(current, end, defaultSpeed, minSpeed);
    }

    public double animate(double target, double current, double speed) {
        if(timerUtil.hasTimeElapsed(20)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0) {
                speed = 0.0;
            } else if (speed > 1.0) {
                speed = 1.0;
            }
            double dif = Math.max(target, current) - Math.min(target, current);
            double factor = dif * speed;
//            if(Math.abs(factor) > 0.05){
//                factor = factor > 0 ? 0.5 : -0.5;
//            }

            current = larger ? current + factor : current - factor;
            timerUtil.reset();
        }
        if (Math.abs(current - target) <= 0.1) {
            return target;
        } else {
            return current;
        }
    }
    public double animateHigh(double target, double current, double speed) {
        boolean larger = target > current;
        if(timerUtil.hasTimeElapsed(20)) {
            if (speed < 0.0) {
                speed = 0.0;
            } else if (speed > 1.0) {
                speed = 1.0;
            }
            double dif = Math.max(target, current) - Math.min(target, current);
            double factor = dif * speed;

            current = larger ? current + factor : current - factor;
            timerUtil.reset();
        }
        if (Math.abs(current - target) <= 0.01 || (target > 0 && current >= target) || (target < 0 && current <= target)) {
            return target;
        } else {
            return current;
        }
    }
    public float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = 0;
        if (timerUtil.hasTimeElapsed(20)) {
            movement = (end - current) * smoothSpeed;
            if (movement > 0.0f) {
                movement = Math.max((float) minSpeed, (float) movement);
                movement = Math.min((float) (end - current), (float) movement);
            } else if (movement < 0.0f) {
                movement = Math.min((float) (-minSpeed), (float) movement);
                movement = Math.max((float) (end - current), (float) movement);
            }
            timerUtil.reset();
        }
        return current + movement;
    }

}
