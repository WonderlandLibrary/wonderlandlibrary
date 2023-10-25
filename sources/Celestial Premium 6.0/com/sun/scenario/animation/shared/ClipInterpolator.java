/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyFrame
 *  javafx.util.Duration
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.GeneralClipInterpolator;
import com.sun.scenario.animation.shared.SimpleClipInterpolator;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public abstract class ClipInterpolator {
    static ClipInterpolator create(KeyFrame[] arrkeyFrame, long[] arrl) {
        return ClipInterpolator.getRealKeyFrameCount(arrkeyFrame) == 2 ? (arrkeyFrame.length == 1 ? new SimpleClipInterpolator(arrkeyFrame[0], arrl[0]) : new SimpleClipInterpolator(arrkeyFrame[0], arrkeyFrame[1], arrl[1])) : new GeneralClipInterpolator(arrkeyFrame, arrl);
    }

    static int getRealKeyFrameCount(KeyFrame[] arrkeyFrame) {
        int n = arrkeyFrame.length;
        return n == 0 ? 0 : (arrkeyFrame[0].getTime().greaterThan(Duration.ZERO) ? n + 1 : n);
    }

    abstract ClipInterpolator setKeyFrames(KeyFrame[] var1, long[] var2);

    abstract void interpolate(long var1);

    abstract void validate(boolean var1);
}

