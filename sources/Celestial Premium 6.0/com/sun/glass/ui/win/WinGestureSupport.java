/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.GestureSupport;
import com.sun.glass.ui.TouchInputSupport;
import com.sun.glass.ui.View;

final class WinGestureSupport {
    private static final double multiplier = 1.0;
    private static final GestureSupport gestures;
    private static final TouchInputSupport touches;
    private static int modifiers;
    private static boolean isDirect;

    WinGestureSupport() {
    }

    private static native void _initIDs();

    public static void notifyBeginTouchEvent(View view, int n, boolean bl, int n2) {
        touches.notifyBeginTouchEvent(view, n, bl, n2);
    }

    public static void notifyNextTouchEvent(View view, int n, long l, int n2, int n3, int n4, int n5) {
        touches.notifyNextTouchEvent(view, n, l, n2, n3, n4, n5);
    }

    public static void notifyEndTouchEvent(View view) {
        touches.notifyEndTouchEvent(view);
        WinGestureSupport.gestureFinished(view, touches.getTouchCount(), false);
    }

    private static void gestureFinished(View view, int n, boolean bl) {
        if (gestures.isScrolling() && n == 0) {
            gestures.handleScrollingEnd(view, modifiers, n, isDirect, bl, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        if (gestures.isRotating() && n < 2) {
            gestures.handleRotationEnd(view, modifiers, isDirect, bl, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        if (gestures.isZooming() && n < 2) {
            gestures.handleZoomingEnd(view, modifiers, isDirect, bl, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
    }

    public static void inertiaGestureFinished(View view) {
        WinGestureSupport.gestureFinished(view, 0, true);
    }

    public static void gesturePerformed(View view, int n, boolean bl, boolean bl2, int n2, int n3, int n4, int n5, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        modifiers = n;
        isDirect = bl;
        int n6 = touches.getTouchCount();
        if (n6 >= 2) {
            gestures.handleTotalZooming(view, n, bl, bl2, n2, n3, n4, n5, f5, f6);
            gestures.handleTotalRotation(view, n, bl, bl2, n2, n3, n4, n5, Math.toDegrees(f7));
        }
        gestures.handleTotalScrolling(view, n, bl, bl2, n6, n2, n3, n4, n5, f3, f4, 1.0, 1.0);
    }

    static {
        WinGestureSupport._initIDs();
        gestures = new GestureSupport(true);
        touches = new TouchInputSupport(gestures.createTouchCountListener(), true);
    }
}

