/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public enum EnumWorldBlockLayer {
    SOLID("Solid"),
    CUTOUT_MIPPED("Mipped Cutout"),
    CUTOUT("Cutout"),
    TRANSLUCENT("Translucent");

    private final String layerName;

    public String toString() {
        return this.layerName;
    }

    private EnumWorldBlockLayer(String string2) {
        this.layerName = string2;
    }
}

