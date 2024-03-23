package dev.zprestige.prestige.client.util.impl;

import dev.zprestige.prestige.client.setting.impl.DragSetting;

public class TimerUtil {
    public long lastMs = System.currentTimeMillis();

    public boolean delay(float f) {
        return (float)(System.currentTimeMillis() - lastMs) > f;
    }

    public boolean delay(DragSetting dragSetting) {
        return delay(dragSetting.getValue());
    }

    public float getElapsedTime() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
}
