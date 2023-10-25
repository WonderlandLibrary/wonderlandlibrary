/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.border;

public enum EnumBorderStatus {
    GROWING(4259712),
    SHRINKING(0xFF3030),
    STATIONARY(2138367);

    private final int id;

    public int getID() {
        return this.id;
    }

    private EnumBorderStatus(int n2) {
        this.id = n2;
    }
}

