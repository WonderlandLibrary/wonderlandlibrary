/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.DPathConsumer2D;
import com.sun.marlin.MarlinProperties;

public final class PathSimplifier
implements DPathConsumer2D {
    private static final double PIX_THRESHOLD = MarlinProperties.getPathSimplifierPixelTolerance();
    private static final double SQUARE_TOLERANCE = PIX_THRESHOLD * PIX_THRESHOLD;
    private DPathConsumer2D delegate;
    private double cx;
    private double cy;
    private boolean skipped;
    private double sx;
    private double sy;

    PathSimplifier() {
    }

    public PathSimplifier init(DPathConsumer2D dPathConsumer2D) {
        this.delegate = dPathConsumer2D;
        this.skipped = false;
        return this;
    }

    private void finishPath() {
        if (this.skipped) {
            this._lineTo(this.sx, this.sy);
        }
    }

    @Override
    public void pathDone() {
        this.finishPath();
        this.delegate.pathDone();
    }

    @Override
    public void closePath() {
        this.finishPath();
        this.delegate.closePath();
    }

    @Override
    public void moveTo(double d, double d2) {
        this.finishPath();
        this.delegate.moveTo(d, d2);
        this.cx = d;
        this.cy = d2;
    }

    @Override
    public void lineTo(double d, double d2) {
        double d3 = d - this.cx;
        double d4 = d2 - this.cy;
        if (d3 * d3 + d4 * d4 <= SQUARE_TOLERANCE) {
            this.skipped = true;
            this.sx = d;
            this.sy = d2;
            return;
        }
        this._lineTo(d, d2);
    }

    private void _lineTo(double d, double d2) {
        this.delegate.lineTo(d, d2);
        this.cx = d;
        this.cy = d2;
        this.skipped = false;
    }

    @Override
    public void quadTo(double d, double d2, double d3, double d4) {
        double d5 = d3 - this.cx;
        double d6 = d4 - this.cy;
        if (d5 * d5 + d6 * d6 <= SQUARE_TOLERANCE && (d5 = d - this.cx) * d5 + (d6 = d2 - this.cy) * d6 <= SQUARE_TOLERANCE) {
            this.skipped = true;
            this.sx = d3;
            this.sy = d4;
            return;
        }
        this.delegate.quadTo(d, d2, d3, d4);
        this.cx = d3;
        this.cy = d4;
        this.skipped = false;
    }

    @Override
    public void curveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d5 - this.cx;
        double d8 = d6 - this.cy;
        if (d7 * d7 + d8 * d8 <= SQUARE_TOLERANCE && (d7 = d - this.cx) * d7 + (d8 = d2 - this.cy) * d8 <= SQUARE_TOLERANCE && (d7 = d3 - this.cx) * d7 + (d8 = d4 - this.cy) * d8 <= SQUARE_TOLERANCE) {
            this.skipped = true;
            this.sx = d5;
            this.sy = d6;
            return;
        }
        this.delegate.curveTo(d, d2, d3, d4, d5, d6);
        this.cx = d5;
        this.cy = d6;
        this.skipped = false;
    }
}

