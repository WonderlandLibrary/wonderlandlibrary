/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.GMatrix;
import javax.vecmath.MismatchedSizeException;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;
import javax.vecmath.VecMathI18N;
import javax.vecmath.VecMathUtil;

public class GVector
implements Serializable,
Cloneable {
    private int length;
    double[] values;
    static final long serialVersionUID = 1398850036893875112L;

    public GVector(int length) {
        this.length = length;
        this.values = new double[length];
        for (int i = 0; i < length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public GVector(double[] vector) {
        this.length = vector.length;
        this.values = new double[vector.length];
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = vector[i];
        }
    }

    public GVector(GVector vector) {
        this.values = new double[vector.length];
        this.length = vector.length;
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = vector.values[i];
        }
    }

    public GVector(Tuple2f tuple) {
        this.values = new double[2];
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.length = 2;
    }

    public GVector(Tuple3f tuple) {
        this.values = new double[3];
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        this.length = 3;
    }

    public GVector(Tuple3d tuple) {
        this.values = new double[3];
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        this.length = 3;
    }

    public GVector(Tuple4f tuple) {
        this.values = new double[4];
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        this.values[3] = tuple.w;
        this.length = 4;
    }

    public GVector(Tuple4d tuple) {
        this.values = new double[4];
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        this.values[3] = tuple.w;
        this.length = 4;
    }

    public GVector(double[] vector, int length) {
        this.length = length;
        this.values = new double[length];
        for (int i = 0; i < length; ++i) {
            this.values[i] = vector[i];
        }
    }

    public final double norm() {
        double sq = 0.0;
        for (int i = 0; i < this.length; ++i) {
            sq += this.values[i] * this.values[i];
        }
        return Math.sqrt(sq);
    }

    public final double normSquared() {
        double sq = 0.0;
        for (int i = 0; i < this.length; ++i) {
            sq += this.values[i] * this.values[i];
        }
        return sq;
    }

    public final void normalize(GVector v1) {
        int i;
        double sq = 0.0;
        if (this.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector0"));
        }
        for (i = 0; i < this.length; ++i) {
            sq += v1.values[i] * v1.values[i];
        }
        double invMag = 1.0 / Math.sqrt(sq);
        for (i = 0; i < this.length; ++i) {
            this.values[i] = v1.values[i] * invMag;
        }
    }

    public final void normalize() {
        int i;
        double sq = 0.0;
        for (i = 0; i < this.length; ++i) {
            sq += this.values[i] * this.values[i];
        }
        double invMag = 1.0 / Math.sqrt(sq);
        for (i = 0; i < this.length; ++i) {
            this.values[i] = this.values[i] * invMag;
        }
    }

    public final void scale(double s, GVector v1) {
        if (this.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector1"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = v1.values[i] * s;
        }
    }

    public final void scale(double s) {
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = this.values[i] * s;
        }
    }

    public final void scaleAdd(double s, GVector v1, GVector v2) {
        if (v2.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector2"));
        }
        if (this.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector3"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = v1.values[i] * s + v2.values[i];
        }
    }

    public final void add(GVector vector) {
        if (this.length != vector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector4"));
        }
        for (int i = 0; i < this.length; ++i) {
            int n = i;
            this.values[n] = this.values[n] + vector.values[i];
        }
    }

    public final void add(GVector vector1, GVector vector2) {
        if (vector1.length != vector2.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector5"));
        }
        if (this.length != vector1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector6"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = vector1.values[i] + vector2.values[i];
        }
    }

    public final void sub(GVector vector) {
        if (this.length != vector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector7"));
        }
        for (int i = 0; i < this.length; ++i) {
            int n = i;
            this.values[n] = this.values[n] - vector.values[i];
        }
    }

    public final void sub(GVector vector1, GVector vector2) {
        if (vector1.length != vector2.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector8"));
        }
        if (this.length != vector1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector9"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = vector1.values[i] - vector2.values[i];
        }
    }

    public final void mul(GMatrix m1, GVector v1) {
        if (m1.getNumCol() != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector10"));
        }
        if (this.length != m1.getNumRow()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector11"));
        }
        double[] v = v1 != this ? v1.values : (double[])this.values.clone();
        for (int j = this.length - 1; j >= 0; --j) {
            this.values[j] = 0.0;
            for (int i = v1.length - 1; i >= 0; --i) {
                int n = j;
                this.values[n] = this.values[n] + m1.values[j][i] * v[i];
            }
        }
    }

    public final void mul(GVector v1, GMatrix m1) {
        if (m1.getNumRow() != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector12"));
        }
        if (this.length != m1.getNumCol()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector13"));
        }
        double[] v = v1 != this ? v1.values : (double[])this.values.clone();
        for (int j = this.length - 1; j >= 0; --j) {
            this.values[j] = 0.0;
            for (int i = v1.length - 1; i >= 0; --i) {
                int n = j;
                this.values[n] = this.values[n] + m1.values[i][j] * v[i];
            }
        }
    }

    public final void negate() {
        int i = this.length - 1;
        while (i >= 0) {
            int n = i--;
            this.values[n] = this.values[n] * -1.0;
        }
    }

    public final void zero() {
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public final void setSize(int length) {
        double[] tmp = new double[length];
        int max = this.length < length ? this.length : length;
        for (int i = 0; i < max; ++i) {
            tmp[i] = this.values[i];
        }
        this.length = length;
        this.values = tmp;
    }

    public final void set(double[] vector) {
        for (int i = this.length - 1; i >= 0; --i) {
            this.values[i] = vector[i];
        }
    }

    public final void set(GVector vector) {
        if (this.length < vector.length) {
            this.length = vector.length;
            this.values = new double[this.length];
            for (int i = 0; i < this.length; ++i) {
                this.values[i] = vector.values[i];
            }
        } else {
            int i;
            for (i = 0; i < vector.length; ++i) {
                this.values[i] = vector.values[i];
            }
            for (i = vector.length; i < this.length; ++i) {
                this.values[i] = 0.0;
            }
        }
    }

    public final void set(Tuple2f tuple) {
        if (this.length < 2) {
            this.length = 2;
            this.values = new double[2];
        }
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        for (int i = 2; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public final void set(Tuple3f tuple) {
        if (this.length < 3) {
            this.length = 3;
            this.values = new double[3];
        }
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        for (int i = 3; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public final void set(Tuple3d tuple) {
        if (this.length < 3) {
            this.length = 3;
            this.values = new double[3];
        }
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        for (int i = 3; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public final void set(Tuple4f tuple) {
        if (this.length < 4) {
            this.length = 4;
            this.values = new double[4];
        }
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        this.values[3] = tuple.w;
        for (int i = 4; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public final void set(Tuple4d tuple) {
        if (this.length < 4) {
            this.length = 4;
            this.values = new double[4];
        }
        this.values[0] = tuple.x;
        this.values[1] = tuple.y;
        this.values[2] = tuple.z;
        this.values[3] = tuple.w;
        for (int i = 4; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }

    public final int getSize() {
        return this.values.length;
    }

    public final double getElement(int index) {
        return this.values[index];
    }

    public final void setElement(int index, double value) {
        this.values[index] = value;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(this.length * 8);
        for (int i = 0; i < this.length; ++i) {
            buffer.append(this.values[i]).append(" ");
        }
        return buffer.toString();
    }

    public int hashCode() {
        long bits = 1L;
        for (int i = 0; i < this.length; ++i) {
            bits = VecMathUtil.hashDoubleBits(bits, this.values[i]);
        }
        return VecMathUtil.hashFinish(bits);
    }

    public boolean equals(GVector vector1) {
        try {
            if (this.length != vector1.length) {
                return false;
            }
            for (int i = 0; i < this.length; ++i) {
                if (this.values[i] == vector1.values[i]) continue;
                return false;
            }
            return true;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object o1) {
        try {
            GVector v2 = (GVector)o1;
            if (this.length != v2.length) {
                return false;
            }
            for (int i = 0; i < this.length; ++i) {
                if (this.values[i] == v2.values[i]) continue;
                return false;
            }
            return true;
        }
        catch (ClassCastException e1) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean epsilonEquals(GVector v1, double epsilon) {
        if (this.length != v1.length) {
            return false;
        }
        for (int i = 0; i < this.length; ++i) {
            double diff = this.values[i] - v1.values[i];
            double d = diff < 0.0 ? -diff : diff;
            if (!(d > epsilon)) continue;
            return false;
        }
        return true;
    }

    public final double dot(GVector v1) {
        if (this.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector14"));
        }
        double result = 0.0;
        for (int i = 0; i < this.length; ++i) {
            result += this.values[i] * v1.values[i];
        }
        return result;
    }

    public final void SVDBackSolve(GMatrix U, GMatrix W, GMatrix V, GVector b) {
        if (U.nRow != b.getSize() || U.nRow != U.nCol || U.nRow != W.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector15"));
        }
        if (W.nCol != this.values.length || W.nCol != V.nCol || W.nCol != V.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector23"));
        }
        GMatrix tmp = new GMatrix(U.nRow, W.nCol);
        tmp.mul(U, V);
        tmp.mulTransposeRight(U, W);
        tmp.invert();
        this.mul(tmp, b);
    }

    public final void LUDBackSolve(GMatrix LU, GVector b, GVector permutation) {
        int i;
        int size = LU.nRow * LU.nCol;
        double[] temp = new double[size];
        double[] result = new double[size];
        int[] row_perm = new int[b.getSize()];
        if (LU.nRow != b.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector16"));
        }
        if (LU.nRow != permutation.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector24"));
        }
        if (LU.nRow != LU.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector25"));
        }
        for (i = 0; i < LU.nRow; ++i) {
            for (int j = 0; j < LU.nCol; ++j) {
                temp[i * LU.nCol + j] = LU.values[i][j];
            }
        }
        for (i = 0; i < size; ++i) {
            result[i] = 0.0;
        }
        for (i = 0; i < LU.nRow; ++i) {
            result[i * LU.nCol] = b.values[i];
        }
        for (i = 0; i < LU.nCol; ++i) {
            row_perm[i] = (int)permutation.values[i];
        }
        GMatrix.luBacksubstitution(LU.nRow, temp, row_perm, result);
        for (i = 0; i < LU.nRow; ++i) {
            this.values[i] = result[i * LU.nCol];
        }
    }

    public final double angle(GVector v1) {
        return Math.acos(this.dot(v1) / (this.norm() * v1.norm()));
    }

    public final void interpolate(GVector v1, GVector v2, float alpha) {
        this.interpolate(v1, v2, (double)alpha);
    }

    public final void interpolate(GVector v1, float alpha) {
        this.interpolate(v1, (double)alpha);
    }

    public final void interpolate(GVector v1, GVector v2, double alpha) {
        if (v2.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector20"));
        }
        if (this.length != v1.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector21"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = (1.0 - alpha) * v1.values[i] + alpha * v2.values[i];
        }
    }

    public final void interpolate(GVector v1, double alpha) {
        if (v1.length != this.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector22"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = (1.0 - alpha) * this.values[i] + alpha * v1.values[i];
        }
    }

    public Object clone() {
        GVector v1 = null;
        try {
            v1 = (GVector)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        v1.values = new double[this.length];
        for (int i = 0; i < this.length; ++i) {
            v1.values[i] = this.values[i];
        }
        return v1;
    }
}

