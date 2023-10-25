/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PerspectiveFilter
extends TransformFilter {
    private float x0;
    private float y0;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float x3;
    private float y3;
    private float dx1;
    private float dy1;
    private float dx2;
    private float dy2;
    private float dx3;
    private float dy3;
    private float A;
    private float B;
    private float C;
    private float D;
    private float E;
    private float F;
    private float G;
    private float H;
    private float I;
    private float a11;
    private float a12;
    private float a13;
    private float a21;
    private float a22;
    private float a23;
    private float a31;
    private float a32;
    private float a33;
    private boolean scaled;
    private boolean clip = false;

    public PerspectiveFilter() {
        this(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }

    public PerspectiveFilter(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this.unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public boolean getClip() {
        return this.clip;
    }

    public void setCorners(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this.unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        this.scaled = true;
    }

    public void unitSquareToQuad(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.dx1 = x1 - x2;
        this.dy1 = y1 - y2;
        this.dx2 = x3 - x2;
        this.dy2 = y3 - y2;
        this.dx3 = x0 - x1 + x2 - x3;
        this.dy3 = y0 - y1 + y2 - y3;
        if (this.dx3 == 0.0f && this.dy3 == 0.0f) {
            this.a11 = x1 - x0;
            this.a21 = x2 - x1;
            this.a31 = x0;
            this.a12 = y1 - y0;
            this.a22 = y2 - y1;
            this.a32 = y0;
            this.a23 = 0.0f;
            this.a13 = 0.0f;
        } else {
            this.a13 = (this.dx3 * this.dy2 - this.dx2 * this.dy3) / (this.dx1 * this.dy2 - this.dy1 * this.dx2);
            this.a23 = (this.dx1 * this.dy3 - this.dy1 * this.dx3) / (this.dx1 * this.dy2 - this.dy1 * this.dx2);
            this.a11 = x1 - x0 + this.a13 * x1;
            this.a21 = x3 - x0 + this.a23 * x3;
            this.a31 = x0;
            this.a12 = y1 - y0 + this.a13 * y1;
            this.a22 = y3 - y0 + this.a23 * y3;
            this.a32 = y0;
        }
        this.a33 = 1.0f;
        this.scaled = false;
    }

    public void quadToUnitSquare(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this.unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        float ta11 = this.a22 * this.a33 - this.a32 * this.a23;
        float ta21 = this.a32 * this.a13 - this.a12 * this.a33;
        float ta31 = this.a12 * this.a23 - this.a22 * this.a13;
        float ta12 = this.a31 * this.a23 - this.a21 * this.a33;
        float ta22 = this.a11 * this.a33 - this.a31 * this.a13;
        float ta32 = this.a21 * this.a13 - this.a11 * this.a23;
        float ta13 = this.a21 * this.a32 - this.a31 * this.a22;
        float ta23 = this.a31 * this.a12 - this.a11 * this.a32;
        float ta33 = this.a11 * this.a22 - this.a21 * this.a12;
        float f = 1.0f / ta33;
        this.a11 = ta11 * f;
        this.a21 = ta12 * f;
        this.a31 = ta13 * f;
        this.a12 = ta21 * f;
        this.a22 = ta22 * f;
        this.a32 = ta23 * f;
        this.a13 = ta31 * f;
        this.a23 = ta32 * f;
        this.a33 = 1.0f;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.A = this.a22 * this.a33 - this.a32 * this.a23;
        this.B = this.a31 * this.a23 - this.a21 * this.a33;
        this.C = this.a21 * this.a32 - this.a31 * this.a22;
        this.D = this.a32 * this.a13 - this.a12 * this.a33;
        this.E = this.a11 * this.a33 - this.a31 * this.a13;
        this.F = this.a31 * this.a12 - this.a11 * this.a32;
        this.G = this.a12 * this.a23 - this.a22 * this.a13;
        this.H = this.a21 * this.a13 - this.a11 * this.a23;
        this.I = this.a11 * this.a22 - this.a21 * this.a12;
        if (!this.scaled) {
            int width = src.getWidth();
            int height = src.getHeight();
            float invWidth = 1.0f / (float)width;
            float invHeight = 1.0f / (float)height;
            this.A *= invWidth;
            this.D *= invWidth;
            this.G *= invWidth;
            this.B *= invHeight;
            this.E *= invHeight;
            this.H *= invHeight;
        }
        return super.filter(src, dst);
    }

    @Override
    protected void transformSpace(Rectangle rect) {
        if (this.scaled) {
            rect.x = (int)Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3));
            rect.y = (int)Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3));
            rect.width = (int)Math.max(Math.max(this.x0, this.x1), Math.max(this.x2, this.x3)) - rect.x;
            rect.height = (int)Math.max(Math.max(this.y0, this.y1), Math.max(this.y2, this.y3)) - rect.y;
            return;
        }
        if (!this.clip) {
            float w = (float)rect.getWidth();
            float h = (float)rect.getHeight();
            Rectangle r = new Rectangle();
            r.add(this.getPoint2D(new Point2D.Float(0.0f, 0.0f), null));
            r.add(this.getPoint2D(new Point2D.Float(w, 0.0f), null));
            r.add(this.getPoint2D(new Point2D.Float(0.0f, h), null));
            r.add(this.getPoint2D(new Point2D.Float(w, h), null));
            rect.setRect(r);
        }
    }

    public float getOriginX() {
        return this.x0 - (float)((int)Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3)));
    }

    public float getOriginY() {
        return this.y0 - (float)((int)Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3)));
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        if (this.clip) {
            return new Rectangle(0, 0, src.getWidth(), src.getHeight());
        }
        float w = src.getWidth();
        float h = src.getHeight();
        Rectangle2D.Float r = new Rectangle2D.Float();
        r.add(this.getPoint2D(new Point2D.Float(0.0f, 0.0f), null));
        r.add(this.getPoint2D(new Point2D.Float(w, 0.0f), null));
        r.add(this.getPoint2D(new Point2D.Float(0.0f, h), null));
        r.add(this.getPoint2D(new Point2D.Float(w, h), null));
        return r;
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Float();
        }
        float x = (float)srcPt.getX();
        float y = (float)srcPt.getY();
        float f = 1.0f / (x * this.a13 + y * this.a23 + this.a33);
        dstPt.setLocation((x * this.a11 + y * this.a21 + this.a31) * f, (x * this.a12 + y * this.a22 + this.a32) * f);
        return dstPt;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (float)this.originalSpace.width * (this.A * (float)x + this.B * (float)y + this.C) / (this.G * (float)x + this.H * (float)y + this.I);
        out[1] = (float)this.originalSpace.height * (this.D * (float)x + this.E * (float)y + this.F) / (this.G * (float)x + this.H * (float)y + this.I);
    }

    public String toString() {
        return "Distort/Perspective...";
    }
}

