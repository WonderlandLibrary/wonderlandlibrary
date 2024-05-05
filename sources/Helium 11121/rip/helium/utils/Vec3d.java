package rip.helium.utils;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class Vec3d {
    public static final Vec3d ZERO;

    static {
        ZERO = new Vec3d(0.0, 0.0, 0.0);
    }

    public final double xCoord;
    public final double yCoord;
    public final double zCoord;

    public Vec3d(double x, double y2, double z) {
        if (x == -0.0) {
            x = 0.0;
        }
        if (y2 == -0.0) {
            y2 = 0.0;
        }
        if (z == -0.0) {
            z = 0.0;
        }
        this.xCoord = x;
        this.yCoord = y2;
        this.zCoord = z;
    }

    public Vec3d(final Vec3i vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vec3d fromPitchYaw(final float p_189986_0_, final float p_189986_1_) {
        final float f = MathHelper.cos(-p_189986_1_ * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-p_189986_1_ * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-p_189986_0_ * 0.017453292f);
        final float f4 = MathHelper.sin(-p_189986_0_ * 0.017453292f);
        return new Vec3d(f2 * f3, f4, f * f3);
    }

    public Vec3d subtractReverse(final Vec3d vec) {
        return new Vec3d(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public Vec3d normalize() {
        final double d0 = Math.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return (d0 < 1.0E-4) ? Vec3d.ZERO : new Vec3d(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }

    public double dotProduct(final Vec3d vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    public Vec3d crossProduct(final Vec3d vec) {
        return new Vec3d(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3d subtract(final Vec3d vec) {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3d subtract(final double x, final double y2, final double z) {
        return this.addVector(-x, -y2, -z);
    }

    public Vec3d add(final Vec3d vec) {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3d addVector(final double x, final double y2, final double z) {
        return new Vec3d(this.xCoord + x, this.yCoord + y2, this.zCoord + z);
    }

    public double distanceTo(final Vec3d vec) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        return Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public double squareDistanceTo(final Vec3d vec) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }

    public double squareDistanceTo(final double xIn, final double yIn, final double zIn) {
        final double d0 = xIn - this.xCoord;
        final double d2 = yIn - this.yCoord;
        final double d3 = zIn - this.zCoord;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }

    public Vec3d scale(final double p_186678_1_) {
        return new Vec3d(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, this.zCoord * p_186678_1_);
    }

    public double lengthVector() {
        return Math.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public double lengthSquared() {
        return this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord;
    }

    public Vec3d getIntermediateWithXValue(final Vec3d vec, final double x) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        if (d0 * d0 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (x - this.xCoord) / d0;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3d(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
    }

    public Vec3d getIntermediateWithYValue(final Vec3d vec, final double y2) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        if (d2 * d2 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (y2 - this.yCoord) / d2;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3d(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
    }

    public Vec3d getIntermediateWithZValue(final Vec3d vec, final double z) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        if (d3 * d3 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (z - this.zCoord) / d3;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3d(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
    }

    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Vec3d)) {
            return false;
        }
        final Vec3d vec3d = (Vec3d) p_equals_1_;
        return Double.compare(vec3d.xCoord, this.xCoord) == 0 && Double.compare(vec3d.yCoord, this.yCoord) == 0 && Double.compare(vec3d.zCoord, this.zCoord) == 0;
    }

    @Override
    public int hashCode() {
        long j = Double.doubleToLongBits(this.xCoord);
        int i = (int) (j ^ j >>> 32);
        j = Double.doubleToLongBits(this.yCoord);
        i = 31 * i + (int) (j ^ j >>> 32);
        j = Double.doubleToLongBits(this.zCoord);
        i = 31 * i + (int) (j ^ j >>> 32);
        return i;
    }

    @Override
    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public Vec3d rotatePitch(final float pitch) {
        final float f = MathHelper.cos(pitch);
        final float f2 = MathHelper.sin(pitch);
        final double d0 = this.xCoord;
        final double d2 = this.yCoord * f + this.zCoord * f2;
        final double d3 = this.zCoord * f - this.yCoord * f2;
        return new Vec3d(d0, d2, d3);
    }

    public Vec3d rotateYaw(final float yaw) {
        final float f = MathHelper.cos(yaw);
        final float f2 = MathHelper.sin(yaw);
        final double d0 = this.xCoord * f + this.zCoord * f2;
        final double d2 = this.yCoord;
        final double d3 = this.zCoord * f - this.xCoord * f2;
        return new Vec3d(d0, d2, d3);
    }
}