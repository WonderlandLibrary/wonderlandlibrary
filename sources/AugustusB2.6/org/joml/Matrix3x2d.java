// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.nio.DoubleBuffer;
import java.io.Externalizable;

public class Matrix3x2d implements Matrix3x2dc, Cloneable, Externalizable
{
    private static final long serialVersionUID = 1L;
    public double m00;
    public double m01;
    public double m10;
    public double m11;
    public double m20;
    public double m21;
    
    public Matrix3x2d() {
        this.m00 = 1.0;
        this.m11 = 1.0;
    }
    
    public Matrix3x2d(final Matrix2dc mat) {
        if (mat instanceof Matrix2d) {
            MemUtil.INSTANCE.copy((Matrix2d)mat, this);
        }
        else {
            this.setMatrix2dc(mat);
        }
    }
    
    public Matrix3x2d(final Matrix2fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix3x2d(final Matrix3x2dc mat) {
        if (mat instanceof Matrix3x2d) {
            MemUtil.INSTANCE.copy((Matrix3x2d)mat, this);
        }
        else {
            this.setMatrix3x2dc(mat);
        }
    }
    
    public Matrix3x2d(final double m00, final double m01, final double m10, final double m11, final double m20, final double m21) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m20 = m20;
        this.m21 = m21;
    }
    
    public Matrix3x2d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public double m00() {
        return this.m00;
    }
    
    public double m01() {
        return this.m01;
    }
    
    public double m10() {
        return this.m10;
    }
    
    public double m11() {
        return this.m11;
    }
    
    public double m20() {
        return this.m20;
    }
    
    public double m21() {
        return this.m21;
    }
    
