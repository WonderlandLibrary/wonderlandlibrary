/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;

public final class GestureSupport {
    private static final double THRESHOLD_SCROLL = 1.0;
    private static final double THRESHOLD_SCALE = 0.01;
    private static final double THRESHOLD_EXPANSION = 0.01;
    private static final double THRESHOLD_ROTATE = Math.toDegrees(Math.PI / 180);
    private final GestureState scrolling = new GestureState();
    private final GestureState rotating = new GestureState();
    private final GestureState zooming = new GestureState();
    private final GestureState swiping = new GestureState();
    private double totalScrollX = Double.NaN;
    private double totalScrollY = Double.NaN;
    private double totalScale = 1.0;
    private double totalExpansion = Double.NaN;
    private double totalRotation = 0.0;
    private double multiplierX = 1.0;
    private double multiplierY = 1.0;
    private boolean zoomWithExpansion;

    public GestureSupport(boolean bl) {
        this.zoomWithExpansion = bl;
    }

    private static double multiplicativeDelta(double d, double d2) {
        if (d == 0.0) {
            return Double.NaN;
        }
        return d2 / d;
    }

    private int setScrolling(boolean bl) {
        return this.scrolling.updateProgress(bl);
    }

    private int setRotating(boolean bl) {
        return this.rotating.updateProgress(bl);
    }

    private int setZooming(boolean bl) {
        return this.zooming.updateProgress(bl);
    }

    private int setSwiping(boolean bl) {
        return this.swiping.updateProgress(bl);
    }

    public boolean isScrolling() {
        return !this.scrolling.isIdle();
    }

    public boolean isRotating() {
        return !this.rotating.isIdle();
    }

    public boolean isZooming() {
        return !this.zooming.isIdle();
    }

    public boolean isSwiping() {
        return !this.swiping.isIdle();
    }

    public void handleScrollingEnd(View view, int n, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6) {
        this.scrolling.setIdle();
        if (bl2) {
            return;
        }
        view.notifyScrollGestureEvent(3, n, bl, bl2, n2, n3, n4, n5, n6, 0.0, 0.0, this.totalScrollX, this.totalScrollY, this.multiplierX, this.multiplierY);
    }

    public void handleRotationEnd(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5) {
        this.rotating.setIdle();
        if (bl2) {
            return;
        }
        view.notifyRotateGestureEvent(3, n, bl, bl2, n2, n3, n4, n5, 0.0, this.totalRotation);
    }

    public void handleZoomingEnd(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5) {
        this.zooming.setIdle();
        if (bl2) {
            return;
        }
        view.notifyZoomGestureEvent(3, n, bl, bl2, n2, n3, n4, n5, Double.NaN, 0.0, this.totalScale, this.totalExpansion);
    }

    public void handleSwipeEnd(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5) {
        this.swiping.setIdle();
        if (bl2) {
            return;
        }
        view.notifySwipeGestureEvent(3, n, bl, bl2, Integer.MAX_VALUE, Integer.MAX_VALUE, n2, n3, n4, n5);
    }

    public void handleTotalZooming(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, double d, double d2) {
        double d3 = this.totalScale;
        double d4 = this.totalExpansion;
        if (this.zooming.doesGestureStart(bl2)) {
            d3 = 1.0;
            d4 = 0.0;
        }
        if (Math.abs(d - d3) < 0.01 && (!this.zoomWithExpansion || Math.abs(d2 - d4) < 0.01)) {
            return;
        }
        double d5 = Double.NaN;
        if (this.zoomWithExpansion) {
            d5 = d2 - d4;
        } else {
            d2 = Double.NaN;
        }
        this.totalScale = d;
        this.totalExpansion = d2;
        int n6 = this.setZooming(bl2);
        view.notifyZoomGestureEvent(n6, n, bl, bl2, n2, n3, n4, n5, GestureSupport.multiplicativeDelta(d3, this.totalScale), d5, d, d2);
    }

    public void handleTotalRotation(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, double d) {
        double d2 = this.totalRotation;
        if (this.rotating.doesGestureStart(bl2)) {
            d2 = 0.0;
        }
        if (Math.abs(d - d2) < THRESHOLD_ROTATE) {
            return;
        }
        this.totalRotation = d;
        int n6 = this.setRotating(bl2);
        view.notifyRotateGestureEvent(n6, n, bl, bl2, n2, n3, n4, n5, d - d2, d);
    }

