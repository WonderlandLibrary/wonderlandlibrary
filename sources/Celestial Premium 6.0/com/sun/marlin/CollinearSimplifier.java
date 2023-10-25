/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.DPathConsumer2D;

public final class CollinearSimplifier
implements DPathConsumer2D {
    static final double EPS = 1.0E-4;
    DPathConsumer2D delegate;
    SimplifierState state;
    double px1;
    double py1;
    double px2;
    double py2;
    double pslope;

    CollinearSimplifier() {
    }

    public CollinearSimplifier init(DPathConsumer2D dPathConsumer2D) {
        this.delegate = dPathConsumer2D;
        this.state = SimplifierState.Empty;
        return this;
    }

    @Override
    public void pathDone() {
        this.emitStashedLine();
        this.state = SimplifierState.Empty;
        this.delegate.pathDone();
    }

    @Override
    public void closePath() {
        this.emitStashedLine();
        this.state = SimplifierState.Empty;
        this.delegate.closePath();
    }

    @Override
    public void quadTo(double d, double d2, double d3, double d4) {
        this.emitStashedLine();
        this.delegate.quadTo(d, d2, d3, d4);
        this.state = SimplifierState.PreviousPoint;
        this.px1 = d3;
        this.py1 = d4;
    }

    @Override
    public void curveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        this.emitStashedLine();
        this.delegate.curveTo(d, d2, d3, d4, d5, d6);
        this.state = SimplifierState.PreviousPoint;
        this.px1 = d5;
        this.py1 = d6;
    }

    @Override
    public void moveTo(double d, double d2) {
        this.emitStashedLine();
        this.delegate.moveTo(d, d2);
        this.state = SimplifierState.PreviousPoint;
        this.px1 = d;
        this.py1 = d2;
    }

    @Override
    public void lineTo(double d, double d2) {
        switch (this.state) {
            case Empty: {
                this.delegate.lineTo(d, d2);
                this.state = SimplifierState.PreviousPoint;
                this.px1 = d;
                this.py1 = d2;
                return;
            }
            case PreviousPoint: {
                this.state = SimplifierState.PreviousLine;
                this.px2 = d;
                this.py2 = d2;
                this.pslope = CollinearSimplifier.getSlope(this.px1, this.py1, d, d2);
                return;
            }
            case PreviousLine: {
                double d3 = CollinearSimplifier.getSlope(this.px2, this.py2, d, d2);
                if (d3 == this.pslope || Math.abs(this.pslope - d3) < 1.0E-4) {
                    this.px2 = d;
                    this.py2 = d2;
                    return;
                }
                this.delegate.lineTo(this.px2, this.py2);
                this.px1 = this.px2;
                this.py1 = this.py2;
                this.px2 = d;
                this.py2 = d2;
                this.pslope = d3;
                return;
            }
        }
    }

    private void emitStashedLine() {
        if (this.state == SimplifierState.PreviousLine) {
            this.delegate.lineTo(this.px2, this.py2);
        }
    }

    private static double getSlope(double d, double d2, double d3, double d4) {
        double d5 = d4 - d2;
        if (d5 == 0.0) {
            return d3 > d ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return (d3 - d) / d5;
    }

    static final class SimplifierState
    extends Enum<SimplifierState> {
        public static final /* enum */ SimplifierState Empty = new SimplifierState();
        public static final /* enum */ SimplifierState PreviousPoint = new SimplifierState();
        public static final /* enum */ SimplifierState PreviousLine = new SimplifierState();
        private static final /* synthetic */ SimplifierState[] $VALUES;

        public static SimplifierState[] values() {
            return (SimplifierState[])$VALUES.clone();
        }

        public static SimplifierState valueOf(String string) {
            return Enum.valueOf(SimplifierState.class, string);
        }

        private static /* synthetic */ SimplifierState[] $values() {
            return new SimplifierState[]{Empty, PreviousPoint, PreviousLine};
        }

        static {
            $VALUES = SimplifierState.$values();
        }
    }
}

