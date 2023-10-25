/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.TouchPoint
 */
package com.sun.javafx.scene.input;

import com.sun.javafx.util.Utils;
import javafx.scene.input.TouchPoint;

public class TouchPointHelper {
    private static TouchPointAccessor touchPointAccessor;

    private TouchPointHelper() {
    }

    public static void reset(TouchPoint touchPoint) {
        touchPointAccessor.reset(touchPoint);
    }

    public static void setTouchPointAccessor(TouchPointAccessor touchPointAccessor) {
        if (TouchPointHelper.touchPointAccessor != null) {
            throw new IllegalStateException();
        }
        TouchPointHelper.touchPointAccessor = touchPointAccessor;
    }

    static {
        Utils.forceInit(TouchPoint.class);
    }

    public static interface TouchPointAccessor {
        public void reset(TouchPoint var1);
    }
}

