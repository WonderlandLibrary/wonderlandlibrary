/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.geom.transform.Translate2D;

public abstract class AffineBase
extends BaseTransform {
    protected static final int APPLY_IDENTITY = 0;
    protected static final int APPLY_TRANSLATE = 1;
    protected static final int APPLY_SCALE = 2;
    protected static final int APPLY_SHEAR = 4;
    protected static final int APPLY_3D = 8;
    protected static final int APPLY_2D_MASK = 7;
    protected static final int APPLY_2D_DELTA_MASK = 6;
    protected static final int HI_SHIFT = 4;
    protected static final int HI_IDENTITY = 0;
    protected static final int HI_TRANSLATE = 16;
    protected static final int HI_SCALE = 32;
    protected static final int HI_SHEAR = 64;
    protected static final int HI_3D = 128;
    protected double mxx;
    protected double myx;
    protected double mxy;
    protected double myy;
    protected double mxt;
    protected double myt;
    protected transient int state;
    protected transient int type;
    private static final int[] rot90conversion = new int[]{4, 5, 4, 5, 2, 3, 6, 7};

    protected static void stateError() {
        throw new InternalError("missing case in transform state switch");
    }

    protected void updateState() {
        this.updateState2D();
    }

    protected void updateState2D() {
        if (this.mxy == 0.0 && this.myx == 0.0) {
            if (this.mxx == 1.0 && this.myy == 1.0) {
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 0;
                    this.type = 0;
                } else {
                    this.state = 1;
                    this.type = 1;
                }
            } else {
                this.state = this.mxt == 0.0 && this.myt == 0.0 ? 2 : 3;
                this.type = -1;
            }
        } else {
            this.state = this.mxx == 0.0 && this.myy == 0.0 ? (this.mxt == 0.0 && this.myt == 0.0 ? 4 : 5) : (this.mxt == 0.0 && this.myt == 0.0 ? 6 : 7);
            this.type = -1;
        }
    }

    @Override
    public int getType() {
        if (this.type == -1) {
            this.updateState();
            if (this.type == -1) {
                this.type = this.calculateType();
            }
        }
        return this.type;
    }

    protected int calculateType() {
        int n = (this.state & 8) == 0 ? 0 : 128;
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                n |= 1;
            }
            case 6: {
                boolean bl;
                if (this.mxx * this.mxy + this.myx * this.myy != 0.0) {
                    n |= 0x20;
                    break;
                }
                boolean bl2 = this.mxx >= 0.0;
                boolean bl3 = bl = this.myy >= 0.0;
                if (bl2 == bl) {
                    if (this.mxx != this.myy || this.mxy != -this.myx) {
                        n |= 0x14;
                        break;
                    }
                    if (this.mxx * this.myy - this.mxy * this.myx != 1.0) {
                        n |= 0x12;
                        break;
                    }
                    n |= 0x10;
                    break;
                }
                if (this.mxx != -this.myy || this.mxy != this.myx) {
                    n |= 0x54;
                    break;
                }
                if (this.mxx * this.myy - this.mxy * this.myx != 1.0) {
                    n |= 0x52;
                    break;
                }
                n |= 0x50;
                break;
            }
            case 5: {
                n |= 1;
            }
            case 4: {
                boolean bl;
                boolean bl4 = this.mxy >= 0.0;
                boolean bl5 = bl = this.myx >= 0.0;
                if (bl4 != bl) {
                    if (this.mxy != -this.myx) {
                        n |= 0xC;
                        break;
                    }
                    if (this.mxy != 1.0 && this.mxy != -1.0) {
                        n |= 0xA;
                        break;
                    }
                    n |= 8;
                    break;
                }
                if (this.mxy == this.myx) {
                    n |= 0x4A;
                    break;
                }
                n |= 0x4C;
                break;
            }
            case 3: {
                n |= 1;
            }
            case 2: {
                boolean bl;
                boolean bl6 = this.mxx >= 0.0;
                boolean bl7 = bl = this.myy >= 0.0;
                if (bl6 == bl) {
                    if (bl6) {
                        if (this.mxx == this.myy) {
                            n |= 2;
                            break;
                        }
                        n |= 4;
                        break;
                    }
                    if (this.mxx != this.myy) {
                        n |= 0xC;
                        break;
                    }
                    if (this.mxx != -1.0) {
                        n |= 0xA;
                        break;
                    }
                    n |= 8;
                    break;
                }
                if (this.mxx == -this.myy) {
                    if (this.mxx == 1.0 || this.mxx == -1.0) {
                        n |= 0x40;
                        break;
                    }
                    n |= 0x42;
                    break;
                }
                n |= 0x44;
                break;
            }
            case 1: {
                n |= 1;
            }
            case 0: 
        }
        return n;
    }

    @Override
    public double getMxx() {
        return this.mxx;
    }

    @Override
    public double getMyy() {
        return this.myy;
    }

    @Override
    public double getMxy() {
        return this.mxy;
    }

    @Override
    public double getMyx() {
        return this.myx;
    }

    @Override
    public double getMxt() {
        return this.mxt;
    }

    @Override
    public double getMyt() {
        return this.myt;
    }

    @Override
    public boolean isIdentity() {
        return this.state == 0 || this.getType() == 0;
    }

    @Override
    public boolean isTranslateOrIdentity() {
        return this.state <= 1 || this.getType() <= 1;
    }

    @Override
    public boolean is2D() {
        return this.state < 8 || this.getType() <= 127;
    }

    @Override
    public double getDeterminant() {
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                return this.mxx * this.myy - this.mxy * this.myx;
            }
            case 4: 
            case 5: {
                return -(this.mxy * this.myx);
            }
            case 2: 
            case 3: {
                return this.mxx * this.myy;
            }
            case 0: 
            case 1: 
        }
        return 1.0;
    }

    protected abstract void reset3Delements();

    @Override
    public void setToIdentity() {
        this.myy = 1.0;
        this.mxx = 1.0;
        this.myt = 0.0;
        this.mxt = 0.0;
        this.mxy = 0.0;
        this.myx = 0.0;
        this.reset3Delements();
        this.state = 0;
        this.type = 0;
    }

    public void setTransform(double d, double d2, double d3, double d4, double d5, double d6) {
        this.mxx = d;
        this.myx = d2;
        this.mxy = d3;
        this.myy = d4;
        this.mxt = d5;
        this.myt = d6;
        this.reset3Delements();
        this.updateState2D();
    }

    public void setToShear(double d, double d2) {
        this.mxx = 1.0;
        this.mxy = d;
        this.myx = d2;
        this.myy = 1.0;
        this.mxt = 0.0;
        this.myt = 0.0;
        this.reset3Delements();
        if (d != 0.0 || d2 != 0.0) {
            this.state = 6;
            this.type = -1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public Point2D transform(Point2D point2D) {
        return this.transform(point2D, point2D);
    }

    @Override
    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D();
        }
        double d = point2D.x;
        double d2 = point2D.y;
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                point2D2.setLocation((float)(d * this.mxx + d2 * this.mxy + this.mxt), (float)(d * this.myx + d2 * this.myy + this.myt));
                return point2D2;
            }
            case 6: {
                point2D2.setLocation((float)(d * this.mxx + d2 * this.mxy), (float)(d * this.myx + d2 * this.myy));
                return point2D2;
            }
            case 5: {
                point2D2.setLocation((float)(d2 * this.mxy + this.mxt), (float)(d * this.myx + this.myt));
                return point2D2;
            }
            case 4: {
                point2D2.setLocation((float)(d2 * this.mxy), (float)(d * this.myx));
                return point2D2;
            }
            case 3: {
                point2D2.setLocation((float)(d * this.mxx + this.mxt), (float)(d2 * this.myy + this.myt));
                return point2D2;
            }
            case 2: {
                point2D2.setLocation((float)(d * this.mxx), (float)(d2 * this.myy));
                return point2D2;
            }
            case 1: {
                point2D2.setLocation((float)(d + this.mxt), (float)(d2 + this.myt));
                return point2D2;
            }
            case 0: 
        }
        point2D2.setLocation((float)d, (float)d2);
        return point2D2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                vec3d2.x = d * this.mxx + d2 * this.mxy + this.mxt;
                vec3d2.y = d * this.myx + d2 * this.myy + this.myt;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 6: {
                vec3d2.x = d * this.mxx + d2 * this.mxy;
                vec3d2.y = d * this.myx + d2 * this.myy;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 5: {
                vec3d2.x = d2 * this.mxy + this.mxt;
                vec3d2.y = d * this.myx + this.myt;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 4: {
                vec3d2.x = d2 * this.mxy;
                vec3d2.y = d * this.myx;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 3: {
                vec3d2.x = d * this.mxx + this.mxt;
                vec3d2.y = d2 * this.myy + this.myt;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 2: {
                vec3d2.x = d * this.mxx;
                vec3d2.y = d2 * this.myy;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 1: {
                vec3d2.x = d + this.mxt;
                vec3d2.y = d2 + this.myt;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 0: 
        }
        vec3d2.x = d;
        vec3d2.y = d2;
        vec3d2.z = d3;
        return vec3d2;
    }

    @Override
    public Vec3d deltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                vec3d2.x = d * this.mxx + d2 * this.mxy;
                vec3d2.y = d * this.myx + d2 * this.myy;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 4: 
            case 5: {
                vec3d2.x = d2 * this.mxy;
                vec3d2.y = d * this.myx;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 2: 
            case 3: {
                vec3d2.x = d * this.mxx;
                vec3d2.y = d2 * this.myy;
                vec3d2.z = d3;
                return vec3d2;
            }
            case 0: 
            case 1: 
        }
        vec3d2.x = d;
        vec3d2.y = d2;
        vec3d2.z = d3;
        return vec3d2;
    }

    private BaseBounds transform2DBounds(RectBounds rectBounds, RectBounds rectBounds2) {
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                double d = rectBounds.getMinX();
                double d2 = rectBounds.getMinY();
                double d3 = rectBounds.getMaxX();
                double d4 = rectBounds.getMaxY();
                rectBounds2.setBoundsAndSort((float)(d * this.mxx + d2 * this.mxy), (float)(d * this.myx + d2 * this.myy), (float)(d3 * this.mxx + d4 * this.mxy), (float)(d3 * this.myx + d4 * this.myy));
                rectBounds2.add((float)(d * this.mxx + d4 * this.mxy), (float)(d * this.myx + d4 * this.myy));
                rectBounds2.add((float)(d3 * this.mxx + d2 * this.mxy), (float)(d3 * this.myx + d2 * this.myy));
                rectBounds2.setBounds((float)((double)rectBounds2.getMinX() + this.mxt), (float)((double)rectBounds2.getMinY() + this.myt), (float)((double)rectBounds2.getMaxX() + this.mxt), (float)((double)rectBounds2.getMaxY() + this.myt));
                break;
            }
            case 5: {
                rectBounds2.setBoundsAndSort((float)((double)rectBounds.getMinY() * this.mxy + this.mxt), (float)((double)rectBounds.getMinX() * this.myx + this.myt), (float)((double)rectBounds.getMaxY() * this.mxy + this.mxt), (float)((double)rectBounds.getMaxX() * this.myx + this.myt));
                break;
            }
            case 4: {
                rectBounds2.setBoundsAndSort((float)((double)rectBounds.getMinY() * this.mxy), (float)((double)rectBounds.getMinX() * this.myx), (float)((double)rectBounds.getMaxY() * this.mxy), (float)((double)rectBounds.getMaxX() * this.myx));
                break;
            }
            case 3: {
                rectBounds2.setBoundsAndSort((float)((double)rectBounds.getMinX() * this.mxx + this.mxt), (float)((double)rectBounds.getMinY() * this.myy + this.myt), (float)((double)rectBounds.getMaxX() * this.mxx + this.mxt), (float)((double)rectBounds.getMaxY() * this.myy + this.myt));
                break;
            }
            case 2: {
                rectBounds2.setBoundsAndSort((float)((double)rectBounds.getMinX() * this.mxx), (float)((double)rectBounds.getMinY() * this.myy), (float)((double)rectBounds.getMaxX() * this.mxx), (float)((double)rectBounds.getMaxY() * this.myy));
                break;
            }
            case 1: {
                rectBounds2.setBounds((float)((double)rectBounds.getMinX() + this.mxt), (float)((double)rectBounds.getMinY() + this.myt), (float)((double)rectBounds.getMaxX() + this.mxt), (float)((double)rectBounds.getMaxY() + this.myt));
                break;
            }
            case 0: {
                if (rectBounds == rectBounds2) break;
                rectBounds2.setBounds(rectBounds);
            }
        }
        return rectBounds2;
    }

    private BaseBounds transform3DBounds(BaseBounds baseBounds, BaseBounds baseBounds2) {
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                double d = baseBounds.getMinX();
                double d2 = baseBounds.getMinY();
                double d3 = baseBounds.getMinZ();
                double d4 = baseBounds.getMaxX();
                double d5 = baseBounds.getMaxY();
                double d6 = baseBounds.getMaxZ();
                baseBounds2.setBoundsAndSort((float)(d * this.mxx + d2 * this.mxy), (float)(d * this.myx + d2 * this.myy), (float)d3, (float)(d4 * this.mxx + d5 * this.mxy), (float)(d4 * this.myx + d5 * this.myy), (float)d6);
                baseBounds2.add((float)(d * this.mxx + d5 * this.mxy), (float)(d * this.myx + d5 * this.myy), 0.0f);
                baseBounds2.add((float)(d4 * this.mxx + d2 * this.mxy), (float)(d4 * this.myx + d2 * this.myy), 0.0f);
                baseBounds2.deriveWithNewBounds((float)((double)baseBounds2.getMinX() + this.mxt), (float)((double)baseBounds2.getMinY() + this.myt), baseBounds2.getMinZ(), (float)((double)baseBounds2.getMaxX() + this.mxt), (float)((double)baseBounds2.getMaxY() + this.myt), baseBounds2.getMaxZ());
                break;
            }
            case 5: {
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinY() * this.mxy + this.mxt), (float)((double)baseBounds.getMinX() * this.myx + this.myt), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxY() * this.mxy + this.mxt), (float)((double)baseBounds.getMaxX() * this.myx + this.myt), baseBounds.getMaxZ());
                break;
            }
            case 4: {
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinY() * this.mxy), (float)((double)baseBounds.getMinX() * this.myx), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxY() * this.mxy), (float)((double)baseBounds.getMaxX() * this.myx), baseBounds.getMaxZ());
                break;
            }
            case 3: {
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinX() * this.mxx + this.mxt), (float)((double)baseBounds.getMinY() * this.myy + this.myt), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxX() * this.mxx + this.mxt), (float)((double)baseBounds.getMaxY() * this.myy + this.myt), baseBounds.getMaxZ());
                break;
            }
            case 2: {
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinX() * this.mxx), (float)((double)baseBounds.getMinY() * this.myy), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxX() * this.mxx), (float)((double)baseBounds.getMaxY() * this.myy), baseBounds.getMaxZ());
                break;
            }
            case 1: {
                baseBounds2 = baseBounds2.deriveWithNewBounds((float)((double)baseBounds.getMinX() + this.mxt), (float)((double)baseBounds.getMinY() + this.myt), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxX() + this.mxt), (float)((double)baseBounds.getMaxY() + this.myt), baseBounds.getMaxZ());
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
    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (baseBounds.getBoundsType() != BaseBounds.BoundsType.RECTANGLE || baseBounds2.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            return this.transform3DBounds(baseBounds, baseBounds2);
        }
        return this.transform2DBounds((RectBounds)baseBounds, (RectBounds)baseBounds2);
    }

    @Override
    public void transform(Rectangle rectangle, Rectangle rectangle2) {
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                RectBounds rectBounds = new RectBounds(rectangle);
                rectBounds = (RectBounds)this.transform(rectBounds, rectBounds);
                rectangle2.setBounds(rectBounds);
                return;
            }
            case 1: {
                Translate2D.transform(rectangle, rectangle2, this.mxt, this.myt);
                return;
            }
            case 0: 
        }
        if (rectangle2 != rectangle) {
            rectangle2.setBounds(rectangle);
        }
    }

    @Override
    public void transform(float[] arrf, int n, float[] arrf2, int n2, int n3) {
        this.doTransform(arrf, n, arrf2, n2, n3, this.state & 7);
    }

    @Override
    public void deltaTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) {
        this.doTransform(arrf, n, arrf2, n2, n3, this.state & 6);
    }

    private void doTransform(float[] arrf, int n, float[] arrf2, int n2, int n3, int n4) {
        if (arrf2 == arrf && n2 > n && n2 < n + n3 * 2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
            n = n2;
        }
        switch (n4) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                while (--n3 >= 0) {
                    double d7 = arrf[n++];
                    double d8 = arrf[n++];
                    arrf2[n2++] = (float)(d * d7 + d2 * d8 + d3);
                    arrf2[n2++] = (float)(d4 * d7 + d5 * d8 + d6);
                }
                return;
            }
            case 6: {
                double d = this.mxx;
                double d9 = this.mxy;
                double d10 = this.myx;
                double d11 = this.myy;
                while (--n3 >= 0) {
                    double d12 = arrf[n++];
                    double d13 = arrf[n++];
                    arrf2[n2++] = (float)(d * d12 + d9 * d13);
                    arrf2[n2++] = (float)(d10 * d12 + d11 * d13);
                }
                return;
            }
            case 5: {
                double d = this.mxy;
                double d14 = this.mxt;
                double d15 = this.myx;
                double d16 = this.myt;
                while (--n3 >= 0) {
                    double d17 = arrf[n++];
                    arrf2[n2++] = (float)(d * (double)arrf[n++] + d14);
                    arrf2[n2++] = (float)(d15 * d17 + d16);
                }
                return;
            }
            case 4: {
                double d = this.mxy;
                double d18 = this.myx;
                while (--n3 >= 0) {
                    double d19 = arrf[n++];
                    arrf2[n2++] = (float)(d * (double)arrf[n++]);
                    arrf2[n2++] = (float)(d18 * d19);
                }
                return;
            }
            case 3: {
                double d = this.mxx;
                double d20 = this.mxt;
                double d21 = this.myy;
                double d22 = this.myt;
                while (--n3 >= 0) {
                    arrf2[n2++] = (float)(d * (double)arrf[n++] + d20);
                    arrf2[n2++] = (float)(d21 * (double)arrf[n++] + d22);
                }
                return;
            }
            case 2: {
                double d = this.mxx;
                double d23 = this.myy;
                while (--n3 >= 0) {
                    arrf2[n2++] = (float)(d * (double)arrf[n++]);
                    arrf2[n2++] = (float)(d23 * (double)arrf[n++]);
                }
                return;
            }
            case 1: {
                double d = this.mxt;
                double d24 = this.myt;
                while (--n3 >= 0) {
                    arrf2[n2++] = (float)((double)arrf[n++] + d);
                    arrf2[n2++] = (float)((double)arrf[n++] + d24);
                }
                return;
            }
            case 0: 
        }
        if (arrf != arrf2 || n != n2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
        }
    }

    @Override
    public void transform(double[] arrd, int n, double[] arrd2, int n2, int n3) {
        this.doTransform(arrd, n, arrd2, n2, n3, this.state & 7);
    }

    @Override
    public void deltaTransform(double[] arrd, int n, double[] arrd2, int n2, int n3) {
        this.doTransform(arrd, n, arrd2, n2, n3, this.state & 6);
    }

    private void doTransform(double[] arrd, int n, double[] arrd2, int n2, int n3, int n4) {
        if (arrd2 == arrd && n2 > n && n2 < n + n3 * 2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
            n = n2;
        }
        switch (n4) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                while (--n3 >= 0) {
                    double d7 = arrd[n++];
                    double d8 = arrd[n++];
                    arrd2[n2++] = d * d7 + d2 * d8 + d3;
                    arrd2[n2++] = d4 * d7 + d5 * d8 + d6;
                }
                return;
            }
            case 6: {
                double d = this.mxx;
                double d9 = this.mxy;
                double d10 = this.myx;
                double d11 = this.myy;
                while (--n3 >= 0) {
                    double d12 = arrd[n++];
                    double d13 = arrd[n++];
                    arrd2[n2++] = d * d12 + d9 * d13;
                    arrd2[n2++] = d10 * d12 + d11 * d13;
                }
                return;
            }
            case 5: {
                double d = this.mxy;
                double d14 = this.mxt;
                double d15 = this.myx;
                double d16 = this.myt;
                while (--n3 >= 0) {
                    double d17 = arrd[n++];
                    arrd2[n2++] = d * arrd[n++] + d14;
                    arrd2[n2++] = d15 * d17 + d16;
                }
                return;
            }
            case 4: {
                double d = this.mxy;
                double d18 = this.myx;
                while (--n3 >= 0) {
                    double d19 = arrd[n++];
                    arrd2[n2++] = d * arrd[n++];
                    arrd2[n2++] = d18 * d19;
                }
                return;
            }
            case 3: {
                double d = this.mxx;
                double d20 = this.mxt;
                double d21 = this.myy;
                double d22 = this.myt;
                while (--n3 >= 0) {
                    arrd2[n2++] = d * arrd[n++] + d20;
                    arrd2[n2++] = d21 * arrd[n++] + d22;
                }
                return;
            }
            case 2: {
                double d = this.mxx;
                double d23 = this.myy;
                while (--n3 >= 0) {
                    arrd2[n2++] = d * arrd[n++];
                    arrd2[n2++] = d23 * arrd[n++];
                }
                return;
            }
            case 1: {
                double d = this.mxt;
                double d24 = this.myt;
                while (--n3 >= 0) {
                    arrd2[n2++] = arrd[n++] + d;
                    arrd2[n2++] = arrd[n++] + d24;
                }
                return;
            }
            case 0: 
        }
        if (arrd != arrd2 || n != n2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
        }
    }

    @Override
    public void transform(float[] arrf, int n, double[] arrd, int n2, int n3) {
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                while (--n3 >= 0) {
                    double d7 = arrf[n++];
                    double d8 = arrf[n++];
                    arrd[n2++] = d * d7 + d2 * d8 + d3;
                    arrd[n2++] = d4 * d7 + d5 * d8 + d6;
                }
                return;
            }
            case 6: {
                double d = this.mxx;
                double d9 = this.mxy;
                double d10 = this.myx;
                double d11 = this.myy;
                while (--n3 >= 0) {
                    double d12 = arrf[n++];
                    double d13 = arrf[n++];
                    arrd[n2++] = d * d12 + d9 * d13;
                    arrd[n2++] = d10 * d12 + d11 * d13;
                }
                return;
            }
            case 5: {
                double d = this.mxy;
                double d14 = this.mxt;
                double d15 = this.myx;
                double d16 = this.myt;
                while (--n3 >= 0) {
                    double d17 = arrf[n++];
                    arrd[n2++] = d * (double)arrf[n++] + d14;
                    arrd[n2++] = d15 * d17 + d16;
                }
                return;
            }
            case 4: {
                double d = this.mxy;
                double d18 = this.myx;
                while (--n3 >= 0) {
                    double d19 = arrf[n++];
                    arrd[n2++] = d * (double)arrf[n++];
                    arrd[n2++] = d18 * d19;
                }
                return;
            }
            case 3: {
                double d = this.mxx;
                double d20 = this.mxt;
                double d21 = this.myy;
                double d22 = this.myt;
                while (--n3 >= 0) {
                    arrd[n2++] = d * (double)arrf[n++] + d20;
                    arrd[n2++] = d21 * (double)arrf[n++] + d22;
                }
                return;
            }
            case 2: {
                double d = this.mxx;
                double d23 = this.myy;
                while (--n3 >= 0) {
                    arrd[n2++] = d * (double)arrf[n++];
                    arrd[n2++] = d23 * (double)arrf[n++];
                }
                return;
            }
            case 1: {
                double d = this.mxt;
                double d24 = this.myt;
                while (--n3 >= 0) {
                    arrd[n2++] = (double)arrf[n++] + d;
                    arrd[n2++] = (double)arrf[n++] + d24;
                }
                return;
            }
            case 0: 
        }
        while (--n3 >= 0) {
            arrd[n2++] = arrf[n++];
            arrd[n2++] = arrf[n++];
        }
    }

    @Override
    public void transform(double[] arrd, int n, float[] arrf, int n2, int n3) {
        switch (this.state & 7) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                while (--n3 >= 0) {
                    double d7 = arrd[n++];
                    double d8 = arrd[n++];
                    arrf[n2++] = (float)(d * d7 + d2 * d8 + d3);
                    arrf[n2++] = (float)(d4 * d7 + d5 * d8 + d6);
                }
                return;
            }
            case 6: {
                double d = this.mxx;
                double d9 = this.mxy;
                double d10 = this.myx;
                double d11 = this.myy;
                while (--n3 >= 0) {
                    double d12 = arrd[n++];
                    double d13 = arrd[n++];
                    arrf[n2++] = (float)(d * d12 + d9 * d13);
                    arrf[n2++] = (float)(d10 * d12 + d11 * d13);
                }
                return;
            }
            case 5: {
                double d = this.mxy;
                double d14 = this.mxt;
                double d15 = this.myx;
                double d16 = this.myt;
                while (--n3 >= 0) {
                    double d17 = arrd[n++];
                    arrf[n2++] = (float)(d * arrd[n++] + d14);
                    arrf[n2++] = (float)(d15 * d17 + d16);
                }
                return;
            }
            case 4: {
                double d = this.mxy;
                double d18 = this.myx;
                while (--n3 >= 0) {
                    double d19 = arrd[n++];
                    arrf[n2++] = (float)(d * arrd[n++]);
                    arrf[n2++] = (float)(d18 * d19);
                }
                return;
            }
            case 3: {
                double d = this.mxx;
                double d20 = this.mxt;
                double d21 = this.myy;
                double d22 = this.myt;
                while (--n3 >= 0) {
                    arrf[n2++] = (float)(d * arrd[n++] + d20);
                    arrf[n2++] = (float)(d21 * arrd[n++] + d22);
                }
                return;
            }
            case 2: {
                double d = this.mxx;
                double d23 = this.myy;
                while (--n3 >= 0) {
                    arrf[n2++] = (float)(d * arrd[n++]);
                    arrf[n2++] = (float)(d23 * arrd[n++]);
                }
                return;
            }
            case 1: {
                double d = this.mxt;
                double d24 = this.myt;
                while (--n3 >= 0) {
                    arrf[n2++] = (float)(arrd[n++] + d);
                    arrf[n2++] = (float)(arrd[n++] + d24);
                }
                return;
            }
            case 0: 
        }
        while (--n3 >= 0) {
            arrf[n2++] = (float)arrd[n++];
            arrf[n2++] = (float)arrd[n++];
        }
    }

    @Override
    public Point2D inverseTransform(Point2D point2D, Point2D point2D2) throws NoninvertibleTransformException {
        if (point2D2 == null) {
            point2D2 = new Point2D();
        }
        double d = point2D.x;
        double d2 = point2D.y;
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                d -= this.mxt;
                d2 -= this.myt;
            }
            case 6: {
                double d3 = this.mxx * this.myy - this.mxy * this.myx;
                if (d3 == 0.0 || Math.abs(d3) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d3);
                }
                point2D2.setLocation((float)((d * this.myy - d2 * this.mxy) / d3), (float)((d2 * this.mxx - d * this.myx) / d3));
                return point2D2;
            }
            case 5: {
                d -= this.mxt;
                d2 -= this.myt;
            }
            case 4: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                point2D2.setLocation((float)(d2 / this.myx), (float)(d / this.mxy));
                return point2D2;
            }
            case 3: {
                d -= this.mxt;
                d2 -= this.myt;
            }
            case 2: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                point2D2.setLocation((float)(d / this.mxx), (float)(d2 / this.myy));
                return point2D2;
            }
            case 1: {
                point2D2.setLocation((float)(d - this.mxt), (float)(d2 - this.myt));
                return point2D2;
            }
            case 0: 
        }
        point2D2.setLocation((float)d, (float)d2);
        return point2D2;
    }

    @Override
    public Vec3d inverseTransform(Vec3d vec3d, Vec3d vec3d2) throws NoninvertibleTransformException {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                d -= this.mxt;
                d2 -= this.myt;
            }
            case 6: {
                double d4 = this.mxx * this.myy - this.mxy * this.myx;
                if (d4 == 0.0 || Math.abs(d4) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d4);
                }
                vec3d2.set((d * this.myy - d2 * this.mxy) / d4, (d2 * this.mxx - d * this.myx) / d4, d3);
                return vec3d2;
            }
            case 5: {
                d -= this.mxt;
                d2 -= this.myt;
            }
            case 4: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                vec3d2.set(d2 / this.myx, d / this.mxy, d3);
                return vec3d2;
            }
            case 3: {
                d -= this.mxt;
                d2 -= this.myt;
            }
            case 2: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                vec3d2.set(d / this.mxx, d2 / this.myy, d3);
                return vec3d2;
            }
            case 1: {
                vec3d2.set(d - this.mxt, d2 - this.myt, d3);
                return vec3d2;
            }
            case 0: 
        }
        vec3d2.set(d, d2, d3);
        return vec3d2;
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d vec3d, Vec3d vec3d2) throws NoninvertibleTransformException {
        if (vec3d2 == null) {
            vec3d2 = new Vec3d();
        }
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                double d4 = this.mxx * this.myy - this.mxy * this.myx;
                if (d4 == 0.0 || Math.abs(d4) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d4);
                }
                vec3d2.set((d * this.myy - d2 * this.mxy) / d4, (d2 * this.mxx - d * this.myx) / d4, d3);
                return vec3d2;
            }
            case 4: 
            case 5: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                vec3d2.set(d2 / this.myx, d / this.mxy, d3);
                return vec3d2;
            }
            case 2: 
            case 3: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                vec3d2.set(d / this.mxx, d2 / this.myy, d3);
                return vec3d2;
            }
            case 0: 
            case 1: 
        }
        vec3d2.set(d, d2, d3);
        return vec3d2;
    }

    private BaseBounds inversTransform2DBounds(RectBounds rectBounds, RectBounds rectBounds2) throws NoninvertibleTransformException {
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                double d = this.mxx * this.myy - this.mxy * this.myx;
                if (d == 0.0 || Math.abs(d) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d);
                }
                double d2 = (double)rectBounds.getMinX() - this.mxt;
                double d3 = (double)rectBounds.getMinY() - this.myt;
                double d4 = (double)rectBounds.getMaxX() - this.mxt;
                double d5 = (double)rectBounds.getMaxY() - this.myt;
                rectBounds2.setBoundsAndSort((float)((d2 * this.myy - d3 * this.mxy) / d), (float)((d3 * this.mxx - d2 * this.myx) / d), (float)((d4 * this.myy - d5 * this.mxy) / d), (float)((d5 * this.mxx - d4 * this.myx) / d));
                rectBounds2.add((float)((d4 * this.myy - d3 * this.mxy) / d), (float)((d3 * this.mxx - d4 * this.myx) / d));
                rectBounds2.add((float)((d2 * this.myy - d5 * this.mxy) / d), (float)((d5 * this.mxx - d2 * this.myx) / d));
                return rectBounds2;
            }
            case 5: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                rectBounds2.setBoundsAndSort((float)(((double)rectBounds.getMinY() - this.myt) / this.myx), (float)(((double)rectBounds.getMinX() - this.mxt) / this.mxy), (float)(((double)rectBounds.getMaxY() - this.myt) / this.myx), (float)(((double)rectBounds.getMaxX() - this.mxt) / this.mxy));
                break;
            }
            case 4: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                rectBounds2.setBoundsAndSort((float)((double)rectBounds.getMinY() / this.myx), (float)((double)rectBounds.getMinX() / this.mxy), (float)((double)rectBounds.getMaxY() / this.myx), (float)((double)rectBounds.getMaxX() / this.mxy));
                break;
            }
            case 3: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                rectBounds2.setBoundsAndSort((float)(((double)rectBounds.getMinX() - this.mxt) / this.mxx), (float)(((double)rectBounds.getMinY() - this.myt) / this.myy), (float)(((double)rectBounds.getMaxX() - this.mxt) / this.mxx), (float)(((double)rectBounds.getMaxY() - this.myt) / this.myy));
                break;
            }
            case 2: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                rectBounds2.setBoundsAndSort((float)((double)rectBounds.getMinX() / this.mxx), (float)((double)rectBounds.getMinY() / this.myy), (float)((double)rectBounds.getMaxX() / this.mxx), (float)((double)rectBounds.getMaxY() / this.myy));
                break;
            }
            case 1: {
                rectBounds2.setBounds((float)((double)rectBounds.getMinX() - this.mxt), (float)((double)rectBounds.getMinY() - this.myt), (float)((double)rectBounds.getMaxX() - this.mxt), (float)((double)rectBounds.getMaxY() - this.myt));
                break;
            }
            case 0: {
                if (rectBounds2 == rectBounds) break;
                rectBounds2.setBounds(rectBounds);
            }
        }
        return rectBounds2;
    }

    private BaseBounds inversTransform3DBounds(BaseBounds baseBounds, BaseBounds baseBounds2) throws NoninvertibleTransformException {
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                double d = this.mxx * this.myy - this.mxy * this.myx;
                if (d == 0.0 || Math.abs(d) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d);
                }
                double d2 = (double)baseBounds.getMinX() - this.mxt;
                double d3 = (double)baseBounds.getMinY() - this.myt;
                double d4 = baseBounds.getMinZ();
                double d5 = (double)baseBounds.getMaxX() - this.mxt;
                double d6 = (double)baseBounds.getMaxY() - this.myt;
                double d7 = baseBounds.getMaxZ();
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((d2 * this.myy - d3 * this.mxy) / d), (float)((d3 * this.mxx - d2 * this.myx) / d), (float)(d4 / d), (float)((d5 * this.myy - d6 * this.mxy) / d), (float)((d6 * this.mxx - d5 * this.myx) / d), (float)(d7 / d));
                baseBounds2.add((float)((d5 * this.myy - d3 * this.mxy) / d), (float)((d3 * this.mxx - d5 * this.myx) / d), 0.0f);
                baseBounds2.add((float)((d2 * this.myy - d6 * this.mxy) / d), (float)((d6 * this.mxx - d2 * this.myx) / d), 0.0f);
                return baseBounds2;
            }
            case 3: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)(((double)baseBounds.getMinX() - this.mxt) / this.mxx), (float)(((double)baseBounds.getMinY() - this.myt) / this.myy), baseBounds.getMinZ(), (float)(((double)baseBounds.getMaxX() - this.mxt) / this.mxx), (float)(((double)baseBounds.getMaxY() - this.myt) / this.myy), baseBounds.getMaxZ());
                break;
            }
            case 2: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                baseBounds2 = baseBounds2.deriveWithNewBoundsAndSort((float)((double)baseBounds.getMinX() / this.mxx), (float)((double)baseBounds.getMinY() / this.myy), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxX() / this.mxx), (float)((double)baseBounds.getMaxY() / this.myy), baseBounds.getMaxZ());
                break;
            }
            case 1: {
                baseBounds2 = baseBounds2.deriveWithNewBounds((float)((double)baseBounds.getMinX() - this.mxt), (float)((double)baseBounds.getMinY() - this.myt), baseBounds.getMinZ(), (float)((double)baseBounds.getMaxX() - this.mxt), (float)((double)baseBounds.getMaxY() - this.myt), baseBounds.getMaxZ());
                break;
            }
            case 0: {
                if (baseBounds2 == baseBounds) break;
                baseBounds2 = baseBounds2.deriveWithNewBounds(baseBounds);
            }
        }
        return baseBounds2;
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds baseBounds, BaseBounds baseBounds2) throws NoninvertibleTransformException {
        if (baseBounds.getBoundsType() != BaseBounds.BoundsType.RECTANGLE || baseBounds2.getBoundsType() != BaseBounds.BoundsType.RECTANGLE) {
            return this.inversTransform3DBounds(baseBounds, baseBounds2);
        }
        return this.inversTransform2DBounds((RectBounds)baseBounds, (RectBounds)baseBounds2);
    }

    @Override
    public void inverseTransform(Rectangle rectangle, Rectangle rectangle2) throws NoninvertibleTransformException {
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                RectBounds rectBounds = new RectBounds(rectangle);
                rectBounds = (RectBounds)this.inverseTransform(rectBounds, rectBounds);
                rectangle2.setBounds(rectBounds);
                return;
            }
            case 1: {
                Translate2D.transform(rectangle, rectangle2, -this.mxt, -this.myt);
                return;
            }
            case 0: 
        }
        if (rectangle2 != rectangle) {
            rectangle2.setBounds(rectangle);
        }
    }

    @Override
    public void inverseTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) throws NoninvertibleTransformException {
        this.doInverseTransform(arrf, n, arrf2, n2, n3, this.state);
    }

    @Override
    public void inverseDeltaTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) throws NoninvertibleTransformException {
        this.doInverseTransform(arrf, n, arrf2, n2, n3, this.state & 0xFFFFFFFE);
    }

    private void doInverseTransform(float[] arrf, int n, float[] arrf2, int n2, int n3, int n4) throws NoninvertibleTransformException {
        if (arrf2 == arrf && n2 > n && n2 < n + n3 * 2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
            n = n2;
        }
        switch (n4) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                double d7 = d * d5 - d2 * d4;
                if (d7 == 0.0 || Math.abs(d7) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d7);
                }
                while (--n3 >= 0) {
                    double d8 = (double)arrf[n++] - d3;
                    double d9 = (double)arrf[n++] - d6;
                    arrf2[n2++] = (float)((d8 * d5 - d9 * d2) / d7);
                    arrf2[n2++] = (float)((d9 * d - d8 * d4) / d7);
                }
                return;
            }
            case 6: {
                double d = this.mxx;
                double d10 = this.mxy;
                double d11 = this.myx;
                double d12 = this.myy;
                double d13 = d * d12 - d10 * d11;
                if (d13 == 0.0 || Math.abs(d13) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d13);
                }
                while (--n3 >= 0) {
                    double d14 = arrf[n++];
                    double d15 = arrf[n++];
                    arrf2[n2++] = (float)((d14 * d12 - d15 * d10) / d13);
                    arrf2[n2++] = (float)((d15 * d - d14 * d11) / d13);
                }
                return;
            }
            case 5: {
                double d = this.mxy;
                double d16 = this.mxt;
                double d17 = this.myx;
                double d18 = this.myt;
                if (d == 0.0 || d17 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    double d19 = (double)arrf[n++] - d16;
                    arrf2[n2++] = (float)(((double)arrf[n++] - d18) / d17);
                    arrf2[n2++] = (float)(d19 / d);
                }
                return;
            }
            case 4: {
                double d = this.mxy;
                double d20 = this.myx;
                if (d == 0.0 || d20 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    double d21 = arrf[n++];
                    arrf2[n2++] = (float)((double)arrf[n++] / d20);
                    arrf2[n2++] = (float)(d21 / d);
                }
                return;
            }
            case 3: {
                double d = this.mxx;
                double d22 = this.mxt;
                double d23 = this.myy;
                double d24 = this.myt;
                if (d == 0.0 || d23 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    arrf2[n2++] = (float)(((double)arrf[n++] - d22) / d);
                    arrf2[n2++] = (float)(((double)arrf[n++] - d24) / d23);
                }
                return;
            }
            case 2: {
                double d = this.mxx;
                double d25 = this.myy;
                if (d == 0.0 || d25 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    arrf2[n2++] = (float)((double)arrf[n++] / d);
                    arrf2[n2++] = (float)((double)arrf[n++] / d25);
                }
                return;
            }
            case 1: {
                double d = this.mxt;
                double d26 = this.myt;
                while (--n3 >= 0) {
                    arrf2[n2++] = (float)((double)arrf[n++] - d);
                    arrf2[n2++] = (float)((double)arrf[n++] - d26);
                }
                return;
            }
            case 0: 
        }
        if (arrf != arrf2 || n != n2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
        }
    }

    @Override
    public void inverseTransform(double[] arrd, int n, double[] arrd2, int n2, int n3) throws NoninvertibleTransformException {
        if (arrd2 == arrd && n2 > n && n2 < n + n3 * 2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
            n = n2;
        }
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                double d7 = d * d5 - d2 * d4;
                if (d7 == 0.0 || Math.abs(d7) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d7);
                }
                while (--n3 >= 0) {
                    double d8 = arrd[n++] - d3;
                    double d9 = arrd[n++] - d6;
                    arrd2[n2++] = (d8 * d5 - d9 * d2) / d7;
                    arrd2[n2++] = (d9 * d - d8 * d4) / d7;
                }
                return;
            }
            case 6: {
                double d = this.mxx;
                double d10 = this.mxy;
                double d11 = this.myx;
                double d12 = this.myy;
                double d13 = d * d12 - d10 * d11;
                if (d13 == 0.0 || Math.abs(d13) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d13);
                }
                while (--n3 >= 0) {
                    double d14 = arrd[n++];
                    double d15 = arrd[n++];
                    arrd2[n2++] = (d14 * d12 - d15 * d10) / d13;
                    arrd2[n2++] = (d15 * d - d14 * d11) / d13;
                }
                return;
            }
            case 5: {
                double d = this.mxy;
                double d16 = this.mxt;
                double d17 = this.myx;
                double d18 = this.myt;
                if (d == 0.0 || d17 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    double d19 = arrd[n++] - d16;
                    arrd2[n2++] = (arrd[n++] - d18) / d17;
                    arrd2[n2++] = d19 / d;
                }
                return;
            }
            case 4: {
                double d = this.mxy;
                double d20 = this.myx;
                if (d == 0.0 || d20 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    double d21 = arrd[n++];
                    arrd2[n2++] = arrd[n++] / d20;
                    arrd2[n2++] = d21 / d;
                }
                return;
            }
            case 3: {
                double d = this.mxx;
                double d22 = this.mxt;
                double d23 = this.myy;
                double d24 = this.myt;
                if (d == 0.0 || d23 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    arrd2[n2++] = (arrd[n++] - d22) / d;
                    arrd2[n2++] = (arrd[n++] - d24) / d23;
                }
                return;
            }
            case 2: {
                double d = this.mxx;
                double d25 = this.myy;
                if (d == 0.0 || d25 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                while (--n3 >= 0) {
                    arrd2[n2++] = arrd[n++] / d;
                    arrd2[n2++] = arrd[n++] / d25;
                }
                return;
            }
            case 1: {
                double d = this.mxt;
                double d26 = this.myt;
                while (--n3 >= 0) {
                    arrd2[n2++] = arrd[n++] - d;
                    arrd2[n2++] = arrd[n++] - d26;
                }
                return;
            }
            case 0: 
        }
        if (arrd != arrd2 || n != n2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
        }
    }

    @Override
    public Shape createTransformedShape(Shape shape) {
        if (shape == null) {
            return null;
        }
        return new Path2D(shape, this);
    }

    public void translate(double d, double d2) {
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                this.mxt = d * this.mxx + d2 * this.mxy + this.mxt;
                this.myt = d * this.myx + d2 * this.myy + this.myt;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 6;
                    if (this.type != -1) {
                        this.type &= 0xFFFFFFFE;
                    }
                }
                return;
            }
            case 6: {
                this.mxt = d * this.mxx + d2 * this.mxy;
                this.myt = d * this.myx + d2 * this.myy;
                if (this.mxt != 0.0 || this.myt != 0.0) {
                    this.state = 7;
                    this.type |= 1;
                }
                return;
            }
            case 5: {
                this.mxt = d2 * this.mxy + this.mxt;
                this.myt = d * this.myx + this.myt;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 4;
                    if (this.type != -1) {
                        this.type &= 0xFFFFFFFE;
                    }
                }
                return;
            }
            case 4: {
                this.mxt = d2 * this.mxy;
                this.myt = d * this.myx;
                if (this.mxt != 0.0 || this.myt != 0.0) {
                    this.state = 5;
                    this.type |= 1;
                }
                return;
            }
            case 3: {
                this.mxt = d * this.mxx + this.mxt;
                this.myt = d2 * this.myy + this.myt;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 2;
                    if (this.type != -1) {
                        this.type &= 0xFFFFFFFE;
                    }
                }
                return;
            }
            case 2: {
                this.mxt = d * this.mxx;
                this.myt = d2 * this.myy;
                if (this.mxt != 0.0 || this.myt != 0.0) {
                    this.state = 3;
                    this.type |= 1;
                }
                return;
            }
            case 1: {
                this.mxt = d + this.mxt;
                this.myt = d2 + this.myt;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 0;
                    this.type = 0;
                }
                return;
            }
            case 0: 
        }
        this.mxt = d;
        this.myt = d2;
        if (d != 0.0 || d2 != 0.0) {
            this.state = 1;
            this.type = 1;
        }
    }

    protected final void rotate90() {
        double d = this.mxx;
        this.mxx = this.mxy;
        this.mxy = -d;
        d = this.myx;
        this.myx = this.myy;
        this.myy = -d;
        int n = rot90conversion[this.state];
        if ((n & 6) == 2 && this.mxx == 1.0 && this.myy == 1.0) {
            n -= 2;
        }
        this.state = n;
        this.type = -1;
    }

    protected final void rotate180() {
        this.mxx = -this.mxx;
        this.myy = -this.myy;
        int n = this.state;
        if ((n & 4) != 0) {
            this.mxy = -this.mxy;
            this.myx = -this.myx;
        } else {
            this.state = this.mxx == 1.0 && this.myy == 1.0 ? n & 0xFFFFFFFD : n | 2;
        }
        this.type = -1;
    }

    protected final void rotate270() {
        double d = this.mxx;
        this.mxx = -this.mxy;
        this.mxy = d;
        d = this.myx;
        this.myx = -this.myy;
        this.myy = d;
        int n = rot90conversion[this.state];
        if ((n & 6) == 2 && this.mxx == 1.0 && this.myy == 1.0) {
            n -= 2;
        }
        this.state = n;
        this.type = -1;
    }

    public void rotate(double d) {
        double d2 = Math.sin(d);
        if (d2 == 1.0) {
            this.rotate90();
        } else if (d2 == -1.0) {
            this.rotate270();
        } else {
            double d3 = Math.cos(d);
            if (d3 == -1.0) {
                this.rotate180();
            } else if (d3 != 1.0) {
                double d4 = this.mxx;
                double d5 = this.mxy;
                this.mxx = d3 * d4 + d2 * d5;
                this.mxy = -d2 * d4 + d3 * d5;
                d4 = this.myx;
                d5 = this.myy;
                this.myx = d3 * d4 + d2 * d5;
                this.myy = -d2 * d4 + d3 * d5;
                this.updateState2D();
            }
        }
    }

    public void scale(double d, double d2) {
        int n = this.state;
        switch (n) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                this.mxx *= d;
                this.myy *= d2;
            }
            case 4: 
            case 5: {
                this.mxy *= d2;
                this.myx *= d;
                if (this.mxy == 0.0 && this.myx == 0.0) {
                    n &= 1;
                    if (this.mxx == 1.0 && this.myy == 1.0) {
                        this.type = n == 0 ? 0 : 1;
                    } else {
                        n |= 2;
                        this.type = -1;
                    }
                    this.state = n;
                }
                return;
            }
            case 2: 
            case 3: {
                this.mxx *= d;
                this.myy *= d2;
                if (this.mxx == 1.0 && this.myy == 1.0) {
                    this.state = n &= 1;
                    this.type = n == 0 ? 0 : 1;
                } else {
                    this.type = -1;
                }
                return;
            }
            case 0: 
            case 1: 
        }
        this.mxx = d;
        this.myy = d2;
        if (d != 1.0 || d2 != 1.0) {
            this.state = n | 2;
            this.type = -1;
        }
    }

    public void shear(double d, double d2) {
        int n = this.state;
        switch (n) {
            default: {
                AffineBase.stateError();
            }
            case 6: 
            case 7: {
                double d3 = this.mxx;
                double d4 = this.mxy;
                this.mxx = d3 + d4 * d2;
                this.mxy = d3 * d + d4;
                d3 = this.myx;
                d4 = this.myy;
                this.myx = d3 + d4 * d2;
                this.myy = d3 * d + d4;
                this.updateState2D();
                return;
            }
            case 4: 
            case 5: {
                this.mxx = this.mxy * d2;
                this.myy = this.myx * d;
                if (this.mxx != 0.0 || this.myy != 0.0) {
                    this.state = n | 2;
                }
                this.type = -1;
                return;
            }
            case 2: 
            case 3: {
                this.mxy = this.mxx * d;
                this.myx = this.myy * d2;
                if (this.mxy != 0.0 || this.myx != 0.0) {
                    this.state = n | 4;
                }
                this.type = -1;
                return;
            }
            case 0: 
            case 1: 
        }
        this.mxy = d;
        this.myx = d2;
        if (this.mxy != 0.0 || this.myx != 0.0) {
            this.state = n | 2 | 4;
            this.type = -1;
        }
    }

    public void concatenate(BaseTransform baseTransform) {
        switch (baseTransform.getDegree()) {
            case IDENTITY: {
                return;
            }
            case TRANSLATE_2D: {
                this.translate(baseTransform.getMxt(), baseTransform.getMyt());
                return;
            }
            case AFFINE_2D: {
                break;
            }
            default: {
                if (!baseTransform.is2D()) {
                    AffineBase.degreeError(BaseTransform.Degree.AFFINE_2D);
                }
                if (baseTransform instanceof AffineBase) break;
                baseTransform = new Affine2D(baseTransform);
            }
        }
        int n = this.state;
        AffineBase affineBase = (AffineBase)baseTransform;
        int n2 = affineBase.state;
        switch (n2 << 4 | n) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                return;
            }
            case 112: {
                this.mxy = affineBase.mxy;
                this.myx = affineBase.myx;
            }
            case 48: {
                this.mxx = affineBase.mxx;
                this.myy = affineBase.myy;
            }
            case 16: {
                this.mxt = affineBase.mxt;
                this.myt = affineBase.myt;
                this.state = n2;
                this.type = affineBase.type;
                return;
            }
            case 96: {
                this.mxy = affineBase.mxy;
                this.myx = affineBase.myx;
            }
            case 32: {
                this.mxx = affineBase.mxx;
                this.myy = affineBase.myy;
                this.state = n2;
                this.type = affineBase.type;
                return;
            }
            case 80: {
                this.mxt = affineBase.mxt;
                this.myt = affineBase.myt;
            }
            case 64: {
                this.mxy = affineBase.mxy;
                this.myx = affineBase.myx;
                this.myy = 0.0;
                this.mxx = 0.0;
                this.state = n2;
                this.type = affineBase.type;
                return;
            }
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: {
                this.translate(affineBase.mxt, affineBase.myt);
                return;
            }
            case 33: 
            case 34: 
            case 35: 
            case 36: 
            case 37: 
            case 38: 
            case 39: {
                this.scale(affineBase.mxx, affineBase.myy);
                return;
            }
            case 70: 
            case 71: {
                double d = affineBase.mxy;
                double d2 = affineBase.myx;
                double d3 = this.mxx;
                this.mxx = this.mxy * d2;
                this.mxy = d3 * d;
                d3 = this.myx;
                this.myx = this.myy * d2;
                this.myy = d3 * d;
                this.type = -1;
                return;
            }
            case 68: 
            case 69: {
                this.mxx = this.mxy * affineBase.myx;
                this.mxy = 0.0;
                this.myy = this.myx * affineBase.mxy;
                this.myx = 0.0;
                this.state = n ^ 6;
                this.type = -1;
                return;
            }
            case 66: 
            case 67: {
                this.mxy = this.mxx * affineBase.mxy;
                this.mxx = 0.0;
                this.myx = this.myy * affineBase.myx;
                this.myy = 0.0;
                this.state = n ^ 6;
                this.type = -1;
                return;
            }
            case 65: {
                this.mxx = 0.0;
                this.mxy = affineBase.mxy;
                this.myx = affineBase.myx;
                this.myy = 0.0;
                this.state = 5;
                this.type = -1;
                return;
            }
        }
        double d = affineBase.mxx;
        double d4 = affineBase.mxy;
        double d5 = affineBase.mxt;
        double d6 = affineBase.myx;
        double d7 = affineBase.myy;
        double d8 = affineBase.myt;
        switch (n) {
            default: {
                AffineBase.stateError();
            }
            case 6: {
                this.state = n | n2;
            }
            case 7: {
                double d9 = this.mxx;
                double d10 = this.mxy;
                this.mxx = d * d9 + d6 * d10;
                this.mxy = d4 * d9 + d7 * d10;
                this.mxt += d5 * d9 + d8 * d10;
                d9 = this.myx;
                d10 = this.myy;
                this.myx = d * d9 + d6 * d10;
                this.myy = d4 * d9 + d7 * d10;
                this.myt += d5 * d9 + d8 * d10;
                this.type = -1;
                return;
            }
            case 4: 
            case 5: {
                double d11 = this.mxy;
                this.mxx = d6 * d11;
                this.mxy = d7 * d11;
                this.mxt += d8 * d11;
                d11 = this.myx;
                this.myx = d * d11;
                this.myy = d4 * d11;
                this.myt += d5 * d11;
                break;
            }
            case 2: 
            case 3: {
                double d12 = this.mxx;
                this.mxx = d * d12;
                this.mxy = d4 * d12;
                this.mxt += d5 * d12;
                d12 = this.myy;
                this.myx = d6 * d12;
                this.myy = d7 * d12;
                this.myt += d8 * d12;
                break;
            }
            case 1: {
                this.mxx = d;
                this.mxy = d4;
                this.mxt += d5;
                this.myx = d6;
                this.myy = d7;
                this.myt += d8;
                this.state = n2 | 1;
                this.type = -1;
                return;
            }
        }
        this.updateState2D();
    }

    public void concatenate(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = this.mxx * d + this.mxy * d4;
        double d8 = this.mxx * d2 + this.mxy * d5;
        double d9 = this.mxx * d3 + this.mxy * d6 + this.mxt;
        double d10 = this.myx * d + this.myy * d4;
        double d11 = this.myx * d2 + this.myy * d5;
        double d12 = this.myx * d3 + this.myy * d6 + this.myt;
        this.mxx = d7;
        this.mxy = d8;
        this.mxt = d9;
        this.myx = d10;
        this.myy = d11;
        this.myt = d12;
        this.updateState();
    }

    @Override
    public void invert() throws NoninvertibleTransformException {
        switch (this.state) {
            default: {
                AffineBase.stateError();
            }
            case 7: {
                double d = this.mxx;
                double d2 = this.mxy;
                double d3 = this.mxt;
                double d4 = this.myx;
                double d5 = this.myy;
                double d6 = this.myt;
                double d7 = d * d5 - d2 * d4;
                if (d7 == 0.0 || Math.abs(d7) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d7);
                }
                this.mxx = d5 / d7;
                this.myx = -d4 / d7;
                this.mxy = -d2 / d7;
                this.myy = d / d7;
                this.mxt = (d2 * d6 - d5 * d3) / d7;
                this.myt = (d4 * d3 - d * d6) / d7;
                break;
            }
            case 6: {
                double d = this.mxx;
                double d8 = this.mxy;
                double d9 = this.myx;
                double d10 = this.myy;
                double d11 = d * d10 - d8 * d9;
                if (d11 == 0.0 || Math.abs(d11) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d11);
                }
                this.mxx = d10 / d11;
                this.myx = -d9 / d11;
                this.mxy = -d8 / d11;
                this.myy = d / d11;
                break;
            }
            case 5: {
                double d = this.mxy;
                double d12 = this.mxt;
                double d13 = this.myx;
                double d14 = this.myt;
                if (d == 0.0 || d13 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.myx = 1.0 / d;
                this.mxy = 1.0 / d13;
                this.mxt = -d14 / d13;
                this.myt = -d12 / d;
                break;
            }
            case 4: {
                double d = this.mxy;
                double d15 = this.myx;
                if (d == 0.0 || d15 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.myx = 1.0 / d;
                this.mxy = 1.0 / d15;
                break;
            }
            case 3: {
                double d = this.mxx;
                double d16 = this.mxt;
                double d17 = this.myy;
                double d18 = this.myt;
                if (d == 0.0 || d17 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.mxx = 1.0 / d;
                this.myy = 1.0 / d17;
                this.mxt = -d16 / d;
                this.myt = -d18 / d17;
                break;
            }
            case 2: {
                double d = this.mxx;
                double d19 = this.myy;
                if (d == 0.0 || d19 == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                this.mxx = 1.0 / d;
                this.myy = 1.0 / d19;
                break;
            }
            case 1: {
                this.mxt = -this.mxt;
                this.myt = -this.myt;
            }
            case 0: 
        }
    }
}

