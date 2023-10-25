/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

public enum EnumSkyBlock {
    SKY(15),
    BLOCK(0);

    public final int defaultLightValue;

    private EnumSkyBlock(int defaultLightValueIn) {
        this.defaultLightValue = defaultLightValueIn;
    }
}

