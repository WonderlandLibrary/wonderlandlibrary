/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.setting.settings;

public interface EnumSetting {
    default public void increment() {
    }

    default public String getValueName() {
        return "";
    }

    default public int getValueIndex() {
        return 0;
    }
}

