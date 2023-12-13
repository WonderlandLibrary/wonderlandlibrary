/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.api.minecraft;

import com.viaversion.viaversion.api.minecraft.BlockFace;

public class Position {
    private final int x;
    private final int y;
    private final int z;

    public Position(int x2, int y2, int z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    @Deprecated
    public Position(int x2, short y2, int z2) {
        this(x2, (int)y2, z2);
    }

    @Deprecated
    public Position(Position toCopy) {
        this(toCopy.x(), toCopy.y(), toCopy.z());
    }

    public Position getRelative(BlockFace face) {
        return new Position(this.x + face.modX(), (short)(this.y + face.modY()), this.z + face.modZ());
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int z() {
        return this.z;
    }

    @Deprecated
    public int getX() {
        return this.x;
    }

    @Deprecated
    public int getY() {
        return this.y;
    }

    @Deprecated
    public int getZ() {
        return this.z;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Position position = (Position)o2;
        if (this.x != position.x) {
            return false;
        }
        if (this.y != position.y) {
            return false;
        }
        return this.z == position.z;
    }

    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    public String toString() {
        return "Position{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
}

