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

public class Matrix2f implements Externalizable, Cloneable, Matrix2fc
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m10;
    public float m11;
    
    public Matrix2f() {
        this.m00 = 1.0f;
        this.m11 = 1.0f;
    }
    
    public Matrix2f(final Matrix2fc mat) {
        if (mat instanceof Matrix2f) {
            MemUtil.INSTANCE.copy((Matrix2f)mat, this);
        }
        else {
            this.setMatrix2fc(mat);
        }
    }
    
    public Matrix2f(final Matrix3fc mat) {
        if (mat instanceof Matrix3f) {
            MemUtil.INSTANCE.copy((Matrix3f)mat, this);
        }
        else {
            this.setMatrix3fc(mat);
        }
    }
    
    public Matrix2f(final float m00, final float m01, final float m10, final float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }
    
    public Matrix2f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Matrix2f(final Vector2fc col0, final Vector2fc col1) {
        this.m00 = col0.x();
        this.m01 = col0.y();
        this.m10 = col1.x();
        this.m11 = col1.y();
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
    
    public Matrix2f m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    public Matrix2f m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    public Matrix2f m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    public Matrix2f m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix2f _m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix2f _m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix2f _m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix2f _m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    public Matrix2f set(final Matrix2fc m) {
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
    
    public Matrix2f set(final Matrix3x2fc m) {
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
    }
    
    public Matrix2f set(final Matrix3fc m) {
        if (m instanceof Matrix3f) {
            MemUtil.INSTANCE.copy((Matrix3f)m, this);
        }
        else {
            this.setMatrix3fc(m);
        }
        return this;
    }
    
    private void setMatrix3fc(final Matrix3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix2f mul(final Matrix2fc right) {
        return this.mul(right, this);
    }
    
    public Matrix2f mul(final Matrix2fc right, final Matrix2f dest) {
        final float nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final float nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final float nm3 = this.m00 * right.m10() + this.m10 * right.m11();
        final float nm4 = this.m01 * right.m10() + this.m11 * right.m11();
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2f mulLocal(final Matrix2fc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix2f mulLocal(final Matrix2fc left, final Matrix2f dest) {
        final float nm00 = left.m00() * this.m00 + left.m10() * this.m01;
        final float nm2 = left.m01() * this.m00 + left.m11() * this.m01;
        final float nm3 = left.m00() * this.m10 + left.m10() * this.m11;
        final float nm4 = left.m01() * this.m10 + left.m11() * this.m11;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2f set(final float m00, final float m01, final float m10, final float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        return this;
    }
    
    public Matrix2f set(final float[] m) {
        MemUtil.INSTANCE.copy(m, 0, this);
        return this;
    }
    
    public Matrix2f set(final Vector2fc col0, final Vector2fc col1) {
        this.m00 = col0.x();
        this.m01 = col0.y();
        this.m10 = col1.x();
        this.m11 = col1.y();
        return this;
    }
    
    public float determinant() {
        return this.m00 * this.m11 - this.m10 * this.m01;
    }
    
    public Matrix2f invert() {
        return this.invert(this);
    }
    
    public Matrix2f invert(final Matrix2f dest) {
        final float s = 1.0f / this.determinant();
        final float nm00 = this.m11 * s;
        final float nm2 = -this.m01 * s;
        final float nm3 = -this.m10 * s;
        final float nm4 = this.m00 * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2f transpose() {
        return this.transpose(this);
    }
    
    public Matrix2f transpose(final Matrix2f dest) {
        dest.set(this.m00, this.m10, this.m01, this.m11);
        return dest;
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
        return Runtime.format(this.m00, formatter) + " " + Runtime.format(this.m10, formatter) + "\n" + Runtime.format(this.m01, formatter) + " " + Runtime.format(this.m11, formatter) + "\n";
    }
    
    public Matrix2f get(final Matrix2f dest) {
        return dest.set(this);
    }
    
    public Matrix3x2f get(final Matrix3x2f dest) {
        return dest.set(this);
    }
    
    public Matrix3f get(final Matrix3f dest) {
        return dest.set(this);
    }
    
    public float getRotation() {
        return Math.atan2(this.m01, this.m11);
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
    
    public FloatBuffer getTransposed(final FloatBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public FloatBuffer getTransposed(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer getTransposed(final ByteBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public ByteBuffer getTransposed(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, index, buffer);
        return buffer;
    }
    
    public Matrix2fc getToAddress(final long address) {
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
    
    public Matrix2f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix2f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix2f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix2f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix2f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Matrix2f zero() {
        MemUtil.INSTANCE.zero(this);
        return this;
    }
    
    public Matrix2f identity() {
        MemUtil.INSTANCE.identity(this);
        return this;
    }
    
    public Matrix2f scale(final Vector2fc xy, final Matrix2f dest) {
        return this.scale(xy.x(), xy.y(), dest);
    }
    
    public Matrix2f scale(final Vector2fc xy) {
        return this.scale(xy.x(), xy.y(), this);
    }
    
    public Matrix2f scale(final float x, final float y, final Matrix2f dest) {
        dest.m00 = this.m00 * x;
        dest.m01 = this.m01 * x;
        dest.m10 = this.m10 * y;
        dest.m11 = this.m11 * y;
        return dest;
    }
    
    public Matrix2f scale(final float x, final float y) {
        return this.scale(x, y, this);
    }
    
    public Matrix2f scale(final float xy, final Matrix2f dest) {
        return this.scale(xy, xy, dest);
    }
    
    public Matrix2f scale(final float xy) {
        return this.scale(xy, xy);
    }
    
    public Matrix2f scaleLocal(final float x, final float y, final Matrix2f dest) {
        dest.m00 = x * this.m00;
        dest.m01 = y * this.m01;
        dest.m10 = x * this.m10;
        dest.m11 = y * this.m11;
        return dest;
    }
    
    public Matrix2f scaleLocal(final float x, final float y) {
        return this.scaleLocal(x, y, this);
    }
    
    public Matrix2f scaling(final float factor) {
        MemUtil.INSTANCE.zero(this);
        this.m00 = factor;
        this.m11 = factor;
        return this;
    }
    
    public Matrix2f scaling(final float x, final float y) {
        MemUtil.INSTANCE.zero(this);
        this.m00 = x;
        this.m11 = y;
        return this;
    }
    
    public Matrix2f scaling(final Vector2fc xy) {
        return this.scaling(xy.x(), xy.y());
    }
    
    public Matrix2f rotation(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
        return this;
    }
    
    public Vector2f transform(final Vector2f v) {
        return v.mul(this);
    }
    
    public Vector2f transform(final Vector2fc v, final Vector2f dest) {
        v.mul(this, dest);
        return dest;
    }
    
    public Vector2f transform(final float x, final float y, final Vector2f dest) {
        dest.set(this.m00 * x + this.m10 * y, this.m01 * x + this.m11 * y);
        return dest;
    }
    
    public Vector2f transformTranspose(final Vector2f v) {
        return v.mulTranspose(this);
    }
    
    public Vector2f transformTranspose(final Vector2fc v, final Vector2f dest) {
        v.mulTranspose(this, dest);
        return dest;
    }
    
    public Vector2f transformTranspose(final float x, final float y, final Vector2f dest) {
        dest.set(this.m00 * x + this.m01 * y, this.m10 * x + this.m11 * y);
        return dest;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.m00);
        out.writeFloat(this.m01);
        out.writeFloat(this.m10);
        out.writeFloat(this.m11);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this.m00 = in.readFloat();
        this.m01 = in.readFloat();
        this.m10 = in.readFloat();
        this.m11 = in.readFloat();
    }
    
    public Matrix2f rotate(final float angle) {
        return this.rotate(angle, this);
    }
    
    public Matrix2f rotate(final float angle, final Matrix2f dest) {
        final float s = Math.sin(angle);
        final float c = Math.cosFromSin(s, angle);
        final float nm00 = this.m00 * c + this.m10 * s;
        final float nm2 = this.m01 * c + this.m11 * s;
        final float nm3 = this.m10 * c - this.m00 * s;
        final float nm4 = this.m11 * c - this.m01 * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2f rotateLocal(final float angle) {
        return this.rotateLocal(angle, this);
    }
    
    public Matrix2f rotateLocal(final float angle, final Matrix2f dest) {
        final float s = Math.sin(angle);
        final float c = Math.cosFromSin(s, angle);
        final float nm00 = c * this.m00 - s * this.m01;
        final float nm2 = s * this.m00 + c * this.m01;
        final float nm3 = c * this.m10 - s * this.m11;
        final float nm4 = s * this.m10 + c * this.m11;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Vector2f getRow(final int row, final Vector2f dest) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                dest.x = this.m00;
                dest.y = this.m10;
                break;
            }
            case 1: {
                dest.x = this.m01;
                dest.y = this.m11;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Matrix2f setRow(final int row, final Vector2fc src) throws IndexOutOfBoundsException {
        return this.setRow(row, src.x(), src.y());
    }
    
    public Matrix2f setRow(final int row, final float x, final float y) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                this.m00 = x;
                this.m10 = y;
                break;
            }
            case 1: {
                this.m01 = x;
                this.m11 = y;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return this;
    }
    
    public Vector2f getColumn(final int column, final Vector2f dest) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                dest.x = this.m00;
                dest.y = this.m01;
                break;
            }
            case 1: {
                dest.x = this.m10;
                dest.y = this.m11;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Matrix2f setColumn(final int column, final Vector2fc src) throws IndexOutOfBoundsException {
        return this.setColumn(column, src.x(), src.y());
    }
    
    public Matrix2f setColumn(final int column, final float x, final float y) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                this.m00 = x;
                this.m01 = y;
                break;
            }
            case 1: {
                this.m10 = x;
                this.m11 = y;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return this;
    }
    
    public float get(final int column, final int row) {
        Label_0109: {
            switch (column) {
                case 0: {
                    switch (row) {
                        case 0: {
                            return this.m00;
                        }
                        case 1: {
                            return this.m01;
                        }
                        default: {
                            break Label_0109;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (row) {
                        case 0: {
                            return this.m10;
                        }
                        case 1: {
                            return this.m11;
                        }
                        default: {
                            break Label_0109;
                        }
                    }
                    break;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }
    
    public Matrix2f set(final int column, final int row, final float value) {
        Label_0117: {
            switch (column) {
                case 0: {
                    switch (row) {
                        case 0: {
                            this.m00 = value;
                            return this;
                        }
                        case 1: {
                            this.m01 = value;
                            return this;
                        }
                        default: {
                            break Label_0117;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (row) {
                        case 0: {
                            this.m10 = value;
                            return this;
                        }
                        case 1: {
                            this.m11 = value;
                            return this;
                        }
                        default: {
                            break Label_0117;
                        }
                    }
                    break;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }
    
    public Matrix2f normal() {
        return this.normal(this);
    }
    
    public Matrix2f normal(final Matrix2f dest) {
        final float det = this.m00 * this.m11 - this.m10 * this.m01;
        final float s = 1.0f / det;
        final float nm00 = this.m11 * s;
        final float nm2 = -this.m10 * s;
        final float nm3 = -this.m01 * s;
        final float nm4 = this.m00 * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Vector2f getScale(final Vector2f dest) {
        dest.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01);
        dest.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11);
        return dest;
    }
    
    public Vector2f positiveX(final Vector2f dir) {
        if (this.m00 * this.m11 < this.m01 * this.m10) {
            dir.x = -this.m11;
            dir.y = this.m01;
        }
        else {
            dir.x = this.m11;
            dir.y = -this.m01;
        }
        return dir.normalize(dir);
    }
    
    public Vector2f normalizedPositiveX(final Vector2f dir) {
        if (this.m00 * this.m11 < this.m01 * this.m10) {
            dir.x = -this.m11;
            dir.y = this.m01;
        }
        else {
            dir.x = this.m11;
            dir.y = -this.m01;
        }
        return dir;
    }
    
    public Vector2f positiveY(final Vector2f dir) {
        if (this.m00 * this.m11 < this.m01 * this.m10) {
            dir.x = this.m10;
            dir.y = -this.m00;
        }
        else {
            dir.x = -this.m10;
            dir.y = this.m00;
        }
        return dir.normalize(dir);
    }
    
    public Vector2f normalizedPositiveY(final Vector2f dir) {
        if (this.m00 * this.m11 < this.m01 * this.m10) {
            dir.x = this.m10;
            dir.y = -this.m00;
        }
        else {
            dir.x = -this.m10;
            dir.y = this.m00;
        }
        return dir;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.m00);
        result = 31 * result + Float.floatToIntBits(this.m01);
        result = 31 * result + Float.floatToIntBits(this.m10);
        result = 31 * result + Float.floatToIntBits(this.m11);
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
        final Matrix2f other = (Matrix2f)obj;
        return Float.floatToIntBits(this.m00) == Float.floatToIntBits(other.m00) && Float.floatToIntBits(this.m01) == Float.floatToIntBits(other.m01) && Float.floatToIntBits(this.m10) == Float.floatToIntBits(other.m10) && Float.floatToIntBits(this.m11) == Float.floatToIntBits(other.m11);
    }
    
    public boolean equals(final Matrix2fc m, final float delta) {
        return this == m || (m != null && m instanceof Matrix2f && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta));
    }
    
    public Matrix2f swap(final Matrix2f other) {
        MemUtil.INSTANCE.swap(this, other);
        return this;
    }
    
    public Matrix2f add(final Matrix2fc other) {
        return this.add(other, this);
    }
    
    public Matrix2f add(final Matrix2fc other, final Matrix2f dest) {
        dest.m00 = this.m00 + other.m00();
        dest.m01 = this.m01 + other.m01();
        dest.m10 = this.m10 + other.m10();
        dest.m11 = this.m11 + other.m11();
        return dest;
    }
    
    public Matrix2f sub(final Matrix2fc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix2f sub(final Matrix2fc other, final Matrix2f dest) {
        dest.m00 = this.m00 - other.m00();
        dest.m01 = this.m01 - other.m01();
        dest.m10 = this.m10 - other.m10();
        dest.m11 = this.m11 - other.m11();
        return dest;
    }
    
    public Matrix2f mulComponentWise(final Matrix2fc other) {
        return this.sub(other, this);
    }
    
    public Matrix2f mulComponentWise(final Matrix2fc other, final Matrix2f dest) {
        dest.m00 = this.m00 * other.m00();
        dest.m01 = this.m01 * other.m01();
        dest.m10 = this.m10 * other.m10();
        dest.m11 = this.m11 * other.m11();
        return dest;
    }
    
    public Matrix2f lerp(final Matrix2fc other, final float t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix2f lerp(final Matrix2fc other, final float t, final Matrix2f dest) {
        dest.m00 = Math.fma(other.m00() - this.m00, t, this.m00);
        dest.m01 = Math.fma(other.m01() - this.m01, t, this.m01);
        dest.m10 = Math.fma(other.m10() - this.m10, t, this.m10);
        dest.m11 = Math.fma(other.m11() - this.m11, t, this.m11);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m10) && Math.isFinite(this.m11);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
