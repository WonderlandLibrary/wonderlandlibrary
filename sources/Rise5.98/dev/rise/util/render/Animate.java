package dev.rise.util.render;

import dev.rise.util.InstanceAccess;

public enum Animate implements InstanceAccess {
    getInstance();

    public float animate(float current, float end, float minSpeed) {
        float movement = (end - current) * 0.065f * 240 / mc.getDebugFPS();

        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
    }
}