    public void handleTotalScrolling(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, int n6, double d, double d2, double d3, double d4) {
        this.multiplierX = d3;
        this.multiplierY = d4;
        double d5 = this.totalScrollX;
        double d6 = this.totalScrollY;
        if (this.scrolling.doesGestureStart(bl2)) {
            d5 = 0.0;
            d6 = 0.0;
        }
        if (Math.abs(d - this.totalScrollX) < 1.0 && Math.abs(d2 - this.totalScrollY) < 1.0) {
            return;
        }
        this.totalScrollX = d;
        this.totalScrollY = d2;
        int n7 = this.setScrolling(bl2);
        view.notifyScrollGestureEvent(n7, n, bl, bl2, n2, n3, n4, n5, n6, d - d5, d2 - d6, d, d2, d3, d4);
    }

    public void handleDeltaZooming(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, double d, double d2) {
        double d3 = this.totalScale;
        double d4 = this.totalExpansion;
        if (this.zooming.doesGestureStart(bl2)) {
            d3 = 1.0;
            d4 = 0.0;
        }
        this.totalScale = d3 * (1.0 + d);
        this.totalExpansion = this.zoomWithExpansion ? d4 + d2 : Double.NaN;
        int n6 = this.setZooming(bl2);
        view.notifyZoomGestureEvent(n6, n, bl, bl2, n2, n3, n4, n5, GestureSupport.multiplicativeDelta(d3, this.totalScale), d2, this.totalScale, this.totalExpansion);
    }

    public void handleDeltaRotation(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, double d) {
        double d2 = this.totalRotation;
        if (this.rotating.doesGestureStart(bl2)) {
            d2 = 0.0;
        }
        this.totalRotation = d2 + d;
        int n6 = this.setRotating(bl2);
        view.notifyRotateGestureEvent(n6, n, bl, bl2, n2, n3, n4, n5, d, this.totalRotation);
    }

    public void handleDeltaScrolling(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, int n6, double d, double d2, double d3, double d4) {
        this.multiplierX = d3;
        this.multiplierY = d4;
        double d5 = this.totalScrollX;
        double d6 = this.totalScrollY;
        if (this.scrolling.doesGestureStart(bl2)) {
            d5 = 0.0;
            d6 = 0.0;
        }
        this.totalScrollX = d5 + d;
        this.totalScrollY = d6 + d2;
        int n7 = this.setScrolling(bl2);
        view.notifyScrollGestureEvent(n7, n, bl, bl2, n2, n3, n4, n5, n6, d, d2, this.totalScrollX, this.totalScrollY, d3, d4);
    }

    public void handleSwipe(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = this.setSwiping(bl2);
        view.notifySwipeGestureEvent(n8, n, bl, bl2, n2, n3, n4, n5, n6, n7);
    }

    public static void handleSwipePerformed(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, int n6, int n7) {
        view.notifySwipeGestureEvent(2, n, bl, bl2, n2, n3, n4, n5, n6, n7);
    }

    public static void handleScrollingPerformed(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, int n6, double d, double d2, double d3, double d4) {
        view.notifyScrollGestureEvent(2, n, bl, bl2, n2, n3, n4, n5, n6, d, d2, d, d2, d3, d4);
    }

    public TouchInputSupport.TouchCountListener createTouchCountListener() {
        Application.checkEventThread();
        return (touchInputSupport, view, n, bl) -> {
            boolean bl2 = false;
            if (this.isScrolling()) {
                this.handleScrollingEnd(view, n, touchInputSupport.getTouchCount(), bl, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            if (this.isRotating()) {
                this.handleRotationEnd(view, n, bl, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            if (this.isZooming()) {
                this.handleZoomingEnd(view, n, bl, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        };
    }

    private static class GestureState {
        private StateId id = StateId.Idle;

        private GestureState() {
        }

        void setIdle() {
            this.id = StateId.Idle;
        }

        boolean isIdle() {
            return this.id == StateId.Idle;
        }

        int updateProgress(boolean bl) {
            int n = 2;
            if (this.doesGestureStart(bl) && !bl) {
                n = 1;
            }
            this.id = bl ? StateId.Inertia : StateId.Running;
            return n;
        }

        boolean doesGestureStart(boolean bl) {
            switch (this.id) {
                case Running: {
                    return bl;
                }
                case Inertia: {
                    return !bl;
                }
            }
            return true;
        }

        static final class StateId
        extends Enum<StateId> {
            public static final /* enum */ StateId Idle = new StateId();
            public static final /* enum */ StateId Running = new StateId();
            public static final /* enum */ StateId Inertia = new StateId();
            private static final /* synthetic */ StateId[] $VALUES;

            public static StateId[] values() {
                return (StateId[])$VALUES.clone();
            }

            public static StateId valueOf(String string) {
                return Enum.valueOf(StateId.class, string);
            }

            private static /* synthetic */ StateId[] $values() {
                return new StateId[]{Idle, Running, Inertia};
            }

            static {
                $VALUES = StateId.$values();
            }
        }
    }
}

