// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

public class Vec3
{
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
    
    public Vec3 subtractReverse(final Vec3 vec) {
        return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }
    
    public Vec3 normalize() {
        final double var1 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return (var1 < 1.0E-4) ? new Vec3(0.0, 0.0, 0.0) : new Vec3(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }
    
    public double dotProduct(final Vec3 vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }
    
    public Vec3 crossProduct(final Vec3 vec) {
        return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }
    
    public Vec3 subtract(final Vec3 p_178788_1_) {
        return this.subtract(p_178788_1_.xCoord, p_178788_1_.yCoord, p_178788_1_.zCoord);
    }
    
    public Vec3 subtract(final double p_178786_1_, final double p_178786_3_, final double p_178786_5_) {
        return this.addVector(-p_178786_1_, -p_178786_3_, -p_178786_5_);
    }
    
    public Vec3 add(final Vec3 p_178787_1_) {
        return this.addVector(p_178787_1_.xCoord, p_178787_1_.yCoord, p_178787_1_.zCoord);
    }
    
    public Vec3 addVector(final double x, final double y, final double z) {
        return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }
    
    public double distanceTo(final Vec3 vec) {
        final double var2 = vec.xCoord - this.xCoord;
        final double var3 = vec.yCoord - this.yCoord;
        final double var4 = vec.zCoord - this.zCoord;
        return MathHelper.sqrt_double(var2 * var2 + var3 * var3 + var4 * var4);
    }
    
    public double squareDistanceTo(final Vec3 vec) {
        final double var2 = vec.xCoord - this.xCoord;
        final double var3 = vec.yCoord - this.yCoord;
        final double var4 = vec.zCoord - this.zCoord;
        return var2 * var2 + var3 * var3 + var4 * var4;
    }
    
    public double lengthVector() {
        return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }
    
    public Vec3 getIntermediateWithXValue(final Vec3 vec, final double x) {
        final double var4 = vec.xCoord - this.xCoord;
        final double var5 = vec.yCoord - this.yCoord;
        final double var6 = vec.zCoord - this.zCoord;
        if (var4 * var4 < 1.0000000116860974E-7) {
            return null;
        }
        final double var7 = (x - this.xCoord) / var4;
        return (var7 >= 0.0 && var7 <= 1.0) ? new Vec3(this.xCoord + var4 * var7, this.yCoord + var5 * var7, this.zCoord + var6 * var7) : null;
    }
    
    public Vec3 getIntermediateWithYValue(final Vec3 vec, final double y) {
        final double var4 = vec.xCoord - this.xCoord;
        final double var5 = vec.yCoord - this.yCoord;
        final double var6 = vec.zCoord - this.zCoord;
        if (var5 * var5 < 1.0000000116860974E-7) {
            return null;
        }
        final double var7 = (y - this.yCoord) / var5;
        return (var7 >= 0.0 && var7 <= 1.0) ? new Vec3(this.xCoord + var4 * var7, this.yCoord + var5 * var7, this.zCoord + var6 * var7) : null;
    }
    
    public Vec3 getIntermediateWithZValue(final Vec3 vec, final double z) {
        final double var4 = vec.xCoord - this.xCoord;
        final double var5 = vec.yCoord - this.yCoord;
        final double var6 = vec.zCoord - this.zCoord;
        if (var6 * var6 < 1.0000000116860974E-7) {
            return null;
        }
        final double var7 = (z - this.zCoord) / var6;
        return (var7 >= 0.0 && var7 <= 1.0) ? new Vec3(this.xCoord + var4 * var7, this.yCoord + var5 * var7, this.zCoord + var6 * var7) : null;
    }
    
    @Override
    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }
    
    public Vec3 rotatePitch(final float p_178789_1_) {
        final float var2 = MathHelper.cos(p_178789_1_);
        final float var3 = MathHelper.sin(p_178789_1_);
        final double var4 = this.xCoord;
        final double var5 = this.yCoord * var2 + this.zCoord * var3;
        final double var6 = this.zCoord * var2 - this.yCoord * var3;
        return new Vec3(var4, var5, var6);
    }
    
    public Vec3 rotateYaw(final float p_178785_1_) {
        final float var2 = MathHelper.cos(p_178785_1_);
        final float var3 = MathHelper.sin(p_178785_1_);
        final double var4 = this.xCoord * var2 + this.zCoord * var3;
        final double var5 = this.yCoord;
        final double var6 = this.zCoord * var2 - this.xCoord * var3;
        return new Vec3(var4, var5, var6);
    }
}
