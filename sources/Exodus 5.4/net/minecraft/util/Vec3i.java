/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 */
package net.minecraft.util;

import com.google.common.base.Objects;
import net.minecraft.util.MathHelper;

public class Vec3i
implements Comparable<Vec3i> {
    private final int y;
    private final int x;
    public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
    private final int z;

    public double distanceSq(double d, double d2, double d3) {
        double d4 = (double)this.getX() - d;
        double d5 = (double)this.getY() - d2;
        double d6 = (double)this.getZ() - d3;
        return d4 * d4 + d5 * d5 + d6 * d6;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Vec3i)) {
            return false;
        }
        Vec3i vec3i = (Vec3i)object;
        return this.getX() != vec3i.getX() ? false : (this.getY() != vec3i.getY() ? false : this.getZ() == vec3i.getZ());
    }

    public Vec3i crossProduct(Vec3i vec3i) {
        return new Vec3i(this.getY() * vec3i.getZ() - this.getZ() * vec3i.getY(), this.getZ() * vec3i.getX() - this.getX() * vec3i.getZ(), this.getX() * vec3i.getY() - this.getY() * vec3i.getX());
    }

    @Override
    public int compareTo(Vec3i vec3i) {
        return this.getY() == vec3i.getY() ? (this.getZ() == vec3i.getZ() ? this.getX() - vec3i.getX() : this.getZ() - vec3i.getZ()) : this.getY() - vec3i.getY();
    }

    public Vec3i(double d, double d2, double d3) {
        this(MathHelper.floor_double(d), MathHelper.floor_double(d2), MathHelper.floor_double(d3));
    }

    public double distanceSq(Vec3i vec3i) {
        return this.distanceSq(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public Vec3i(int n, int n2, int n3) {
        this.x = n;
        this.y = n2;
        this.z = n3;
    }

    public int getZ() {
        return this.z;
    }

    public double distanceSqToCenter(double d, double d2, double d3) {
        double d4 = (double)this.getX() + 0.5 - d;
        double d5 = (double)this.getY() + 0.5 - d2;
        double d6 = (double)this.getZ() + 0.5 - d3;
        return d4 * d4 + d5 * d5 + d6 * d6;
    }

    public int getX() {
        return this.x;
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    public int getY() {
        return this.y;
    }
}

