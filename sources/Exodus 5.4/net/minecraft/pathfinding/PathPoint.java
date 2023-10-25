/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

public class PathPoint {
    public boolean visited;
    int index = -1;
    PathPoint previous;
    private final int hash;
    float distanceToTarget;
    float distanceToNext;
    float totalPathDistance;
    public final int zCoord;
    public final int yCoord;
    public final int xCoord;

    public boolean isAssigned() {
        return this.index >= 0;
    }

    public float distanceToSquared(PathPoint pathPoint) {
        float f = pathPoint.xCoord - this.xCoord;
        float f2 = pathPoint.yCoord - this.yCoord;
        float f3 = pathPoint.zCoord - this.zCoord;
        return f * f + f2 * f2 + f3 * f3;
    }

    public float distanceTo(PathPoint pathPoint) {
        float f = pathPoint.xCoord - this.xCoord;
        float f2 = pathPoint.yCoord - this.yCoord;
        float f3 = pathPoint.zCoord - this.zCoord;
        return MathHelper.sqrt_float(f * f + f2 * f2 + f3 * f3);
    }

    public static int makeHash(int n, int n2, int n3) {
        return n2 & 0xFF | (n & Short.MAX_VALUE) << 8 | (n3 & Short.MAX_VALUE) << 24 | (n < 0 ? Integer.MIN_VALUE : 0) | (n3 < 0 ? 32768 : 0);
    }

    public boolean equals(Object object) {
        if (!(object instanceof PathPoint)) {
            return false;
        }
        PathPoint pathPoint = (PathPoint)object;
        return this.hash == pathPoint.hash && this.xCoord == pathPoint.xCoord && this.yCoord == pathPoint.yCoord && this.zCoord == pathPoint.zCoord;
    }

    public int hashCode() {
        return this.hash;
    }

    public PathPoint(int n, int n2, int n3) {
        this.xCoord = n;
        this.yCoord = n2;
        this.zCoord = n3;
        this.hash = PathPoint.makeHash(n, n2, n3);
    }

    public String toString() {
        return String.valueOf(this.xCoord) + ", " + this.yCoord + ", " + this.zCoord;
    }
}

