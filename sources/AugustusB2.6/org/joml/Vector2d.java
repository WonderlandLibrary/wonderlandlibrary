// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.text.NumberFormat;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Vector2d implements Externalizable, Cloneable, Vector2dc
{
    private static final long serialVersionUID = 1L;
    public double x;
    public double y;
    
    public Vector2d() {
    }
    
    public Vector2d(final double d) {
        this.x = d;
        this.y = d;
    }
    
    public Vector2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2d(final Vector2dc v) {
        this.x = v.x();
        this.y = v.y();
    }
    
    public Vector2d(final Vector2fc v) {
        this.x = v.x();
        this.y = v.y();
    }
    
    public Vector2d(final Vector2ic v) {
        this.x = v.x();
        this.y = v.y();
    }
    
    public Vector2d(final double[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }
    
    public Vector2d(final float[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }
    
    public Vector2d(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector2d(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector2d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector2d(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public double x() {
        return this.x;
    }
    
    public double y() {
        return this.y;
    }
    
    public Vector2d set(final double d) {
        this.x = d;
        this.y = d;
        return this;
    }
    
    public Vector2d set(final double x, final double y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector2d set(final Vector2dc v) {
        this.x = v.x();
        this.y = v.y();
        return this;
    }
    
    public Vector2d set(final Vector2fc v) {
        this.x = v.x();
        this.y = v.y();
        return this;
    }
    
    public Vector2d set(final Vector2ic v) {
        this.x = v.x();
        this.y = v.y();
        return this;
    }
    
    public Vector2d set(final double[] xy) {
        this.x = xy[0];
        this.y = xy[1];
        return this;
    }
    
    public Vector2d set(final float[] xy) {
        this.x = xy[0];
        this.y = xy[1];
        return this;
    }
    
    public Vector2d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector2d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector2d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector2d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector2d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public double get(final int component) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                return this.x;
            }
            case 1: {
                return this.y;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public Vector2i get(final int mode, final Vector2i dest) {
        dest.x = Math.roundUsing(this.x(), mode);
        dest.y = Math.roundUsing(this.y(), mode);
        return dest;
    }
    
    public Vector2f get(final Vector2f dest) {
        dest.x = (float)this.x();
        dest.y = (float)this.y();
        return dest;
    }
    
    public Vector2d get(final Vector2d dest) {
        dest.x = this.x();
        dest.y = this.y();
        return dest;
    }
    
    public Vector2d setComponent(final int component, final double value) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                this.x = value;
                break;
            }
            case 1: {
                this.y = value;
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        return this;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public DoubleBuffer get(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public DoubleBuffer get(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public Vector2dc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector2d perpendicular() {
        final double xTemp = this.y;
        this.y = this.x * -1.0;
        this.x = xTemp;
        return this;
    }
    
    public Vector2d sub(final Vector2dc v) {
        this.x -= v.x();
        this.y -= v.y();
        return this;
    }
    
    public Vector2d sub(final double x, final double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    
    public Vector2d sub(final double x, final double y, final Vector2d dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        return dest;
    }
    
    public Vector2d sub(final Vector2fc v) {
        this.x -= v.x();
        this.y -= v.y();
        return this;
    }
    
    public Vector2d sub(final Vector2dc v, final Vector2d dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        return dest;
    }
    
    public Vector2d sub(final Vector2fc v, final Vector2d dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        return dest;
    }
    
    public Vector2d mul(final double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    
    public Vector2d mul(final double scalar, final Vector2d dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        return dest;
    }
    
    public Vector2d mul(final double x, final double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }
    
    public Vector2d mul(final double x, final double y, final Vector2d dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        return dest;
    }
    
    public Vector2d mul(final Vector2dc v) {
        this.x *= v.x();
        this.y *= v.y();
        return this;
    }
    
    public Vector2d mul(final Vector2dc v, final Vector2d dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        return dest;
    }
    
    public Vector2d div(final double scalar) {
        final double inv = 1.0 / scalar;
        this.x *= inv;
        this.y *= inv;
        return this;
    }
    
    public Vector2d div(final double scalar, final Vector2d dest) {
        final double inv = 1.0 / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        return dest;
    }
    
    public Vector2d div(final double x, final double y) {
        this.x /= x;
        this.y /= y;
        return this;
    }
    
    public Vector2d div(final double x, final double y, final Vector2d dest) {
        dest.x = this.x / x;
        dest.y = this.y / y;
        return dest;
    }
    
    public Vector2d div(final Vector2d v) {
        this.x /= v.x();
        this.y /= v.y();
        return this;
    }
    
    public Vector2d div(final Vector2fc v) {
        this.x /= v.x();
        this.y /= v.y();
        return this;
    }
    
    public Vector2d div(final Vector2fc v, final Vector2d dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        return dest;
    }
    
    public Vector2d div(final Vector2dc v, final Vector2d dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        return dest;
    }
    
    public Vector2d mul(final Matrix2fc mat) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2d mul(final Matrix2dc mat) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2d mul(final Matrix2dc mat, final Vector2d dest) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2d mul(final Matrix2fc mat, final Vector2d dest) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2d mulTranspose(final Matrix2dc mat) {
        final double rx = mat.m00() * this.x + mat.m01() * this.y;
        final double ry = mat.m10() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2d mulTranspose(final Matrix2dc mat, final Vector2d dest) {
        final double rx = mat.m00() * this.x + mat.m01() * this.y;
        final double ry = mat.m10() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2d mulTranspose(final Matrix2fc mat) {
        final double rx = mat.m00() * this.x + mat.m01() * this.y;
        final double ry = mat.m10() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2d mulTranspose(final Matrix2fc mat, final Vector2d dest) {
        final double rx = mat.m00() * this.x + mat.m01() * this.y;
        final double ry = mat.m10() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2d mulPosition(final Matrix3x2dc mat) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y + mat.m20();
        final double ry = mat.m01() * this.x + mat.m11() * this.y + mat.m21();
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2d mulPosition(final Matrix3x2dc mat, final Vector2d dest) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y + mat.m20();
        final double ry = mat.m01() * this.x + mat.m11() * this.y + mat.m21();
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2d mulDirection(final Matrix3x2dc mat) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2d mulDirection(final Matrix3x2dc mat, final Vector2d dest) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public double dot(final Vector2dc v) {
        return this.x * v.x() + this.y * v.y();
    }
    
    public double angle(final Vector2dc v) {
        final double dot = this.x * v.x() + this.y * v.y();
        final double det = this.x * v.y() - this.y * v.x();
        return Math.atan2(det, dot);
    }
    
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    public static double lengthSquared(final double x, final double y) {
        return x * x + y * y;
    }
    
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    
    public static double length(final double x, final double y) {
        return Math.sqrt(x * x + y * y);
    }
    
    public double distance(final Vector2dc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double distanceSquared(final Vector2dc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        return dx * dx + dy * dy;
    }
    
    public double distance(final Vector2fc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double distanceSquared(final Vector2fc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        return dx * dx + dy * dy;
    }
    
    public double distance(final double x, final double y) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double distanceSquared(final double x, final double y) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        return dx * dx + dy * dy;
    }
    
    public static double distance(final double x1, final double y1, final double x2, final double y2) {
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public static double distanceSquared(final double x1, final double y1, final double x2, final double y2) {
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        return dx * dx + dy * dy;
    }
    
    public Vector2d normalize() {
        final double invLength = Math.invsqrt(this.x * this.x + this.y * this.y);
        this.x *= invLength;
        this.y *= invLength;
        return this;
    }
    
    public Vector2d normalize(final Vector2d dest) {
        final double invLength = Math.invsqrt(this.x * this.x + this.y * this.y);
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        return dest;
    }
    
    public Vector2d normalize(final double length) {
        final double invLength = Math.invsqrt(this.x * this.x + this.y * this.y) * length;
        this.x *= invLength;
        this.y *= invLength;
        return this;
    }
    
    public Vector2d normalize(final double length, final Vector2d dest) {
        final double invLength = Math.invsqrt(this.x * this.x + this.y * this.y) * length;
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        return dest;
    }
    
    public Vector2d add(final Vector2dc v) {
        this.x += v.x();
        this.y += v.y();
        return this;
    }
    
    public Vector2d add(final double x, final double y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Vector2d add(final double x, final double y, final Vector2d dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        return dest;
    }
    
    public Vector2d add(final Vector2fc v) {
        this.x += v.x();
        this.y += v.y();
        return this;
    }
    
    public Vector2d add(final Vector2dc v, final Vector2d dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        return dest;
    }
    
    public Vector2d add(final Vector2fc v, final Vector2d dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        return dest;
    }
    
    public Vector2d zero() {
        this.x = 0.0;
        this.y = 0.0;
        return this;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readDouble();
        this.y = in.readDouble();
    }
    
    public Vector2d negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    public Vector2d negate(final Vector2d dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        return dest;
    }
    
    public Vector2d lerp(final Vector2dc other, final double t) {
        this.x += (other.x() - this.x) * t;
        this.y += (other.y() - this.y) * t;
        return this;
    }
    
    public Vector2d lerp(final Vector2dc other, final double t, final Vector2d dest) {
        dest.x = this.x + (other.x() - this.x) * t;
        dest.y = this.y + (other.y() - this.y) * t;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Vector2d other = (Vector2d)obj;
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
    }
    
    public boolean equals(final Vector2dc v, final double delta) {
        return this == v || (v != null && v instanceof Vector2dc && Runtime.equals(this.x, v.x(), delta) && Runtime.equals(this.y, v.y(), delta));
    }
    
    public boolean equals(final double x, final double y) {
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(y);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + ")";
    }
    
    public Vector2d fma(final Vector2dc a, final Vector2dc b) {
        this.x += a.x() * b.x();
        this.y += a.y() * b.y();
        return this;
    }
    
    public Vector2d fma(final double a, final Vector2dc b) {
        this.x += a * b.x();
        this.y += a * b.y();
        return this;
    }
    
    public Vector2d fma(final Vector2dc a, final Vector2dc b, final Vector2d dest) {
        dest.x = this.x + a.x() * b.x();
        dest.y = this.y + a.y() * b.y();
        return dest;
    }
    
    public Vector2d fma(final double a, final Vector2dc b, final Vector2d dest) {
        dest.x = this.x + a * b.x();
        dest.y = this.y + a * b.y();
        return dest;
    }
    
    public Vector2d min(final Vector2dc v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        return this;
    }
    
    public Vector2d min(final Vector2dc v, final Vector2d dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        return dest;
    }
    
    public Vector2d max(final Vector2dc v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        return this;
    }
    
    public Vector2d max(final Vector2dc v, final Vector2d dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        return dest;
    }
    
    public int maxComponent() {
        final double absX = Math.abs(this.x);
        final double absY = Math.abs(this.y);
        if (absX >= absY) {
            return 0;
        }
        return 1;
    }
    
    public int minComponent() {
        final double absX = Math.abs(this.x);
        final double absY = Math.abs(this.y);
        if (absX < absY) {
            return 0;
        }
        return 1;
    }
    
    public Vector2d floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        return this;
    }
    
    public Vector2d floor(final Vector2d dest) {
        dest.x = Math.floor(this.x);
        dest.y = Math.floor(this.y);
        return dest;
    }
    
    public Vector2d ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        return this;
    }
    
    public Vector2d ceil(final Vector2d dest) {
        dest.x = Math.ceil(this.x);
        dest.y = Math.ceil(this.y);
        return dest;
    }
    
    public Vector2d round() {
        this.x = (double)Math.round(this.x);
        this.y = (double)Math.round(this.y);
        return this;
    }
    
    public Vector2d round(final Vector2d dest) {
        dest.x = (double)Math.round(this.x);
        dest.y = (double)Math.round(this.y);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y);
    }
    
    public Vector2d absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }
    
    public Vector2d absolute(final Vector2d dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        return dest;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
