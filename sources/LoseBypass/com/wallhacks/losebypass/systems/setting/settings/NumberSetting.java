/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

public interface NumberSetting {
    default public double getNumber() {
        return 0.0;
    }

    default public double getMin() {
        return 0.0;
    }

    default public double getMax() {
        return 0.0;
    }

    default public void setNumber(double value) {
    }
}

