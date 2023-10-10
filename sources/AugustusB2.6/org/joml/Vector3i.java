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

public class Vector3i implements Externalizable, Cloneable, Vector3ic
{
    private static final long serialVersionUID = 1L;
    public int x;
    public int y;
    public int z;
    
    public Vector3i() {
    }
    
    public Vector3i(final int d) {
        this.x = d;
        this.y = d;
        this.z = d;
    }
    
    public Vector3i(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3i(final Vector3ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }
    
    public Vector3i(final Vector2ic v, final int z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }
    
    public Vector3i(final float x, final float y, final float z, final int mode) {
        this.x = Math.roundUsing(x, mode);
        this.y = Math.roundUsing(y, mode);
        this.z = Math.roundUsing(z, mode);
    }
    
    public Vector3i(final double x, final double y, final double z, final int mode) {
        this.x = Math.roundUsing(x, mode);
        this.y = Math.roundUsing(y, mode);
        this.z = Math.roundUsing(z, mode);
    }
    
    public Vector3i(final Vector2fc v, final float z, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(z, mode);
    }
    
    public Vector3i(final Vector3fc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
    }
    
    public Vector3i(final Vector2dc v, final float z, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(z, mode);
    }
    
    public Vector3i(final Vector3dc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
    }
    
    public Vector3i(final int[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }
    
    public Vector3i(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector3i(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector3i(final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector3i(final int index, final IntBuffer buffer) {
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
    
    public Vector3i set(final Vector3ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }
    
    public Vector3i set(final Vector3dc v) {
        this.x = (int)v.x();
        this.y = (int)v.y();
        this.z = (int)v.z();
        return this;
    }
    
    public Vector3i set(final Vector3dc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        return this;
    }
    
    public Vector3i set(final Vector3fc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        this.z = Math.roundUsing(v.z(), mode);
        return this;
    }
    
    public Vector3i set(final Vector2ic v, final int z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        return this;
    }
    
    public Vector3i set(final int d) {
        this.x = d;
        this.y = d;
        this.z = d;
        return this;
    }
    
    public Vector3i set(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3i set(final int[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
        return this;
    }
    
    public Vector3i set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector3i set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector3i set(final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector3i set(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector3i setFromAddress(final long address) {
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
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public Vector3i setComponent(final int component, final int value) throws IllegalArgumentException {
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
    
    public Vector3ic getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector3i sub(final Vector3ic v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        return this;
    }
    
    public Vector3i sub(final Vector3ic v, final Vector3i dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        return dest;
    }
    
    public Vector3i sub(final int x, final int y, final int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3i sub(final int x, final int y, final int z, final Vector3i dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        return dest;
    }
    
    public Vector3i add(final Vector3ic v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        return this;
    }
    
    public Vector3i add(final Vector3ic v, final Vector3i dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        return dest;
    }
    
    public Vector3i add(final int x, final int y, final int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3i add(final int x, final int y, final int z, final Vector3i dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        return dest;
    }
    
    public Vector3i mul(final int scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vector3i mul(final int scalar, final Vector3i dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }
    
    public Vector3i mul(final Vector3ic v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        return this;
    }
    
    public Vector3i mul(final Vector3ic v, final Vector3i dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        return dest;
    }
    
    public Vector3i mul(final int x, final int y, final int z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public Vector3i mul(final int x, final int y, final int z, final Vector3i dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        dest.z = this.z * z;
        return dest;
    }
    
    public Vector3i div(final float scalar) {
        final float invscalar = 1.0f / scalar;
        this.x *= (int)invscalar;
        this.y *= (int)invscalar;
        this.z *= (int)invscalar;
        return this;
    }
    
    public Vector3i div(final float scalar, final Vector3i dest) {
        final float invscalar = 1.0f / scalar;
        dest.x = (int)(this.x * invscalar);
        dest.y = (int)(this.y * invscalar);
        dest.z = (int)(this.z * invscalar);
        return dest;
    }
    
    public Vector3i div(final int scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        return this;
    }
    
    public Vector3i div(final int scalar, final Vector3i dest) {
        dest.x = this.x / scalar;
        dest.y = this.y / scalar;
        dest.z = this.z / scalar;
        return dest;
    }
    
    public long lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public static long lengthSquared(final int x, final int y, final int z) {
        return x * x + y * y + z * z;
    }
    
    public double length() {
        return Math.sqrt((float)(this.x * this.x + this.y * this.y + this.z * this.z));
    }
    
    public static double length(final int x, final int y, final int z) {
        return Math.sqrt((float)(x * x + y * y + z * z));
    }
    
    public double distance(final Vector3ic v) {
        final int dx = this.x - v.x();
        final int dy = this.y - v.y();
        final int dz = this.z - v.z();
        return Math.sqrt((float)(dx * dx + dy * dy + dz * dz));
    }
    
    public double distance(final int x, final int y, final int z) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        return Math.sqrt((float)(dx * dx + dy * dy + dz * dz));
    }
    
    public long gridDistance(final Vector3ic v) {
        return Math.abs(v.x() - this.x()) + Math.abs(v.y() - this.y()) + Math.abs(v.z() - this.z());
    }
    
    public long gridDistance(final int x, final int y, final int z) {
        return Math.abs(x - this.x()) + Math.abs(y - this.y()) + Math.abs(z - this.z());
    }
    
    public long distanceSquared(final Vector3ic v) {
        final int dx = this.x - v.x();
        final int dy = this.y - v.y();
        final int dz = this.z - v.z();
        return dx * dx + dy * dy + dz * dz;
    }
    
    public long distanceSquared(final int x, final int y, final int z) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public static double distance(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return Math.sqrt((float)distanceSquared(x1, y1, z1, x2, y2, z2));
    }
    
    public static long distanceSquared(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        final int dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public Vector3i zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + formatter.format(this.x) + " " + formatter.format(this.y) + " " + formatter.format(this.z) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
    }
    
    public Vector3i negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    public Vector3i negate(final Vector3i dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }
    
    public Vector3i min(final Vector3ic v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        this.z = ((this.z < v.z()) ? this.z : v.z());
        return this;
    }
    
    public Vector3i min(final Vector3ic v, final Vector3i dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        dest.z = ((this.z < v.z()) ? this.z : v.z());
        return dest;
    }
    
    public Vector3i max(final Vector3ic v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        this.z = ((this.z > v.z()) ? this.z : v.z());
        return this;
    }
    
    public Vector3i max(final Vector3ic v, final Vector3i dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        dest.z = ((this.z > v.z()) ? this.z : v.z());
        return dest;
    }
    
    public int maxComponent() {
        final float absX = (float)Math.abs(this.x);
        final float absY = (float)Math.abs(this.y);
        final float absZ = (float)Math.abs(this.z);
        if (absX >= absY && absX >= absZ) {
            return 0;
        }
        if (absY >= absZ) {
            return 1;
        }
        return 2;
    }
    
    public int minComponent() {
        final float absX = (float)Math.abs(this.x);
        final float absY = (float)Math.abs(this.y);
        final float absZ = (float)Math.abs(this.z);
        if (absX < absY && absX < absZ) {
            return 0;
        }
        if (absY < absZ) {
            return 1;
        }
        return 2;
    }
    
    public Vector3i absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }
    
    public Vector3i absolute(final Vector3i dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        dest.z = Math.abs(this.z);
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
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
        final Vector3i other = (Vector3i)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
    
    public boolean equals(final int x, final int y, final int z) {
        return this.x == x && this.y == y && this.z == z;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
