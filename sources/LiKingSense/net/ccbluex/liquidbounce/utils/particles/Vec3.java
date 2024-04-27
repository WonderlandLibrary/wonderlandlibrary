/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3i
 */
package net.ccbluex.liquidbounce.utils.particles;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class Vec3 {
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public Vec3(double x, double y, double z) {
        if (x == -0.0) {
            x = 0.0;
        }
        if (y == -0.0) {
            y = 0.0;
        }
        if (z == -0.0) {
            z = 0.0;
        }
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public Vec3(Vec3i p_i46377_1_) {
        this(p_i46377_1_.func_177958_n(), p_i46377_1_.func_177956_o(), p_i46377_1_.func_177952_p());
    }

    public Vec3 subtractReverse(Vec3 vec) {
        return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public Vec3 normalize() {
        double d0 = MathHelper.func_76133_a((double)(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord));
        return d0 < 1.0E-4 ? new Vec3(0.0, 0.0, 0.0) : new Vec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }

    public double dotProduct(Vec3 vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    public Vec3 crossProduct(Vec3 vec) {
        return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3 subtract(Vec3 vec) {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3 subtract(double x, double y, double z) {
        return this.addVector(-x, -y, -z);
    }

    public Vec3 add(Vec3 vec) {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3 addVector(double x, double y, double z) {
        return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public double distanceTo(Vec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        return MathHelper.func_76133_a((double)(d0 * d0 + d1 * d1 + d2 * d2));
    }

    public double squareDistanceTo(Vec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double lengthVector() {
        return MathHelper.func_76133_a((double)(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord));
    }

    public Vec3 getIntermediateWithXValue(Vec3 vec, double x) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        if (d0 * d0 < (double)1.0E-7f) {
            return null;
        }
        double d3 = (x - this.xCoord) / d0;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
    }

    public Vec3 getIntermediateWithYValue(Vec3 vec, double y) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        if (d1 * d1 < (double)1.0E-7f) {
            return null;
        }
        double d3 = (y - this.yCoord) / d1;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
    }

    public Vec3 getIntermediateWithZValue(Vec3 vec, double z) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        if (d2 * d2 < (double)1.0E-7f) {
            return null;
        }
        double d3 = (z - this.zCoord) / d2;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public Vec3 rotatePitch(float pitch) {
        float f = MathHelper.func_76134_b((float)pitch);
        float f1 = MathHelper.func_76126_a((float)pitch);
        double d0 = this.xCoord;
        double d1 = this.yCoord * (double)f + this.zCoord * (double)f1;
        double d2 = this.zCoord * (double)f - this.yCoord * (double)f1;
        return new Vec3(d0, d1, d2);
    }

    public Vec3 rotateYaw(float yaw) {
        float f = MathHelper.func_76134_b((float)yaw);
        float f1 = MathHelper.func_76126_a((float)yaw);
        double d0 = this.xCoord * (double)f + this.zCoord * (double)f1;
        double d1 = this.yCoord;
        double d2 = this.zCoord * (double)f - this.xCoord * (double)f1;
        return new Vec3(d0, d1, d2);
    }
}

