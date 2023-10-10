/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;

public abstract class PlayerPositionStorage
implements StorableObject {
    private double x;
    private double y;
    private double z;

    protected PlayerPositionStorage() {
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x2) {
        this.x = x2;
    }

    public void setY(double y2) {
        this.y = y2;
    }

    public void setZ(double z2) {
        this.z = z2;
    }

    public void setCoordinates(PacketWrapper wrapper, boolean relative) throws Exception {
        this.setCoordinates(wrapper.get(Type.DOUBLE, 0), wrapper.get(Type.DOUBLE, 1), wrapper.get(Type.DOUBLE, 2), relative);
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

