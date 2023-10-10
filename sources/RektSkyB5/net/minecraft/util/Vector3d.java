/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public Vector3d() {
        this.z = 0.0;
        this.y = 0.0;
        this.x = 0.0;
    }

    public Vector3d(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Vector3d add(Vector3d vec) {
        Vector3d newVec = new Vector3d(this.x, this.y, this.z);
        newVec.x += vec.x;
        newVec.y += vec.y;
        newVec.z += vec.z;
        return newVec;
    }
}

