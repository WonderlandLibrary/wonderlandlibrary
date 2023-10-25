/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class Vec3 {
    public final double zCoord;
    public final double xCoord;
    public final double yCoord;

    public Vec3 subtract(Vec3 vec3) {
        return this.subtract(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public Vec3 crossProduct(Vec3 vec3) {
        return new Vec3(this.yCoord * vec3.zCoord - this.zCoord * vec3.yCoord, this.zCoord * vec3.xCoord - this.xCoord * vec3.zCoord, this.xCoord * vec3.yCoord - this.yCoord * vec3.xCoord);
    }

    public Vec3 getIntermediateWithZValue(Vec3 vec3, double d) {
        double d2 = vec3.xCoord - this.xCoord;
        double d3 = vec3.yCoord - this.yCoord;
        double d4 = vec3.zCoord - this.zCoord;
        if (d4 * d4 < (double)1.0E-7f) {
            return null;
        }
        double d5 = (d - this.zCoord) / d4;
        return d5 >= 0.0 && d5 <= 1.0 ? new Vec3(this.xCoord + d2 * d5, this.yCoord + d3 * d5, this.zCoord + d4 * d5) : null;
    }

    public Vec3 getIntermediateWithXValue(Vec3 vec3, double d) {
        double d2 = vec3.xCoord - this.xCoord;
        double d3 = vec3.yCoord - this.yCoord;
        double d4 = vec3.zCoord - this.zCoord;
        if (d2 * d2 < (double)1.0E-7f) {
            return null;
        }
        double d5 = (d - this.xCoord) / d2;
        return d5 >= 0.0 && d5 <= 1.0 ? new Vec3(this.xCoord + d2 * d5, this.yCoord + d3 * d5, this.zCoord + d4 * d5) : null;
    }

    public Vec3(Vec3i vec3i) {
        this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public Vec3 addVector(double d, double d2, double d3) {
        return new Vec3(this.xCoord + d, this.yCoord + d2, this.zCoord + d3);
    }

    public Vec3 subtract(double d, double d2, double d3) {
        return this.addVector(-d, -d2, -d3);
    }

    public Vec3 add(Vec3 vec3) {
        return this.addVector(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public Vec3 rotatePitch(float f) {
        float f2 = MathHelper.cos(f);
        float f3 = MathHelper.sin(f);
        double d = this.xCoord;
        double d2 = this.yCoord * (double)f2 + this.zCoord * (double)f3;
        double d3 = this.zCoord * (double)f2 - this.yCoord * (double)f3;
        return new Vec3(d, d2, d3);
    }

    public Vec3 getIntermediateWithYValue(Vec3 vec3, double d) {
        double d2 = vec3.xCoord - this.xCoord;
        double d3 = vec3.yCoord - this.yCoord;
        double d4 = vec3.zCoord - this.zCoord;
        if (d3 * d3 < (double)1.0E-7f) {
            return null;
        }
        double d5 = (d - this.yCoord) / d3;
        return d5 >= 0.0 && d5 <= 1.0 ? new Vec3(this.xCoord + d2 * d5, this.yCoord + d3 * d5, this.zCoord + d4 * d5) : null;
    }

    public Vec3 rotateYaw(float f) {
        float f2 = MathHelper.cos(f);
        float f3 = MathHelper.sin(f);
        double d = this.xCoord * (double)f2 + this.zCoord * (double)f3;
        double d2 = this.yCoord;
        double d3 = this.zCoord * (double)f2 - this.xCoord * (double)f3;
        return new Vec3(d, d2, d3);
    }

    public double squareDistanceTo(Vec3 vec3) {
        double d = vec3.xCoord - this.xCoord;
        double d2 = vec3.yCoord - this.yCoord;
        double d3 = vec3.zCoord - this.zCoord;
        return d * d + d2 * d2 + d3 * d3;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(this);
    }

    public Vec3(double d, double d2, double d3) {
        if (d == -0.0) {
            d = 0.0;
        }
        if (d2 == -0.0) {
            d2 = 0.0;
        }
        if (d3 == -0.0) {
            d3 = 0.0;
        }
        this.xCoord = d;
        this.yCoord = d2;
        this.zCoord = d3;
    }

    public double lengthVector() {
        return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public double distanceTo(Vec3 vec3) {
        double d = vec3.xCoord - this.xCoord;
        double d2 = vec3.yCoord - this.yCoord;
        double d3 = vec3.zCoord - this.zCoord;
        return MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3);
    }

    public double dotProduct(Vec3 vec3) {
        return this.xCoord * vec3.xCoord + this.yCoord * vec3.yCoord + this.zCoord * vec3.zCoord;
    }

    public Vec3 normalize() {
        double d = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return d < 1.0E-4 ? new Vec3(0.0, 0.0, 0.0) : new Vec3(this.xCoord / d, this.yCoord / d, this.zCoord / d);
    }

    public Vec3 subtractReverse(Vec3 vec3) {
        return new Vec3(vec3.xCoord - this.xCoord, vec3.yCoord - this.yCoord, vec3.zCoord - this.zCoord);
    }
}

