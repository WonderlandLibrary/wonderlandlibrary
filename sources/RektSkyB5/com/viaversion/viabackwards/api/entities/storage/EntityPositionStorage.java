/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.api.entities.storage;

public abstract class EntityPositionStorage {
    private double x;
    private double y;
    private double z;

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setCoordinates(double x2, double y2, double z2, boolean relative) {
        if (relative) {
            this.x += x2;
            this.y += y2;
            this.z += z2;
        } else {
            this.x = x2;
            this.y = y2;
            this.z = z2;
        }
    }
}

