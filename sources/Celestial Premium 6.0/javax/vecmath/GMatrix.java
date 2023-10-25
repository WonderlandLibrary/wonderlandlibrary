/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.GVector;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.MismatchedSizeException;
import javax.vecmath.SingularMatrixException;
import javax.vecmath.VecMathI18N;
import javax.vecmath.VecMathUtil;

public class GMatrix
implements Serializable,
Cloneable {
    static final long serialVersionUID = 2777097312029690941L;
    private static final boolean debug = false;
    int nRow;
    int nCol;
    double[][] values;
    private static final double EPS = 1.0E-10;

    public GMatrix(int nRow, int nCol) {
        int i;
        this.values = new double[nRow][nCol];
        this.nRow = nRow;
        this.nCol = nCol;
        for (i = 0; i < nRow; ++i) {
            for (int j = 0; j < nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
        int l = nRow < nCol ? nRow : nCol;
        for (i = 0; i < l; ++i) {
            this.values[i][i] = 1.0;
        }
    }

    public GMatrix(int nRow, int nCol, double[] matrix) {
        this.values = new double[nRow][nCol];
        this.nRow = nRow;
        this.nCol = nCol;
        for (int i = 0; i < nRow; ++i) {
            for (int j = 0; j < nCol; ++j) {
                this.values[i][j] = matrix[i * nCol + j];
            }
        }
    }

    public GMatrix(GMatrix matrix) {
        this.nRow = matrix.nRow;
        this.nCol = matrix.nCol;
        this.values = new double[this.nRow][this.nCol];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = matrix.values[i][j];
            }
        }
    }

    public final void mul(GMatrix m1) {
        if (this.nCol != m1.nRow || this.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix0"));
        }
        double[][] tmp = new double[this.nRow][this.nCol];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                tmp[i][j] = 0.0;
                for (int k = 0; k < this.nCol; ++k) {
                    double[] arrd = tmp[i];
                    int n = j;
                    arrd[n] = arrd[n] + this.values[i][k] * m1.values[k][j];
                }
            }
        }
        this.values = tmp;
    }

    public final void mul(GMatrix m1, GMatrix m2) {
        if (m1.nCol != m2.nRow || this.nRow != m1.nRow || this.nCol != m2.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix1"));
        }
        double[][] tmp = new double[this.nRow][this.nCol];
        for (int i = 0; i < m1.nRow; ++i) {
            for (int j = 0; j < m2.nCol; ++j) {
                tmp[i][j] = 0.0;
                for (int k = 0; k < m1.nCol; ++k) {
                    double[] arrd = tmp[i];
                    int n = j;
                    arrd[n] = arrd[n] + m1.values[i][k] * m2.values[k][j];
                }
            }
        }
        this.values = tmp;
    }

    public final void mul(GVector v1, GVector v2) {
        if (this.nRow < v1.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix2"));
        }
        if (this.nCol < v2.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix3"));
        }
        for (int i = 0; i < v1.getSize(); ++i) {
            for (int j = 0; j < v2.getSize(); ++j) {
                this.values[i][j] = v1.values[i] * v2.values[j];
            }
        }
    }

    public final void add(GMatrix m1) {
        if (this.nRow != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix4"));
        }
        if (this.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix5"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = this.values[i][j] + m1.values[i][j];
            }
        }
    }

    public final void add(GMatrix m1, GMatrix m2) {
        if (m2.nRow != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix6"));
        }
        if (m2.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix7"));
        }
        if (this.nCol != m1.nCol || this.nRow != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix8"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = m1.values[i][j] + m2.values[i][j];
            }
        }
    }

    public final void sub(GMatrix m1) {
        if (this.nRow != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix9"));
        }
        if (this.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix28"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = this.values[i][j] - m1.values[i][j];
            }
        }
    }

    public final void sub(GMatrix m1, GMatrix m2) {
        if (m2.nRow != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix10"));
        }
        if (m2.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix11"));
        }
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix12"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = m1.values[i][j] - m2.values[i][j];
            }
        }
    }

    public final void negate() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = -this.values[i][j];
            }
        }
    }

    public final void negate(GMatrix m1) {
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix13"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = -m1.values[i][j];
            }
        }
    }

    public final void setIdentity() {
        int i;
        for (i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
        int l = this.nRow < this.nCol ? this.nRow : this.nCol;
        for (i = 0; i < l; ++i) {
            this.values[i][i] = 1.0;
        }
    }

    public final void setZero() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public final void identityMinus() {
        int i;
        for (i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = -this.values[i][j];
            }
        }
        int l = this.nRow < this.nCol ? this.nRow : this.nCol;
        i = 0;
        while (i < l) {
            double[] arrd = this.values[i];
            int n = i++;
            arrd[n] = arrd[n] + 1.0;
        }
    }

    public final void invert() {
        this.invertGeneral(this);
    }

    public final void invert(GMatrix m1) {
        this.invertGeneral(m1);
    }

    public final void copySubMatrix(int rowSource, int colSource, int numRow, int numCol, int rowDest, int colDest, GMatrix target) {
        if (this != target) {
            for (int i = 0; i < numRow; ++i) {
                for (int j = 0; j < numCol; ++j) {
                    target.values[rowDest + i][colDest + j] = this.values[rowSource + i][colSource + j];
                }
            }
        } else {
            int j;
            int i;
            double[][] tmp = new double[numRow][numCol];
            for (i = 0; i < numRow; ++i) {
                for (j = 0; j < numCol; ++j) {
                    tmp[i][j] = this.values[rowSource + i][colSource + j];
                }
            }
            for (i = 0; i < numRow; ++i) {
                for (j = 0; j < numCol; ++j) {
                    target.values[rowDest + i][colDest + j] = tmp[i][j];
                }
            }
        }
    }

    public final void setSize(int nRow, int nCol) {
        double[][] tmp = new double[nRow][nCol];
        int maxRow = this.nRow < nRow ? this.nRow : nRow;
        int maxCol = this.nCol < nCol ? this.nCol : nCol;
        for (int i = 0; i < maxRow; ++i) {
            for (int j = 0; j < maxCol; ++j) {
                tmp[i][j] = this.values[i][j];
            }
        }
        this.nRow = nRow;
        this.nCol = nCol;
        this.values = tmp;
    }

    public final void set(double[] matrix) {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = matrix[this.nCol * i + j];
            }
        }
    }

    public final void set(Matrix3f m1) {
        if (this.nCol < 3 || this.nRow < 3) {
            this.nCol = 3;
            this.nRow = 3;
            this.values = new double[this.nRow][this.nCol];
        }
        this.values[0][0] = m1.m00;
        this.values[0][1] = m1.m01;
        this.values[0][2] = m1.m02;
        this.values[1][0] = m1.m10;
        this.values[1][1] = m1.m11;
        this.values[1][2] = m1.m12;
        this.values[2][0] = m1.m20;
        this.values[2][1] = m1.m21;
        this.values[2][2] = m1.m22;
        for (int i = 3; i < this.nRow; ++i) {
            for (int j = 3; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public final void set(Matrix3d m1) {
        if (this.nRow < 3 || this.nCol < 3) {
            this.values = new double[3][3];
            this.nRow = 3;
            this.nCol = 3;
        }
        this.values[0][0] = m1.m00;
        this.values[0][1] = m1.m01;
        this.values[0][2] = m1.m02;
        this.values[1][0] = m1.m10;
        this.values[1][1] = m1.m11;
        this.values[1][2] = m1.m12;
        this.values[2][0] = m1.m20;
        this.values[2][1] = m1.m21;
        this.values[2][2] = m1.m22;
        for (int i = 3; i < this.nRow; ++i) {
            for (int j = 3; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public final void set(Matrix4f m1) {
        if (this.nRow < 4 || this.nCol < 4) {
            this.values = new double[4][4];
            this.nRow = 4;
            this.nCol = 4;
        }
        this.values[0][0] = m1.m00;
        this.values[0][1] = m1.m01;
        this.values[0][2] = m1.m02;
        this.values[0][3] = m1.m03;
        this.values[1][0] = m1.m10;
        this.values[1][1] = m1.m11;
        this.values[1][2] = m1.m12;
        this.values[1][3] = m1.m13;
        this.values[2][0] = m1.m20;
        this.values[2][1] = m1.m21;
        this.values[2][2] = m1.m22;
        this.values[2][3] = m1.m23;
        this.values[3][0] = m1.m30;
        this.values[3][1] = m1.m31;
        this.values[3][2] = m1.m32;
        this.values[3][3] = m1.m33;
        for (int i = 4; i < this.nRow; ++i) {
            for (int j = 4; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public final void set(Matrix4d m1) {
        if (this.nRow < 4 || this.nCol < 4) {
            this.values = new double[4][4];
            this.nRow = 4;
            this.nCol = 4;
        }
        this.values[0][0] = m1.m00;
        this.values[0][1] = m1.m01;
        this.values[0][2] = m1.m02;
        this.values[0][3] = m1.m03;
        this.values[1][0] = m1.m10;
        this.values[1][1] = m1.m11;
        this.values[1][2] = m1.m12;
        this.values[1][3] = m1.m13;
        this.values[2][0] = m1.m20;
        this.values[2][1] = m1.m21;
        this.values[2][2] = m1.m22;
        this.values[2][3] = m1.m23;
        this.values[3][0] = m1.m30;
        this.values[3][1] = m1.m31;
        this.values[3][2] = m1.m32;
        this.values[3][3] = m1.m33;
        for (int i = 4; i < this.nRow; ++i) {
            for (int j = 4; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public final void set(GMatrix m1) {
        int j;
        int i;
        if (this.nRow < m1.nRow || this.nCol < m1.nCol) {
            this.nRow = m1.nRow;
            this.nCol = m1.nCol;
            this.values = new double[this.nRow][this.nCol];
        }
        for (i = 0; i < Math.min(this.nRow, m1.nRow); ++i) {
            for (j = 0; j < Math.min(this.nCol, m1.nCol); ++j) {
                this.values[i][j] = m1.values[i][j];
            }
        }
        for (i = m1.nRow; i < this.nRow; ++i) {
            for (j = m1.nCol; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }

    public final int getNumRow() {
        return this.nRow;
    }

    public final int getNumCol() {
        return this.nCol;
    }

    public final double getElement(int row, int column) {
        return this.values[row][column];
    }

    public final void setElement(int row, int column, double value) {
        this.values[row][column] = value;
    }

    public final void getRow(int row, double[] array) {
        for (int i = 0; i < this.nCol; ++i) {
            array[i] = this.values[row][i];
        }
    }

    public final void getRow(int row, GVector vector) {
        if (vector.getSize() < this.nCol) {
            vector.setSize(this.nCol);
        }
        for (int i = 0; i < this.nCol; ++i) {
            vector.values[i] = this.values[row][i];
        }
    }

    public final void getColumn(int col, double[] array) {
        for (int i = 0; i < this.nRow; ++i) {
            array[i] = this.values[i][col];
        }
    }

    public final void getColumn(int col, GVector vector) {
        if (vector.getSize() < this.nRow) {
            vector.setSize(this.nRow);
        }
        for (int i = 0; i < this.nRow; ++i) {
            vector.values[i] = this.values[i][col];
        }
    }

    public final void get(Matrix3d m1) {
        if (this.nRow < 3 || this.nCol < 3) {
            m1.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    m1.m00 = this.values[0][0];
                    if (this.nRow > 1) {
                        m1.m10 = this.values[1][0];
                        if (this.nRow > 2) {
                            m1.m20 = this.values[2][0];
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        m1.m01 = this.values[0][1];
                        if (this.nRow > 1) {
                            m1.m11 = this.values[1][1];
                            if (this.nRow > 2) {
                                m1.m21 = this.values[2][1];
                            }
                        }
                    }
                    if (this.nCol > 2 && this.nRow > 0) {
                        m1.m02 = this.values[0][2];
                        if (this.nRow > 1) {
                            m1.m12 = this.values[1][2];
                            if (this.nRow > 2) {
                                m1.m22 = this.values[2][2];
                            }
                        }
                    }
                }
            }
        } else {
            m1.m00 = this.values[0][0];
            m1.m01 = this.values[0][1];
            m1.m02 = this.values[0][2];
            m1.m10 = this.values[1][0];
            m1.m11 = this.values[1][1];
            m1.m12 = this.values[1][2];
            m1.m20 = this.values[2][0];
            m1.m21 = this.values[2][1];
            m1.m22 = this.values[2][2];
        }
    }

    public final void get(Matrix3f m1) {
        if (this.nRow < 3 || this.nCol < 3) {
            m1.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    m1.m00 = (float)this.values[0][0];
                    if (this.nRow > 1) {
                        m1.m10 = (float)this.values[1][0];
                        if (this.nRow > 2) {
                            m1.m20 = (float)this.values[2][0];
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        m1.m01 = (float)this.values[0][1];
                        if (this.nRow > 1) {
                            m1.m11 = (float)this.values[1][1];
                            if (this.nRow > 2) {
                                m1.m21 = (float)this.values[2][1];
                            }
                        }
                    }
                    if (this.nCol > 2 && this.nRow > 0) {
                        m1.m02 = (float)this.values[0][2];
                        if (this.nRow > 1) {
                            m1.m12 = (float)this.values[1][2];
                            if (this.nRow > 2) {
                                m1.m22 = (float)this.values[2][2];
                            }
                        }
                    }
                }
            }
        } else {
            m1.m00 = (float)this.values[0][0];
            m1.m01 = (float)this.values[0][1];
            m1.m02 = (float)this.values[0][2];
            m1.m10 = (float)this.values[1][0];
            m1.m11 = (float)this.values[1][1];
            m1.m12 = (float)this.values[1][2];
            m1.m20 = (float)this.values[2][0];
            m1.m21 = (float)this.values[2][1];
            m1.m22 = (float)this.values[2][2];
        }
    }

    public final void get(Matrix4d m1) {
        if (this.nRow < 4 || this.nCol < 4) {
            m1.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    m1.m00 = this.values[0][0];
                    if (this.nRow > 1) {
                        m1.m10 = this.values[1][0];
                        if (this.nRow > 2) {
                            m1.m20 = this.values[2][0];
                            if (this.nRow > 3) {
                                m1.m30 = this.values[3][0];
                            }
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        m1.m01 = this.values[0][1];
                        if (this.nRow > 1) {
                            m1.m11 = this.values[1][1];
                            if (this.nRow > 2) {
                                m1.m21 = this.values[2][1];
                                if (this.nRow > 3) {
                                    m1.m31 = this.values[3][1];
                                }
                            }
                        }
                    }
                    if (this.nCol > 2) {
                        if (this.nRow > 0) {
                            m1.m02 = this.values[0][2];
                            if (this.nRow > 1) {
                                m1.m12 = this.values[1][2];
                                if (this.nRow > 2) {
                                    m1.m22 = this.values[2][2];
                                    if (this.nRow > 3) {
                                        m1.m32 = this.values[3][2];
                                    }
                                }
                            }
                        }
                        if (this.nCol > 3 && this.nRow > 0) {
                            m1.m03 = this.values[0][3];
                            if (this.nRow > 1) {
                                m1.m13 = this.values[1][3];
                                if (this.nRow > 2) {
                                    m1.m23 = this.values[2][3];
                                    if (this.nRow > 3) {
                                        m1.m33 = this.values[3][3];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            m1.m00 = this.values[0][0];
            m1.m01 = this.values[0][1];
            m1.m02 = this.values[0][2];
            m1.m03 = this.values[0][3];
            m1.m10 = this.values[1][0];
            m1.m11 = this.values[1][1];
            m1.m12 = this.values[1][2];
            m1.m13 = this.values[1][3];
            m1.m20 = this.values[2][0];
            m1.m21 = this.values[2][1];
            m1.m22 = this.values[2][2];
            m1.m23 = this.values[2][3];
            m1.m30 = this.values[3][0];
            m1.m31 = this.values[3][1];
            m1.m32 = this.values[3][2];
            m1.m33 = this.values[3][3];
        }
    }

    public final void get(Matrix4f m1) {
        if (this.nRow < 4 || this.nCol < 4) {
            m1.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    m1.m00 = (float)this.values[0][0];
                    if (this.nRow > 1) {
                        m1.m10 = (float)this.values[1][0];
                        if (this.nRow > 2) {
                            m1.m20 = (float)this.values[2][0];
                            if (this.nRow > 3) {
                                m1.m30 = (float)this.values[3][0];
                            }
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        m1.m01 = (float)this.values[0][1];
                        if (this.nRow > 1) {
                            m1.m11 = (float)this.values[1][1];
                            if (this.nRow > 2) {
                                m1.m21 = (float)this.values[2][1];
                                if (this.nRow > 3) {
                                    m1.m31 = (float)this.values[3][1];
                                }
                            }
                        }
                    }
                    if (this.nCol > 2) {
                        if (this.nRow > 0) {
                            m1.m02 = (float)this.values[0][2];
                            if (this.nRow > 1) {
                                m1.m12 = (float)this.values[1][2];
                                if (this.nRow > 2) {
                                    m1.m22 = (float)this.values[2][2];
                                    if (this.nRow > 3) {
                                        m1.m32 = (float)this.values[3][2];
                                    }
                                }
                            }
                        }
                        if (this.nCol > 3 && this.nRow > 0) {
                            m1.m03 = (float)this.values[0][3];
                            if (this.nRow > 1) {
                                m1.m13 = (float)this.values[1][3];
                                if (this.nRow > 2) {
                                    m1.m23 = (float)this.values[2][3];
                                    if (this.nRow > 3) {
                                        m1.m33 = (float)this.values[3][3];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            m1.m00 = (float)this.values[0][0];
            m1.m01 = (float)this.values[0][1];
            m1.m02 = (float)this.values[0][2];
            m1.m03 = (float)this.values[0][3];
            m1.m10 = (float)this.values[1][0];
            m1.m11 = (float)this.values[1][1];
            m1.m12 = (float)this.values[1][2];
            m1.m13 = (float)this.values[1][3];
            m1.m20 = (float)this.values[2][0];
            m1.m21 = (float)this.values[2][1];
            m1.m22 = (float)this.values[2][2];
            m1.m23 = (float)this.values[2][3];
            m1.m30 = (float)this.values[3][0];
            m1.m31 = (float)this.values[3][1];
            m1.m32 = (float)this.values[3][2];
            m1.m33 = (float)this.values[3][3];
        }
    }

    public final void get(GMatrix m1) {
        int j;
        int i;
        int nc = this.nCol < m1.nCol ? this.nCol : m1.nCol;
        int nr = this.nRow < m1.nRow ? this.nRow : m1.nRow;
        for (i = 0; i < nr; ++i) {
            for (j = 0; j < nc; ++j) {
                m1.values[i][j] = this.values[i][j];
            }
        }
        for (i = nr; i < m1.nRow; ++i) {
            for (j = 0; j < m1.nCol; ++j) {
                m1.values[i][j] = 0.0;
            }
        }
        for (j = nc; j < m1.nCol; ++j) {
            for (i = 0; i < nr; ++i) {
                m1.values[i][j] = 0.0;
            }
        }
    }

    public final void setRow(int row, double[] array) {
        for (int i = 0; i < this.nCol; ++i) {
            this.values[row][i] = array[i];
        }
    }

    public final void setRow(int row, GVector vector) {
        for (int i = 0; i < this.nCol; ++i) {
            this.values[row][i] = vector.values[i];
        }
    }

    public final void setColumn(int col, double[] array) {
        for (int i = 0; i < this.nRow; ++i) {
            this.values[i][col] = array[i];
        }
    }

    public final void setColumn(int col, GVector vector) {
        for (int i = 0; i < this.nRow; ++i) {
            this.values[i][col] = vector.values[i];
        }
    }

    public final void mulTransposeBoth(GMatrix m1, GMatrix m2) {
        if (m1.nRow != m2.nCol || this.nRow != m1.nCol || this.nCol != m2.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix14"));
        }
        if (m1 == this || m2 == this) {
            double[][] tmp = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    tmp[i][j] = 0.0;
                    for (int k = 0; k < m1.nRow; ++k) {
                        double[] arrd = tmp[i];
                        int n = j;
                        arrd[n] = arrd[n] + m1.values[k][i] * m2.values[j][k];
                    }
                }
            }
            this.values = tmp;
        } else {
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    this.values[i][j] = 0.0;
                    for (int k = 0; k < m1.nRow; ++k) {
                        double[] arrd = this.values[i];
                        int n = j;
                        arrd[n] = arrd[n] + m1.values[k][i] * m2.values[j][k];
                    }
                }
            }
        }
    }

    public final void mulTransposeRight(GMatrix m1, GMatrix m2) {
        if (m1.nCol != m2.nCol || this.nCol != m2.nRow || this.nRow != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix15"));
        }
        if (m1 == this || m2 == this) {
            double[][] tmp = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    tmp[i][j] = 0.0;
                    for (int k = 0; k < m1.nCol; ++k) {
                        double[] arrd = tmp[i];
                        int n = j;
                        arrd[n] = arrd[n] + m1.values[i][k] * m2.values[j][k];
                    }
                }
            }
            this.values = tmp;
        } else {
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    this.values[i][j] = 0.0;
                    for (int k = 0; k < m1.nCol; ++k) {
                        double[] arrd = this.values[i];
                        int n = j;
                        arrd[n] = arrd[n] + m1.values[i][k] * m2.values[j][k];
                    }
                }
            }
        }
    }

    public final void mulTransposeLeft(GMatrix m1, GMatrix m2) {
        if (m1.nRow != m2.nRow || this.nCol != m2.nCol || this.nRow != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix16"));
        }
        if (m1 == this || m2 == this) {
            double[][] tmp = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    tmp[i][j] = 0.0;
                    for (int k = 0; k < m1.nRow; ++k) {
                        double[] arrd = tmp[i];
                        int n = j;
                        arrd[n] = arrd[n] + m1.values[k][i] * m2.values[k][j];
                    }
                }
            }
            this.values = tmp;
        } else {
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    this.values[i][j] = 0.0;
                    for (int k = 0; k < m1.nRow; ++k) {
                        double[] arrd = this.values[i];
                        int n = j;
                        arrd[n] = arrd[n] + m1.values[k][i] * m2.values[k][j];
                    }
                }
            }
        }
    }

    public final void transpose() {
        if (this.nRow != this.nCol) {
            int i = this.nRow;
            this.nRow = this.nCol;
            this.nCol = i;
            double[][] tmp = new double[this.nRow][this.nCol];
            for (i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    tmp[i][j] = this.values[j][i];
                }
            }
            this.values = tmp;
        } else {
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < i; ++j) {
                    double swap = this.values[i][j];
                    this.values[i][j] = this.values[j][i];
                    this.values[j][i] = swap;
                }
            }
        }
    }

    public final void transpose(GMatrix m1) {
        if (this.nRow != m1.nCol || this.nCol != m1.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix17"));
        }
        if (m1 != this) {
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    this.values[i][j] = m1.values[j][i];
                }
            }
        } else {
            this.transpose();
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(this.nRow * this.nCol * 8);
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                buffer.append(this.values[i][j]).append(" ");
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }

    private static void checkMatrix(GMatrix m) {
        for (int i = 0; i < m.nRow; ++i) {
            for (int j = 0; j < m.nCol; ++j) {
                if (Math.abs(m.values[i][j]) < 1.0E-10) {
                    System.out.print(" 0.0     ");
                    continue;
                }
                System.out.print(" " + m.values[i][j]);
            }
            System.out.print("\n");
        }
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashLongBits(bits, this.nRow);
        bits = VecMathUtil.hashLongBits(bits, this.nCol);
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                bits = VecMathUtil.hashDoubleBits(bits, this.values[i][j]);
            }
        }
        return VecMathUtil.hashFinish(bits);
    }

    public boolean equals(GMatrix m1) {
        try {
            if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
                return false;
            }
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    if (this.values[i][j] == m1.values[i][j]) continue;
                    return false;
                }
            }
            return true;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object o1) {
        try {
            GMatrix m2 = (GMatrix)o1;
            if (this.nRow != m2.nRow || this.nCol != m2.nCol) {
                return false;
            }
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    if (this.values[i][j] == m2.values[i][j]) continue;
                    return false;
                }
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

    public boolean epsilonEquals(GMatrix m1, float epsilon) {
        return this.epsilonEquals(m1, (double)epsilon);
    }

    public boolean epsilonEquals(GMatrix m1, double epsilon) {
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            return false;
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                double diff = this.values[i][j] - m1.values[i][j];
                double d = diff < 0.0 ? -diff : diff;
                if (!(d > epsilon)) continue;
                return false;
            }
        }
        return true;
    }

    public final double trace() {
        int l = this.nRow < this.nCol ? this.nRow : this.nCol;
        double t = 0.0;
        for (int i = 0; i < l; ++i) {
            t += this.values[i][i];
        }
        return t;
    }

    public final int SVD(GMatrix U, GMatrix W, GMatrix V) {
        if (this.nCol != V.nCol || this.nCol != V.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix18"));
        }
        if (this.nRow != U.nRow || this.nRow != U.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix25"));
        }
        if (this.nRow != W.nRow || this.nCol != W.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix26"));
        }
        if (this.nRow == 2 && this.nCol == 2 && this.values[1][0] == 0.0) {
            U.setIdentity();
            V.setIdentity();
            if (this.values[0][1] == 0.0) {
                return 2;
            }
            double[] sinl = new double[1];
            double[] sinr = new double[1];
            double[] cosl = new double[1];
            double[] cosr = new double[1];
            double[] single_values = new double[]{this.values[0][0], this.values[1][1]};
            GMatrix.compute_2X2(this.values[0][0], this.values[0][1], this.values[1][1], single_values, sinl, cosl, sinr, cosr, 0);
            GMatrix.update_u(0, U, cosl, sinl);
            GMatrix.update_v(0, V, cosr, sinr);
            return 2;
        }
        return GMatrix.computeSVD(this, U, W, V);
    }

    public final int LUD(GMatrix LU, GVector permutation) {
        int j;
        int i;
        int size = LU.nRow * LU.nCol;
        double[] temp = new double[size];
        int[] even_row_exchange = new int[1];
        int[] row_perm = new int[LU.nRow];
        if (this.nRow != this.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix19"));
        }
        if (this.nRow != LU.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix27"));
        }
        if (this.nCol != LU.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix27"));
        }
        if (LU.nRow != permutation.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix20"));
        }
        for (i = 0; i < this.nRow; ++i) {
            for (j = 0; j < this.nCol; ++j) {
                temp[i * this.nCol + j] = this.values[i][j];
            }
        }
        if (!GMatrix.luDecomposition(LU.nRow, temp, row_perm, even_row_exchange)) {
            throw new SingularMatrixException(VecMathI18N.getString("GMatrix21"));
        }
        for (i = 0; i < this.nRow; ++i) {
            for (j = 0; j < this.nCol; ++j) {
                LU.values[i][j] = temp[i * this.nCol + j];
            }
        }
        for (i = 0; i < LU.nRow; ++i) {
            permutation.values[i] = row_perm[i];
        }
        return even_row_exchange[0];
    }

    public final void setScale(double scale) {
        int i;
        int l = this.nRow < this.nCol ? this.nRow : this.nCol;
        for (i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
        for (i = 0; i < l; ++i) {
            this.values[i][i] = scale;
        }
    }

    final void invertGeneral(GMatrix m1) {
        int j;
        int i;
        int size = m1.nRow * m1.nCol;
        double[] temp = new double[size];
        double[] result = new double[size];
        int[] row_perm = new int[m1.nRow];
        int[] even_row_exchange = new int[1];
        if (m1.nRow != m1.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix22"));
        }
        for (i = 0; i < this.nRow; ++i) {
            for (j = 0; j < this.nCol; ++j) {
                temp[i * this.nCol + j] = m1.values[i][j];
            }
        }
        if (!GMatrix.luDecomposition(m1.nRow, temp, row_perm, even_row_exchange)) {
            throw new SingularMatrixException(VecMathI18N.getString("GMatrix21"));
        }
        for (i = 0; i < size; ++i) {
            result[i] = 0.0;
        }
        for (i = 0; i < this.nCol; ++i) {
            result[i + i * this.nCol] = 1.0;
        }
        GMatrix.luBacksubstitution(m1.nRow, temp, row_perm, result);
        for (i = 0; i < this.nRow; ++i) {
            for (j = 0; j < this.nCol; ++j) {
                this.values[i][j] = result[i * this.nCol + j];
            }
        }
    }

    static boolean luDecomposition(int dim, double[] matrix0, int[] row_perm, int[] even_row_xchg) {
        double temp;
        int j;
        double big;
        double[] row_scale = new double[dim];
        int ptr = 0;
        int rs = 0;
        even_row_xchg[0] = 1;
        int i = dim;
        while (i-- != 0) {
            big = 0.0;
            j = dim;
            while (j-- != 0) {
                temp = matrix0[ptr++];
                if (!((temp = Math.abs(temp)) > big)) continue;
                big = temp;
            }
            if (big == 0.0) {
                return false;
            }
            row_scale[rs++] = 1.0 / big;
        }
        int mtx = 0;
        for (j = 0; j < dim; ++j) {
            int p2;
            int p1;
            int k;
            double sum;
            int target;
            for (i = 0; i < j; ++i) {
                target = mtx + dim * i + j;
                sum = matrix0[target];
                k = i;
                p1 = mtx + dim * i;
                p2 = mtx + j;
                while (k-- != 0) {
                    sum -= matrix0[p1] * matrix0[p2];
                    ++p1;
                    p2 += dim;
                }
                matrix0[target] = sum;
            }
            big = 0.0;
            int imax = -1;
            for (i = j; i < dim; ++i) {
                double d;
                target = mtx + dim * i + j;
                sum = matrix0[target];
                k = j;
                p1 = mtx + dim * i;
                p2 = mtx + j;
                while (k-- != 0) {
                    sum -= matrix0[p1] * matrix0[p2];
                    ++p1;
                    p2 += dim;
                }
                matrix0[target] = sum;
                temp = row_scale[i] * Math.abs(sum);
                if (!(d >= big)) continue;
                big = temp;
                imax = i;
            }
            if (imax < 0) {
                throw new RuntimeException(VecMathI18N.getString("GMatrix24"));
            }
            if (j != imax) {
                k = dim;
                p1 = mtx + dim * imax;
                p2 = mtx + dim * j;
                while (k-- != 0) {
                    temp = matrix0[p1];
                    matrix0[p1++] = matrix0[p2];
                    matrix0[p2++] = temp;
                }
                row_scale[imax] = row_scale[j];
                even_row_xchg[0] = -even_row_xchg[0];
            }
            row_perm[j] = imax;
            if (matrix0[mtx + dim * j + j] == 0.0) {
                return false;
            }
            if (j == dim - 1) continue;
            temp = 1.0 / matrix0[mtx + dim * j + j];
            target = mtx + dim * (j + 1) + j;
            i = dim - 1 - j;
            while (i-- != 0) {
                int n = target;
                matrix0[n] = matrix0[n] * temp;
                target += dim;
            }
        }
        return true;
    }

    static void luBacksubstitution(int dim, double[] matrix1, int[] row_perm, double[] matrix2) {
        int rp = 0;
        for (int k = 0; k < dim; ++k) {
            int j;
            int rv;
            int i;
            int cv = k;
            int ii = -1;
            for (i = 0; i < dim; ++i) {
                int ip = row_perm[rp + i];
                double sum = matrix2[cv + dim * ip];
                matrix2[cv + dim * ip] = matrix2[cv + dim * i];
                if (ii >= 0) {
                    rv = i * dim;
                    for (j = ii; j <= i - 1; ++j) {
                        sum -= matrix1[rv + j] * matrix2[cv + dim * j];
                    }
                } else if (sum != 0.0) {
                    ii = i;
                }
                matrix2[cv + dim * i] = sum;
            }
            for (i = 0; i < dim; ++i) {
                int ri = dim - 1 - i;
                rv = dim * ri;
                double tt = 0.0;
                for (j = 1; j <= i; ++j) {
                    tt += matrix1[rv + dim - j] * matrix2[cv + dim * (dim - j)];
                }
                matrix2[cv + dim * ri] = (matrix2[cv + dim * ri] - tt) / matrix1[rv + ri];
            }
        }
    }

    static int computeSVD(GMatrix mat, GMatrix U, GMatrix W, GMatrix V) {
        int i;
        int eLength;
        int sLength;
        GMatrix tmp = new GMatrix(mat.nRow, mat.nCol);
        GMatrix u = new GMatrix(mat.nRow, mat.nCol);
        GMatrix v = new GMatrix(mat.nRow, mat.nCol);
        GMatrix m = new GMatrix(mat);
        if (m.nRow >= m.nCol) {
            sLength = m.nCol;
            eLength = m.nCol - 1;
        } else {
            sLength = m.nRow;
            eLength = m.nRow;
        }
        int vecLength = m.nRow > m.nCol ? m.nRow : m.nCol;
        double[] vec = new double[vecLength];
        double[] single_values = new double[sLength];
        double[] e = new double[eLength];
        int rank = 0;
        U.setIdentity();
        V.setIdentity();
        int nr = m.nRow;
        int nc = m.nCol;
        for (int si = 0; si < sLength; ++si) {
            double t;
            int k;
            int j;
            double scale;
            double mag;
            if (nr > 1) {
                mag = 0.0;
                for (i = 0; i < nr; ++i) {
                    mag += m.values[i + si][si] * m.values[i + si][si];
                }
                mag = Math.sqrt(mag);
                vec[0] = m.values[si][si] == 0.0 ? mag : m.values[si][si] + GMatrix.d_sign(mag, m.values[si][si]);
                for (i = 1; i < nr; ++i) {
                    vec[i] = m.values[si + i][si];
                }
                scale = 0.0;
                for (i = 0; i < nr; ++i) {
                    scale += vec[i] * vec[i];
                }
                scale = 2.0 / scale;
                for (j = si; j < m.nRow; ++j) {
                    for (k = si; k < m.nRow; ++k) {
                        u.values[j][k] = -scale * vec[j - si] * vec[k - si];
                    }
                }
                i = si;
                while (i < m.nRow) {
                    double[] arrd = u.values[i];
                    int n = i++;
                    arrd[n] = arrd[n] + 1.0;
                }
                t = 0.0;
                for (i = si; i < m.nRow; ++i) {
                    t += u.values[si][i] * m.values[i][si];
                }
                m.values[si][si] = t;
                for (j = si; j < m.nRow; ++j) {
                    for (k = si + 1; k < m.nCol; ++k) {
                        tmp.values[j][k] = 0.0;
                        for (i = si; i < m.nCol; ++i) {
                            double[] arrd = tmp.values[j];
                            int n = k;
                            arrd[n] = arrd[n] + u.values[j][i] * m.values[i][k];
                        }
                    }
                }
                for (j = si; j < m.nRow; ++j) {
                    for (k = si + 1; k < m.nCol; ++k) {
                        m.values[j][k] = tmp.values[j][k];
                    }
                }
                for (j = si; j < m.nRow; ++j) {
                    for (k = 0; k < m.nCol; ++k) {
                        tmp.values[j][k] = 0.0;
                        for (i = si; i < m.nCol; ++i) {
                            double[] arrd = tmp.values[j];
                            int n = k;
                            arrd[n] = arrd[n] + u.values[j][i] * U.values[i][k];
                        }
                    }
                }
                for (j = si; j < m.nRow; ++j) {
                    for (k = 0; k < m.nCol; ++k) {
                        U.values[j][k] = tmp.values[j][k];
                    }
                }
                --nr;
            }
            if (nc <= 2) continue;
            mag = 0.0;
            for (i = 1; i < nc; ++i) {
                mag += m.values[si][si + i] * m.values[si][si + i];
            }
            mag = Math.sqrt(mag);
            vec[0] = m.values[si][si + 1] == 0.0 ? mag : m.values[si][si + 1] + GMatrix.d_sign(mag, m.values[si][si + 1]);
            for (i = 1; i < nc - 1; ++i) {
                vec[i] = m.values[si][si + i + 1];
            }
            scale = 0.0;
            for (i = 0; i < nc - 1; ++i) {
                scale += vec[i] * vec[i];
            }
            scale = 2.0 / scale;
            for (j = si + 1; j < nc; ++j) {
                for (k = si + 1; k < m.nCol; ++k) {
                    v.values[j][k] = -scale * vec[j - si - 1] * vec[k - si - 1];
                }
            }
            i = si + 1;
            while (i < m.nCol) {
                double[] arrd = v.values[i];
                int n = i++;
                arrd[n] = arrd[n] + 1.0;
            }
            t = 0.0;
            for (i = si; i < m.nCol; ++i) {
                t += v.values[i][si + 1] * m.values[si][i];
            }
            m.values[si][si + 1] = t;
            for (j = si + 1; j < m.nRow; ++j) {
                for (k = si + 1; k < m.nCol; ++k) {
                    tmp.values[j][k] = 0.0;
                    for (i = si + 1; i < m.nCol; ++i) {
                        double[] arrd = tmp.values[j];
                        int n = k;
                        arrd[n] = arrd[n] + v.values[i][k] * m.values[j][i];
                    }
                }
            }
            for (j = si + 1; j < m.nRow; ++j) {
                for (k = si + 1; k < m.nCol; ++k) {
                    m.values[j][k] = tmp.values[j][k];
                }
            }
            for (j = 0; j < m.nRow; ++j) {
                for (k = si + 1; k < m.nCol; ++k) {
                    tmp.values[j][k] = 0.0;
                    for (i = si + 1; i < m.nCol; ++i) {
                        double[] arrd = tmp.values[j];
                        int n = k;
                        arrd[n] = arrd[n] + v.values[i][k] * V.values[j][i];
                    }
                }
            }
            for (j = 0; j < m.nRow; ++j) {
                for (k = si + 1; k < m.nCol; ++k) {
                    V.values[j][k] = tmp.values[j][k];
                }
            }
            --nc;
        }
        for (i = 0; i < sLength; ++i) {
            single_values[i] = m.values[i][i];
        }
        for (i = 0; i < eLength; ++i) {
            e[i] = m.values[i][i + 1];
        }
        if (m.nRow == 2 && m.nCol == 2) {
            double[] cosl = new double[1];
            double[] cosr = new double[1];
            double[] sinl = new double[1];
            double[] sinr = new double[1];
            GMatrix.compute_2X2(single_values[0], e[0], single_values[1], single_values, sinl, cosl, sinr, cosr, 0);
            GMatrix.update_u(0, U, cosl, sinl);
            GMatrix.update_v(0, V, cosr, sinr);
            return 2;
        }
        GMatrix.compute_qr(0, e.length - 1, single_values, e, U, V);
        rank = single_values.length;
        return rank;
    }

    static void compute_qr(int start, int end, double[] s, double[] e, GMatrix u, GMatrix v) {
        int i;
        double[] cosl = new double[1];
        double[] cosr = new double[1];
        double[] sinl = new double[1];
        double[] sinr = new double[1];
        GMatrix m = new GMatrix(u.nCol, v.nRow);
        int MAX_INTERATIONS = 2;
        double CONVERGE_TOL = 4.89E-15;
        double c_b48 = 1.0;
        double c_b71 = -1.0;
        boolean converged = false;
        double f = 0.0;
        double g = 0.0;
        for (int k = 0; k < 2 && !converged; ++k) {
            double r;
            for (i = start; i <= end; ++i) {
                if (i == start) {
                    int sl = e.length == s.length ? end : end + 1;
                    double shift = GMatrix.compute_shift(s[sl - 1], e[end], s[sl]);
                    f = (Math.abs(s[i]) - shift) * (GMatrix.d_sign(c_b48, s[i]) + shift / s[i]);
                    g = e[i];
                }
                r = GMatrix.compute_rot(f, g, sinr, cosr);
                if (i != start) {
                    e[i - 1] = r;
                }
                f = cosr[0] * s[i] + sinr[0] * e[i];
                e[i] = cosr[0] * e[i] - sinr[0] * s[i];
                g = sinr[0] * s[i + 1];
                s[i + 1] = cosr[0] * s[i + 1];
                GMatrix.update_v(i, v, cosr, sinr);
                s[i] = r = GMatrix.compute_rot(f, g, sinl, cosl);
                f = cosl[0] * e[i] + sinl[0] * s[i + 1];
                s[i + 1] = cosl[0] * s[i + 1] - sinl[0] * e[i];
                if (i < end) {
                    g = sinl[0] * e[i + 1];
                    e[i + 1] = cosl[0] * e[i + 1];
                }
                GMatrix.update_u(i, u, cosl, sinl);
            }
            if (s.length == e.length) {
                r = GMatrix.compute_rot(f, g, sinr, cosr);
                f = cosr[0] * s[i] + sinr[0] * e[i];
                e[i] = cosr[0] * e[i] - sinr[0] * s[i];
                s[i + 1] = cosr[0] * s[i + 1];
                GMatrix.update_v(i, v, cosr, sinr);
            }
            while (end - start > 1 && Math.abs(e[end]) < 4.89E-15) {
                --end;
            }
            for (int n = end - 2; n > start; --n) {
                if (!(Math.abs(e[n]) < 4.89E-15)) continue;
                GMatrix.compute_qr(n + 1, end, s, e, u, v);
                end = n - 1;
                while (end - start > 1 && Math.abs(e[end]) < 4.89E-15) {
                    --end;
                }
            }
            if (end - start > 1 || !(Math.abs(e[start + 1]) < 4.89E-15)) continue;
            converged = true;
        }
        if (Math.abs(e[1]) < 4.89E-15) {
            GMatrix.compute_2X2(s[start], e[start], s[start + 1], s, sinl, cosl, sinr, cosr, 0);
            e[start] = 0.0;
            e[start + 1] = 0.0;
        }
        i = start;
        GMatrix.update_u(i, u, cosl, sinl);
        GMatrix.update_v(i, v, cosr, sinr);
    }

    private static void print_se(double[] s, double[] e) {
        System.out.println("\ns =" + s[0] + " " + s[1] + " " + s[2]);
        System.out.println("e =" + e[0] + " " + e[1]);
    }

    private static void update_v(int index, GMatrix v, double[] cosr, double[] sinr) {
        for (int j = 0; j < v.nRow; ++j) {
            double vtemp = v.values[j][index];
            v.values[j][index] = cosr[0] * vtemp + sinr[0] * v.values[j][index + 1];
            v.values[j][index + 1] = -sinr[0] * vtemp + cosr[0] * v.values[j][index + 1];
        }
    }

    private static void chase_up(double[] s, double[] e, int k, GMatrix v) {
        int i;
        double[] cosr = new double[1];
        double[] sinr = new double[1];
        GMatrix t = new GMatrix(v.nRow, v.nCol);
        GMatrix m = new GMatrix(v.nRow, v.nCol);
        double f = e[k];
        double g = s[k];
        for (i = k; i > 0; --i) {
            double r = GMatrix.compute_rot(f, g, sinr, cosr);
            f = -e[i - 1] * sinr[0];
            g = s[i - 1];
            s[i] = r;
            e[i - 1] = e[i - 1] * cosr[0];
            GMatrix.update_v_split(i, k + 1, v, cosr, sinr, t, m);
        }
        s[i + 1] = GMatrix.compute_rot(f, g, sinr, cosr);
        GMatrix.update_v_split(i, k + 1, v, cosr, sinr, t, m);
    }

    private static void chase_across(double[] s, double[] e, int k, GMatrix u) {
        int i;
        double[] cosl = new double[1];
        double[] sinl = new double[1];
        GMatrix t = new GMatrix(u.nRow, u.nCol);
        GMatrix m = new GMatrix(u.nRow, u.nCol);
        double g = e[k];
        double f = s[k + 1];
        for (i = k; i < u.nCol - 2; ++i) {
            double r = GMatrix.compute_rot(f, g, sinl, cosl);
            g = -e[i + 1] * sinl[0];
            f = s[i + 2];
            s[i + 1] = r;
            e[i + 1] = e[i + 1] * cosl[0];
            GMatrix.update_u_split(k, i + 1, u, cosl, sinl, t, m);
        }
        s[i + 1] = GMatrix.compute_rot(f, g, sinl, cosl);
        GMatrix.update_u_split(k, i + 1, u, cosl, sinl, t, m);
    }

    private static void update_v_split(int topr, int bottomr, GMatrix v, double[] cosr, double[] sinr, GMatrix t, GMatrix m) {
        for (int j = 0; j < v.nRow; ++j) {
            double vtemp = v.values[j][topr];
            v.values[j][topr] = cosr[0] * vtemp - sinr[0] * v.values[j][bottomr];
            v.values[j][bottomr] = sinr[0] * vtemp + cosr[0] * v.values[j][bottomr];
        }
        System.out.println("topr    =" + topr);
        System.out.println("bottomr =" + bottomr);
        System.out.println("cosr =" + cosr[0]);
        System.out.println("sinr =" + sinr[0]);
        System.out.println("\nm =");
        GMatrix.checkMatrix(m);
        System.out.println("\nv =");
        GMatrix.checkMatrix(t);
        m.mul(m, t);
        System.out.println("\nt*m =");
        GMatrix.checkMatrix(m);
    }

    private static void update_u_split(int topr, int bottomr, GMatrix u, double[] cosl, double[] sinl, GMatrix t, GMatrix m) {
        for (int j = 0; j < u.nCol; ++j) {
            double utemp = u.values[topr][j];
            u.values[topr][j] = cosl[0] * utemp - sinl[0] * u.values[bottomr][j];
            u.values[bottomr][j] = sinl[0] * utemp + cosl[0] * u.values[bottomr][j];
        }
        System.out.println("\nm=");
        GMatrix.checkMatrix(m);
        System.out.println("\nu=");
        GMatrix.checkMatrix(t);
        m.mul(t, m);
        System.out.println("\nt*m=");
        GMatrix.checkMatrix(m);
    }

    private static void update_u(int index, GMatrix u, double[] cosl, double[] sinl) {
        for (int j = 0; j < u.nCol; ++j) {
            double utemp = u.values[index][j];
            u.values[index][j] = cosl[0] * utemp + sinl[0] * u.values[index + 1][j];
            u.values[index + 1][j] = -sinl[0] * utemp + cosl[0] * u.values[index + 1][j];
        }
    }

    private static void print_m(GMatrix m, GMatrix u, GMatrix v) {
        GMatrix mtmp = new GMatrix(m.nCol, m.nRow);
        mtmp.mul(u, mtmp);
        mtmp.mul(mtmp, v);
        System.out.println("\n m = \n" + GMatrix.toString(mtmp));
    }

    private static String toString(GMatrix m) {
        StringBuffer buffer = new StringBuffer(m.nRow * m.nCol * 8);
        for (int i = 0; i < m.nRow; ++i) {
            for (int j = 0; j < m.nCol; ++j) {
                if (Math.abs(m.values[i][j]) < 1.0E-9) {
                    buffer.append("0.0000 ");
                    continue;
                }
                buffer.append(m.values[i][j]).append(" ");
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }

    private static void print_svd(double[] s, double[] e, GMatrix u, GMatrix v) {
        int i;
        GMatrix mtmp = new GMatrix(u.nCol, v.nRow);
        System.out.println(" \ns = ");
        for (i = 0; i < s.length; ++i) {
            System.out.println(" " + s[i]);
        }
        System.out.println(" \ne = ");
        for (i = 0; i < e.length; ++i) {
            System.out.println(" " + e[i]);
        }
        System.out.println(" \nu  = \n" + u.toString());
        System.out.println(" \nv  = \n" + v.toString());
        mtmp.setIdentity();
        for (i = 0; i < s.length; ++i) {
            mtmp.values[i][i] = s[i];
        }
        for (i = 0; i < e.length; ++i) {
            mtmp.values[i][i + 1] = e[i];
        }
        System.out.println(" \nm  = \n" + mtmp.toString());
        mtmp.mulTransposeLeft(u, mtmp);
        mtmp.mulTransposeRight(mtmp, v);
        System.out.println(" \n u.transpose*m*v.transpose  = \n" + mtmp.toString());
    }

    static double max(double a, double b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    static double min(double a, double b) {
        if (a < b) {
            return a;
        }
        return b;
    }

    static double compute_shift(double f, double g, double h) {
        double ssmin;
        double fa = Math.abs(f);
        double ga = Math.abs(g);
        double ha = Math.abs(h);
        double fhmn = GMatrix.min(fa, ha);
        double fhmx = GMatrix.max(fa, ha);
        if (fhmn == 0.0) {
            ssmin = 0.0;
            if (fhmx != 0.0) {
                double d__1 = GMatrix.min(fhmx, ga) / GMatrix.max(fhmx, ga);
            }
        } else if (ga < fhmx) {
            double as = fhmn / fhmx + 1.0;
            double at = (fhmx - fhmn) / fhmx;
            double d__1 = ga / fhmx;
            double au = d__1 * d__1;
            double c = 2.0 / (Math.sqrt(as * as + au) + Math.sqrt(at * at + au));
            ssmin = fhmn * c;
        } else {
            double au = fhmx / ga;
            if (au == 0.0) {
                ssmin = fhmn * fhmx / ga;
            } else {
                double as = fhmn / fhmx + 1.0;
                double at = (fhmx - fhmn) / fhmx;
                double d__1 = as * au;
                double d__2 = at * au;
                double c = 1.0 / (Math.sqrt(d__1 * d__1 + 1.0) + Math.sqrt(d__2 * d__2 + 1.0));
                ssmin = fhmn * c * au;
                ssmin += ssmin;
            }
        }
        return ssmin;
    }

    static int compute_2X2(double f, double g, double h, double[] single_values, double[] snl, double[] csl, double[] snr, double[] csr, int index) {
        double gt;
        double ga;
        double c_b3 = 2.0;
        double c_b4 = 1.0;
        double ssmax = single_values[0];
        double ssmin = single_values[1];
        double clt = 0.0;
        double crt = 0.0;
        double slt = 0.0;
        double srt = 0.0;
        double tsign = 0.0;
        double ft = f;
        double fa = Math.abs(ft);
        double ht = h;
        double ha = Math.abs(h);
        int pmax = 1;
        boolean swap = ha > fa;
        if (swap) {
            pmax = 3;
            double temp = ft;
            ft = ht;
            ht = temp;
            temp = fa;
            fa = ha;
            ha = temp;
        }
        if ((ga = Math.abs(gt = g)) == 0.0) {
            single_values[1] = ha;
            single_values[0] = fa;
            clt = 1.0;
            crt = 1.0;
            slt = 0.0;
            srt = 0.0;
        } else {
            boolean gasmal = true;
            if (ga > fa) {
                pmax = 2;
                if (fa / ga < 1.0E-10) {
                    gasmal = false;
                    ssmax = ga;
                    ssmin = ha > 1.0 ? fa / (ga / ha) : fa / ga * ha;
                    clt = 1.0;
                    slt = ht / gt;
                    srt = 1.0;
                    crt = ft / gt;
                }
            }
            if (gasmal) {
                double d = fa - ha;
                double l = d == fa ? 1.0 : d / fa;
                double m = gt / ft;
                double t = 2.0 - l;
                double mm = m * m;
                double tt = t * t;
                double s = Math.sqrt(tt + mm);
                double r = l == 0.0 ? Math.abs(m) : Math.sqrt(l * l + mm);
                double a = (s + r) * 0.5;
                if (ga > fa) {
                    pmax = 2;
                    if (fa / ga < 1.0E-10) {
                        gasmal = false;
                        ssmax = ga;
                        ssmin = ha > 1.0 ? fa / (ga / ha) : fa / ga * ha;
                        clt = 1.0;
                        slt = ht / gt;
                        srt = 1.0;
                        crt = ft / gt;
                    }
                }
                if (gasmal) {
                    d = fa - ha;
                    l = d == fa ? 1.0 : d / fa;
                    m = gt / ft;
                    t = 2.0 - l;
                    mm = m * m;
                    tt = t * t;
                    s = Math.sqrt(tt + mm);
                    r = l == 0.0 ? Math.abs(m) : Math.sqrt(l * l + mm);
                    a = (s + r) * 0.5;
                    ssmin = ha / a;
                    ssmax = fa * a;
                    t = mm == 0.0 ? (l == 0.0 ? GMatrix.d_sign(c_b3, ft) * GMatrix.d_sign(c_b4, gt) : gt / GMatrix.d_sign(d, ft) + m / t) : (m / (s + t) + m / (r + l)) * (a + 1.0);
                    l = Math.sqrt(t * t + 4.0);
                    crt = 2.0 / l;
                    srt = t / l;
                    clt = (crt + srt * m) / a;
                    slt = ht / ft * srt / a;
                }
            }
            if (swap) {
                csl[0] = srt;
                snl[0] = crt;
                csr[0] = slt;
                snr[0] = clt;
            } else {
                csl[0] = clt;
                snl[0] = slt;
                csr[0] = crt;
                snr[0] = srt;
            }
            if (pmax == 1) {
                tsign = GMatrix.d_sign(c_b4, csr[0]) * GMatrix.d_sign(c_b4, csl[0]) * GMatrix.d_sign(c_b4, f);
            }
            if (pmax == 2) {
                tsign = GMatrix.d_sign(c_b4, snr[0]) * GMatrix.d_sign(c_b4, csl[0]) * GMatrix.d_sign(c_b4, g);
            }
            if (pmax == 3) {
                tsign = GMatrix.d_sign(c_b4, snr[0]) * GMatrix.d_sign(c_b4, snl[0]) * GMatrix.d_sign(c_b4, h);
            }
            single_values[index] = GMatrix.d_sign(ssmax, tsign);
            double d__1 = tsign * GMatrix.d_sign(c_b4, f) * GMatrix.d_sign(c_b4, h);
            single_values[index + 1] = GMatrix.d_sign(ssmin, d__1);
        }
        return 0;
    }

    static double compute_rot(double f, double g, double[] sin, double[] cos) {
        double r;
        double sn;
        double cs;
        double safmn2 = 2.002083095183101E-146;
        double safmx2 = 4.9947976805055876E145;
        if (g == 0.0) {
            cs = 1.0;
            sn = 0.0;
            r = f;
        } else if (f == 0.0) {
            cs = 0.0;
            sn = 1.0;
            r = g;
        } else {
            double f1 = f;
            double g1 = g;
            double scale = GMatrix.max(Math.abs(f1), Math.abs(g1));
            if (scale >= 4.9947976805055876E145) {
                int count = 0;
                while (scale >= 4.9947976805055876E145) {
                    ++count;
                    scale = GMatrix.max(Math.abs(f1 *= 2.002083095183101E-146), Math.abs(g1 *= 2.002083095183101E-146));
                }
                r = Math.sqrt(f1 * f1 + g1 * g1);
                cs = f1 / r;
                sn = g1 / r;
                int i__1 = count;
                for (int i = 1; i <= count; ++i) {
                    r *= 4.9947976805055876E145;
                }
            } else if (scale <= 2.002083095183101E-146) {
                int count = 0;
                while (scale <= 2.002083095183101E-146) {
                    ++count;
                    scale = GMatrix.max(Math.abs(f1 *= 4.9947976805055876E145), Math.abs(g1 *= 4.9947976805055876E145));
                }
                r = Math.sqrt(f1 * f1 + g1 * g1);
                cs = f1 / r;
                sn = g1 / r;
                int i__1 = count;
                for (int i = 1; i <= count; ++i) {
                    r *= 2.002083095183101E-146;
                }
            } else {
                r = Math.sqrt(f1 * f1 + g1 * g1);
                cs = f1 / r;
                sn = g1 / r;
            }
            if (Math.abs(f) > Math.abs(g) && cs < 0.0) {
                cs = -cs;
                sn = -sn;
                r = -r;
            }
        }
        sin[0] = sn;
        cos[0] = cs;
        return r;
    }

    static double d_sign(double a, double b) {
        double x = a >= 0.0 ? a : -a;
        return b >= 0.0 ? x : -x;
    }

    public Object clone() {
        GMatrix m1 = null;
        try {
            m1 = (GMatrix)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        m1.values = new double[this.nRow][this.nCol];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                m1.values[i][j] = this.values[i][j];
            }
        }
        return m1;
    }
}

