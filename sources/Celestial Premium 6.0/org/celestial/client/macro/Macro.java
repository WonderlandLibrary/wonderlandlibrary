/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.macro;

public class Macro {
    public String value;
    public int key;

    public Macro(int key, String macroValue) {
        this.key = key;
        this.value = macroValue;
    }

    public int getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}