    Matrix3x2d _m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix3x2d _m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix3x2d _m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix3x2d _m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix3x2d _m20(final double m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix3x2d _m21(final double m21) {
        this.m21 = m21;
        return this;
    }
    
    public Matrix3x2d set(final Matrix3x2dc m) {
        if (m instanceof Matrix3x2d) {
            MemUtil.INSTANCE.copy((Matrix3x2d)m, this);
        }
        else {
            this.setMatrix3x2dc(m);
        }
        return this;
    }
    
    private void setMatrix3x2dc(final Matrix3x2dc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
    }
    
    public Matrix3x2d set(final Matrix2dc m) {
        if (m instanceof Matrix2d) {
            MemUtil.INSTANCE.copy((Matrix2d)m, this);
        }
        else {
            this.setMatrix2dc(m);
        }
        return this;
    }
    
    private void setMatrix2dc(final Matrix2dc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix3x2d set(final Matrix2fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m10 = m.m10();
        this.m11 = m.m11();
        return this;
    }
    
    public Matrix3x2d mul(final Matrix3x2dc right) {
        return this.mul(right, this);
    }
    
    public Matrix3x2d mul(final Matrix3x2dc right, final Matrix3x2d dest) {
        final double nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final double nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final double nm3 = this.m00 * right.m10() + this.m10 * right.m11();
        final double nm4 = this.m01 * right.m10() + this.m11 * right.m11();
        final double nm5 = this.m00 * right.m20() + this.m10 * right.m21() + this.m20;
        final double nm6 = this.m01 * right.m20() + this.m11 * right.m21() + this.m21;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2d mulLocal(final Matrix3x2dc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix3x2d mulLocal(final Matrix3x2dc left, final Matrix3x2d dest) {
        final double nm00 = left.m00() * this.m00 + left.m10() * this.m01;
        final double nm2 = left.m01() * this.m00 + left.m11() * this.m01;
        final double nm3 = left.m00() * this.m10 + left.m10() * this.m11;
        final double nm4 = left.m01() * this.m10 + left.m11() * this.m11;
        final double nm5 = left.m00() * this.m20 + left.m10() * this.m21 + left.m20();
        final double nm6 = left.m01() * this.m20 + left.m11() * this.m21 + left.m21();
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2d set(final double m00, final double m01, final double m10, final double m11, final double m20, final double m21) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m20 = m20;
        this.m21 = m21;
        return this;
    }
    
    public Matrix3x2d set(final double[] m) {
        MemUtil.INSTANCE.copy(m, 0, this);
        return this;
    }
    
    public double determinant() {
        return this.m00 * this.m11 - this.m01 * this.m10;
    }
    
    public Matrix3x2d invert() {
        return this.invert(this);
    }
    
    public Matrix3x2d invert(final Matrix3x2d dest) {
        final double s = 1.0 / (this.m00 * this.m11 - this.m01 * this.m10);
        final double nm00 = this.m11 * s;
        final double nm2 = -this.m01 * s;
        final double nm3 = -this.m10 * s;
        final double nm4 = this.m00 * s;
        final double nm5 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final double nm6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2d translation(final double x, final double y) {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m20 = x;
        this.m21 = y;
        return this;
    }
    
    public Matrix3x2d translation(final Vector2dc offset) {
        return this.translation(offset.x(), offset.y());
    }
    
    public Matrix3x2d setTranslation(final double x, final double y) {
        this.m20 = x;
        this.m21 = y;
        return this;
    }
    
    public Matrix3x2d setTranslation(final Vector2dc offset) {
        return this.setTranslation(offset.x(), offset.y());
    }
    
    public Matrix3x2d translate(final double x, final double y, final Matrix3x2d dest) {
        final double rm20 = x;
        final double rm21 = y;
        dest.m20 = this.m00 * rm20 + this.m10 * rm21 + this.m20;
        dest.m21 = this.m01 * rm20 + this.m11 * rm21 + this.m21;
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        return dest;
    }
    
    public Matrix3x2d translate(final double x, final double y) {
        return this.translate(x, y, this);
    }
    
    public Matrix3x2d translate(final Vector2dc offset, final Matrix3x2d dest) {
        return this.translate(offset.x(), offset.y(), dest);
    }
    
    public Matrix3x2d translate(final Vector2dc offset) {
        return this.translate(offset.x(), offset.y(), this);
    }
    
    public Matrix3x2d translateLocal(final Vector2dc offset) {
        return this.translateLocal(offset.x(), offset.y());
    }
    
    public Matrix3x2d translateLocal(final Vector2dc offset, final Matrix3x2d dest) {
        return this.translateLocal(offset.x(), offset.y(), dest);
    }
    
    public Matrix3x2d translateLocal(final double x, final double y, final Matrix3x2d dest) {
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m20 = this.m20 + x;
        dest.m21 = this.m21 + y;
        return dest;
    }
    
    public Matrix3x2d translateLocal(final double x, final double y) {
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
    
    public Matrix3x2d get(final Matrix3x2d dest) {
        return dest.set(this);
    }
    
    public DoubleBuffer get(final DoubleBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public DoubleBuffer get(final int index, final DoubleBuffer buffer) {
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
    
    public DoubleBuffer get3x3(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put3x3(this, 0, buffer);
        return buffer;
    }
    
    public DoubleBuffer get3x3(final int index, final DoubleBuffer buffer) {
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
    
    public DoubleBuffer get4x4(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, 0, buffer);
        return buffer;
    }
    
    public DoubleBuffer get4x4(final int index, final DoubleBuffer buffer) {
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
    
    public Matrix3x2dc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public double[] get(final double[] arr, final int offset) {
        MemUtil.INSTANCE.copy(this, arr, offset);
        return arr;
    }
    
    public double[] get(final double[] arr) {
        return this.get(arr, 0);
    }
    
    public double[] get3x3(final double[] arr, final int offset) {
        MemUtil.INSTANCE.copy3x3(this, arr, offset);
        return arr;
    }
    
    public double[] get3x3(final double[] arr) {
        return this.get3x3(arr, 0);
    }
    
    public double[] get4x4(final double[] arr, final int offset) {
        MemUtil.INSTANCE.copy4x4(this, arr, offset);
        return arr;
    }
    
    public double[] get4x4(final double[] arr) {
        return this.get4x4(arr, 0);
    }
    
    public Matrix3x2d set(final DoubleBuffer buffer) {
        final int pos = buffer.position();
        MemUtil.INSTANCE.get(this, pos, buffer);
        return this;
    }
    
    public Matrix3x2d set(final ByteBuffer buffer) {
        final int pos = buffer.position();
        MemUtil.INSTANCE.get(this, pos, buffer);
        return this;
    }
    
    public Matrix3x2d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3x2d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3x2d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Matrix3x2d zero() {
        MemUtil.INSTANCE.zero(this);
        return this;
    }
    
    public Matrix3x2d identity() {
        MemUtil.INSTANCE.identity(this);
        return this;
    }
    
    public Matrix3x2d scale(final double x, final double y, final Matrix3x2d dest) {
        dest.m00 = this.m00 * x;
        dest.m01 = this.m01 * x;
        dest.m10 = this.m10 * y;
        dest.m11 = this.m11 * y;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2d scale(final double x, final double y) {
        return this.scale(x, y, this);
    }
    
    public Matrix3x2d scale(final Vector2dc xy) {
        return this.scale(xy.x(), xy.y(), this);
    }
    
    public Matrix3x2d scale(final Vector2dc xy, final Matrix3x2d dest) {
        return this.scale(xy.x(), xy.y(), dest);
    }
    
    public Matrix3x2d scale(final Vector2fc xy) {
        return this.scale(xy.x(), xy.y(), this);
    }
    
    public Matrix3x2d scale(final Vector2fc xy, final Matrix3x2d dest) {
        return this.scale(xy.x(), xy.y(), dest);
    }
    
    public Matrix3x2d scale(final double xy, final Matrix3x2d dest) {
        return this.scale(xy, xy, dest);
    }
    
    public Matrix3x2d scale(final double xy) {
        return this.scale(xy, xy);
    }
    
    public Matrix3x2d scaleLocal(final double x, final double y, final Matrix3x2d dest) {
        dest.m00 = x * this.m00;
        dest.m01 = y * this.m01;
        dest.m10 = x * this.m10;
        dest.m11 = y * this.m11;
        dest.m20 = x * this.m20;
        dest.m21 = y * this.m21;
        return dest;
    }
    
    public Matrix3x2d scaleLocal(final double x, final double y) {
        return this.scaleLocal(x, y, this);
    }
    
    public Matrix3x2d scaleLocal(final double xy, final Matrix3x2d dest) {
        return this.scaleLocal(xy, xy, dest);
    }
    
    public Matrix3x2d scaleLocal(final double xy) {
        return this.scaleLocal(xy, xy, this);
    }
    
    public Matrix3x2d scaleAround(final double sx, final double sy, final double ox, final double oy, final Matrix3x2d dest) {
        final double nm20 = this.m00 * ox + this.m10 * oy + this.m20;
        final double nm21 = this.m01 * ox + this.m11 * oy + this.m21;
        dest.m00 = this.m00 * sx;
        dest.m01 = this.m01 * sx;
        dest.m10 = this.m10 * sy;
        dest.m11 = this.m11 * sy;
        dest.m20 = dest.m00 * -ox + dest.m10 * -oy + nm20;
        dest.m21 = dest.m01 * -ox + dest.m11 * -oy + nm21;
        return dest;
    }
    
    public Matrix3x2d scaleAround(final double sx, final double sy, final double ox, final double oy) {
        return this.scaleAround(sx, sy, ox, oy, this);
    }
    
    public Matrix3x2d scaleAround(final double factor, final double ox, final double oy, final Matrix3x2d dest) {
        return this.scaleAround(factor, factor, ox, oy, this);
    }
    
    public Matrix3x2d scaleAround(final double factor, final double ox, final double oy) {
        return this.scaleAround(factor, factor, ox, oy, this);
    }
    
    public Matrix3x2d scaleAroundLocal(final double sx, final double sy, final double ox, final double oy, final Matrix3x2d dest) {
        dest.m00 = sx * this.m00;
        dest.m01 = sy * this.m01;
        dest.m10 = sx * this.m10;
        dest.m11 = sy * this.m11;
        dest.m20 = sx * this.m20 - sx * ox + ox;
        dest.m21 = sy * this.m21 - sy * oy + oy;
        return dest;
    }
    
    public Matrix3x2d scaleAroundLocal(final double factor, final double ox, final double oy, final Matrix3x2d dest) {
        return this.scaleAroundLocal(factor, factor, ox, oy, dest);
    }
    
    public Matrix3x2d scaleAroundLocal(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz) {
        return this.scaleAroundLocal(sx, sy, ox, oy, this);
    }
    
    public Matrix3x2d scaleAroundLocal(final double factor, final double ox, final double oy) {
        return this.scaleAroundLocal(factor, factor, ox, oy, this);
    }
    
    public Matrix3x2d scaling(final double factor) {
        return this.scaling(factor, factor);
    }
    
    public Matrix3x2d scaling(final double x, final double y) {
        this.m00 = x;
        this.m01 = 0.0;
        this.m10 = 0.0;
        this.m11 = y;
        this.m20 = 0.0;
        this.m21 = 0.0;
        return this;
    }
    
    public Matrix3x2d rotation(final double angle) {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        this.m00 = cos;
        this.m10 = -sin;
        this.m20 = 0.0;
        this.m01 = sin;
        this.m11 = cos;
        this.m21 = 0.0;
        return this;
    }
    
    public Vector3d transform(final Vector3d v) {
        return v.mul(this);
    }
    
    public Vector3d transform(final Vector3dc v, final Vector3d dest) {
        return v.mul(this, dest);
    }
    
    public Vector3d transform(final double x, final double y, final double z, final Vector3d dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20 * z, this.m01 * x + this.m11 * y + this.m21 * z, z);
    }
    
    public Vector2d transformPosition(final Vector2d v) {
        v.set(this.m00 * v.x + this.m10 * v.y + this.m20, this.m01 * v.x + this.m11 * v.y + this.m21);
        return v;
    }
    
    public Vector2d transformPosition(final Vector2dc v, final Vector2d dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20, this.m01 * v.x() + this.m11 * v.y() + this.m21);
        return dest;
    }
    
    public Vector2d transformPosition(final double x, final double y, final Vector2d dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20, this.m01 * x + this.m11 * y + this.m21);
    }
    
    public Vector2d transformDirection(final Vector2d v) {
        v.set(this.m00 * v.x + this.m10 * v.y, this.m01 * v.x + this.m11 * v.y);
        return v;
    }
    
    public Vector2d transformDirection(final Vector2dc v, final Vector2d dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y(), this.m01 * v.x() + this.m11 * v.y());
        return dest;
    }
    
    public Vector2d transformDirection(final double x, final double y, final Vector2d dest) {
        return dest.set(this.m00 * x + this.m10 * y, this.m01 * x + this.m11 * y);
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.m00);
        out.writeDouble(this.m01);
        out.writeDouble(this.m10);
        out.writeDouble(this.m11);
        out.writeDouble(this.m20);
        out.writeDouble(this.m21);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this.m00 = in.readDouble();
        this.m01 = in.readDouble();
        this.m10 = in.readDouble();
        this.m11 = in.readDouble();
        this.m20 = in.readDouble();
        this.m21 = in.readDouble();
    }
    
    public Matrix3x2d rotate(final double ang) {
        return this.rotate(ang, this);
    }
    
    public Matrix3x2d rotate(final double ang, final Matrix3x2d dest) {
        final double cos = Math.cos(ang);
        final double sin = Math.sin(ang);
        final double rm00 = cos;
        final double rm2 = sin;
        final double rm3 = -sin;
        final double rm4 = cos;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2;
        dest.m10 = this.m00 * rm3 + this.m10 * rm4;
        dest.m11 = this.m01 * rm3 + this.m11 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2d rotateLocal(final double ang, final Matrix3x2d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm00 = cos * this.m00 - sin * this.m01;
        final double nm2 = sin * this.m00 + cos * this.m01;
        final double nm3 = cos * this.m10 - sin * this.m11;
        final double nm4 = sin * this.m10 + cos * this.m11;
        final double nm5 = cos * this.m20 - sin * this.m21;
        final double nm6 = sin * this.m20 + cos * this.m21;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m20 = nm5;
        dest.m21 = nm6;
        return dest;
    }
    
    public Matrix3x2d rotateLocal(final double ang) {
        return this.rotateLocal(ang, this);
    }
    
    public Matrix3x2d rotateAbout(final double ang, final double x, final double y) {
        return this.rotateAbout(ang, x, y, this);
    }
    
    public Matrix3x2d rotateAbout(final double ang, final double x, final double y, final Matrix3x2d dest) {
        final double tm20 = this.m00 * x + this.m10 * y + this.m20;
        final double tm21 = this.m01 * x + this.m11 * y + this.m21;
        final double cos = Math.cos(ang);
        final double sin = Math.sin(ang);
        final double nm00 = this.m00 * cos + this.m10 * sin;
        final double nm2 = this.m01 * cos + this.m11 * sin;
        dest.m10 = this.m00 * -sin + this.m10 * cos;
        dest.m11 = this.m01 * -sin + this.m11 * cos;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m20 = dest.m00 * -x + dest.m10 * -y + tm20;
        dest.m21 = dest.m01 * -x + dest.m11 * -y + tm21;
        return dest;
    }
    
    public Matrix3x2d rotateTo(final Vector2dc fromDir, final Vector2dc toDir, final Matrix3x2d dest) {
        final double dot = fromDir.x() * toDir.x() + fromDir.y() * toDir.y();
        final double det = fromDir.x() * toDir.y() - fromDir.y() * toDir.x();
        final double rm00 = dot;
        final double rm2 = det;
        final double rm3 = -det;
        final double rm4 = dot;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2;
        dest.m10 = this.m00 * rm3 + this.m10 * rm4;
        dest.m11 = this.m01 * rm3 + this.m11 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        return dest;
    }
    
    public Matrix3x2d rotateTo(final Vector2dc fromDir, final Vector2dc toDir) {
        return this.rotateTo(fromDir, toDir, this);
    }
    
    public Matrix3x2d view(final double left, final double right, final double bottom, final double top, final Matrix3x2d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (left + right) / (left - right);
        final double rm4 = (bottom + top) / (bottom - top);
        dest.m20 = this.m00 * rm3 + this.m10 * rm4 + this.m20;
        dest.m21 = this.m01 * rm3 + this.m11 * rm4 + this.m21;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        return dest;
    }
    
    public Matrix3x2d view(final double left, final double right, final double bottom, final double top) {
        return this.view(left, right, bottom, top, this);
    }
    
    public Matrix3x2d setView(final double left, final double right, final double bottom, final double top) {
        this.m00 = 2.0 / (right - left);
        this.m01 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / (top - bottom);
        this.m20 = (left + right) / (left - right);
        this.m21 = (bottom + top) / (bottom - top);
        return this;
    }
    
    public Vector2d origin(final Vector2d origin) {
        final double s = 1.0 / (this.m00 * this.m11 - this.m01 * this.m10);
        origin.x = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        origin.y = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        return origin;
    }
    
    public double[] viewArea(final double[] area) {
        final double s = 1.0 / (this.m00 * this.m11 - this.m01 * this.m10);
        final double rm00 = this.m11 * s;
        final double rm2 = -this.m01 * s;
        final double rm3 = -this.m10 * s;
        final double rm4 = this.m00 * s;
        final double rm5 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final double rm6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        final double nxnyX = -rm00 - rm3;
        final double nxnyY = -rm2 - rm4;
        final double pxnyX = rm00 - rm3;
        final double pxnyY = rm2 - rm4;
        final double nxpyX = -rm00 + rm3;
        final double nxpyY = -rm2 + rm4;
        final double pxpyX = rm00 + rm3;
        final double pxpyY = rm2 + rm4;
        double minX = nxnyX;
        minX = ((minX < nxpyX) ? minX : nxpyX);
        minX = ((minX < pxnyX) ? minX : pxnyX);
        minX = ((minX < pxpyX) ? minX : pxpyX);
        double minY = nxnyY;
        minY = ((minY < nxpyY) ? minY : nxpyY);
        minY = ((minY < pxnyY) ? minY : pxnyY);
        minY = ((minY < pxpyY) ? minY : pxpyY);
        double maxX = nxnyX;
        maxX = ((maxX > nxpyX) ? maxX : nxpyX);
        maxX = ((maxX > pxnyX) ? maxX : pxnyX);
        maxX = ((maxX > pxpyX) ? maxX : pxpyX);
        double maxY = nxnyY;
        maxY = ((maxY > nxpyY) ? maxY : nxpyY);
        maxY = ((maxY > pxnyY) ? maxY : pxnyY);
        maxY = ((maxY > pxpyY) ? maxY : pxpyY);
        area[0] = minX + rm5;
        area[1] = minY + rm6;
        area[2] = maxX + rm5;
        area[3] = maxY + rm6;
        return area;
    }
    
    public Vector2d positiveX(final Vector2d dir) {
        double s = this.m00 * this.m11 - this.m01 * this.m10;
        s = 1.0 / s;
        dir.x = this.m11 * s;
        dir.y = -this.m01 * s;
        return dir.normalize(dir);
    }
    
    public Vector2d normalizedPositiveX(final Vector2d dir) {
        dir.x = this.m11;
        dir.y = -this.m01;
        return dir;
    }
    
    public Vector2d positiveY(final Vector2d dir) {
        double s = this.m00 * this.m11 - this.m01 * this.m10;
        s = 1.0 / s;
        dir.x = -this.m10 * s;
        dir.y = this.m00 * s;
        return dir.normalize(dir);
    }
    
    public Vector2d normalizedPositiveY(final Vector2d dir) {
        dir.x = -this.m10;
        dir.y = this.m00;
        return dir;
    }
    
    public Vector2d unproject(final double winX, final double winY, final int[] viewport, final Vector2d dest) {
        final double s = 1.0 / (this.m00 * this.m11 - this.m01 * this.m10);
        final double im00 = this.m11 * s;
        final double im2 = -this.m01 * s;
        final double im3 = -this.m10 * s;
        final double im4 = this.m00 * s;
        final double im5 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final double im6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        dest.x = im00 * ndcX + im3 * ndcY + im5;
        dest.y = im2 * ndcX + im4 * ndcY + im6;
        return dest;
    }
    
    public Vector2d unprojectInv(final double winX, final double winY, final int[] viewport, final Vector2d dest) {
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        dest.x = this.m00 * ndcX + this.m10 * ndcY + this.m20;
        dest.y = this.m01 * ndcX + this.m11 * ndcY + this.m21;
        return dest;
    }
    
    public Matrix3x2d span(final Vector2d corner, final Vector2d xDir, final Vector2d yDir) {
        final double s = 1.0 / (this.m00 * this.m11 - this.m01 * this.m10);
        final double nm00 = this.m11 * s;
        final double nm2 = -this.m01 * s;
        final double nm3 = -this.m10 * s;
        final double nm4 = this.m00 * s;
        corner.x = -nm00 - nm3 + (this.m10 * this.m21 - this.m20 * this.m11) * s;
        corner.y = -nm2 - nm4 + (this.m20 * this.m01 - this.m00 * this.m21) * s;
        xDir.x = 2.0 * nm00;
        xDir.y = 2.0 * nm2;
        yDir.x = 2.0 * nm3;
        yDir.y = 2.0 * nm4;
        return this;
    }
    
    public boolean testPoint(final double x, final double y) {
        final double nxX = this.m00;
        final double nxY = this.m10;
        final double nxW = 1.0 + this.m20;
        final double pxX = -this.m00;
        final double pxY = -this.m10;
        final double pxW = 1.0 - this.m20;
        final double nyX = this.m01;
        final double nyY = this.m11;
        final double nyW = 1.0 + this.m21;
        final double pyX = -this.m01;
        final double pyY = -this.m11;
        final double pyW = 1.0 - this.m21;
        return nxX * x + nxY * y + nxW >= 0.0 && pxX * x + pxY * y + pxW >= 0.0 && nyX * x + nyY * y + nyW >= 0.0 && pyX * x + pyY * y + pyW >= 0.0;
    }
    
    public boolean testCircle(final double x, final double y, final double r) {
        double nxX = this.m00;
        double nxY = this.m10;
        double nxW = 1.0 + this.m20;
        double invl = Math.invsqrt(nxX * nxX + nxY * nxY);
        nxX *= invl;
        nxY *= invl;
        nxW *= invl;
        double pxX = -this.m00;
        double pxY = -this.m10;
        double pxW = 1.0 - this.m20;
        invl = Math.invsqrt(pxX * pxX + pxY * pxY);
        pxX *= invl;
        pxY *= invl;
        pxW *= invl;
        double nyX = this.m01;
        double nyY = this.m11;
        double nyW = 1.0 + this.m21;
        invl = Math.invsqrt(nyX * nyX + nyY * nyY);
        nyX *= invl;
        nyY *= invl;
        nyW *= invl;
        double pyX = -this.m01;
        double pyY = -this.m11;
        double pyW = 1.0 - this.m21;
        invl = Math.invsqrt(pyX * pyX + pyY * pyY);
        pyX *= invl;
        pyY *= invl;
        pyW *= invl;
        return nxX * x + nxY * y + nxW >= -r && pxX * x + pxY * y + pxW >= -r && nyX * x + nyY * y + nyW >= -r && pyX * x + pyY * y + pyW >= -r;
    }
    
    public boolean testAar(final double minX, final double minY, final double maxX, final double maxY) {
        final double nxX = this.m00;
        final double nxY = this.m10;
        final double nxW = 1.0 + this.m20;
        final double pxX = -this.m00;
        final double pxY = -this.m10;
        final double pxW = 1.0 - this.m20;
        final double nyX = this.m01;
        final double nyY = this.m11;
        final double nyW = 1.0 + this.m21;
        final double pyX = -this.m01;
        final double pyY = -this.m11;
        final double pyW = 1.0 - this.m21;
        return nxX * ((nxX < 0.0) ? minX : maxX) + nxY * ((nxY < 0.0) ? minY : maxY) >= -nxW && pxX * ((pxX < 0.0) ? minX : maxX) + pxY * ((pxY < 0.0) ? minY : maxY) >= -pxW && nyX * ((nyX < 0.0) ? minX : maxX) + nyY * ((nyY < 0.0) ? minY : maxY) >= -nyW && pyX * ((pyX < 0.0) ? minX : maxX) + pyY * ((pyY < 0.0) ? minY : maxY) >= -pyW;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.m00);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m01);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m10);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m11);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m20);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m21);
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
        final Matrix3x2d other = (Matrix3x2d)obj;
        return Double.doubleToLongBits(this.m00) == Double.doubleToLongBits(other.m00) && Double.doubleToLongBits(this.m01) == Double.doubleToLongBits(other.m01) && Double.doubleToLongBits(this.m10) == Double.doubleToLongBits(other.m10) && Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(other.m11) && Double.doubleToLongBits(this.m20) == Double.doubleToLongBits(other.m20) && Double.doubleToLongBits(this.m21) == Double.doubleToLongBits(other.m21);
    }
    
    public boolean equals(final Matrix3x2dc m, final double delta) {
        return this == m || (m != null && m instanceof Matrix3x2d && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta));
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m20) && Math.isFinite(this.m21);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
