/*
 * Decompiled with CFR 0.152.
 */
package de.gerrygames.viarewind.utils.math;

import java.util.Objects;

public class Vector3d {
    double x;
    double y;
    double z;

    public Vector3d(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Vector3d() {
    }

    public void set(Vector3d vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vector3d set(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
        return this;
    }

    public Vector3d multiply(double a2) {
        this.x *= a2;
        this.y *= a2;
        this.z *= a2;
        return this;
    }

    public Vector3d add(Vector3d vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vector3d substract(Vector3d vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3d normalize() {
        double length = this.length();
        this.multiply(1.0 / length);
        return this;
    }

    public Vector3d clone() {
        return new Vector3d(this.x, this.y, this.z);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Vector3d vector3d = (Vector3d)o2;
        return Double.compare(vector3d.x, this.x) == 0 && Double.compare(vector3d.y, this.y) == 0 && Double.compare(vector3d.z, this.z) == 0;
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }

    public String toString() {
        return "Vector3d{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
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
}

