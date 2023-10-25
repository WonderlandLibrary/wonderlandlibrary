/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.sg.prism.NGParallelCamera;

public class NGDefaultCamera
extends NGParallelCamera {
    public void validate(int n, int n2) {
        if ((double)n != this.viewWidth || (double)n2 != this.viewHeight) {
            this.setViewWidth(n);
            this.setViewHeight(n2);
            double d = n > n2 ? (double)n / 2.0 : (double)n2 / 2.0;
            this.projViewTx.ortho(0.0, n, n2, 0.0, -d, d);
        }
    }
}

