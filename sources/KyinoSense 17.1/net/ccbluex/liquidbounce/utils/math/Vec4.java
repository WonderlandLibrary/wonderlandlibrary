/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Vec3
 */
package net.ccbluex.liquidbounce.utils.math;

import net.minecraft.util.Vec3;

public class Vec4 {
    private double x;
    private double y;
    private double z;

    public Vec4(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vec4 addVector(double x, double y, double z) {
        return new Vec4(this.x + x, this.y + y, this.z + z);
    }

    public Vec4 floor() {
        return new Vec4(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(Vec4 v) {
        return Math.pow(v.x - this.x, 2.0) + Math.pow(v.y - this.y, 2.0) + Math.pow(v.z - this.z, 2.0);
    }

    public Vec4 add(Vec4 v) {
        return this.addVector(v.getX(), v.getY(), v.getZ());
    }

    public Vec3 mc() {
        return new Vec3(this.x, this.y, this.z);
    }
}

