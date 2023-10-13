/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.MathHelper;

public class Vec3 {
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;
    private static final String __OBFID = "CL_00000612";

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

    public Vec3 subtractReverse(Vec3 vec) {
        return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public Vec3 normalize() {
        double var1 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return var1 < 1.0E-4 ? new Vec3(0.0, 0.0, 0.0) : new Vec3(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }

    public double dotProduct(Vec3 vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    public Vec3 crossProduct(Vec3 vec) {
        return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3 subtract(Vec3 p_178788_1_) {
        return this.subtract(p_178788_1_.xCoord, p_178788_1_.yCoord, p_178788_1_.zCoord);
    }

    public Vec3 subtract(double p_178786_1_, double p_178786_3_, double p_178786_5_) {
        return this.addVector(-p_178786_1_, -p_178786_3_, -p_178786_5_);
    }

    public Vec3 add(Vec3 p_178787_1_) {
        return this.addVector(p_178787_1_.xCoord, p_178787_1_.yCoord, p_178787_1_.zCoord);
    }

    public Vec3 addVector(double x, double y, double z) {
        return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public double distanceTo(Vec3 vec) {
        double var2 = vec.xCoord - this.xCoord;
        double var4 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }

    public double squareDistanceTo(Vec3 vec) {
        double var2 = vec.xCoord - this.xCoord;
        double var4 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public double lengthVector() {
        return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3 getIntermediateWithXValue(Vec3 vec, double x) {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;
        if (var4 * var4 < (double)1.0E-7f) {
            return null;
        }
        double var10 = (x - this.xCoord) / var4;
        return var10 >= 0.0 && var10 <= 1.0 ? new Vec3(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
    }

    public Vec3 getIntermediateWithYValue(Vec3 vec, double y) {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;
        if (var6 * var6 < (double)1.0E-7f) {
            return null;
        }
        double var10 = (y - this.yCoord) / var6;
        return var10 >= 0.0 && var10 <= 1.0 ? new Vec3(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
    }

    public Vec3 getIntermediateWithZValue(Vec3 vec, double z) {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;
        if (var8 * var8 < (double)1.0E-7f) {
            return null;
        }
        double var10 = (z - this.zCoord) / var8;
        return var10 >= 0.0 && var10 <= 1.0 ? new Vec3(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public Vec3 rotatePitch(float p_178789_1_) {
        float var2 = MathHelper.cos(p_178789_1_);
        float var3 = MathHelper.sin(p_178789_1_);
        double var4 = this.xCoord;
        double var6 = this.yCoord * (double)var2 + this.zCoord * (double)var3;
        double var8 = this.zCoord * (double)var2 - this.yCoord * (double)var3;
        return new Vec3(var4, var6, var8);
    }

    public Vec3 rotateYaw(float p_178785_1_) {
        float var2 = MathHelper.cos(p_178785_1_);
        float var3 = MathHelper.sin(p_178785_1_);
        double var4 = this.xCoord * (double)var2 + this.zCoord * (double)var3;
        double var6 = this.yCoord;
        double var8 = this.zCoord * (double)var2 - this.xCoord * (double)var3;
        return new Vec3(var4, var6, var8);
    }
}

