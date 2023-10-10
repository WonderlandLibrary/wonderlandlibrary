// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.text.NumberFormat;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Vector2f implements Externalizable, Cloneable, Vector2fc
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    
    public Vector2f() {
    }
    
    public Vector2f(final float d) {
        this.x = d;
        this.y = d;
    }
    
    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2f(final Vector2fc v) {
        this.x = v.x();
        this.y = v.y();
    }
    
    public Vector2f(final Vector2ic v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
    }
    
    public Vector2f(final float[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }
    
    public Vector2f(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector2f(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector2f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector2f(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public Vector2f set(final float d) {
        this.x = d;
        this.y = d;
        return this;
    }
    
    public Vector2f set(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector2f set(final double d) {
        this.x = (float)d;
        this.y = (float)d;
        return this;
    }
    
    public Vector2f set(final double x, final double y) {
        this.x = (float)x;
        this.y = (float)y;
        return this;
    }
    
    public Vector2f set(final Vector2fc v) {
        this.x = v.x();
        this.y = v.y();
        return this;
    }
    
    public Vector2f set(final Vector2ic v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        return this;
    }
    
    public Vector2f set(final Vector2dc v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        return this;
    }
    
    public Vector2f set(final float[] xy) {
        this.x = xy[0];
        this.y = xy[1];
        return this;
    }
    
    public Vector2f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector2f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector2f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector2f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector2f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public float get(final int component) throws IllegalArgumentException {
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
        dest.x = this.x();
        dest.y = this.y();
        return dest;
    }
    
    public Vector2d get(final Vector2d dest) {
        dest.x = this.x();
        dest.y = this.y();
        return dest;
    }
    
    public Vector2f setComponent(final int component, final float value) throws IllegalArgumentException {
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
    
    public FloatBuffer get(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public Vector2fc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector2f perpendicular() {
        final float xTemp = this.y;
        this.y = this.x * -1.0f;
        this.x = xTemp;
        return this;
    }
    
    public Vector2f sub(final Vector2fc v) {
        this.x -= v.x();
        this.y -= v.y();
        return this;
    }
    
    public Vector2f sub(final Vector2fc v, final Vector2f dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        return dest;
    }
    
    public Vector2f sub(final float x, final float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    
    public Vector2f sub(final float x, final float y, final Vector2f dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        return dest;
    }
    
    public float dot(final Vector2fc v) {
        return this.x * v.x() + this.y * v.y();
    }
    
    public float angle(final Vector2fc v) {
        final float dot = this.x * v.x() + this.y * v.y();
        final float det = this.x * v.y() - this.y * v.x();
        return Math.atan2(det, dot);
    }
    
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    public static float lengthSquared(final float x, final float y) {
        return x * x + y * y;
    }
    
    public float length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    
    public static float length(final float x, final float y) {
        return Math.sqrt(x * x + y * y);
    }
    
    public float distance(final Vector2fc v) {
        final float dx = this.x - v.x();
        final float dy = this.y - v.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public float distanceSquared(final Vector2fc v) {
        final float dx = this.x - v.x();
        final float dy = this.y - v.y();
        return dx * dx + dy * dy;
    }
    
    public float distance(final float x, final float y) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public float distanceSquared(final float x, final float y) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        return dx * dx + dy * dy;
    }
    
    public static float distance(final float x1, final float y1, final float x2, final float y2) {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public static float distanceSquared(final float x1, final float y1, final float x2, final float y2) {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        return dx * dx + dy * dy;
    }
    
    public Vector2f normalize() {
        final float invLength = Math.invsqrt(this.x * this.x + this.y * this.y);
        this.x *= invLength;
        this.y *= invLength;
        return this;
    }
    
    public Vector2f normalize(final Vector2f dest) {
        final float invLength = Math.invsqrt(this.x * this.x + this.y * this.y);
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        return dest;
    }
    
    public Vector2f normalize(final float length) {
        final float invLength = Math.invsqrt(this.x * this.x + this.y * this.y) * length;
        this.x *= invLength;
        this.y *= invLength;
        return this;
    }
    
    public Vector2f normalize(final float length, final Vector2f dest) {
        final float invLength = Math.invsqrt(this.x * this.x + this.y * this.y) * length;
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        return dest;
    }
    
    public Vector2f add(final Vector2fc v) {
        this.x += v.x();
        this.y += v.y();
        return this;
    }
    
    public Vector2f add(final Vector2fc v, final Vector2f dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        return dest;
    }
    
    public Vector2f add(final float x, final float y) {
        return this.add(x, y, this);
    }
    
    public Vector2f add(final float x, final float y, final Vector2f dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        return dest;
    }
    
    public Vector2f zero() {
        this.x = 0.0f;
        this.y = 0.0f;
        return this;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }
    
    public Vector2f negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    public Vector2f negate(final Vector2f dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        return dest;
    }
    
    public Vector2f mul(final float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    
    public Vector2f mul(final float scalar, final Vector2f dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        return dest;
    }
    
    public Vector2f mul(final float x, final float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }
    
    public Vector2f mul(final float x, final float y, final Vector2f dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        return dest;
    }
    
    public Vector2f mul(final Vector2fc v) {
        this.x *= v.x();
        this.y *= v.y();
        return this;
    }
    
    public Vector2f mul(final Vector2fc v, final Vector2f dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        return dest;
    }
    
    public Vector2f div(final Vector2fc v) {
        this.x /= v.x();
        this.y /= v.y();
        return this;
    }
    
    public Vector2f div(final Vector2fc v, final Vector2f dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        return dest;
    }
    
    public Vector2f div(final float scalar) {
        final float inv = 1.0f / scalar;
        this.x *= inv;
        this.y *= inv;
        return this;
    }
    
    public Vector2f div(final float scalar, final Vector2f dest) {
        final float inv = 1.0f / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        return dest;
    }
    
    public Vector2f div(final float x, final float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }
    
    public Vector2f div(final float x, final float y, final Vector2f dest) {
        dest.x = this.x / x;
        dest.y = this.y / y;
        return dest;
    }
    
    public Vector2f mul(final Matrix2fc mat) {
        final float rx = mat.m00() * this.x + mat.m10() * this.y;
        final float ry = mat.m01() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2f mul(final Matrix2fc mat, final Vector2f dest) {
        final float rx = mat.m00() * this.x + mat.m10() * this.y;
        final float ry = mat.m01() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2f mul(final Matrix2dc mat) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        this.x = (float)rx;
        this.y = (float)ry;
        return this;
    }
    
    public Vector2f mul(final Matrix2dc mat, final Vector2f dest) {
        final double rx = mat.m00() * this.x + mat.m10() * this.y;
        final double ry = mat.m01() * this.x + mat.m11() * this.y;
        dest.x = (float)rx;
        dest.y = (float)ry;
        return dest;
    }
    
    public Vector2f mulTranspose(final Matrix2fc mat) {
        final float rx = mat.m00() * this.x + mat.m01() * this.y;
        final float ry = mat.m10() * this.x + mat.m11() * this.y;
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector2f mulTranspose(final Matrix2fc mat, final Vector2f dest) {
        final float rx = mat.m00() * this.x + mat.m01() * this.y;
        final float ry = mat.m10() * this.x + mat.m11() * this.y;
        dest.x = rx;
        dest.y = ry;
        return dest;
    }
    
    public Vector2f mulPosition(final Matrix3x2fc mat) {
        this.x = mat.m00() * this.x + mat.m10() * this.y + mat.m20();
        this.y = mat.m01() * this.x + mat.m11() * this.y + mat.m21();
        return this;
    }
    
    public Vector2f mulPosition(final Matrix3x2fc mat, final Vector2f dest) {
        dest.x = mat.m00() * this.x + mat.m10() * this.y + mat.m20();
        dest.y = mat.m01() * this.x + mat.m11() * this.y + mat.m21();
        return dest;
    }
    
    public Vector2f mulDirection(final Matrix3x2fc mat) {
        this.x = mat.m00() * this.x + mat.m10() * this.y;
        this.y = mat.m01() * this.x + mat.m11() * this.y;
        return this;
    }
    
    public Vector2f mulDirection(final Matrix3x2fc mat, final Vector2f dest) {
        dest.x = mat.m00() * this.x + mat.m10() * this.y;
        dest.y = mat.m01() * this.x + mat.m11() * this.y;
        return dest;
    }
    
    public Vector2f lerp(final Vector2fc other, final float t) {
        this.x += (other.x() - this.x) * t;
        this.y += (other.y() - this.y) * t;
        return this;
    }
    
    public Vector2f lerp(final Vector2fc other, final float t, final Vector2f dest) {
        dest.x = this.x + (other.x() - this.x) * t;
        dest.y = this.y + (other.y() - this.y) * t;
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
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
        final Vector2f other = (Vector2f)obj;
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y);
    }
    
    public boolean equals(final Vector2fc v, final float delta) {
        return this == v || (v != null && v instanceof Vector2fc && Runtime.equals(this.x, v.x(), delta) && Runtime.equals(this.y, v.y(), delta));
    }
    
    public boolean equals(final float x, final float y) {
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(y);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + ")";
    }
    
    public Vector2f fma(final Vector2fc a, final Vector2fc b) {
        this.x += a.x() * b.x();
        this.y += a.y() * b.y();
        return this;
    }
    
    public Vector2f fma(final float a, final Vector2fc b) {
        this.x += a * b.x();
        this.y += a * b.y();
        return this;
    }
    
    public Vector2f fma(final Vector2fc a, final Vector2fc b, final Vector2f dest) {
        dest.x = this.x + a.x() * b.x();
        dest.y = this.y + a.y() * b.y();
        return dest;
    }
    
    public Vector2f fma(final float a, final Vector2fc b, final Vector2f dest) {
        dest.x = this.x + a * b.x();
        dest.y = this.y + a * b.y();
        return dest;
    }
    
    public Vector2f min(final Vector2fc v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        return this;
    }
    
    public Vector2f min(final Vector2fc v, final Vector2f dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        return dest;
    }
    
    public Vector2f max(final Vector2fc v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        return this;
    }
    
    public Vector2f max(final Vector2fc v, final Vector2f dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        return dest;
    }
    
    public int maxComponent() {
        final float absX = Math.abs(this.x);
        final float absY = Math.abs(this.y);
        if (absX >= absY) {
            return 0;
        }
        return 1;
    }
    
    public int minComponent() {
        final float absX = Math.abs(this.x);
        final float absY = Math.abs(this.y);
        if (absX < absY) {
            return 0;
        }
        return 1;
    }
    
    public Vector2f floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        return this;
    }
    
    public Vector2f floor(final Vector2f dest) {
        dest.x = Math.floor(this.x);
        dest.y = Math.floor(this.y);
        return dest;
    }
    
    public Vector2f ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        return this;
    }
    
    public Vector2f ceil(final Vector2f dest) {
        dest.x = Math.ceil(this.x);
        dest.y = Math.ceil(this.y);
        return dest;
    }
    
    public Vector2f round() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        return this;
    }
    
    public Vector2f round(final Vector2f dest) {
        dest.x = (float)Math.round(this.x);
        dest.y = (float)Math.round(this.y);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y);
    }
    
    public Vector2f absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }
    
    public Vector2f absolute(final Vector2f dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        return dest;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
