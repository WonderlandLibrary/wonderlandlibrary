/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.geom.transform.TransformHelper;

public class Affine3D
extends AffineBase {
    private double mxz;
    private double myz;
    private double mzx;
    private double mzy;
    private double mzz;
    private double mzt;

    public Affine3D() {
        this.mzz = 1.0;
        this.myy = 1.0;
        this.mxx = 1.0;
    }

    public Affine3D(BaseTransform baseTransform) {
        this.setTransform(baseTransform);
    }

    public Affine3D(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        this.mxx = d;
        this.mxy = d2;
        this.mxz = d3;
        this.mxt = d4;
        this.myx = d5;
        this.myy = d6;
        this.myz = d7;
        this.myt = d8;
        this.mzx = d9;
        this.mzy = d10;
        this.mzz = d11;
        this.mzt = d12;
        this.updateState();
    }

    public Affine3D(Affine3D affine3D) {
        this.mxx = affine3D.mxx;
        this.mxy = affine3D.mxy;
        this.mxz = affine3D.mxz;
        this.mxt = affine3D.mxt;
        this.myx = affine3D.myx;
        this.myy = affine3D.myy;
        this.myz = affine3D.myz;
        this.myt = affine3D.myt;
        this.mzx = affine3D.mzx;
        this.mzy = affine3D.mzy;
        this.mzz = affine3D.mzz;
        this.mzt = affine3D.mzt;
        this.state = affine3D.state;
        this.type = affine3D.type;
    }

    @Override
    public BaseTransform copy() {
        return new Affine3D(this);
    }

    @Override
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.AFFINE_3D;
    }

    @Override
    protected void reset3Delements() {
        this.mxz = 0.0;
        this.myz = 0.0;
        this.mzx = 0.0;
        this.mzy = 0.0;
        this.mzz = 1.0;
        this.mzt = 0.0;
    }

    @Override
    protected void updateState() {
        super.updateState();
        if (!(Affine3D.almostZero(this.mxz) && Affine3D.almostZero(this.myz) && Affine3D.almostZero(this.mzx) && Affine3D.almostZero(this.mzy) && Affine3D.almostOne(this.mzz) && Affine3D.almostZero(this.mzt))) {
            this.state |= 8;
            if (this.type != -1) {
                this.type |= 0x80;
            }
        }
    }

    @Override
    public double getMxz() {
        return this.mxz;
    }

    @Override
    public double getMyz() {
        return this.myz;
    }

    @Override
    public double getMzx() {
        return this.mzx;
    }

    @Override
    public double getMzy() {
        return this.mzy;
    }

    @Override
    public double getMzz() {
        return this.mzz;
    }

    @Override
    public double getMzt() {
        return this.mzt;
    }

    @Override
    public double getDeterminant() {
        if ((this.state & 8) == 0) {
            return super.getDeterminant();
        }
        return this.mxx * (this.myy * this.mzz - this.mzy * this.myz) + this.mxy * (this.myz * this.mzx - this.mzz * this.myx) + this.mxz * (this.myx * this.mzy - this.mzx * this.myy);
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        this.mxx = baseTransform.getMxx();
        this.mxy = baseTransform.getMxy();
        this.mxz = baseTransform.getMxz();
        this.mxt = baseTransform.getMxt();
        this.myx = baseTransform.getMyx();
        this.myy = baseTransform.getMyy();
        this.myz = baseTransform.getMyz();
        this.myt = baseTransform.getMyt();
        this.mzx = baseTransform.getMzx();
        this.mzy = baseTransform.getMzy();
        this.mzz = baseTransform.getMzz();
        this.mzt = baseTransform.getMzt();
        this.updateState();
    }

    public void setTransform(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        this.mxx = d;
        this.mxy = d2;
        this.mxz = d3;
        this.mxt = d4;
        this.myx = d5;
        this.myy = d6;
        this.myz = d7;
        this.myt = d8;
        this.mzx = d9;
        this.mzy = d10;
        this.mzz = d11;
        this.mzt = d12;
        this.updateState();
    }

    public void setToTranslation(double d, double d2, double d3) {
        this.mxx = 1.0;
        this.mxy = 0.0;
        this.mxz = 0.0;
        this.mxt = d;
        this.myx = 0.0;
        this.myy = 1.0;
        this.myz = 0.0;
        this.myt = d2;
        this.mzx = 0.0;
        this.mzy = 0.0;
        this.mzz = 1.0;
        this.mzt = d3;
        if (d3 == 0.0) {
            if (d == 0.0 && d2 == 0.0) {
                this.state = 0;
                this.type = 0;
            } else {
                this.state = 1;
                this.type = 1;
            }
        } else if (d == 0.0 && d2 == 0.0) {
            this.state = 8;
            this.type = 128;
        } else {
            this.state = 9;
            this.type = 129;
        }
    }

    public void setToScale(double d, double d2, double d3) {
        this.mxx = d;
        this.mxy = 0.0;
        this.mxz = 0.0;
        this.mxt = 0.0;
        this.myx = 0.0;
        this.myy = d2;
        this.myz = 0.0;
        this.myt = 0.0;
        this.mzx = 0.0;
        this.mzy = 0.0;
        this.mzz = d3;
        this.mzt = 0.0;
        if (d3 == 1.0) {
            if (d == 1.0 && d2 == 1.0) {
                this.state = 0;
                this.type = 0;
            } else {
                this.state = 2;
                this.type = -1;
            }
        } else if (d == 1.0 && d2 == 1.0) {
            this.state = 8;
            this.type = 128;
        } else {
            this.state = 10;
            this.type = -1;
        }
    }

    public void setToRotation(double d, double d2, double d3, double d4, double d5, double d6, double d7) {
        this.setToRotation(d, d2, d3, d4);
        if (d5 != 0.0 || d6 != 0.0 || d7 != 0.0) {
            this.preTranslate(d5, d6, d7);
            this.translate(-d5, -d6, -d7);
        }
    }

    public void setToRotation(double d, double d2, double d3, double d4) {
        double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
        if (Affine3D.almostZero(d5)) {
            this.setToIdentity();
            return;
        }
        d5 = 1.0 / d5;
        double d6 = d2 * d5;
        double d7 = d3 * d5;
        double d8 = d4 * d5;
        double d9 = Math.sin(d);
        double d10 = Math.cos(d);
        double d11 = 1.0 - d10;
        double d12 = d6 * d8;
        double d13 = d6 * d7;
        double d14 = d7 * d8;
        this.mxx = d11 * d6 * d6 + d10;
        this.mxy = d11 * d13 - d9 * d8;
        this.mxz = d11 * d12 + d9 * d7;
        this.mxt = 0.0;
        this.myx = d11 * d13 + d9 * d8;
        this.myy = d11 * d7 * d7 + d10;
        this.myz = d11 * d14 - d9 * d6;
        this.myt = 0.0;
        this.mzx = d11 * d12 - d9 * d7;
        this.mzy = d11 * d14 + d9 * d6;
        this.mzz = d11 * d8 * d8 + d10;
        this.mzt = 0.0;
        this.updateState();
    }

    @Override
    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if ((this.state & 8) == 0) {
            baseBounds2 = super.transform(baseBounds, baseBounds2);
            return baseBounds2;
        }
        switch (this.state) {
            default: {
                Vec3d vec3d = new Vec3d();
                baseBounds2 = TransformHelper.general3dBoundsTransform(this, baseBounds, baseBounds2, vec3d);
                break;
            }
            case 3: {
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinX() * this.mxx + this.mxt), (float)((double)baseBounds.getMinY() * this.myy + this.myt), (float)((double)baseBounds.getMinZ() * this.mzz + this.mzt), (float)((double)baseBounds.getMaxX() * this.mxx + this.mxt), (float)((double)baseBounds.getMaxY() * this.myy + this.myt), (float)((double)baseBounds.getMaxZ() * this.mzz + this.mzt));
                break;
            }
            case 2: {
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinX() * this.mxx), (float)((double)baseBounds.getMinY() * this.myy), (float)((double)baseBounds.getMinZ() * this.mzz), (float)((double)baseBounds.getMaxX() * this.mxx), (float)((double)baseBounds.getMaxY() * this.myy), (float)((double)baseBounds.getMaxZ() * this.mzz));
                break;
            }
            case 1: {
                baseBounds2 = baseBounds2.deriveWithNewBounds((float)((double)baseBounds.getMinX() + this.mxt), (float)((double)baseBounds.getMinY() + this.myt), (float)((double)baseBounds.getMinZ() + this.mzt), (float)((double)baseBounds.getMaxX() + this.mxt), (float)((double)baseBounds.getMaxY() + this.myt), (float)((double)baseBounds.getMaxZ() + this.mzt));
                break;
            }
            case 0: {
                if (baseBounds == baseBounds2) break;
                baseBounds2 = baseBounds2.deriveWithNewBounds(baseBounds);
            }
        }
        return baseBounds2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if ((this.state & 8) == 0) {
            return super.transform(vec3d, vec3d2);
        }
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        vec3d2.x = this.mxx * d + this.mxy * d2 + this.mxz * d3 + this.mxt;
        vec3d2.y = this.myx * d + this.myy * d2 + this.myz * d3 + this.myt;
        vec3d2.z = this.mzx * d + this.mzy * d2 + this.mzz * d3 + this.mzt;
        return vec3d2;
    }

    @Override
    public Vec3d deltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if ((this.state & 8) == 0) {
            return super.deltaTransform(vec3d, vec3d2);
        }
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        vec3d2.x = this.mxx * d + this.mxy * d2 + this.mxz * d3;
        vec3d2.y = this.myx * d + this.myy * d2 + this.myz * d3;
        vec3d2.z = this.mzx * d + this.mzy * d2 + this.mzz * d3;
        return vec3d2;
    }

    @Override
    public void inverseTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseTransform(arrf, n, arrf2, n2, n3);
        } else {
            this.createInverse().transform(arrf, n, arrf2, n2, n3);
        }
    }

    @Override
    public void inverseDeltaTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseDeltaTransform(arrf, n, arrf2, n2, n3);
        } else {
            this.createInverse().deltaTransform(arrf, n, arrf2, n2, n3);
        }
    }

    @Override
    public void inverseTransform(double[] arrd, int n, double[] arrd2, int n2, int n3) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseTransform(arrd, n, arrd2, n2, n3);
        } else {
            this.createInverse().transform(arrd, n, arrd2, n2, n3);
        }
    }

    @Override
    public Point2D inverseTransform(Point2D point2D, Point2D point2D2) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            return super.inverseTransform(point2D, point2D2);
        }
        return this.createInverse().transform(point2D, point2D2);
    }

    @Override
    public Vec3d inverseTransform(Vec3d vec3d, Vec3d vec3d2) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            return super.inverseTransform(vec3d, vec3d2);
        }
        return this.createInverse().transform(vec3d, vec3d2);
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d vec3d, Vec3d vec3d2) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            return super.inverseDeltaTransform(vec3d, vec3d2);
        }
        return this.createInverse().deltaTransform(vec3d, vec3d2);
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds baseBounds, BaseBounds baseBounds2) throws NoninvertibleTransformException {
        baseBounds2 = (this.state & 8) == 0 ? super.inverseTransform(baseBounds, baseBounds2) : this.createInverse().transform(baseBounds, baseBounds2);
        return baseBounds2;
    }

    @Override
    public void inverseTransform(Rectangle rectangle, Rectangle rectangle2) throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.inverseTransform(rectangle, rectangle2);
        } else {
            this.createInverse().transform(rectangle, rectangle2);
        }
    }

    @Override
    public BaseTransform createInverse() throws NoninvertibleTransformException {
        BaseTransform baseTransform = this.copy();
        baseTransform.invert();
        return baseTransform;
    }

    @Override
    public void invert() throws NoninvertibleTransformException {
        if ((this.state & 8) == 0) {
            super.invert();
            return;
        }
        double d = this.minor(0, 0);
        double d2 = -this.minor(0, 1);
        double d3 = this.minor(0, 2);
        double d4 = -this.minor(1, 0);
        double d5 = this.minor(1, 1);
        double d6 = -this.minor(1, 2);
        double d7 = this.minor(2, 0);
        double d8 = -this.minor(2, 1);
        double d9 = this.minor(2, 2);
        double d10 = -this.minor(3, 0);
        double d11 = this.minor(3, 1);
        double d12 = -this.minor(3, 2);
        double d13 = this.getDeterminant();
        this.mxx = d / d13;
        this.mxy = d4 / d13;
        this.mxz = d7 / d13;
        this.mxt = d10 / d13;
        this.myx = d2 / d13;
        this.myy = d5 / d13;
        this.myz = d8 / d13;
        this.myt = d11 / d13;
        this.mzx = d3 / d13;
        this.mzy = d6 / d13;
        this.mzz = d9 / d13;
        this.mzt = d12 / d13;
        this.updateState();
    }

    private double minor(int n, int n2) {
        double d = this.mxx;
        double d2 = this.mxy;
        double d3 = this.mxz;
        double d4 = this.myx;
        double d5 = this.myy;
        double d6 = this.myz;
        double d7 = this.mzx;
        double d8 = this.mzy;
        double d9 = this.mzz;
        switch (n2) {
            case 0: {
                d = d2;
                d4 = d5;
                d7 = d8;
            }
            case 1: {
                d2 = d3;
                d5 = d6;
                d8 = d9;
            }
            case 2: {
                d3 = this.mxt;
                d6 = this.myt;
                d9 = this.mzt;
            }
        }
        switch (n) {
            case 0: {
                d = d4;
                d2 = d5;
            }
            case 1: {
                d4 = d7;
                d5 = d8;
            }
            case 2: {
                break;
            }
            case 3: {
                return d * (d5 * d9 - d8 * d6) + d2 * (d6 * d7 - d9 * d4) + d3 * (d4 * d8 - d7 * d5);
            }
        }
        return d * d5 - d2 * d4;
    }

    @Override
    public Affine3D deriveWithNewTransform(BaseTransform baseTransform) {
        this.setTransform(baseTransform);
        return this;
    }

    @Override
    public Affine3D deriveWithTranslation(double d, double d2) {
        this.translate(d, d2, 0.0);
        return this;
    }

    @Override
    public void translate(double d, double d2) {
        if ((this.state & 8) == 0) {
            super.translate(d, d2);
        } else {
            this.translate(d, d2, 0.0);
        }
    }

    public void translate(double d, double d2, double d3) {
        if ((this.state & 8) == 0) {
            super.translate(d, d2);
            if (d3 != 0.0) {
                this.mzt = d3;
                this.state |= 8;
                if (this.type != -1) {
                    this.type |= 0x80;
                }
            }
            return;
        }
        this.mxt = d * this.mxx + d2 * this.mxy + d3 * this.mxz + this.mxt;
        this.myt = d * this.myx + d2 * this.myy + d3 * this.myz + this.myt;
        this.mzt = d * this.mzx + d2 * this.mzy + d3 * this.mzz + this.mzt;
        this.updateState();
    }

    @Override
    public Affine3D deriveWithPreTranslation(double d, double d2) {
        this.preTranslate(d, d2, 0.0);
        return this;
    }

    @Override
    public BaseTransform deriveWithTranslation(double d, double d2, double d3) {
        this.translate(d, d2, d3);
        return this;
    }

    @Override
    public BaseTransform deriveWithScale(double d, double d2, double d3) {
        this.scale(d, d2, d3);
        return this;
    }

    @Override
    public BaseTransform deriveWithRotation(double d, double d2, double d3, double d4) {
        this.rotate(d, d2, d3, d4);
        return this;
    }

    public void preTranslate(double d, double d2, double d3) {
        this.mxt += d;
        this.myt += d2;
        this.mzt += d3;
        int n = 0;
        int n2 = 0;
        if (this.mzt == 0.0) {
            if ((this.state & 8) != 0) {
                this.updateState();
                return;
            }
        } else {
            this.state |= 8;
            n2 = 128;
        }
        if (this.mxt == 0.0 && this.myt == 0.0) {
            this.state &= 0xFFFFFFFE;
            n = 1;
        } else {
            this.state |= 1;
            n2 |= 1;
        }
        if (this.type != -1) {
            this.type = this.type & ~n | n2;
        }
    }

    @Override
    public void scale(double d, double d2) {
        if ((this.state & 8) == 0) {
            super.scale(d, d2);
        } else {
            this.scale(d, d2, 1.0);
        }
    }

    public void scale(double d, double d2, double d3) {
        if ((this.state & 8) == 0) {
            super.scale(d, d2);
            if (d3 != 1.0) {
                this.mzz = d3;
                this.state |= 8;
                if (this.type != -1) {
                    this.type |= 0x80;
                }
            }
            return;
        }
        this.mxx *= d;
        this.mxy *= d2;
        this.mxz *= d3;
        this.myx *= d;
        this.myy *= d2;
        this.myz *= d3;
        this.mzx *= d;
        this.mzy *= d2;
        this.mzz *= d3;
        this.updateState();
    }

    @Override
    public void rotate(double d) {
        if ((this.state & 8) == 0) {
            super.rotate(d);
        } else {
            this.rotate(d, 0.0, 0.0, 1.0);
        }
    }

    public void rotate(double d, double d2, double d3, double d4) {
        if ((this.state & 8) == 0 && Affine3D.almostZero(d2) && Affine3D.almostZero(d3)) {
            if (d4 > 0.0) {
                super.rotate(d);
            } else if (d4 < 0.0) {
                super.rotate(-d);
            }
            return;
        }
        double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
        if (Affine3D.almostZero(d5)) {
            return;
        }
        d5 = 1.0 / d5;
        double d6 = d2 * d5;
        double d7 = d3 * d5;
        double d8 = d4 * d5;
        double d9 = Math.sin(d);
        double d10 = Math.cos(d);
        double d11 = 1.0 - d10;
        double d12 = d6 * d8;
        double d13 = d6 * d7;
        double d14 = d7 * d8;
        double d15 = d11 * d6 * d6 + d10;
        double d16 = d11 * d13 - d9 * d8;
        double d17 = d11 * d12 + d9 * d7;
        double d18 = d11 * d13 + d9 * d8;
        double d19 = d11 * d7 * d7 + d10;
        double d20 = d11 * d14 - d9 * d6;
        double d21 = d11 * d12 - d9 * d7;
        double d22 = d11 * d14 + d9 * d6;
        double d23 = d11 * d8 * d8 + d10;
        double d24 = this.mxx * d15 + this.mxy * d18 + this.mxz * d21;
        double d25 = this.mxx * d16 + this.mxy * d19 + this.mxz * d22;
        double d26 = this.mxx * d17 + this.mxy * d20 + this.mxz * d23;
        double d27 = this.myx * d15 + this.myy * d18 + this.myz * d21;
        double d28 = this.myx * d16 + this.myy * d19 + this.myz * d22;
        double d29 = this.myx * d17 + this.myy * d20 + this.myz * d23;
        double d30 = this.mzx * d15 + this.mzy * d18 + this.mzz * d21;
        double d31 = this.mzx * d16 + this.mzy * d19 + this.mzz * d22;
        double d32 = this.mzx * d17 + this.mzy * d20 + this.mzz * d23;
        this.mxx = d24;
        this.mxy = d25;
        this.mxz = d26;
        this.myx = d27;
        this.myy = d28;
        this.myz = d29;
        this.mzx = d30;
        this.mzy = d31;
        this.mzz = d32;
        this.updateState();
    }

    @Override
    public void shear(double d, double d2) {
        if ((this.state & 8) == 0) {
            super.shear(d, d2);
            return;
        }
        double d3 = this.mxx + this.mxy * d2;
        double d4 = this.mxy + this.mxx * d;
        double d5 = this.myx + this.myy * d2;
        double d6 = this.myy + this.myx * d;
        double d7 = this.mzx + this.mzy * d2;
        double d8 = this.mzy + this.mzx * d;
        this.mxx = d3;
        this.mxy = d4;
        this.myx = d5;
        this.myy = d6;
        this.mzx = d7;
        this.mzy = d8;
        this.updateState();
    }

    @Override
    public Affine3D deriveWithConcatenation(BaseTransform baseTransform) {
        this.concatenate(baseTransform);
        return this;
    }

    @Override
    public Affine3D deriveWithPreConcatenation(BaseTransform baseTransform) {
        this.preConcatenate(baseTransform);
        return this;
    }

    @Override
    public void concatenate(BaseTransform baseTransform) {
        switch (baseTransform.getDegree()) {
            case IDENTITY: {
                return;
            }
            case TRANSLATE_2D: {
                this.translate(baseTransform.getMxt(), baseTransform.getMyt());
                return;
            }
            case TRANSLATE_3D: {
                this.translate(baseTransform.getMxt(), baseTransform.getMyt(), baseTransform.getMzt());
                return;
            }
            case AFFINE_3D: {
                if (!baseTransform.is2D()) break;
            }
            case AFFINE_2D: {
                if ((this.state & 8) != 0) break;
                super.concatenate(baseTransform);
                return;
            }
        }
        double d = baseTransform.getMxx();
        double d2 = baseTransform.getMxy();
        double d3 = baseTransform.getMxz();
        double d4 = baseTransform.getMxt();
        double d5 = baseTransform.getMyx();
        double d6 = baseTransform.getMyy();
        double d7 = baseTransform.getMyz();
        double d8 = baseTransform.getMyt();
        double d9 = baseTransform.getMzx();
        double d10 = baseTransform.getMzy();
        double d11 = baseTransform.getMzz();
        double d12 = baseTransform.getMzt();
        double d13 = this.mxx * d + this.mxy * d5 + this.mxz * d9;
        double d14 = this.mxx * d2 + this.mxy * d6 + this.mxz * d10;
        double d15 = this.mxx * d3 + this.mxy * d7 + this.mxz * d11;
        double d16 = this.mxx * d4 + this.mxy * d8 + this.mxz * d12 + this.mxt;
        double d17 = this.myx * d + this.myy * d5 + this.myz * d9;
        double d18 = this.myx * d2 + this.myy * d6 + this.myz * d10;
        double d19 = this.myx * d3 + this.myy * d7 + this.myz * d11;
        double d20 = this.myx * d4 + this.myy * d8 + this.myz * d12 + this.myt;
        double d21 = this.mzx * d + this.mzy * d5 + this.mzz * d9;
        double d22 = this.mzx * d2 + this.mzy * d6 + this.mzz * d10;
        double d23 = this.mzx * d3 + this.mzy * d7 + this.mzz * d11;
        double d24 = this.mzx * d4 + this.mzy * d8 + this.mzz * d12 + this.mzt;
        this.mxx = d13;
        this.mxy = d14;
        this.mxz = d15;
        this.mxt = d16;
        this.myx = d17;
        this.myy = d18;
        this.myz = d19;
        this.myt = d20;
        this.mzx = d21;
        this.mzy = d22;
        this.mzz = d23;
        this.mzt = d24;
        this.updateState();
    }

    public void concatenate(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        double d13 = this.mxx * d + this.mxy * d5 + this.mxz * d9;
        double d14 = this.mxx * d2 + this.mxy * d6 + this.mxz * d10;
        double d15 = this.mxx * d3 + this.mxy * d7 + this.mxz * d11;
        double d16 = this.mxx * d4 + this.mxy * d8 + this.mxz * d12 + this.mxt;
        double d17 = this.myx * d + this.myy * d5 + this.myz * d9;
        double d18 = this.myx * d2 + this.myy * d6 + this.myz * d10;
        double d19 = this.myx * d3 + this.myy * d7 + this.myz * d11;
        double d20 = this.myx * d4 + this.myy * d8 + this.myz * d12 + this.myt;
        double d21 = this.mzx * d + this.mzy * d5 + this.mzz * d9;
        double d22 = this.mzx * d2 + this.mzy * d6 + this.mzz * d10;
        double d23 = this.mzx * d3 + this.mzy * d7 + this.mzz * d11;
        double d24 = this.mzx * d4 + this.mzy * d8 + this.mzz * d12 + this.mzt;
        this.mxx = d13;
        this.mxy = d14;
        this.mxz = d15;
        this.mxt = d16;
        this.myx = d17;
        this.myy = d18;
        this.myz = d19;
        this.myt = d20;
        this.mzx = d21;
        this.mzy = d22;
        this.mzz = d23;
        this.mzt = d24;
        this.updateState();
    }

    @Override
    public Affine3D deriveWithConcatenation(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.mxx * d + this.mxy * d2;
        double d8 = this.mxx * d3 + this.mxy * d4;
        double d9 = this.mxx * d5 + this.mxy * d6 + this.mxt;
        double d10 = this.myx * d + this.myy * d2;
        double d11 = this.myx * d3 + this.myy * d4;
        double d12 = this.myx * d5 + this.myy * d6 + this.myt;
        double d13 = this.mzx * d + this.mzy * d2;
        double d14 = this.mzx * d3 + this.mzy * d4;
        double d15 = this.mzx * d5 + this.mzy * d6 + this.mzt;
        this.mxx = d7;
        this.mxy = d8;
        this.mxt = d9;
        this.myx = d10;
        this.myy = d11;
        this.myt = d12;
        this.mzx = d13;
        this.mzy = d14;
        this.mzt = d15;
        this.updateState();
        return this;
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        this.concatenate(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        return this;
    }

    public void preConcatenate(BaseTransform baseTransform) {
        switch (baseTransform.getDegree()) {
            case IDENTITY: {
                return;
            }
            case TRANSLATE_2D: {
                this.preTranslate(baseTransform.getMxt(), baseTransform.getMyt(), 0.0);
                return;
            }
            case TRANSLATE_3D: {
                this.preTranslate(baseTransform.getMxt(), baseTransform.getMyt(), baseTransform.getMzt());
                return;
            }
        }
        double d = baseTransform.getMxx();
        double d2 = baseTransform.getMxy();
        double d3 = baseTransform.getMxz();
        double d4 = baseTransform.getMxt();
        double d5 = baseTransform.getMyx();
        double d6 = baseTransform.getMyy();
        double d7 = baseTransform.getMyz();
        double d8 = baseTransform.getMyt();
        double d9 = baseTransform.getMzx();
        double d10 = baseTransform.getMzy();
        double d11 = baseTransform.getMzz();
        double d12 = baseTransform.getMzt();
        double d13 = d * this.mxx + d2 * this.myx + d3 * this.mzx;
        double d14 = d * this.mxy + d2 * this.myy + d3 * this.mzy;
        double d15 = d * this.mxz + d2 * this.myz + d3 * this.mzz;
        double d16 = d * this.mxt + d2 * this.myt + d3 * this.mzt + d4;
        double d17 = d5 * this.mxx + d6 * this.myx + d7 * this.mzx;
        double d18 = d5 * this.mxy + d6 * this.myy + d7 * this.mzy;
        double d19 = d5 * this.mxz + d6 * this.myz + d7 * this.mzz;
        double d20 = d5 * this.mxt + d6 * this.myt + d7 * this.mzt + d8;
        double d21 = d9 * this.mxx + d10 * this.myx + d11 * this.mzx;
        double d22 = d9 * this.mxy + d10 * this.myy + d11 * this.mzy;
        double d23 = d9 * this.mxz + d10 * this.myz + d11 * this.mzz;
        double d24 = d9 * this.mxt + d10 * this.myt + d11 * this.mzt + d12;
        this.mxx = d13;
        this.mxy = d14;
        this.mxz = d15;
        this.mxt = d16;
        this.myx = d17;
        this.myy = d18;
        this.myz = d19;
        this.myt = d20;
        this.mzx = d21;
        this.mzy = d22;
        this.mzz = d23;
        this.mzt = d24;
        this.updateState();
    }

    @Override
    public void restoreTransform(double d, double d2, double d3, double d4, double d5, double d6) {
        throw new InternalError("must use Affine3D restore method to prevent loss of information");
    }

    @Override
    public void restoreTransform(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        this.mxx = d;
        this.mxy = d2;
        this.mxz = d3;
        this.mxt = d4;
        this.myx = d5;
        this.myy = d6;
        this.myz = d7;
        this.myt = d8;
        this.mzx = d9;
        this.mzy = d10;
        this.mzz = d11;
        this.mzt = d12;
        this.updateState();
    }

    public Affine3D lookAt(Vec3d vec3d, Vec3d vec3d2, Vec3d vec3d3) {
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = 1.0 / Math.sqrt(d * d + d2 * d2 + d3 * d3);
        d *= d4;
        d2 *= d4;
        d3 *= d4;
        d4 = 1.0 / Math.sqrt(vec3d3.x * vec3d3.x + vec3d3.y * vec3d3.y + vec3d3.z * vec3d3.z);
        double d5 = vec3d3.x * d4;
        double d6 = vec3d3.y * d4;
        double d7 = vec3d3.z * d4;
        double d8 = d6 * d3 - d2 * d7;
        double d9 = d7 * d - d5 * d3;
        double d10 = d5 * d2 - d6 * d;
        d4 = 1.0 / Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
        d5 = d2 * (d10 *= d4) - (d9 *= d4) * d3;
        d6 = d3 * (d8 *= d4) - d * d10;
        d7 = d * d9 - d2 * d8;
        this.mxx = d8;
        this.mxy = d9;
        this.mxz = d10;
        this.myx = d5;
        this.myy = d6;
        this.myz = d7;
        this.mzx = d;
        this.mzy = d2;
        this.mzz = d3;
        this.mxt = -vec3d.x * this.mxx + -vec3d.y * this.mxy + -vec3d.z * this.mxz;
        this.myt = -vec3d.x * this.myx + -vec3d.y * this.myy + -vec3d.z * this.myz;
        this.mzt = -vec3d.x * this.mzx + -vec3d.y * this.mzy + -vec3d.z * this.mzz;
        this.updateState();
        return this;
    }

    static boolean almostOne(double d) {
        return d < 1.00001 && d > 0.99999;
    }

    private static double _matround(double d) {
        return Math.rint(d * 1.0E15) / 1.0E15;
    }

    @Override
    public String toString() {
        return "Affine3D[[" + Affine3D._matround(this.mxx) + ", " + Affine3D._matround(this.mxy) + ", " + Affine3D._matround(this.mxz) + ", " + Affine3D._matround(this.mxt) + "], [" + Affine3D._matround(this.myx) + ", " + Affine3D._matround(this.myy) + ", " + Affine3D._matround(this.myz) + ", " + Affine3D._matround(this.myt) + "], [" + Affine3D._matround(this.mzx) + ", " + Affine3D._matround(this.mzy) + ", " + Affine3D._matround(this.mzz) + ", " + Affine3D._matround(this.mzt) + "]]";
    }
}

