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

public class Matrix2d implements Externalizable, Cloneable, Matrix2dc
{
    private static final long serialVersionUID = 1L;
    public double m00;
    public double m01;
    public double m10;
    public double m11;
    
    public Matrix2d() {
        this.m00 = 1.0;
        this.m11 = 1.0;
    }
    
    public Matrix2d(final Matrix2dc mat) {
        if (mat instanceof Matrix2d) {
            MemUtil.INSTANCE.copy((Matrix2d)mat, this);
        }
        else {
            this.setMatrix2dc(mat);
        }
    }
    
    public Matrix2d(final Matrix2fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix2d(final Matrix3dc mat) {
        if (mat instanceof Matrix3d) {
            MemUtil.INSTANCE.copy((Matrix3d)mat, this);
        }
        else {
            this.setMatrix3dc(mat);
        }
    }
    
    public Matrix2d(final Matrix3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix2d(final double m00, final double m01, final double m10, final double m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }
    
    public Matrix2d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Matrix2d(final Vector2dc col0, final Vector2dc col1) {
        this.m00 = col0.x();
        this.m01 = col0.y();
        this.m10 = col1.x();
        this.m11 = col1.y();
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
    
    public Matrix2d m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    public Matrix2d m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    public Matrix2d m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    public Matrix2d m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix2d _m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix2d _m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix2d _m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix2d _m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    public Matrix2d set(final Matrix2dc m) {
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
    
    public Matrix2d set(final Matrix2fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m10 = m.m10();
        this.m11 = m.m11();
        return this;
    }
    
    public Matrix2d set(final Matrix3x2dc m) {
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
    }
    
    public Matrix2d set(final Matrix3x2fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m10 = m.m10();
        this.m11 = m.m11();
        return this;
    }
    
    public Matrix2d set(final Matrix3dc m) {
        if (m instanceof Matrix3d) {
            MemUtil.INSTANCE.copy((Matrix3d)m, this);
        }
        else {
            this.setMatrix3dc(m);
        }
        return this;
    }
    
    private void setMatrix3dc(final Matrix3dc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
    }
    
    public Matrix2d set(final Matrix3fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m10 = m.m10();
        this.m11 = m.m11();
        return this;
    }
    
    public Matrix2d mul(final Matrix2dc right) {
        return this.mul(right, this);
    }
    
    public Matrix2d mul(final Matrix2dc right, final Matrix2d dest) {
        final double nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final double nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final double nm3 = this.m00 * right.m10() + this.m10 * right.m11();
        final double nm4 = this.m01 * right.m10() + this.m11 * right.m11();
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2d mul(final Matrix2fc right) {
        return this.mul(right, this);
    }
    
    public Matrix2d mul(final Matrix2fc right, final Matrix2d dest) {
        final double nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final double nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final double nm3 = this.m00 * right.m10() + this.m10 * right.m11();
        final double nm4 = this.m01 * right.m10() + this.m11 * right.m11();
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2d mulLocal(final Matrix2dc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix2d mulLocal(final Matrix2dc left, final Matrix2d dest) {
        final double nm00 = left.m00() * this.m00 + left.m10() * this.m01;
        final double nm2 = left.m01() * this.m00 + left.m11() * this.m01;
        final double nm3 = left.m00() * this.m10 + left.m10() * this.m11;
        final double nm4 = left.m01() * this.m10 + left.m11() * this.m11;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2d set(final double m00, final double m01, final double m10, final double m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        return this;
    }
    
    public Matrix2d set(final double[] m) {
        MemUtil.INSTANCE.copy(m, 0, this);
        return this;
    }
    
    public Matrix2d set(final Vector2dc col0, final Vector2dc col1) {
        this.m00 = col0.x();
        this.m01 = col0.y();
        this.m10 = col1.x();
        this.m11 = col1.y();
        return this;
    }
    
    public double determinant() {
        return this.m00 * this.m11 - this.m10 * this.m01;
    }
    
    public Matrix2d invert() {
        return this.invert(this);
    }
    
    public Matrix2d invert(final Matrix2d dest) {
        final double s = 1.0 / this.determinant();
        final double nm00 = this.m11 * s;
        final double nm2 = -this.m01 * s;
        final double nm3 = -this.m10 * s;
        final double nm4 = this.m00 * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2d transpose() {
        return this.transpose(this);
    }
    
    public Matrix2d transpose(final Matrix2d dest) {
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
    
    public Matrix2d get(final Matrix2d dest) {
        return dest.set(this);
    }
    
    public Matrix3x2d get(final Matrix3x2d dest) {
        return dest.set(this);
    }
    
    public Matrix3d get(final Matrix3d dest) {
        return dest.set(this);
    }
    
    public double getRotation() {
        return Math.atan2(this.m01, this.m11);
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
    
    public DoubleBuffer getTransposed(final DoubleBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public DoubleBuffer getTransposed(final int index, final DoubleBuffer buffer) {
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
    
    public Matrix2dc getToAddress(final long address) {
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
    
    public Matrix2d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix2d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix2d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix2d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix2d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Matrix2d zero() {
        MemUtil.INSTANCE.zero(this);
        return this;
    }
    
    public Matrix2d identity() {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        return this;
    }
    
    public Matrix2d scale(final Vector2dc xy, final Matrix2d dest) {
        return this.scale(xy.x(), xy.y(), dest);
    }
    
    public Matrix2d scale(final Vector2dc xy) {
        return this.scale(xy.x(), xy.y(), this);
    }
    
    public Matrix2d scale(final double x, final double y, final Matrix2d dest) {
        dest.m00 = this.m00 * x;
        dest.m01 = this.m01 * x;
        dest.m10 = this.m10 * y;
        dest.m11 = this.m11 * y;
        return dest;
    }
    
    public Matrix2d scale(final double x, final double y) {
        return this.scale(x, y, this);
    }
    
    public Matrix2d scale(final double xy, final Matrix2d dest) {
        return this.scale(xy, xy, dest);
    }
    
    public Matrix2d scale(final double xy) {
        return this.scale(xy, xy);
    }
    
    public Matrix2d scaleLocal(final double x, final double y, final Matrix2d dest) {
        dest.m00 = x * this.m00;
        dest.m01 = y * this.m01;
        dest.m10 = x * this.m10;
        dest.m11 = y * this.m11;
        return dest;
    }
    
    public Matrix2d scaleLocal(final double x, final double y) {
        return this.scaleLocal(x, y, this);
    }
    
    public Matrix2d scaling(final double factor) {
        MemUtil.INSTANCE.zero(this);
        this.m00 = factor;
        this.m11 = factor;
        return this;
    }
    
    public Matrix2d scaling(final double x, final double y) {
        MemUtil.INSTANCE.zero(this);
        this.m00 = x;
        this.m11 = y;
        return this;
    }
    
    public Matrix2d scaling(final Vector2dc xy) {
        return this.scaling(xy.x(), xy.y());
    }
    
    public Matrix2d rotation(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
        return this;
    }
    
    public Vector2d transform(final Vector2d v) {
        return v.mul(this);
    }
    
    public Vector2d transform(final Vector2dc v, final Vector2d dest) {
        v.mul(this, dest);
        return dest;
    }
    
    public Vector2d transform(final double x, final double y, final Vector2d dest) {
        dest.set(this.m00 * x + this.m10 * y, this.m01 * x + this.m11 * y);
        return dest;
    }
    
    public Vector2d transformTranspose(final Vector2d v) {
        return v.mulTranspose(this);
    }
    
    public Vector2d transformTranspose(final Vector2dc v, final Vector2d dest) {
        v.mulTranspose(this, dest);
        return dest;
    }
    
    public Vector2d transformTranspose(final double x, final double y, final Vector2d dest) {
        dest.set(this.m00 * x + this.m01 * y, this.m10 * x + this.m11 * y);
        return dest;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.m00);
        out.writeDouble(this.m01);
        out.writeDouble(this.m10);
        out.writeDouble(this.m11);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this.m00 = in.readDouble();
        this.m01 = in.readDouble();
        this.m10 = in.readDouble();
        this.m11 = in.readDouble();
    }
    
    public Matrix2d rotate(final double angle) {
        return this.rotate(angle, this);
    }
    
    public Matrix2d rotate(final double angle, final Matrix2d dest) {
        final double s = Math.sin(angle);
        final double c = Math.cosFromSin(s, angle);
        final double nm00 = this.m00 * c + this.m10 * s;
        final double nm2 = this.m01 * c + this.m11 * s;
        final double nm3 = this.m10 * c - this.m00 * s;
        final double nm4 = this.m11 * c - this.m01 * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Matrix2d rotateLocal(final double angle) {
        return this.rotateLocal(angle, this);
    }
    
    public Matrix2d rotateLocal(final double angle, final Matrix2d dest) {
        final double s = Math.sin(angle);
        final double c = Math.cosFromSin(s, angle);
        final double nm00 = c * this.m00 - s * this.m01;
        final double nm2 = s * this.m00 + c * this.m01;
        final double nm3 = c * this.m10 - s * this.m11;
        final double nm4 = s * this.m10 + c * this.m11;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Vector2d getRow(final int row, final Vector2d dest) throws IndexOutOfBoundsException {
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
    
    public Matrix2d setRow(final int row, final Vector2dc src) throws IndexOutOfBoundsException {
        return this.setRow(row, src.x(), src.y());
    }
    
    public Matrix2d setRow(final int row, final double x, final double y) throws IndexOutOfBoundsException {
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
    
    public Vector2d getColumn(final int column, final Vector2d dest) throws IndexOutOfBoundsException {
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
    
    public Matrix2d setColumn(final int column, final Vector2dc src) throws IndexOutOfBoundsException {
        return this.setColumn(column, src.x(), src.y());
    }
    
    public Matrix2d setColumn(final int column, final double x, final double y) throws IndexOutOfBoundsException {
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
    
    public double get(final int column, final int row) {
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
    
    public Matrix2d set(final int column, final int row, final double value) {
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
    
    public Matrix2d normal() {
        return this.normal(this);
    }
    
    public Matrix2d normal(final Matrix2d dest) {
        final double det = this.m00 * this.m11 - this.m10 * this.m01;
        final double s = 1.0 / det;
        final double nm00 = this.m11 * s;
        final double nm2 = -this.m10 * s;
        final double nm3 = -this.m01 * s;
        final double nm4 = this.m00 * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m10 = nm3;
        dest.m11 = nm4;
        return dest;
    }
    
    public Vector2d getScale(final Vector2d dest) {
        dest.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01);
        dest.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11);
        return dest;
    }
    
    public Vector2d positiveX(final Vector2d dir) {
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
    
    public Vector2d normalizedPositiveX(final Vector2d dir) {
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
    
    public Vector2d positiveY(final Vector2d dir) {
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
    
    public Vector2d normalizedPositiveY(final Vector2d dir) {
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
        long temp = Double.doubleToLongBits(this.m00);
        result = 31 * result + (int)(temp >>> 32 ^ temp);
        temp = Double.doubleToLongBits(this.m01);
        result = 31 * result + (int)(temp >>> 32 ^ temp);
        temp = Double.doubleToLongBits(this.m10);
        result = 31 * result + (int)(temp >>> 32 ^ temp);
        temp = Double.doubleToLongBits(this.m11);
        result = 31 * result + (int)(temp >>> 32 ^ temp);
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
        final Matrix2d other = (Matrix2d)obj;
        return Double.doubleToLongBits(this.m00) == Double.doubleToLongBits(other.m00) && Double.doubleToLongBits(this.m01) == Double.doubleToLongBits(other.m01) && Double.doubleToLongBits(this.m10) == Double.doubleToLongBits(other.m10) && Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(other.m11);
    }
    
    public boolean equals(final Matrix2dc m, final double delta) {
        return this == m || (m != null && m instanceof Matrix2d && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta));
    }
    
    public Matrix2d swap(final Matrix2d other) {
        MemUtil.INSTANCE.swap(this, other);
        return this;
    }
    
    public Matrix2d add(final Matrix2dc other) {
        return this.add(other, this);
    }
    
    public Matrix2d add(final Matrix2dc other, final Matrix2d dest) {
        dest.m00 = this.m00 + other.m00();
        dest.m01 = this.m01 + other.m01();
        dest.m10 = this.m10 + other.m10();
        dest.m11 = this.m11 + other.m11();
        return dest;
    }
    
    public Matrix2d sub(final Matrix2dc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix2d sub(final Matrix2dc other, final Matrix2d dest) {
        dest.m00 = this.m00 - other.m00();
        dest.m01 = this.m01 - other.m01();
        dest.m10 = this.m10 - other.m10();
        dest.m11 = this.m11 - other.m11();
        return dest;
    }
    
    public Matrix2d mulComponentWise(final Matrix2dc other) {
        return this.sub(other, this);
    }
    
    public Matrix2d mulComponentWise(final Matrix2dc other, final Matrix2d dest) {
        dest.m00 = this.m00 * other.m00();
        dest.m01 = this.m01 * other.m01();
        dest.m10 = this.m10 * other.m10();
        dest.m11 = this.m11 * other.m11();
        return dest;
    }
    
    public Matrix2d lerp(final Matrix2dc other, final double t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix2d lerp(final Matrix2dc other, final double t, final Matrix2d dest) {
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
