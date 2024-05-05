/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

public interface Toggleable {
    default public void toggle() {
    }

    default public boolean isOn() {
        return true;
    }
}

