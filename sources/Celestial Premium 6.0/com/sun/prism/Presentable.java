/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.PresentableState;
import com.sun.prism.RenderTarget;

public interface Presentable
extends RenderTarget {
    public boolean lockResources(PresentableState var1);

    public boolean prepare(Rectangle var1);

    public boolean present();

    public float getPixelScaleFactorX();

    public float getPixelScaleFactorY();
}

