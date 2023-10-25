/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.CanTransformVec3d;
import com.sun.javafx.geom.transform.SingularMatrixException;
import com.sun.javafx.geom.transform.TransformHelper;

public final class GeneralTransform3D
implements CanTransformVec3d {
    protected double[] mat = new double[16];
    private boolean identity;
    private Vec3d tempV3d;
    private static final double EPSILON_ABSOLUTE = 1.0E-5;

    public GeneralTransform3D() {
        this.setIdentity();
    }

    public boolean isAffine() {
        return !this.isInfOrNaN() && GeneralTransform3D.almostZero(this.mat[12]) && GeneralTransform3D.almostZero(this.mat[13]) && GeneralTransform3D.almostZero(this.mat[14]) && GeneralTransform3D.almostOne(this.mat[15]);
    }

    public GeneralTransform3D set(GeneralTransform3D generalTransform3D) {
        System.arraycopy(generalTransform3D.mat, 0, this.mat, 0, this.mat.length);
        this.updateState();
        return this;
    }

    public GeneralTransform3D set(double[] arrd) {
        System.arraycopy(arrd, 0, this.mat, 0, this.mat.length);
        this.updateState();
        return this;
    }

    public double[] get(double[] arrd) {
        if (arrd == null) {
            arrd = new double[this.mat.length];
        }
        System.arraycopy(this.mat, 0, arrd, 0, this.mat.length);
        return arrd;
    }

    public double get(int n) {
        assert (n >= 0 && n < this.mat.length);
        return this.mat[n];
    }

    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (this.tempV3d == null) {
            this.tempV3d = new Vec3d();
        }
        return TransformHelper.general3dBoundsTransform(this, baseBounds, baseBounds2, this.tempV3d);
    }

    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D();
        }
        double d = this.mat[12] * (double)point2D.x + this.mat[13] * (double)point2D.y + this.mat[15];
        float f = (float)(this.mat[0] * (double)point2D.x + this.mat[1] * (double)point2D.y + this.mat[3]);
        point2D2.y = (float)(this.mat[4] * (double)point2D.x + this.mat[5] * (double)point2D.y + this.mat[7]);
        point2D2.x = f;
        if (d != 0.0) {
            point2D2.x = (float)((double)point2D2.x / d);
            point2D2.y = (float)((double)point2D2.y / d);
        }
        return point2D2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = this.mat[12] * vec3d.x + this.mat[13] * vec3d.y + this.mat[14] * vec3d.z + this.mat[15];
        double d2 = this.mat[0] * vec3d.x + this.mat[1] * vec3d.y + this.mat[2] * vec3d.z + this.mat[3];
        double d3 = this.mat[4] * vec3d.x + this.mat[5] * vec3d.y + this.mat[6] * vec3d.z + this.mat[7];
        vec3d2.z = this.mat[8] * vec3d.x + this.mat[9] * vec3d.y + this.mat[10] * vec3d.z + this.mat[11];
        vec3d2.x = d2;
        vec3d2.y = d3;
        if (d != 0.0) {
            vec3d2.x /= d;
            vec3d2.y /= d;
            vec3d2.z /= d;
        }
        return vec3d2;
    }

    public Vec3d transform(Vec3d vec3d) {
        return this.transform(vec3d, vec3d);
    }

    public Vec3f transformNormal(Vec3f vec3f, Vec3f vec3f2) {
        if (vec3f2 == null) {
            vec3f2 = new Vec3f();
        }
        float f = (float)(this.mat[0] * (double)vec3f.x + this.mat[1] * (double)vec3f.y + this.mat[2] * (double)vec3f.z);
        float f2 = (float)(this.mat[4] * (double)vec3f.x + this.mat[5] * (double)vec3f.y + this.mat[6] * (double)vec3f.z);
        vec3f2.z = (float)(this.mat[8] * (double)vec3f.x + this.mat[9] * (double)vec3f.y + this.mat[10] * (double)vec3f.z);
        vec3f2.x = f;
        vec3f2.y = f2;
        return vec3f2;
    }

    public Vec3f transformNormal(Vec3f vec3f) {
        return this.transformNormal(vec3f, vec3f);
    }

    public GeneralTransform3D perspective(boolean bl, double d, double d2, double d3, double d4) {
        double d5 = d * 0.5;
        double d6 = d4 - d3;
        double d7 = Math.sin(d5);
        double d8 = Math.cos(d5) / d7;
        this.mat[0] = bl ? d8 / d2 : d8;
        this.mat[5] = bl ? d8 : d8 * d2;
        this.mat[10] = -(d4 + d3) / d6;
        this.mat[11] = -2.0 * d3 * d4 / d6;
        this.mat[14] = -1.0;
        this.mat[15] = 0.0;
        this.mat[13] = 0.0;
        this.mat[12] = 0.0;
        this.mat[9] = 0.0;
        this.mat[8] = 0.0;
        this.mat[7] = 0.0;
        this.mat[6] = 0.0;
        this.mat[4] = 0.0;
        this.mat[3] = 0.0;
        this.mat[2] = 0.0;
        this.mat[1] = 0.0;
        this.updateState();
        return this;
    }

    public GeneralTransform3D ortho(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = 1.0 / (d2 - d);
        double d8 = 1.0 / (d4 - d3);
        double d9 = 1.0 / (d6 - d5);
        this.mat[0] = 2.0 * d7;
        this.mat[3] = -(d2 + d) * d7;
        this.mat[5] = 2.0 * d8;
        this.mat[7] = -(d4 + d3) * d8;
        this.mat[10] = 2.0 * d9;
        this.mat[11] = (d6 + d5) * d9;
        this.mat[14] = 0.0;
        this.mat[13] = 0.0;
        this.mat[12] = 0.0;
        this.mat[9] = 0.0;
        this.mat[8] = 0.0;
        this.mat[6] = 0.0;
        this.mat[4] = 0.0;
        this.mat[2] = 0.0;
        this.mat[1] = 0.0;
        this.mat[15] = 1.0;
        this.updateState();
        return this;
    }

    public double computeClipZCoord() {
        double d = (1.0 - this.mat[15]) / this.mat[14];
        double d2 = this.mat[10] * d + this.mat[11];
        return d2;
    }

    public double determinant() {
        return this.mat[0] * (this.mat[5] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) - this.mat[6] * (this.mat[9] * this.mat[15] - this.mat[11] * this.mat[13]) + this.mat[7] * (this.mat[9] * this.mat[14] - this.mat[10] * this.mat[13])) - this.mat[1] * (this.mat[4] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) - this.mat[6] * (this.mat[8] * this.mat[15] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[8] * this.mat[14] - this.mat[10] * this.mat[12])) + this.mat[2] * (this.mat[4] * (this.mat[9] * this.mat[15] - this.mat[11] * this.mat[13]) - this.mat[5] * (this.mat[8] * this.mat[15] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[8] * this.mat[13] - this.mat[9] * this.mat[12])) - this.mat[3] * (this.mat[4] * (this.mat[9] * this.mat[14] - this.mat[10] * this.mat[13]) - this.mat[5] * (this.mat[8] * this.mat[14] - this.mat[10] * this.mat[12]) + this.mat[6] * (this.mat[8] * this.mat[13] - this.mat[9] * this.mat[12]));
    }

    public GeneralTransform3D invert() {
        return this.invert(this);
    }

    private GeneralTransform3D invert(GeneralTransform3D generalTransform3D) {
        double[] arrd = new double[16];
        int[] arrn = new int[4];
        System.arraycopy(generalTransform3D.mat, 0, arrd, 0, arrd.length);
        if (!GeneralTransform3D.luDecomposition(arrd, arrn)) {
            throw new SingularMatrixException();
        }
        this.mat[0] = 1.0;
        this.mat[1] = 0.0;
        this.mat[2] = 0.0;
        this.mat[3] = 0.0;
        this.mat[4] = 0.0;
        this.mat[5] = 1.0;
        this.mat[6] = 0.0;
        this.mat[7] = 0.0;
        this.mat[8] = 0.0;
        this.mat[9] = 0.0;
        this.mat[10] = 1.0;
        this.mat[11] = 0.0;
        this.mat[12] = 0.0;
        this.mat[13] = 0.0;
        this.mat[14] = 0.0;
        this.mat[15] = 1.0;
        GeneralTransform3D.luBacksubstitution(arrd, arrn, this.mat);
        this.updateState();
        return this;
    }

    private static boolean luDecomposition(double[] arrd, int[] arrn) {
        int n;
        double[] arrd2 = new double[4];
        int n2 = 0;
        int n3 = 0;
        int n4 = 4;
        while (n4-- != 0) {
            double d = 0.0;
            n = 4;
            while (n-- != 0) {
                double d2 = arrd[n2++];
                if (!((d2 = Math.abs(d2)) > d)) continue;
                d = d2;
            }
            if (d == 0.0) {
                return false;
            }
            arrd2[n3++] = 1.0 / d;
        }
        n = 0;
        for (n4 = 0; n4 < 4; ++n4) {
            double d;
            int n5;
            int n6;
            int n7;
            double d3;
            int n8;
            for (n2 = 0; n2 < n4; ++n2) {
                n8 = n + 4 * n2 + n4;
                d3 = arrd[n8];
                int n9 = n2;
                int n10 = n + 4 * n2;
                n7 = n + n4;
                while (n9-- != 0) {
                    d3 -= arrd[n10] * arrd[n7];
                    ++n10;
                    n7 += 4;
                }
                arrd[n8] = d3;
            }
            double d4 = 0.0;
            n3 = -1;
            for (n2 = n4; n2 < 4; ++n2) {
                double d5;
                n8 = n + 4 * n2 + n4;
                d3 = arrd[n8];
                n6 = n4;
                n5 = n + 4 * n2;
                n7 = n + n4;
                while (n6-- != 0) {
                    d3 -= arrd[n5] * arrd[n7];
                    ++n5;
                    n7 += 4;
                }
                arrd[n8] = d3;
                d = arrd2[n2] * Math.abs(d3);
                if (!(d5 >= d4)) continue;
                d4 = d;
                n3 = n2;
            }
            if (n3 < 0) {
                return false;
            }
            if (n4 != n3) {
                n6 = 4;
                n5 = n + 4 * n3;
                n7 = n + 4 * n4;
                while (n6-- != 0) {
                    d = arrd[n5];
                    arrd[n5++] = arrd[n7];
                    arrd[n7++] = d;
                }
                arrd2[n3] = arrd2[n4];
            }
            arrn[n4] = n3;
            if (arrd[n + 4 * n4 + n4] == 0.0) {
                return false;
            }
            if (n4 == 3) continue;
            d = 1.0 / arrd[n + 4 * n4 + n4];
            n8 = n + 4 * (n4 + 1) + n4;
            n2 = 3 - n4;
            while (n2-- != 0) {
                int n11 = n8;
                arrd[n11] = arrd[n11] * d;
                n8 += 4;
            }
        }
        return true;
    }

    private static void luBacksubstitution(double[] arrd, int[] arrn, double[] arrd2) {
        int n = 0;
        for (int i = 0; i < 4; ++i) {
            int n2;
            int n3 = i;
            int n4 = -1;
            for (int j = 0; j < 4; ++j) {
                int n5 = arrn[n + j];
                double d = arrd2[n3 + 4 * n5];
                arrd2[n3 + 4 * n5] = arrd2[n3 + 4 * j];
                if (n4 >= 0) {
                    n2 = j * 4;
                    for (int k = n4; k <= j - 1; ++k) {
                        d -= arrd[n2 + k] * arrd2[n3 + 4 * k];
                    }
                } else if (d != 0.0) {
                    n4 = j;
                }
                arrd2[n3 + 4 * j] = d;
            }
            n2 = 12;
            int n6 = n3 + 12;
            arrd2[n6] = arrd2[n6] / arrd[n2 + 3];
            arrd2[n3 + 8] = (arrd2[n3 + 8] - arrd[(n2 -= 4) + 3] * arrd2[n3 + 12]) / arrd[n2 + 2];
            arrd2[n3 + 4] = (arrd2[n3 + 4] - arrd[(n2 -= 4) + 2] * arrd2[n3 + 8] - arrd[n2 + 3] * arrd2[n3 + 12]) / arrd[n2 + 1];
            arrd2[n3 + 0] = (arrd2[n3 + 0] - arrd[(n2 -= 4) + 1] * arrd2[n3 + 4] - arrd[n2 + 2] * arrd2[n3 + 8] - arrd[n2 + 3] * arrd2[n3 + 12]) / arrd[n2 + 0];
        }
    }

    public GeneralTransform3D mul(BaseTransform baseTransform) {
        double d;
        double d2;
        double d3;
        double d4;
        if (baseTransform.isIdentity()) {
            return this;
        }
        double d5 = baseTransform.getMxx();
        double d6 = baseTransform.getMxy();
        double d7 = baseTransform.getMxz();
        double d8 = baseTransform.getMxt();
        double d9 = baseTransform.getMyx();
        double d10 = baseTransform.getMyy();
        double d11 = baseTransform.getMyz();
        double d12 = baseTransform.getMyt();
        double d13 = baseTransform.getMzx();
        double d14 = baseTransform.getMzy();
        double d15 = baseTransform.getMzz();
        double d16 = baseTransform.getMzt();
        double d17 = this.mat[0] * d5 + this.mat[1] * d9 + this.mat[2] * d13;
        double d18 = this.mat[0] * d6 + this.mat[1] * d10 + this.mat[2] * d14;
        double d19 = this.mat[0] * d7 + this.mat[1] * d11 + this.mat[2] * d15;
        double d20 = this.mat[0] * d8 + this.mat[1] * d12 + this.mat[2] * d16 + this.mat[3];
        double d21 = this.mat[4] * d5 + this.mat[5] * d9 + this.mat[6] * d13;
        double d22 = this.mat[4] * d6 + this.mat[5] * d10 + this.mat[6] * d14;
        double d23 = this.mat[4] * d7 + this.mat[5] * d11 + this.mat[6] * d15;
        double d24 = this.mat[4] * d8 + this.mat[5] * d12 + this.mat[6] * d16 + this.mat[7];
        double d25 = this.mat[8] * d5 + this.mat[9] * d9 + this.mat[10] * d13;
        double d26 = this.mat[8] * d6 + this.mat[9] * d10 + this.mat[10] * d14;
        double d27 = this.mat[8] * d7 + this.mat[9] * d11 + this.mat[10] * d15;
        double d28 = this.mat[8] * d8 + this.mat[9] * d12 + this.mat[10] * d16 + this.mat[11];
        if (this.isAffine()) {
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
            d = 1.0;
        } else {
            d2 = this.mat[12] * d5 + this.mat[13] * d9 + this.mat[14] * d13;
            d3 = this.mat[12] * d6 + this.mat[13] * d10 + this.mat[14] * d14;
            d4 = this.mat[12] * d7 + this.mat[13] * d11 + this.mat[14] * d15;
            d = this.mat[12] * d8 + this.mat[13] * d12 + this.mat[14] * d16 + this.mat[15];
        }
        this.mat[0] = d17;
        this.mat[1] = d18;
        this.mat[2] = d19;
        this.mat[3] = d20;
        this.mat[4] = d21;
        this.mat[5] = d22;
        this.mat[6] = d23;
        this.mat[7] = d24;
        this.mat[8] = d25;
        this.mat[9] = d26;
        this.mat[10] = d27;
        this.mat[11] = d28;
        this.mat[12] = d2;
        this.mat[13] = d3;
        this.mat[14] = d4;
        this.mat[15] = d;
        this.updateState();
        return this;
    }

    public GeneralTransform3D scale(double d, double d2, double d3) {
        boolean bl = false;
        if (d != 1.0) {
            this.mat[0] = this.mat[0] * d;
            this.mat[4] = this.mat[4] * d;
            this.mat[8] = this.mat[8] * d;
            this.mat[12] = this.mat[12] * d;
            bl = true;
        }
        if (d2 != 1.0) {
            this.mat[1] = this.mat[1] * d2;
            this.mat[5] = this.mat[5] * d2;
            this.mat[9] = this.mat[9] * d2;
            this.mat[13] = this.mat[13] * d2;
            bl = true;
        }
        if (d3 != 1.0) {
            this.mat[2] = this.mat[2] * d3;
            this.mat[6] = this.mat[6] * d3;
            this.mat[10] = this.mat[10] * d3;
            this.mat[14] = this.mat[14] * d3;
            bl = true;
        }
        if (bl) {
            this.updateState();
        }
        return this;
    }

    public GeneralTransform3D mul(GeneralTransform3D generalTransform3D) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14;
        double d15;
        double d16;
        if (generalTransform3D.isIdentity()) {
            return this;
        }
        if (generalTransform3D.isAffine()) {
            d16 = this.mat[0] * generalTransform3D.mat[0] + this.mat[1] * generalTransform3D.mat[4] + this.mat[2] * generalTransform3D.mat[8];
            d15 = this.mat[0] * generalTransform3D.mat[1] + this.mat[1] * generalTransform3D.mat[5] + this.mat[2] * generalTransform3D.mat[9];
            d14 = this.mat[0] * generalTransform3D.mat[2] + this.mat[1] * generalTransform3D.mat[6] + this.mat[2] * generalTransform3D.mat[10];
            d13 = this.mat[0] * generalTransform3D.mat[3] + this.mat[1] * generalTransform3D.mat[7] + this.mat[2] * generalTransform3D.mat[11] + this.mat[3];
            d12 = this.mat[4] * generalTransform3D.mat[0] + this.mat[5] * generalTransform3D.mat[4] + this.mat[6] * generalTransform3D.mat[8];
            d11 = this.mat[4] * generalTransform3D.mat[1] + this.mat[5] * generalTransform3D.mat[5] + this.mat[6] * generalTransform3D.mat[9];
            d10 = this.mat[4] * generalTransform3D.mat[2] + this.mat[5] * generalTransform3D.mat[6] + this.mat[6] * generalTransform3D.mat[10];
            d9 = this.mat[4] * generalTransform3D.mat[3] + this.mat[5] * generalTransform3D.mat[7] + this.mat[6] * generalTransform3D.mat[11] + this.mat[7];
            d8 = this.mat[8] * generalTransform3D.mat[0] + this.mat[9] * generalTransform3D.mat[4] + this.mat[10] * generalTransform3D.mat[8];
            d7 = this.mat[8] * generalTransform3D.mat[1] + this.mat[9] * generalTransform3D.mat[5] + this.mat[10] * generalTransform3D.mat[9];
            d6 = this.mat[8] * generalTransform3D.mat[2] + this.mat[9] * generalTransform3D.mat[6] + this.mat[10] * generalTransform3D.mat[10];
            d5 = this.mat[8] * generalTransform3D.mat[3] + this.mat[9] * generalTransform3D.mat[7] + this.mat[10] * generalTransform3D.mat[11] + this.mat[11];
            if (this.isAffine()) {
                d4 = 0.0;
                d3 = 0.0;
                d2 = 0.0;
                d = 1.0;
            } else {
                d2 = this.mat[12] * generalTransform3D.mat[0] + this.mat[13] * generalTransform3D.mat[4] + this.mat[14] * generalTransform3D.mat[8];
                d3 = this.mat[12] * generalTransform3D.mat[1] + this.mat[13] * generalTransform3D.mat[5] + this.mat[14] * generalTransform3D.mat[9];
                d4 = this.mat[12] * generalTransform3D.mat[2] + this.mat[13] * generalTransform3D.mat[6] + this.mat[14] * generalTransform3D.mat[10];
                d = this.mat[12] * generalTransform3D.mat[3] + this.mat[13] * generalTransform3D.mat[7] + this.mat[14] * generalTransform3D.mat[11] + this.mat[15];
            }
        } else {
            d16 = this.mat[0] * generalTransform3D.mat[0] + this.mat[1] * generalTransform3D.mat[4] + this.mat[2] * generalTransform3D.mat[8] + this.mat[3] * generalTransform3D.mat[12];
            d15 = this.mat[0] * generalTransform3D.mat[1] + this.mat[1] * generalTransform3D.mat[5] + this.mat[2] * generalTransform3D.mat[9] + this.mat[3] * generalTransform3D.mat[13];
            d14 = this.mat[0] * generalTransform3D.mat[2] + this.mat[1] * generalTransform3D.mat[6] + this.mat[2] * generalTransform3D.mat[10] + this.mat[3] * generalTransform3D.mat[14];
            d13 = this.mat[0] * generalTransform3D.mat[3] + this.mat[1] * generalTransform3D.mat[7] + this.mat[2] * generalTransform3D.mat[11] + this.mat[3] * generalTransform3D.mat[15];
            d12 = this.mat[4] * generalTransform3D.mat[0] + this.mat[5] * generalTransform3D.mat[4] + this.mat[6] * generalTransform3D.mat[8] + this.mat[7] * generalTransform3D.mat[12];
            d11 = this.mat[4] * generalTransform3D.mat[1] + this.mat[5] * generalTransform3D.mat[5] + this.mat[6] * generalTransform3D.mat[9] + this.mat[7] * generalTransform3D.mat[13];
            d10 = this.mat[4] * generalTransform3D.mat[2] + this.mat[5] * generalTransform3D.mat[6] + this.mat[6] * generalTransform3D.mat[10] + this.mat[7] * generalTransform3D.mat[14];
            d9 = this.mat[4] * generalTransform3D.mat[3] + this.mat[5] * generalTransform3D.mat[7] + this.mat[6] * generalTransform3D.mat[11] + this.mat[7] * generalTransform3D.mat[15];
            d8 = this.mat[8] * generalTransform3D.mat[0] + this.mat[9] * generalTransform3D.mat[4] + this.mat[10] * generalTransform3D.mat[8] + this.mat[11] * generalTransform3D.mat[12];
            d7 = this.mat[8] * generalTransform3D.mat[1] + this.mat[9] * generalTransform3D.mat[5] + this.mat[10] * generalTransform3D.mat[9] + this.mat[11] * generalTransform3D.mat[13];
            d6 = this.mat[8] * generalTransform3D.mat[2] + this.mat[9] * generalTransform3D.mat[6] + this.mat[10] * generalTransform3D.mat[10] + this.mat[11] * generalTransform3D.mat[14];
            d5 = this.mat[8] * generalTransform3D.mat[3] + this.mat[9] * generalTransform3D.mat[7] + this.mat[10] * generalTransform3D.mat[11] + this.mat[11] * generalTransform3D.mat[15];
            if (this.isAffine()) {
                d2 = generalTransform3D.mat[12];
                d3 = generalTransform3D.mat[13];
                d4 = generalTransform3D.mat[14];
                d = generalTransform3D.mat[15];
            } else {
                d2 = this.mat[12] * generalTransform3D.mat[0] + this.mat[13] * generalTransform3D.mat[4] + this.mat[14] * generalTransform3D.mat[8] + this.mat[15] * generalTransform3D.mat[12];
                d3 = this.mat[12] * generalTransform3D.mat[1] + this.mat[13] * generalTransform3D.mat[5] + this.mat[14] * generalTransform3D.mat[9] + this.mat[15] * generalTransform3D.mat[13];
                d4 = this.mat[12] * generalTransform3D.mat[2] + this.mat[13] * generalTransform3D.mat[6] + this.mat[14] * generalTransform3D.mat[10] + this.mat[15] * generalTransform3D.mat[14];
                d = this.mat[12] * generalTransform3D.mat[3] + this.mat[13] * generalTransform3D.mat[7] + this.mat[14] * generalTransform3D.mat[11] + this.mat[15] * generalTransform3D.mat[15];
            }
        }
        this.mat[0] = d16;
        this.mat[1] = d15;
        this.mat[2] = d14;
        this.mat[3] = d13;
        this.mat[4] = d12;
        this.mat[5] = d11;
        this.mat[6] = d10;
        this.mat[7] = d9;
        this.mat[8] = d8;
        this.mat[9] = d7;
        this.mat[10] = d6;
        this.mat[11] = d5;
        this.mat[12] = d2;
        this.mat[13] = d3;
        this.mat[14] = d4;
        this.mat[15] = d;
        this.updateState();
        return this;
    }

    public GeneralTransform3D setIdentity() {
        this.mat[0] = 1.0;
        this.mat[1] = 0.0;
        this.mat[2] = 0.0;
        this.mat[3] = 0.0;
        this.mat[4] = 0.0;
        this.mat[5] = 1.0;
        this.mat[6] = 0.0;
        this.mat[7] = 0.0;
        this.mat[8] = 0.0;
        this.mat[9] = 0.0;
        this.mat[10] = 1.0;
        this.mat[11] = 0.0;
        this.mat[12] = 0.0;
        this.mat[13] = 0.0;
        this.mat[14] = 0.0;
        this.mat[15] = 1.0;
        this.identity = true;
        return this;
    }

    public boolean isIdentity() {
        return this.identity;
    }

    private void updateState() {
        this.identity = this.mat[0] == 1.0 && this.mat[5] == 1.0 && this.mat[10] == 1.0 && this.mat[15] == 1.0 && this.mat[1] == 0.0 && this.mat[2] == 0.0 && this.mat[3] == 0.0 && this.mat[4] == 0.0 && this.mat[6] == 0.0 && this.mat[7] == 0.0 && this.mat[8] == 0.0 && this.mat[9] == 0.0 && this.mat[11] == 0.0 && this.mat[12] == 0.0 && this.mat[13] == 0.0 && this.mat[14] == 0.0;
    }

    boolean isInfOrNaN() {
        double d = 0.0;
        for (int i = 0; i < this.mat.length; ++i) {
            d *= this.mat[i];
        }
        return d != 0.0;
    }

    static boolean almostZero(double d) {
        return d < 1.0E-5 && d > -1.0E-5;
    }

    static boolean almostOne(double d) {
        return d < 1.00001 && d > 0.99999;
    }

    public GeneralTransform3D copy() {
        GeneralTransform3D generalTransform3D = new GeneralTransform3D();
        generalTransform3D.set(this);
        return generalTransform3D;
    }

    public String toString() {
        return this.mat[0] + ", " + this.mat[1] + ", " + this.mat[2] + ", " + this.mat[3] + "\n" + this.mat[4] + ", " + this.mat[5] + ", " + this.mat[6] + ", " + this.mat[7] + "\n" + this.mat[8] + ", " + this.mat[9] + ", " + this.mat[10] + ", " + this.mat[11] + "\n" + this.mat[12] + ", " + this.mat[13] + ", " + this.mat[14] + ", " + this.mat[15] + "\n";
    }
}

