/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.SingularMatrixException;
import javax.vecmath.Tuple4f;
import javax.vecmath.VecMathI18N;
import javax.vecmath.VecMathUtil;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class Matrix4f
implements Serializable,
Cloneable {
    static final long serialVersionUID = -8405036035410109353L;
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;
    private static final double EPS = 1.0E-8;

    public Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    public Matrix4f(float[] v) {
        this.m00 = v[0];
        this.m01 = v[1];
        this.m02 = v[2];
        this.m03 = v[3];
        this.m10 = v[4];
        this.m11 = v[5];
        this.m12 = v[6];
        this.m13 = v[7];
        this.m20 = v[8];
        this.m21 = v[9];
        this.m22 = v[10];
        this.m23 = v[11];
        this.m30 = v[12];
        this.m31 = v[13];
        this.m32 = v[14];
        this.m33 = v[15];
    }

    public Matrix4f(Quat4f q1, Vector3f t1, float s) {
        this.m00 = (float)((double)s * (1.0 - 2.0 * (double)q1.y * (double)q1.y - 2.0 * (double)q1.z * (double)q1.z));
        this.m10 = (float)((double)s * (2.0 * (double)(q1.x * q1.y + q1.w * q1.z)));
        this.m20 = (float)((double)s * (2.0 * (double)(q1.x * q1.z - q1.w * q1.y)));
        this.m01 = (float)((double)s * (2.0 * (double)(q1.x * q1.y - q1.w * q1.z)));
        this.m11 = (float)((double)s * (1.0 - 2.0 * (double)q1.x * (double)q1.x - 2.0 * (double)q1.z * (double)q1.z));
        this.m21 = (float)((double)s * (2.0 * (double)(q1.y * q1.z + q1.w * q1.x)));
        this.m02 = (float)((double)s * (2.0 * (double)(q1.x * q1.z + q1.w * q1.y)));
        this.m12 = (float)((double)s * (2.0 * (double)(q1.y * q1.z - q1.w * q1.x)));
        this.m22 = (float)((double)s * (1.0 - 2.0 * (double)q1.x * (double)q1.x - 2.0 * (double)q1.y * (double)q1.y));
        this.m03 = t1.x;
        this.m13 = t1.y;
        this.m23 = t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public Matrix4f(Matrix4d m1) {
        this.m00 = (float)m1.m00;
        this.m01 = (float)m1.m01;
        this.m02 = (float)m1.m02;
        this.m03 = (float)m1.m03;
        this.m10 = (float)m1.m10;
        this.m11 = (float)m1.m11;
        this.m12 = (float)m1.m12;
        this.m13 = (float)m1.m13;
        this.m20 = (float)m1.m20;
        this.m21 = (float)m1.m21;
        this.m22 = (float)m1.m22;
        this.m23 = (float)m1.m23;
        this.m30 = (float)m1.m30;
        this.m31 = (float)m1.m31;
        this.m32 = (float)m1.m32;
        this.m33 = (float)m1.m33;
    }

    public Matrix4f(Matrix4f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;
        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;
    }

    public Matrix4f(Matrix3f m1, Vector3f t1, float s) {
        this.m00 = m1.m00 * s;
        this.m01 = m1.m01 * s;
        this.m02 = m1.m02 * s;
        this.m03 = t1.x;
        this.m10 = m1.m10 * s;
        this.m11 = m1.m11 * s;
        this.m12 = m1.m12 * s;
        this.m13 = t1.y;
        this.m20 = m1.m20 * s;
        this.m21 = m1.m21 * s;
        this.m22 = m1.m22 * s;
        this.m23 = t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public Matrix4f() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 0.0f;
    }

    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
    }

    public final void setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void setElement(int row, int column, float value) {
        block0 : switch (row) {
            case 0: {
                switch (column) {
                    case 0: {
                        this.m00 = value;
                        break block0;
                    }
                    case 1: {
                        this.m01 = value;
                        break block0;
                    }
                    case 2: {
                        this.m02 = value;
                        break block0;
                    }
                    case 3: {
                        this.m03 = value;
                        break block0;
                    }
                }
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
            }
            case 1: {
                switch (column) {
                    case 0: {
                        this.m10 = value;
                        break block0;
                    }
                    case 1: {
                        this.m11 = value;
                        break block0;
                    }
                    case 2: {
                        this.m12 = value;
                        break block0;
                    }
                    case 3: {
                        this.m13 = value;
                        break block0;
                    }
                }
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
            }
            case 2: {
                switch (column) {
                    case 0: {
                        this.m20 = value;
                        break block0;
                    }
                    case 1: {
                        this.m21 = value;
                        break block0;
                    }
                    case 2: {
                        this.m22 = value;
                        break block0;
                    }
                    case 3: {
                        this.m23 = value;
                        break block0;
                    }
                }
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
            }
            case 3: {
                switch (column) {
                    case 0: {
                        this.m30 = value;
                        break block0;
                    }
                    case 1: {
                        this.m31 = value;
                        break block0;
                    }
                    case 2: {
                        this.m32 = value;
                        break block0;
                    }
                    case 3: {
                        this.m33 = value;
                        break block0;
                    }
                }
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
            }
        }
    }

    public final float getElement(int row, int column) {
        switch (row) {
            case 0: {
                switch (column) {
                    case 0: {
                        return this.m00;
                    }
                    case 1: {
                        return this.m01;
                    }
                    case 2: {
                        return this.m02;
                    }
                    case 3: {
                        return this.m03;
                    }
                }
                break;
            }
            case 1: {
                switch (column) {
                    case 0: {
                        return this.m10;
                    }
                    case 1: {
                        return this.m11;
                    }
                    case 2: {
                        return this.m12;
                    }
                    case 3: {
                        return this.m13;
                    }
                }
                break;
            }
            case 2: {
                switch (column) {
                    case 0: {
                        return this.m20;
                    }
                    case 1: {
                        return this.m21;
                    }
                    case 2: {
                        return this.m22;
                    }
                    case 3: {
                        return this.m23;
                    }
                }
                break;
            }
            case 3: {
                switch (column) {
                    case 0: {
                        return this.m30;
                    }
                    case 1: {
                        return this.m31;
                    }
                    case 2: {
                        return this.m32;
                    }
                    case 3: {
                        return this.m33;
                    }
                }
                break;
            }
        }
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
    }

    public final void getRow(int row, Vector4f v) {
        if (row == 0) {
            v.x = this.m00;
            v.y = this.m01;
            v.z = this.m02;
            v.w = this.m03;
        } else if (row == 1) {
            v.x = this.m10;
            v.y = this.m11;
            v.z = this.m12;
            v.w = this.m13;
        } else if (row == 2) {
            v.x = this.m20;
            v.y = this.m21;
            v.z = this.m22;
            v.w = this.m23;
        } else if (row == 3) {
            v.x = this.m30;
            v.y = this.m31;
            v.z = this.m32;
            v.w = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
        }
    }

    public final void getRow(int row, float[] v) {
        if (row == 0) {
            v[0] = this.m00;
            v[1] = this.m01;
            v[2] = this.m02;
            v[3] = this.m03;
        } else if (row == 1) {
            v[0] = this.m10;
            v[1] = this.m11;
            v[2] = this.m12;
            v[3] = this.m13;
        } else if (row == 2) {
            v[0] = this.m20;
            v[1] = this.m21;
            v[2] = this.m22;
            v[3] = this.m23;
        } else if (row == 3) {
            v[0] = this.m30;
            v[1] = this.m31;
            v[2] = this.m32;
            v[3] = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
        }
    }

    public final void getColumn(int column, Vector4f v) {
        if (column == 0) {
            v.x = this.m00;
            v.y = this.m10;
            v.z = this.m20;
            v.w = this.m30;
        } else if (column == 1) {
            v.x = this.m01;
            v.y = this.m11;
            v.z = this.m21;
            v.w = this.m31;
        } else if (column == 2) {
            v.x = this.m02;
            v.y = this.m12;
            v.z = this.m22;
            v.w = this.m32;
        } else if (column == 3) {
            v.x = this.m03;
            v.y = this.m13;
            v.z = this.m23;
            v.w = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
        }
    }

    public final void getColumn(int column, float[] v) {
        if (column == 0) {
            v[0] = this.m00;
            v[1] = this.m10;
            v[2] = this.m20;
            v[3] = this.m30;
        } else if (column == 1) {
            v[0] = this.m01;
            v[1] = this.m11;
            v[2] = this.m21;
            v[3] = this.m31;
        } else if (column == 2) {
            v[0] = this.m02;
            v[1] = this.m12;
            v[2] = this.m22;
            v[3] = this.m32;
        } else if (column == 3) {
            v[0] = this.m03;
            v[1] = this.m13;
            v[2] = this.m23;
            v[3] = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
        }
    }

    public final void setScale(float scale) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        this.m00 = (float)(tmp_rot[0] * (double)scale);
        this.m01 = (float)(tmp_rot[1] * (double)scale);
        this.m02 = (float)(tmp_rot[2] * (double)scale);
        this.m10 = (float)(tmp_rot[3] * (double)scale);
        this.m11 = (float)(tmp_rot[4] * (double)scale);
        this.m12 = (float)(tmp_rot[5] * (double)scale);
        this.m20 = (float)(tmp_rot[6] * (double)scale);
        this.m21 = (float)(tmp_rot[7] * (double)scale);
        this.m22 = (float)(tmp_rot[8] * (double)scale);
    }

    public final void get(Matrix3d m1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        m1.m00 = tmp_rot[0];
        m1.m01 = tmp_rot[1];
        m1.m02 = tmp_rot[2];
        m1.m10 = tmp_rot[3];
        m1.m11 = tmp_rot[4];
        m1.m12 = tmp_rot[5];
        m1.m20 = tmp_rot[6];
        m1.m21 = tmp_rot[7];
        m1.m22 = tmp_rot[8];
    }

    public final void get(Matrix3f m1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        m1.m00 = (float)tmp_rot[0];
        m1.m01 = (float)tmp_rot[1];
        m1.m02 = (float)tmp_rot[2];
        m1.m10 = (float)tmp_rot[3];
        m1.m11 = (float)tmp_rot[4];
        m1.m12 = (float)tmp_rot[5];
        m1.m20 = (float)tmp_rot[6];
        m1.m21 = (float)tmp_rot[7];
        m1.m22 = (float)tmp_rot[8];
    }

    public final float get(Matrix3f m1, Vector3f t1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        m1.m00 = (float)tmp_rot[0];
        m1.m01 = (float)tmp_rot[1];
        m1.m02 = (float)tmp_rot[2];
        m1.m10 = (float)tmp_rot[3];
        m1.m11 = (float)tmp_rot[4];
        m1.m12 = (float)tmp_rot[5];
        m1.m20 = (float)tmp_rot[6];
        m1.m21 = (float)tmp_rot[7];
        m1.m22 = (float)tmp_rot[8];
        t1.x = this.m03;
        t1.y = this.m13;
        t1.z = this.m23;
        return (float)Matrix3d.max3(tmp_scale);
    }

    public final void get(Quat4f q1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        double ww = 0.25 * (1.0 + tmp_rot[0] + tmp_rot[4] + tmp_rot[8]);
        if (!((ww < 0.0 ? -ww : ww) < 1.0E-30)) {
            q1.w = (float)Math.sqrt(ww);
            ww = 0.25 / (double)q1.w;
            q1.x = (float)((tmp_rot[7] - tmp_rot[5]) * ww);
            q1.y = (float)((tmp_rot[2] - tmp_rot[6]) * ww);
            q1.z = (float)((tmp_rot[3] - tmp_rot[1]) * ww);
            return;
        }
        q1.w = 0.0f;
        ww = -0.5 * (tmp_rot[4] + tmp_rot[8]);
        if (!((ww < 0.0 ? -ww : ww) < 1.0E-30)) {
            q1.x = (float)Math.sqrt(ww);
            ww = 0.5 / (double)q1.x;
            q1.y = (float)(tmp_rot[3] * ww);
            q1.z = (float)(tmp_rot[6] * ww);
            return;
        }
        q1.x = 0.0f;
        ww = 0.5 * (1.0 - tmp_rot[8]);
        if (!((ww < 0.0 ? -ww : ww) < 1.0E-30)) {
            q1.y = (float)Math.sqrt(ww);
            q1.z = (float)(tmp_rot[7] / (2.0 * (double)q1.y));
            return;
        }
        q1.y = 0.0f;
        q1.z = 1.0f;
    }

    public final void get(Vector3f trans) {
        trans.x = this.m03;
        trans.y = this.m13;
        trans.z = this.m23;
    }

    public final void getRotationScale(Matrix3f m1) {
        m1.m00 = this.m00;
        m1.m01 = this.m01;
        m1.m02 = this.m02;
        m1.m10 = this.m10;
        m1.m11 = this.m11;
        m1.m12 = this.m12;
        m1.m20 = this.m20;
        m1.m21 = this.m21;
        m1.m22 = this.m22;
    }

    public final float getScale() {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        return (float)Matrix3d.max3(tmp_scale);
    }

    public final void setRotationScale(Matrix3f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    public final void setRow(int row, float x, float y, float z, float w) {
        switch (row) {
            case 0: {
                this.m00 = x;
                this.m01 = y;
                this.m02 = z;
                this.m03 = w;
                break;
            }
            case 1: {
                this.m10 = x;
                this.m11 = y;
                this.m12 = z;
                this.m13 = w;
                break;
            }
            case 2: {
                this.m20 = x;
                this.m21 = y;
                this.m22 = z;
                this.m23 = w;
                break;
            }
            case 3: {
                this.m30 = x;
                this.m31 = y;
                this.m32 = z;
                this.m33 = w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
            }
        }
    }

    public final void setRow(int row, Vector4f v) {
        switch (row) {
            case 0: {
                this.m00 = v.x;
                this.m01 = v.y;
                this.m02 = v.z;
                this.m03 = v.w;
                break;
            }
            case 1: {
                this.m10 = v.x;
                this.m11 = v.y;
                this.m12 = v.z;
                this.m13 = v.w;
                break;
            }
            case 2: {
                this.m20 = v.x;
                this.m21 = v.y;
                this.m22 = v.z;
                this.m23 = v.w;
                break;
            }
            case 3: {
                this.m30 = v.x;
                this.m31 = v.y;
                this.m32 = v.z;
                this.m33 = v.w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
            }
        }
    }

    public final void setRow(int row, float[] v) {
        switch (row) {
            case 0: {
                this.m00 = v[0];
                this.m01 = v[1];
                this.m02 = v[2];
                this.m03 = v[3];
                break;
            }
            case 1: {
                this.m10 = v[0];
                this.m11 = v[1];
                this.m12 = v[2];
                this.m13 = v[3];
                break;
            }
            case 2: {
                this.m20 = v[0];
                this.m21 = v[1];
                this.m22 = v[2];
                this.m23 = v[3];
                break;
            }
            case 3: {
                this.m30 = v[0];
                this.m31 = v[1];
                this.m32 = v[2];
                this.m33 = v[3];
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
            }
        }
    }

    public final void setColumn(int column, float x, float y, float z, float w) {
        switch (column) {
            case 0: {
                this.m00 = x;
                this.m10 = y;
                this.m20 = z;
                this.m30 = w;
                break;
            }
            case 1: {
                this.m01 = x;
                this.m11 = y;
                this.m21 = z;
                this.m31 = w;
                break;
            }
            case 2: {
                this.m02 = x;
                this.m12 = y;
                this.m22 = z;
                this.m32 = w;
                break;
            }
            case 3: {
                this.m03 = x;
                this.m13 = y;
                this.m23 = z;
                this.m33 = w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
            }
        }
    }

    public final void setColumn(int column, Vector4f v) {
        switch (column) {
            case 0: {
                this.m00 = v.x;
                this.m10 = v.y;
                this.m20 = v.z;
                this.m30 = v.w;
                break;
            }
            case 1: {
                this.m01 = v.x;
                this.m11 = v.y;
                this.m21 = v.z;
                this.m31 = v.w;
                break;
            }
            case 2: {
                this.m02 = v.x;
                this.m12 = v.y;
                this.m22 = v.z;
                this.m32 = v.w;
                break;
            }
            case 3: {
                this.m03 = v.x;
                this.m13 = v.y;
                this.m23 = v.z;
                this.m33 = v.w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
            }
        }
    }

    public final void setColumn(int column, float[] v) {
        switch (column) {
            case 0: {
                this.m00 = v[0];
                this.m10 = v[1];
                this.m20 = v[2];
                this.m30 = v[3];
                break;
            }
            case 1: {
                this.m01 = v[0];
                this.m11 = v[1];
                this.m21 = v[2];
                this.m31 = v[3];
                break;
            }
            case 2: {
                this.m02 = v[0];
                this.m12 = v[1];
                this.m22 = v[2];
                this.m32 = v[3];
                break;
            }
            case 3: {
                this.m03 = v[0];
                this.m13 = v[1];
                this.m23 = v[2];
                this.m33 = v[3];
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
            }
        }
    }

    public final void add(float scalar) {
        this.m00 += scalar;
        this.m01 += scalar;
        this.m02 += scalar;
        this.m03 += scalar;
        this.m10 += scalar;
        this.m11 += scalar;
        this.m12 += scalar;
        this.m13 += scalar;
        this.m20 += scalar;
        this.m21 += scalar;
        this.m22 += scalar;
        this.m23 += scalar;
        this.m30 += scalar;
        this.m31 += scalar;
        this.m32 += scalar;
        this.m33 += scalar;
    }

    public final void add(float scalar, Matrix4f m1) {
        this.m00 = m1.m00 + scalar;
        this.m01 = m1.m01 + scalar;
        this.m02 = m1.m02 + scalar;
        this.m03 = m1.m03 + scalar;
        this.m10 = m1.m10 + scalar;
        this.m11 = m1.m11 + scalar;
        this.m12 = m1.m12 + scalar;
        this.m13 = m1.m13 + scalar;
        this.m20 = m1.m20 + scalar;
        this.m21 = m1.m21 + scalar;
        this.m22 = m1.m22 + scalar;
        this.m23 = m1.m23 + scalar;
        this.m30 = m1.m30 + scalar;
        this.m31 = m1.m31 + scalar;
        this.m32 = m1.m32 + scalar;
        this.m33 = m1.m33 + scalar;
    }

    public final void add(Matrix4f m1, Matrix4f m2) {
        this.m00 = m1.m00 + m2.m00;
        this.m01 = m1.m01 + m2.m01;
        this.m02 = m1.m02 + m2.m02;
        this.m03 = m1.m03 + m2.m03;
        this.m10 = m1.m10 + m2.m10;
        this.m11 = m1.m11 + m2.m11;
        this.m12 = m1.m12 + m2.m12;
        this.m13 = m1.m13 + m2.m13;
        this.m20 = m1.m20 + m2.m20;
        this.m21 = m1.m21 + m2.m21;
        this.m22 = m1.m22 + m2.m22;
        this.m23 = m1.m23 + m2.m23;
        this.m30 = m1.m30 + m2.m30;
        this.m31 = m1.m31 + m2.m31;
        this.m32 = m1.m32 + m2.m32;
        this.m33 = m1.m33 + m2.m33;
    }

    public final void add(Matrix4f m1) {
        this.m00 += m1.m00;
        this.m01 += m1.m01;
        this.m02 += m1.m02;
        this.m03 += m1.m03;
        this.m10 += m1.m10;
        this.m11 += m1.m11;
        this.m12 += m1.m12;
        this.m13 += m1.m13;
        this.m20 += m1.m20;
        this.m21 += m1.m21;
        this.m22 += m1.m22;
        this.m23 += m1.m23;
        this.m30 += m1.m30;
        this.m31 += m1.m31;
        this.m32 += m1.m32;
        this.m33 += m1.m33;
    }

    public final void sub(Matrix4f m1, Matrix4f m2) {
        this.m00 = m1.m00 - m2.m00;
        this.m01 = m1.m01 - m2.m01;
        this.m02 = m1.m02 - m2.m02;
        this.m03 = m1.m03 - m2.m03;
        this.m10 = m1.m10 - m2.m10;
        this.m11 = m1.m11 - m2.m11;
        this.m12 = m1.m12 - m2.m12;
        this.m13 = m1.m13 - m2.m13;
        this.m20 = m1.m20 - m2.m20;
        this.m21 = m1.m21 - m2.m21;
        this.m22 = m1.m22 - m2.m22;
        this.m23 = m1.m23 - m2.m23;
        this.m30 = m1.m30 - m2.m30;
        this.m31 = m1.m31 - m2.m31;
        this.m32 = m1.m32 - m2.m32;
        this.m33 = m1.m33 - m2.m33;
    }

    public final void sub(Matrix4f m1) {
        this.m00 -= m1.m00;
        this.m01 -= m1.m01;
        this.m02 -= m1.m02;
        this.m03 -= m1.m03;
        this.m10 -= m1.m10;
        this.m11 -= m1.m11;
        this.m12 -= m1.m12;
        this.m13 -= m1.m13;
        this.m20 -= m1.m20;
        this.m21 -= m1.m21;
        this.m22 -= m1.m22;
        this.m23 -= m1.m23;
        this.m30 -= m1.m30;
        this.m31 -= m1.m31;
        this.m32 -= m1.m32;
        this.m33 -= m1.m33;
    }

    public final void transpose() {
        float temp = this.m10;
        this.m10 = this.m01;
        this.m01 = temp;
        temp = this.m20;
        this.m20 = this.m02;
        this.m02 = temp;
        temp = this.m30;
        this.m30 = this.m03;
        this.m03 = temp;
        temp = this.m21;
        this.m21 = this.m12;
        this.m12 = temp;
        temp = this.m31;
        this.m31 = this.m13;
        this.m13 = temp;
        temp = this.m32;
        this.m32 = this.m23;
        this.m23 = temp;
    }

    public final void transpose(Matrix4f m1) {
        if (this != m1) {
            this.m00 = m1.m00;
            this.m01 = m1.m10;
            this.m02 = m1.m20;
            this.m03 = m1.m30;
            this.m10 = m1.m01;
            this.m11 = m1.m11;
            this.m12 = m1.m21;
            this.m13 = m1.m31;
            this.m20 = m1.m02;
            this.m21 = m1.m12;
            this.m22 = m1.m22;
            this.m23 = m1.m32;
            this.m30 = m1.m03;
            this.m31 = m1.m13;
            this.m32 = m1.m23;
            this.m33 = m1.m33;
        } else {
            this.transpose();
        }
    }

    public final void set(Quat4f q1) {
        this.m00 = 1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z;
        this.m10 = 2.0f * (q1.x * q1.y + q1.w * q1.z);
        this.m20 = 2.0f * (q1.x * q1.z - q1.w * q1.y);
        this.m01 = 2.0f * (q1.x * q1.y - q1.w * q1.z);
        this.m11 = 1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z;
        this.m21 = 2.0f * (q1.y * q1.z + q1.w * q1.x);
        this.m02 = 2.0f * (q1.x * q1.z + q1.w * q1.y);
        this.m12 = 2.0f * (q1.y * q1.z - q1.w * q1.x);
        this.m22 = 1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y;
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(AxisAngle4f a1) {
        float mag = (float)Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
        if ((double)mag < 1.0E-8) {
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m02 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
            this.m12 = 0.0f;
            this.m20 = 0.0f;
            this.m21 = 0.0f;
            this.m22 = 1.0f;
        } else {
            mag = 1.0f / mag;
            float ax = a1.x * mag;
            float ay = a1.y * mag;
            float az = a1.z * mag;
            float sinTheta = (float)Math.sin(a1.angle);
            float cosTheta = (float)Math.cos(a1.angle);
            float t = 1.0f - cosTheta;
            float xz = ax * az;
            float xy = ax * ay;
            float yz = ay * az;
            this.m00 = t * ax * ax + cosTheta;
            this.m01 = t * xy - sinTheta * az;
            this.m02 = t * xz + sinTheta * ay;
            this.m10 = t * xy + sinTheta * az;
            this.m11 = t * ay * ay + cosTheta;
            this.m12 = t * yz - sinTheta * ax;
            this.m20 = t * xz - sinTheta * ay;
            this.m21 = t * yz + sinTheta * ax;
            this.m22 = t * az * az + cosTheta;
        }
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Quat4d q1) {
        this.m00 = (float)(1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z);
        this.m10 = (float)(2.0 * (q1.x * q1.y + q1.w * q1.z));
        this.m20 = (float)(2.0 * (q1.x * q1.z - q1.w * q1.y));
        this.m01 = (float)(2.0 * (q1.x * q1.y - q1.w * q1.z));
        this.m11 = (float)(1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z);
        this.m21 = (float)(2.0 * (q1.y * q1.z + q1.w * q1.x));
        this.m02 = (float)(2.0 * (q1.x * q1.z + q1.w * q1.y));
        this.m12 = (float)(2.0 * (q1.y * q1.z - q1.w * q1.x));
        this.m22 = (float)(1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y);
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(AxisAngle4d a1) {
        double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
        if (mag < 1.0E-8) {
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m02 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
            this.m12 = 0.0f;
            this.m20 = 0.0f;
            this.m21 = 0.0f;
            this.m22 = 1.0f;
        } else {
            mag = 1.0 / mag;
            double ax = a1.x * mag;
            double ay = a1.y * mag;
            double az = a1.z * mag;
            float sinTheta = (float)Math.sin(a1.angle);
            float cosTheta = (float)Math.cos(a1.angle);
            float t = 1.0f - cosTheta;
            float xz = (float)(ax * az);
            float xy = (float)(ax * ay);
            float yz = (float)(ay * az);
            this.m00 = t * (float)(ax * ax) + cosTheta;
            this.m01 = t * xy - sinTheta * (float)az;
            this.m02 = t * xz + sinTheta * (float)ay;
            this.m10 = t * xy + sinTheta * (float)az;
            this.m11 = t * (float)(ay * ay) + cosTheta;
            this.m12 = t * yz - sinTheta * (float)ax;
            this.m20 = t * xz - sinTheta * (float)ay;
            this.m21 = t * yz + sinTheta * (float)ax;
            this.m22 = t * (float)(az * az) + cosTheta;
        }
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Quat4d q1, Vector3d t1, double s) {
        this.m00 = (float)(s * (1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z));
        this.m10 = (float)(s * (2.0 * (q1.x * q1.y + q1.w * q1.z)));
        this.m20 = (float)(s * (2.0 * (q1.x * q1.z - q1.w * q1.y)));
        this.m01 = (float)(s * (2.0 * (q1.x * q1.y - q1.w * q1.z)));
        this.m11 = (float)(s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z));
        this.m21 = (float)(s * (2.0 * (q1.y * q1.z + q1.w * q1.x)));
        this.m02 = (float)(s * (2.0 * (q1.x * q1.z + q1.w * q1.y)));
        this.m12 = (float)(s * (2.0 * (q1.y * q1.z - q1.w * q1.x)));
        this.m22 = (float)(s * (1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y));
        this.m03 = (float)t1.x;
        this.m13 = (float)t1.y;
        this.m23 = (float)t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Quat4f q1, Vector3f t1, float s) {
        this.m00 = s * (1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z);
        this.m10 = s * (2.0f * (q1.x * q1.y + q1.w * q1.z));
        this.m20 = s * (2.0f * (q1.x * q1.z - q1.w * q1.y));
        this.m01 = s * (2.0f * (q1.x * q1.y - q1.w * q1.z));
        this.m11 = s * (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z);
        this.m21 = s * (2.0f * (q1.y * q1.z + q1.w * q1.x));
        this.m02 = s * (2.0f * (q1.x * q1.z + q1.w * q1.y));
        this.m12 = s * (2.0f * (q1.y * q1.z - q1.w * q1.x));
        this.m22 = s * (1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y);
        this.m03 = t1.x;
        this.m13 = t1.y;
        this.m23 = t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Matrix4d m1) {
        this.m00 = (float)m1.m00;
        this.m01 = (float)m1.m01;
        this.m02 = (float)m1.m02;
        this.m03 = (float)m1.m03;
        this.m10 = (float)m1.m10;
        this.m11 = (float)m1.m11;
        this.m12 = (float)m1.m12;
        this.m13 = (float)m1.m13;
        this.m20 = (float)m1.m20;
        this.m21 = (float)m1.m21;
        this.m22 = (float)m1.m22;
        this.m23 = (float)m1.m23;
        this.m30 = (float)m1.m30;
        this.m31 = (float)m1.m31;
        this.m32 = (float)m1.m32;
        this.m33 = (float)m1.m33;
    }

    public final void set(Matrix4f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;
        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;
    }

    public final void invert(Matrix4f m1) {
        this.invertGeneral(m1);
    }

    public final void invert() {
        this.invertGeneral(this);
    }

    final void invertGeneral(Matrix4f m1) {
        double[] temp = new double[16];
        double[] result = new double[16];
        int[] row_perm = new int[4];
        temp[0] = m1.m00;
        temp[1] = m1.m01;
        temp[2] = m1.m02;
        temp[3] = m1.m03;
        temp[4] = m1.m10;
        temp[5] = m1.m11;
        temp[6] = m1.m12;
        temp[7] = m1.m13;
        temp[8] = m1.m20;
        temp[9] = m1.m21;
        temp[10] = m1.m22;
        temp[11] = m1.m23;
        temp[12] = m1.m30;
        temp[13] = m1.m31;
        temp[14] = m1.m32;
        temp[15] = m1.m33;
        if (!Matrix4f.luDecomposition(temp, row_perm)) {
            throw new SingularMatrixException(VecMathI18N.getString("Matrix4f12"));
        }
        for (int i = 0; i < 16; ++i) {
            result[i] = 0.0;
        }
        result[0] = 1.0;
        result[5] = 1.0;
        result[10] = 1.0;
        result[15] = 1.0;
        Matrix4f.luBacksubstitution(temp, row_perm, result);
        this.m00 = (float)result[0];
        this.m01 = (float)result[1];
        this.m02 = (float)result[2];
        this.m03 = (float)result[3];
        this.m10 = (float)result[4];
        this.m11 = (float)result[5];
        this.m12 = (float)result[6];
        this.m13 = (float)result[7];
        this.m20 = (float)result[8];
        this.m21 = (float)result[9];
        this.m22 = (float)result[10];
        this.m23 = (float)result[11];
        this.m30 = (float)result[12];
        this.m31 = (float)result[13];
        this.m32 = (float)result[14];
        this.m33 = (float)result[15];
    }

    static boolean luDecomposition(double[] matrix0, int[] row_perm) {
        double[] row_scale = new double[4];
        int ptr = 0;
        int rs = 0;
        int i = 4;
        while (i-- != 0) {
            double big = 0.0;
            int j = 4;
            while (j-- != 0) {
                double temp = matrix0[ptr++];
                if (!((temp = Math.abs(temp)) > big)) continue;
                big = temp;
            }
            if (big == 0.0) {
                return false;
            }
            row_scale[rs++] = 1.0 / big;
        }
        int mtx = 0;
        for (int j = 0; j < 4; ++j) {
            double temp;
            int p1;
            int k;
            int p2;
            double sum;
            int target;
            int i2;
            for (i2 = 0; i2 < j; ++i2) {
                target = mtx + 4 * i2 + j;
                sum = matrix0[target];
                int k2 = i2;
                int p12 = mtx + 4 * i2;
                p2 = mtx + j;
                while (k2-- != 0) {
                    sum -= matrix0[p12] * matrix0[p2];
                    ++p12;
                    p2 += 4;
                }
                matrix0[target] = sum;
            }
            double big = 0.0;
            int imax = -1;
            for (i2 = j; i2 < 4; ++i2) {
                double d;
                target = mtx + 4 * i2 + j;
                sum = matrix0[target];
                k = j;
                p1 = mtx + 4 * i2;
                p2 = mtx + j;
                while (k-- != 0) {
                    sum -= matrix0[p1] * matrix0[p2];
                    ++p1;
                    p2 += 4;
                }
                matrix0[target] = sum;
                temp = row_scale[i2] * Math.abs(sum);
                if (!(d >= big)) continue;
                big = temp;
                imax = i2;
            }
            if (imax < 0) {
                throw new RuntimeException(VecMathI18N.getString("Matrix4f13"));
            }
            if (j != imax) {
                k = 4;
                p1 = mtx + 4 * imax;
                p2 = mtx + 4 * j;
                while (k-- != 0) {
                    temp = matrix0[p1];
                    matrix0[p1++] = matrix0[p2];
                    matrix0[p2++] = temp;
                }
                row_scale[imax] = row_scale[j];
            }
            row_perm[j] = imax;
            if (matrix0[mtx + 4 * j + j] == 0.0) {
                return false;
            }
            if (j == 3) continue;
            temp = 1.0 / matrix0[mtx + 4 * j + j];
            target = mtx + 4 * (j + 1) + j;
            i2 = 3 - j;
            while (i2-- != 0) {
                int n = target;
                matrix0[n] = matrix0[n] * temp;
                target += 4;
            }
        }
        return true;
    }

    static void luBacksubstitution(double[] matrix1, int[] row_perm, double[] matrix2) {
        int rp = 0;
        for (int k = 0; k < 4; ++k) {
            int rv;
            int cv = k;
            int ii = -1;
            for (int i = 0; i < 4; ++i) {
                int ip = row_perm[rp + i];
                double sum = matrix2[cv + 4 * ip];
                matrix2[cv + 4 * ip] = matrix2[cv + 4 * i];
                if (ii >= 0) {
                    rv = i * 4;
                    for (int j = ii; j <= i - 1; ++j) {
                        sum -= matrix1[rv + j] * matrix2[cv + 4 * j];
                    }
                } else if (sum != 0.0) {
                    ii = i;
                }
                matrix2[cv + 4 * i] = sum;
            }
            rv = 12;
            int n = cv + 12;
            matrix2[n] = matrix2[n] / matrix1[rv + 3];
            matrix2[cv + 8] = (matrix2[cv + 8] - matrix1[(rv -= 4) + 3] * matrix2[cv + 12]) / matrix1[rv + 2];
            matrix2[cv + 4] = (matrix2[cv + 4] - matrix1[(rv -= 4) + 2] * matrix2[cv + 8] - matrix1[rv + 3] * matrix2[cv + 12]) / matrix1[rv + 1];
            matrix2[cv + 0] = (matrix2[cv + 0] - matrix1[(rv -= 4) + 1] * matrix2[cv + 4] - matrix1[rv + 2] * matrix2[cv + 8] - matrix1[rv + 3] * matrix2[cv + 12]) / matrix1[rv + 0];
        }
    }

    public final float determinant() {
        float det = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
        det -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
        det += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
        return det -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
    }

    public final void set(Matrix3f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = 0.0f;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = 0.0f;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Matrix3d m1) {
        this.m00 = (float)m1.m00;
        this.m01 = (float)m1.m01;
        this.m02 = (float)m1.m02;
        this.m03 = 0.0f;
        this.m10 = (float)m1.m10;
        this.m11 = (float)m1.m11;
        this.m12 = (float)m1.m12;
        this.m13 = 0.0f;
        this.m20 = (float)m1.m20;
        this.m21 = (float)m1.m21;
        this.m22 = (float)m1.m22;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(float scale) {
        this.m00 = scale;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = scale;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = scale;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(float[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m03 = m[3];
        this.m10 = m[4];
        this.m11 = m[5];
        this.m12 = m[6];
        this.m13 = m[7];
        this.m20 = m[8];
        this.m21 = m[9];
        this.m22 = m[10];
        this.m23 = m[11];
        this.m30 = m[12];
        this.m31 = m[13];
        this.m32 = m[14];
        this.m33 = m[15];
    }

    public final void set(Vector3f v1) {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = v1.x;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = v1.y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = v1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(float scale, Vector3f t1) {
        this.m00 = scale;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = t1.x;
        this.m10 = 0.0f;
        this.m11 = scale;
        this.m12 = 0.0f;
        this.m13 = t1.y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = scale;
        this.m23 = t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Vector3f t1, float scale) {
        this.m00 = scale;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = scale * t1.x;
        this.m10 = 0.0f;
        this.m11 = scale;
        this.m12 = 0.0f;
        this.m13 = scale * t1.y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = scale;
        this.m23 = scale * t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Matrix3f m1, Vector3f t1, float scale) {
        this.m00 = m1.m00 * scale;
        this.m01 = m1.m01 * scale;
        this.m02 = m1.m02 * scale;
        this.m03 = t1.x;
        this.m10 = m1.m10 * scale;
        this.m11 = m1.m11 * scale;
        this.m12 = m1.m12 * scale;
        this.m13 = t1.y;
        this.m20 = m1.m20 * scale;
        this.m21 = m1.m21 * scale;
        this.m22 = m1.m22 * scale;
        this.m23 = t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void set(Matrix3d m1, Vector3d t1, double scale) {
        this.m00 = (float)(m1.m00 * scale);
        this.m01 = (float)(m1.m01 * scale);
        this.m02 = (float)(m1.m02 * scale);
        this.m03 = (float)t1.x;
        this.m10 = (float)(m1.m10 * scale);
        this.m11 = (float)(m1.m11 * scale);
        this.m12 = (float)(m1.m12 * scale);
        this.m13 = (float)t1.y;
        this.m20 = (float)(m1.m20 * scale);
        this.m21 = (float)(m1.m21 * scale);
        this.m22 = (float)(m1.m22 * scale);
        this.m23 = (float)t1.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void setTranslation(Vector3f trans) {
        this.m03 = trans.x;
        this.m13 = trans.y;
        this.m23 = trans.z;
    }

    public final void rotX(float angle) {
        float sinAngle = (float)Math.sin(angle);
        float cosAngle = (float)Math.cos(angle);
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = cosAngle;
        this.m12 = -sinAngle;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = sinAngle;
        this.m22 = cosAngle;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void rotY(float angle) {
        float cosAngle;
        float sinAngle = (float)Math.sin(angle);
        this.m00 = cosAngle = (float)Math.cos(angle);
        this.m01 = 0.0f;
        this.m02 = sinAngle;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = -sinAngle;
        this.m21 = 0.0f;
        this.m22 = cosAngle;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void rotZ(float angle) {
        float cosAngle;
        float sinAngle = (float)Math.sin(angle);
        this.m00 = cosAngle = (float)Math.cos(angle);
        this.m01 = -sinAngle;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = sinAngle;
        this.m11 = cosAngle;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public final void mul(float scalar) {
        this.m00 *= scalar;
        this.m01 *= scalar;
        this.m02 *= scalar;
        this.m03 *= scalar;
        this.m10 *= scalar;
        this.m11 *= scalar;
        this.m12 *= scalar;
        this.m13 *= scalar;
        this.m20 *= scalar;
        this.m21 *= scalar;
        this.m22 *= scalar;
        this.m23 *= scalar;
        this.m30 *= scalar;
        this.m31 *= scalar;
        this.m32 *= scalar;
        this.m33 *= scalar;
    }

    public final void mul(float scalar, Matrix4f m1) {
        this.m00 = m1.m00 * scalar;
        this.m01 = m1.m01 * scalar;
        this.m02 = m1.m02 * scalar;
        this.m03 = m1.m03 * scalar;
        this.m10 = m1.m10 * scalar;
        this.m11 = m1.m11 * scalar;
        this.m12 = m1.m12 * scalar;
        this.m13 = m1.m13 * scalar;
        this.m20 = m1.m20 * scalar;
        this.m21 = m1.m21 * scalar;
        this.m22 = m1.m22 * scalar;
        this.m23 = m1.m23 * scalar;
        this.m30 = m1.m30 * scalar;
        this.m31 = m1.m31 * scalar;
        this.m32 = m1.m32 * scalar;
        this.m33 = m1.m33 * scalar;
    }

    public final void mul(Matrix4f m1) {
        float m00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20 + this.m03 * m1.m30;
        float m01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21 + this.m03 * m1.m31;
        float m02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22 + this.m03 * m1.m32;
        float m03 = this.m00 * m1.m03 + this.m01 * m1.m13 + this.m02 * m1.m23 + this.m03 * m1.m33;
        float m10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20 + this.m13 * m1.m30;
        float m11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21 + this.m13 * m1.m31;
        float m12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22 + this.m13 * m1.m32;
        float m13 = this.m10 * m1.m03 + this.m11 * m1.m13 + this.m12 * m1.m23 + this.m13 * m1.m33;
        float m20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20 + this.m23 * m1.m30;
        float m21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21 + this.m23 * m1.m31;
        float m22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22 + this.m23 * m1.m32;
        float m23 = this.m20 * m1.m03 + this.m21 * m1.m13 + this.m22 * m1.m23 + this.m23 * m1.m33;
        float m30 = this.m30 * m1.m00 + this.m31 * m1.m10 + this.m32 * m1.m20 + this.m33 * m1.m30;
        float m31 = this.m30 * m1.m01 + this.m31 * m1.m11 + this.m32 * m1.m21 + this.m33 * m1.m31;
        float m32 = this.m30 * m1.m02 + this.m31 * m1.m12 + this.m32 * m1.m22 + this.m33 * m1.m32;
        float m33 = this.m30 * m1.m03 + this.m31 * m1.m13 + this.m32 * m1.m23 + this.m33 * m1.m33;
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    public final void mul(Matrix4f m1, Matrix4f m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
            this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
            this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
            this.m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;
            this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
            this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
            this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
            this.m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;
            this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
            this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
            this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
            this.m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;
            this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
            this.m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
            this.m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
            this.m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;
        } else {
            float m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
            float m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
            float m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
            float m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;
            float m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
            float m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
            float m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
            float m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;
            float m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
            float m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
            float m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
            float m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;
            float m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
            float m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
            float m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
            float m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;
            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }
    }

    public final void mulTransposeBoth(Matrix4f m1, Matrix4f m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02 + m1.m30 * m2.m03;
            this.m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12 + m1.m30 * m2.m13;
            this.m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22 + m1.m30 * m2.m23;
            this.m03 = m1.m00 * m2.m30 + m1.m10 * m2.m31 + m1.m20 * m2.m32 + m1.m30 * m2.m33;
            this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02 + m1.m31 * m2.m03;
            this.m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12 + m1.m31 * m2.m13;
            this.m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22 + m1.m31 * m2.m23;
            this.m13 = m1.m01 * m2.m30 + m1.m11 * m2.m31 + m1.m21 * m2.m32 + m1.m31 * m2.m33;
            this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02 + m1.m32 * m2.m03;
            this.m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12 + m1.m32 * m2.m13;
            this.m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22 + m1.m32 * m2.m23;
            this.m23 = m1.m02 * m2.m30 + m1.m12 * m2.m31 + m1.m22 * m2.m32 + m1.m32 * m2.m33;
            this.m30 = m1.m03 * m2.m00 + m1.m13 * m2.m01 + m1.m23 * m2.m02 + m1.m33 * m2.m03;
            this.m31 = m1.m03 * m2.m10 + m1.m13 * m2.m11 + m1.m23 * m2.m12 + m1.m33 * m2.m13;
            this.m32 = m1.m03 * m2.m20 + m1.m13 * m2.m21 + m1.m23 * m2.m22 + m1.m33 * m2.m23;
            this.m33 = m1.m03 * m2.m30 + m1.m13 * m2.m31 + m1.m23 * m2.m32 + m1.m33 * m2.m33;
        } else {
            float m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02 + m1.m30 * m2.m03;
            float m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12 + m1.m30 * m2.m13;
            float m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22 + m1.m30 * m2.m23;
            float m03 = m1.m00 * m2.m30 + m1.m10 * m2.m31 + m1.m20 * m2.m32 + m1.m30 * m2.m33;
            float m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02 + m1.m31 * m2.m03;
            float m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12 + m1.m31 * m2.m13;
            float m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22 + m1.m31 * m2.m23;
            float m13 = m1.m01 * m2.m30 + m1.m11 * m2.m31 + m1.m21 * m2.m32 + m1.m31 * m2.m33;
            float m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02 + m1.m32 * m2.m03;
            float m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12 + m1.m32 * m2.m13;
            float m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22 + m1.m32 * m2.m23;
            float m23 = m1.m02 * m2.m30 + m1.m12 * m2.m31 + m1.m22 * m2.m32 + m1.m32 * m2.m33;
            float m30 = m1.m03 * m2.m00 + m1.m13 * m2.m01 + m1.m23 * m2.m02 + m1.m33 * m2.m03;
            float m31 = m1.m03 * m2.m10 + m1.m13 * m2.m11 + m1.m23 * m2.m12 + m1.m33 * m2.m13;
            float m32 = m1.m03 * m2.m20 + m1.m13 * m2.m21 + m1.m23 * m2.m22 + m1.m33 * m2.m23;
            float m33 = m1.m03 * m2.m30 + m1.m13 * m2.m31 + m1.m23 * m2.m32 + m1.m33 * m2.m33;
            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }
    }

    public final void mulTransposeRight(Matrix4f m1, Matrix4f m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03;
            this.m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13;
            this.m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23;
            this.m03 = m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33;
            this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03;
            this.m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13;
            this.m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23;
            this.m13 = m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33;
            this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03;
            this.m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13;
            this.m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23;
            this.m23 = m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33;
            this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03;
            this.m31 = m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13;
            this.m32 = m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23;
            this.m33 = m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33;
        } else {
            float m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02 + m1.m03 * m2.m03;
            float m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12 + m1.m03 * m2.m13;
            float m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22 + m1.m03 * m2.m23;
            float m03 = m1.m00 * m2.m30 + m1.m01 * m2.m31 + m1.m02 * m2.m32 + m1.m03 * m2.m33;
            float m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02 + m1.m13 * m2.m03;
            float m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12 + m1.m13 * m2.m13;
            float m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22 + m1.m13 * m2.m23;
            float m13 = m1.m10 * m2.m30 + m1.m11 * m2.m31 + m1.m12 * m2.m32 + m1.m13 * m2.m33;
            float m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02 + m1.m23 * m2.m03;
            float m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12 + m1.m23 * m2.m13;
            float m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22 + m1.m23 * m2.m23;
            float m23 = m1.m20 * m2.m30 + m1.m21 * m2.m31 + m1.m22 * m2.m32 + m1.m23 * m2.m33;
            float m30 = m1.m30 * m2.m00 + m1.m31 * m2.m01 + m1.m32 * m2.m02 + m1.m33 * m2.m03;
            float m31 = m1.m30 * m2.m10 + m1.m31 * m2.m11 + m1.m32 * m2.m12 + m1.m33 * m2.m13;
            float m32 = m1.m30 * m2.m20 + m1.m31 * m2.m21 + m1.m32 * m2.m22 + m1.m33 * m2.m23;
            float m33 = m1.m30 * m2.m30 + m1.m31 * m2.m31 + m1.m32 * m2.m32 + m1.m33 * m2.m33;
            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }
    }

    public final void mulTransposeLeft(Matrix4f m1, Matrix4f m2) {
        if (this != m1 && this != m2) {
            this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30;
            this.m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31;
            this.m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32;
            this.m03 = m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33;
            this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30;
            this.m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31;
            this.m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32;
            this.m13 = m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33;
            this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30;
            this.m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31;
            this.m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32;
            this.m23 = m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33;
            this.m30 = m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30;
            this.m31 = m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31;
            this.m32 = m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32;
            this.m33 = m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33;
        } else {
            float m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20 + m1.m30 * m2.m30;
            float m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21 + m1.m30 * m2.m31;
            float m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22 + m1.m30 * m2.m32;
            float m03 = m1.m00 * m2.m03 + m1.m10 * m2.m13 + m1.m20 * m2.m23 + m1.m30 * m2.m33;
            float m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20 + m1.m31 * m2.m30;
            float m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21 + m1.m31 * m2.m31;
            float m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22 + m1.m31 * m2.m32;
            float m13 = m1.m01 * m2.m03 + m1.m11 * m2.m13 + m1.m21 * m2.m23 + m1.m31 * m2.m33;
            float m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20 + m1.m32 * m2.m30;
            float m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21 + m1.m32 * m2.m31;
            float m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22 + m1.m32 * m2.m32;
            float m23 = m1.m02 * m2.m03 + m1.m12 * m2.m13 + m1.m22 * m2.m23 + m1.m32 * m2.m33;
            float m30 = m1.m03 * m2.m00 + m1.m13 * m2.m10 + m1.m23 * m2.m20 + m1.m33 * m2.m30;
            float m31 = m1.m03 * m2.m01 + m1.m13 * m2.m11 + m1.m23 * m2.m21 + m1.m33 * m2.m31;
            float m32 = m1.m03 * m2.m02 + m1.m13 * m2.m12 + m1.m23 * m2.m22 + m1.m33 * m2.m32;
            float m33 = m1.m03 * m2.m03 + m1.m13 * m2.m13 + m1.m23 * m2.m23 + m1.m33 * m2.m33;
            this.m00 = m00;
            this.m01 = m01;
            this.m02 = m02;
            this.m03 = m03;
            this.m10 = m10;
            this.m11 = m11;
            this.m12 = m12;
            this.m13 = m13;
            this.m20 = m20;
            this.m21 = m21;
            this.m22 = m22;
            this.m23 = m23;
            this.m30 = m30;
            this.m31 = m31;
            this.m32 = m32;
            this.m33 = m33;
        }
    }

    public boolean equals(Matrix4f m1) {
        try {
            return this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02 && this.m03 == m1.m03 && this.m10 == m1.m10 && this.m11 == m1.m11 && this.m12 == m1.m12 && this.m13 == m1.m13 && this.m20 == m1.m20 && this.m21 == m1.m21 && this.m22 == m1.m22 && this.m23 == m1.m23 && this.m30 == m1.m30 && this.m31 == m1.m31 && this.m32 == m1.m32 && this.m33 == m1.m33;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object t1) {
        try {
            Matrix4f m2 = (Matrix4f)t1;
            return this.m00 == m2.m00 && this.m01 == m2.m01 && this.m02 == m2.m02 && this.m03 == m2.m03 && this.m10 == m2.m10 && this.m11 == m2.m11 && this.m12 == m2.m12 && this.m13 == m2.m13 && this.m20 == m2.m20 && this.m21 == m2.m21 && this.m22 == m2.m22 && this.m23 == m2.m23 && this.m30 == m2.m30 && this.m31 == m2.m31 && this.m32 == m2.m32 && this.m33 == m2.m33;
        }
        catch (ClassCastException e1) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean epsilonEquals(Matrix4f m1, float epsilon) {
        boolean status = true;
        if (Math.abs(this.m00 - m1.m00) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m01 - m1.m01) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m02 - m1.m02) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m03 - m1.m03) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m10 - m1.m10) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m11 - m1.m11) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m12 - m1.m12) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m13 - m1.m13) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m20 - m1.m20) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m21 - m1.m21) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m22 - m1.m22) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m23 - m1.m23) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m30 - m1.m30) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m31 - m1.m31) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m32 - m1.m32) > epsilon) {
            status = false;
        }
        if (Math.abs(this.m33 - m1.m33) > epsilon) {
            status = false;
        }
        return status;
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashFloatBits(bits, this.m00);
        bits = VecMathUtil.hashFloatBits(bits, this.m01);
        bits = VecMathUtil.hashFloatBits(bits, this.m02);
        bits = VecMathUtil.hashFloatBits(bits, this.m03);
        bits = VecMathUtil.hashFloatBits(bits, this.m10);
        bits = VecMathUtil.hashFloatBits(bits, this.m11);
        bits = VecMathUtil.hashFloatBits(bits, this.m12);
        bits = VecMathUtil.hashFloatBits(bits, this.m13);
        bits = VecMathUtil.hashFloatBits(bits, this.m20);
        bits = VecMathUtil.hashFloatBits(bits, this.m21);
        bits = VecMathUtil.hashFloatBits(bits, this.m22);
        bits = VecMathUtil.hashFloatBits(bits, this.m23);
        bits = VecMathUtil.hashFloatBits(bits, this.m30);
        bits = VecMathUtil.hashFloatBits(bits, this.m31);
        bits = VecMathUtil.hashFloatBits(bits, this.m32);
        bits = VecMathUtil.hashFloatBits(bits, this.m33);
        return VecMathUtil.hashFinish(bits);
    }

    public final void transform(Tuple4f vec, Tuple4f vecOut) {
        float x = this.m00 * vec.x + this.m01 * vec.y + this.m02 * vec.z + this.m03 * vec.w;
        float y = this.m10 * vec.x + this.m11 * vec.y + this.m12 * vec.z + this.m13 * vec.w;
        float z = this.m20 * vec.x + this.m21 * vec.y + this.m22 * vec.z + this.m23 * vec.w;
        vecOut.w = this.m30 * vec.x + this.m31 * vec.y + this.m32 * vec.z + this.m33 * vec.w;
        vecOut.x = x;
        vecOut.y = y;
        vecOut.z = z;
    }

    public final void transform(Tuple4f vec) {
        float x = this.m00 * vec.x + this.m01 * vec.y + this.m02 * vec.z + this.m03 * vec.w;
        float y = this.m10 * vec.x + this.m11 * vec.y + this.m12 * vec.z + this.m13 * vec.w;
        float z = this.m20 * vec.x + this.m21 * vec.y + this.m22 * vec.z + this.m23 * vec.w;
        vec.w = this.m30 * vec.x + this.m31 * vec.y + this.m32 * vec.z + this.m33 * vec.w;
        vec.x = x;
        vec.y = y;
        vec.z = z;
    }

    public final void transform(Point3f point, Point3f pointOut) {
        float x = this.m00 * point.x + this.m01 * point.y + this.m02 * point.z + this.m03;
        float y = this.m10 * point.x + this.m11 * point.y + this.m12 * point.z + this.m13;
        pointOut.z = this.m20 * point.x + this.m21 * point.y + this.m22 * point.z + this.m23;
        pointOut.x = x;
        pointOut.y = y;
    }

    public final void transform(Point3f point) {
        float x = this.m00 * point.x + this.m01 * point.y + this.m02 * point.z + this.m03;
        float y = this.m10 * point.x + this.m11 * point.y + this.m12 * point.z + this.m13;
        point.z = this.m20 * point.x + this.m21 * point.y + this.m22 * point.z + this.m23;
        point.x = x;
        point.y = y;
    }

    public final void transform(Vector3f normal, Vector3f normalOut) {
        float x = this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z;
        float y = this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z;
        normalOut.z = this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z;
        normalOut.x = x;
        normalOut.y = y;
    }

    public final void transform(Vector3f normal) {
        float x = this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z;
        float y = this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z;
        normal.z = this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z;
        normal.x = x;
        normal.y = y;
    }

    public final void setRotation(Matrix3d m1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        this.m00 = (float)(m1.m00 * tmp_scale[0]);
        this.m01 = (float)(m1.m01 * tmp_scale[1]);
        this.m02 = (float)(m1.m02 * tmp_scale[2]);
        this.m10 = (float)(m1.m10 * tmp_scale[0]);
        this.m11 = (float)(m1.m11 * tmp_scale[1]);
        this.m12 = (float)(m1.m12 * tmp_scale[2]);
        this.m20 = (float)(m1.m20 * tmp_scale[0]);
        this.m21 = (float)(m1.m21 * tmp_scale[1]);
        this.m22 = (float)(m1.m22 * tmp_scale[2]);
    }

    public final void setRotation(Matrix3f m1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        this.m00 = (float)((double)m1.m00 * tmp_scale[0]);
        this.m01 = (float)((double)m1.m01 * tmp_scale[1]);
        this.m02 = (float)((double)m1.m02 * tmp_scale[2]);
        this.m10 = (float)((double)m1.m10 * tmp_scale[0]);
        this.m11 = (float)((double)m1.m11 * tmp_scale[1]);
        this.m12 = (float)((double)m1.m12 * tmp_scale[2]);
        this.m20 = (float)((double)m1.m20 * tmp_scale[0]);
        this.m21 = (float)((double)m1.m21 * tmp_scale[1]);
        this.m22 = (float)((double)m1.m22 * tmp_scale[2]);
    }

    public final void setRotation(Quat4f q1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        this.m00 = (float)((double)(1.0f - 2.0f * q1.y * q1.y - 2.0f * q1.z * q1.z) * tmp_scale[0]);
        this.m10 = (float)((double)(2.0f * (q1.x * q1.y + q1.w * q1.z)) * tmp_scale[0]);
        this.m20 = (float)((double)(2.0f * (q1.x * q1.z - q1.w * q1.y)) * tmp_scale[0]);
        this.m01 = (float)((double)(2.0f * (q1.x * q1.y - q1.w * q1.z)) * tmp_scale[1]);
        this.m11 = (float)((double)(1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.z * q1.z) * tmp_scale[1]);
        this.m21 = (float)((double)(2.0f * (q1.y * q1.z + q1.w * q1.x)) * tmp_scale[1]);
        this.m02 = (float)((double)(2.0f * (q1.x * q1.z + q1.w * q1.y)) * tmp_scale[2]);
        this.m12 = (float)((double)(2.0f * (q1.y * q1.z - q1.w * q1.x)) * tmp_scale[2]);
        this.m22 = (float)((double)(1.0f - 2.0f * q1.x * q1.x - 2.0f * q1.y * q1.y) * tmp_scale[2]);
    }

    public final void setRotation(Quat4d q1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        this.m00 = (float)((1.0 - 2.0 * q1.y * q1.y - 2.0 * q1.z * q1.z) * tmp_scale[0]);
        this.m10 = (float)(2.0 * (q1.x * q1.y + q1.w * q1.z) * tmp_scale[0]);
        this.m20 = (float)(2.0 * (q1.x * q1.z - q1.w * q1.y) * tmp_scale[0]);
        this.m01 = (float)(2.0 * (q1.x * q1.y - q1.w * q1.z) * tmp_scale[1]);
        this.m11 = (float)((1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.z * q1.z) * tmp_scale[1]);
        this.m21 = (float)(2.0 * (q1.y * q1.z + q1.w * q1.x) * tmp_scale[1]);
        this.m02 = (float)(2.0 * (q1.x * q1.z + q1.w * q1.y) * tmp_scale[2]);
        this.m12 = (float)(2.0 * (q1.y * q1.z - q1.w * q1.x) * tmp_scale[2]);
        this.m22 = (float)((1.0 - 2.0 * q1.x * q1.x - 2.0 * q1.y * q1.y) * tmp_scale[2]);
    }

    public final void setRotation(AxisAngle4f a1) {
        double[] tmp_rot = new double[9];
        double[] tmp_scale = new double[3];
        this.getScaleRotate(tmp_scale, tmp_rot);
        double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);
        if (mag < 1.0E-8) {
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m02 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
            this.m12 = 0.0f;
            this.m20 = 0.0f;
            this.m21 = 0.0f;
            this.m22 = 1.0f;
        } else {
            mag = 1.0 / mag;
            double ax = (double)a1.x * mag;
            double ay = (double)a1.y * mag;
            double az = (double)a1.z * mag;
            double sinTheta = Math.sin(a1.angle);
            double cosTheta = Math.cos(a1.angle);
            double t = 1.0 - cosTheta;
            double xz = a1.x * a1.z;
            double xy = a1.x * a1.y;
            double yz = a1.y * a1.z;
            this.m00 = (float)((t * ax * ax + cosTheta) * tmp_scale[0]);
            this.m01 = (float)((t * xy - sinTheta * az) * tmp_scale[1]);
            this.m02 = (float)((t * xz + sinTheta * ay) * tmp_scale[2]);
            this.m10 = (float)((t * xy + sinTheta * az) * tmp_scale[0]);
            this.m11 = (float)((t * ay * ay + cosTheta) * tmp_scale[1]);
            this.m12 = (float)((t * yz - sinTheta * ax) * tmp_scale[2]);
            this.m20 = (float)((t * xz - sinTheta * ay) * tmp_scale[0]);
            this.m21 = (float)((t * yz + sinTheta * ax) * tmp_scale[1]);
            this.m22 = (float)((t * az * az + cosTheta) * tmp_scale[2]);
        }
    }

    public final void setZero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 0.0f;
    }

    public final void negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m01;
        this.m02 = -this.m02;
        this.m03 = -this.m03;
        this.m10 = -this.m10;
        this.m11 = -this.m11;
        this.m12 = -this.m12;
        this.m13 = -this.m13;
        this.m20 = -this.m20;
        this.m21 = -this.m21;
        this.m22 = -this.m22;
        this.m23 = -this.m23;
        this.m30 = -this.m30;
        this.m31 = -this.m31;
        this.m32 = -this.m32;
        this.m33 = -this.m33;
    }

    public final void negate(Matrix4f m1) {
        this.m00 = -m1.m00;
        this.m01 = -m1.m01;
        this.m02 = -m1.m02;
        this.m03 = -m1.m03;
        this.m10 = -m1.m10;
        this.m11 = -m1.m11;
        this.m12 = -m1.m12;
        this.m13 = -m1.m13;
        this.m20 = -m1.m20;
        this.m21 = -m1.m21;
        this.m22 = -m1.m22;
        this.m23 = -m1.m23;
        this.m30 = -m1.m30;
        this.m31 = -m1.m31;
        this.m32 = -m1.m32;
        this.m33 = -m1.m33;
    }

    private final void getScaleRotate(double[] scales, double[] rots) {
        double[] tmp = new double[]{this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22};
        Matrix3d.compute_svd(tmp, scales, rots);
    }

    public Object clone() {
        Matrix4f m1 = null;
        try {
            m1 = (Matrix4f)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return m1;
    }

    public final float getM00() {
        return this.m00;
    }

    public final void setM00(float m00) {
        this.m00 = m00;
    }

    public final float getM01() {
        return this.m01;
    }

    public final void setM01(float m01) {
        this.m01 = m01;
    }

    public final float getM02() {
        return this.m02;
    }

    public final void setM02(float m02) {
        this.m02 = m02;
    }

    public final float getM10() {
        return this.m10;
    }

    public final void setM10(float m10) {
        this.m10 = m10;
    }

    public final float getM11() {
        return this.m11;
    }

    public final void setM11(float m11) {
        this.m11 = m11;
    }

    public final float getM12() {
        return this.m12;
    }

    public final void setM12(float m12) {
        this.m12 = m12;
    }

    public final float getM20() {
        return this.m20;
    }

    public final void setM20(float m20) {
        this.m20 = m20;
    }

    public final float getM21() {
        return this.m21;
    }

    public final void setM21(float m21) {
        this.m21 = m21;
    }

    public final float getM22() {
        return this.m22;
    }

    public final void setM22(float m22) {
        this.m22 = m22;
    }

    public final float getM03() {
        return this.m03;
    }

    public final void setM03(float m03) {
        this.m03 = m03;
    }

    public final float getM13() {
        return this.m13;
    }

    public final void setM13(float m13) {
        this.m13 = m13;
    }

    public final float getM23() {
        return this.m23;
    }

    public final void setM23(float m23) {
        this.m23 = m23;
    }

    public final float getM30() {
        return this.m30;
    }

    public final void setM30(float m30) {
        this.m30 = m30;
    }

    public final float getM31() {
        return this.m31;
    }

    public final void setM31(float m31) {
        this.m31 = m31;
    }

    public final float getM32() {
        return this.m32;
    }

    public final void setM32(float m32) {
        this.m32 = m32;
    }

    public final float getM33() {
        return this.m33;
    }

    public final void setM33(float m33) {
        this.m33 = m33;
    }
}

