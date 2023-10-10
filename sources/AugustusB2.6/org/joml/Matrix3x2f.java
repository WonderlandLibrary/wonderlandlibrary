// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.nio.FloatBuffer;
import java.io.Externalizable;

public class Matrix3x2f implements Matrix3x2fc, Externalizable, Cloneable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m10;
    public float m11;
    public float m20;
    public float m21;
    
    public Matrix3x2f() {
        this.m00 = 1.0f;
        this.m11 = 1.0f;
    }
    
    public Matrix3x2f(final Matrix3x2fc mat) {
        if (mat instanceof Matrix3x2f) {
            MemUtil.INSTANCE.copy((Matrix3x2f)mat, this);
        }
        else {
            this.setMatrix3x2fc(mat);
        }
    }
    
    public Matrix3x2f(final Matrix2fc mat) {
        if (mat instanceof Matrix2f) {
            MemUtil.INSTANCE.copy((Matrix2f)mat, this);
        }
        else {
            this.setMatrix2fc(mat);
        }
    }
    
    public Matrix3x2f(final float m00, final float m01, final float m10, final float m11, final float m20, final float m21) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m20 = m20;
        this.m21 = m21;
    }
    
    public Matrix3x2f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public float m00() {
        return this.m00;
    }
    
    public float m01() {
        return this.m01;
    }
    
    public float m10() {
        return this.m10;
    }
    
    public float m11() {
        return this.m11;
    }
    
    public float m20() {
        return this.m20;
    }
    
    public float m21() {
        return this.m21;
    }
    
    Matrix3x2f _m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix3x2f _m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix3x2f _m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix3x2f _m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix3x2f _m20(final float m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix3x2f _m21(final float m21) {
        this.m21 = m21;
        return this;
    }
    
    public Matrix3x2f set(final Matrix3x2fc m) {
        if (m instanceof Matrix3x2f) {
            MemUtil.INSTANCE.copy((Matrix3x2f)m, this);
        }
        else {
            this.setMatrix3x2fc(m);
        }
        return this;
    }
    
    private void setMatrix3x2fc(final Matrix3x2fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
    }
    
    public Matrix3x2f set(final Matrix2fc m) {
        if (m instanceof Matrix2f) {
            MemUtil.INSTANCE.copy((Matrix2f)m, this);
        }
        else {
            this.setMatrix2fc(m);
        }
        return this;
    }
    
    private void setMatrix2fc(final Matrix2fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix3x2f mul(final Matrix3x2fc right) {
        return this.mul(right, this);
    }
    
    public Matrix3x2f mul(final Matrix3x2fc right, final Matrix3x2f dest) {
        final float nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final float nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final float nm3 = this.m00 * right.m10() + this.m10 * right.m11();
        final float nm4 = this.m01 * right.m10() + this.m11 * right.m11();
        final float nm5 = this.m00 * right.m20() + this.m10 * right.m21() + this.m20;
        final float nm6 = this.m01 * right.m20() + this.m11 * right.m21() + this.m21;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2f mulLocal(final Matrix3x2fc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix3x2f mulLocal(final Matrix3x2fc left, final Matrix3x2f dest) {
        final float nm00 = left.m00() * this.m00 + left.m10() * this.m01;
        final float nm2 = left.m01() * this.m00 + left.m11() * this.m01;
        final float nm3 = left.m00() * this.m10 + left.m10() * this.m11;
        final float nm4 = left.m01() * this.m10 + left.m11() * this.m11;
        final float nm5 = left.m00() * this.m20 + left.m10() * this.m21 + left.m20();
        final float nm6 = left.m01() * this.m20 + left.m11() * this.m21 + left.m21();
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2f set(final float m00, final float m01, final float m10, final float m11, final float m20, final float m21) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m20 = m20;
        this.m21 = m21;
        return this;
    }
    
    public Matrix3x2f set(final float[] m) {
        MemUtil.INSTANCE.copy(m, 0, this);
        return this;
    }
    
    public float determinant() {
        return this.m00 * this.m11 - this.m01 * this.m10;
    }
    
    public Matrix3x2f invert() {
        return this.invert(this);
    }
    
    public Matrix3x2f invert(final Matrix3x2f dest) {
        final float s = 1.0f / (this.m00 * this.m11 - this.m01 * this.m10);
        final float nm00 = this.m11 * s;
        final float nm2 = -this.m01 * s;
        final float nm3 = -this.m10 * s;
        final float nm4 = this.m00 * s;
        final float nm5 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final float nm6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2f translation(final float x, final float y) {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m20 = x;
        this.m21 = y;
        return this;
    }
    
    public Matrix3x2f translation(final Vector2fc offset) {
        return this.translation(offset.x(), offset.y());
    }
    
    public Matrix3x2f setTranslation(final float x, final float y) {
        this.m20 = x;
        this.m21 = y;
        return this;
    }
    
    public Matrix3x2f setTranslation(final Vector2f offset) {
        return this.setTranslation(offset.x, offset.y);
    }
    
    public Matrix3x2f translate(final float x, final float y, final Matrix3x2f dest) {
        final float rm20 = x;
        final float rm21 = y;
        dest.m20 = this.m00 * rm20 + this.m10 * rm21 + this.m20;
        dest.m21 = this.m01 * rm20 + this.m11 * rm21 + this.m21;
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        return dest;
    }
    
    public Matrix3x2f translate(final float x, final float y) {
        return this.translate(x, y, this);
    }
    
    public Matrix3x2f translate(final Vector2fc offset, final Matrix3x2f dest) {
        return this.translate(offset.x(), offset.y(), dest);
    }
    
    public Matrix3x2f translate(final Vector2fc offset) {
        return this.translate(offset.x(), offset.y(), this);
    }
    
    public Matrix3x2f translateLocal(final Vector2fc offset) {
        return this.translateLocal(offset.x(), offset.y());
    }
    
    public Matrix3x2f translateLocal(final Vector2fc offset, final Matrix3x2f dest) {
        return this.translateLocal(offset.x(), offset.y(), dest);
    }
    
    public Matrix3x2f translateLocal(final float x, final float y, final Matrix3x2f dest) {
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m20 = this.m20 + x;
        dest.m21 = this.m21 + y;
        return dest;
    }
    
    public Matrix3x2f translateLocal(final float x, final float y) {
        return this.translateLocal(x, y, this);
    }
    
    public String toString() {
        final String str = this.toString(Options.NUMBER_FORMAT);
        final StringBuffer res = new StringBuffer();
        int eIndex = Integer.MIN_VALUE;
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            if (c == 'E') {
                eIndex = i;
            }
            else {
                if (c == ' ' && eIndex == i - 1) {
                    res.append('+');
                    continue;
                }
                if (Character.isDigit(c) && eIndex == i - 1) {
                    res.append('+');
                }
            }
            res.append(c);
        }
        return res.toString();
    }
    
    public String toString(final NumberFormat formatter) {
        return Runtime.format(this.m00, formatter) + " " + Runtime.format(this.m10, formatter) + " " + Runtime.format(this.m20, formatter) + "\n" + Runtime.format(this.m01, formatter) + " " + Runtime.format(this.m11, formatter) + " " + Runtime.format(this.m21, formatter) + "\n";
    }
    
    public Matrix3x2f get(final Matrix3x2f dest) {
        return dest.set(this);
    }
    
    public FloatBuffer get(final FloatBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get3x3(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put3x3(this, 0, buffer);
        return buffer;
    }
    
    public FloatBuffer get3x3(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put3x3(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get3x3(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put3x3(this, 0, buffer);
        return buffer;
    }
    
    public ByteBuffer get3x3(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put3x3(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get4x4(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, 0, buffer);
        return buffer;
    }
    
    public FloatBuffer get4x4(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get4x4(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, 0, buffer);
        return buffer;
    }
    
    public ByteBuffer get4x4(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, index, buffer);
        return buffer;
    }
    
    public Matrix3x2fc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public float[] get(final float[] arr, final int offset) {
        MemUtil.INSTANCE.copy(this, arr, offset);
        return arr;
    }
    
    public float[] get(final float[] arr) {
        return this.get(arr, 0);
    }
    
    public float[] get3x3(final float[] arr, final int offset) {
        MemUtil.INSTANCE.copy3x3(this, arr, offset);
        return arr;
    }
    
    public float[] get3x3(final float[] arr) {
        return this.get3x3(arr, 0);
    }
    
    public float[] get4x4(final float[] arr, final int offset) {
        MemUtil.INSTANCE.copy4x4(this, arr, offset);
        return arr;
    }
    
    public float[] get4x4(final float[] arr) {
        return this.get4x4(arr, 0);
    }
    
    public Matrix3x2f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3x2f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3x2f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3x2f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3x2f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Matrix3x2f zero() {
        MemUtil.INSTANCE.zero(this);
        return this;
    }
    
    public Matrix3x2f identity() {
        MemUtil.INSTANCE.identity(this);
        return this;
    }
    
    public Matrix3x2f scale(final float x, final float y, final Matrix3x2f dest) {
        dest.m00 = this.m00 * x;
        dest.m01 = this.m01 * x;
        dest.m10 = this.m10 * y;
        dest.m11 = this.m11 * y;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2f scale(final float x, final float y) {
        return this.scale(x, y, this);
    }
    
    public Matrix3x2f scale(final Vector2fc xy) {
        return this.scale(xy.x(), xy.y(), this);
    }
    
    public Matrix3x2f scale(final Vector2fc xy, final Matrix3x2f dest) {
        return this.scale(xy.x(), xy.y(), dest);
    }
    
    public Matrix3x2f scale(final float xy, final Matrix3x2f dest) {
        return this.scale(xy, xy, dest);
    }
    
    public Matrix3x2f scale(final float xy) {
        return this.scale(xy, xy);
    }
    
    public Matrix3x2f scaleLocal(final float x, final float y, final Matrix3x2f dest) {
        dest.m00 = x * this.m00;
        dest.m01 = y * this.m01;
        dest.m10 = x * this.m10;
        dest.m11 = y * this.m11;
        dest.m20 = x * this.m20;
        dest.m21 = y * this.m21;
        return dest;
    }
    
    public Matrix3x2f scaleLocal(final float x, final float y) {
        return this.scaleLocal(x, y, this);
    }
    
    public Matrix3x2f scaleLocal(final float xy, final Matrix3x2f dest) {
        return this.scaleLocal(xy, xy, dest);
    }
    
    public Matrix3x2f scaleLocal(final float xy) {
        return this.scaleLocal(xy, xy, this);
    }
    
    public Matrix3x2f scaleAround(final float sx, final float sy, final float ox, final float oy, final Matrix3x2f dest) {
        final float nm20 = this.m00 * ox + this.m10 * oy + this.m20;
        final float nm21 = this.m01 * ox + this.m11 * oy + this.m21;
        dest.m00 = this.m00 * sx;
        dest.m01 = this.m01 * sx;
        dest.m10 = this.m10 * sy;
        dest.m11 = this.m11 * sy;
        dest.m20 = dest.m00 * -ox + dest.m10 * -oy + nm20;
        dest.m21 = dest.m01 * -ox + dest.m11 * -oy + nm21;
        return dest;
    }
    
    public Matrix3x2f scaleAround(final float sx, final float sy, final float ox, final float oy) {
        return this.scaleAround(sx, sy, ox, oy, this);
    }
    
    public Matrix3x2f scaleAround(final float factor, final float ox, final float oy, final Matrix3x2f dest) {
        return this.scaleAround(factor, factor, ox, oy, this);
    }
    
    public Matrix3x2f scaleAround(final float factor, final float ox, final float oy) {
        return this.scaleAround(factor, factor, ox, oy, this);
    }
    
    public Matrix3x2f scaleAroundLocal(final float sx, final float sy, final float ox, final float oy, final Matrix3x2f dest) {
        dest.m00 = sx * this.m00;
        dest.m01 = sy * this.m01;
        dest.m10 = sx * this.m10;
        dest.m11 = sy * this.m11;
        dest.m20 = sx * this.m20 - sx * ox + ox;
        dest.m21 = sy * this.m21 - sy * oy + oy;
        return dest;
    }
    
    public Matrix3x2f scaleAroundLocal(final float factor, final float ox, final float oy, final Matrix3x2f dest) {
        return this.scaleAroundLocal(factor, factor, ox, oy, dest);
    }
    
    public Matrix3x2f scaleAroundLocal(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz) {
        return this.scaleAroundLocal(sx, sy, ox, oy, this);
    }
    
    public Matrix3x2f scaleAroundLocal(final float factor, final float ox, final float oy) {
        return this.scaleAroundLocal(factor, factor, ox, oy, this);
    }
    
    public Matrix3x2f scaling(final float factor) {
        return this.scaling(factor, factor);
    }
    
    public Matrix3x2f scaling(final float x, final float y) {
        this.m00 = x;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        return this;
    }
    
    public Matrix3x2f rotation(final float angle) {
        final float cos = Math.cos(angle);
        final float sin = Math.sin(angle);
        this.m00 = cos;
        this.m10 = -sin;
        this.m20 = 0.0f;
        this.m01 = sin;
        this.m11 = cos;
        this.m21 = 0.0f;
        return this;
    }
    
    public Vector3f transform(final Vector3f v) {
        return v.mul(this);
    }
    
    public Vector3f transform(final Vector3f v, final Vector3f dest) {
        return v.mul(this, dest);
    }
    
    public Vector3f transform(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20 * z, this.m01 * x + this.m11 * y + this.m21 * z, z);
    }
    
    public Vector2f transformPosition(final Vector2f v) {
        v.set(this.m00 * v.x + this.m10 * v.y + this.m20, this.m01 * v.x + this.m11 * v.y + this.m21);
        return v;
    }
    
    public Vector2f transformPosition(final Vector2fc v, final Vector2f dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20, this.m01 * v.x() + this.m11 * v.y() + this.m21);
        return dest;
    }
    
    public Vector2f transformPosition(final float x, final float y, final Vector2f dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20, this.m01 * x + this.m11 * y + this.m21);
    }
    
    public Vector2f transformDirection(final Vector2f v) {
        v.set(this.m00 * v.x + this.m10 * v.y, this.m01 * v.x + this.m11 * v.y);
        return v;
    }
    
    public Vector2f transformDirection(final Vector2fc v, final Vector2f dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y(), this.m01 * v.x() + this.m11 * v.y());
        return dest;
    }
    
    public Vector2f transformDirection(final float x, final float y, final Vector2f dest) {
        return dest.set(this.m00 * x + this.m10 * y, this.m01 * x + this.m11 * y);
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.m00);
        out.writeFloat(this.m01);
        out.writeFloat(this.m10);
        out.writeFloat(this.m11);
        out.writeFloat(this.m20);
        out.writeFloat(this.m21);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this.m00 = in.readFloat();
        this.m01 = in.readFloat();
        this.m10 = in.readFloat();
        this.m11 = in.readFloat();
        this.m20 = in.readFloat();
        this.m21 = in.readFloat();
    }
    
    public Matrix3x2f rotate(final float ang) {
        return this.rotate(ang, this);
    }
    
    public Matrix3x2f rotate(final float ang, final Matrix3x2f dest) {
        final float cos = Math.cos(ang);
        final float sin = Math.sin(ang);
        final float rm00 = cos;
        final float rm2 = sin;
        final float rm3 = -sin;
        final float rm4 = cos;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2;
        dest.m10 = this.m00 * rm3 + this.m10 * rm4;
        dest.m11 = this.m01 * rm3 + this.m11 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2f rotateLocal(final float ang, final Matrix3x2f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm00 = cos * this.m00 - sin * this.m01;
        final float nm2 = sin * this.m00 + cos * this.m01;
        final float nm3 = cos * this.m10 - sin * this.m11;
        final float nm4 = sin * this.m10 + cos * this.m11;
        final float nm5 = cos * this.m20 - sin * this.m21;
        final float nm6 = sin * this.m20 + cos * this.m21;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2f rotateLocal(final float ang) {
        return this.rotateLocal(ang, this);
    }
    
    public Matrix3x2f rotateAbout(final float ang, final float x, final float y) {
        return this.rotateAbout(ang, x, y, this);
    }
    
    public Matrix3x2f rotateAbout(final float ang, final float x, final float y, final Matrix3x2f dest) {
        final float tm20 = this.m00 * x + this.m10 * y + this.m20;
        final float tm21 = this.m01 * x + this.m11 * y + this.m21;
        final float cos = Math.cos(ang);
        final float sin = Math.sin(ang);
        final float nm00 = this.m00 * cos + this.m10 * sin;
        final float nm2 = this.m01 * cos + this.m11 * sin;
        dest.m10 = this.m00 * -sin + this.m10 * cos;
        dest.m11 = this.m01 * -sin + this.m11 * cos;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m20 = dest.m00 * -x + dest.m10 * -y + tm20;
        dest.m21 = dest.m01 * -x + dest.m11 * -y + tm21;
        return dest;
    }
    
    public Matrix3x2f rotateTo(final Vector2fc fromDir, final Vector2fc toDir, final Matrix3x2f dest) {
        final float dot = fromDir.x() * toDir.x() + fromDir.y() * toDir.y();
        final float det = fromDir.x() * toDir.y() - fromDir.y() * toDir.x();
        final float rm00 = dot;
        final float rm2 = det;
        final float rm3 = -det;
        final float rm4 = dot;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2;
        dest.m10 = this.m00 * rm3 + this.m10 * rm4;
        dest.m11 = this.m01 * rm3 + this.m11 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2f rotateTo(final Vector2fc fromDir, final Vector2fc toDir) {
        return this.rotateTo(fromDir, toDir, this);
    }
    
    public Matrix3x2f view(final float left, final float right, final float bottom, final float top, final Matrix3x2f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (left + right) / (left - right);
        final float rm4 = (bottom + top) / (bottom - top);
        dest.m20 = this.m00 * rm3 + this.m10 * rm4 + this.m20;
        dest.m21 = this.m01 * rm3 + this.m11 * rm4 + this.m21;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        return dest;
    }
    
    public Matrix3x2f view(final float left, final float right, final float bottom, final float top) {
        return this.view(left, right, bottom, top, this);
    }
    
    public Matrix3x2f setView(final float left, final float right, final float bottom, final float top) {
        this.m00 = 2.0f / (right - left);
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 2.0f / (top - bottom);
        this.m20 = (left + right) / (left - right);
        this.m21 = (bottom + top) / (bottom - top);
        return this;
    }
    
    public Vector2f origin(final Vector2f origin) {
        final float s = 1.0f / (this.m00 * this.m11 - this.m01 * this.m10);
        origin.x = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        origin.y = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        return origin;
    }
    
    public float[] viewArea(final float[] area) {
        final float s = 1.0f / (this.m00 * this.m11 - this.m01 * this.m10);
        final float rm00 = this.m11 * s;
        final float rm2 = -this.m01 * s;
        final float rm3 = -this.m10 * s;
        final float rm4 = this.m00 * s;
        final float rm5 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final float rm6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        final float nxnyX = -rm00 - rm3;
        final float nxnyY = -rm2 - rm4;
        final float pxnyX = rm00 - rm3;
        final float pxnyY = rm2 - rm4;
        final float nxpyX = -rm00 + rm3;
        final float nxpyY = -rm2 + rm4;
        final float pxpyX = rm00 + rm3;
        final float pxpyY = rm2 + rm4;
        float minX = nxnyX;
        minX = ((minX < nxpyX) ? minX : nxpyX);
        minX = ((minX < pxnyX) ? minX : pxnyX);
        minX = ((minX < pxpyX) ? minX : pxpyX);
        float minY = nxnyY;
        minY = ((minY < nxpyY) ? minY : nxpyY);
        minY = ((minY < pxnyY) ? minY : pxnyY);
        minY = ((minY < pxpyY) ? minY : pxpyY);
        float maxX = nxnyX;
        maxX = ((maxX > nxpyX) ? maxX : nxpyX);
        maxX = ((maxX > pxnyX) ? maxX : pxnyX);
        maxX = ((maxX > pxpyX) ? maxX : pxpyX);
        float maxY = nxnyY;
        maxY = ((maxY > nxpyY) ? maxY : nxpyY);
        maxY = ((maxY > pxnyY) ? maxY : pxnyY);
        maxY = ((maxY > pxpyY) ? maxY : pxpyY);
        area[0] = minX + rm5;
        area[1] = minY + rm6;
        area[2] = maxX + rm5;
        area[3] = maxY + rm6;
        return area;
    }
    
    public Vector2f positiveX(final Vector2f dir) {
        float s = this.m00 * this.m11 - this.m01 * this.m10;
        s = 1.0f / s;
        dir.x = this.m11 * s;
        dir.y = -this.m01 * s;
        return dir.normalize(dir);
    }
    
    public Vector2f normalizedPositiveX(final Vector2f dir) {
        dir.x = this.m11;
        dir.y = -this.m01;
        return dir;
    }
    
    public Vector2f positiveY(final Vector2f dir) {
        float s = this.m00 * this.m11 - this.m01 * this.m10;
        s = 1.0f / s;
        dir.x = -this.m10 * s;
        dir.y = this.m00 * s;
        return dir.normalize(dir);
    }
    
    public Vector2f normalizedPositiveY(final Vector2f dir) {
        dir.x = -this.m10;
        dir.y = this.m00;
        return dir;
    }
    
    public Vector2f unproject(final float winX, final float winY, final int[] viewport, final Vector2f dest) {
        final float s = 1.0f / (this.m00 * this.m11 - this.m01 * this.m10);
        final float im00 = this.m11 * s;
        final float im2 = -this.m01 * s;
        final float im3 = -this.m10 * s;
        final float im4 = this.m00 * s;
        final float im5 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final float im6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        dest.x = im00 * ndcX + im3 * ndcY + im5;
        dest.y = im2 * ndcX + im4 * ndcY + im6;
        return dest;
    }
    
    public Vector2f unprojectInv(final float winX, final float winY, final int[] viewport, final Vector2f dest) {
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        dest.x = this.m00 * ndcX + this.m10 * ndcY + this.m20;
        dest.y = this.m01 * ndcX + this.m11 * ndcY + this.m21;
        return dest;
    }
    
    public Matrix3x2f shearX(final float yFactor) {
        return this.shearX(yFactor, this);
    }
    
    public Matrix3x2f shearX(final float yFactor, final Matrix3x2f dest) {
        final float nm10 = this.m00 * yFactor + this.m10;
        final float nm11 = this.m01 * yFactor + this.m11;
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m10 = nm10;
        dest.m11 = nm11;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2f shearY(final float xFactor) {
        return this.shearY(xFactor, this);
    }
    
    public Matrix3x2f shearY(final float xFactor, final Matrix3x2f dest) {
        final float nm00 = this.m00 + this.m10 * xFactor;
        final float nm2 = this.m01 + this.m11 * xFactor;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2f span(final Vector2f corner, final Vector2f xDir, final Vector2f yDir) {
        final float s = 1.0f / (this.m00 * this.m11 - this.m01 * this.m10);
        final float nm00 = this.m11 * s;
        final float nm2 = -this.m01 * s;
        final float nm3 = -this.m10 * s;
        final float nm4 = this.m00 * s;
        corner.x = -nm00 - nm3 + (this.m10 * this.m21 - this.m20 * this.m11) * s;
        corner.y = -nm2 - nm4 + (this.m20 * this.m01 - this.m00 * this.m21) * s;
        xDir.x = 2.0f * nm00;
        xDir.y = 2.0f * nm2;
        yDir.x = 2.0f * nm3;
        yDir.y = 2.0f * nm4;
        return this;
    }
    
    public boolean testPoint(final float x, final float y) {
        final float nxX = this.m00;
        final float nxY = this.m10;
        final float nxW = 1.0f + this.m20;
        final float pxX = -this.m00;
        final float pxY = -this.m10;
        final float pxW = 1.0f - this.m20;
        final float nyX = this.m01;
        final float nyY = this.m11;
        final float nyW = 1.0f + this.m21;
        final float pyX = -this.m01;
        final float pyY = -this.m11;
        final float pyW = 1.0f - this.m21;
        return nxX * x + nxY * y + nxW >= 0.0f && pxX * x + pxY * y + pxW >= 0.0f && nyX * x + nyY * y + nyW >= 0.0f && pyX * x + pyY * y + pyW >= 0.0f;
    }
    
    public boolean testCircle(final float x, final float y, final float r) {
        float nxX = this.m00;
        float nxY = this.m10;
        float nxW = 1.0f + this.m20;
        float invl = Math.invsqrt(nxX * nxX + nxY * nxY);
        nxX *= invl;
        nxY *= invl;
        nxW *= invl;
        float pxX = -this.m00;
        float pxY = -this.m10;
        float pxW = 1.0f - this.m20;
        invl = Math.invsqrt(pxX * pxX + pxY * pxY);
        pxX *= invl;
        pxY *= invl;
        pxW *= invl;
        float nyX = this.m01;
        float nyY = this.m11;
        float nyW = 1.0f + this.m21;
        invl = Math.invsqrt(nyX * nyX + nyY * nyY);
        nyX *= invl;
        nyY *= invl;
        nyW *= invl;
        float pyX = -this.m01;
        float pyY = -this.m11;
        float pyW = 1.0f - this.m21;
        invl = Math.invsqrt(pyX * pyX + pyY * pyY);
        pyX *= invl;
        pyY *= invl;
        pyW *= invl;
        return nxX * x + nxY * y + nxW >= -r && pxX * x + pxY * y + pxW >= -r && nyX * x + nyY * y + nyW >= -r && pyX * x + pyY * y + pyW >= -r;
    }
    
    public boolean testAar(final float minX, final float minY, final float maxX, final float maxY) {
        final float nxX = this.m00;
        final float nxY = this.m10;
        final float nxW = 1.0f + this.m20;
        final float pxX = -this.m00;
        final float pxY = -this.m10;
        final float pxW = 1.0f - this.m20;
        final float nyX = this.m01;
        final float nyY = this.m11;
        final float nyW = 1.0f + this.m21;
        final float pyX = -this.m01;
        final float pyY = -this.m11;
        final float pyW = 1.0f - this.m21;
        return nxX * ((nxX < 0.0f) ? minX : maxX) + nxY * ((nxY < 0.0f) ? minY : maxY) >= -nxW && pxX * ((pxX < 0.0f) ? minX : maxX) + pxY * ((pxY < 0.0f) ? minY : maxY) >= -pxW && nyX * ((nyX < 0.0f) ? minX : maxX) + nyY * ((nyY < 0.0f) ? minY : maxY) >= -nyW && pyX * ((pyX < 0.0f) ? minX : maxX) + pyY * ((pyY < 0.0f) ? minY : maxY) >= -pyW;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.m00);
        result = 31 * result + Float.floatToIntBits(this.m01);
        result = 31 * result + Float.floatToIntBits(this.m10);
        result = 31 * result + Float.floatToIntBits(this.m11);
        result = 31 * result + Float.floatToIntBits(this.m20);
        result = 31 * result + Float.floatToIntBits(this.m21);
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
        final Matrix3x2f other = (Matrix3x2f)obj;
        return Float.floatToIntBits(this.m00) == Float.floatToIntBits(other.m00) && Float.floatToIntBits(this.m01) == Float.floatToIntBits(other.m01) && Float.floatToIntBits(this.m10) == Float.floatToIntBits(other.m10) && Float.floatToIntBits(this.m11) == Float.floatToIntBits(other.m11) && Float.floatToIntBits(this.m20) == Float.floatToIntBits(other.m20) && Float.floatToIntBits(this.m21) == Float.floatToIntBits(other.m21);
    }
    
    public boolean equals(final Matrix3x2fc m, final float delta) {
        return this == m || (m != null && m instanceof Matrix3x2f && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta));
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m20) && Math.isFinite(this.m21);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
