/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.minecraft;

public class Vector {
    private int blockX;
    private int blockY;
    private int blockZ;

    public Vector(int blockX, int blockY, int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    public int getBlockX() {
        return this.blockX;
    }

    public void setBlockX(int blockX) {
        this.blockX = blockX;
    }

    public int getBlockY() {
        return this.blockY;
    }

    public void setBlockY(int blockY) {
        this.blockY = blockY;
    }

    public int getBlockZ() {
        return this.blockZ;
    }

    public void setBlockZ(int blockZ) {
        this.blockZ = blockZ;
    }
}

