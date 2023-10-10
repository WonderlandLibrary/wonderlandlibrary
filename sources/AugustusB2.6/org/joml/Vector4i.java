// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Vector4i implements Externalizable, Cloneable, Vector4ic
{
    private static final long serialVersionUID = 1L;
    public int x;
    public int y;
    public int z;
    public int w;
    
    public Vector4i() {
        this.w = 1;
    }
    
    public Vector4i(final Vector4ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
    }
    
    public Vector4i(final Vector3ic v, final int w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }
    
    public Vector4i(final Vector2ic v, final int z, final int w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
    }
    
    public Vector4i(final Vector3fc v, float w, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        w = (float)Math.roundUsing(w, mode);
    }
    
    public Vector4i(final Vector4fc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        this.w = Math.roundUsing(v.w(), mode);
    }
    
    public Vector4i(final Vector4dc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        this.w = Math.roundUsing(v.w(), mode);
    }
    
    public Vector4i(final int s) {
        this.x = s;
        this.y = s;
        this.z = s;
        this.w = s;
    }
    
    public Vector4i(final int x, final int y, final int z, final int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4i(final int[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
    }
    
    public Vector4i(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector4i(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector4i(final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector4i(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public int x() {
        return this.x;
    }
    
    public int y() {
        return this.y;
    }
    
    public int z() {
        return this.z;
    }
    
    public int w() {
        return this.w;
    }
    
    public Vector4i set(final Vector4ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }
    
    public Vector4i set(final Vector4dc v) {
        this.x = (int)v.x();
        this.y = (int)v.y();
        this.z = (int)v.z();
        this.w = (int)v.w();
        return this;
    }
    
    public Vector4i set(final Vector4dc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        this.w = Math.roundUsing(v.w(), mode);
        return this;
    }
    
    public Vector4i set(final Vector4fc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        this.w = Math.roundUsing(v.w(), mode);
        return this;
    }
    
    public Vector4i set(final Vector3ic v, final int w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
        return this;
    }
    
    public Vector4i set(final Vector2ic v, final int z, final int w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4i set(final int s) {
        this.x = s;
        this.y = s;
        this.z = s;
        this.w = s;
        return this;
    }
    
    public Vector4i set(final int x, final int y, final int z, final int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4i set(final int[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
        return this;
    }
    
    public Vector4i set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector4i set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector4i set(final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector4i set(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector4i setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public int get(final int component) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                return this.x;
            }
            case 1: {
                return this.y;
            }
            case 2: {
                return this.z;
            }
            case 3: {
                return this.w;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public int maxComponent() {
        final int absX = Math.abs(this.x);
        final int absY = Math.abs(this.y);
        final int absZ = Math.abs(this.z);
        final int absW = Math.abs(this.w);
        if (absX >= absY && absX >= absZ && absX >= absW) {
            return 0;
        }
        if (absY >= absZ && absY >= absW) {
            return 1;
        }
        if (absZ >= absW) {
            return 2;
        }
        return 3;
    }
    
    public int minComponent() {
        final int absX = Math.abs(this.x);
        final int absY = Math.abs(this.y);
        final int absZ = Math.abs(this.z);
        final int absW = Math.abs(this.w);
        if (absX < absY && absX < absZ && absX < absW) {
            return 0;
        }
        if (absY < absZ && absY < absW) {
            return 1;
        }
        if (absZ < absW) {
            return 2;
        }
        return 3;
    }
    
    public Vector4i setComponent(final int component, final int value) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                this.x = value;
                break;
            }
            case 1: {
                this.y = value;
                break;
            }
            case 2: {
                this.z = value;
                break;
            }
            case 3: {
                this.w = value;
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        return this;
    }
    
    public IntBuffer get(final IntBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public IntBuffer get(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public Vector4ic getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector4i sub(final Vector4ic v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }
    
    public Vector4i sub(final int x, final int y, final int z, final int w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }
    
    public Vector4i sub(final Vector4ic v, final Vector4i dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        dest.w = this.w - v.w();
        return dest;
    }
    
    public Vector4i sub(final int x, final int y, final int z, final int w, final Vector4i dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        dest.w = this.w - w;
        return dest;
    }
    
    public Vector4i add(final Vector4ic v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }
    
    public Vector4i add(final Vector4ic v, final Vector4i dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        dest.w = this.w + v.w();
        return dest;
    }
    
    public Vector4i add(final int x, final int y, final int z, final int w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }
    
    public Vector4i add(final int x, final int y, final int z, final int w, final Vector4i dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        dest.w = this.w + w;
        return dest;
    }
    
    public Vector4i mul(final Vector4ic v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        this.w *= v.w();
        return this;
    }
    
    public Vector4i mul(final Vector4ic v, final Vector4i dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        dest.w = this.w * v.w();
        return dest;
    }
    
    public Vector4i div(final Vector4ic v) {
        this.x /= v.x();
        this.y /= v.y();
        this.z /= v.z();
        this.w /= v.w();
        return this;
    }
    
    public Vector4i div(final Vector4ic v, final Vector4i dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        dest.z = this.z / v.z();
        dest.w = this.w / v.w();
        return dest;
    }
    
    public Vector4i mul(final int scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
    
    public Vector4i mul(final int scalar, final Vector4i dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        dest.w = this.w * scalar;
        return dest;
    }
    
    public Vector4i div(final float scalar) {
        final float invscalar = 1.0f / scalar;
        this.x *= (int)invscalar;
        this.y *= (int)invscalar;
        this.z *= (int)invscalar;
        this.w *= (int)invscalar;
        return this;
    }
    
    public Vector4i div(final float scalar, final Vector4i dest) {
        final float invscalar = 1.0f / scalar;
        dest.x = (int)(this.x * invscalar);
        dest.y = (int)(this.y * invscalar);
        dest.z = (int)(this.z * invscalar);
        dest.w = (int)(this.w * invscalar);
        return dest;
    }
    
    public Vector4i div(final int scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;
        return this;
    }
    
    public Vector4i div(final int scalar, final Vector4i dest) {
        dest.x = this.x / scalar;
        dest.y = this.y / scalar;
        dest.z = this.z / scalar;
        dest.w = this.w / scalar;
        return dest;
    }
    
    public long lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }
    
    public static long lengthSquared(final int x, final int y, final int z, final int w) {
        return x * x + y * y + z * z + w * w;
    }
    
    public double length() {
        return Math.sqrt((float)(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w));
    }
    
    public static double length(final int x, final int y, final int z, final int w) {
        return Math.sqrt((float)(x * x + y * y + z * z + w * w));
    }
    
    public double distance(final Vector4ic v) {
        final int dx = this.x - v.x();
        final int dy = this.y - v.y();
        final int dz = this.z - v.z();
        final int dw = this.w - v.w();
        return Math.sqrt(Math.fma((float)dx, (float)dx, Math.fma((float)dy, (float)dy, Math.fma((float)dz, (float)dz, (float)(dw * dw)))));
    }
    
    public double distance(final int x, final int y, final int z, final int w) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        final int dw = this.w - w;
        return Math.sqrt(Math.fma((float)dx, (float)dx, Math.fma((float)dy, (float)dy, Math.fma((float)dz, (float)dz, (float)(dw * dw)))));
    }
    
    public long gridDistance(final Vector4ic v) {
        return Math.abs(v.x() - this.x()) + Math.abs(v.y() - this.y()) + Math.abs(v.z() - this.z()) + Math.abs(v.w() - this.w());
    }
    
    public long gridDistance(final int x, final int y, final int z, final int w) {
        return Math.abs(x - this.x()) + Math.abs(y - this.y()) + Math.abs(z - this.z()) + Math.abs(w - this.w());
    }
    
    public int distanceSquared(final Vector4ic v) {
        final int dx = this.x - v.x();
        final int dy = this.y - v.y();
        final int dz = this.z - v.z();
        final int dw = this.w - v.w();
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }
    
    public int distanceSquared(final int x, final int y, final int z, final int w) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        final int dw = this.w - w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }
    
    public static double distance(final int x1, final int y1, final int z1, final int w1, final int x2, final int y2, final int z2, final int w2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        final int dz = z1 - z2;
        final int dw = w1 - w2;
        return Math.sqrt((float)(dx * dx + dy * dy + dz * dz + dw * dw));
    }
    
    public static long distanceSquared(final int x1, final int y1, final int z1, final int w1, final int x2, final int y2, final int z2, final int w2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        final int dz = z1 - z2;
        final int dw = w1 - w2;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }
    
    public int dot(final Vector4ic v) {
        return this.x * v.x() + this.y * v.y() + this.z * v.z() + this.w * v.w();
    }
    
    public Vector4i zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
        return this;
    }
    
    public Vector4i negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }
    
    public Vector4i negate(final Vector4i dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        dest.w = -this.w;
        return dest;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + formatter.format(this.x) + " " + formatter.format(this.y) + " " + formatter.format(this.z) + " " + formatter.format(this.w) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
        out.writeInt(this.w);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
        this.w = in.readInt();
    }
    
    public Vector4i min(final Vector4ic v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        this.z = ((this.z < v.z()) ? this.z : v.z());
        this.w = ((this.w < v.w()) ? this.w : v.w());
        return this;
    }
    
    public Vector4i min(final Vector4ic v, final Vector4i dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        dest.z = ((this.z < v.z()) ? this.z : v.z());
        dest.w = ((this.w < v.w()) ? this.w : v.w());
        return dest;
    }
    
    public Vector4i max(final Vector4ic v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        this.z = ((this.z > v.z()) ? this.z : v.z());
        this.w = ((this.w > v.w()) ? this.w : v.w());
        return this;
    }
    
    public Vector4i max(final Vector4ic v, final Vector4i dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        dest.z = ((this.z > v.z()) ? this.z : v.z());
        dest.w = ((this.w > v.w()) ? this.w : v.w());
        return dest;
    }
    
    public Vector4i absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        this.w = Math.abs(this.w);
        return this;
    }
    
    public Vector4i absolute(final Vector4i dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        dest.z = Math.abs(this.z);
        dest.w = Math.abs(this.w);
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        result = 31 * result + this.w;
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
        final Vector4i other = (Vector4i)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z && this.w == other.w;
    }
    
    public boolean equals(final int x, final int y, final int z, final int w) {
        return this.x == x && this.y == y && this.z == z && this.w == w;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
