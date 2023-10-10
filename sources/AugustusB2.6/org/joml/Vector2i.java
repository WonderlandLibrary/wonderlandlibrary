// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.text.NumberFormat;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Vector2i implements Externalizable, Cloneable, Vector2ic
{
    private static final long serialVersionUID = 1L;
    public int x;
    public int y;
    
    public Vector2i() {
    }
    
    public Vector2i(final int s) {
        this.x = s;
        this.y = s;
    }
    
    public Vector2i(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2i(final float x, final float y, final int mode) {
        this.x = Math.roundUsing(x, mode);
        this.y = Math.roundUsing(y, mode);
    }
    
    public Vector2i(final double x, final double y, final int mode) {
        this.x = Math.roundUsing(x, mode);
        this.y = Math.roundUsing(y, mode);
    }
    
    public Vector2i(final Vector2ic v) {
        this.x = v.x();
        this.y = v.y();
    }
    
    public Vector2i(final Vector2fc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
    }
    
    public Vector2i(final Vector2dc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
    }
    
    public Vector2i(final int[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }
    
    public Vector2i(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector2i(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector2i(final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector2i(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public int x() {
        return this.x;
    }
    
    public int y() {
        return this.y;
    }
    
    public Vector2i set(final int s) {
        this.x = s;
        this.y = s;
        return this;
    }
    
    public Vector2i set(final int x, final int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector2i set(final Vector2ic v) {
        this.x = v.x();
        this.y = v.y();
        return this;
    }
    
    public Vector2i set(final Vector2dc v) {
        this.x = (int)v.x();
        this.y = (int)v.y();
        return this;
    }
    
    public Vector2i set(final Vector2dc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        return this;
    }
    
    public Vector2i set(final Vector2fc v, final int mode) {
        this.x = Math.roundUsing(v.x(), mode);
        this.y = Math.roundUsing(v.y(), mode);
        return this;
    }
    
    public Vector2i set(final int[] xy) {
        this.x = xy[0];
        this.y = xy[1];
        return this;
    }
    
    public Vector2i set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector2i set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector2i set(final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector2i set(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector2i setFromAddress(final long address) {
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
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public Vector2i setComponent(final int component, final int value) throws IllegalArgumentException {
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
    
    public IntBuffer get(final IntBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public IntBuffer get(final int index, final IntBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public Vector2ic getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector2i sub(final Vector2ic v) {
        this.x -= v.x();
        this.y -= v.y();
        return this;
    }
    
    public Vector2i sub(final Vector2ic v, final Vector2i dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        return dest;
    }
    
    public Vector2i sub(final int x, final int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    
    public Vector2i sub(final int x, final int y, final Vector2i dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        return dest;
    }
    
    public long lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    public static long lengthSquared(final int x, final int y) {
        return x * x + y * y;
    }
    
    public double length() {
        return Math.sqrt((float)(this.x * this.x + this.y * this.y));
    }
    
    public static double length(final int x, final int y) {
        return Math.sqrt((float)(x * x + y * y));
    }
    
    public double distance(final Vector2ic v) {
        final int dx = this.x - v.x();
        final int dy = this.y - v.y();
        return Math.sqrt((float)(dx * dx + dy * dy));
    }
    
    public double distance(final int x, final int y) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        return Math.sqrt((float)(dx * dx + dy * dy));
    }
    
    public long distanceSquared(final Vector2ic v) {
        final int dx = this.x - v.x();
        final int dy = this.y - v.y();
        return dx * dx + dy * dy;
    }
    
    public long distanceSquared(final int x, final int y) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        return dx * dx + dy * dy;
    }
    
    public long gridDistance(final Vector2ic v) {
        return Math.abs(v.x() - this.x()) + Math.abs(v.y() - this.y());
    }
    
    public long gridDistance(final int x, final int y) {
        return Math.abs(x - this.x()) + Math.abs(y - this.y());
    }
    
    public static double distance(final int x1, final int y1, final int x2, final int y2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        return Math.sqrt((float)(dx * dx + dy * dy));
    }
    
    public static long distanceSquared(final int x1, final int y1, final int x2, final int y2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        return dx * dx + dy * dy;
    }
    
    public Vector2i add(final Vector2ic v) {
        this.x += v.x();
        this.y += v.y();
        return this;
    }
    
    public Vector2i add(final Vector2ic v, final Vector2i dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        return dest;
    }
    
    public Vector2i add(final int x, final int y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Vector2i add(final int x, final int y, final Vector2i dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        return dest;
    }
    
    public Vector2i mul(final int scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    
    public Vector2i mul(final int scalar, final Vector2i dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        return dest;
    }
    
    public Vector2i mul(final Vector2ic v) {
        this.x *= v.x();
        this.y *= v.y();
        return this;
    }
    
    public Vector2i mul(final Vector2ic v, final Vector2i dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        return dest;
    }
    
    public Vector2i mul(final int x, final int y) {
        this.x *= x;
        this.y *= y;
        return this;
    }
    
    public Vector2i mul(final int x, final int y, final Vector2i dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        return dest;
    }
    
    public Vector2i div(final float scalar) {
        final float invscalar = 1.0f / scalar;
        this.x *= (int)invscalar;
        this.y *= (int)invscalar;
        return this;
    }
    
    public Vector2i div(final float scalar, final Vector2i dest) {
        final float invscalar = 1.0f / scalar;
        dest.x = (int)(this.x * invscalar);
        dest.y = (int)(this.y * invscalar);
        return dest;
    }
    
    public Vector2i div(final int scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }
    
    public Vector2i div(final int scalar, final Vector2i dest) {
        dest.x = this.x / scalar;
        dest.y = this.y / scalar;
        return dest;
    }
    
    public Vector2i zero() {
        this.x = 0;
        this.y = 0;
        return this;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.y);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readInt();
        this.y = in.readInt();
    }
    
    public Vector2i negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    public Vector2i negate(final Vector2i dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        return dest;
    }
    
    public Vector2i min(final Vector2ic v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        return this;
    }
    
    public Vector2i min(final Vector2ic v, final Vector2i dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        return dest;
    }
    
    public Vector2i max(final Vector2ic v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        return this;
    }
    
    public Vector2i max(final Vector2ic v, final Vector2i dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        return dest;
    }
    
    public int maxComponent() {
        final int absX = Math.abs(this.x);
        final int absY = Math.abs(this.y);
        if (absX >= absY) {
            return 0;
        }
        return 1;
    }
    
    public int minComponent() {
        final int absX = Math.abs(this.x);
        final int absY = Math.abs(this.y);
        if (absX < absY) {
            return 0;
        }
        return 1;
    }
    
    public Vector2i absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }
    
    public Vector2i absolute(final Vector2i dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
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
        final Vector2i other = (Vector2i)obj;
        return this.x == other.x && this.y == other.y;
    }
    
    public boolean equals(final int x, final int y) {
        return this.x == x && this.y == y;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + formatter.format(this.x) + " " + formatter.format(this.y) + ")";
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
