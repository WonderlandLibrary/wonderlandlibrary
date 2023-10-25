/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.dispenser;

import net.minecraft.dispenser.IPosition;

public class PositionImpl
implements IPosition {
    protected final double z;
    protected final double y;
    protected final double x;

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public double getX() {
        return this.x;
    }

    public PositionImpl(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    @Override
    public double getY() {
        return this.y;
    }
}

